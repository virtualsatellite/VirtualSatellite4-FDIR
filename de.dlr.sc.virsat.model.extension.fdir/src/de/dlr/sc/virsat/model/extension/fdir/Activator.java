/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;


/**
 * Activator for loading the Z3 Plugin
 * 
 * @author muel_s8
 *
 */
public class Activator extends Plugin {
	
	private static BundleContext context;

	// The shared instance
	private static Activator plugin;
	
	private static String pluginId;
	
	/**
	 * getter method to retrieve the context
	 * 
	 * @return {@link BundleContext} the context of the bundle in the OSGi
	 *         Framework bundle context
	 */
	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		plugin = this;
		pluginId = getBundle().getSymbolicName();
		Activator.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		plugin = null;
		Activator.context = null;
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
	 * @return the plugin id
	 */
	public static String getPluginId() {
		return pluginId;
	}

}
