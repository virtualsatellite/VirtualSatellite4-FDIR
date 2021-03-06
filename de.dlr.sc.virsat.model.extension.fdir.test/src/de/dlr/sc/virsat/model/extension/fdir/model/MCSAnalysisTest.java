/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.model.dvlm.structural.StructuralElement;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralFactory;
import de.dlr.sc.virsat.model.dvlm.structural.util.StructuralInstantiator;
import de.dlr.sc.virsat.project.editingDomain.VirSatEditingDomainRegistry;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;
import de.dlr.sc.virsat.project.resources.VirSatResourceSet;

// *****************************************************************
// * Import Statements
// *****************************************************************

// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class MCSAnalysisTest extends AMCSAnalysisTest {
	private static final double EPS = 0.00001;
	private IProject project;
	private VirSatTransactionalEditingDomain ed;

	@Before
	public void setup() throws CoreException {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("testMCSAnalysisProject");
		if (!project.exists()) {
			project.create(null);
			project.open(null);
		}

		VirSatResourceSet rs = VirSatResourceSet.getResourceSet(project, false);
		ed = VirSatEditingDomainRegistry.INSTANCE.getEd(rs);
	}

	@After
	public void tearDown() throws CoreException {
		if (project.exists()) {
			project.delete(true, null);
		}
	}

	@Test
	public void testPerform() {
		StructuralElement se = StructuralFactory.eINSTANCE.createStructuralElement();
		StructuralElementInstance sei = new StructuralInstantiator().generateInstance(se, "System");
		IProgressMonitor monitor = null;
		MCSAnalysis mcsAnalysis = new MCSAnalysis(concept);
		sei.getCategoryAssignments().add(mcsAnalysis.getTypeInstance());

		Fault fault = new Fault(concept);
		fault.setSeverity(Fault.SEVERITY_Critical_NAME);
		BasicEvent be1 = new BasicEvent(concept);
		be1.setName("B");
		be1.setHotFailureRate(1);
		fault.getBasicEvents().add(be1);
		BasicEvent be2 = new BasicEvent(concept);
		be2.setHotFailureRate(2);
		be2.setName("A");
		fault.getBasicEvents().add(be2);
		sei.getCategoryAssignments().add(fault.getTypeInstance());

		Command analysisCommand = mcsAnalysis.perform(ed, monitor);

		assertTrue(analysisCommand.canExecute());

		analysisCommand.execute();

		assertEquals(0, mcsAnalysis.getFaultTolerance());
		assertEquals(2, mcsAnalysis.getMinimumCutSets().size());
		
		CutSet cutSet1 = mcsAnalysis.getMinimumCutSets().get(0);
		assertEquals(be2, cutSet1.getBasicEvents().get(0));
		
		CutSet cutSet2 = mcsAnalysis.getMinimumCutSets().get(1);
		assertEquals(be1, cutSet2.getBasicEvents().get(0));
		assertEquals(1, cutSet2.getMeanTimeToFailure(), EPS);
	}
}
