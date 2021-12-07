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
import de.dlr.sc.virsat.model.concept.list.TypeSafeReferencePropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyReference;
import de.dlr.sc.virsat.model.concept.list.TypeSafeReferencePropertyInstanceList;
import org.eclipse.emf.edit.domain.EditingDomain;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyBoolean;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
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
public abstract class AFaultEventTransition extends Transition implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.FaultEventTransition";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_GUARDS = "guards";
	public static final String PROPERTY_ISREPAIR = "isRepair";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFaultEventTransition() {
	}
	
	public AFaultEventTransition(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "FaultEventTransition");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "FaultEventTransition");
		setTypeInstance(categoryAssignement);
	}
	
	public AFaultEventTransition(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
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
		
		private IBeanList<BeanPropertyReference<FaultTreeNode>> guardsBean = new TypeSafeReferencePropertyBeanList<>();
		
		private void safeAccessGuardsBean() {
			if (guardsBean.getArrayInstance() == null) {
				guardsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("guards"));
			}
		}
		
		@XmlElement
		public IBeanList<BeanPropertyReference<FaultTreeNode>> getGuardsBean() {
			safeAccessGuardsBean();
			return guardsBean;
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
	
	@XmlElement
	public BeanPropertyBoolean getIsRepairBean() {
		safeAccessIsRepair();
		return isRepair;
	}
	
	
}
