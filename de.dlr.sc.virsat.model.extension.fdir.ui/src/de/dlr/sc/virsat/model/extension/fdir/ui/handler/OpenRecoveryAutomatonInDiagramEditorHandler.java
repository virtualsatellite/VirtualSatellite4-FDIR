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

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DiagramEditor;

/**
 * A class that handles opening faults in the fault tree editor
 * @author muel_s8
 *
 */

public class OpenRecoveryAutomatonInDiagramEditorHandler extends AOpenInDiagramEdiorHandler {

	private static final String TYPE_ID = "recovery automata";
	private static final String EDITOR_ID = "de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.editor";
	private static final String EXTENSION = "dvlmra";
	
	@Override
	protected void onCreateNewDiagram(DiagramEditor editor, Diagram diagram) {
		AddContext addContext = new AddContext();
		addContext.setNewObject(firstSelectedEObject);
		addContext.setTargetContainer(diagram);
		IFeatureProvider fp = editor.getDiagramTypeProvider().getFeatureProvider();
		fp.addIfPossible(addContext);
		
		ed.saveAll();
		
		editor.updateDirtyState();
	}

	@Override
	protected String getTypeID() {
		return TYPE_ID;
	}

	@Override
	protected String getEditorID() {
		return EDITOR_ID;
	}

	@Override
	protected String getExtension() {
		return EXTENSION;
	}
}
