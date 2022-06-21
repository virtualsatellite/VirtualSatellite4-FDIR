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
import de.dlr.sc.virsat.model.concept.list.TypeSafeArrayInstanceList;
import de.dlr.sc.virsat.model.concept.types.category.IBeanCategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;
import javax.xml.bind.annotation.XmlRootElement;
import de.dlr.sc.virsat.model.dvlm.categories.util.CategoryInstantiator;
import de.dlr.sc.virsat.model.concept.list.IBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import javax.xml.bind.annotation.XmlAccessType;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.common.command.Command;
import de.dlr.sc.virsat.model.concept.list.TypeSafeComposedPropertyBeanList;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.UnitValuePropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
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
public abstract class AFDIRParameters extends GenericCategory implements IBeanCategoryAssignment {

	public static final String FULL_QUALIFIED_CATEGORY_NAME = "de.dlr.sc.virsat.model.extension.fdir.FDIRParameters";
	
	/**
 	* Call this method to get the full qualified name of the underlying category
 	* @return The FQN of the category as String
 	*/
	public String getFullQualifiedCategoryName() {
		return FULL_QUALIFIED_CATEGORY_NAME;
	}
	
	// property name constants
	public static final String PROPERTY_MISSIONTIME = "missionTime";
	public static final String PROPERTY_TIMESTEP = "timestep";
	public static final String PROPERTY_PROBABILITYLEVELS = "probabilityLevels";
	public static final String PROPERTY_DETECTIONLEVELS = "detectionLevels";
	public static final String PROPERTY_CRITICALITYMATRICES = "criticalityMatrices";
	
	
	
	// *****************************************************************
	// * Class Constructors
	// *****************************************************************
	
	public AFDIRParameters() {
	}
	
	public AFDIRParameters(Concept concept) {
		Category categoryFromActiveCategories = ActiveConceptHelper.getCategory(concept, "FDIRParameters");
		CategoryAssignment categoryAssignement = new CategoryInstantiator().generateInstance(categoryFromActiveCategories, "FDIRParameters");
		setTypeInstance(categoryAssignement);
	}
	
	public AFDIRParameters(CategoryAssignment categoryAssignement) {
		setTypeInstance(categoryAssignement);
	}
	
	
	// *****************************************************************
	// * Attribute: missionTime
	// *****************************************************************
	private BeanPropertyFloat missionTime = new BeanPropertyFloat();
	
	private void safeAccessMissionTime() {
		if (missionTime.getTypeInstance() == null) {
			missionTime.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("missionTime"));
		}
	}
	
	public Command setMissionTime(EditingDomain ed, double value) {
		safeAccessMissionTime();
		return this.missionTime.setValue(ed, value);
	}
	
	public void setMissionTime(double value) {
		safeAccessMissionTime();
		this.missionTime.setValue(value);
	}
	
	public double getMissionTime() {
		safeAccessMissionTime();
		return missionTime.getValue();
	}
	
	public boolean isSetMissionTime() {
		safeAccessMissionTime();
		return missionTime.isSet();
	}
	
	@XmlElement
	public BeanPropertyFloat getMissionTimeBean() {
		safeAccessMissionTime();
		return missionTime;
	}
	
	// *****************************************************************
	// * Attribute: timestep
	// *****************************************************************
	private BeanPropertyFloat timestep = new BeanPropertyFloat();
	
	private void safeAccessTimestep() {
		if (timestep.getTypeInstance() == null) {
			timestep.setTypeInstance((UnitValuePropertyInstance) helper.getPropertyInstance("timestep"));
		}
	}
	
	public Command setTimestep(EditingDomain ed, double value) {
		safeAccessTimestep();
		return this.timestep.setValue(ed, value);
	}
	
	public void setTimestep(double value) {
		safeAccessTimestep();
		this.timestep.setValue(value);
	}
	
	public double getTimestep() {
		safeAccessTimestep();
		return timestep.getValue();
	}
	
	public boolean isSetTimestep() {
		safeAccessTimestep();
		return timestep.isSet();
	}
	
	@XmlElement
	public BeanPropertyFloat getTimestepBean() {
		safeAccessTimestep();
		return timestep;
	}
	
	// *****************************************************************
	// * Array Attribute: probabilityLevels
	// *****************************************************************
	private IBeanList<BeanPropertyFloat> probabilityLevelsBean = new TypeSafeArrayInstanceList<>(BeanPropertyFloat.class);
	
	private void safeAccessProbabilityLevelsBean() {
		if (probabilityLevelsBean.getArrayInstance() == null) {
			probabilityLevelsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("probabilityLevels"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyFloat> getProbabilityLevelsBean() {
		safeAccessProbabilityLevelsBean();
		return probabilityLevelsBean;
	}
	
	// *****************************************************************
	// * Array Attribute: detectionLevels
	// *****************************************************************
	private IBeanList<BeanPropertyFloat> detectionLevelsBean = new TypeSafeArrayInstanceList<>(BeanPropertyFloat.class);
	
	private void safeAccessDetectionLevelsBean() {
		if (detectionLevelsBean.getArrayInstance() == null) {
			detectionLevelsBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("detectionLevels"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyFloat> getDetectionLevelsBean() {
		safeAccessDetectionLevelsBean();
		return detectionLevelsBean;
	}
	
	// *****************************************************************
	// * Array Attribute: criticalityMatrices
	// *****************************************************************
	private IBeanList<CriticalityMatrix> criticalityMatrices = new TypeSafeComposedPropertyInstanceList<>(CriticalityMatrix.class);
	
	private void safeAccessCriticalityMatrices() {
		if (criticalityMatrices.getArrayInstance() == null) {
			criticalityMatrices.setArrayInstance((ArrayInstance) helper.getPropertyInstance("criticalityMatrices"));
		}
	}
	
	public IBeanList<CriticalityMatrix> getCriticalityMatrices() {
		safeAccessCriticalityMatrices();
		return criticalityMatrices;
	}
	
	private IBeanList<BeanPropertyComposed<CriticalityMatrix>> criticalityMatricesBean = new TypeSafeComposedPropertyBeanList<>();
	
	private void safeAccessCriticalityMatricesBean() {
		if (criticalityMatricesBean.getArrayInstance() == null) {
			criticalityMatricesBean.setArrayInstance((ArrayInstance) helper.getPropertyInstance("criticalityMatrices"));
		}
	}
	
	@XmlElement
	public IBeanList<BeanPropertyComposed<CriticalityMatrix>> getCriticalityMatricesBean() {
		safeAccessCriticalityMatricesBean();
		return criticalityMatricesBean;
	}
	
	
}
