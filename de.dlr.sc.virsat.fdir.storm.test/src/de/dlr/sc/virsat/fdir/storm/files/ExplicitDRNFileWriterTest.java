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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.test.TestResourceGetter;

/**
 * This class tests the ExplicitDRNFileWriter class
 * @author yoge_re
 *
 */
public class ExplicitDRNFileWriterTest {
	
	@Test
	public void writeFile() throws IOException {
		File testFile = File.createTempFile("test1", ".drn");	
		testFile.deleteOnExit();
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState init = new MarkovState();
		MarkovState fail = new MarkovState();
		MarkovState nondet = new MarkovState();
		
		ma.addState(init);
		ma.addState(fail);
		ma.addState(nondet);
		
		ma.getFinalStates().add(fail);
		
		final double RATE = 10;
		ma.addMarkovianTransition("a", init, fail, RATE);
		ma.addMarkovianTransition("b", init, nondet, RATE);
		ma.addNondeterministicTransition("c", nondet, fail);
		
		new ExplicitDRNFileWriter(ma, testFile.getAbsolutePath()).writeFile();
		String output = new String(Files.readAllBytes(Paths.get(testFile.getAbsolutePath())), StandardCharsets.UTF_8);
		
		TestResourceGetter testResourceGetter = new TestResourceGetter("de.dlr.sc.virsat.fdir.storm.test");
		InputStream is = testResourceGetter.getResourceContentAsStream("/resources/writeTest.drn");
		String expected = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
		
		assertEquals("Written DRN file matches expected DRN file", expected, output);
	}
}
