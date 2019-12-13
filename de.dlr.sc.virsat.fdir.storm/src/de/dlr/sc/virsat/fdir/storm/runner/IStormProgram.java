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
import java.util.List;

import de.dlr.sc.virsat.fdir.storm.files.IFileProvider;
import de.dlr.sc.virsat.fdir.storm.files.InstanceFileGenerator;

/**
 * This class provides an interface to STORM programs.
 * Each program should have an implementation of this interface
 * @author muel_s8
 *
 * @param <S> Return type of the storm program
 */
public interface IStormProgram<S> {
	String PROPERTIES_FILE = "prop";
	String EXPLICIT_DRN_FILE = "drn";
	
	/**
	 * The name of the storm binary
	 * @return the name of the storm binary
	 */
	String getExecutableName();
	
	/**
	 * Creates the input files
	 * @param fileGenerator the generator for creating instance files
	 * @throws IOException thrown if there was a problem in the write
	 * @return the paths to the created instance files
	 */
	String[] createInstanceFiles(InstanceFileGenerator fileGenerator) throws IOException;
	
	/**
	 * Build the actual invokation command
	 * @param instanceFilePath the location of the input file
	 * @param getSchedule get schedule or not
	 * @return the command line command
	 */
	String[] buildCommandWithArgs(String[] instanceFilePath, boolean getSchedule);
	
	/**
	 * Extracts the actual results from the result of a STORM call
	 * @param result result
	 * @return the extracted result
	 */
	List<S> extractResult(List<String> result);
	
	/**
	 * Callback after execution
	 * @param fileProvider a file provider for accessing files produced by the program run
	 */
	void onRunFinish(IFileProvider fileProvider);
}
