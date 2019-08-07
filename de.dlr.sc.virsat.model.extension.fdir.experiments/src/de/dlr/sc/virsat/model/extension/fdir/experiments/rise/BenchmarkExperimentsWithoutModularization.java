/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.experiments.rise;



import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisStatistics;

/**
 * A test case for benchmarking experiments
 * @author jord_ad
 *
 */
public class BenchmarkExperimentsWithoutModularization extends ASynthesizerExperiment {
	
	protected BasicSynthesizer synthesizer;
	
	@Before
	public void setUp() {
		super.setUp();
		synthesizer = new BasicSynthesizer();
	}

	
	/* ***************************************************************************
	 * RAILWAY CROSSINGS
	 * **************************************************************************/
	
	@Test
	public void testRC11() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-1-1-sc.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "RC-1-1", "rise/2019/benchmarkStatisticsWithoutModularizationWithoutModularization");
	}
	
	
	/* ***************************************************************************
	 * Vehicle Guidance Systems (VGS)
	 * **************************************************************************/
	
	@Test
	public void testVGS2() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs2.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "VGS2", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	@Test
	public void testVGS3() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs3.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "VGS3", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	@Test
	public void testVGS4() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs4.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "VGS4", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	@Test
	public void testVGS5() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs5.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "VGS5", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	@Test
	public void testVGS6() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs6.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "VGS6", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	@Test
	public void testVGS7() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs7.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "VGS7", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	@Test
	public void testVGS8() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs8.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "VGS8", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	/* ***************************************************************************
	 * Active Heat Rejection Systems (AHRS)
	 * **************************************************************************/
	@Test
	public void testAHRS1() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/ahrs/ahrs1.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "AHRS1", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	/* ***************************************************************************
	 * Hypothetical Example Computer Systems (HECS)
	 * **************************************************************************/
	@Test
	public void testHECS1() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs1-1.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(fault);
		saveStatistics(synthesizer.getStatistics(), "HECS1", "rise/2019/benchmarkStatisticsWithoutModularization");
	}
	
	/**
	 * Write statistic to a file
	 * @param statistics the statistics
	 * @param testName the name of the test
	 * @param filePath the path
	 * @throws IOException exception
	 */
	protected void saveStatistics(SynthesisStatistics statistics, String testName, String filePath) throws IOException {
		Path path = Paths.get("resources/results/" + filePath + ".txt");
		if (!Files.exists(path.getParent())) {
			Files.createDirectories(path.getParent());
		}
		
		OutputStream outFile = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		PrintStream writer = new PrintStream(outFile);
		writer.println(testName);
		writer.println("===============================================");
		writer.println(statistics);
		writer.println();
	}
}