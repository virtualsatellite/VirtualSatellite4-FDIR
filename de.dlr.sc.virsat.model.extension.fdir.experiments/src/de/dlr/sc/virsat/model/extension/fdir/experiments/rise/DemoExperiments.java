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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ModularSynthesizer;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.SynthesisQuery;

/**
 * A test case for benchmarking experiments
 * @author jord_ad
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DemoExperiments extends ASynthesizerExperiment {
	
	protected ModularSynthesizer synthesizer;
	
	@Before
	public void setUp() {
		super.setUp();
		synthesizer = new ModularSynthesizer();
	}
	
	/* ***************************************************************************
	 * WITH MODULARIZATION
	 * **************************************************************************/
	@Test
	public void testRC11() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-1-1-sc.dft");
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
	
	@Test
	public void testAHRS1() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/ahrs/ahrs1.dft");
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
	
	@Test
	public void testCMSimple() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/cm/cm_simple.dft");
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}

	@Test
	public void testCM1() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/cm/cm1.dft");
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
	
	@Test
	public void testCM2() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/cm/cm2.dft");
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
	
	/* ***************************************************************************
	 * WITHOUT MODULARIZATION
	 * **************************************************************************/
	
	@Test
	public void testRC11WithoutModularizer() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/rc/rc-1-1-sc.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
	
	@Test
	public void testAHRS1WithoutModularizer() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/ahrs/ahrs1.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
	
	@Test
	public void testCMSimpleWithoutModularizer() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/cm/cm_simple.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
	
	@Test
	public void testCM1WithoutModularizer() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/cm/cm1.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
	
	@Test
	public void testCM2WithoutModularizer() throws Exception {
		Fault fault = createDFT("/resources/rise/2019/cm/cm2.dft");
		synthesizer.setModularizer(null);
		synthesizer.synthesize(new SynthesisQuery(fault), null);
	}
}