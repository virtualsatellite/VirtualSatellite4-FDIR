/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.command;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import de.dlr.sc.virsat.model.concept.types.property.BeanPropertyFloat;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.calculation.OpClassifyPL;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRParameters;

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * 
 * 
 */
public class CreateAddFDIRParametersCommand extends ACreateAddFDIRParametersCommand {
	@Override
	public Command create(EditingDomain editingDomain, EObject owner, Concept activeConcept) {
		Command createCommand = super.create(editingDomain, owner, activeConcept);
		CompoundCommand cmd = new CompoundCommand(createCommand.getLabel());
		cmd.append(createCommand);
		cmd.append(new RecordingCommand((TransactionalEditingDomain) editingDomain) {
			@Override
			protected void doExecute() {
				CategoryAssignment ca = (CategoryAssignment) createCommand.getResult().iterator().next();
				FDIRParameters fdirParameters = new FDIRParameters(ca);
				for (int i = 0; i < OpClassifyPL.DEFAULT_PL_THRESHOLDS.length; ++i) {
					BeanPropertyFloat beanFloat = fdirParameters.getProbabilityLevels().get(i);
					beanFloat.setValueAsBaseUnit(OpClassifyPL.DEFAULT_PL_THRESHOLDS[i]);
				}
			}
		});
		
		return cmd;
	}
}
