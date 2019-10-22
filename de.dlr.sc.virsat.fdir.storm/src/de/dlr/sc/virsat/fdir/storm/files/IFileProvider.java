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
 * This class provides a generic interface for accessing the file system
 * @author muel_s8
 *
 */

public interface IFileProvider {
	
	/**
	 * Gets a file of the given name
	 * @param fileName the file name
	 * @return returns the file content
	 */
	String getFile(String fileName);
}
