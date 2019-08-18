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
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import org.eclipse.emf.edit.domain.EditingDomain;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.category.ABeanCategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;


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
public abstract class AFaultAnalysis extends ABeanCategoryAssignment implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.FaultAnalysis";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_REMAININGMISSIONTIME = "remainingMissionTime";
	public static final String PROPERTY_TIMESTEP = "timestep";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFaultAnalysis() {
	}
	
	public AFaultAnalysis(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "FaultAnalysis");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "FaultAnalysis");
		setTypeInstance(categoryAssignement);
	}
	
	public AFaultAnalysis(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: remainingMissionTime
	// *****************************************************************
	private BeanPropertyFloat remainingMissionTime = new BeanPropertyFloat();
	
	private void safeAccessRemainingMissionTime() {
		if (remainingMissionTime.getTypeInstance() == null) {
			remainingMissionTime.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("remainingMissionTime"));
		}
	}
	
	public Command setRemainingMissionTime(EditingDomain ed, double value) {
		safeAccessRemainingMissionTime();
		return this.remainingMissionTime.setValue(ed, value);
	}
	
	public void setRemainingMissionTime(double value) {
		safeAccessRemainingMissionTime();
		this.remainingMissionTime.setValue(value);
	}
	
	public double getRemainingMissionTime() {
		safeAccessRemainingMissionTime();
		return remainingMissionTime.getValue();
	}
	
	public boolean isSetRemainingMissionTime() {
		safeAccessRemainingMissionTime();
		return remainingMissionTime.isSet();
	}
	
	public BeanPropertyFloat getRemainingMissionTimeBean() {
		safeAccessRemainingMissionTime();
		return remainingMissionTime;
	}
	
	// *****************************************************************
	// * Attribute: timestep
	// *****************************************************************
	private BeanPropertyFloat timestep = new BeanPropertyFloat();
	
	private void safeAccessTimestep() {
		if (timestep.getTypeInstance() == null) {
			timestep.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("timestep"));
		}
	}
	
	public Command setTimestep(EditingDomain ed, double value) {
		safeAccessTimestep();
		return this.timestep.setValue(ed, value);
	}
	
	public void setTimestep(double value) {
		safeAccessTimestep();
		this.timestep.setValue(value);
	}
	
	public double getTimestep() {
		safeAccessTimestep();
		return timestep.getValue();
	}
	
	public boolean isSetTimestep() {
		safeAccessTimestep();
		return timestep.isSet();
	}
	
	public BeanPropertyFloat getTimestepBean() {
		safeAccessTimestep();
		return timestep;
	}
	
	
}
