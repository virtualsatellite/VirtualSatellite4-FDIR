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
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;
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
public abstract class AAbstractFaultTreeEdge extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.AbstractFaultTreeEdge";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_FROM = "from";
	public static final String PROPERTY_TO = "to";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AAbstractFaultTreeEdge() {
	}
	
	public AAbstractFaultTreeEdge(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "AbstractFaultTreeEdge");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "AbstractFaultTreeEdge");
		setTypeInstance(categoryAssignement);
	}
	
	public AAbstractFaultTreeEdge(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: from
	// *****************************************************************
	private BeanPropertyReference<FaultTreeNode> from = new BeanPropertyReference<>();
	
	private void safeAccessFrom() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("from");
		from.setTypeInstance(propertyInstance);
	}
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
	public FaultTreeNode getFrom() {
		safeAccessFrom();
		return from.getValue();
	}
	
	public Command setFrom(EditingDomain ed, FaultTreeNode value) {
		safeAccessFrom();
		return from.setValue(ed, value);
	}
	
	public void setFrom(FaultTreeNode value) {
		safeAccessFrom();
		from.setValue(value);
	}
	
	public BeanPropertyReference<FaultTreeNode> getFromBean() {
		safeAccessFrom();
		return from;
	}
	
	// *****************************************************************
	// * Attribute: to
	// *****************************************************************
	private BeanPropertyReference<FaultTreeNode> to = new BeanPropertyReference<>();
	
	private void safeAccessTo() {
		ReferencePropertyInstance propertyInstance = (ReferencePropertyInstance) helper.getPropertyInstance("to");
		to.setTypeInstance(propertyInstance);
	}
	
	@XmlElement(nillable = true)
	@XmlJavaTypeAdapter(ABeanObjectAdapter.class)
	public FaultTreeNode getTo() {
		safeAccessTo();
		return to.getValue();
	}
	
	public Command setTo(EditingDomain ed, FaultTreeNode value) {
		safeAccessTo();
		return to.setValue(ed, value);
	}
	
	public void setTo(FaultTreeNode value) {
		safeAccessTo();
		to.setValue(value);
	}
	
	public BeanPropertyReference<FaultTreeNode> getToBean() {
		safeAccessTo();
		return to;
	}
	
	
}
