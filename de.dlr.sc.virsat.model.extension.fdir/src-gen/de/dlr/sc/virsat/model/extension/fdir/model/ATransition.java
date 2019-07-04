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
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAction;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import org.eclipse.core.runtime.CoreException;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.PropertyinstancesPackage;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.concept.types.factory.BeanCategoryAssignmentFactory;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.list.TypeSafeReferencePropertyInstanceList;
import org.eclipse.emf.edit.domain.EditingDomain;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyBoolean;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.category.ABeanCategoryAssignment;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;


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
public abstract class ATransition extends ABeanCategoryAssignment implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.Transition";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_FROM = "from";
	public static final String PROPERTY_TO = "to";
	public static final String PROPERTY_GUARDS = "guards";
	public static final String PROPERTY_ISREPAIR = "isRepair";
	public static final String PROPERTY_RECOVERYACTIONS = "recoveryActions";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ATransition() {
	}
	
	public ATransition(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "Transition");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "Transition");
		setTypeInstance(categoryAssignement);
	}
	
	public ATransition(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: from
	// *****************************************************************
	private State from;
	
	private void safeAccessFrom() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("from");
		CategoryAssignment ca = (CategoryAssignment) propertyInstance.getReference();
		
		if (ca != null) {
			if (from == null) {
				createFrom(ca);
			}
			from.setTypeInstance(ca);
		} else {
			from = null;
		}
	}
	
	private void createFrom(CategoryAssignment ca) {
		try {
			BeanCategoryAssignmentFactory beanFactory = new BeanCategoryAssignmentFactory();
			from = (State) beanFactory.getInstanceFor(ca);
		} catch (CoreException e) {
			
		}
	}
					
	public State getFrom() {
		safeAccessFrom();
		return from;
	}
	
	public Command setFrom(EditingDomain ed, State value) {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("from");
		CategoryAssignment ca = value.getTypeInstance();
		return SetCommand.create(ed, propertyInstance, PropertyinstancesPackage.Literals.REFERENCE_PROPERTY_INSTANCE__REFERENCE, ca);
	}
	
	public void setFrom(State value) {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("from");
		if (value != null) {
			propertyInstance.setReference(value.getTypeInstance());
		} else {
			propertyInstance.setReference(null);
		}
	}
	
	// *****************************************************************
	// * Attribute: to
	// *****************************************************************
	private State to;
	
	private void safeAccessTo() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("to");
		CategoryAssignment ca = (CategoryAssignment) propertyInstance.getReference();
		
		if (ca != null) {
			if (to == null) {
				createTo(ca);
			}
			to.setTypeInstance(ca);
		} else {
			to = null;
		}
	}
	
	private void createTo(CategoryAssignment ca) {
		try {
			BeanCategoryAssignmentFactory beanFactory = new BeanCategoryAssignmentFactory();
			to = (State) beanFactory.getInstanceFor(ca);
		} catch (CoreException e) {
			
		}
	}
					
	public State getTo() {
		safeAccessTo();
		return to;
	}
	
	public Command setTo(EditingDomain ed, State value) {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("to");
		CategoryAssignment ca = value.getTypeInstance();
		return SetCommand.create(ed, propertyInstance, PropertyinstancesPackage.Literals.REFERENCE_PROPERTY_INSTANCE__REFERENCE, ca);
	}
	
	public void setTo(State value) {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("to");
		if (value != null) {
			propertyInstance.setReference(value.getTypeInstance());
		} else {
			propertyInstance.setReference(null);
		}
	}
	
	// *****************************************************************
	// * Array Attribute: guards
	// *****************************************************************
		private IBeanList<FaultTreeNode> guards = new TypeSafeReferencePropertyInstanceList<>(FaultTreeNode.class);
	
		private void safeAccessGuards() {
			if (guards.getArrayInstance() == null) {
				guards.setArrayInstance((ArrayInstance) helper.getPropertyInstance("guards"));
			}
		}
	
		public IBeanList<FaultTreeNode> getGuards() {
			safeAccessGuards();
			return guards;
		}
	
	// *****************************************************************
	// * Attribute: isRepair
	// *****************************************************************
	private BeanPropertyBoolean isRepair = new BeanPropertyBoolean();
	
	private void safeAccessIsRepair() {
		if (isRepair.getTypeInstance() == null) {
			isRepair.setTypeInstance((ValuePropertyInstance) helper.getPropertyInstance("isRepair"));
		}
	}
	
	public Command setIsRepair(EditingDomain ed, boolean value) {
		safeAccessIsRepair();
		return this.isRepair.setValue(ed, value);
	}
	
	public void setIsRepair(boolean value) {
		safeAccessIsRepair();
		this.isRepair.setValue(value);
	}
	
	public boolean getIsRepair() {
		safeAccessIsRepair();
		return isRepair.getValue();
	}
	
	public BeanPropertyBoolean getIsRepairBean() {
		safeAccessIsRepair();
		return isRepair;
	}
	
	// *****************************************************************
	// * Array Attribute: recoveryActions
	// *****************************************************************
	private IBeanList<RecoveryAction> recoveryActions = new TypeSafeComposedPropertyInstanceList<>(RecoveryAction.class);
	
	private void safeAccessRecoveryActions() {
		if (recoveryActions.getArrayInstance() == null) {
			recoveryActions.setArrayInstance((ArrayInstance) helper.getPropertyInstance("recoveryActions"));
		}
	}
	
	public IBeanList<RecoveryAction> getRecoveryActions() {
		safeAccessRecoveryActions();
		return recoveryActions;
	}
	
	
}
