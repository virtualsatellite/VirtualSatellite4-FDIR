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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovState;
import de.dlr.sc.virsat.fdir.core.metrics.IBaseMetric;
import de.dlr.sc.virsat.fdir.storm.files.ExplicitDRNFileWriter;
import de.dlr.sc.virsat.fdir.storm.files.ExplicitPropertiesWriter;
import de.dlr.sc.virsat.fdir.storm.files.IFileProvider;
import de.dlr.sc.virsat.fdir.storm.files.InstanceFileGenerator;

/**
 * 
 * @author yoge_re
 * 
 */
public class Storm implements IStormProgram<Double> {

	public static final String SCHEDULE_FILE_NAME = "scheduler.txt";
	
	private IBaseMetric[] metrics;
	private MarkovAutomaton<? extends MarkovState> ma;
	private double delta;
	
	// Temporary data
	private Map<String, String> mapFileToContent = new HashMap<String, String>();
	
	/**
	 * 
	 * @param metrics
	 *            Metrics
	 * @param ma
	 *            Markov Automaton
	 * @param delta
	 *            timeslice
	 */
	public Storm(MarkovAutomaton<? extends MarkovState> ma, double delta, IBaseMetric... metrics) {
		this.metrics = metrics;
		this.ma = ma;
		this.delta = delta;
	}

	@Override
	public String getExecutableName() {
		return "storm";
	}

	@Override
	public String[] createInstanceFiles(InstanceFileGenerator fileGenerator) throws IOException {
		String drnFilePath = fileGenerator.generateInstanceFile(EXPLICIT_DRN_FILE);
		String propertiesFilePath = fileGenerator.generateInstanceFile(PROPERTIES_FILE);

		new ExplicitDRNFileWriter(ma, drnFilePath, metrics).writeFile();
		new ExplicitPropertiesWriter(delta, propertiesFilePath, metrics).writeFile();

		return new String[] { drnFilePath, propertiesFilePath };
	}

	@Override
	public String[] buildCommandWithArgs(String[] instanceFilePath, boolean schedule) {
		mapFileToContent.clear();
		
		List<String> commandWithArgs = new ArrayList<>();

		commandWithArgs.add(getExecutableName());
		commandWithArgs.add("--explicit-drn");
		commandWithArgs.add(instanceFilePath[0]);
		if (ma.isCTMC()) {
			commandWithArgs.add("--io:to-nondet");
		}
		commandWithArgs.add("--prop");
		commandWithArgs.add(instanceFilePath[1]);
		if (schedule) {
			commandWithArgs.add("--exportscheduler");
			commandWithArgs.add(SCHEDULE_FILE_NAME);
			mapFileToContent.put(SCHEDULE_FILE_NAME, null);
		}
		return commandWithArgs.stream().toArray(String[]::new);
	}

	private static final String RESULT_LINES_START = "Result (for initial states): ";

	@Override
	public List<Double> extractResult(List<String> result) {
		List<Double> resultExtracted = new ArrayList<Double>();
		for (String line : result) {
			if (line.startsWith(RESULT_LINES_START)) {
				String stippedLine = line.substring(RESULT_LINES_START.length(), line.length());
				StringTokenizer tokenizer = new StringTokenizer(stippedLine, ", ");

				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if (token.equals("inf")) {
						resultExtracted.add(Double.POSITIVE_INFINITY);
					} else {
						resultExtracted.add(Double.parseDouble(token));
					}
				}
			}
		}

		return resultExtracted;
	}
	
	@Override
	public void onRunFinish(IFileProvider fileProvider) {
		for (String fileName : mapFileToContent.keySet()) {
			mapFileToContent.put(fileName, fileProvider.getFile(fileName));
		}
	}
	
	private static final int SCHEDULE_START_LINE = 3;
	
	/**
	 * 
	 * @param result schedule text
	 * @return schedule map
	 */
	public Map<Integer, Integer> convertScheduleToMap(String result) {
		String[] lines = result.split("\n");
		Map<Integer, Integer> schedulerMap = new HashMap<>();
		for (int lineNumber = SCHEDULE_START_LINE; lineNumber < lines.length - 1; lineNumber++) {
			String[] contents = lines[lineNumber].toString().trim().split("\\s+");
			Integer key = Integer.valueOf(contents[0]);
			Integer value = Integer.valueOf(contents[1]);
			schedulerMap.put(key, value);
		}
		return schedulerMap;
	}
	
	/**
	 * Extracts the schedule from the last program run
	 * @return the schedule produced from the last program run
	 */
	public Map<Integer, Integer> extractSchedule() {
		return convertScheduleToMap(mapFileToContent.get(SCHEDULE_FILE_NAME));
	}
}
 