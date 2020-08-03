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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyEnum;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyComposed;


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
public abstract class ABasicEvent extends FaultEvent implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.BasicEvent";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_HOTFAILURERATE = "hotFailureRate";
	public static final String PROPERTY_DISTRIBUTION = "distribution";
	public static final String PROPERTY_COLDFAILURERATE = "coldFailureRate";
	public static final String PROPERTY_REPAIRRATE = "repairRate";
	public static final String PROPERTY_REPAIRACTIONS = "repairActions";
	
	// Distribution enumeration value names
	public static final String DISTRIBUTION_EXP_NAME = "EXP";
	public static final String DISTRIBUTION_UNIFORM_NAME = "UNIFORM";
	// Distribution enumeration values
	public static final String DISTRIBUTION_EXP_VALUE = "0";
	public static final String DISTRIBUTION_UNIFORM_VALUE = "1";
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ABasicEvent() {
	}
	
	public ABasicEvent(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "BasicEvent");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "BasicEvent");
		setTypeInstance(categoryAssignement);
	}
	
	public ABasicEvent(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: hotFailureRate
	// *****************************************************************
	private BeanPropertyFloat hotFailureRate = new BeanPropertyFloat();
	
	private void safeAccessHotFailureRate() {
		if (hotFailureRate.getTypeInstance() == null) {
			hotFailureRate.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("hotFailureRate"));
		}
	}
	
	public Command setHotFailureRate(EditingDomain ed, double value) {
		safeAccessHotFailureRate();
		return this.hotFailureRate.setValue(ed, value);
	}
	
	public void setHotFailureRate(double value) {
		safeAccessHotFailureRate();
		this.hotFailureRate.setValue(value);
	}
	
	public double getHotFailureRate() {
		safeAccessHotFailureRate();
		return hotFailureRate.getValue();
	}
	
	public boolean isSetHotFailureRate() {
		safeAccessHotFailureRate();
		return hotFailureRate.isSet();
	}
	
	public BeanPropertyFloat getHotFailureRateBean() {
		safeAccessHotFailureRate();
		return hotFailureRate;
	}
	
	// *****************************************************************
	// * Attribute: distribution
	// *****************************************************************
	private BeanPropertyEnum distribution = new BeanPropertyEnum();
	
	private void safeAccessDistribution() {
		if (distribution.getTypeInstance() == null) {
			distribution.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("distribution"));
		}
	}
	
	public Command setDistribution(EditingDomain ed, String value) {
		safeAccessDistribution();
		return this.distribution.setValue(ed, value);
	}
	
	public void setDistribution(String value) {
		safeAccessDistribution();
		this.distribution.setValue(value);
	}
	
	public String getDistribution() {
		safeAccessDistribution();
		return distribution.getValue();
	}
	
	public double getDistributionEnum() {
		safeAccessDistribution();
		return distribution.getEnumValue();
	}
	
	public BeanPropertyEnum getDistributionBean() {
		safeAccessDistribution();
		return distribution;
	}
	
	// *****************************************************************
	// * Attribute: coldFailureRate
	// *****************************************************************
	private BeanPropertyFloat coldFailureRate = new BeanPropertyFloat();
	
	private void safeAccessColdFailureRate() {
		if (coldFailureRate.getTypeInstance() == null) {
			coldFailureRate.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("coldFailureRate"));
		}
	}
	
	public Command setColdFailureRate(EditingDomain ed, double value) {
		safeAccessColdFailureRate();
		return this.coldFailureRate.setValue(ed, value);
	}
	
	public void setColdFailureRate(double value) {
		safeAccessColdFailureRate();
		this.coldFailureRate.setValue(value);
	}
	
	public double getColdFailureRate() {
		safeAccessColdFailureRate();
		return coldFailureRate.getValue();
	}
	
	public boolean isSetColdFailureRate() {
		safeAccessColdFailureRate();
		return coldFailureRate.isSet();
	}
	
	public BeanPropertyFloat getColdFailureRateBean() {
		safeAccessColdFailureRate();
		return coldFailureRate;
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
	
	public BeanPropertyFloat getRepairRateBean() {
		safeAccessRepairRate();
		return repairRate;
	}
	
	// *****************************************************************
	// * Array Attribute: repairActions
	// *****************************************************************
	private IBeanList<RepairAction> repairActions = new TypeSafeComposedPropertyInstanceList<>(RepairAction.class);
	
	private void safeAccessRepairActions() {
		if (repairActions.getArrayInstance() == null) {
			repairActions.setArrayInstance((ArrayInstance) helper.getPropertyInstance("repairActions"));
		}
	}
	
	public IBeanList<RepairAction> getRepairActions() {
		safeAccessRepairActions();
		return repairActions;
	}
	
	private IBeanList<BeanPropertyComposed<RepairAction>> repairActionsBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessRepairActionsBean() {
		if (repairActionsBean.getArrayInstance() == null) {
			repairActionsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("repairActions"));
		}
	}
	
	public IBeanList<BeanPropertyComposed<RepairAction>> getRepairActionsBean() {
		safeAccessRepairActionsBean();
		return repairActionsBean;
	}
	
	
}
