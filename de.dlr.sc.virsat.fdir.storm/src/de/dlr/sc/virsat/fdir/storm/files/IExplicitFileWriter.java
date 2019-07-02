/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.storm.files;

/**
 * 
 * @author yoge_re
 *
 */
public interface IExplicitFileWriter {
	String FAILED_STATE = "failed";
	String INITIAL_STATE = "init";
	String DEFAULT_STATE = "other";
	
	/**
	 * create the explicit file
	 */
	void writeFile();
}
