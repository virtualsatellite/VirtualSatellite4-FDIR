/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.runner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingQuery;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingStatistics;
import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MeanTimeToFailure;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;

/**
 * This class uses Storm for model checking markov automata
 * @author yoge_re
 *
 */
public class StormModelChecker implements IMarkovModelChecker {

	private double delta;
	private StormExecutionEnvironment stormExecutionEnvironment;
	private List<Double> resultExtracted = new ArrayList<Double>();
	private int startIndex = 0;
	 
	private ModelCheckingResult modelCheckingResult;
	private ModelCheckingStatistics statistics;
	
	/**
	 * 
	 * @param delta timeslice
	 * @param stormExecutionEnvironment 
	 */
	public StormModelChecker(double delta, StormExecutionEnvironment stormExecutionEnvironment) {
		this.delta = delta;
		this.stormExecutionEnvironment = stormExecutionEnvironment;
	}

	@Override
	public void visit(Reliability reliabilityMetric, SubMonitor subMonitor) {
		modelCheckingResult.getFailRates().add((double) 0);
		int endIndex = (int) (startIndex  + reliabilityMetric.getTime() / delta);
		modelCheckingResult.getFailRates().addAll(resultExtracted.subList(startIndex, endIndex == 0 ? 1 : endIndex));
		startIndex += modelCheckingResult.getFailRates().size() - 1;
	}


	@Override
	public void visit(MeanTimeToFailure mttfMetric, SubMonitor monitor) {
		modelCheckingResult.setMeanTimeToFailure(resultExtracted.get(startIndex));
		startIndex++;
	}

	@Override
	public void visit(SteadyStateAvailability steadyStateavailabilityMetric, SubMonitor monitor) {
		modelCheckingResult.setSteadyStateAvailability(resultExtracted.get(startIndex));
		startIndex++;
	}
	
	@Override
	public void visit(Availability pointAvailabilityMetric, SubMonitor subMonitor) {
		
	}
	
	@Override
	public void visit(MinimumCutSet minimumCutSet, SubMonitor monitor) {
		
	}

	@Override
	public ModelCheckingResult checkModel(ModelCheckingQuery<? extends MarkovState> modelCheckingQuery, SubMonitor subMonitor) {
		statistics = new ModelCheckingStatistics();
		statistics.time = System.currentTimeMillis();
		
		Storm storm = new Storm(modelCheckingQuery.getMa(), delta, modelCheckingQuery.getMetrics());
		StormRunner<Double> stormRunner = createStormRunner(storm);
		
		modelCheckingResult = new ModelCheckingResult();
		
		try {
			resultExtracted  = stormRunner.run();
			for (IBaseMetric metric : modelCheckingQuery.getMetrics()) {
				metric.accept(this, subMonitor);
			}

		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		statistics.time = System.currentTimeMillis() - statistics.time;
		
		return modelCheckingResult;
	}
	
	@Override
	public ModelCheckingStatistics getStatistics() {
		return statistics;
	}
	
	/**
	 * Creates a storm runner for a storm program
	 * @param storm the storm program
	 * @return the storm runner
	 */
	protected StormRunner<Double> createStormRunner(Storm storm) {
		return new StormRunner<Double>(storm, stormExecutionEnvironment);
	}

	@Override
	public double getDelta() {
		return delta;
	}
}
