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

import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

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
	
	@Override
	public Command create(EditingDomain ed, CategoryAssignment eObject) {
		DeleteCommand deleteCommand = (DeleteCommand) DeleteCommand.create(ed, eObject);
				
		// Make sure the cascade deletion only happens when we actually try to delete the object
		// Otherwise there are severe performance issues in the menues due to all the checking for valid deletion
		deleteCommand.append(new CommandWrapper() {
			@Override
			protected Command createCommand() {
				CompoundCommand cc = new CompoundCommand();
				Set<EObject> referencingInstances = getReferencingInstances(eObject, ed.getResourceSet());
				for (EObject referencingInstance : referencingInstances) {
					cc.append(DeleteCommand.create(ed, referencingInstance));
				}
				return cc;
			}
			
			@Override
			protected boolean prepare() {
				return true;
			}
			
			@Override
			public void execute() {
				if (command == null) {
					command = createCommand();
				}
				super.execute();
			}
		});
		
		return deleteCommand;
	}
}
