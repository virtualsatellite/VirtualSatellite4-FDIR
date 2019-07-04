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
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyString;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEvent;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;


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
	public static final String PROPERTY_COLDFAILURERATE = "coldFailureRate";
	public static final String PROPERTY_REPAIRRATE = "repairRate";
	public static final String PROPERTY_REPAIRACTION = "repairAction";
	
	
	
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
	// * Attribute: repairAction
	// *****************************************************************
	private BeanPropertyString repairAction = new BeanPropertyString();
	
	private void safeAccessRepairAction() {
		if (repairAction.getTypeInstance() == null) {
			repairAction.setTypeInstance((ValuePropertyInstance) helper.getPropertyInstance("repairAction"));
		}
	}
	
	public Command setRepairAction(EditingDomain ed, String value) {
		safeAccessRepairAction();
		return this.repairAction.setValue(ed, value);
	}
	
	public void setRepairAction(String value) {
		safeAccessRepairAction();
		this.repairAction.setValue(value);
	}
	
	public String getRepairAction() {
		safeAccessRepairAction();
		return repairAction.getValue();
	}
	
	public BeanPropertyString getRepairActionBean() {
		safeAccessRepairAction();
		return repairAction;
	}
	
	
}
