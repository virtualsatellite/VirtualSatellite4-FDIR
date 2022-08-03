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
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.json.ABeanObjectAdapter;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
public abstract class AClaimAction extends RecoveryAction implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.ClaimAction";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_SPAREGATE = "spareGate";
	public static final String PROPERTY_CLAIMSPARE = "claimSpare";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AClaimAction() {
	}
	
	public AClaimAction(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "ClaimAction");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "ClaimAction");
		setTypeInstance(categoryAssignement);
	}
	
	public AClaimAction(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: spareGate
	// *****************************************************************
	private BeanPropertyReference<SPARE> spareGate = new BeanPropertyReference<>();
	
	private void safeAccessSpareGate() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("spareGate");
		spareGate.setTypeInstance(propertyInstance);
	}
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
	public SPARE getSpareGate() {
		safeAccessSpareGate();
		return spareGate.getValue();
	}
	
	public Command setSpareGate(EditingDomain ed, SPARE value) {
		safeAccessSpareGate();
		return spareGate.setValue(ed, value);
	}
	
	public void setSpareGate(SPARE value) {
		safeAccessSpareGate();
		spareGate.setValue(value);
	}
	
	public BeanPropertyReference<SPARE> getSpareGateBean() {
		safeAccessSpareGate();
		return spareGate;
	}
	
	// *****************************************************************
	// * Attribute: claimSpare
	// *****************************************************************
	private BeanPropertyReference<FaultTreeNode> claimSpare = new BeanPropertyReference<>();
	
	private void safeAccessClaimSpare() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("claimSpare");
		claimSpare.setTypeInstance(propertyInstance);
	}
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
	public FaultTreeNode getClaimSpare() {
		safeAccessClaimSpare();
		return claimSpare.getValue();
	}
	
	public Command setClaimSpare(EditingDomain ed, FaultTreeNode value) {
		safeAccessClaimSpare();
		return claimSpare.setValue(ed, value);
	}
	
	public void setClaimSpare(FaultTreeNode value) {
		safeAccessClaimSpare();
		claimSpare.setValue(value);
	}
	
	public BeanPropertyReference<FaultTreeNode> getClaimSpareBean() {
		safeAccessClaimSpare();
		return claimSpare;
	}
	
	
}
