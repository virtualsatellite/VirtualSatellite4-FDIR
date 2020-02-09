/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.galileo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.fdir.galileo.dft.DftFactory;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode;
import de.dlr.sc.virsat.fdir.galileo.dft.Named;
import de.dlr.sc.virsat.fdir.galileo.dft.Observer;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.ATypeInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.SPARE;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

/**
 * This class takes care of converting a fault to a galileo dft representation
 * @author muel_s8
 *
 */
public class DFT2GalileoDFT {
	
	private boolean useFQN = true;
	
	/**
	 * Standard constructor.
	 */
	public DFT2GalileoDFT() {
		
	}
	
	/**
	 * Constructor to configure what kind of identifiers to use in the transformation
	 * @param useFQN set to true to use the FullQualifiedNames as identifiers (standard behavior)
	 * or set it to false to use UUIDs instead.
	 */
	public DFT2GalileoDFT(boolean useFQN) {
		this.useFQN = useFQN;
	}
	
	/**
	 * Takes the passed sei and looks for the first top level fault to convert
	 * @param sei the structural element instance
	 * @return a galileo representation of the first top level fault in the passed sei
	 */
	public GalileoDft convert(StructuralElementInstance sei) {
		BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance();
		beanSei.setStructuralElementInstance(sei);
		
		List<Fault> allFaults = beanSei.getAll(Fault.class);
		Fault topLevelFault = allFaults.stream().filter(Fault::isLocalTopLevelFault).findFirst().get();
		return convert(topLevelFault);
	}
	
	/**
	 * Converts the fault tree induced by the passed fault to a galileo fault tree representation
	 * @param root the root fault
	 * @return a galileo dft representation
	 */
	public GalileoDft convert(Fault root) {
		FaultTreeHelper ftHelper = new FaultTreeHelper(root.getConcept());
		
		GalileoDft galileoDft = DftFactory.eINSTANCE.createGalileoDft();
		
		Map<FaultTreeNode, GalileoFaultTreeNode> mapDftNodeToGalileoNode = new HashMap<>();
		
		List<FaultTreeNode> allNodes = ftHelper.getAllNodes(root);
		for (FaultTreeNode node : allNodes) {
			GalileoFaultTreeNode galileoNode = DftFactory.eINSTANCE.createGalileoFaultTreeNode();
			galileoNode.setName(getIdentifier(node.getTypeInstance()));
			mapDftNodeToGalileoNode.put(node, galileoNode);
			
			if (node instanceof Gate) {
				Gate gate = (Gate) node;
				Named nodeType = DftFactory.eINSTANCE.createNamed();
				galileoNode.setType(nodeType);
				
				if (node instanceof VOTE) {
					VOTE vote = (VOTE) node;
					nodeType.setTypeName(vote.getVotingThreshold() + "of" +  ftHelper.getChildren(vote).size());
				} else if (node instanceof SPARE) {
					nodeType.setTypeName("wsp");
				} else {
					nodeType.setTypeName(gate.getFaultTreeNodeType().toString().toLowerCase());
				}
				galileoDft.getGates().add(galileoNode);
			} else if (node instanceof Fault) {
				Named nodeType = DftFactory.eINSTANCE.createNamed();
				galileoNode.setType(nodeType);
				nodeType.setTypeName(FaultTreeNodeType.OR.name().toLowerCase());
				galileoDft.getGates().add(galileoNode);
			} else if (node instanceof BasicEvent) {
				BasicEvent basicEvent = (BasicEvent) node;
				galileoNode.setName(getIdentifier(basicEvent.getTypeInstance()));
				galileoNode.setLambda(String.valueOf(basicEvent.getHotFailureRateBean().getValueToBaseUnit()));
				galileoNode.setDorm(String.valueOf(basicEvent.getColdFailureRateBean().getValueToBaseUnit()));
				if (basicEvent.getRepairRate() != 0) {
					galileoNode.setRepair(String.valueOf(basicEvent.getRepairRateBean().getValueToBaseUnit()));
				}
				galileoDft.getBasicEvents().add(galileoNode);
			} 
			
			if (node == root) {
				galileoDft.setRoot(galileoNode);
			}
		}
		
		for (FaultTreeNode node : allNodes) {
			GalileoFaultTreeNode galileoNode = mapDftNodeToGalileoNode.get(node);
			List<FaultTreeNode> allChildren = new ArrayList<>();
			allChildren.addAll(ftHelper.getChildren(node));
			allChildren.addAll(ftHelper.getSpares(node));
			
			if (node instanceof Fault) {
				Fault fault = (Fault) node;
				allChildren.addAll(fault.getBasicEvents());
			} else if (node instanceof MONITOR) {
				List<FaultTreeNode> observables = ftHelper.getObservables(node);
				for (FaultTreeNode observable : observables) {
					GalileoFaultTreeNode galileoChild = mapDftNodeToGalileoNode.get(observable);
					((Observer) galileoNode.getType()).getObservables().add(galileoChild);
				}
			}
			
			for (FaultTreeNode child : allChildren) {
				GalileoFaultTreeNode galileoChild = mapDftNodeToGalileoNode.get(child);
				galileoNode.getChildren().add(galileoChild);
			}
		}
		
		return galileoDft;
	}
	
	/**
	 * Gets the node identified for a given type instance
	 * @param ti the node for which we need an identified
	 * @return either the FQN of the node or the UUID depending on the flag useFQN
	 */
	private String getIdentifier(ATypeInstance ti) {
		return useFQN ? ti.getFullQualifiedInstanceName() : ti.getUuid().toString();
	}
	
}
