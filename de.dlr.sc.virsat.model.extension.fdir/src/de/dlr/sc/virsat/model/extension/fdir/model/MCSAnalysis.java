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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToDetection;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateDetectability;
import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.concept.types.structural.IBeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
// *****************************************************************
// * Import Statements
// *****************************************************************
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.ecore.VirSatEcoreUtil;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FailableBasicEventsProvider;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.IFaultTreeEvaluator;

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public  class MCSAnalysis extends AMCSAnalysis {
	
	/**
	 * Constructor of Concept Class
	 */
	public MCSAnalysis() {
		super();
	}

	/**
	 * Constructor of Concept Class which will instantiate 
	 * a CategoryAssignment in the background from the given concept
	 * @param concept the concept where it will find the correct Category to instantiate from
	 */
	public MCSAnalysis(Concept concept) {
		super(concept);
	}	

	/**
	 * Constructor of Concept Class that can be initialized manually by a given Category Assignment
	 * @param categoryAssignment The category Assignment to be used for background initialization of the Category bean
	 */
	public MCSAnalysis(CategoryAssignment categoryAssignment) {
		super(categoryAssignment);
	}
	
	/**
	 * Peforms a minimum cut set analysis on the top level fault this analysis has been attached to
	 * @param ed the editing domain
	 * @param monitor 
	 * @return a command that sets the results of this analysis
	 */
	public Command perform(TransactionalEditingDomain ed, IProgressMonitor monitor) {
		IBeanStructuralElementInstance parent = new BeanStructuralElementInstance((StructuralElementInstance) getTypeInstance().eContainer());
		List<Fault> faults = parent.getAll(Fault.class);
		
		final int COUNT_TASKS = 2 + faults.size();
		SubMonitor subMonitor = SubMonitor.convert(monitor, COUNT_TASKS);
		subMonitor.setTaskName("MCS Analysis");
		subMonitor.split(1);
		subMonitor.subTask("Creating Data Model");
		
		RecoveryAutomaton ra = parent.getFirst(RecoveryAutomaton.class);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra);
		
		Map<Set<Object>, List<Fault>> mapCutSetToFaults = gatherMinimumCutSets(faults, ftEvaluator, subMonitor);

		subMonitor.split(1);
		subMonitor.subTask("Computing MCS metrics");
		
		Map<Set<Object>, ModelCheckingResult> mapMcsToResult = computeMinimumCutSetsMetrics(mapCutSetToFaults, ftEvaluator);
		
		subMonitor.split(1);
		subMonitor.subTask("Updating Results");
		
		long faultTolerance = mapCutSetToFaults.keySet().stream().mapToLong(Set::size).min().orElse(1) - 1;
		
		return new RecordingCommand(ed, "MCS Analysis") {
			@Override
			protected void doExecute() {
				setFaultTolerance(faultTolerance);
				
				getMinimumCutSets().clear();
				getMinimumCutSets().addAll(generateEntries(mapCutSetToFaults, mapMcsToResult));
			}
		};
	}
	
	/**
	 * Collects all Minimum Cut Sets that can cause the given faults to occur
	 * @param faults the faults
	 * @param ftEvaluator the fault tree evaluator
	 * @param monitor the monitor
	 * @return a mapping from cut set to faults that are caused by them
	 */
	private Map<Set<Object>, List<Fault>> gatherMinimumCutSets(List<Fault> faults, IFaultTreeEvaluator ftEvaluator, SubMonitor monitor) {
		Map<Set<Object>, List<Fault>> mapCutSetToFaults = new HashMap<>();
		long maxMinimumCutSetSize = getMaxMinimumCutSetSizeBean().isSet() ? getMaxMinimumCutSetSize() : 0;
		
		for (Fault fault : faults) {
			monitor.split(1);
			monitor.subTask("Analysing fault " + fault.getName());
			
			ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, new MinimumCutSet(maxMinimumCutSetSize));
			
			for (Set<Object> minCutSet : result.getMinCutSets()) {
				mapCutSetToFaults.computeIfAbsent(minCutSet, v -> new ArrayList<>()).add(fault);
			}
		}
		
		return mapCutSetToFaults;
	}
	
	/**
	 * Computes for each Minimum Cut Set its associated metrics
	 * @param mapCutSetToFaults the mapping from cut set to fault
	 * @param ftEvaluator the fault tree evaluator
	 * @return the model checking result
	 */
	private Map<Set<Object>, ModelCheckingResult> computeMinimumCutSetsMetrics(Map<Set<Object>, List<Fault>> mapCutSetToFaults, IFaultTreeEvaluator ftEvaluator) {
		Map<Set<Object>, ModelCheckingResult> mapMcsToResult = new HashMap<>();
		for (Entry<Set<Object>, List<Fault>> entry : mapCutSetToFaults.entrySet()) {
			Set<Object> minCutSet = entry.getKey();
			List<Fault> failures = entry.getValue();
			Fault failure = failures.get(0);
			FailableBasicEventsProvider failNodeProvider = new FailableBasicEventsProvider();
			for (Object obj : minCutSet) {
				failNodeProvider.getBasicEvents().add((BasicEvent) obj);
			}
			
			ModelCheckingResult mcsResult = ftEvaluator.evaluateFaultTree(failure, failNodeProvider, null,
					MeanTimeToFailure.MTTF, SteadyStateDetectability.SSD, MeanTimeToDetection.MTTD);
			
			mapMcsToResult.put(minCutSet, mcsResult);
		}
		
		return mapMcsToResult;
	}
	
	/**
	 * Generates the Minimum Cut Set entries from the analysis results
	 * @param mapCutSetToFaults the generated minimum cut sets
	 * @param mapMcsToResult the associated metrics
	 * @return the generated cut sets
	 */
	private List<CutSet> generateEntries(Map<Set<Object>, List<Fault>> mapCutSetToFaults, Map<Set<Object>, ModelCheckingResult> mapMcsToResult) {
		FDIRParameters fdirParameters = null;
		EObject root = VirSatEcoreUtil.getRootContainer(getTypeInstance().eContainer());
		if (root != null) {
			BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance((StructuralElementInstance) root);
			fdirParameters = beanSei.getFirst(FDIRParameters.class);
		}

		List<CutSet> generatedEntries = new ArrayList<>();
		for (Entry<Set<Object>, List<Fault>> entry : mapCutSetToFaults.entrySet()) {
			Set<Object> minimumCutSet = entry.getKey();
			List<Fault> failures = entry.getValue();
			ModelCheckingResult mcsResult = mapMcsToResult.get(minimumCutSet);
			for (Fault failure : failures) {
				CutSet cutSet = new CutSet(getConcept());
				for (Object object : minimumCutSet) {
					cutSet.getBasicEvents().add((BasicEvent) object);
				}
				generatedEntries.add(cutSet);
				cutSet.setFailure(failure);
				cutSet.fill(mcsResult, fdirParameters);
			}
		}
		
		generatedEntries.sort(new Comparator<CutSet>() {
			@Override
			public int compare(CutSet cutSet1, CutSet cutSet2) {
				int compareCutSetSizes = Integer.compare(cutSet1.getBasicEvents().size(), cutSet2.getBasicEvents().size());
				if (compareCutSetSizes != 0) {
					return compareCutSetSizes;
				}
				
				String[] labels1 = { cutSet1.getFailure().getName(), cutSet1.getBasicEventsLabel() };
				String[] labels2 = { cutSet2.getFailure().getName(), cutSet2.getBasicEventsLabel() };
				
				for (int i = 0; i < labels1.length; ++i) {
					int compareLabels = labels1[i].compareTo(labels2[i]);
					if (compareLabels != 0) {
						return compareLabels;
					}
				}
				
				return 0;
			}
		});
		
		return generatedEntries;
	}
}
