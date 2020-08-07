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

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovTransition;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.IMarkovScheduler;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.MarkovScheduler;
import de.dlr.sc.virsat.fdir.core.markov.scheduler.ScheduleQuery;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFT2MAConverter;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;

/**
 * A very basic synthesizer
 * @author muel_s8
 *
 */

public class BasicSynthesizer extends ASynthesizer {

	private static final int TICKS = 2;
	
	protected IMarkovScheduler<DFTState> scheduler = new MarkovScheduler<>();
	
	@Override
	protected RecoveryAutomaton convertToRecoveryAutomaton(MarkovAutomaton<DFTState> ma, DFTState initialMa, SubMonitor subMonitor) {
		subMonitor = SubMonitor.convert(subMonitor, TICKS);
		ScheduleQuery<DFTState> scheduelQuery = new ScheduleQuery<>(ma, initialMa);
		Map<DFTState, List<MarkovTransition<DFTState>>> schedule = scheduler.computeOptimalScheduler(scheduelQuery, subMonitor.split(1));
		return new Schedule2RAConverter<>(ma, concept).convert(schedule, initialMa, subMonitor.split(1));
	}

	@Override
	protected DFT2MAConverter createDFT2MAConverter() {
		return new DFT2MAConverter();
	}
}
