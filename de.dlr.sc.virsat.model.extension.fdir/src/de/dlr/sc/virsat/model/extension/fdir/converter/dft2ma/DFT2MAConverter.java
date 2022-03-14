/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.A2MAConverter;
import de.dlr.sc.virsat.fdir.core.markov.algorithm.Bisimulation;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IQualitativeMetric;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FailableBasicEventsProvider;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

/**
 * Constructs a markov automaton from a DFT.
 * @author muel_s8
 *
 */
public class DFT2MAConverter extends A2MAConverter<DFTState, DFT2MAStateSpaceGenerator> {
	
	/**
	 * Converts a fault tree with the passed node as a root to a Markov automaton.
	 * @param root a fault tree node used as a root node for the conversion
	 * @param failableBasicEventsProvider the nodes that need to fail
	 * @param failLabelProvider the fail label criterion
	 * @param monitor the monitor
	 * @return the generated Markov automaton resulting from the conversion
	 */
	public MarkovAutomaton<DFTState> convert(FaultTreeNode root, FailableBasicEventsProvider failableBasicEventsProvider, SubMonitor monitor) {
		FaultTreeNode holderRoot = root instanceof BasicEvent ? root.getFault() : root;
		FaultTreeHolder ftHolder = new FaultTreeHolder(holderRoot);
		stateSpaceGenerator.configure(ftHolder, failableBasicEventsProvider);
		MarkovAutomaton<DFTState> ma = maBuilder.build(stateSpaceGenerator, monitor);
		boolean bisimulationFlag = false;
		if (bisimulationFlag) {
			Bisimulation<DFTState> bisimulation = new Bisimulation<>(ma);
			bisimulation.computeQuotient(monitor);
		}
		
		return ma;
	}
	
	/**
	 * Converts a fault tree with the passed node as a root to a
	 * Markov automaton.
	 * @param root a fault tree node used as a root node for the conversion
	 * @return the generated Markov automaton resulting from the conversion
	 */
	public MarkovAutomaton<DFTState> convert(FaultTreeNode root) {
		return convert(root, null, null);
	}
	
	/**
	 * Configures the state space generator
	 * @param metrics the metrics to evaluate
	 * @param failableBasicEventsProvider the basic events provider
	 */
	public void configure(DFTSemantics semantics, IMetric[] metrics) {
		stateSpaceGenerator.setSemantics(semantics);
		stateSpaceGenerator.getDftSemantics().setAllowsRepairEvents(!hasQualitativeMetric(metrics));
	}
	
	/**
	 * Checks if there is a qualitative metric
	 * @param metrics the metrics
	 * @return true iff at least one metric is qualitative
	 */
	private boolean hasQualitativeMetric(IMetric[] metrics) {
		for (IMetric metric : metrics) {
			if (metric instanceof IQualitativeMetric) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected DFT2MAStateSpaceGenerator createStateSpaceGenerator() {
		return new DFT2MAStateSpaceGenerator();
	}
}
