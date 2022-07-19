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
import org.eclipse.emf.common.util.URI;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ResourcePropertyInstance;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyResource;
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;
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
public abstract class ARecoveryAutomatonGen extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.RecoveryAutomatonGen";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_OBJECTIVEMETRIC = "objectiveMetric";
	public static final String PROPERTY_LASTGENERATIONLOG = "lastGenerationLog";
	
	// ObjectiveMetric enumeration value names
	public static final String OBJECTIVEMETRIC_MeanTimeToFailure_NAME = "MeanTimeToFailure";
	public static final String OBJECTIVEMETRIC_SteadyStateAvailability_NAME = "SteadyStateAvailability";
	public static final String OBJECTIVEMETRIC_SteadyStateDetectability_NAME = "SteadyStateDetectability";
	// ObjectiveMetric enumeration values
	public static final String OBJECTIVEMETRIC_MeanTimeToFailure_VALUE = "0";
	public static final String OBJECTIVEMETRIC_SteadyStateAvailability_VALUE = "1";
	public static final String OBJECTIVEMETRIC_SteadyStateDetectability_VALUE = "2";
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ARecoveryAutomatonGen() {
	}
	
	public ARecoveryAutomatonGen(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "RecoveryAutomatonGen");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "RecoveryAutomatonGen");
		setTypeInstance(categoryAssignement);
	}
	
	public ARecoveryAutomatonGen(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: objectiveMetric
	// *****************************************************************
	private BeanPropertyEnum objectiveMetric = new BeanPropertyEnum();
	
	private void safeAccessObjectiveMetric() {
		if (objectiveMetric.getTypeInstance() == null) {
			objectiveMetric.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("objectiveMetric"));
		}
	}
	
	public Command setObjectiveMetric(EditingDomain ed, String value) {
		safeAccessObjectiveMetric();
		return this.objectiveMetric.setValue(ed, value);
	}
	
	public void setObjectiveMetric(String value) {
		safeAccessObjectiveMetric();
		this.objectiveMetric.setValue(value);
	}
	
	public String getObjectiveMetric() {
		safeAccessObjectiveMetric();
		return objectiveMetric.getValue();
	}
	
	public double getObjectiveMetricEnum() {
		safeAccessObjectiveMetric();
		return objectiveMetric.getEnumValue();
	}
	
	@XmlElement
	public BeanPropertyEnum getObjectiveMetricBean() {
		safeAccessObjectiveMetric();
		return objectiveMetric;
	}
	
	// *****************************************************************
	// * Attribute: lastGenerationLog
	// *****************************************************************
	private BeanPropertyResource lastGenerationLog = new BeanPropertyResource();
	
	private void safeAccessLastGenerationLog() {
		if (lastGenerationLog.getTypeInstance() == null) {
			lastGenerationLog.setTypeInstance((ResourcePropertyInstance) helper.getPropertyInstance("lastGenerationLog"));
		}
	}
	
	public Command setLastGenerationLog(EditingDomain ed, URI value) {
		safeAccessLastGenerationLog();
		return this.lastGenerationLog.setValue(ed, value);
	}
	
	public void setLastGenerationLog(URI value) {
		safeAccessLastGenerationLog();
		this.lastGenerationLog.setValue(value);
	}
	
	public URI getLastGenerationLog() {
		safeAccessLastGenerationLog();
		return lastGenerationLog.getValue();
	}
	
	@XmlElement
	public BeanPropertyResource getLastGenerationLogBean() {
		safeAccessLastGenerationLog();
		return lastGenerationLog;
	}
	
	
}
