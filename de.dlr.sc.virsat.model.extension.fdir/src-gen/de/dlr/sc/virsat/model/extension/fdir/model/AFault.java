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
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTree;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyString;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEvent;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ComposedPropertyInstance;


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
public abstract class AFault extends FaultEvent implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.Fault";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_DETAIL = "detail";
	public static final String PROPERTY_BASICEVENTS = "basicEvents";
	public static final String PROPERTY_MEANTIMETOFAILURE = "meanTimeToFailure";
	public static final String PROPERTY_SEVERITYLEVEL = "severityLevel";
	public static final String PROPERTY_PROBABILITYLEVEL = "probabilityLevel";
	public static final String PROPERTY_DETECTIONLEVEL = "detectionLevel";
	public static final String PROPERTY_CRITICALITYLEVEL = "criticalityLevel";
	public static final String PROPERTY_FAILURERATE = "failureRate";
	public static final String PROPERTY_FAULTTREE = "faultTree";
	
	// SeverityLevel enumeration value names
	public static final String SEVERITYLEVEL_Catastrophic_NAME = "Catastrophic";
	public static final String SEVERITYLEVEL_Critical_NAME = "Critical";
	public static final String SEVERITYLEVEL_Major_NAME = "Major";
	public static final String SEVERITYLEVEL_Minor_NAME = "Minor";
	// SeverityLevel enumeration values
	public static final String SEVERITYLEVEL_Catastrophic_VALUE = "4";
	public static final String SEVERITYLEVEL_Critical_VALUE = "3";
	public static final String SEVERITYLEVEL_Major_VALUE = "2";
	public static final String SEVERITYLEVEL_Minor_VALUE = "1";
	// ProbabilityLevel enumeration value names
	public static final String PROBABILITYLEVEL_ExtremelyRemote_NAME = "ExtremelyRemote";
	public static final String PROBABILITYLEVEL_Remote_NAME = "Remote";
	public static final String PROBABILITYLEVEL_Occasional_NAME = "Occasional";
	public static final String PROBABILITYLEVEL_Probable_NAME = "Probable";
	// ProbabilityLevel enumeration values
	public static final String PROBABILITYLEVEL_ExtremelyRemote_VALUE = "1";
	public static final String PROBABILITYLEVEL_Remote_VALUE = "2";
	public static final String PROBABILITYLEVEL_Occasional_VALUE = "3";
	public static final String PROBABILITYLEVEL_Probable_VALUE = "4";
	// DetectionLevel enumeration value names
	public static final String DETECTIONLEVEL_ExtremelyUnlikely_NAME = "ExtremelyUnlikely";
	public static final String DETECTIONLEVEL_Unlikely_NAME = "Unlikely";
	public static final String DETECTIONLEVEL_Likely_NAME = "Likely";
	public static final String DETECTIONLEVEL_VeryLikely_NAME = "VeryLikely";
	// DetectionLevel enumeration values
	public static final String DETECTIONLEVEL_ExtremelyUnlikely_VALUE = "4";
	public static final String DETECTIONLEVEL_Unlikely_VALUE = "3";
	public static final String DETECTIONLEVEL_Likely_VALUE = "2";
	public static final String DETECTIONLEVEL_VeryLikely_VALUE = "1";
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFault() {
	}
	
	public AFault(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "Fault");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "Fault");
		setTypeInstance(categoryAssignement);
	}
	
	public AFault(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: detail
	// *****************************************************************
	private BeanPropertyString detail = new BeanPropertyString();
	
	private void safeAccessDetail() {
		if (detail.getTypeInstance() == null) {
			detail.setTypeInstance((ValuePropertyInstance) helper.getPropertyInstance("detail"));
		}
	}
	
	public Command setDetail(EditingDomain ed, String value) {
		safeAccessDetail();
		return this.detail.setValue(ed, value);
	}
	
	public void setDetail(String value) {
		safeAccessDetail();
		this.detail.setValue(value);
	}
	
	public String getDetail() {
		safeAccessDetail();
		return detail.getValue();
	}
	
	public BeanPropertyString getDetailBean() {
		safeAccessDetail();
		return detail;
	}
	
	// *****************************************************************
	// * Array Attribute: basicEvents
	// *****************************************************************
	private IBeanList<BasicEvent> basicEvents = new TypeSafeComposedPropertyInstanceList<>(BasicEvent.class);
	
	private void safeAccessBasicEvents() {
		if (basicEvents.getArrayInstance() == null) {
			basicEvents.setArrayInstance((ArrayInstance) helper.getPropertyInstance("basicEvents"));
		}
	}
	
	public IBeanList<BasicEvent> getBasicEvents() {
		safeAccessBasicEvents();
		return basicEvents;
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
	// * Attribute: severityLevel
	// *****************************************************************
	private BeanPropertyEnum severityLevel = new BeanPropertyEnum();
	
	private void safeAccessSeverityLevel() {
		if (severityLevel.getTypeInstance() == null) {
			severityLevel.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("severityLevel"));
		}
	}
	
	public Command setSeverityLevel(EditingDomain ed, String value) {
		safeAccessSeverityLevel();
		return this.severityLevel.setValue(ed, value);
	}
	
	public void setSeverityLevel(String value) {
		safeAccessSeverityLevel();
		this.severityLevel.setValue(value);
	}
	
	public String getSeverityLevel() {
		safeAccessSeverityLevel();
		return severityLevel.getValue();
	}
	
	public double getSeverityLevelEnum() {
		safeAccessSeverityLevel();
		return severityLevel.getEnumValue();
	}
	
	public BeanPropertyEnum getSeverityLevelBean() {
		safeAccessSeverityLevel();
		return severityLevel;
	}
	
	// *****************************************************************
	// * Attribute: probabilityLevel
	// *****************************************************************
	private BeanPropertyEnum probabilityLevel = new BeanPropertyEnum();
	
	private void safeAccessProbabilityLevel() {
		if (probabilityLevel.getTypeInstance() == null) {
			probabilityLevel.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("probabilityLevel"));
		}
	}
	
	public Command setProbabilityLevel(EditingDomain ed, String value) {
		safeAccessProbabilityLevel();
		return this.probabilityLevel.setValue(ed, value);
	}
	
	public void setProbabilityLevel(String value) {
		safeAccessProbabilityLevel();
		this.probabilityLevel.setValue(value);
	}
	
	public String getProbabilityLevel() {
		safeAccessProbabilityLevel();
		return probabilityLevel.getValue();
	}
	
	public double getProbabilityLevelEnum() {
		safeAccessProbabilityLevel();
		return probabilityLevel.getEnumValue();
	}
	
	public BeanPropertyEnum getProbabilityLevelBean() {
		safeAccessProbabilityLevel();
		return probabilityLevel;
	}
	
	// *****************************************************************
	// * Attribute: detectionLevel
	// *****************************************************************
	private BeanPropertyEnum detectionLevel = new BeanPropertyEnum();
	
	private void safeAccessDetectionLevel() {
		if (detectionLevel.getTypeInstance() == null) {
			detectionLevel.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("detectionLevel"));
		}
	}
	
	public Command setDetectionLevel(EditingDomain ed, String value) {
		safeAccessDetectionLevel();
		return this.detectionLevel.setValue(ed, value);
	}
	
	public void setDetectionLevel(String value) {
		safeAccessDetectionLevel();
		this.detectionLevel.setValue(value);
	}
	
	public String getDetectionLevel() {
		safeAccessDetectionLevel();
		return detectionLevel.getValue();
	}
	
	public double getDetectionLevelEnum() {
		safeAccessDetectionLevel();
		return detectionLevel.getEnumValue();
	}
	
	public BeanPropertyEnum getDetectionLevelBean() {
		safeAccessDetectionLevel();
		return detectionLevel;
	}
	
	// *****************************************************************
	// * Attribute: criticalityLevel
	// *****************************************************************
	private BeanPropertyFloat criticalityLevel = new BeanPropertyFloat();
	
	private void safeAccessCriticalityLevel() {
		if (criticalityLevel.getTypeInstance() == null) {
			criticalityLevel.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("criticalityLevel"));
		}
	}
	
	public Command setCriticalityLevel(EditingDomain ed, double value) {
		safeAccessCriticalityLevel();
		return this.criticalityLevel.setValue(ed, value);
	}
	
	public void setCriticalityLevel(double value) {
		safeAccessCriticalityLevel();
		this.criticalityLevel.setValue(value);
	}
	
	public double getCriticalityLevel() {
		safeAccessCriticalityLevel();
		return criticalityLevel.getValue();
	}
	
	public boolean isSetCriticalityLevel() {
		safeAccessCriticalityLevel();
		return criticalityLevel.isSet();
	}
	
	public BeanPropertyFloat getCriticalityLevelBean() {
		safeAccessCriticalityLevel();
		return criticalityLevel;
	}
	
	// *****************************************************************
	// * Attribute: failureRate
	// *****************************************************************
	private BeanPropertyFloat failureRate = new BeanPropertyFloat();
	
	private void safeAccessFailureRate() {
		if (failureRate.getTypeInstance() == null) {
			failureRate.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("failureRate"));
		}
	}
	
	public Command setFailureRate(EditingDomain ed, double value) {
		safeAccessFailureRate();
		return this.failureRate.setValue(ed, value);
	}
	
	public void setFailureRate(double value) {
		safeAccessFailureRate();
		this.failureRate.setValue(value);
	}
	
	public double getFailureRate() {
		safeAccessFailureRate();
		return failureRate.getValue();
	}
	
	public boolean isSetFailureRate() {
		safeAccessFailureRate();
		return failureRate.isSet();
	}
	
	public BeanPropertyFloat getFailureRateBean() {
		safeAccessFailureRate();
		return failureRate;
	}
	
	// *****************************************************************
	// * Attribute: faultTree
	// *****************************************************************
	private FaultTree faultTree = new FaultTree();
	
	private void safeAccessFaultTree() {
		if (faultTree.getTypeInstance() == null) {
			ComposedPropertyInstance propertyInstance = (ComposedPropertyInstance) helper.getPropertyInstance("faultTree");
			faultTree.setTypeInstance(propertyInstance.getTypeInstance());
		}
	}
	
	public FaultTree getFaultTree () {
		safeAccessFaultTree();
		return faultTree;
	}
	
	
}
