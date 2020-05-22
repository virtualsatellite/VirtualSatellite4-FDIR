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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ReferencePropertyInstance;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.util.BasicEventHolder;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHolder;

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
public  class FaultTree extends AFaultTree {
	
	/**
	 * Constructor of Concept Class
	 */
	public FaultTree() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public FaultTree(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public FaultTree(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Gets the root node of this fault tree
	 * @return the root fault
	 */
	public Fault getRoot() {
		return new Fault((CategoryAssignment) getTypeInstance().eContainer().eContainer());
	}
	
	/**
	 * Gets all child spares of this fault tree
	 * @return the set of child spares
	 */
	public Set<Fault> getChildSpares() {
		Set<Fault> spares = new HashSet<>();
		for (FaultTreeEdge fte : getSpares()) {
			FaultTreeNode node = fte.getFrom();
			if (node instanceof Fault) {
				spares.add((Fault) node);
			}
		}
		
		return spares;
	}
	
	/**
	 * Gets all faults that are directly affected by the occurence of this fault
	 * @return the set of affected faults
	 */
	public Set<Fault> getAffectedFaults() {
		Set<Fault> affectedFaults = new HashSet<>();
		Fault root = getRoot();
		Resource resource = getTypeInstance().eResource();
		
		if (resource == null) {
			return Collections.emptySet();
		}
		
		ResourceSet rs = resource.getResourceSet();
		EcoreUtil.UsageCrossReferencer.find(root.getTypeInstance(), rs).forEach(setting -> {
			EObject eObject = setting.getEObject();
			if (eObject instanceof ReferencePropertyInstance && eObject.eContainer() instanceof CategoryAssignment) {
				CategoryAssignment ca = (CategoryAssignment) eObject.eContainer();
				if (ca.getType().getName().equals(FaultTreeEdge.class.getSimpleName())) {
					FaultTreeEdge fte = new FaultTreeEdge(ca);
					if (fte.getTo() != null) {
						if (Objects.equals(fte.getFrom(), root)) {
							affectedFaults.add(fte.getTo().getFault());
						}
					}
				}
			}
		});
		return affectedFaults;
	}

	/**
	 * Gets all recovery actions directly contained in this fault tree
	 * that can contribute to recovering the top level fault of this fault tree
	 * @return a list of all local recovery actions
	 */
	public List<String> getPotentialRecoveryActions() {
		List<String> potentialRecoveryActions = new ArrayList<>();
		
		FaultTreeHolder ftHolder = new FaultTreeHolder(getRoot());
		List<BasicEvent> basicEvents = ftHolder.getChildFaults(getRoot()).stream()
				.flatMap(fault -> fault.getBasicEvents().stream())
				.collect(Collectors.toList());
		
		basicEvents.addAll(getRoot().getBasicEvents());
		
		for (BasicEvent be : basicEvents) {
			double transientRepairRate = BasicEventHolder.getRateValue(be.getRepairRateBean());
			if (BasicEventHolder.isRateDefined(transientRepairRate)) {
				potentialRecoveryActions.add("Transient Failure");
			}
			for (RepairAction repairAction : be.getRepairActions()) {
				potentialRecoveryActions.add(repairAction.getName());
			}
		}
		
		for (FaultTreeEdge spareEdge : getSpares()) {
			FaultTreeNode from = spareEdge.getFrom();
			if (from != null) {
				String recoveryAction = "Switch to " + (from.getParent() != null ? from.getParent().getName() + "." : "") + from.getName();
				potentialRecoveryActions.add(recoveryAction);
			}
		}
		
		return potentialRecoveryActions;
	}
	
	/**
	 * Creates a dot representation of this fault tree
	 * @return the dot representation
	 */
	public String toDot() {
		return new FaultTreeHelper().toDot(this);
	}
}
