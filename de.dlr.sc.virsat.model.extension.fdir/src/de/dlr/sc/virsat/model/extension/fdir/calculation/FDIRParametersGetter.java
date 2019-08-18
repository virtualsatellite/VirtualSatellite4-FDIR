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
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.categories.propertyinstances.APropertyInstance;
import de.dlr.sc.virsat.model.extension.fdir.model.FDIRParameters;

/**
 * This class gets the probability levels from FDIRParameters
 * @author muel_s8
 *
 */

public class FDIRParametersGetter implements IInputGetter {

	private ValuePropertyGetter vpg = new ValuePropertyGetter();
	
	/**
	 * Checks if this getter is applicable for the input
	 * @param input the input
	 * @return true iff the input is a numeric array from the fdir concept
	 */
	public boolean isApplicableFor(EObject input) {
		// Check that we have an array...
		if (!(input instanceof CategoryAssignment)) {
			return false;
		}
		
		CategoryAssignment ca = (CategoryAssignment) input;
		return ca.getType().getName().equals(FDIRParameters.class.getSimpleName());
	}
	
	@Override
	public IExpressionResult get(EObject input) {
		if (!isApplicableFor(input)) {
			return null;
		}
		
		CategoryAssignment ca = (CategoryAssignment) input;
		FDIRParameters fdirParameters = new FDIRParameters(ca);
		List<NumberLiteralResult> results = new ArrayList<>();
		
		for (APropertyInstance pi : fdirParameters.getProbabilityLevels().getArrayInstance().getArrayInstances()) {
			NumberLiteralResult nlr = (NumberLiteralResult) vpg.get(pi);
			results.add(nlr);
		}
		
		for (APropertyInstance pi : fdirParameters.getDetectionLevels().getArrayInstance().getArrayInstances()) {
			NumberLiteralResult nlr = (NumberLiteralResult) vpg.get(pi);
			results.add(nlr);
		}
		
		return new ArrayResult(results);
	}

}
