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
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.EnumUnitPropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
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
public abstract class AMetricConstraint extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.MetricConstraint";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_METRIC = "metric";
	public static final String PROPERTY_VALUE = "value";
	
	// Metric enumeration value names
	public static final String METRIC_MeanTimeToFailure_NAME = "MeanTimeToFailure";
	public static final String METRIC_SteadyStateAvailability_NAME = "SteadyStateAvailability";
	public static final String METRIC_SteadyStateDetectability_NAME = "SteadyStateDetectability";
	public static final String METRIC_FaultTolerance_NAME = "FaultTolerance";
	public static final String METRIC_MeanTimeToDetection_NAME = "MeanTimeToDetection";
	// Metric enumeration values
	public static final String METRIC_MeanTimeToFailure_VALUE = "0";
	public static final String METRIC_SteadyStateAvailability_VALUE = "1";
	public static final String METRIC_SteadyStateDetectability_VALUE = "2";
	public static final String METRIC_FaultTolerance_VALUE = "3";
	public static final String METRIC_MeanTimeToDetection_VALUE = "4";
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AMetricConstraint() {
	}
	
	public AMetricConstraint(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "MetricConstraint");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "MetricConstraint");
		setTypeInstance(categoryAssignement);
	}
	
	public AMetricConstraint(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: metric
	// *****************************************************************
	private BeanPropertyEnum metric = new BeanPropertyEnum();
	
	private void safeAccessMetric() {
		if (metric.getTypeInstance() == null) {
			metric.setTypeInstance((EnumUnitPropertyInstance) helper.getPropertyInstance("metric"));
		}
	}
	
	public Command setMetric(EditingDomain ed, String value) {
		safeAccessMetric();
		return this.metric.setValue(ed, value);
	}
	
	public void setMetric(String value) {
		safeAccessMetric();
		this.metric.setValue(value);
	}
	
	public String getMetric() {
		safeAccessMetric();
		return metric.getValue();
	}
	
	public double getMetricEnum() {
		safeAccessMetric();
		return metric.getEnumValue();
	}
	
	public BeanPropertyEnum getMetricBean() {
		safeAccessMetric();
		return metric;
	}
	
	// *****************************************************************
	// * Attribute: value
	// *****************************************************************
	private BeanPropertyFloat value = new BeanPropertyFloat();
	
	private void safeAccessValue() {
		if (value.getTypeInstance() == null) {
			value.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("value"));
		}
	}
	
	public Command setValue(EditingDomain ed, double value) {
		safeAccessValue();
		return this.value.setValue(ed, value);
	}
	
	public void setValue(double value) {
		safeAccessValue();
		this.value.setValue(value);
	}
	
	public double getValue() {
		safeAccessValue();
		return value.getValue();
	}
	
	public boolean isSetValue() {
		safeAccessValue();
		return value.isSet();
	}
	
	public BeanPropertyFloat getValueBean() {
		safeAccessValue();
		return value;
	}
	
	
}
