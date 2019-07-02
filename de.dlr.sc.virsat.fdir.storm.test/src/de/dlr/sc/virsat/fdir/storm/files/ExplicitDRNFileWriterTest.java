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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;

/**
 * This class tests the ExplicitDRNFileWriter class
 * @author yoge_re
 *
 */
public class ExplicitDRNFileWriterTest {
	
	@Test
	public void testDRNFile() throws IOException {
		File testFile = File.createTempFile("test1", ".drn");	
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		MarkovState state1 = new MarkovState();
		MarkovState state2 = new MarkovState();
		ma.addState(state1);
		ma.addState(state2);
		ma.getFinalStates().add(state2);
		final double RATE = 10;
		ma.addMarkovianTransition("a", state1, state2, RATE);

		new ExplicitDRNFileWriter(ma, testFile.getAbsolutePath()).writeFile();
		
		File expectedFile = File.createTempFile("test2", ".drn");
		PrintWriter writer = new PrintWriter(expectedFile);
		writer.print("@type: Markov Automaton\n");
		writer.print("@parameters\n" + "\n");
		writer.print("@reward_models\n" + "\n");
		writer.print("@nr_states" + "\n");
		writer.print(2 + "\n");		
		writer.print("@model" + "\n");
		writer.print("state 0 !" + RATE + " init\n");
		writer.print("\taction 0\n");
		writer.print("\t\t" + 1 + " : " + 1.0 + "\n");
		writer.print("state 1 !1.0 failed\n");
		writer.print("\taction 0\n");
		writer.print("\t\t" + 1 + " : " + 1.0 + "\n");
		writer.close();
		
		testFile.deleteOnExit();
		expectedFile.deleteOnExit();
		
		assertEquals(new String(Files.readAllBytes(Paths.get(expectedFile.getAbsolutePath())), StandardCharsets.UTF_8), new String(Files.readAllBytes(Paths.get(testFile.getAbsolutePath())), StandardCharsets.UTF_8));

	}
}
