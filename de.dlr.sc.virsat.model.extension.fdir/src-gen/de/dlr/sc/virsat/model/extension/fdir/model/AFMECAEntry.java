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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyEnum;
import de.dlr.sc.virsat.model.concept.list.TypeSafeReferencePropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyString;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyReference;
import de.dlr.sc.virsat.model.concept.list.TypeSafeReferencePropertyInstanceList;
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
public abstract class AFMECAEntry extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.FMECAEntry";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_FAILURE = "failure";
	public static final String PROPERTY_FAILUREMODE = "failureMode";
	public static final String PROPERTY_FAILURECAUSE = "failureCause";
	public static final String PROPERTY_FAILUREEFFECTS = "failureEffects";
	public static final String PROPERTY_SEVERITY = "severity";
	public static final String PROPERTY_PROBABILITY = "probability";
	public static final String PROPERTY_CRITICALITY = "criticality";
	public static final String PROPERTY_MEANTIMETOFAILURE = "meanTimeToFailure";
	public static final String PROPERTY_COMPENSATION = "compensation";
	
	// Severity enumeration value names
	public static final String SEVERITY_Catastrophic_NAME = "Catastrophic";
	public static final String SEVERITY_Critical_NAME = "Critical";
	public static final String SEVERITY_Major_NAME = "Major";
	public static final String SEVERITY_Minor_NAME = "Minor";
	public static final String SEVERITY_Unknown_NAME = "Unknown";
	// Severity enumeration values
	public static final String SEVERITY_Catastrophic_VALUE = "4";
	public static final String SEVERITY_Critical_VALUE = "3";
	public static final String SEVERITY_Major_VALUE = "2";
	public static final String SEVERITY_Minor_VALUE = "1";
	public static final String SEVERITY_Unknown_VALUE = "0";
	// Probability enumeration value names
	public static final String PROBABILITY_Unknown_NAME = "Unknown";
	public static final String PROBABILITY_ExtremelyRemote_NAME = "ExtremelyRemote";
	public static final String PROBABILITY_Remote_NAME = "Remote";
	public static final String PROBABILITY_Occasional_NAME = "Occasional";
	public static final String PROBABILITY_Probable_NAME = "Probable";
	// Probability enumeration values
	public static final String PROBABILITY_Unknown_VALUE = "0";
	public static final String PROBABILITY_ExtremelyRemote_VALUE = "1";
	public static final String PROBABILITY_Remote_VALUE = "2";
	public static final String PROBABILITY_Occasional_VALUE = "3";
	public static final String PROBABILITY_Probable_VALUE = "4";
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFMECAEntry() {
	}
	
	public AFMECAEntry(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "FMECAEntry");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "FMECAEntry");
		setTypeInstance(categoryAssignement);
	}
	
	public AFMECAEntry(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: failure
	// *****************************************************************
	private BeanPropertyReference<Fault> failure = new BeanPropertyReference<>();
	
	private void safeAccessFailure() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("failure");
		failure.setTypeInstance(propertyInstance);
	}
	
	public Fault getFailure() {
		safeAccessFailure();
		return failure.getValue();
	}
	
	public Command setFailure(EditingDomain ed, Fault value) {
		safeAccessFailure();
		return failure.setValue(ed, value);
	}
	
	public void setFailure(Fault value) {
		safeAccessFailure();
		failure.setValue(value);
	}
	
	public BeanPropertyReference<Fault> getFailureBean() {
		safeAccessFailure();
		return failure;
	}
	
	// *****************************************************************
	// * Attribute: failureMode
	// *****************************************************************
	private BeanPropertyReference<FaultEvent> failureMode = new BeanPropertyReference<>();
	
	private void safeAccessFailureMode() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("failureMode");
		failureMode.setTypeInstance(propertyInstance);
	}
	
	public FaultEvent getFailureMode() {
		safeAccessFailureMode();
		return failureMode.getValue();
	}
	
	public Command setFailureMode(EditingDomain ed, FaultEvent value) {
		safeAccessFailureMode();
		return failureMode.setValue(ed, value);
	}
	
	public void setFailureMode(FaultEvent value) {
		safeAccessFailureMode();
		failureMode.setValue(value);
	}
	
	public BeanPropertyReference<FaultEvent> getFailureModeBean() {
		safeAccessFailureMode();
		return failureMode;
	}
	
	// *****************************************************************
	// * Attribute: failureCause
	// *****************************************************************
	private BeanPropertyReference<FaultEvent> failureCause = new BeanPropertyReference<>();
	
	private void safeAccessFailureCause() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("failureCause");
		failureCause.setTypeInstance(propertyInstance);
	}
	
	public FaultEvent getFailureCause() {
		safeAccessFailureCause();
		return failureCause.getValue();
	}
	
	public Command setFailureCause(EditingDomain ed, FaultEvent value) {
		safeAccessFailureCause();
		return failureCause.setValue(ed, value);
	}
	
	public void setFailureCause(FaultEvent value) {
		safeAccessFailureCause();
		failureCause.setValue(value);
	}
	
	public BeanPropertyReference<FaultEvent> getFailureCauseBean() {
		safeAccessFailureCause();
		return failureCause;
	}
	
	// *****************************************************************
	// * Array Attribute: failureEffects
	// *****************************************************************
		private IBeanList<Fault> failureEffects = new TypeSafeReferencePropertyInstanceList<>(Fault.class);
	
		private void safeAccessFailureEffects() {
			if (failureEffects.getArrayInstance() == null) {
				failureEffects.setArrayInstance((ArrayInstance) helper.getPropertyInstance("failureEffects"));
			}
		}
	
		public IBeanList<Fault> getFailureEffects() {
			safeAccessFailureEffects();
			return failureEffects;
		}
		
		private IBeanList<BeanPropertyReference<Fault>> failureEffectsBean = new TypeSafeReferencePropertyBeanList<>();
		
		private void safeAccessFailureEffectsBean() {
			if (failureEffectsBean.getArrayInstance() == null) {
				failureEffectsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("failureEffects"));
			}
		}
		
		public IBeanList<BeanPropertyReference<Fault>> getFailureEffectsBean() {
			safeAccessFailureEffectsBean();
			return failureEffectsBean;
		}
	
	// *****************************************************************
	// * Attribute: severity
	// *****************************************************************
	private BeanPropertyEnum severity = new BeanPropertyEnum();
	
	private void safeAccessSeverity() {
		if (severity.getTypeInstance() == null) {
			severity.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("severity"));
		}
	}
	
	public Command setSeverity(EditingDomain ed, String value) {
		safeAccessSeverity();
		return this.severity.setValue(ed, value);
	}
	
	public void setSeverity(String value) {
		safeAccessSeverity();
		this.severity.setValue(value);
	}
	
	public String getSeverity() {
		safeAccessSeverity();
		return severity.getValue();
	}
	
	public double getSeverityEnum() {
		safeAccessSeverity();
		return severity.getEnumValue();
	}
	
	public BeanPropertyEnum getSeverityBean() {
		safeAccessSeverity();
		return severity;
	}
	
	// *****************************************************************
	// * Attribute: probability
	// *****************************************************************
	private BeanPropertyEnum probability = new BeanPropertyEnum();
	
	private void safeAccessProbability() {
		if (probability.getTypeInstance() == null) {
			probability.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("probability"));
		}
	}
	
	public Command setProbability(EditingDomain ed, String value) {
		safeAccessProbability();
		return this.probability.setValue(ed, value);
	}
	
	public void setProbability(String value) {
		safeAccessProbability();
		this.probability.setValue(value);
	}
	
	public String getProbability() {
		safeAccessProbability();
		return probability.getValue();
	}
	
	public double getProbabilityEnum() {
		safeAccessProbability();
		return probability.getEnumValue();
	}
	
	public BeanPropertyEnum getProbabilityBean() {
		safeAccessProbability();
		return probability;
	}
	
	// *****************************************************************
	// * Attribute: criticality
	// *****************************************************************
	private BeanPropertyFloat criticality = new BeanPropertyFloat();
	
	private void safeAccessCriticality() {
		if (criticality.getTypeInstance() == null) {
			criticality.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("criticality"));
		}
	}
	
	public Command setCriticality(EditingDomain ed, double value) {
		safeAccessCriticality();
		return this.criticality.setValue(ed, value);
	}
	
	public void setCriticality(double value) {
		safeAccessCriticality();
		this.criticality.setValue(value);
	}
	
	public double getCriticality() {
		safeAccessCriticality();
		return criticality.getValue();
	}
	
	public boolean isSetCriticality() {
		safeAccessCriticality();
		return criticality.isSet();
	}
	
	public BeanPropertyFloat getCriticalityBean() {
		safeAccessCriticality();
		return criticality;
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
	// * Array Attribute: compensation
	// *****************************************************************
	private IBeanList<BeanPropertyString> compensationBean = new TypeSafeArrayInstanceList<>(BeanPropertyString.class);
	
	private void safeAccessCompensationBean() {
		if (compensationBean.getArrayInstance() == null) {
			compensationBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("compensation"));
		}
	}
	
	public IBeanList<BeanPropertyString> getCompensationBean() {
		safeAccessCompensationBean();
		return compensationBean;
	}
	
	
}
