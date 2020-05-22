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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.fdir.galileo.GalileoDFTParser;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoRepairAction;
import de.dlr.sc.virsat.fdir.galileo.dft.Named;
import de.dlr.sc.virsat.fdir.galileo.dft.Observer;
import de.dlr.sc.virsat.fdir.galileo.dft.Parametrized;
import de.dlr.sc.virsat.model.concept.types.structural.ABeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.DELAY;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.model.MONITOR;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.RepairAction;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeBuilder;

/**
 * Converts a DFT from the galileo DFT model to a VirSat DFT and embeds the resulting objects into an FMECA table.
 * @author muel_s8
 *
 */

public class GalileoDFT2DFT {
	
	public static final String GALILEO_VOTE = "of";
	public static final String GALILEO_SPARE_GATE_SUFFIX = "sp";
	public static final String GALILEO_FDEP = "fdep";
	
	private final InputStream is;
	private final Concept concept;
	private final ABeanStructuralElementInstance parent;
	private final FaultTreeBuilder ftBuilder;
	
	private Fault fault;
	
	private Map<GalileoFaultTreeNode, FaultTreeNode> mapGalileoFaultTreeNodeToFaultTreeNode;
	private Map<GalileoFaultTreeNode, BasicEvent> mapGalileoFaultTreeNodeToBasicEvent;
	
	/**
	 * Default Constructor. 
	 * @param concept the concept defining out VirSat objects
	 * @param is the input stream containing the galileo DFT
	 * @param parent the parent that will contain the faults
	 */
	
	public GalileoDFT2DFT(Concept concept, InputStream is, ABeanStructuralElementInstance parent) {
		this.is = is;
		this.concept = concept;
		this.parent = parent;
		ftBuilder = new FaultTreeBuilder(concept);
	}
	
	/**
	 * Simplified Constructor for testing
	 * @param concept the concept defining out VirSat objects
	 * @param is the input stream containing the galileo DFT
	 */
	
	public GalileoDFT2DFT(Concept concept, InputStream is) {
		this(concept, is, null);
	}
	
	/**
	 * Performs the actual conversion
	 * @return an FMECA table containing the faults and fault trees describes in the galileo DFT.
	 * @throws IOException 
	 */
	public Fault convert() throws IOException {
		fault = new Fault(concept);
		
		if (parent != null) {
			parent.add(fault);
		}
		
		GalileoDFTParser parser = new GalileoDFTParser();
		GalileoDft galileoDft = parser.parse(is);
		
		mapGalileoFaultTreeNodeToFaultTreeNode = new HashMap<>();
		mapGalileoFaultTreeNodeToBasicEvent = new HashMap<>();
		
		convertGates(galileoDft);
		convertBasicEvents(galileoDft);
		convertEdges(galileoDft);
		convertRoot(galileoDft);
		
		return fault;
	}

	/**
	 * Converts the root node of the galileo dft to a root of a dft
	 * @param galileoDft the galileo dft
	 */
	private void convertRoot(GalileoDft galileoDft) {
		GalileoFaultTreeNode root = galileoDft.getRoot();
		fault.setName(root.getName());
		
		// Connect root node of galileo dft with root fault node
		FaultTreeNode ftnToGalileoRoot = mapGalileoFaultTreeNodeToFaultTreeNode.get(root);
		ftBuilder.connect(fault, ftnToGalileoRoot, fault);
	}

	/**
	 * Converts the edges in a galileo dft to the appropriately typed fault tree edges in a dft
	 * @param galileoDft the galileo dft
	 */
	private void convertEdges(GalileoDft galileoDft) {
		// Create the propagation edges and dependencies
		for (GalileoFaultTreeNode galileoFtn : galileoDft.getGates()) {
			List<GalileoFaultTreeNode> children = galileoFtn.getChildren();
			FaultTreeNode ftnParent = mapGalileoFaultTreeNodeToFaultTreeNode.get(galileoFtn);

			boolean primaryAdded = false;
			
			for (GalileoFaultTreeNode child : children) {
				FaultTreeNode ftnChild = mapGalileoFaultTreeNodeToFaultTreeNode.get(child);
				
				if (ftnParent instanceof ADEP) {
					if (primaryAdded) {
						// DEPs always trigger the basic events directly
						ftnChild = mapGalileoFaultTreeNodeToBasicEvent.get(child);
						ftBuilder.connectDep(fault, ftnParent, ftnChild);
					} else {
						ftBuilder.connect(fault, ftnChild, ftnParent);
					}	
				} else {
					if (ftnParent.getFaultTreeNodeType().equals(FaultTreeNodeType.SPARE) && primaryAdded) {
						ftBuilder.connectSpare(fault, ftnChild, ftnParent);
					} else {
						ftBuilder.connect(fault, ftnChild, ftnParent);
					}
				}
				
				primaryAdded = true;
			}
			
			if (galileoFtn.getType() instanceof Observer) {
				List<GalileoFaultTreeNode> observables = ((Observer) galileoFtn.getType()).getObservables();
				for (GalileoFaultTreeNode observable : observables) {
					FaultTreeNode ftnFrom = mapGalileoFaultTreeNodeToFaultTreeNode.get(observable);
					FaultTreeNode ftnTo = mapGalileoFaultTreeNodeToFaultTreeNode.get(galileoFtn);
					ftBuilder.connectObserver(fault, ftnFrom, ftnTo);
				}
			}
		}
	}

