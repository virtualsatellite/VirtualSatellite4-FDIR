/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.experiments;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.junit.Before;

import de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.MarkovAutomatonBuildStatistics;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.util.IStatistics;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.galileo.GalileoDFT2DFT;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.DFTEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer.MinimizationStatistics;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ISynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ModularSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisQuery;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisStatistics;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeStatistics;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHelper;

/**
 * This class provides a generic setup for making experiments based on the RA synthesizer.
 * @author muel_s8
 *
 */

public class ASynthesizerExperiment {
	private static final String PLUGIN_ID = "de.dlr.sc.virsat.model.extension.fdir";
	private static final String FRAGMENT_ID = PLUGIN_ID + ".experiments";
	private static final long DEFAULT_BENCHMARK_TIMEOUT_SECONDS = 30;
	private static final long BENCHMARK_SLEEP = 1000;
	
	protected ModularSynthesizer synthesizer;
	
	protected Concept concept;
	protected FaultTreeBuilder ftBuilder;
	protected RecoveryAutomatonHelper raHelper;
	protected long timeoutSeconds = DEFAULT_BENCHMARK_TIMEOUT_SECONDS;
	protected Map<String, SynthesisStatistics> mapBenchmarkToSynthesisStatistics = new TreeMap<>();
	protected Map<String, FaultTreeStatistics> mapBenchmarkToFaultTreeStatistics = new TreeMap<>();
	
	protected int countSolved;
	protected int countTimeout;
	protected int countOutOfMemory;
	protected int countTotal;
	protected long totalSolveTime;
	
	@Before
	public void setUp() {
		concept = ConceptXmiLoader.loadConceptFromPlugin(PLUGIN_ID + "/concept/concept.xmi");
		this.ftBuilder = new FaultTreeBuilder(concept);
		this.raHelper = new RecoveryAutomatonHelper(concept);
		this.synthesizer = new ModularSynthesizer();
	}
	
	/**
	 * Creates a GALILEO DFT from a locally available resource file.
	 * @param resourcePath the path to the .dft file
	 * @return the root of the DFT
	 * @throws IOException thrown if reading the .dft file fails
	 */
	protected Fault createDFT(String resourcePath) throws IOException {
		URL url = new URL("platform:/plugin/" + FRAGMENT_ID + resourcePath);
		InputStream inputStream = url.openConnection().getInputStream();
		GalileoDFT2DFT converter = new GalileoDFT2DFT(concept, inputStream);
		return converter.convert();
	}
	
	/**
	 * Prints various relevant statistics regarding the state and transition count
	 * for a given fault tree evaluation
	 * @param ftEvaluator the fault tree evaluator
	 */
	protected void printStateStatistics(FaultTreeEvaluator ftEvaluator) {
		if (ftEvaluator.getEvaluator() instanceof DFTEvaluator) {
			DFTEvaluator dftEvaluator = (DFTEvaluator) ftEvaluator.getEvaluator();
			System.out.println("MC #States: " + dftEvaluator.getStatistics().maBuildStatistics.maxStates);
			System.out.println("MC #Transitions: " + dftEvaluator.getStatistics().maBuildStatistics.maxTransitions);
		}
	}
	
	/**
	 * Tests all of the .dft benchmarks in the given folder
	 * @param suite the file with all the dft names
	 * @param filePath the path to the file
	 * @param saveFileName the name of the file you wish to save the results to
	 * @param synthesizerSupplier a supplier for the synthesizer
	 * @throws IOException exception
	 */
	protected void benchmark(File suite, String filePath, String saveFileName, Supplier<ISynthesizer> synthesizerSupplier) throws IOException {	
		System.out.println("Creating benchmark data " + saveFileName + ".");
		
		List<String> fileNames = Files.readAllLines(suite.toPath());
		File parentFolder = suite.getParentFile();
		for (String fileName : fileNames) {
			Path path = Paths.get(parentFolder.toString(), fileName);
			File suiteEntry = path.toFile();
			benchmarkSuiteEntry(suiteEntry, synthesizerSupplier);
		}
		
		saveStatistics(saveFileName);
		
		System.out.println("Finished benchmark data " + saveFileName + ".");
	}
	
