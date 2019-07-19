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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import de.dlr.sc.virsat.model.calculation.compute.IExpressionResult;
import de.dlr.sc.virsat.model.calculation.compute.IInputGetter;
import de.dlr.sc.virsat.model.calculation.compute.extensions.NumberLiteralResult;
import de.dlr.sc.virsat.model.calculation.compute.extensions.ValuePropertyGetter;
import de.dlr.sc.virsat.model.dvlm.categories.ATypeDefinition;
import de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.FloatProperty;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.ArrayInstance;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.concepts.util.ActiveConceptHelper;

/**
 * This class gets an ArrayResult from a DVLM array,
 * if the array is part of the FDIR concept.
 * @author muel_s8
 *
 */

public class ArrayGetter implements IInputGetter {

	private ValuePropertyGetter vpg = new ValuePropertyGetter();
	
	/**
	 * Checks if this getter is applicable for the input
	 * @param input the input
	 * @return true iff the input is a numeric array from the fdir concept
	 */
	public boolean isApplicableFor(EObject input) {
		// Check that we have an array...
		if (!(input instanceof ArrayInstance)) {
			return false;
		}
		
		ArrayInstance ai = (ArrayInstance) input;
		ATypeDefinition aiType = ai.getType();
		
		// ... over float ...
		if (!(aiType instanceof FloatProperty)) {
			return false;
		}
		
		// ... thats from the FDIR concept
		Concept concept = ActiveConceptHelper.getConcept(aiType);
		return concept.getName().equals("de.dlr.sc.virsat.model.extension.fdir");
	}
	
	@Override
	public IExpressionResult get(EObject input) {
		if (!isApplicableFor(input)) {
			return null;
		}
		
		ArrayInstance ai = (ArrayInstance) input;
		List<NumberLiteralResult> results = new ArrayList<>();
		for (APropertyInstance pi : ai.getArrayInstances()) {
			NumberLiteralResult nlr = (NumberLiteralResult) vpg.get(pi);
			results.add(nlr);
		}
		
		return new ArrayResult(results);
	}

}
