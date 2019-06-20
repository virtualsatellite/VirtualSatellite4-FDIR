/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.galileo.ui;

import com.google.inject.Injector;
import de.dlr.sc.virsat.fdir.galileo.ui.internal.GalileoActivator;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class DftExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return GalileoActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return GalileoActivator.getInstance().getInjector(GalileoActivator.DE_DLR_SC_VIRSAT_FDIR_GALILEO_DFT);
	}
	
}
