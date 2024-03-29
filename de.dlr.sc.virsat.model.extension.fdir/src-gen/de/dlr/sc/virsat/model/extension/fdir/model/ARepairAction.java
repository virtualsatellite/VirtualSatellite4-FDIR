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
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
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
public abstract class ARepairAction extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.RepairAction";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_REPAIRRATE = "repairRate";
	public static final String PROPERTY_OBSERVATIONS = "observations";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ARepairAction() {
	}
	
	public ARepairAction(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "RepairAction");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "RepairAction");
		setTypeInstance(categoryAssignement);
	}
	
	public ARepairAction(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: repairRate
	// *****************************************************************
	private BeanPropertyFloat repairRate = new BeanPropertyFloat();
	
	private void safeAccessRepairRate() {
		if (repairRate.getTypeInstance() == null) {
			repairRate.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("repairRate"));
		}
	}
	
	public Command setRepairRate(EditingDomain ed, double value) {
		safeAccessRepairRate();
		return this.repairRate.setValue(ed, value);
	}
	
	public void setRepairRate(double value) {
		safeAccessRepairRate();
		this.repairRate.setValue(value);
	}
	
	public double getRepairRate() {
		safeAccessRepairRate();
		return repairRate.getValue();
	}
	
	public boolean isSetRepairRate() {
		safeAccessRepairRate();
		return repairRate.isSet();
	}
	
	@XmlElement
	public BeanPropertyFloat getRepairRateBean() {
		safeAccessRepairRate();
		return repairRate;
	}
	
	// *****************************************************************
	// * Array Attribute: observations
	// *****************************************************************
		private IBeanList<FaultTreeNode> observations = new TypeSafeReferencePropertyInstanceList<>(FaultTreeNode.class);
	
		private void safeAccessObservations() {
			if (observations.getArrayInstance() == null) {
				observations.setArrayInstance((ArrayInstance) helper.getPropertyInstance("observations"));
			}
		}
	
		public IBeanList<FaultTreeNode> getObservations() {
			safeAccessObservations();
			return observations;
		}
		
		private IBeanList<BeanPropertyReference<FaultTreeNode>> observationsBean = new TypeSafeReferencePropertyBeanList<>();
		
		private void safeAccessObservationsBean() {
			if (observationsBean.getArrayInstance() == null) {
				observationsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("observations"));
			}
		}
		
		@XmlElement
		public IBeanList<BeanPropertyReference<FaultTreeNode>> getObservationsBean() {
			safeAccessObservationsBean();
			return observationsBean;
		}
	
	
}
