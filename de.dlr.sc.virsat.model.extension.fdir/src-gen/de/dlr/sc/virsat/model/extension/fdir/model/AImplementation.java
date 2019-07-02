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
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.list.TypeSafeReferencePropertyInstanceList;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.category.ABeanCategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyInt;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;


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
public abstract class AImplementation extends ABeanCategoryAssignment implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.Implementation";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_NUMREQUIREDWORKING = "numRequiredWorking";
	public static final String PROPERTY_REQUIREDWORKING = "requiredWorking";
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AImplementation() {
	}
	
	public AImplementation(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "Implementation");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "Implementation");
		setTypeInstance(categoryAssignement);
	}
	
	public AImplementation(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: numRequiredWorking
	// *****************************************************************
	private BeanPropertyInt numRequiredWorking = new BeanPropertyInt();
	
	private void safeAccessNumRequiredWorking() {
		if (numRequiredWorking.getTypeInstance() == null) {
			numRequiredWorking.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("numRequiredWorking"));
		}
	}
	
	public Command setNumRequiredWorking(EditingDomain ed, long value) {
		safeAccessNumRequiredWorking();
		return this.numRequiredWorking.setValue(ed, value);
	}
	
	public void setNumRequiredWorking(long value) {
		safeAccessNumRequiredWorking();
		this.numRequiredWorking.setValue(value);
	}
	
	public long getNumRequiredWorking() {
		safeAccessNumRequiredWorking();
		return numRequiredWorking.getValue();
	}
	
	public boolean isSetNumRequiredWorking() {
		safeAccessNumRequiredWorking();
		return numRequiredWorking.isSet();
	}
	
	public BeanPropertyInt getNumRequiredWorkingBean() {
		safeAccessNumRequiredWorking();
		return numRequiredWorking;
	}
	
	// *****************************************************************
	// * Array Attribute: requiredWorking
	// *****************************************************************
		private IBeanList<Fault> requiredWorking = new TypeSafeReferencePropertyInstanceList<>(Fault.class);
	
		private void safeAccessRequiredWorking() {
			if (requiredWorking.getArrayInstance() == null) {
				requiredWorking.setArrayInstance((ArrayInstance) helper.getPropertyInstance("requiredWorking"));
			}
		}
	
		public IBeanList<Fault> getRequiredWorking() {
			safeAccessRequiredWorking();
			return requiredWorking;
		}
	
	
}
