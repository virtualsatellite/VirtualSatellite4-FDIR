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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyEnum;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyString;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyComposed;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ComposedPropertyInstance;
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
	public static final String PROPERTY_SEVERITY = "severity";
	public static final String PROPERTY_FAULTTREE = "faultTree";
	public static final String PROPERTY_RELIABILITYANALYSIS = "reliabilityAnalysis";
	public static final String PROPERTY_AVAILABILITYANALYSIS = "availabilityAnalysis";
	public static final String PROPERTY_DETECTABILITYANALYSIS = "detectabilityAnalysis";
	
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
	
	@XmlElement
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
	
	private IBeanList<BeanPropertyComposed<BasicEvent>> basicEventsBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessBasicEventsBean() {
		if (basicEventsBean.getArrayInstance() == null) {
			basicEventsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("basicEvents"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<BasicEvent>> getBasicEventsBean() {
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
	// * Attribute: faultTree
	// *****************************************************************
	private BeanPropertyComposed<FaultTree> faultTree = new BeanPropertyComposed<>();
	
	private void safeAccessFaultTree() {
		if (faultTree.getTypeInstance() == null) {
			ComposedPropertyInstance propertyInstance = (ComposedPropertyInstance) helper.getPropertyInstance("faultTree");
			faultTree.setTypeInstance(propertyInstance);
		}
	}
	
	@XmlElement(nillable = true)
	public FaultTree getFaultTree() {
		safeAccessFaultTree();
		return faultTree.getValue();
	}
	
	public BeanPropertyComposed<FaultTree> getFaultTreeBean() {
		safeAccessFaultTree();
		return faultTree;
	}
	
	// *****************************************************************
	// * Array Attribute: reliabilityAnalysis
	// *****************************************************************
	private IBeanList<ReliabilityAnalysis> reliabilityAnalysis = new TypeSafeComposedPropertyInstanceList<>(ReliabilityAnalysis.class);
	
	private void safeAccessReliabilityAnalysis() {
		if (reliabilityAnalysis.getArrayInstance() == null) {
			reliabilityAnalysis.setArrayInstance((ArrayInstance) helper.getPropertyInstance("reliabilityAnalysis"));
		}
	}
	
	public IBeanList<ReliabilityAnalysis> getReliabilityAnalysis() {
		safeAccessReliabilityAnalysis();
		return reliabilityAnalysis;
	}
	
	private IBeanList<BeanPropertyComposed<ReliabilityAnalysis>> reliabilityAnalysisBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessReliabilityAnalysisBean() {
		if (reliabilityAnalysisBean.getArrayInstance() == null) {
			reliabilityAnalysisBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("reliabilityAnalysis"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<ReliabilityAnalysis>> getReliabilityAnalysisBean() {
		safeAccessReliabilityAnalysisBean();
		return reliabilityAnalysisBean;
	}
	
	// *****************************************************************
	// * Array Attribute: availabilityAnalysis
	// *****************************************************************
	private IBeanList<AvailabilityAnalysis> availabilityAnalysis = new TypeSafeComposedPropertyInstanceList<>(AvailabilityAnalysis.class);
	
	private void safeAccessAvailabilityAnalysis() {
		if (availabilityAnalysis.getArrayInstance() == null) {
			availabilityAnalysis.setArrayInstance((ArrayInstance) helper.getPropertyInstance("availabilityAnalysis"));
		}
	}
	
	public IBeanList<AvailabilityAnalysis> getAvailabilityAnalysis() {
		safeAccessAvailabilityAnalysis();
		return availabilityAnalysis;
	}
	
	private IBeanList<BeanPropertyComposed<AvailabilityAnalysis>> availabilityAnalysisBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessAvailabilityAnalysisBean() {
		if (availabilityAnalysisBean.getArrayInstance() == null) {
			availabilityAnalysisBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("availabilityAnalysis"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<AvailabilityAnalysis>> getAvailabilityAnalysisBean() {
		safeAccessAvailabilityAnalysisBean();
		return availabilityAnalysisBean;
	}
	
	// *****************************************************************
	// * Array Attribute: detectabilityAnalysis
	// *****************************************************************
	private IBeanList<DetectabilityAnalysis> detectabilityAnalysis = new TypeSafeComposedPropertyInstanceList<>(DetectabilityAnalysis.class);
	
	private void safeAccessDetectabilityAnalysis() {
		if (detectabilityAnalysis.getArrayInstance() == null) {
			detectabilityAnalysis.setArrayInstance((ArrayInstance) helper.getPropertyInstance("detectabilityAnalysis"));
		}
	}
	
	public IBeanList<DetectabilityAnalysis> getDetectabilityAnalysis() {
		safeAccessDetectabilityAnalysis();
		return detectabilityAnalysis;
	}
	
	private IBeanList<BeanPropertyComposed<DetectabilityAnalysis>> detectabilityAnalysisBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessDetectabilityAnalysisBean() {
		if (detectabilityAnalysisBean.getArrayInstance() == null) {
			detectabilityAnalysisBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("detectabilityAnalysis"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<DetectabilityAnalysis>> getDetectabilityAnalysisBean() {
		safeAccessDetectabilityAnalysisBean();
		return detectabilityAnalysisBean;
	}
	
	
}
