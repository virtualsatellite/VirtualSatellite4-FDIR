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
	public void testFolderCM() throws Exception {
		final File folder = new File("./resources/rise/2019/cm");
		this.testFolder(folder, "/resources/rise/2019/cm");
	}
	
	@Test
	public void testFolderAHRS() throws Exception {
		final File folder = new File("./resources/rise/2019/ahrs");
		this.testFolder(folder, "/resources/rise/2019/ahrs");
	}
	
	@Test
	public void testFolderHECS() throws Exception {
		final File folder = new File("./resources/rise/2019/hecs");
		this.testFolder(folder, "/resources/rise/2019/hecs");
	}
	
	@Test
	public void testFolderMAS() throws Exception {
		final File folder = new File("./resources/rise/2019/mas");
		this.testFolder(folder, "/resources/rise/2019/mas");
	}
	
	@Test
	public void testFolderRC() throws Exception {
		final File folder = new File("./resources/rise/2019/rc");
		this.testFolder(folder, "/resources/rise/2019/rc");
	}
	
	@Test
	public void testFolderVGS() throws Exception {
		final File folder = new File("./resources/rise/2019/vgs");
		this.testFolder(folder, "/resources/rise/2019/vgs");
	}

}
