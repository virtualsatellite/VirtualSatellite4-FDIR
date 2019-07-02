/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.galileo.tests

import com.google.inject.Inject
import de.dlr.sc.virsat.fdir.galileo.dft.GalileoDft
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import de.dlr.sc.virsat.fdir.galileo.tests.DftInjectorProvider

@RunWith(XtextRunner)
@InjectWith(DftInjectorProvider)
class DftParsingTest {
	@Inject
	ParseHelper<GalileoDft> parseHelper
	
	@Test 
	def void loadModel() {
		val result = parseHelper.parse('''
			toplevel "Fault";
			"Fault" lambda=0.5 dorm=0.1;
		''')
		Assert.assertNotNull(result)
		Assert.assertEquals("Name correct", "Fault", result.root.name)
		Assert.assertEquals("Lambda correct", "0.5", result.basicEvents.get(0).lambda);
		Assert.assertEquals("Dormancy correct", "0.1", result.basicEvents.get(0).dorm);
	}
}
