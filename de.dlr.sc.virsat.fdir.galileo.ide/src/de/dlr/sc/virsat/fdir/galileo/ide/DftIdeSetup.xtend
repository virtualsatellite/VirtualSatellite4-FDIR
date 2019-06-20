/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.galileo.ide

import com.google.inject.Guice
import de.dlr.sc.virsat.fdir.galileo.DftRuntimeModule
import de.dlr.sc.virsat.fdir.galileo.DftStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class DftIdeSetup extends DftStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new DftRuntimeModule, new DftIdeModule))
	}
	
}
