/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.command;

import org.eclipse.emf.ecore.EObject;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.command.CreateDeleteWithReferencingCategoryAssignmentsCommand;
import de.dlr.sc.virsat.model.extension.fdir.model.RecoveryAutomaton;

/**
 * This class can create commands that delete a set of objects along
 * with all referencing objects
 * @author muel_s8
 *
 */

public class CreateDeleteWithReferencingFDIRCategoryAssignmentsCommand extends CreateDeleteWithReferencingCategoryAssignmentsCommand {

	@Override
	protected EObject getActualObjectToDelete(EObject referencingObject) {
		EObject objectToDelete = super.getActualObjectToDelete(referencingObject);
		
		if (objectToDelete instanceof CategoryAssignment) {
			CategoryAssignment caToDelete = (CategoryAssignment) objectToDelete;
			if (!caToDelete.getType().getName().equals(RecoveryAutomaton.class.getSimpleName())) {
				return caToDelete;
			}
		}

		return null;
	}
}
