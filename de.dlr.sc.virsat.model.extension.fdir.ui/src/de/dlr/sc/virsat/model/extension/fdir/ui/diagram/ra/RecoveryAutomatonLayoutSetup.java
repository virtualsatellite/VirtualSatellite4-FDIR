/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra;

import org.eclipse.elk.core.service.IDiagramLayoutConnector;

import com.google.inject.Binder;

import de.dlr.sc.virsat.model.extension.fdir.ui.diagram.VirSatGraphitiLayoutSetup;

/**
 * Setup for Recovery Automaton Diagram Layouting
 * @author muel_s8
 *
 */

public class RecoveryAutomatonLayoutSetup extends VirSatGraphitiLayoutSetup {
	
	@Override
	public boolean supports(String diagramType) {
		return diagramType.equals("recovery automata");
	}

	@Override
	protected LayoutModule createLayoutModule() {
		return new RecoveryAutomatonLayoutModule();
	}
	
	/**
	 * Guice module for the VirSat Graphiti connector.
	 */
	public static class RecoveryAutomatonLayoutModule extends LayoutModule {
		@Override
		public void configure(final Binder binder) {
			super.configure(binder);
			binder.bind(IDiagramLayoutConnector.class).to(RecoveryAutomatonDiagramLayoutConnector.class);
		}
	}
}
