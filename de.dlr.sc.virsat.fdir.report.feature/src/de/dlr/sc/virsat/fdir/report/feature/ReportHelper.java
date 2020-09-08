/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.report.feature;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.swt.SWT;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.project.structure.VirSatProjectCommons;

public class ReportHelper {
	/**
	 * @param fault is given fault
	 * @return image byte array that shows the fault tree diagram for given fault
	 */
	public byte[] getImageForFaultTree(Fault fault) {
		CategoryAssignment ca = fault.getTypeInstance();
		String diagramName = ca.getUuid().toString();

		StructuralElementInstance sei = (StructuralElementInstance) ca.eContainer();
		Resource resource = sei.eResource();
		URI seiUri = resource.getURI();

		String projectName = VirSatProjectCommons.getProjectNameByUri(seiUri);
		String structuralElementInstanceDocPath = VirSatProjectCommons.getStructuralElementInstanceDocumentPath(sei);

		String path = "/" + projectName + "/" + structuralElementInstanceDocPath + "/" + diagramName + "." + "dvlmft";
		URI uri = URI.createPlatformResourceURI(path, true);

		Resource resourceFromSet = resource.getResourceSet().getResource(uri, true);
		EObject eObject = resourceFromSet.getContents().get(0);

		Diagram diagram = (Diagram) eObject;
		byte[] bytes = GraphitiUi.getImageService().convertDiagramToBytes(diagram, SWT.IMAGE_JPEG);
		return bytes;
	}
}
