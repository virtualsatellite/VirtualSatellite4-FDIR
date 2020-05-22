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

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.model.State;
import de.dlr.sc.virsat.model.extension.fdir.model.TimeoutTransition;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageFaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageRecoveryAutomaton;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageState;
import de.dlr.sc.virsat.model.extension.fdir.ui.PluginXml.conceptImages.ConceptImageTimeoutTransition;

/**
 * Makes icons available for Graphiti.
 * @author muel_s8
 *
 */

public class RecoveryAutomatonDiagramImageProvider extends AbstractImageProvider {
	@Override
	protected void addAvailableImages() {
		addImageFilePath(RecoveryAutomaton.FULL_QUALIFIED_CATEGORY_NAME, ConceptImageRecoveryAutomaton.PATHTOIMAGE);
		addImageFilePath(State.FULL_QUALIFIED_CATEGORY_NAME, ConceptImageState.PATHTOIMAGE);
		addImageFilePath(FaultEventTransition.FULL_QUALIFIED_CATEGORY_NAME, ConceptImageFaultEventTransition.PATHTOIMAGE);
		addImageFilePath(TimeoutTransition.FULL_QUALIFIED_CATEGORY_NAME, ConceptImageTimeoutTransition.PATHTOIMAGE);
		addImageFilePath("OpenEditor", "platform:/plugin/de.dlr.sc.virsat.uiengine.ui/icons/VirSat_Component_Edit.png");
		addImageFilePath(RecoveryAutomaton.PROPERTY_INITIAL, "resources/icons/InitialState.gif");
		addImageFilePath("Checked", "resources/icons/Checked.gif");
		addImageFilePath("Unchecked", "resources/icons/Unchecked.gif");
		addImageFilePath("Comment", "resources/icons/comment.gif");
	}
}