	/**
	 * Benchmarks a single entry in a benchmark suite.
	 * If the entry is a folder, recuresively calls itself.
	 * @param entry the suite entry
	 * @param synthesizerSupplier a supplier for the synthesizer
	 * @throws IOException exception
	 */
	protected void benchmarkSuiteEntry(File entry, Supplier<ISynthesizer> synthesizerSupplier) throws IOException {
		if (entry.isDirectory()) {
			File[] files = entry.listFiles();
			for (File file : files) {
				benchmarkSuiteEntry(file, synthesizerSupplier);
			}
		} else {
			boolean runBenchmark = true;
			while (runBenchmark) {
				try {
					Path platformPath = Paths.get(".\\").relativize(entry.toPath());
					benchmarkDFT("/" + platformPath.toString(), entry.getName(), synthesizerSupplier);
					runBenchmark = false;
				} catch (OutOfMemoryError e) {
					System.out.println("Got OOM during benchmark setup! Cleaning and then trying again.");
				}
				
				// Try to bring us into a clean state by triggering the garbage collection
				// and discarding dead objects. Note that this is only a best effort cleaning.
				clean();
			}
		}
	}
	
	private static final int TIMEOUT_FACTOR = 2;
	private static final int OUT_OF_MEMROY_FACTORY = 3;
	
	/**
	 * Benchmarks a single DFT and saves the statistics
	 * @param dftPath the path to the dft
	 * @param benchmarkName the name of the benchmark
	 * @param synthesizerSupplier a supplier for the synthesizer
	 * @throws IOException
	 */
	protected void benchmarkDFT(String dftPath, String benchmarkName, Supplier<ISynthesizer> synthesizerSupplier) throws IOException {
		System.out.print("Benchmarking " + benchmarkName + "... ");
		
		// Prepare the necessary data and helper classes for the benchmark
		Duration timeout = Duration.ofSeconds(timeoutSeconds);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Fault fault = createDFT(dftPath);
		SynthesisQuery query = new SynthesisQuery(fault);
		
		// Using the supplier we generate a fresh new synthesizer to avoid having
		// previous data influence our measurements
		ISynthesizer benchmarkSynthesizer = synthesizerSupplier.get();
		
		// Create a monitor so we can properly cancel the synthesis call
		SubMonitor monitor = SubMonitor.convert(new NullProgressMonitor());
		
		// Execute the benchmark and handle timeout and out of memory events
		final Future<?> handler = executor.submit(() -> {
			benchmarkSynthesizer.synthesize(query, monitor);
		});
		
		try {
		    handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
		    System.out.println("DONE.");
		} catch (ExecutionException e) {
			benchmarkSynthesizer.getStatistics().time = IStatistics.OOM;
			System.out.println("OUT OF MEMORY.");
		} catch (TimeoutException | InterruptedException e) {
		    handler.cancel(true);
		    monitor.setCanceled(true);
		    System.out.println("TIMEOUT.");
		}
		
		// Terminate the possibily still ongoing benchmark.
		// Since termination is a cooperative process, we may need to wait some time
		// until the benchmarked synthesizer hits a point where it checks for cancellation
		executor.shutdownNow();
		while (!executor.isTerminated()) {
			try {
				executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS);
				if (!executor.isTerminated()) {
					System.out.println("Previous benchmark has not succesfully terminated yet... awaiting termination... ");
				} 
			} catch (InterruptedException e) {
				// Nothing to handle here
			}
		}
		
		// Memorize the results for later processing
		// Also mark the timeout and out of memory with special benchmark times depending on the benchmark timeout
		// This way we can later properly display the data in graphs
		
		countTotal++;
		if (benchmarkSynthesizer.getStatistics().time == IStatistics.OOM) {
			countOutOfMemory++;
			benchmarkSynthesizer.getStatistics().time = OUT_OF_MEMROY_FACTORY * TimeUnit.MILLISECONDS.convert(timeoutSeconds, TimeUnit.SECONDS);
		} else if (benchmarkSynthesizer.getStatistics().time == IStatistics.TIMEOUT) {
			countTimeout++;
			benchmarkSynthesizer.getStatistics().time = TIMEOUT_FACTOR * TimeUnit.MILLISECONDS.convert(timeoutSeconds, TimeUnit.SECONDS);
		} else {
			countSolved++;
			totalSolveTime += benchmarkSynthesizer.getStatistics().time;
		}
		
