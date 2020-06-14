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



import java.io.File;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;
import de.dlr.sc.virsat.model.extension.fdir.synthesizer.ModularSynthesizer;

/**
 * A test case for benchmarking experiments
 * @author jord_ad
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BenchmarkExperimentsWithoutModularization extends ASynthesizerExperiment {
	
	@Before
	public void setUp() {
		super.setUp();
		synthesizer = new ModularSynthesizer();
		synthesizer.setModularizer(null);
	}
	
	@Test
	public void testExperimentSet() throws Exception {
		final File experimentSet = new File("./resources/rise/2019/experimentSetWM");
		this.testFile(experimentSet, "/resources/rise/2019", "rise/2019/benchmarkExperimentsStatisticsWM", this.synthesizer);
	}
}