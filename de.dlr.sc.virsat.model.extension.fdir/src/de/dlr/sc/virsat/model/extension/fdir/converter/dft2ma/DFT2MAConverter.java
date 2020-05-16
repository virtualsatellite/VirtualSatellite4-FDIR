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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomatonBuilder;
import de.dlr.sc.virsat.fdir.core.metrics.FailLabelProvider;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IQualitativeMetric;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PONDDFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.semantics.DFTSemantics;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FailableBasicEventsProvider;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

public class DFT2MAConverter {
	private DFT2MAStateSpaceGenerator stateSpaceGenerator = new DFT2MAStateSpaceGenerator();
	private MarkovAutomatonBuilder<DFTState> maBuilder = new MarkovAutomatonBuilder<DFTState>(stateSpaceGenerator);
	
	public MarkovAutomaton<DFTState> convert(FaultTreeNode root, FailableBasicEventsProvider failableBasicEventsProvider, FailLabelProvider failLabelProvider) {
		stateSpaceGenerator.setRoot(root);
		stateSpaceGenerator.setFailableBasicEventsProvider(failableBasicEventsProvider);
		stateSpaceGenerator.setFailLabelProvider(failLabelProvider);
		
		MarkovAutomaton<DFTState> ma = maBuilder.build();
		
		return ma;
	}
	
	public MarkovAutomaton<DFTState> convert(FaultTreeNode root) {
		return convert(root, null, null);
	}
	
	public DFT2MAStateSpaceGenerator getStateSpaceGenerator() {
		return stateSpaceGenerator;
	}
	
	public MarkovAutomatonBuilder<DFTState> getMaBuilder() {
		return maBuilder;
	}
	
	/**
	 * Configures the state space generator
	 * @param ftHolder the fault tree to convert
	 * @param metrics the metrics to evaluate
	 * @param failableBasicEventsProvider the basic events provider
	 */
	public void configure(FaultTreeHolder ftHolder, DFTSemantics semantics, IMetric[] metrics, FailableBasicEventsProvider failableBasicEventsProvider) {
		stateSpaceGenerator.setSemantics(semantics);
		stateSpaceGenerator.getDftSemantics().setAllowsRepairEvents(!hasQualitativeMetric(metrics));
		
		if (stateSpaceGenerator.getDftSemantics() instanceof PONDDFTSemantics) {
			stateSpaceGenerator.getStaticAnalysis().setSymmetryChecker(null);
			stateSpaceGenerator.setAllowsDontCareFailing(false);
		} else {
			stateSpaceGenerator.getStaticAnalysis().setSymmetryChecker(new DFTSymmetryChecker());
			stateSpaceGenerator.setAllowsDontCareFailing(true);
		}
		
		if (failableBasicEventsProvider != null) {
			stateSpaceGenerator.getStaticAnalysis().setSymmetryChecker(null);
		}
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
}
