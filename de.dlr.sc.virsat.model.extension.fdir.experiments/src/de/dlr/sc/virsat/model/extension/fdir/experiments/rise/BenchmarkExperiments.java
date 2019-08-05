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



import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.BasicSynthesizer;

/**
 * A test case for benchmarking experiments
 * @author jord_ad
 *
 */
public class BenchmarkExperiments extends ASynthesizerExperiment {
	
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
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC12() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-1-2-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC101sc() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-10-1-sc.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC151() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-15-1-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC201() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-20-1-hc.dft");
		RecoveryAutomaton ra = synthesizer.synthesize(fault);
		saveRA(ra, "rise2019/rc/rc201");
	}
	
	@Test
	public void testRC13() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-1-3-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC14hc() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-1-4-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC14sc() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-1-4-sc.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC15() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-1-5-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC101hc() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-10-1-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testRC1010() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-10-10-hc.dft");
		synthesizer.synthesize(fault);
	}
	
	/* ***************************************************************************
	 * Vehicle Guidance Systems (VGS)
	 * **************************************************************************/
	@Test
	public void testVGS1() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testVGS2() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs2.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testVGS3() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs3.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testVGS4() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs4.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testVGS5() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs5.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testVGS6() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs6.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testVGS7() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs7.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testVGS8() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/vgs/vgs8.dft");
		synthesizer.synthesize(fault);
	}
	
	/* ***************************************************************************
	 * Active Heat Rejection Systems (AHRS)
	 * **************************************************************************/
	@Test
	public void testAHRS1() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/ahrs/ahrs1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testAHRS2() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/ahrs/ahrs2.dft");
		synthesizer.synthesize(fault);
	}
	
	/* ***************************************************************************
	 * Hypothetical Example Computer Systems (HECS)
	 * **************************************************************************/
	@Test
	public void testHECS1() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs1-1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testHECS2() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs2-1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testHECS3() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs3-1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testHECS4() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs4-1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testHECS5() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs5-1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testHECS6() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs6-1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testHECS7() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs7-1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testHECS8() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/hecs/hecs8-1.dft");
		synthesizer.synthesize(fault);
	}
	
	/* ***************************************************************************
	 * Mission Avionics Systems (MAS)
	 * **************************************************************************/
	@Test
	public void testMAS1() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/mas/mas1.dft");
		synthesizer.synthesize(fault);
	}
	
	@Test
	public void testMAS2() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/mas/mas2.dft");
		synthesizer.synthesize(fault);
	}
	
	
}
