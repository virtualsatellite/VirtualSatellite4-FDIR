/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.recovery.minimizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.util.IStatistics;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.RecoveryAutomatonHolder;

/**
 * Class that implements a series of recovery automata minimizers 
 * @author mika_li
 *
 */
public class ComposedMinimizer extends ARecoveryAutomatonMinimizer {

	private List<ARecoveryAutomatonMinimizer> minimizers = new ArrayList<>();
	
	@Override
	protected List<String> getMinimizerNames() {
		if (minimizers != null) {
			return minimizers.stream()
					.flatMap(m -> m.getMinimizerNames().stream())
					.distinct()	
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}
	
	
	@Override
	protected void minimize(RecoveryAutomatonHolder raHolder, FaultTreeHolder ftHolder, SubMonitor subMonitor) {
		subMonitor = SubMonitor.convert(subMonitor, minimizers.size());
		
		for (ARecoveryAutomatonMinimizer minimizer : minimizers) {
			int removedStates = raHolder.getRa().getStates().size();
			minimizer.minimize(raHolder, ftHolder, subMonitor.split(1));
			removedStates -= raHolder.getRa().getStates().size();
			getStatistics().minimizers.merge(minimizer.getClass().getSimpleName(), removedStates, 
				(v1, v2) -> v1 == IStatistics.NA ? v2 : v1 + v2
			);
		}
	}
	
	/**
	 * Adds a minimizer to the list of recovery automaton minimizers 
	 * @param minimizer  
	 */
	public void addMinimizer(ARecoveryAutomatonMinimizer minimizer) {
		minimizers.add(minimizer);
	}

	/**
	 * Returns a composed minimizer as a series of minimizers  
	 * @return composed minimizer
	 */
	public static ComposedMinimizer createDefaultMinimizer() {
		ComposedMinimizer composedMinimizer = new ComposedMinimizer();
		composedMinimizer.addMinimizer(new PartitionRefinementMinimizer());
		composedMinimizer.addMinimizer(new FinalStateMinimizer());
		composedMinimizer.addMinimizer(new PartitionRefinementMinimizer());
		composedMinimizer.addMinimizer(new OrthogonalPartitionRefinementMinimizer());
		composedMinimizer.addMinimizer(new CleanMinimizer());
		return composedMinimizer;
	}
}
