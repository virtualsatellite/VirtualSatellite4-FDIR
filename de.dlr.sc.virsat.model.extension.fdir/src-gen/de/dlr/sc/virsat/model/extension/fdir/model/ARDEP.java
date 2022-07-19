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
public abstract class ARDEP extends ADEP implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.RDEP";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_RATECHANGE = "rateChange";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public ARDEP() {
	}
	
	public ARDEP(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "RDEP");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "RDEP");
		setTypeInstance(categoryAssignement);
	}
	
	public ARDEP(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: rateChange
	// *****************************************************************
	private BeanPropertyFloat rateChange = new BeanPropertyFloat();
	
	private void safeAccessRateChange() {
		if (rateChange.getTypeInstance() == null) {
			rateChange.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("rateChange"));
		}
	}
	
	public Command setRateChange(EditingDomain ed, double value) {
		safeAccessRateChange();
		return this.rateChange.setValue(ed, value);
	}
	
	public void setRateChange(double value) {
		safeAccessRateChange();
		this.rateChange.setValue(value);
	}
	
	public double getRateChange() {
		safeAccessRateChange();
		return rateChange.getValue();
	}
	
	public boolean isSetRateChange() {
		safeAccessRateChange();
		return rateChange.isSet();
	}
	
	@XmlElement
	public BeanPropertyFloat getRateChangeBean() {
		safeAccessRateChange();
		return rateChange;
	}
	
	
}
