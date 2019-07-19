/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class tests the FDIR expression extension
 * @author muel_s8
 *
 */

public class FDIRExpressionExtensionTest {

	@Test
	public void testGetExtensions() {
		FDIRExpressionExtension extension = new FDIRExpressionExtension();
		assertTrue(extension.getTypeInstanceSetters().isEmpty());
		assertEquals(1, extension.getExpressionEvaluators().size());
		assertEquals(1, extension.getInputGetters().size());
	}

}
