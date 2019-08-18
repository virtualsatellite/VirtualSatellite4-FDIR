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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
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
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FailNodeProvider;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.FaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.evaluator.IFaultTreeEvaluator;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

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
		if (monitor != null) {
			monitor.setTaskName("MCS Analysis");
		}
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, COUNT_TASKS);
		
		RecoveryAutomaton ra = parent.getFirst(RecoveryAutomaton.class);
		FaultTreeEvaluator ftEvaluator = FaultTreeEvaluator.createDefaultFaultTreeEvaluator(ra != null);
		if (ra != null) {
			ftEvaluator.setRecoveryStrategy(new RecoveryStrategy(ra));
		}
		
		Map<Set<Object>, List<Fault>> mapCutSetToFaults = gatherMinimumCutSets(faults, ftEvaluator, subMonitor);
		
		if (subMonitor.isCanceled()) {
			return UnexecutableCommand.INSTANCE;
		}
		subMonitor.setTaskName("Computing MCS metrics");
		subMonitor.split(1);
		
		Map<Set<Object>, ModelCheckingResult> mapMcsToResult = computeMinimumCutSetsMetrics(mapCutSetToFaults, ftEvaluator);
		
		if (subMonitor.isCanceled()) {
			return UnexecutableCommand.INSTANCE;
		}
		subMonitor.setTaskName("Updating Results");
		subMonitor.split(1);
		
		long faultTolerance = mapCutSetToFaults.keySet().stream().mapToLong(cutSet -> cutSet.size()).min().orElse(1) - 1;
		
		return new RecordingCommand(ed, "MCS Analysis") {
			@Override
			protected void doExecute() {
				setFaultTolerance(faultTolerance);
				
				getMinimumCutSets().clear();
				getMinimumCutSets().addAll(generateEntries(mapCutSetToFaults, mapMcsToResult));
			}
		};
	}
	
	private Map<Set<Object>, List<Fault>> gatherMinimumCutSets(List<Fault> faults, IFaultTreeEvaluator ftEvaluator, SubMonitor monitor) {
		Map<Set<Object>, List<Fault>> mapCutSetToFaults = new HashMap<>();
		long maxMinimumCutSetSize = getMaxMinimumCutSetSizeBean().isSet() ? getMaxMinimumCutSetSize() : 0;
		
		for (Fault fault : faults) {
			if (monitor != null) {
				if (monitor.isCanceled()) {
					return null;
				}
				
				monitor.setTaskName("Analysing fault " + fault.getName());
				monitor.split(1);
			}
			
			ModelCheckingResult result = ftEvaluator.evaluateFaultTree(fault, new MinimumCutSet(maxMinimumCutSetSize));
			
			for (Set<Object> minCutSet : result.getMinCutSets()) {
				mapCutSetToFaults.computeIfAbsent(minCutSet, v -> new ArrayList<>()).add(fault);
			}
		}
		
		return mapCutSetToFaults;
	}
	
	private Map<Set<Object>, ModelCheckingResult> computeMinimumCutSetsMetrics(Map<Set<Object>, List<Fault>> mapCutSetToFaults, IFaultTreeEvaluator ftEvaluator) {
		Map<Set<Object>, ModelCheckingResult> mapMcsToResult = new HashMap<>();
		for (Set<Object> minCutSet : mapCutSetToFaults.keySet()) {
			List<Fault> failures = mapCutSetToFaults.get(minCutSet);
			Fault failure = failures.get(0);
			FailNodeProvider failNodeProvider = new FailNodeProvider();
			for (Object obj : minCutSet) {
				failNodeProvider.getFailNodes().add((BasicEvent) obj);
			}
			
			ModelCheckingResult mcsResult = ftEvaluator.evaluateFaultTree(failure, failNodeProvider,
					MTTF.MTTF, SteadyStateDetectability.STEADY_STATE_DETECTABILITY, MeanTimeToDetection.MTTD);
			
			mapMcsToResult.put(minCutSet, mcsResult);
		}
		
		return mapMcsToResult;
	}
	
	private List<CutSet> generateEntries(Map<Set<Object>, List<Fault>> mapCutSetToFaults, Map<Set<Object>, ModelCheckingResult> mapMcsToResult) {
		List<Set<Object>> sortedMinimumCutSets = mapCutSetToFaults.keySet().stream()
				.sorted((b1, b2) -> Integer.compare(b1.size(), b2.size()))
				.collect(Collectors.toList());
		
		FDIRParameters fdirParameters = null;
		EObject root = VirSatEcoreUtil.getRootContainer(getTypeInstance().eContainer());
		if (root != null) {
			BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance((StructuralElementInstance) root);
			fdirParameters = beanSei.getFirst(FDIRParameters.class);
		}
			
		
		List<CutSet> generatedEntries = new ArrayList<>();
		for (Set<Object> minimumCutSet : sortedMinimumCutSets) {
			List<Fault> failures = mapCutSetToFaults.get(minimumCutSet);
			ModelCheckingResult mcsResult = mapMcsToResult.get(minimumCutSet);
			for (Fault failure : failures) {
				CutSet cutSet = new CutSet(getConcept());
				for (Object object : minimumCutSet) {
					cutSet.getBasicEvents().add((BasicEvent) object);
				}
				getMinimumCutSets().add(cutSet);
				cutSet.setFailure(failure);
				cutSet.fill(mcsResult, fdirParameters);
			}
		}
		return generatedEntries;
	}
}