		mapBenchmarkToSynthesisStatistics.put(benchmarkName, benchmarkSynthesizer.getStatistics());
		mapBenchmarkToFaultTreeStatistics.put(benchmarkName, query.getFTHolder().getStatistics());
	}
	
	/**
	 * Sleep some to give the gc time to trigger, especially
 	 * if a out of memory exception just a occurred
	 */
	private void clean() {
		System.gc();
		System.runFinalization();
		
		try {
			Thread.sleep(BENCHMARK_SLEEP);
		} catch (InterruptedException e) {
			// Nothing to handle here
		}
	}
	
	/**
	 * Prints the evaluation results of a fault tree evaluator
	 * @param result the results
	 * @param evaluator the evaluator
	 * @param delta the timestep
	 */
	protected void printResults(FaultTreeEvaluator evaluator, ModelCheckingResult result, float delta) {
		System.out.println("MTTF: " + result.getMeanTimeToFailure());
		printStateStatistics(evaluator);
		
		for (int i = 0; i < result.getFailRates().size(); ++i) {
			float time = i * delta;
			double reliability = 1 - result.getFailRates().get(i);
			System.out.println(time + " " + reliability);
		}
	}
	
	/**
	 * Saves an ra in the results folder
	 * @param ra the recovery automaton to save
	 * @param filePath the path
	 * @throws IOException throws IO Exception if saving failed
	 */
	protected void saveRA(RecoveryAutomaton ra, String filePath) throws IOException {
		Path path = Paths.get("resources/results/" + filePath + ".dot");
		Files.createFile(path);
		
		try (OutputStream outFile = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE); 
			PrintStream writer = new PrintStream(outFile)) {
			writer.println(ra.toDot());
		}
	}
	
	/**
	 * Write synthesis statistic to files
	 * @param filePathWithPrefix the path with prefix
	 * @param suffix the statistics suffix
	 * @param columns the columns
	 * @param getValues the values getter
	 * @throws IOException exception
	 */
	protected void saveStatistics(String filePathWithPrefix, String suffix, List<String> columns, Function<SynthesisStatistics, List<String>> getValues) throws IOException {
		Path pathSynthesizer = Paths.get(filePathWithPrefix + "-" + suffix + ".txt");
		try (OutputStream outFile = Files.newOutputStream(pathSynthesizer, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			PrintStream writer = new PrintStream(outFile)) {
			
			String header = "benchmarkName " + String.join(" ", columns);
			writer.println(header);
			
			for (Entry<String, SynthesisStatistics> entry : mapBenchmarkToSynthesisStatistics.entrySet()) {
				String record = entry.getKey() + " " + String.join(" ", getValues.apply(entry.getValue()));
				writer.println(record);
			}
		}
	}
	
	/**
	 * Write statistic to files
	 * @param filePath the path
	 * @throws IOException exception
	 */
	protected void saveStatistics(String filePath) throws IOException {
		String filePathWithPrefix = "resources/results/" + filePath;
		Path pathSummary = Paths.get(filePathWithPrefix + "-summary.txt");
		Files.createDirectories(pathSummary.getParent());
		
		try (OutputStream outFile = Files.newOutputStream(pathSummary, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			PrintStream writer = new PrintStream(outFile)) {
			writer.println("solved timeouts ooms solveTime");
			writer.print(countSolved + "/" + countTotal + " ");
			writer.print(countTimeout + " ");
			writer.print(countOutOfMemory + " ");
			writer.print(TimeUnit.SECONDS.convert(totalSolveTime, TimeUnit.MILLISECONDS));
		}
		
		Path pathFaultTree = Paths.get(filePathWithPrefix + "-fault-tree.txt");
		try (OutputStream outFile = Files.newOutputStream(pathFaultTree, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			PrintStream writer = new PrintStream(outFile)) {
			
			String header = "benchmarkName " + String.join(" ", FaultTreeStatistics.getColumns());
			writer.println(header);
			
			for (Entry<String, FaultTreeStatistics> entry : mapBenchmarkToFaultTreeStatistics.entrySet()) {
				String record = entry.getKey() + " " + String.join(" ", entry.getValue().getValues());
				writer.println(record);
			}
		}
		
		saveStatistics(filePathWithPrefix, "synthesizer", SynthesisStatistics.getColumns(), SynthesisStatistics::getValues);
		saveStatistics(filePathWithPrefix, "ma-builder", MarkovAutomatonBuildStatistics.getColumns(), statistics -> statistics.maBuildStatistics.getValues());
		saveStatistics(filePathWithPrefix, "ra-minimizer", MinimizationStatistics.getColumns(), statistics -> statistics.minimizationStatistics.getValues());
	}
}
