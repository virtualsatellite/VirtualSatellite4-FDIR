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
import de.dlr.sc.virsat.model.extension.fdir.model.MetricConstraint;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
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
	public static final String PROPERTY_CONSTRAINTS = "constraints";
	
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
	
	public BeanPropertyEnum getObjectiveMetricBean() {
		safeAccessObjectiveMetric();
		return objectiveMetric;
	}
	
	// *****************************************************************
	// * Array Attribute: constraints
	// *****************************************************************
	private IBeanList<MetricConstraint> constraints = new TypeSafeComposedPropertyInstanceList<>(MetricConstraint.class);
	
	private void safeAccessConstraints() {
		if (constraints.getArrayInstance() == null) {
			constraints.setArrayInstance((ArrayInstance) helper.getPropertyInstance("constraints"));
		}
	}
	
	public IBeanList<MetricConstraint> getConstraints() {
		safeAccessConstraints();
		return constraints;
	}
	
	
}
