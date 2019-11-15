/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ft;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

import de.dlr.sc.virsat.model.extension.fdir.model.BasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.model.FDEP;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;
import de.dlr.sc.virsat.model.extension.fdir.model.RDEP;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageAND;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageBasicEvent;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageDELAY;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageFDEP;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageFault;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageFaultTreeEdge;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageMONITOR;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageOR;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImagePAND;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImagePANDI;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImagePDEP;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImagePOR;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImagePORI;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageRDEP;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageSAND;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageSPARE;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageVOTE;

/**
 * Makes icons available for Graphiti.
 * @author muel_s8
 *
 */

public class FaultTreeDiagramImageProvider extends AbstractImageProvider {

	@Override
	protected void addAvailableImages() {
		addImageFilePath(FaultTreeNodeType.FAULT.toString(), ConceptImageFault.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.SPARE.toString(), ConceptImageSPARE.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.AND.toString(), ConceptImageAND.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.OR.toString(), ConceptImageOR.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.PAND.toString(), ConceptImagePAND.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.POR.toString(), ConceptImagePOR.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.PAND_I.toString(), ConceptImagePANDI.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.POR_I.toString(), ConceptImagePORI.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.VOTE.toString(), ConceptImageVOTE.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.SAND.toString(), ConceptImageSAND.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.FDEP.toString(), ConceptImageFDEP.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.RDEP.toString(), ConceptImageRDEP.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.PDEP.toString(), ConceptImagePDEP.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.MONITOR.toString(), ConceptImageMONITOR.PATHTOIMAGE);
		addImageFilePath(FaultTreeNodeType.DELAY.toString(), ConceptImageDELAY.PATHTOIMAGE);
		
		addImageFilePath(BasicEvent.FULL_QUALIFIED_CATEGORY_NAME, ConceptImageBasicEvent.PATHTOIMAGE);
		addImageFilePath(FaultTreeEdge.FULL_QUALIFIED_CATEGORY_NAME, ConceptImageFaultTreeEdge.PATHTOIMAGE);
		addImageFilePath(RDEP.FULL_QUALIFIED_CATEGORY_NAME, ConceptImageRDEP.PATHTOIMAGE);
		addImageFilePath(FDEP.FULL_QUALIFIED_CATEGORY_NAME, ConceptImageFDEP.PATHTOIMAGE);
		addImageFilePath("OpenEditor", "platform:/plugin/de.dlr.sc.virsat.uiengine.ui/icons/VirSat_Component_Edit.png");
		addImageFilePath("Comment", "resources/icons/comment.gif");
	}

}
