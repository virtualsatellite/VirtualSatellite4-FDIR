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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
 * Auto Generated Abstract Generator Gap Class
 * 
 * Don't Manually modify this class
 * 
 * 
 * 
 */
public class RecoveryAutomatonTest extends ARecoveryAutomatonTest {
	private IProject project;
	private VirSatTransactionalEditingDomain ed;

	@Before
	public void setup() throws CoreException {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject("testRecoveryAutomatonProject");
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
		RecoveryAutomaton ra = new RecoveryAutomaton(concept);
		FaultEventTransition transition = new FaultEventTransition(concept);
		
		State state1 = new State(concept);
		ra.getStates().add(state1);

		State state2 = new State(concept);
		ra.getStates().add(state2);

		transition.setFrom(state1);
		transition.setTo(state2);
		ra.getTransitions().add(transition);
		
		Command recAutomatonMinimizeCommand = ra
				.performMinimize((TransactionalEditingDomain) ed);
		ed.getCommandStack().execute(recAutomatonMinimizeCommand);
		
		final int EXPECTED_RA_SIZE = 1;
		assertEquals(EXPECTED_RA_SIZE, ra.getStates().size());
	}
}
