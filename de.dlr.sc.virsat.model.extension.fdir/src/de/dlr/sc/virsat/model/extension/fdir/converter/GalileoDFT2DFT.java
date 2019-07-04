/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dlr.sc.virsat.fdir.galileo.GalileoDFTParser;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoFaultTreeNode;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoNodeType;
import de.dlr.sc.virsat.model.concept.types.structural.ABeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.ADEP;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.Gate;
import de.dlr.sc.virsat.model.extension.fdir.model.OBSERVER;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.VOTE;
import de.dlr.sc.virsat.model.extension.fdir.util.FaultTreeHelper;

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
	private final FaultTreeHelper ftHelper;
	
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
		ftHelper = new FaultTreeHelper(concept);
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
	 */
	public Fault convert() {
		fault = new Fault(concept);
		
		if (parent != null) {
			parent.add(fault);
		}
		
		mapGalileoFaultTreeNodeToFaultTreeNode = new HashMap<>();
		mapGalileoFaultTreeNodeToBasicEvent = new HashMap<>();
		
		GalileoDFTParser parser = new GalileoDFTParser();
		try {
			GalileoDft galileoDft = parser.parse(is);
			GalileoFaultTreeNode root = galileoDft.getRoot();
			fault.setName(root.getName());
			
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
			
			for (GalileoFaultTreeNode galileoFtn : galileoDft.getBasicEvents()) {
				Fault leafFault = (Fault) convertGalileoDftNode(galileoFtn);
				BasicEvent be = new BasicEvent(concept);
				
				mapGalileoFaultTreeNodeToBasicEvent.put(galileoFtn, be);
				
				leafFault.getBasicEvents().add(be);
				if (parent != null) {
					parent.add(leafFault);
				}
				
				be.setName(leafFault.getName());
				
				double hotFailureRate = Double.valueOf(galileoFtn.getLambda());
				double coldFailureRate = galileoFtn.getDorm() == null ? 0 : Double.valueOf(galileoFtn.getDorm()) * hotFailureRate;
				double repairRate = galileoFtn.getRepair() == null ? 0 : Double.valueOf(galileoFtn.getRepair());
				
				be.setHotFailureRate(hotFailureRate);
				be.setColdFailureRate(coldFailureRate);
				be.setRepairRate(repairRate);
			}
			
			
			// Connect root node of galileo dft with root fault node
			FaultTreeNode ftnToGalileoRoot = mapGalileoFaultTreeNodeToFaultTreeNode.get(root);
			
			FaultTreeEdge fteRoot = new FaultTreeEdge(concept);
			fteRoot.setFrom(ftnToGalileoRoot);
			fteRoot.setTo(fault);
			fault.getFaultTree().getPropagations().add(fteRoot);
			
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
							ftHelper.connectDep(fault, ftnParent, ftnChild);
						} else {
							ftHelper.connect(fault, ftnChild, ftnParent);
						}	
					} else {
						if (ftnParent.getFaultTreeNodeType().equals(FaultTreeNodeType.SPARE) && primaryAdded) {
							ftHelper.connectSpare(fault, ftnChild, ftnParent);
						} else {
							ftHelper.connect(fault, ftnChild, ftnParent);
						}
					}
					
					primaryAdded = true;
				}
				
				List<GalileoFaultTreeNode> observables = galileoFtn.getType().getObservables();
				for (GalileoFaultTreeNode observable : observables) {
					FaultTreeNode ftnFrom = mapGalileoFaultTreeNodeToFaultTreeNode.get(galileoFtn);
					FaultTreeNode ftnTo = mapGalileoFaultTreeNodeToFaultTreeNode.get(observable);
					ftHelper.connectObserver(fault, ftnFrom, ftnTo);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fault;
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
			FaultTreeNodeType type = galileoNodeType2FaultTreeNodeType(galileoType);
			Gate gate = ftHelper.createGate(type);
			
			if (type.equals(FaultTreeNodeType.VOTE)) {
				String[] split = galileoType.getTypeName().split(GALILEO_VOTE);
				Integer x = Integer.valueOf(split[0]);
				((VOTE) gate).setVotingThreshold(x);
			} else if (type.equals(FaultTreeNodeType.OBSERVER)) {
				((OBSERVER) gate).setObservationRate(Double.valueOf(galileoType.getObservationRate()));
			} else if (type.equals(FaultTreeNodeType.RDEP)) {
				((RDEP) gate).setRateChange(Double.valueOf(galileoType.getRateFactor()));
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
	private FaultTreeNodeType galileoNodeType2FaultTreeNodeType(GalileoNodeType galileoType) {
		if (galileoType.getTypeName() != null) {
			String typeName = galileoType.getTypeName();
			if (typeName.contains(GALILEO_VOTE)) {
				return FaultTreeNodeType.VOTE;
			} else if (typeName.contains(GALILEO_SPARE_GATE_SUFFIX)) {
				return FaultTreeNodeType.SPARE;
			} else {
				return FaultTreeNodeType.valueOf(typeName.toUpperCase());
			}
		} else if (galileoType.getObservationRate() != null) {
			return FaultTreeNodeType.OBSERVER;
		} else if (galileoType.getRateFactor() != null) {
			return FaultTreeNodeType.RDEP;
		}
		
		throw new RuntimeException("Unknown Galileo Fault Tree Node Type: " + galileoType);
	}
}
