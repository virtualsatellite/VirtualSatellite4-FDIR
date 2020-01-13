/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.junit.Test;

import de.dlr.sc.virsat.concept.unittest.util.ConceptXmiLoader;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.Activator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeEdge;
import de.dlr.sc.virsat.project.structure.VirSatProjectCommons;
import de.dlr.sc.virsat.project.test.AProjectTestCase;

public class FaultValidatorTest extends AProjectTestCase {
	
	private Concept concept = ConceptXmiLoader.loadConceptFromPlugin(Activator.getPluginId() + "/concept/concept.xmi");
	
	@Test
	public void testValidate() throws IOException, CoreException {
		FaultValidator validator = new FaultValidator();
		
		Fault fault = new Fault(concept);
		ResourceImpl resource = new XMLResourceImpl();
		resource.getContents().add(fault.getTypeInstance());
		URI uri = URI.createPlatformResourceURI(testProject.getName() + "/testFile", true);
		resource.setURI(uri);
		resource.save(null);
		IResource iResource = VirSatProjectCommons.getWorkspaceResource(fault.getTypeInstance());
		
		assertTrue("The model is inititally valid", validator.validate(fault));
		assertEquals("No markers were created", 0, iResource.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE).length);
		
		FaultTreeEdge fte = new FaultTreeEdge(concept);
		
		fault.getFaultTree().getPropagations().add(fte);
		
		assertFalse("The model is no longer valid", validator.validate(fault));
		assertEquals("There is a marker now, one for each missing reference", 2, iResource.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE).length);
	}
}
