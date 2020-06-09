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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.Detectability;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToDetection;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateDetectability;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;

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
public  class DetectabilityAnalysis extends ADetectabilityAnalysis {
	private static final double EPS = 0.000001;
	
	/**
	 * Constructor of Concept Class
	 */
	public DetectabilityAnalysis() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public DetectabilityAnalysis(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public DetectabilityAnalysis(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}

	/**
	 * Performs a detectability analysis
	 * @param editingDomain the editing domain
	 * @param monitor the montior
	 * @return the command for executing the detectability analysis
	 */
	public Command perform(TransactionalEditingDomain editingDomain, IProgressMonitor monitor) {
		FaultTreeNode fault = getParentCaBeanOfClass(Fault.class);
				
		final int COUNT_TASKS = 3;
		SubMonitor subMonitor = SubMonitor.convert(monitor, COUNT_TASKS);
		subMonitor.setTaskName("Detectability Analysis");
		subMonitor.split(1);
		subMonitor.subTask("Creating Data Model");
		
		double delta = getTimestepBean().getValueToBaseUnit();

		IBeanStructuralElementInstance parent = new BeanStructuralElementInstance(
				(StructuralElementInstance) fault.getTypeInstance().eContainer());
		RecoveryAutomaton ra = parent.getFirst(RecoveryAutomaton.class);

		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra, delta, EPS);
		
		double maxTime = getRemainingMissionTimeBean().getValueToBaseUnit();
		subMonitor.split(1);
		subMonitor.subTask("Performing Model Checking");

		ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault,
				new Detectability(maxTime), SteadyStateDetectability.SSD, MeanTimeToDetection.MTTD);
	
		double meanTimeToDetection = result.getMeanTimeToDetection();
		double steadyStateDetectability = result.getSteadyStateDetectability();
		
		subMonitor.split(1);
		subMonitor.subTask("Updating Results");
		
		return new RecordingCommand(editingDomain, "Detectability Analysis") {
			@Override
			protected void doExecute() {
				getSteadyStateDetectabilityBean().setValueAsBaseUnit(steadyStateDetectability);
				double detectability = result.getDetectabiity().get(result.getDetectabiity().size() - 1);
				getDetectabilityBean().setValueAsBaseUnit(detectability);
				getMeanTimeToDetectionBean().setValueAsBaseUnit(meanTimeToDetection);
				getDetectabilityCurve().clear();
				for (int i = 0; i < result.getDetectabiity().size(); ++i) {
					createNewDetectabilityCurveEntry(result.getDetectabiity().get(i));
				}
			}
		};
	}

	/**
	 * Creates a new entry for the reliability curve
	 * 
	 * @param value
	 *            the new entry in the reliability curve
	 */
	private void createNewDetectabilityCurveEntry(double value) {
		CategoryInstantiator ci = new CategoryInstantiator();
		APropertyInstance pi = ci.generateInstance(getDetectabilityCurve().getArrayInstance());
		BeanPropertyFloat newBeanProperty = new BeanPropertyFloat();
		newBeanProperty.setTypeInstance((UnitValuePropertyInstance) pi);
		newBeanProperty.setValueAsBaseUnit(value);
		getDetectabilityCurve().add(newBeanProperty);
	}
}
