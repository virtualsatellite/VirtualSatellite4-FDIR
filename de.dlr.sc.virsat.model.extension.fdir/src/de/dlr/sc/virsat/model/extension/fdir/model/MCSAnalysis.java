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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
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
		Fault fault = getFault();
		
		if (fault != null) {
			SubMonitor subMonitor = null;
			if (monitor != null) {
				monitor.setTaskName("Availability Analysis");
				final int COUNT_TASKS = 3;
				subMonitor = SubMonitor.convert(monitor, COUNT_TASKS);
				subMonitor.setTaskName("Creating Data Model");
				subMonitor.split(1);
			}
			IBeanStructuralElementInstance parent = new BeanStructuralElementInstance((StructuralElementInstance) getTypeInstance().eContainer());
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
			ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault);
		
			long maxMinimumCutSetSize = getMaxMinimumCutSetSizeBean().isSet() ? getMaxMinimumCutSetSize() : -1;
			
			Set<Set<Object>> minimumCutSets = result.getMinCutSets();
			List<Set<Object>> sortedMinimumCutSets = minimumCutSets.stream()
						.filter(cutset -> maxMinimumCutSetSize == -1 ? true : maxMinimumCutSetSize >= cutset.size())
						.sorted((b1, b2) -> Integer.compare(b1.size(), b2.size()))
						.collect(Collectors.toList());
			
			int faultTolerance = minimumCutSets.stream().mapToInt(cutSet -> cutSet.size()).min().orElse(0);
			if (monitor != null) {
				if (monitor.isCanceled()) {
					return UnexecutableCommand.INSTANCE;
				}
				subMonitor.setTaskName("Updating Results");
				subMonitor.split(1);
			}
			return new RecordingCommand(ed, "MCS Analysis") {
				@Override
				protected void doExecute() {
					setFaultTolerance(faultTolerance);
					
					getMinimumCutSets().clear();
					for (Set<Object> minimumCutSet : sortedMinimumCutSets) {
						CutSet cutSet = new CutSet(getConcept());
						for (Object object : minimumCutSet) {
							cutSet.getBasicEvents().add((BasicEvent) object);
						}
						getMinimumCutSets().add(cutSet);
					}
				}
			};
		} else {
			return UnexecutableCommand.INSTANCE;
		}
	}
}
