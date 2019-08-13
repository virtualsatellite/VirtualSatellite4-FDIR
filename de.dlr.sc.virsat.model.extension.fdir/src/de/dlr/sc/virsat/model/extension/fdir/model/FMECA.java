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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyString;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.calculation.AdvancedFunction;
import de.dlr.sc.virsat.model.dvlm.calculation.CalculationFactory;
import de.dlr.sc.virsat.model.dvlm.calculation.Equation;
import de.dlr.sc.virsat.model.dvlm.calculation.ReferencedInput;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;
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
public  class FMECA extends AFMECA {
	
	/**
	 * Constructor of Concept Class
	 */
	public FMECA() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FMECA(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FMECA(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	@Override
	public IBeanStructuralElementInstance getParent() {
		return new BeanStructuralElementInstance((StructuralElementInstance) getTypeInstance().eContainer());
	}

	/**
	 * Creates a command for regenerating the FMECA
	 * @param editingDomain the editing domain
	 * @param monitor a monitor
	 * @return a command to regenerate the fmeca
	 */
	public Command perform(TransactionalEditingDomain editingDomain, IProgressMonitor monitor) {
		return new RecordingCommand(editingDomain, "FMECA") {
			@Override
			protected void doExecute() {
				getEntries().clear();
				getEntries().addAll(generateEntries(monitor));
			}
		};
	}
	
	/**
	 * Generates the FMECA entries
	 * @param monitor a progress monitor
	 * @return the newly generated fmeca entries
	 */
	public List<FMECAEntry> generateEntries(IProgressMonitor monitor) {
		List<FMECAEntry> entries = new ArrayList<>();
		List<Fault> failures = getParent().getAll(Fault.class).stream()
				.filter(fault -> fault.isLocalTopLevelFault())
				.collect(Collectors.toList());
		
		monitor.beginTask("Collecting FMECA entries", 1);
		for (Fault failure : failures) {
			Set<FaultEvent> failureModes = failure.getFaultTree().getFailureModes();
			for (FaultEvent failureMode : failureModes) {
				if (failureMode instanceof Fault) {
					Fault nonBasicFailureMode = (Fault) failureMode; 
					Set<FaultEvent> failureCauses = nonBasicFailureMode.getFaultTree().getFailureModes();
					
					for (FaultEvent failureCause : failureCauses) {
						entries.add(generateEntry(failure, failureMode, failureCause, monitor));
					}
					
					if (failureCauses.isEmpty()) {
						entries.add(generateEntry(failure, failureMode, null, monitor));
					}
				} else {
					entries.add(generateEntry(failure, failureMode, null, monitor));
				}
			}
			
			if (failureModes.isEmpty()) {
				entries.add(generateEntry(failure, null, null, monitor));
			}
		}
		monitor.worked(1);
		
		monitor.beginTask("Filling out FMECA entries", entries.size());
		for (FMECAEntry entry : entries) {
			fillEntry(entry);
			monitor.worked(1);
		}
		
		return entries;
	}
	
	/**
	 * Generates a single fmeca entry for a <failure, failureMode, failureCause> triple
	 * @param failure the failure
	 * @param failureMode the failure mode
	 * @param failureCause the failure cause
	 * @param monitor the progress monitor
	 * @return the fmeca entry
	 */
	private FMECAEntry generateEntry(Fault failure, FaultEvent failureMode, FaultEvent failureCause, IProgressMonitor monitor) {
		FMECAEntry entry = new FMECAEntry(getConcept());
		entry.setFailure(failure);
		entry.setFailureMode(failureMode);
		entry.setFailureCause(failureCause);
		return entry;
	}
	
	/**
	 * Fills out an FMECA entry as much as possible
	 * @param entry the fmeca entry
	 */
	private void fillEntry(FMECAEntry entry) {
		entry.setSeverity(entry.getFailure().getSeverity());
		
		FaultTreeNode analysisNode = entry.getFailureCause() != null ? entry.getFailureCause() : entry.getFailureMode();
		
		if (analysisNode != null) {
			RecoveryAutomaton ra = getParent().getFirst(RecoveryAutomaton.class);
			FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra != null);
			if (ra != null) {
				ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
			}
			
			ModelCheckingResult result = ftEvaluator.evaluateFaultTree(analysisNode, MTTF.MTTF);
			entry.getMeanTimeToFailureBean().setValueAsBaseUnit(result.getMeanTimeToFailure());
		} else {
			entry.setMeanTimeToFailure(Double.NaN);
		}
		
		entry.getFailureEffects().addAll(entry.getFailure().getFaultTree().getAffectedFaults());
		
		Set<String> proposedRecoveryActions = new HashSet<>();
		proposedRecoveryActions.addAll(entry.getFailure().getFaultTree().getPotentialRecoveryActions());
		if (entry.getFailureMode() != null) {
			proposedRecoveryActions.addAll(entry.getFailureMode().getFault().getFaultTree().getPotentialRecoveryActions());
		}
		if (entry.getFailureCause() != null) {
			proposedRecoveryActions.addAll(entry.getFailureCause().getFault().getFaultTree().getPotentialRecoveryActions());
		}
		
		
		CategoryInstantiator ci = new CategoryInstantiator();
		for (String proposedRecoveryAction : proposedRecoveryActions) {
			APropertyInstance pi = ci.generateInstance(entry.getProposedRecovery().getArrayInstance());
			BeanPropertyString newBeanProperty = new BeanPropertyString();
			newBeanProperty.setTypeInstance((ValuePropertyInstance) pi);
			newBeanProperty.setValue(proposedRecoveryAction);
			entry.getProposedRecovery().add(newBeanProperty);
		}
		
		EObject root = VirSatEcoreUtil.getRootContainer(getTypeInstance().eContainer());
		if (root != null) {
			BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance((StructuralElementInstance) root);
			FDIRParameters fdirParameters = beanSei.getFirst(FDIRParameters.class);
			
			if (fdirParameters != null) {
				Equation equation = entry.getTypeInstance().getEquationSection().getEquations().get(0);
				AdvancedFunction opClassifyPL = (AdvancedFunction) equation.getExpression();
				ReferencedInput ri = CalculationFactory.eINSTANCE.createReferencedInput();
				ri.setReference(fdirParameters.getTypeInstance());
				opClassifyPL.getInputs().add(ri);
			}
		}
	}
}
