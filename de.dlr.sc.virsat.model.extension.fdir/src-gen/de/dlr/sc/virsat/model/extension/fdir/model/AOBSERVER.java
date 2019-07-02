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
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import org.eclipse.emf.edit.domain.EditingDomain;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;


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
public abstract class AOBSERVER extends Gate implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.OBSERVER";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_OBSERVATIONRATE = "observationRate";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AOBSERVER() {
	}
	
	public AOBSERVER(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "OBSERVER");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "OBSERVER");
		setTypeInstance(categoryAssignement);
	}
	
	public AOBSERVER(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: observationRate
	// *****************************************************************
	private BeanPropertyFloat observationRate = new BeanPropertyFloat();
	
	private void safeAccessObservationRate() {
		if (observationRate.getTypeInstance() == null) {
			observationRate.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("observationRate"));
		}
	}
	
	public Command setObservationRate(EditingDomain ed, double value) {
		safeAccessObservationRate();
		return this.observationRate.setValue(ed, value);
	}
	
	public void setObservationRate(double value) {
		safeAccessObservationRate();
		this.observationRate.setValue(value);
	}
	
	public double getObservationRate() {
		safeAccessObservationRate();
		return observationRate.getValue();
	}
	
	public boolean isSetObservationRate() {
		safeAccessObservationRate();
		return observationRate.isSet();
	}
	
	public BeanPropertyFloat getObservationRateBean() {
		safeAccessObservationRate();
		return observationRate;
	}
	
	
}
