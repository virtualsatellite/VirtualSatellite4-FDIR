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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.junit.Before;

import de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.MarkovAutomatonBuildStatistics;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
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
	
	protected ModularSynthesizer synthesizer;
	
	protected Concept concept;
	protected FaultTreeBuilder ftBuilder;
	protected RecoveryAutomatonHelper raHelper;
	protected long timeoutSeconds = DEFAULT_BENCHMARK_TIMEOUT_SECONDS;
	protected Map<String, SynthesisStatistics> mapBenchmarkToStatistics = new TreeMap<>();
	
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
	 * @param folder the folder
	 * @param folderPath the path to the folder
	 * @throws IOException exception
	 */
	protected void benchmarkFolder(File folder, String folderPath) throws IOException {
		if (folder.isDirectory()) {
			System.out.println("Not a directory: " + folder.getAbsolutePath());
		}
		
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					benchmarkFolder(file, folderPath + "/" + file.getName());
				} else {
					benchmarkDFT(folderPath + "/" + file.getName(), file.getName());
				}
			}
		}
	}
	
	/**
	 * Tests all of the .dft benchmarks in the given folder
	 * @param suite the file with all the dft names
	 * @param filePath the path to the file
	 * @param saveFileName the name of the file you wish to save the results to
	 * @param synthesizer the synthesizer
	 * @throws IOException exception
	 */
	protected void benchmark(File suite, String filePath, String saveFileName, ISynthesizer synthesizer) throws IOException {	
		System.out.println("Creating benchmark data " + saveFileName + ".");
		
		String entireFile = new String(Files.readAllBytes(suite.toPath()));
		
		for (String filename : entireFile.split("\\r?\\n")) {
			File parentFolder = suite.getParentFile();
			
			File[] childFolders = parentFolder.listFiles();
			if (childFolders == null) {
				continue;
			}
			
			for (File childFolder : childFolders) {
				if (childFolder.isDirectory()) {
					File[] matchingFiles = childFolder.listFiles((File dir, String name) ->
						name.equals(filename)
					);
				
					if (matchingFiles.length != 0) {
						File benchmarkFile = matchingFiles[0];
						String dftPath = filePath + "/" + childFolder.getName() + "/" + benchmarkFile.getName();
						benchmarkDFT(dftPath, benchmarkFile.getName());
					}
				}
			}
		}
		
		saveStatistics(saveFileName);
		
		System.out.println("Finished benchmark data " + saveFileName + ".");
	}
	
	/**
	 * Benchmarks a single DFT and saves the statistics
	 * @param dftPath the path to the dft
	 * @param benchmarkName the name of the benchmark
	 * @throws IOException
	 */
	protected void benchmarkDFT(String dftPath, String benchmarkName) throws IOException {
		System.out.print("Benchmarking " + benchmarkName + "... ");
		
		Duration timeout = Duration.ofSeconds(timeoutSeconds);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Fault fault = createDFT(dftPath);
		SynthesisQuery query = new SynthesisQuery(fault);
		
		// Cretae a monitor so we can properly cancel the synthesis call
		SubMonitor monitor = SubMonitor.convert(new NullProgressMonitor());
		
		final Future<?> handler = executor.submit(() -> {
			synthesizer.synthesize(query, monitor);
		});

		try {
		    handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
		    System.out.println("done.");
		} catch (TimeoutException | InterruptedException | ExecutionException e) {
		    handler.cancel(true);
		    monitor.setCanceled(true);
		    System.out.println("TIMEOUT.");
		}
		
		executor.shutdownNow();
		
		mapBenchmarkToStatistics.put(benchmarkName, synthesizer.getStatistics());
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
			for (Entry<String, SynthesisStatistics> entry : mapBenchmarkToStatistics.entrySet()) {
				writer.println(entry.getKey());
				writer.println("===============================================");
				writer.println(entry.getValue());
				writer.println();
			}
		}
		
		Path pathSynthesizer = Paths.get(filePathWithPrefix + "-synthesizer.txt");
		try (OutputStream outFile = Files.newOutputStream(pathSynthesizer, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			PrintStream writer = new PrintStream(outFile)) {
			
			String header = "benchmarkName," + String.join(",", SynthesisStatistics.getColumns());
			writer.println(header);
			
			for (Entry<String, SynthesisStatistics> entry : mapBenchmarkToStatistics.entrySet()) {
				String record = entry.getKey() + "," + String.join(",", entry.getValue().getValues());
				writer.println(record);
			}
		}
		
		Path pathMaBuilder = Paths.get(filePathWithPrefix + "-ma-builder.txt");
		try (OutputStream outFile = Files.newOutputStream(pathMaBuilder, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			PrintStream writer = new PrintStream(outFile)) {
			
			String header = "benchmarkName," + String.join(",", MarkovAutomatonBuildStatistics.getColumns());
			writer.println(header);
			
			for (Entry<String, SynthesisStatistics> entry : mapBenchmarkToStatistics.entrySet()) {
				String record = entry.getKey() + "," + String.join(",", entry.getValue().maBuildStatistics.getValues());
				writer.println(record);
			}
		}
		
		Path pathRaMinimizer = Paths.get(filePathWithPrefix + "-ra-minimizer.txt");
		try (OutputStream outFile = Files.newOutputStream(pathRaMinimizer, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			PrintStream writer = new PrintStream(outFile)) {
			
			String header = "benchmarkName," + String.join(",", MinimizationStatistics.getColumns());
			writer.println(header);
			
			for (Entry<String, SynthesisStatistics> entry : mapBenchmarkToStatistics.entrySet()) {
				String record = entry.getKey() + "," + String.join(",", entry.getValue().minimizationStatistics.getValues());
				writer.println(record);
			}
		}
	}
}
