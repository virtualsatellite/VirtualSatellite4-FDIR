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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.test.TestResourceGetter;

/**
 * This class tests the ExplicitPropFileWriter class
 * @author yoge_re
 *
 */
public class ExplicitPropFileWriterTest {
	@Test
	public void writeFile() throws IOException {
		
		File testFile = File.createTempFile("test1", ".prop");
		testFile.deleteOnExit();
		
		final double DELTA = 360;
		final double timeHorizon = 720;
		Reliability reliability = new Reliability(timeHorizon);
		
		new ExplicitPropertiesWriter(DELTA, testFile.getAbsolutePath(), MTTF.MTTF, reliability).writeFile();
		String output = new String(Files.readAllBytes(Paths.get(testFile.getAbsolutePath())), StandardCharsets.UTF_8);
		
		TestResourceGetter testResourceGetter = new TestResourceGetter("de.dlr.sc.virsat.fdir.storm.test");
		InputStream is = testResourceGetter.getResourceContentAsStream("/resources/writeTest.prop");
		String expected = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
		
		assertEquals("Written PROP file matches expected PROP file", expected, output);
	}
}
