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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
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
public class ReliabilityAnalysisTest extends AReliabilityAnalysisTest {
	private IProject project;
	private VirSatTransactionalEditingDomain ed;

	@Before
	public void setup() throws CoreException {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("testReliabilityAnalysisProject");
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
		ReliabilityAnalysis reliabilityAnalysis = new ReliabilityAnalysis(concept);
		reliabilityAnalysis.setRemainingMissionTime(1);
		final double TEST_DELTA = 0.1;
		reliabilityAnalysis.setTimestep(TEST_DELTA);
		sei.getCategoryAssignments().add(reliabilityAnalysis.getTypeInstance());

		Command unexecutableCommand = reliabilityAnalysis.perform(ed, new NullProgressMonitor());

		assertEquals(UnexecutableCommand.INSTANCE, unexecutableCommand);

		Fault fault = new Fault(concept);
		fault.getReliabilityAnalysis().add(reliabilityAnalysis);
		BasicEvent be = new BasicEvent(concept);
		be.setHotFailureRate(1);
		fault.getBasicEvents().add(be);
		sei.getCategoryAssignments().add(fault.getTypeInstance());

		Command analysisCommand = reliabilityAnalysis.perform(ed, new NullProgressMonitor());

		assertTrue(analysisCommand.canExecute());

		analysisCommand.execute();

		final long EXPECTED_NUMBER_OF_RELIABILITY_VALUES = 11;
		assertEquals(EXPECTED_NUMBER_OF_RELIABILITY_VALUES, reliabilityAnalysis.getReliabilityCurve().size());
		final double EPS = 0.001;
		assertEquals(1, reliabilityAnalysis.getMeanTimeToFailure(), EPS);
	}
}
