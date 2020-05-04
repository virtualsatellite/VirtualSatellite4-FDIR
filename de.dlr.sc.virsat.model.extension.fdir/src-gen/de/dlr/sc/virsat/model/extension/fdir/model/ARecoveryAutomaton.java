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

// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import org.eclipse.core.runtime.CoreException;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.PropertyinstancesPackage;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.concept.types.factory.BeanCategoryAssignmentFactory;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;


// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */	
public abstract class ARecoveryAutomaton extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.RecoveryAutomaton";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_INITIAL = "initial";
	public static final String PROPERTY_STATES = "states";
	public static final String PROPERTY_TRANSITIONS = "transitions";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ARecoveryAutomaton() {
	}
	
	public ARecoveryAutomaton(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "RecoveryAutomaton");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "RecoveryAutomaton");
		setTypeInstance(categoryAssignement);
	}
	
	public ARecoveryAutomaton(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: initial
	// *****************************************************************
	private State initial;
	
	private void safeAccessInitial() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("initial");
		CategoryAssignment ca = (CategoryAssignment) propertyInstance.getReference();
		
		if (ca != null) {
			if (initial == null) {
				createInitial(ca);
			}
			initial.setTypeInstance(ca);
		} else {
			initial = null;
		}
	}
	
	private void createInitial(CategoryAssignment ca) {
		try {
			BeanCategoryAssignmentFactory beanFactory = new BeanCategoryAssignmentFactory();
			initial = (State) beanFactory.getInstanceFor(ca);
		} catch (CoreException e) {
			
		}
	}
					
	public State getInitial() {
		safeAccessInitial();
		return initial;
	}
	
	public Command setInitial(EditingDomain ed, State value) {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("initial");
		CategoryAssignment ca = value.getTypeInstance();
		return SetCommand.create(ed, propertyInstance, PropertyinstancesPackage.Literals.REFERENCE_PROPERTY_INSTANCE__REFERENCE, ca);
	}
	
	public void setInitial(State value) {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("initial");
		if (value != null) {
			propertyInstance.setReference(value.getTypeInstance());
		} else {
			propertyInstance.setReference(null);
		}
	}
	
	// *****************************************************************
	// * Array Attribute: states
	// *****************************************************************
	private IBeanList<State> states = new TypeSafeComposedPropertyInstanceList<>(State.class);
	
	private void safeAccessStates() {
		if (states.getArrayInstance() == null) {
			states.setArrayInstance((ArrayInstance) helper.getPropertyInstance("states"));
		}
	}
	
	public IBeanList<State> getStates() {
		safeAccessStates();
		return states;
	}
	
	// *****************************************************************
	// * Array Attribute: transitions
	// *****************************************************************
	private IBeanList<Transition> transitions = new TypeSafeComposedPropertyInstanceList<>(Transition.class);
	
	private void safeAccessTransitions() {
		if (transitions.getArrayInstance() == null) {
			transitions.setArrayInstance((ArrayInstance) helper.getPropertyInstance("transitions"));
		}
	}
	
	public IBeanList<Transition> getTransitions() {
		safeAccessTransitions();
		return transitions;
	}
	
	
}
