/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.migrator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

// *****************************************************************
// * Class Declaration
// *****************************************************************

import org.junit.Test;

// *****************************************************************
// * Import Statements
// *****************************************************************


import de.dlr.sc.virsat.model.dvlm.DVLMFactory;
import de.dlr.sc.virsat.model.dvlm.Repository;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * VirSat DLR FDIR Concept
 * 
 */
public class Migrator1v2Test extends AMigrator1v2Test {		
	
	@Test
	public void testMigrator1v2() {
		Migrator1v2 testMigrator1v2 = new Migrator1v2();
		
		Repository repository = DVLMFactory.eINSTANCE.createRepository();
		repository.getActiveConcepts().add(conceptMigrateFromRepository);
		
		assertNull(ActiveConceptHelper.getCategory(conceptMigrateFromRepository, "TimeoutTransition"));
		assertNotNull(ActiveConceptHelper.getCategory(conceptMigrateFromRepository, "TimedTransition"));
		
		testMigrator1v2.migrate(conceptMigrateFrom, conceptMigrateFromRepository, conceptMigrateTo);
		
		assertNotNull(ActiveConceptHelper.getCategory(conceptMigrateFromRepository, "TimeoutTransition"));
		assertNull(ActiveConceptHelper.getCategory(conceptMigrateFromRepository, "TimedTransition"));
	}
	
}
