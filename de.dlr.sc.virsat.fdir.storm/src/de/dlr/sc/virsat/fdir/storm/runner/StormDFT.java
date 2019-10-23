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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.SubMonitor;

import de.dlr.sc.virsat.fdir.core.metrics.Availability;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetricVisitor;
import de.dlr.sc.virsat.fdir.core.metrics.MTTF;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;
import de.dlr.sc.virsat.fdir.core.metrics.Reliability;
import de.dlr.sc.virsat.fdir.core.metrics.SteadyStateAvailability;
import de.dlr.sc.virsat.fdir.galileo.GalileoDFTWriter;
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft;
import de.dlr.sc.virsat.fdir.storm.files.InstanceFileGenerator;

/**
 * A storm runner for executing storm-dft
 * 
 * @author sascha
 *
 */

public class StormDFT implements IStormProgram<Double> {

	public static final String FILE_TYPE = "dft";

	private List<IBaseMetric> metrics = new ArrayList<>();

	private GalileoDft dft;
	private double delta;

	/**
	 * Adds a metric
	 * 
	 * @param metric
	 *            the metric to compute
	 */
	public void addMetric(IBaseMetric metric) {
		metrics.add(metric);
	}

	/**
	 * Sets the timeslice
	 * 
	 * @param delta
	 *            the timeslice
	 */
	public void setDelta(double delta) {
		this.delta = delta;
	}

	/**
	 * Set the input fault tree
	 * 
	 * @param dft
	 *            the input fault tree
	 */
	public void setGalileoDft(GalileoDft dft) {
		this.dft = dft;
	}

	@Override
	public String getExecutableName() {
		return "storm-dft";
	}

	@Override
	public String[] buildCommandWithArgs(String[] instanceFilePath) {
		List<String> commandWithArgs = new ArrayList<>();

		commandWithArgs.add(getExecutableName());
		commandWithArgs.add("-dft");
		commandWithArgs.add(instanceFilePath[0]);
		commandWithArgs.add("-symred");

		metrics.stream().forEach(metric -> {
			commandWithArgs.addAll(new MetricToStormArguments().toArguments(null, metric));
		});

		return commandWithArgs.stream().toArray(String[]::new);
	}

	@Override
	public String[] createInstanceFiles(InstanceFileGenerator fileGenerator) throws IOException {
		String instanceFilePath = fileGenerator.generateInstanceFile(FILE_TYPE);
		GalileoDFTWriter dftWriter = new GalileoDFTWriter(instanceFilePath);
		dftWriter.write(dft);
		return new String[] { instanceFilePath };
	}

	private static final String RESULT_LINES_START = "Result: [";

	@Override
	public List<Double> extractResult(List<String> result) {
		System.out.println(result);

		List<Double> resultExtracted = new ArrayList<>();

		for (String line : result) {
			if (line.startsWith(RESULT_LINES_START)) {
				String stippedLine = line.substring(RESULT_LINES_START.length(), line.length() - 1);
				StringTokenizer tokenizer = new StringTokenizer(stippedLine, ",");
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					resultExtracted.add(Double.parseDouble(token));
				}
			}
		}

		return resultExtracted;
	}

	/**
	 * Visitor for turning a metric into a storm argument
	 * 
	 * @author muel_s8
	 *
	 */
	private class MetricToStormArguments implements IBaseMetricVisitor {
		private List<String> stormArguments;

		/**
		 * Takes the metric and turns it into an argument for storm
		 * 
		 * @param metric
		 *            the metric
		 * @return the command line arguments to compute the metric via storm
		 */
		public List<String> toArguments(SubMonitor subMonitor, IBaseMetric metric) {
			stormArguments = new ArrayList<>();
			metric.accept(this, subMonitor);
			return stormArguments;
		}

		@Override
		public void visit(Reliability reliabilityMetric, SubMonitor subMonitor) {
			stormArguments.add("--timepoints");
			stormArguments.add("0");
			stormArguments.add(String.valueOf(reliabilityMetric.getTime()));
			stormArguments.add(String.valueOf(delta));
		}

		@Override
		public void visit(MTTF mttfMetric) {
			stormArguments.add("-mttf");
		}

		@Override
		public void visit(Availability pointAvailabilityMetric, SubMonitor subMonitor) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(SteadyStateAvailability steadyStateAvailabilityMetric) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visit(MinimumCutSet minimumCutSet) {
			// TODO Auto-generated method stub
			
		}
	}

}
