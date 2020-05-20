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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Before;

import de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.converter.galileo.GalileoDFT2DFT;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.DFTEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;
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
	protected BasicSynthesizer synthesizer;
	
	protected Concept concept;
	protected FaultTreeBuilder ftBuilder;
	protected RecoveryAutomatonHelper raHelper;
	
	@Before
	public void setUp() {
		concept = ConceptXmiLoader.loadConceptFromPlugin(PLUGIN_ID + "/concept/concept.xmi");
		this.ftBuilder = new FaultTreeBuilder(concept);
		this.raHelper = new RecoveryAutomatonHelper(concept);
		this.synthesizer = new BasicSynthesizer();
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
	protected void testFolder(File folder, String folderPath) throws IOException {
		if (folder.isDirectory()) {
			System.out.println("Not a directory: " + folder.getAbsolutePath());
		}
		
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					testFolder(file, folderPath + "/" + file.getName());
				} else {
					Fault fault = createDFT(folderPath + "/" + file.getName());
					System.out.println(fault.getFaultTree().toDot());
					synthesizer.synthesize(fault);
					saveStatistics(synthesizer.getStatistics(), file.getName(), "rise/2019/" + folder.getName());
				}
			}
		}
	}
	
	/**
	 * Tests all of the .dft benchmarks in the given folder
	 * @param file the file with all the dft names
	 * @param filePath the path to the file
	 * @param saveFileName the name of the file you wish to save the results to
	 * @param synthesizer the synthesizer
	 * @throws IOException exception
	 */
	protected void testFile(File file, String filePath, String saveFileName, BasicSynthesizer synthesizer) throws IOException {
		
		String entireFile = new String(Files.readAllBytes(file.toPath()));
		
		for (String filename : entireFile.split("\\r?\\n")) {
			File parentFolder = file.getParentFile();
			
			File[] childFolders = parentFolder.listFiles();
			if (childFolders == null) {
				continue;
			}
			
			for (File childFolder : childFolders) {
				if (childFolder.isDirectory()) {
					File[] matchingFiles = childFolder.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.equals(filename);
						}
					});
				
					if (matchingFiles != null && matchingFiles.length != 0) {
						File benchmarkFile = matchingFiles[0];
						Fault fault = createDFT(filePath + "/" + childFolder.getName() + "/" + benchmarkFile.getName());
						synthesizer.synthesize(fault);
						saveStatistics(synthesizer.getStatistics(), benchmarkFile.getName(), saveFileName);
					}
				}
			}
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
	 * Write statistic to a file
	 * @param statistics the statistics
	 * @param testName the name of the test
	 * @param filePath the path
	 * @throws IOException exception
	 */
	protected static void saveStatistics(SynthesisStatistics statistics, String testName, String filePath) throws IOException {
		Path path = Paths.get("resources/results/" + filePath + ".txt");
		Files.createFile(path);
		
		try (OutputStream outFile = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			PrintStream writer = new PrintStream(outFile)) {
			writer.println(testName);
			writer.println("===============================================");
			writer.println(statistics);
			writer.println();
		}
	}
}
