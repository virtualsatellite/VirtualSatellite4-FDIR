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

import org.eclipse.core.expressions.PropertyTester;

import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;

/**
 * This class tests if a category assignment is of a specific type
 * @author muel_s8
 *
 */

public class CategoryAssignmentTester extends PropertyTester {
	
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		CategoryAssignment ca = (CategoryAssignment) receiver;
		String caName = ca.getType().getName();
		return caName.equals(expectedValue);
	}
}
