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
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyReference;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import org.eclipse.emf.edit.domain.EditingDomain;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;


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
public abstract class AFreeAction extends RecoveryAction implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.FreeAction";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_FREESPARE = "freeSpare";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFreeAction() {
	}
	
	public AFreeAction(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "FreeAction");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "FreeAction");
		setTypeInstance(categoryAssignement);
	}
	
	public AFreeAction(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: freeSpare
	// *****************************************************************
	private BeanPropertyReference<FaultTreeNode> freeSpare = new BeanPropertyReference<>();
	
	private void safeAccessFreeSpare() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("freeSpare");
		freeSpare.setTypeInstance(propertyInstance);
	}
	
	public FaultTreeNode getFreeSpare() {
		safeAccessFreeSpare();
		return freeSpare.getValue();
	}
	
	public Command setFreeSpare(EditingDomain ed, FaultTreeNode value) {
		safeAccessFreeSpare();
		return freeSpare.setValue(ed, value);
	}
	
	public void setFreeSpare(FaultTreeNode value) {
		safeAccessFreeSpare();
		freeSpare.setValue(value);
	}
	
	public BeanPropertyReference<FaultTreeNode> getFreeSpareBean() {
		safeAccessFreeSpare();
		return freeSpare;
	}
	
	
}
