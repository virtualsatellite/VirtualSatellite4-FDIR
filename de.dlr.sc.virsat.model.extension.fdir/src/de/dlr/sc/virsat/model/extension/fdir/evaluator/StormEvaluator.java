/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.storm.runner.IStormProgram;
import de.dlr.sc.virsat.fdir.storm.runner.StormDFT;
import de.dlr.sc.virsat.fdir.storm.runner.StormRunner;
import de.dlr.sc.virsat.model.extension.fdir.converter.DFT2GalileoDFT;
import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.preferences.FaultTreePreferences;
import de.dlr.sc.virsat.model.extension.fdir.recovery.RecoveryStrategy;

/**
 * Fault Tree Evaluator using the STORM Engine.
 * @author sascha
 *
 */

public class StormEvaluator implements IFaultTreeEvaluator {
	
	private final double delta;
	
	private List<Double> failRates;
	private double mttf;
	
	/**
	 * Constructor for computing reliability and mean time to failure
	 * @param delta the timestep slice
	 */
	public StormEvaluator(double delta) {
		this.delta = delta;
	}
	
	@Override
	public void evaluateFaultTree(FaultTreeNode root, IMetric... metrics) {
		
		DFT2GalileoDFT converter = new DFT2GalileoDFT(false);
		GalileoDft dft = converter.convert((Fault) root);
		
		try {
			StormDFT storm = new StormDFT();
			storm.setGalileoDft(dft);
			storm.setDelta(delta);
			
			for (IMetric metric : metrics) {
				storm.addMetric(metric);
			}
			
			StormRunner<Double> stormRunner = createStormRunner(storm);
			List<Double> result = stormRunner.run();
			
			mttf = result.get(0);
			
			failRates = new ArrayList<>();
			for (int i = 1; i < result.size(); ++i) {
				Double failRate = result.get(i);
				failRates.add(failRate);
			}
			
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the storm runner for the evaluator.
	 * Overwrite this method to mock a storm runner in the test cases
	 * @param storm the storm program to run
	 * @return a new storm runner
	 */
	protected StormRunner<Double> createStormRunner(IStormProgram<Double> storm) {
		return new StormRunner<>(storm, FaultTreePreferences.getStormExecutionEnvironmentPreference());
	}
	
	@Override
	public List<Double> getFailRates() {
		return failRates;
	}

	@Override
	public double getMeanTimeToFailure() {
		return mttf;
	}

	@Override
	public Set<Set<BasicEvent>> getMinimumCutSets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Double> getPointAvailability() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getSteadyStateAvailability() {
		// TODO Auto-generated method stub
		return 0;
	}





}
