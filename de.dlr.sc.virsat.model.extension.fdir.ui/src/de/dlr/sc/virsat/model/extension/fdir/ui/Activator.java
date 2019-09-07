/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */

public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	private static String pluginId;

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/**
	 * Bundle context start mehtod
	 * @param context the context for starting the bundle
	 * @throws Exception in case things go wrong
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		setPluginId(getDefault().getBundle().getSymbolicName());
	}

	/**
	 * Bundle context stop mehtod
	 * 
	 * @param context the context for stopping the bundle
	 * @throws Exception in case things go wrong
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns the plugin id
	 *
	 * @return pluginId
	 */
	public static String getPluginId() {
		return pluginId;
	}

	/**
	 * Sets the plugin id
	 *
	 * @param pluginId the plugin id
	 */
	private static void setPluginId(String pluginId) {
		Activator.pluginId = pluginId;
	}
	
	/**
	 * Method to access the fragments contents from the resource folder and to hand
	 * it back as string
	 * 
	 * @param resourcePath
	 *            the path to the resource starting with "resource/"
	 * @return the content of the resource as string
	 * @throws IOException
	 *             throws
	 */
	public static InputStream getResourceContentAsString(String resourcePath) throws IOException {
		URL url = new URL("platform:/plugin/" + pluginId + resourcePath);
		InputStream inputStream = url.openConnection().getInputStream();

		return inputStream;
	}
}
