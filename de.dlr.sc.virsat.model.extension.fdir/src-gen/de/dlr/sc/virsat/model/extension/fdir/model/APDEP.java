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
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
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
public abstract class APDEP extends ADEP implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.PDEP";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_PROBABILITY = "probability";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public APDEP() {
	}
	
	public APDEP(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "PDEP");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "PDEP");
		setTypeInstance(categoryAssignement);
	}
	
	public APDEP(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: probability
	// *****************************************************************
	private BeanPropertyFloat probability = new BeanPropertyFloat();
	
	private void safeAccessProbability() {
		if (probability.getTypeInstance() == null) {
			probability.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("probability"));
		}
	}
	
	public Command setProbability(EditingDomain ed, double value) {
		safeAccessProbability();
		return this.probability.setValue(ed, value);
	}
	
	public void setProbability(double value) {
		safeAccessProbability();
		this.probability.setValue(value);
	}
	
	public double getProbability() {
		safeAccessProbability();
		return probability.getValue();
	}
	
	public boolean isSetProbability() {
		safeAccessProbability();
		return probability.isSet();
	}
	
	@XmlElement
	public BeanPropertyFloat getProbabilityBean() {
		safeAccessProbability();
		return probability;
	}
	
	
}
