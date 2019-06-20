/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.propertyTester;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;




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
public class ConceptElementTester extends AConceptEnabledTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		CategoryAssignment ca = (CategoryAssignment) receiver;
		String conceptFqn = ActiveConceptHelper.getConcept(ca.getType()).getFullQualifiedName();
		boolean isConceptElement = conceptFqn.equals("de.dlr.sc.virsat.model.extension.fdir");
		
		return isConceptElement;
	}


}
