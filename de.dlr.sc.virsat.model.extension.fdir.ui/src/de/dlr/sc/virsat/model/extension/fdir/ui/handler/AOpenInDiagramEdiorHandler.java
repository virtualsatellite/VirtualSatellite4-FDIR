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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.dlr.sc.virsat.graphiti.util.DiagramHelper;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.project.resources.VirSatResourceSet;
import de.dlr.sc.virsat.project.structure.VirSatProjectCommons;
import de.dlr.sc.virsat.project.ui.editingDomain.handler.AEditingDomainCommandHandler;

/**
 * Abstract class for defining handlers for opening diagram editors from the navigator
 * @author muel_s8
 *
 */

public abstract class AOpenInDiagramEdiorHandler extends AEditingDomainCommandHandler {
	
	/**
	 * The type id of the diagram
	 * @return the type id
	 */
	protected abstract String getTypeID();
	
	/**
	 * The editor id of the editor
	 * @return the editor id
	 */
	protected abstract String getEditorID();
	
	/**
	 * The file extension
	 * @return the file extension
	 */
	protected abstract String getExtension();
	
	@Override
	protected void execute() {
		CategoryAssignment ca = (CategoryAssignment) firstSelectedEObject;
		VirSatResourceSet resourceSet = ed.getResourceSet();
		
		String diagramName = ca.getUuid().toString();
		
		StructuralElementInstance sei = (StructuralElementInstance) ca.eContainer();
		IFolder documentsFolder = VirSatProjectCommons.getDocumentFolder(sei);
	
		IFile diagramFile = documentsFolder.getFile(diagramName + "." +  getExtension());
		String path = diagramFile.getFullPath().toString(); 
		URI uri = URI.createPlatformResourceURI(path, true);
		
		Diagram diagram = null;
		boolean makeNewDiagram = !diagramFile.exists();
		
		if (makeNewDiagram) {	
			diagram = Graphiti.getPeCreateService().createDiagram(getTypeID(), diagramName, true);
			DiagramHelper.createDiagram(uri, diagram, resourceSet);
		}
		
		Resource resource = resourceSet.getResource(uri, true);
		diagram = (Diagram) resource.getContents().get(0);
		
		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(diagram.getDiagramTypeId());
		DiagramEditorInput editorInput = new DiagramEditorInput(EcoreUtil.getURI(diagram), providerId);
		DiagramEditor editor = openEditor(editorInput);
		
		if (makeNewDiagram && editor != null) {			
			onCreateNewDiagram(editor, diagram);
		}
	}
	
	/**
	 * Method to be overwritten by clients for custom reaction on creation of a new diagram
	 * @param editor the editor
	 * @param diagram the diagram
	 */
	protected void onCreateNewDiagram(DiagramEditor editor, Diagram diagram) {
		
	}
	
	/**
	 * Actually opens the editor
	 * @param editorInput the editor input
	 * @return the editor
	 */
	private DiagramEditor openEditor(DiagramEditorInput editorInput) {
		try {
			return (DiagramEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, getEditorID());
		} catch (PartInitException e) {
			String error = "Error while opening diagram editor";
			IStatus status = new Status(IStatus.ERROR, "de.dlr.sc.virsat.model.extension.fdir.ui", error, e);
			ErrorDialog.openError(null, "An error occurred", null, status);
			return null;
		}
	}
}
