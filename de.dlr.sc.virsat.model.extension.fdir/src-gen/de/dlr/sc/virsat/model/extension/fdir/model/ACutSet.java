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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyEnum;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyReference;
import de.dlr.sc.virsat.model.concept.list.TypeSafeReferencePropertyInstanceList;
import org.eclipse.emf.edit.domain.EditingDomain;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.json.ABeanObjectAdapter;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;
import javax.xml.bind.annotation.XmlAccessorType;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import de.dlr.sc.virsat.model.concept.list.TypeSafeReferencePropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import javax.xml.bind.annotation.XmlAccessType;
import org.eclipse.emf.common.command.Command;
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
public abstract class ACutSet extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.CutSet";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_FAILURE = "failure";
	public static final String PROPERTY_BASICEVENTS = "basicEvents";
	public static final String PROPERTY_SEVERITY = "severity";
	public static final String PROPERTY_PROBABILITY = "probability";
	public static final String PROPERTY_DETECTION = "detection";
	public static final String PROPERTY_CRITICALITY = "criticality";
	public static final String PROPERTY_MEANTIMETOFAILURE = "meanTimeToFailure";
	public static final String PROPERTY_STEADYSTATEDETECTABILITY = "steadyStateDetectability";
	public static final String PROPERTY_MEANTIMETODETECTION = "meanTimeToDetection";
	
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
	// Detection enumeration value names
	public static final String DETECTION_Unknown_NAME = "Unknown";
	public static final String DETECTION_ExtremelyUnlikely_NAME = "ExtremelyUnlikely";
	public static final String DETECTION_Unlikely_NAME = "Unlikely";
	public static final String DETECTION_Likely_NAME = "Likely";
	public static final String DETECTION_VeryLikely_NAME = "VeryLikely";
	// Detection enumeration values
	public static final String DETECTION_Unknown_VALUE = "0";
	public static final String DETECTION_ExtremelyUnlikely_VALUE = "4";
	public static final String DETECTION_Unlikely_VALUE = "3";
	public static final String DETECTION_Likely_VALUE = "2";
	public static final String DETECTION_VeryLikely_VALUE = "1";
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ACutSet() {
	}
	
	public ACutSet(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "CutSet");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "CutSet");
		setTypeInstance(categoryAssignement);
	}
	
	public ACutSet(CategoryAssignment categoryAssignement) {
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
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
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
	// * Array Attribute: basicEvents
	// *****************************************************************
		private IBeanList<BasicEvent> basicEvents = new TypeSafeReferencePropertyInstanceList<>(BasicEvent.class);
	
		private void safeAccessBasicEvents() {
			if (basicEvents.getArrayInstance() == null) {
				basicEvents.setArrayInstance((ArrayInstance) helper.getPropertyInstance("basicEvents"));
			}
		}
	
		public IBeanList<BasicEvent> getBasicEvents() {
			safeAccessBasicEvents();
			return basicEvents;
		}
		
		private IBeanList<BeanPropertyReference<BasicEvent>> basicEventsBean = new TypeSafeReferencePropertyBeanList<>();
		
		private void safeAccessBasicEventsBean() {
			if (basicEventsBean.getArrayInstance() == null) {
				basicEventsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("basicEvents"));
			}
		}
		
		@XmlElement
		public IBeanList<BeanPropertyReference<BasicEvent>> getBasicEventsBean() {
			safeAccessBasicEventsBean();
			return basicEventsBean;
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
	
	@XmlElement
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
	
	@XmlElement
	public BeanPropertyEnum getProbabilityBean() {
		safeAccessProbability();
		return probability;
	}
	
	// *****************************************************************
	// * Attribute: detection
	// *****************************************************************
	private BeanPropertyEnum detection = new BeanPropertyEnum();
	
	private void safeAccessDetection() {
		if (detection.getTypeInstance() == null) {
			detection.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("detection"));
		}
	}
	
	public Command setDetection(EditingDomain ed, String value) {
		safeAccessDetection();
		return this.detection.setValue(ed, value);
	}
	
	public void setDetection(String value) {
		safeAccessDetection();
		this.detection.setValue(value);
	}
	
	public String getDetection() {
		safeAccessDetection();
		return detection.getValue();
	}
	
	public double getDetectionEnum() {
		safeAccessDetection();
		return detection.getEnumValue();
	}
	
	@XmlElement
	public BeanPropertyEnum getDetectionBean() {
		safeAccessDetection();
		return detection;
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
	
	@XmlElement
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
	
	@XmlElement
	public BeanPropertyFloat getMeanTimeToFailureBean() {
		safeAccessMeanTimeToFailure();
		return meanTimeToFailure;
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
	
	@XmlElement
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
	
	@XmlElement
	public BeanPropertyFloat getMeanTimeToDetectionBean() {
		safeAccessMeanTimeToDetection();
		return meanTimeToDetection;
	}
	
	
}
