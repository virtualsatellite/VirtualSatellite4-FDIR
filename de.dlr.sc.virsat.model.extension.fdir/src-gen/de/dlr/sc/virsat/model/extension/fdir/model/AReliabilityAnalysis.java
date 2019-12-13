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
public abstract class AReliabilityAnalysis extends ABeanCategoryAssignment implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.ReliabilityAnalysis";
	
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
	public static final String PROPERTY_RELIABILITY = "reliability";
	public static final String PROPERTY_MEANTIMETOFAILURE = "meanTimeToFailure";
	public static final String PROPERTY_RELIABILITYCURVE = "reliabilityCurve";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AReliabilityAnalysis() {
	}
	
	public AReliabilityAnalysis(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "ReliabilityAnalysis");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "ReliabilityAnalysis");
		setTypeInstance(categoryAssignement);
	}
	
	public AReliabilityAnalysis(CategoryAssignment categoryAssignement) {
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
	// * Attribute: reliability
	// *****************************************************************
	private BeanPropertyFloat reliability = new BeanPropertyFloat();
	
	private void safeAccessReliability() {
		if (reliability.getTypeInstance() == null) {
			reliability.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("reliability"));
		}
	}
	
	public Command setReliability(EditingDomain ed, double value) {
		safeAccessReliability();
		return this.reliability.setValue(ed, value);
	}
	
	public void setReliability(double value) {
		safeAccessReliability();
		this.reliability.setValue(value);
	}
	
	public double getReliability() {
		safeAccessReliability();
		return reliability.getValue();
	}
	
	public boolean isSetReliability() {
		safeAccessReliability();
		return reliability.isSet();
	}
	
	public BeanPropertyFloat getReliabilityBean() {
		safeAccessReliability();
		return reliability;
	}
	
	// *****************************************************************
	// * Attribute: meanTimeToFailure
	// *****************************************************************
	private BeanPropertyFloat meanTimeToFailure = new BeanPropertyFloat();
	
	private void safeAccessMeanTimeToFailure() {
		if (meanTimeToFailure.getTypeInstance() == null) {
			meanTimeToFailure.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("meanTimeToFailure"));
		}
	}
	
	public Command setMeanTimeToFailure(EditingDomain ed, double value) {
		safeAccessMeanTimeToFailure();
		return this.meanTimeToFailure.setValue(ed, value);
	}
	
	public void setMeanTimeToFailure(double value) {
		safeAccessMeanTimeToFailure();
		this.meanTimeToFailure.setValue(value);
	}
	
	public double getMeanTimeToFailure() {
		safeAccessMeanTimeToFailure();
		return meanTimeToFailure.getValue();
	}
	
	public boolean isSetMeanTimeToFailure() {
		safeAccessMeanTimeToFailure();
		return meanTimeToFailure.isSet();
	}
	
	public BeanPropertyFloat getMeanTimeToFailureBean() {
		safeAccessMeanTimeToFailure();
		return meanTimeToFailure;
	}
	
	// *****************************************************************
	// * Array Attribute: reliabilityCurve
	// *****************************************************************
	private IBeanList<BeanPropertyFloat> reliabilityCurve = new TypeSafeArrayInstanceList<>(BeanPropertyFloat.class);
	
	private void safeAccessReliabilityCurve() {
		if (reliabilityCurve.getArrayInstance() == null) {
			reliabilityCurve.setArrayInstance((ArrayInstance) helper.getPropertyInstance("reliabilityCurve"));
		}
	}
		
	public IBeanList<BeanPropertyFloat> getReliabilityCurve() {
		safeAccessReliabilityCurve();
		return reliabilityCurve;
	}
	
	
}
