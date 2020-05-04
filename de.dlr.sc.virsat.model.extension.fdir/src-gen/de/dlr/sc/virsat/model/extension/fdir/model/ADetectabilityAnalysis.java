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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
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
public abstract class ADetectabilityAnalysis extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.DetectabilityAnalysis";
	
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
	public static final String PROPERTY_DETECTABILITY = "detectability";
	public static final String PROPERTY_STEADYSTATEDETECTABILITY = "steadyStateDetectability";
	public static final String PROPERTY_MEANTIMETODETECTION = "meanTimeToDetection";
	public static final String PROPERTY_DETECTABILITYCURVE = "detectabilityCurve";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ADetectabilityAnalysis() {
	}
	
	public ADetectabilityAnalysis(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "DetectabilityAnalysis");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "DetectabilityAnalysis");
		setTypeInstance(categoryAssignement);
	}
	
	public ADetectabilityAnalysis(CategoryAssignment categoryAssignement) {
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
	
	// *****************************************************************
	// * Attribute: detectability
	// *****************************************************************
	private BeanPropertyFloat detectability = new BeanPropertyFloat();
	
	private void safeAccessDetectability() {
		if (detectability.getTypeInstance() == null) {
			detectability.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("detectability"));
		}
	}
	
	public Command setDetectability(EditingDomain ed, double value) {
		safeAccessDetectability();
		return this.detectability.setValue(ed, value);
	}
	
	public void setDetectability(double value) {
		safeAccessDetectability();
		this.detectability.setValue(value);
	}
	
	public double getDetectability() {
		safeAccessDetectability();
		return detectability.getValue();
	}
	
	public boolean isSetDetectability() {
		safeAccessDetectability();
		return detectability.isSet();
	}
	
	public BeanPropertyFloat getDetectabilityBean() {
		safeAccessDetectability();
		return detectability;
	}
	
	// *****************************************************************
	// * Attribute: steadyStateDetectability
	// *****************************************************************
	private BeanPropertyFloat steadyStateDetectability = new BeanPropertyFloat();
	
	private void safeAccessSteadyStateDetectability() {
		if (steadyStateDetectability.getTypeInstance() == null) {
			steadyStateDetectability.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("steadyStateDetectability"));
		}
	}
	
	public Command setSteadyStateDetectability(EditingDomain ed, double value) {
		safeAccessSteadyStateDetectability();
		return this.steadyStateDetectability.setValue(ed, value);
	}
	
	public void setSteadyStateDetectability(double value) {
		safeAccessSteadyStateDetectability();
		this.steadyStateDetectability.setValue(value);
	}
	
	public double getSteadyStateDetectability() {
		safeAccessSteadyStateDetectability();
		return steadyStateDetectability.getValue();
	}
	
	public boolean isSetSteadyStateDetectability() {
		safeAccessSteadyStateDetectability();
		return steadyStateDetectability.isSet();
	}
	
	public BeanPropertyFloat getSteadyStateDetectabilityBean() {
		safeAccessSteadyStateDetectability();
		return steadyStateDetectability;
	}
	
	// *****************************************************************
	// * Attribute: meanTimeToDetection
	// *****************************************************************
	private BeanPropertyFloat meanTimeToDetection = new BeanPropertyFloat();
	
	private void safeAccessMeanTimeToDetection() {
		if (meanTimeToDetection.getTypeInstance() == null) {
			meanTimeToDetection.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("meanTimeToDetection"));
		}
	}
	
	public Command setMeanTimeToDetection(EditingDomain ed, double value) {
		safeAccessMeanTimeToDetection();
		return this.meanTimeToDetection.setValue(ed, value);
	}
	
	public void setMeanTimeToDetection(double value) {
		safeAccessMeanTimeToDetection();
		this.meanTimeToDetection.setValue(value);
	}
	
	public double getMeanTimeToDetection() {
		safeAccessMeanTimeToDetection();
		return meanTimeToDetection.getValue();
	}
	
	public boolean isSetMeanTimeToDetection() {
		safeAccessMeanTimeToDetection();
		return meanTimeToDetection.isSet();
	}
	
	public BeanPropertyFloat getMeanTimeToDetectionBean() {
		safeAccessMeanTimeToDetection();
		return meanTimeToDetection;
	}
	
	// *****************************************************************
	// * Array Attribute: detectabilityCurve
	// *****************************************************************
	private IBeanList<BeanPropertyFloat> detectabilityCurve = new TypeSafeArrayInstanceList<>(BeanPropertyFloat.class);
	
	private void safeAccessDetectabilityCurve() {
		if (detectabilityCurve.getArrayInstance() == null) {
			detectabilityCurve.setArrayInstance((ArrayInstance) helper.getPropertyInstance("detectabilityCurve"));
		}
	}
		
	public IBeanList<BeanPropertyFloat> getDetectabilityCurve() {
		safeAccessDetectabilityCurve();
		return detectabilityCurve;
	}
	
	
}
