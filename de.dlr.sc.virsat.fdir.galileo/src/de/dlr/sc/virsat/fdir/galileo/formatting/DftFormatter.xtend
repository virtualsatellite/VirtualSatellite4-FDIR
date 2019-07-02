/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.galileo.formatting

import com.google.inject.Inject
import de.dlr.sc.virsat.fdir.galileo.services.DftGrammarAccess
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter
import org.eclipse.xtext.formatting.impl.FormattingConfig

/**
 * This class contains custom formatting declarations.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#formatting
 * on how and when to use it.
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class DftFormatter extends AbstractDeclarativeFormatter {
	
	@Inject extension DftGrammarAccess
	
	override protected configureFormatting(FormattingConfig c) {
		for(comma: findKeywords(';')) {
			c.setNoLinewrap().before(comma)
			c.setNoSpace().before(comma)
			c.setLinewrap().after(comma)
		}
		
		for(equals: findKeywords('=')) {
			c.setNoLinewrap().before(equals)
			c.setNoSpace().after(equals)
			c.setNoSpace().before(equals)
			c.setLinewrap().after(equals)
		}
		
		c.setNoLinewrap().before(galileoGateAccess.childrenAssignment_2)
		c.setNoLinewrap().before(galileoNodeTypeRule);
		c.setNoLinewrap().after(galileoNodeTypeRule);
	}
}
