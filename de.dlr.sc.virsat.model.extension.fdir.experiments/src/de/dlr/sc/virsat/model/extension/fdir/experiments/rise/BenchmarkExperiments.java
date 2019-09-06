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
import org.junit.runners.MethodSorters;
import org.junit.Test;

import de.dlr.sc.virsat.model.extension.fdir.experiments.ASynthesizerExperiment;

/**
 * A test case for benchmarking experiments
 * @author jord_ad
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BenchmarkExperiments extends ASynthesizerExperiment {
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testExperimentSet() throws Exception {
		final File experimentSet = new File("./resources/rise/2019/experimentSet");
		this.testFile(experimentSet, "/resources/rise/2019", "rise/2019/benchmarkExperimentsStatistics", this.synthesizer);
	}

}