	/**
	 * Converts all gates in the galileo dft to dft nodes
	 * @param galileoDft the galileo dft
	 */
	private void convertGates(GalileoDft galileoDft) {
		// Create the nodes
		for (GalileoFaultTreeNode galileoFtn : galileoDft.getGates()) {
			FaultTreeNode ftn = convertGalileoDftNode(galileoFtn);
			if (ftn instanceof Gate) {
				fault.getFaultTree().getGates().add((Gate) ftn);
			} else {
				if (parent != null) {
					parent.add(ftn);
				}
			}
		}
	}

	/**
	 * Converts all basic events in a galileo dft to basic events in a dft
	 * @param galileoDft the galileo dft
	 */
	private void convertBasicEvents(GalileoDft galileoDft) {
		for (GalileoFaultTreeNode galileoFtn : galileoDft.getBasicEvents()) {
			Fault leafFault = (Fault) convertGalileoDftNode(galileoFtn);
			
			if (parent != null) {
				parent.add(leafFault);
			}
			
			BasicEvent be = convertBasicEvent(galileoFtn);
			leafFault.getBasicEvents().add(be);
		}
	}

	
	/**
	 * Creates a basic event for a galileo basic event.
	 * @param galileoBe the galileo basic event
	 * @param leafFault the fault to attach the basic event to
	 * @return the created basic event
	 */
	private BasicEvent convertBasicEvent(GalileoFaultTreeNode galileoBe) {
		BasicEvent be = new BasicEvent(concept);
		
		mapGalileoFaultTreeNodeToBasicEvent.put(galileoBe, be);
		
		be.setName(galileoBe.getName());
		
		double hotFailureRate = Double.valueOf(galileoBe.getLambda());
		double coldFailureRate = galileoBe.getDorm() == null ? 0 : Double.valueOf(galileoBe.getDorm()) * hotFailureRate;
		
		be.setHotFailureRate(hotFailureRate);
		be.setColdFailureRate(coldFailureRate);
		
		for (GalileoRepairAction galileoRepairAction : galileoBe.getRepairActions()) {
			double repairRate = Double.valueOf(galileoRepairAction.getRepair());
			if (galileoRepairAction.getName() != null) {
				RepairAction repairAction = new RepairAction(concept);
				be.getRepairActions().add(repairAction);
				repairAction.setRepairRate(repairRate);
				for (GalileoFaultTreeNode galileoObservation : galileoRepairAction.getObservartions()) {
					FaultTreeNode observation = mapGalileoFaultTreeNodeToFaultTreeNode.get(galileoObservation);
					repairAction.getObservations().add(observation);
					repairAction.setName(galileoRepairAction.getName());
				}
			}
			be.setRepairRate(repairRate);
		}
		return be;
	}
	
	/**
	 * Converts a single Galileo DFT Node into a VirSat FaultTreeNode
	 * @param galileoFtn the fault tree node in the galileo DFT
	 * @return a VirSat Fault Tree Node
	 */
	private FaultTreeNode convertGalileoDftNode(GalileoFaultTreeNode galileoFtn) {
		FaultTreeNode ftn = null;
			
		GalileoNodeType galileoType = galileoFtn.getType();
		
		if (galileoType != null) {
			FaultTreeNodeType type = convertNodeType(galileoType);
			Gate gate = ftBuilder.createGate(type);
			
			if (type.equals(FaultTreeNodeType.VOTE)) {
				String[] split = ((Named) galileoType).getTypeName().split(GALILEO_VOTE);
				Integer x = Integer.valueOf(split[0]);
				((VOTE) gate).setVotingThreshold(x);
			} else if (type.equals(FaultTreeNodeType.MONITOR)) {
				((MONITOR) gate).setObservationRate(Double.valueOf(((Observer) galileoType).getObservationRate()));
			} else if (type.equals(FaultTreeNodeType.RDEP)) {
				((RDEP) gate).setRateChange(Double.valueOf((((Parametrized) galileoType).getParameter())));
			} else if (type.equals(FaultTreeNodeType.DELAY)) {
				((DELAY) gate).setTime(Double.valueOf(((Parametrized) galileoType).getParameter()));
			} 
			
			ftn = gate;
		} else {
			ftn = new Fault(concept);
		}
		
		ftn.setName(galileoFtn.getName());
		 
		mapGalileoFaultTreeNodeToFaultTreeNode.put(galileoFtn, ftn);
		
		return ftn;
	}
	
	/**
	 * Converts a galileo fault tree node type to a VirSat fault tree node type
	 * @param galileoType the galileo node type
	 * @return the converted VirSat node type
	 */
	private FaultTreeNodeType convertNodeType(GalileoNodeType galileoType) {
		if (galileoType instanceof Named) {
			String typeName = ((Named) galileoType).getTypeName();
			if (typeName.contains(GALILEO_VOTE)) {
				return FaultTreeNodeType.VOTE;
			} else if (typeName.contains(GALILEO_SPARE_GATE_SUFFIX)) {
				return FaultTreeNodeType.SPARE;
			} else {
				return FaultTreeNodeType.valueOf(typeName.toUpperCase());
			}
		} else if (galileoType instanceof Observer) {
			return FaultTreeNodeType.MONITOR;
		} else if (galileoType instanceof Parametrized) {
			return FaultTreeNodeType.valueOf(((Parametrized) galileoType).getTypeName().toUpperCase());
		} 

		throw new RuntimeException("Unknown Galileo Fault Tree Node Type: " + galileoType);
	}
}
