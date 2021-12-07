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
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyComposed;
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
public abstract class AFaultTree extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.FaultTree";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_GATES = "gates";
	public static final String PROPERTY_PROPAGATIONS = "propagations";
	public static final String PROPERTY_SPARES = "spares";
	public static final String PROPERTY_DEPS = "deps";
	public static final String PROPERTY_OBSERVATIONS = "observations";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFaultTree() {
	}
	
	public AFaultTree(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "FaultTree");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "FaultTree");
		setTypeInstance(categoryAssignement);
	}
	
	public AFaultTree(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Array Attribute: gates
	// *****************************************************************
	private IBeanList<Gate> gates = new TypeSafeComposedPropertyInstanceList<>(Gate.class);
	
	private void safeAccessGates() {
		if (gates.getArrayInstance() == null) {
			gates.setArrayInstance((ArrayInstance) helper.getPropertyInstance("gates"));
		}
	}
	
	public IBeanList<Gate> getGates() {
		safeAccessGates();
		return gates;
	}
	
	private IBeanList<BeanPropertyComposed<Gate>> gatesBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessGatesBean() {
		if (gatesBean.getArrayInstance() == null) {
			gatesBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("gates"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<Gate>> getGatesBean() {
		safeAccessGatesBean();
		return gatesBean;
	}
	
	// *****************************************************************
	// * Array Attribute: propagations
	// *****************************************************************
	private IBeanList<FaultTreeEdge> propagations = new TypeSafeComposedPropertyInstanceList<>(FaultTreeEdge.class);
	
	private void safeAccessPropagations() {
		if (propagations.getArrayInstance() == null) {
			propagations.setArrayInstance((ArrayInstance) helper.getPropertyInstance("propagations"));
		}
	}
	
	public IBeanList<FaultTreeEdge> getPropagations() {
		safeAccessPropagations();
		return propagations;
	}
	
	private IBeanList<BeanPropertyComposed<FaultTreeEdge>> propagationsBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessPropagationsBean() {
		if (propagationsBean.getArrayInstance() == null) {
			propagationsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("propagations"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<FaultTreeEdge>> getPropagationsBean() {
		safeAccessPropagationsBean();
		return propagationsBean;
	}
	
	// *****************************************************************
	// * Array Attribute: spares
	// *****************************************************************
	private IBeanList<FaultTreeEdge> spares = new TypeSafeComposedPropertyInstanceList<>(FaultTreeEdge.class);
	
	private void safeAccessSpares() {
		if (spares.getArrayInstance() == null) {
			spares.setArrayInstance((ArrayInstance) helper.getPropertyInstance("spares"));
		}
	}
	
	public IBeanList<FaultTreeEdge> getSpares() {
		safeAccessSpares();
		return spares;
	}
	
	private IBeanList<BeanPropertyComposed<FaultTreeEdge>> sparesBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessSparesBean() {
		if (sparesBean.getArrayInstance() == null) {
			sparesBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("spares"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<FaultTreeEdge>> getSparesBean() {
		safeAccessSparesBean();
		return sparesBean;
	}
	
	// *****************************************************************
	// * Array Attribute: deps
	// *****************************************************************
	private IBeanList<FaultTreeEdge> deps = new TypeSafeComposedPropertyInstanceList<>(FaultTreeEdge.class);
	
	private void safeAccessDeps() {
		if (deps.getArrayInstance() == null) {
			deps.setArrayInstance((ArrayInstance) helper.getPropertyInstance("deps"));
		}
	}
	
	public IBeanList<FaultTreeEdge> getDeps() {
		safeAccessDeps();
		return deps;
	}
	
	private IBeanList<BeanPropertyComposed<FaultTreeEdge>> depsBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessDepsBean() {
		if (depsBean.getArrayInstance() == null) {
			depsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("deps"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<FaultTreeEdge>> getDepsBean() {
		safeAccessDepsBean();
		return depsBean;
	}
	
	// *****************************************************************
	// * Array Attribute: observations
	// *****************************************************************
	private IBeanList<FaultTreeEdge> observations = new TypeSafeComposedPropertyInstanceList<>(FaultTreeEdge.class);
	
	private void safeAccessObservations() {
		if (observations.getArrayInstance() == null) {
			observations.setArrayInstance((ArrayInstance) helper.getPropertyInstance("observations"));
		}
	}
	
	public IBeanList<FaultTreeEdge> getObservations() {
		safeAccessObservations();
		return observations;
	}
	
	private IBeanList<BeanPropertyComposed<FaultTreeEdge>> observationsBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessObservationsBean() {
		if (observationsBean.getArrayInstance() == null) {
			observationsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("observations"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<FaultTreeEdge>> getObservationsBean() {
		safeAccessObservationsBean();
		return observationsBean;
	}
	
	
}
