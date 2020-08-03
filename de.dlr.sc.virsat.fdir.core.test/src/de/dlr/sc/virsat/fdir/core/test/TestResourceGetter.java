/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Test utility class for getting files in the test plugin
 *
 */
public class TestResourceGetter {

	private String fragmentID;
	
	/**
	 * Standard constructor
	 * @param fragmentID for which plugin is this resource getter?
	 */
	public TestResourceGetter(String fragmentID) {
		this.fragmentID = fragmentID;
	}

	/**
	 * Method to access the fragments contents from the resource folder and to hand it back as an input stream
	 * @param resourcePath the path to the resource starting with "/resource/"
	 * @return the content of the resource as a stream
	 * @throws IOException throws
	 */
	public InputStream getResourceContentAsStream(String resourcePath) throws IOException {
		URL url = new URL("platform:/plugin/" + fragmentID + resourcePath);
		InputStream inputStream = url.openConnection().getInputStream();

		return inputStream;
	}
}
