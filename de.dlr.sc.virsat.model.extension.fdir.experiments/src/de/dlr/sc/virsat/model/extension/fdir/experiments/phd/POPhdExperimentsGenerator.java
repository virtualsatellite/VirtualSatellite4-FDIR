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
 * - Make the TLE and all gates on depth 1 observable. 
 * - The root monitor is always immediately observable
 * - Use a uniform observation rate of 1 for all other MONITOR gates if there is an observation delay
 * 
 * @author muel_s8
 *
 */

public class POPhdExperimentsGenerator {

	private static final String PLUGIN_ID = "de.dlr.sc.virsat.model.extension.fdir";
	private static final String FRAGMENT_ID = PLUGIN_ID + ".experiments";
	
	@Test
	public void generatePO() throws IOException {
		generatePOExperimentSuite("/experimentSet", 0);
	}
	
	@Test
	public void generatePODelayed() throws IOException {
		generatePOExperimentSuite("/experimentSet", 1);
	}

	@Test
	public void generateRepairPO() throws IOException {
		generatePOExperimentSuite("/experimentSet-repair", 0);
	}
	
	@Test
	public void generateRepairPODelayed() throws IOException {
		generatePOExperimentSuite("/experimentSet-repair", 1);
	}
	
	/**
	 * Generates the PO experiments for the given experiment suite
	 * 
	 * @param experimentSuite the experiment suite
	 * @throws IOException
	 */
	private void generatePOExperimentSuite(String experimentSuite, double obsRate) throws IOException {
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
				generatePOExperimentSuiteEntry(suiteEntry, poSuiteEntry, obsRate);
			}
		}
	}

	/**
	 * Generate a suite entry for the po suite
	 * @param suiteEntry the original suit entry
	 * @param poSuiteEntry the po suite directory
	 * @param obsRate the observation rate
	 * @throws IOException
	 */
	private void generatePOExperimentSuiteEntry(File suiteEntry, File poSuiteEntry, double obsRate) throws IOException {
		if (suiteEntry.isDirectory()) {
			File[] files = suiteEntry.listFiles();
			for (File file : files) {
				generatePOExperimentSuiteEntry(file, poSuiteEntry, obsRate);
			}
		} else {
			Path poPath = poSuiteEntry.toPath().resolve(suiteEntry.getName());
			File poFile = poPath.toFile();
			
			Path platformPath = Paths.get(".\\").relativize(suiteEntry.toPath());
			GalileoDft galileoDft = createGalileoDFT("/" + platformPath.toString());
			
			GalileoFaultTreeNode observer = DftFactory.eINSTANCE.createGalileoFaultTreeNode();
			observer.setName("O");
			Observer observerConfig = DftFactory.eINSTANCE.createObserver();
			observerConfig.setObservationRate(String.valueOf(obsRate));
			observer.setType(observerConfig);
			observerConfig.getObservables().add(galileoDft.getRoot());
			galileoDft.getGates().add(observer);

			for (GalileoFaultTreeNode child : galileoDft.getRoot().getChildren()) {
				observerConfig.getObservables().add(child);
			}
			
			String targetPath = poFile.getAbsolutePath();
			GalileoDFTWriter dftWriter = new GalileoDFTWriter(targetPath);
			dftWriter.write(galileoDft);
			
			System.out.println("Created DFT: " + targetPath);
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
