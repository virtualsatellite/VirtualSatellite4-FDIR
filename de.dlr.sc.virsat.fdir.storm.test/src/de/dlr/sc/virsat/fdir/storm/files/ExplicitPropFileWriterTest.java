/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.files;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;

/**
 * This class tests the ExplicitPropFileWriter class
 * @author yoge_re
 *
 */
public class ExplicitPropFileWriterTest {
	@Test
	public void testPropFile() throws IOException {
		
		File testFile = File.createTempFile("test1", ".prop");
		final double DELTA = 360;
		final double timeHorizon = 720;
		Reliability reliability = new Reliability(timeHorizon);
		final IMetric[] METRICS = {MTTF.MTTF, reliability};	
		new ExplicitPropertiesWriter(DELTA, testFile.getAbsolutePath(), METRICS).writeFile();
		
		File expectedFile = File.createTempFile("test1", ".prop");	
		PrintWriter writer = new PrintWriter(expectedFile);
		writer.println("Tmin=? [F \"failed\"];");
		writer.println("Pmin=? [F<=360.0 \"failed\"];");
		writer.println("Pmin=? [F<=720.0 \"failed\"];");
		writer.close();
		
		testFile.deleteOnExit();
		expectedFile.deleteOnExit();
		
		assertEquals(new String(Files.readAllBytes(Paths.get(expectedFile.getAbsolutePath())), StandardCharsets.UTF_8), new String(Files.readAllBytes(Paths.get(testFile.getAbsolutePath())), StandardCharsets.UTF_8));
		
	}
}
