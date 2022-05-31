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
import javax.xml.bind.annotation.XmlAccessorType;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.json.ABeanObjectAdapter;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyComposed;
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;
import javax.xml.bind.annotation.XmlElement;


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
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class ATransition extends GenericCategory implements IBeanCategoryAssignment {

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
	private BeanPropertyReference<State> from = new BeanPropertyReference<>();
	
	private void safeAccessFrom() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("from");
		from.setTypeInstance(propertyInstance);
	}
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
	public State getFrom() {
		safeAccessFrom();
		return from.getValue();
	}
	
	public Command setFrom(EditingDomain ed, State value) {
		safeAccessFrom();
		return from.setValue(ed, value);
	}
	
	public void setFrom(State value) {
		safeAccessFrom();
		from.setValue(value);
	}
	
	public BeanPropertyReference<State> getFromBean() {
		safeAccessFrom();
		return from;
	}
	
	// *****************************************************************
	// * Attribute: to
	// *****************************************************************
	private BeanPropertyReference<State> to = new BeanPropertyReference<>();
	
	private void safeAccessTo() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("to");
		to.setTypeInstance(propertyInstance);
	}
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
	public State getTo() {
		safeAccessTo();
		return to.getValue();
	}
	
	public Command setTo(EditingDomain ed, State value) {
		safeAccessTo();
		return to.setValue(ed, value);
	}
	
	public void setTo(State value) {
		safeAccessTo();
		to.setValue(value);
	}
	
	public BeanPropertyReference<State> getToBean() {
		safeAccessTo();
		return to;
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
	
	private IBeanList<BeanPropertyComposed<RecoveryAction>> recoveryActionsBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessRecoveryActionsBean() {
		if (recoveryActionsBean.getArrayInstance() == null) {
			recoveryActionsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("recoveryActions"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<RecoveryAction>> getRecoveryActionsBean() {
		safeAccessRecoveryActionsBean();
		return recoveryActionsBean;
	}
	
	
}
