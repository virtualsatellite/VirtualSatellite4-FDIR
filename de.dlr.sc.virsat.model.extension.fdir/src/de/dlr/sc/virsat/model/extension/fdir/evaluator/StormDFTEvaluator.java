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
import java.util.List;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IMetric;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.storm.runner.StormDFT;
import de.dlr.sc.virsat.fdir.storm.runner.StormRunnerFactory;
import de.dlr.sc.virsat.model.extension.fdir.converter.galileo.DFT2GalileoDFT;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNode;
import de.dlr.sc.virsat.model.extension.fdir.preferences.FaultTreePreferences;

/**
 * Fault Tree Evaluator using the STORM Engine.
 * @author sascha
 *
 */

public class StormDFTEvaluator implements IFaultTreeEvaluator {
	
	private final double delta;
	private ModelCheckingResult modelCheckingResult;
	private StormRunnerFactory<Double> stormRunnerFactory = new StormRunnerFactory<>();
	
	/**
	 * Constructor for computing reliability and mean time to failure
	 * @param delta the timestep slice
	 */
	public StormDFTEvaluator(double delta) {
		this.delta = delta;
	}
	
	@Override
	public ModelCheckingResult evaluateFaultTree(FaultTreeNode root, FailableBasicEventsProvider failNodeProvider, SubMonitor subMonitor, IMetric... metrics) {
		
		DFT2GalileoDFT converter = new DFT2GalileoDFT(false);
		GalileoDft dft = converter.convert(root);
		modelCheckingResult = new ModelCheckingResult();
		
		try {
			StormDFT storm = new StormDFT();
			storm.setGalileoDft(dft);
			storm.setDelta(delta);
			
			for (IMetric metric : metrics) {
				if (metric instanceof IBaseMetric) {
					storm.addMetric((IBaseMetric) metric);
				}
			}
			
			List<Double> result = stormRunnerFactory.create(storm, FaultTreePreferences.getStormExecutionEnvironmentPreference()).run();
			
			modelCheckingResult.setMeanTimeToFailure(result.get(0));
			
			for (int i = 1; i < result.size(); ++i) {
				Double failRate = result.get(i);
				modelCheckingResult.getFailRates().add(failRate);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		
		return modelCheckingResult;
	}
	
	/**
	 * Sets the internal storm runner factory
	 * @param stormRunnerFactory the storm runner factory
	 */
	public void setStormRunnerFactory(StormRunnerFactory<Double> stormRunnerFactory) {
		this.stormRunnerFactory = stormRunnerFactory;
	}
}
