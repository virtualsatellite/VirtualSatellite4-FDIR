/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package de.dlr.sc.virsat.fdir.swtbot.test;

import java.util.List;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefConnectionEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.ps.model.ConfigurationTree;
import de.dlr.sc.virsat.model.extension.ps.model.ElementConfiguration;
import de.dlr.sc.virsat.swtbot.test.ASwtBotTestCase;


/**
 * This class tests the Ui behaviour of the Graphiti editor
 * @author piet_ci
 *
 */
public class GraphitiEditorTestCase extends ASwtBotTestCase {
	
	private SWTBotTreeItem repositoryNavigatorItem;	
	private SWTBotTreeItem configurationTree;
	private SWTBotTreeItem elementConfiguration;
	private SWTBotTreeItem fault1;
	private SWTBotTreeItem fault2;
	private SWTBotGefEditor diagramEditor;
	
	@Before
	public void before() throws Exception {
		super.before();
		// create the necessary items for the test
		Concept conceptFDIR = ConceptXmiLoader.loadConceptFromPlugin(de.dlr.sc.virsat.model.extension.fdir.Activator.getPluginId() + "/concept/concept.xmi");
		repositoryNavigatorItem = bot.tree().expandNode(SWTBOT_TEST_PROJECTNAME, "Repository");
		configurationTree = addElement(ConfigurationTree.class, conceptPs, repositoryNavigatorItem);
		elementConfiguration = addElement(ElementConfiguration.class, conceptPs, configurationTree);
		fault1 = addElement(Fault.class, conceptFDIR, configurationTree);
		fault2 = addElement(Fault.class, conceptFDIR, elementConfiguration);
		diagramEditor = openDiagramEditorForTreeItem(fault1);
	}
	
	@Test
	public void deleteElementConfigurationTest() {
		dragTreeItemToDiagramEditor(fault2, diagramEditor);
		diagramEditor.activateTool("Basic Event");		
		diagramEditor.click("ElementConfiguration.Fault");
		SWTBotGefEditPart basicEventPart = diagramEditor.getEditPart("BasicEvent");
		Assert.assertNotNull(basicEventPart);
		
		
		diagramEditor.activateTool("Propagation");
		diagramEditor.getEditPart("ElementConfiguration.Fault").children().get(0).click();
		
		
		diagramEditor.getEditPart("ConfigurationTree.Fault").children().get(1).click();
		diagramEditor.activateTool("Select");
		deleteEditPartInDiagramEditor(diagramEditor, "ElementConfiguration.Fault");
 		//Check if EditPart has been removed from the diagram editor as expected
		Assert.assertFalse(isEditPartPresentInDiagramEditor(diagramEditor, "ElementConfiguration.Fault"));
		Assert.assertFalse(isEditPartPresentInDiagramEditor(diagramEditor, "BasicEvent"));
		
 		//Check if associated fault model disappeared from the tree view		
		Assert.assertFalse(isTreeItemPresentInTreeView(fault2));		
	}
	
	@Test
	public void addANDGateTest() {
		dragTreeItemToDiagramEditor(fault2, diagramEditor);
		diagramEditor.activateTool("Basic Event");		
		diagramEditor.click("ElementConfiguration.Fault");
		SWTBotGefEditPart basicEventPart = diagramEditor.getEditPart("BasicEvent");
		Assert.assertNotNull(basicEventPart);
		
		
		diagramEditor.activateTool("Propagation");
		diagramEditor.getEditPart("ElementConfiguration.Fault").children().get(0).click();
		diagramEditor.getEditPart("ConfigurationTree.Fault").children().get(1).click();
		
		diagramEditor.activateTool("AND");
		
		SWTBotGefConnectionEditPart connection = diagramEditor.getEditPart("ElementConfiguration.Fault").children().get(0).sourceConnections().get(0);
		connection.click();
		
		SWTBotGefConnectionEditPart swtBotGefConnectionEditPart = diagramEditor.getEditPart("AND").children().get(0).sourceConnections().get(0);
		SWTBotGefConnectionEditPart swtBotGefConnectionEditPart2 = diagramEditor.getEditPart("AND").children().get(1).targetConnections().get(0);
		
		Assert.assertNotNull(swtBotGefConnectionEditPart);
		Assert.assertNotNull(swtBotGefConnectionEditPart2);
		
		deleteEditPartInDiagramEditor(diagramEditor, "AND");
		
		List<SWTBotGefConnectionEditPart> sourceConnections = diagramEditor.getEditPart("ElementConfiguration.Fault").children().get(0).sourceConnections();
		List<SWTBotGefConnectionEditPart> targetConnections = diagramEditor.getEditPart("ConfigurationTree.Fault").children().get(1).targetConnections();
		
		Assert.assertTrue(sourceConnections.isEmpty());
		Assert.assertTrue(targetConnections.isEmpty());
	}

	/**
	 * @param item Tree item for which a diagram editor gets opened
	 * @return Graphiti GEF Editor handle that SWTBot can work on
	 */
	protected SWTBotGefEditor openDiagramEditorForTreeItem(SWTBotTreeItem item) {
		SWTGefBot gefBot = new SWTGefBot();
		item.contextMenu().menu("Open Diagram Editor").click();
		waitForEditingDomainAndUiThread();		
		String editorTitle = bot.editors().get(1).getTitle();				
		SWTBotGefEditor editor = gefBot.gefEditor(editorTitle);
		return editor;
	}
	
	@Override
	protected void openCorePerspective() {
		bot.menu("Window").menu("Perspective").menu("Open Perspective").menu("Other...").click();
		waitForEditingDomainAndUiThread();
		bot.table().select("FDIR Default");
		bot.button("Open").click();
		waitForEditingDomainAndUiThread(); 
	}
}
