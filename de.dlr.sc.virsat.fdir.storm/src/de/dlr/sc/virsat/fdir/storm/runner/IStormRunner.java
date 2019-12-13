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
import java.util.List;

/**
 * Interface for storm runners.
 * Useful for mocking in test cases.
 * @author muel_s8
 *
 * @param <S>
 */

public interface IStormRunner<S> {
	
	/**
	 * Main method for calling the storm binary
	 * @return the textual result of the call
	 * @throws IOException thrown if there is a file problem
	 * @throws URISyntaxException thrown if there is a file problem
	 */
	List<S> run() throws IOException, URISyntaxException;
}
