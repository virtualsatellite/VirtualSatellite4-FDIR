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
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyComposed;
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;


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
	private BeanPropertyReference<State> initial = new BeanPropertyReference<>();
	
	private void safeAccessInitial() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("initial");
		initial.setTypeInstance(propertyInstance);
	}
	
	public State getInitial() {
		safeAccessInitial();
		return initial.getValue();
	}
	
	public Command setInitial(EditingDomain ed, State value) {
		safeAccessInitial();
		return initial.setValue(ed, value);
	}
	
	public void setInitial(State value) {
		safeAccessInitial();
		initial.setValue(value);
	}
	
	public BeanPropertyReference<State> getInitialBean() {
		safeAccessInitial();
		return initial;
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
	
	private IBeanList<BeanPropertyComposed<State>> statesBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessStatesBean() {
		if (statesBean.getArrayInstance() == null) {
			statesBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("states"));
		}
	}
	
	public IBeanList<BeanPropertyComposed<State>> getStatesBean() {
		safeAccessStatesBean();
		return statesBean;
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
	
	private IBeanList<BeanPropertyComposed<Transition>> transitionsBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessTransitionsBean() {
		if (transitionsBean.getArrayInstance() == null) {
			transitionsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("transitions"));
		}
	}
	
	public IBeanList<BeanPropertyComposed<Transition>> getTransitionsBean() {
		safeAccessTransitionsBean();
		return transitionsBean;
	}
	
	
}
