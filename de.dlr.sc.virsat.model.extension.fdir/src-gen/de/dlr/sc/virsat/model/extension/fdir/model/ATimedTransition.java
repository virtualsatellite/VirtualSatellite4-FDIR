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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;


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
public abstract class ATimedTransition extends Transition implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.TimedTransition";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_TIME = "time";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ATimedTransition() {
	}
	
	public ATimedTransition(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "TimedTransition");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "TimedTransition");
		setTypeInstance(categoryAssignement);
	}
	
	public ATimedTransition(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: time
	// *****************************************************************
	private BeanPropertyFloat time = new BeanPropertyFloat();
	
	private void safeAccessTime() {
		if (time.getTypeInstance() == null) {
			time.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("time"));
		}
	}
	
	public Command setTime(EditingDomain ed, double value) {
		safeAccessTime();
		return this.time.setValue(ed, value);
	}
	
	public void setTime(double value) {
		safeAccessTime();
		this.time.setValue(value);
	}
	
	public double getTime() {
		safeAccessTime();
		return time.getValue();
	}
	
	public boolean isSetTime() {
		safeAccessTime();
		return time.isSet();
	}
	
	public BeanPropertyFloat getTimeBean() {
		safeAccessTime();
		return time;
	}
	
	
}
