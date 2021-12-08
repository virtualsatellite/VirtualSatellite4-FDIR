/*******************************************************************************
 * Copyright (c) 2008-2021 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.experiments.phd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.galileo.GalileoDFTParser;
import de.dlr.sc.virsat.fdir.galileo.GalileoDFTWriter;
import de.dlr.sc.virsat.fdir.galileo.dft.DftFactory;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode;
import de.dlr.sc.virsat.fdir.galileo.dft.Observer;

/**
 * This class generates the partial observable fault trees from a benchmark set.
 * The rules are: 
 * - Make all nodes until depth i - 1 immediately observable
 * - Make all nodes on depth i observable with a uniform observation rate 1 / immediately observable
 * 
 * @author muel_s8
 *
 */

public class POPhdExperimentsGenerator {

	private static final String PLUGIN_ID = "de.dlr.sc.virsat.model.extension.fdir";
	private static final String FRAGMENT_ID = PLUGIN_ID + ".experiments";
	public static final int OBSERVATION_LEVELS = 5;
	
	@Test
	public void generatePO() throws IOException {
		for (int i = 0; i < OBSERVATION_LEVELS; ++i) {
			generatePOExperimentSuite("/experimentSet", i, 0);
		}
	}
	
	@Test
	public void generatePODelayed() throws IOException {
		for (int i = 0; i < OBSERVATION_LEVELS; ++i) {
			generatePOExperimentSuite("/experimentSet", i, 1);
		}
	}

	@Test
	public void generateRepairPO() throws IOException {
		for (int i = 0; i < OBSERVATION_LEVELS; ++i) {
			generatePOExperimentSuite("/experimentSet-repair", i, 0);
		}
	}
	
	@Test
	public void generateRepairPODelayed() throws IOException {
		for (int i = 0; i < OBSERVATION_LEVELS; ++i) {
			generatePOExperimentSuite("/experimentSet-repair", i, 1);
		}
	}
	
	/**
	 * Generates the PO experiments for the given experiment suite
	 * 
	 * @param experimentSuite the experiment suite
	 * @param obsLevel level until which observations are possible
	 * @throws IOException
	 */
	private void generatePOExperimentSuite(String experimentSuite, double obsRate, int obsLevel) throws IOException {
		String suffix = "-po";
		if (obsRate > 0) {
			suffix += "-delay";
		}

		String poExperimentSuite = experimentSuite + suffix;

		File suite = new File("." + PhDExperiments.EXPERIMENTS_PATH + experimentSuite);
		File poSuite = new File("." + PhDExperiments.EXPERIMENTS_PATH + poExperimentSuite);

		try (OutputStream outFile = new FileOutputStream(poSuite); 
			PrintStream writer = new PrintStream(outFile)) {
			List<String> fileNames = Files.readAllLines(suite.toPath());
			File parentFolder = suite.getParentFile();
			for (String fileName : fileNames) {
				writer.println(fileName + suffix);
				Path path = Paths.get(parentFolder.toString(), fileName);
				File suiteEntry = path.toFile();
				Path poPath = Paths.get(parentFolder.toString(), fileName + suffix);
				File poSuiteEntry = poPath.toFile();
				poSuiteEntry.mkdir();
				generatePOExperimentSuiteEntry(suiteEntry, poSuiteEntry, obsRate, obsLevel);
			}
		}
	}

	/**
	 * Generate a suite entry for the po suite
	 * @param suiteEntry the original suit entry
	 * @param poSuiteEntry the po suite directory
	 * @param obsRate the observation rate
	 * @param obsLevel level until which observations are possible
	 * @throws IOException
	 */
	private void generatePOExperimentSuiteEntry(File suiteEntry, File poSuiteEntry, double obsRate, int obsLevel) throws IOException {
		if (suiteEntry.isDirectory()) {
			File[] files = suiteEntry.listFiles();
			for (File file : files) {
				generatePOExperimentSuiteEntry(file, poSuiteEntry, obsRate, obsLevel);
			}
		} else {
			Path poPath = poSuiteEntry.toPath().resolve(suiteEntry.getName());
			File poFile = poPath.toFile();
			
			Path platformPath = Paths.get(".\\").relativize(suiteEntry.toPath());
			GalileoDft galileoDft = createGalileoDFT("/" + platformPath.toString());
			
			// Observer observing all nodes to level obsLevel -1
			GalileoFaultTreeNode obs1 = DftFactory.eINSTANCE.createGalileoFaultTreeNode();
			obs1.setName("O1");
			Observer obs1Config = DftFactory.eINSTANCE.createObserver();
			obs1Config.setObservationRate("0");
			obs1.setType(obs1Config);
			galileoDft.getGates().add(obs1);
			
			// Observer observing all nodes on obsLevel
			GalileoFaultTreeNode obs2 = DftFactory.eINSTANCE.createGalileoFaultTreeNode();
			obs2.setName("O2");
			Observer obs2Config = DftFactory.eINSTANCE.createObserver();
			obs2Config.setObservationRate(String.valueOf(obsRate));
			obs2.setType(obs2Config);
			galileoDft.getGates().add(obs2);

			observeNode(obs1Config, obs2Config, galileoDft.getRoot(), 0, obsLevel);
			
			String targetPath = poFile.getAbsolutePath();
			GalileoDFTWriter dftWriter = new GalileoDFTWriter(targetPath);
			dftWriter.write(galileoDft);
			
			System.out.println("Created DFT: " + targetPath);
		}
	}
	
	/**
	 * Registers an observer for the current node
	 * @param obs1Config a level - 1 observer
	 * @param obs2Config a level observer
	 * @param node the node to observe
	 * @param currentObsLevel the current observation level
	 * @param maxObsLevel the maximum observation level
	 */
	private void observeNode(Observer obs1Config, Observer obs2Config, GalileoFaultTreeNode node, int currentObsLevel, int maxObsLevel) {
		if (currentObsLevel == maxObsLevel) {
			obs2Config.getObservables().add(node);
		} else {
			obs1Config.getObservables().add(node);
			for (GalileoFaultTreeNode child : node.getChildren()) {
				observeNode(obs1Config, obs2Config, child, currentObsLevel + 1, maxObsLevel);
			}
		}
	}

	/**
	 * Creates a GALILEO DFT from a locally available resource file.
	 * @param resourcePath the path to the .dft file
	 * @return the root of the DFT
	 * @throws IOException thrown if reading the .dft file fails
	 */
	protected GalileoDft createGalileoDFT(String resourcePath) throws IOException {
		URL url = new URL("platform:/plugin/" + FRAGMENT_ID + resourcePath);
		InputStream inputStream = url.openConnection().getInputStream();
		GalileoDFTParser parser = new GalileoDFTParser();
		return parser.parse(inputStream);
	}
}
