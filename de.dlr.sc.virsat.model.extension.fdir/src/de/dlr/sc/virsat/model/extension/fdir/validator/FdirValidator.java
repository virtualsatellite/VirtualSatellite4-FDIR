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

import java.util.List;

import de.dlr.sc.virsat.model.concept.types.structural.BeanStructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.validator.IStructuralElementInstanceValidator;
import de.dlr.sc.virsat.model.extension.fdir.model.Fault;


// *****************************************************************
// * Class Declaration
// *****************************************************************

/**
 * Auto Generated Class inheriting from Generator Gap Class
 * 
 * This class is generated once, do your changes here
 * 
 * VirSat DLR FDIR Concept
 * 
 */
public class FdirValidator extends AFdirValidator implements IStructuralElementInstanceValidator {

	private FaultValidator faultValidator = new FaultValidator();
	
	@Override
	public boolean validate(StructuralElementInstance sei) {
		boolean allInfoValid = true;
		
		BeanStructuralElementInstance beanSei = new BeanStructuralElementInstance(sei);
		List<Fault> faults = beanSei.getAll(Fault.class);
		
		for (Fault fault : faults) {
			allInfoValid &= faultValidator.validate(fault);
		}
		
		return super.validate(sei) && allInfoValid;
	}
}
