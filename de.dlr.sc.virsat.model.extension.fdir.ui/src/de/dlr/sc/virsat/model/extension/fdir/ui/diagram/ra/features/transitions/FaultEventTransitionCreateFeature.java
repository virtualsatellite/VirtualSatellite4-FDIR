/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.ui.diagram.ra.features.transitions;

import org.eclipse.graphiti.features.IFeatureProvider;

import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultEventTransition;
import de.dlr.sc.virsat.model.extension.fdir.model.Transition;

/**
 * Feature for creating new transitions in a graphiti recovery automaton diagram.
 * @author muel_s8
 *
 */

public class FaultEventTransitionCreateFeature extends TransitionCreateFeature {

	/**
	 * Default constructor.
	 * @param fp the feature provider.
	 */
	
	public FaultEventTransitionCreateFeature(IFeatureProvider fp) {
		super(fp, "Fault Transition", "Create a transition guarded by faults");
	}

	@Override
	public String getCreateImageId() {
		return FaultEventTransition.FULL_QUALIFIED_CATEGORY_NAME;
	}

	@Override
	public Transition createTransition(Concept concept) {
		return new FaultEventTransition(concept);
	}
	
}
