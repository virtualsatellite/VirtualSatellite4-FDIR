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

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.markov.modelchecker.IMarkovModelChecker;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.PointAvailability;
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
	private List<Double> failRates = new ArrayList<Double>();
	private double mttf;
	private List<Double> resultExtracted = new ArrayList<Double>();
	private int startIndex = 0;
	private double steadyStateAvailability;
	 
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
	public void visit(Reliability reliabilityMetric) {
		failRates.add((double) 0);
		int endIndex = (int) (startIndex  + reliabilityMetric.getTime() / delta);
		failRates.addAll(resultExtracted.subList(startIndex, endIndex == 0 ? 1 : endIndex));
		startIndex += failRates.size() - 1;
	}


	@Override
	public void visit(MTTF mttfMetric) {
		mttf = resultExtracted.get(startIndex);
		startIndex++;
	}
	
	@Override
	public double getMeanTimeToFailure() {
		return mttf;
	}

	@Override
	public List<Double> getFailRates() {
		return failRates;
	}

	@Override
	public double getSteadyStateAvailability() {
		return steadyStateAvailability;
	}

	@Override
	public void visit(SteadyStateAvailability steadyStateavailabilityMetric) {
		steadyStateAvailability = resultExtracted.get(startIndex);
		startIndex++;
		
	}

	@Override
	public void checkModel(MarkovAutomaton<? extends MarkovState> ma, IMetric... metrics) {
		Storm storm = new Storm(ma, delta, metrics);
	
		StormRunner<Double> stormRunner = new StormRunner<Double>(storm, stormExecutionEnvironment);
		try {
			resultExtracted  = stormRunner.run();
			for (IMetric metric : metrics) {
				metric.accept(this);
			}

		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(PointAvailability pointAvailabilityMetric) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Double> getPointAvailability() {
		return null;
	}

}
