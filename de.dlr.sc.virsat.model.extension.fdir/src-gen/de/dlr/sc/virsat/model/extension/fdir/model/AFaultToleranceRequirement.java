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
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import org.eclipse.emf.edit.domain.EditingDomain;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRRequirement;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyInt;


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
public abstract class AFaultToleranceRequirement extends FDIRRequirement implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.FaultToleranceRequirement";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_MINFAULTTOLERANCE = "minFaultTolerance";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFaultToleranceRequirement() {
	}
	
	public AFaultToleranceRequirement(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "FaultToleranceRequirement");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "FaultToleranceRequirement");
		setTypeInstance(categoryAssignement);
	}
	
	public AFaultToleranceRequirement(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: minFaultTolerance
	// *****************************************************************
	private BeanPropertyInt minFaultTolerance = new BeanPropertyInt();
	
	private void safeAccessMinFaultTolerance() {
		if (minFaultTolerance.getTypeInstance() == null) {
			minFaultTolerance.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("minFaultTolerance"));
		}
	}
	
	public Command setMinFaultTolerance(EditingDomain ed, long value) {
		safeAccessMinFaultTolerance();
		return this.minFaultTolerance.setValue(ed, value);
	}
	
	public void setMinFaultTolerance(long value) {
		safeAccessMinFaultTolerance();
		this.minFaultTolerance.setValue(value);
	}
	
	public long getMinFaultTolerance() {
		safeAccessMinFaultTolerance();
		return minFaultTolerance.getValue();
	}
	
	public boolean isSetMinFaultTolerance() {
		safeAccessMinFaultTolerance();
		return minFaultTolerance.isSet();
	}
	
	public BeanPropertyInt getMinFaultToleranceBean() {
		safeAccessMinFaultTolerance();
		return minFaultTolerance;
	}
	
	
}
