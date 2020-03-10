/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.handler;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.dlr.sc.virsat.apps.ui.handler.CopyAndOpenAppHandler;
import de.dlr.sc.virsat.model.extension.fdir.ui.Activator;

public class SoftwareFTGeneratorHandler extends AbstractHandler {

	private CopyAndOpenAppHandler copyGeneratorHandler = new CopyAndOpenAppHandler() {
		public static final String FAULT_TREE_GENERATOR_FILE_NAME = "FaultTreeWithFDIRMechanismGenerator.java";
		
		@Override
		protected String getAppName() {
			return FAULT_TREE_GENERATOR_FILE_NAME;
		}

		@Override
		protected InputStream getAppStream() throws IOException {
			return Activator.getResourceContentAsString("/resources/apps/ft-generation/" + FAULT_TREE_GENERATOR_FILE_NAME);
		}
	};
	
	private CopyAndOpenAppHandler copyGeneratorUIHandler = new CopyAndOpenAppHandler() {
		public static final String FAULT_TREE_GENERATOR_UI_FILE_NAME = "FDIRMechanismSelectionDialog.java";
		
		@Override
		protected String getAppName() {
			return FAULT_TREE_GENERATOR_UI_FILE_NAME;
		}

		@Override
		protected InputStream getAppStream() throws IOException {
			return Activator.getResourceContentAsString("/resources/apps/ft-generation/" + FAULT_TREE_GENERATOR_UI_FILE_NAME);
		}
	};

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage activePage = workbenchWindow.getActivePage();
		IWorkbenchPart activePart = activePage.getActivePart();
		
		copyGeneratorHandler.execute(event);
		
		activePage.activate(activePart);
		
		copyGeneratorUIHandler.execute(event);
		
		return null;
	}
}
