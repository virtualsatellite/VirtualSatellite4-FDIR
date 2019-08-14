/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public  class MCSAnalysis extends AMCSAnalysis {
	
	/**
	 * Constructor of Concept Class
	 */
	public MCSAnalysis() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public MCSAnalysis(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public MCSAnalysis(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Gets the first fault attached to the same structural element instance
	 * @return the top level fault to be analysed
	 */
	public Fault getFault() {
		IBeanStructuralElementInstance parent = new BeanStructuralElementInstance((StructuralElementInstance) getTypeInstance().eContainer());
		return parent.getFirst(Fault.class);
	}
	
	/**
	 * Peforms a minimum cut set analysis on the top level fault this analysis has been attached to
	 * @param ed the editing domain
	 * @param monitor 
	 * @return a command that sets the results of this analysis
	 */
	public Command perform(TransactionalEditingDomain ed, IProgressMonitor monitor) {
		IBeanStructuralElementInstance parent = new BeanStructuralElementInstance((StructuralElementInstance) getTypeInstance().eContainer());
		List<Fault> faults = parent.getAll(Fault.class);
		
		if (faults.isEmpty()) {
			return UnexecutableCommand.INSTANCE;
		}
		
		SubMonitor subMonitor = null;
		final int COUNT_TASKS = 1 + 2 * faults.size();
		if (monitor != null) {
			monitor.setTaskName("MCS Analysis");
			subMonitor = SubMonitor.convert(monitor, COUNT_TASKS);

		}
		
		long maxMinimumCutSetSize = getMaxMinimumCutSetSizeBean().isSet() ? getMaxMinimumCutSetSize() : 0;
		List<Long> faultTolerances = new ArrayList<>();
		Set<Set<Object>> minimumCutSets = new HashSet<>();
		Map<Set<Object>, List<Fault>> mapCutSetToFaults = new HashMap<>();
		
		for (Fault fault : faults) {
			if (monitor != null) {
				subMonitor.setTaskName("Analysing fault " + fault.getName());
				subMonitor.split(1);
			}
			
			RecoveryAutomaton ra = parent.getFirst(RecoveryAutomaton.class);
			
			FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra != null);
			if (ra != null) {
				ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
			}
			if (monitor != null) {
				if (monitor.isCanceled()) {
					return UnexecutableCommand.INSTANCE;
				}
				subMonitor.setTaskName("Calculating MCS");
				subMonitor.split(1);
			}
			
			ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, new MinimumCutSet(maxMinimumCutSetSize));
			for (Set<Object> minCutSet : result.getMinCutSets()) {
				mapCutSetToFaults.computeIfAbsent(minCutSet, v -> new ArrayList<>()).add(fault);
			}
			minimumCutSets.addAll(result.getMinCutSets());
			faultTolerances.add(minimumCutSets.stream().mapToLong(cutSet -> cutSet.size()).min().orElse(Integer.MAX_VALUE));
		}
		
		if (monitor != null) {
			if (monitor.isCanceled()) {
				return UnexecutableCommand.INSTANCE;
			}
			subMonitor.setTaskName("Updating Results");
			subMonitor.split(1);
		}
		
		long faultTolerance = faultTolerances.stream().mapToLong(tolerance -> tolerance).min().orElse(1) - 1;
		List<Set<Object>> sortedMinimumCutSets = minimumCutSets.stream()
				.sorted((b1, b2) -> Integer.compare(b1.size(), b2.size()))
				.collect(Collectors.toList());
		
		return new RecordingCommand(ed, "MCS Analysis") {
			@Override
			protected void doExecute() {
				setFaultTolerance(faultTolerance);
				
				getMinimumCutSets().clear();
				for (Set<Object> minimumCutSet : sortedMinimumCutSets) {
					List<Fault> failures = mapCutSetToFaults.get(minimumCutSet);
					for (Fault failure : failures) {
						CutSet cutSet = new CutSet(getConcept());
						for (Object object : minimumCutSet) {
							cutSet.getBasicEvents().add((BasicEvent) object);
						}
						getMinimumCutSets().add(cutSet);
						cutSet.setFailure(failure);
					}
				}
			}
		};
	}
}
