/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.converter.ma2beliefMa;

import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomaton;
import de.dlr.sc.virsat.fdir.core.markov.MarkovAutomatonBuilder;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.DFTState;
import de.dlr.sc.virsat.model.extension.fdir.converter.dft2ma.po.PODFTState;

public class MA2BeliefMAConverter {
	private BeliefStateSpaceGenerator beliefStateSpaceGenerator = new BeliefStateSpaceGenerator();
	private MarkovAutomatonBuilder<BeliefState> maBuilder = new MarkovAutomatonBuilder<>(beliefStateSpaceGenerator);
	
	public MarkovAutomaton<BeliefState> convert(MarkovAutomaton<DFTState> ma, PODFTState initialStateMa) {
		beliefStateSpaceGenerator.setMa(ma);
		beliefStateSpaceGenerator.setInitialStateMa(initialStateMa);
		
		MarkovAutomaton<BeliefState> beliefMa = maBuilder.build();
		return beliefMa;
	}
	
	public MarkovAutomatonBuilder<BeliefState> getMaBuilder() {
		return maBuilder;
	}
	
	public BeliefStateSpaceGenerator getBeliefStateSpaceGenerator() {
		return beliefStateSpaceGenerator;
	}
}
