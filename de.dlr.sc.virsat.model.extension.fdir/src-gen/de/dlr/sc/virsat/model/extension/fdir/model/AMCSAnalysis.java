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
import de.dlr.sc.virsat.model.extension.fdir.model.CutSet;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyInstanceList;
import de.dlr.sc.virsat.model.ext.core.model.GenericCategory;
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
public abstract class AMCSAnalysis extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.MCSAnalysis";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_MAXMINIMUMCUTSETSIZE = "maxMinimumCutSetSize";
	public static final String PROPERTY_FAULTTOLERANCE = "faultTolerance";
	public static final String PROPERTY_MINIMUMCUTSETS = "minimumCutSets";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AMCSAnalysis() {
	}
	
	public AMCSAnalysis(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "MCSAnalysis");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "MCSAnalysis");
		setTypeInstance(categoryAssignement);
	}
	
	public AMCSAnalysis(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: maxMinimumCutSetSize
	// *****************************************************************
	private BeanPropertyInt maxMinimumCutSetSize = new BeanPropertyInt();
	
	private void safeAccessMaxMinimumCutSetSize() {
		if (maxMinimumCutSetSize.getTypeInstance() == null) {
			maxMinimumCutSetSize.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("maxMinimumCutSetSize"));
		}
	}
	
	public Command setMaxMinimumCutSetSize(EditingDomain ed, long value) {
		safeAccessMaxMinimumCutSetSize();
		return this.maxMinimumCutSetSize.setValue(ed, value);
	}
	
	public void setMaxMinimumCutSetSize(long value) {
		safeAccessMaxMinimumCutSetSize();
		this.maxMinimumCutSetSize.setValue(value);
	}
	
	public long getMaxMinimumCutSetSize() {
		safeAccessMaxMinimumCutSetSize();
		return maxMinimumCutSetSize.getValue();
	}
	
	public boolean isSetMaxMinimumCutSetSize() {
		safeAccessMaxMinimumCutSetSize();
		return maxMinimumCutSetSize.isSet();
	}
	
	public BeanPropertyInt getMaxMinimumCutSetSizeBean() {
		safeAccessMaxMinimumCutSetSize();
		return maxMinimumCutSetSize;
	}
	
	// *****************************************************************
	// * Attribute: faultTolerance
	// *****************************************************************
	private BeanPropertyInt faultTolerance = new BeanPropertyInt();
	
	private void safeAccessFaultTolerance() {
		if (faultTolerance.getTypeInstance() == null) {
			faultTolerance.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("faultTolerance"));
		}
	}
	
	public Command setFaultTolerance(EditingDomain ed, long value) {
		safeAccessFaultTolerance();
		return this.faultTolerance.setValue(ed, value);
	}
	
	public void setFaultTolerance(long value) {
		safeAccessFaultTolerance();
		this.faultTolerance.setValue(value);
	}
	
	public long getFaultTolerance() {
		safeAccessFaultTolerance();
		return faultTolerance.getValue();
	}
	
	public boolean isSetFaultTolerance() {
		safeAccessFaultTolerance();
		return faultTolerance.isSet();
	}
	
	public BeanPropertyInt getFaultToleranceBean() {
		safeAccessFaultTolerance();
		return faultTolerance;
	}
	
	// *****************************************************************
	// * Array Attribute: minimumCutSets
	// *****************************************************************
	private IBeanList<CutSet> minimumCutSets = new TypeSafeComposedPropertyInstanceList<>(CutSet.class);
	
	private void safeAccessMinimumCutSets() {
		if (minimumCutSets.getArrayInstance() == null) {
			minimumCutSets.setArrayInstance((ArrayInstance) helper.getPropertyInstance("minimumCutSets"));
		}
	}
	
	public IBeanList<CutSet> getMinimumCutSets() {
		safeAccessMinimumCutSets();
		return minimumCutSets;
	}
	
	
}
