/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.synthesizer;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.ClaimAction;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.test.ATestCase;

/**
 * This class tests the Schedule to Recovery Automaton converter.
 * @author muel_s8
 *
 */

public class Schedule2RAConverterTest extends ATestCase {
	
	@Test
	public void testNonInternalTransition() {
		SPARE fault = new SPARE(concept);
		ClaimAction ca1 = new ClaimAction(concept);
		ca1.setSpareGate(fault);
		ClaimAction ca2 = new ClaimAction(concept);
		
		MarkovAutomaton<MarkovState> ma = new MarkovAutomaton<>();
		
		MarkovState initial = new MarkovState();
		MarkovState nondet = new MarkovState();
		MarkovState correct = new MarkovState();
		MarkovState wrong = new MarkovState();
		
		ma.addState(initial);
		ma.addState(nondet);
		ma.addState(correct);
		ma.addState(wrong);
		
		ma.addMarkovianTransition(Collections.singleton(fault), initial, nondet, 1);
		MarkovTransition<MarkovState> choice = ma.addNondeterministicTransition(Collections.singletonList(ca1), nondet, correct);
		ma.addNondeterministicTransition(Collections.singletonList(ca2), nondet, wrong);
		
		Map<MarkovState, Set<MarkovTransition<MarkovState>>> schedule = new HashMap<>();
		schedule.put(nondet, Collections.singleton(choice));
		
		Schedule2RAConverter<MarkovState> converter = new Schedule2RAConverter<>(ma, concept);
		RecoveryAutomaton ra = converter.convert(schedule, initial);
		
		assertEquals(2, ra.getStates().size());
		assertEquals(1, ra.getTransitions().size());
		assertEquals(ra.getInitial(), ra.getStates().get(0));
		
		FaultEventTransition transition = (FaultEventTransition) ra.getTransitions().get(0);
		assertEquals(ra.getStates().get(0), transition.getFrom());
		assertEquals(ra.getStates().get(1), transition.getTo());
		assertThat(transition.getGuards(), hasItem(fault));
		assertEquals(1, transition.getRecoveryActions().size());
		assertEquals(fault, ((ClaimAction) transition.getRecoveryActions().get(0)).getSpareGate());
	}

}
