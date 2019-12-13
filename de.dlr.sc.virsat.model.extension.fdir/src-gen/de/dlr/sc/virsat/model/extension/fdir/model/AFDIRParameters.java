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
import de.dlr.sc.virsat.model.concept.list.TypeSafeArrayInstanceList;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.category.ABeanCategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.extension.fdir.model.CriticalityMatrix;


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
public abstract class AFDIRParameters extends ABeanCategoryAssignment implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.FDIRParameters";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_MISSIONTIME = "missionTime";
	public static final String PROPERTY_TIMESTEP = "timestep";
	public static final String PROPERTY_PROBABILITYLEVELS = "probabilityLevels";
	public static final String PROPERTY_DETECTIONLEVELS = "detectionLevels";
	public static final String PROPERTY_CRITICALITYMATRICES = "criticalityMatrices";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFDIRParameters() {
	}
	
	public AFDIRParameters(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "FDIRParameters");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "FDIRParameters");
		setTypeInstance(categoryAssignement);
	}
	
	public AFDIRParameters(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: missionTime
	// *****************************************************************
	private BeanPropertyFloat missionTime = new BeanPropertyFloat();
	
	private void safeAccessMissionTime() {
		if (missionTime.getTypeInstance() == null) {
			missionTime.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("missionTime"));
		}
	}
	
	public Command setMissionTime(EditingDomain ed, double value) {
		safeAccessMissionTime();
		return this.missionTime.setValue(ed, value);
	}
	
	public void setMissionTime(double value) {
		safeAccessMissionTime();
		this.missionTime.setValue(value);
	}
	
	public double getMissionTime() {
		safeAccessMissionTime();
		return missionTime.getValue();
	}
	
	public boolean isSetMissionTime() {
		safeAccessMissionTime();
		return missionTime.isSet();
	}
	
	public BeanPropertyFloat getMissionTimeBean() {
		safeAccessMissionTime();
		return missionTime;
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
	
	// *****************************************************************
	// * Array Attribute: probabilityLevels
	// *****************************************************************
	private IBeanList<BeanPropertyFloat> probabilityLevels = new TypeSafeArrayInstanceList<>(BeanPropertyFloat.class);
	
	private void safeAccessProbabilityLevels() {
		if (probabilityLevels.getArrayInstance() == null) {
			probabilityLevels.setArrayInstance((ArrayInstance) helper.getPropertyInstance("probabilityLevels"));
		}
	}
		
	public IBeanList<BeanPropertyFloat> getProbabilityLevels() {
		safeAccessProbabilityLevels();
		return probabilityLevels;
	}
	
	// *****************************************************************
	// * Array Attribute: detectionLevels
	// *****************************************************************
	private IBeanList<BeanPropertyFloat> detectionLevels = new TypeSafeArrayInstanceList<>(BeanPropertyFloat.class);
	
	private void safeAccessDetectionLevels() {
		if (detectionLevels.getArrayInstance() == null) {
			detectionLevels.setArrayInstance((ArrayInstance) helper.getPropertyInstance("detectionLevels"));
		}
	}
		
	public IBeanList<BeanPropertyFloat> getDetectionLevels() {
		safeAccessDetectionLevels();
		return detectionLevels;
	}
	
	// *****************************************************************
	// * Array Attribute: criticalityMatrices
	// *****************************************************************
	private IBeanList<CriticalityMatrix> criticalityMatrices = new TypeSafeComposedPropertyInstanceList<>(CriticalityMatrix.class);
	
	private void safeAccessCriticalityMatrices() {
		if (criticalityMatrices.getArrayInstance() == null) {
			criticalityMatrices.setArrayInstance((ArrayInstance) helper.getPropertyInstance("criticalityMatrices"));
		}
	}
	
	public IBeanList<CriticalityMatrix> getCriticalityMatrices() {
		safeAccessCriticalityMatrices();
		return criticalityMatrices;
	}
	
	
}
