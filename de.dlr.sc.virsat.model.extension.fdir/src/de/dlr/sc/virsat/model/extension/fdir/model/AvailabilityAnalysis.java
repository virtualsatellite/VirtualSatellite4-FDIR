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
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
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
public class AvailabilityAnalysis extends AAvailabilityAnalysis {
	private static final double EPS = 0.0001;

	/**
	 * Constructor of Concept Class
	 */
	public AvailabilityAnalysis() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate a CategoryAssignment in
	 * the background from the given concept
	 * 
	 * @param concept
	 *            the concept where it will find the correct Category to instantiate
	 *            from
	 */
	public AvailabilityAnalysis(Concept concept) {
		super(concept);
	}

	/**
	 * Constructor of Concept Class that can be initialized manually by a given
	 * Category Assignment
	 * 
	 * @param categoryAssignment
	 *            The category Assignment to be used for background initialization
	 *            of the Category bean
	 */
	public AvailabilityAnalysis(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}

	/**
	 * 
	 * @param ed
	 *            the editing domain
	 * @param monitor
	 *            the progress monitor
	 * @return a command that sets the availability analysis
	 */
	public Command perform(TransactionalEditingDomain ed, IProgressMonitor monitor) {
		FaultTreeNode fault = getParentCaBeanOfClass(Fault.class);

		final int COUNT_TASKS = 3;
		SubMonitor subMonitor = SubMonitor.convert(monitor, COUNT_TASKS);
		subMonitor.setTaskName("Availability Analysis");
		subMonitor.subTask("Creating Data Model");
		subMonitor.split(1);
		
		double delta = getTimestepBean().getValueToBaseUnit();

		IBeanStructuralElementInstance parent = new BeanStructuralElementInstance(
				(StructuralElementInstance) fault.getTypeInstance().eContainer());
		RecoveryAutomaton ra = parent.getFirst(RecoveryAutomaton.class);

		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra, delta, EPS);
		
		double maxTime = getRemainingMissionTimeBean().getValueToBaseUnit();

		subMonitor.subTask("Performing Model Checking");
		ModelCheckingResult result = ftEvaluator
				.evaluateFaultTree(fault, subMonitor.split(1), new Availability(maxTime), SteadyStateAvailability.STEADY_STATE_AVAILABILITY);
		
		
		subMonitor.subTask("Updating Results");
		subMonitor.split(1);
		
		double steadyStateAvailability = result.getSteadyStateAvailability();
		return new RecordingCommand(ed, "Availability Analysis") {
			@Override
			protected void doExecute() {
				getSteadyStateAvailabilityBean().setValueAsBaseUnit(steadyStateAvailability);
				getAvailabilityBean().setValueAsBaseUnit(result.getAvailability().get(result.getAvailability().size() - 1));
				getAvailabilityCurve().clear();
				for (int i = 0; i < result.getAvailability().size(); ++i) {
					createNewAvailabilityCurveEntry(result.getAvailability().get(i));
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
	private void createNewAvailabilityCurveEntry(double value) {
		CategoryInstantiator ci = new CategoryInstantiator();
		APropertyInstance pi = ci.generateInstance(getAvailabilityCurve().getArrayInstance());
		BeanPropertyFloat newBeanProperty = new BeanPropertyFloat();
		newBeanProperty.setTypeInstance((UnitValuePropertyInstance) pi);
		newBeanProperty.setValueAsBaseUnit(value);
		getAvailabilityCurve().add(newBeanProperty);
	}
}
