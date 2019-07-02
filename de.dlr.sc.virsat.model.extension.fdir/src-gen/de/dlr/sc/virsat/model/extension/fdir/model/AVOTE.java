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
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
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
public abstract class AVOTE extends Gate implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.VOTE";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_VOTINGTHRESHOLD = "votingThreshold";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AVOTE() {
	}
	
	public AVOTE(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "VOTE");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "VOTE");
		setTypeInstance(categoryAssignement);
	}
	
	public AVOTE(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: votingThreshold
	// *****************************************************************
	private BeanPropertyInt votingThreshold = new BeanPropertyInt();
	
	private void safeAccessVotingThreshold() {
		if (votingThreshold.getTypeInstance() == null) {
			votingThreshold.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("votingThreshold"));
		}
	}
	
	public Command setVotingThreshold(EditingDomain ed, long value) {
		safeAccessVotingThreshold();
		return this.votingThreshold.setValue(ed, value);
	}
	
	public void setVotingThreshold(long value) {
		safeAccessVotingThreshold();
		this.votingThreshold.setValue(value);
	}
	
	public long getVotingThreshold() {
		safeAccessVotingThreshold();
		return votingThreshold.getValue();
	}
	
	public boolean isSetVotingThreshold() {
		safeAccessVotingThreshold();
		return votingThreshold.isSet();
	}
	
	public BeanPropertyInt getVotingThresholdBean() {
		safeAccessVotingThreshold();
		return votingThreshold;
	}
	
	
}
