/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.evaluator;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.fdir.core.markov.modelchecker.ModelCheckingResult;
import de.dlr.sc.virsat.fdir.core.metrics.MinimumCutSet;

/**
 * This class tests the DFT Metrics composer
 * @author muel_s8
 *
 */

public class DFTMetricsComposerTest {

	private DFTMetricsComposer dftMetricsComposer;
	private List<ModelCheckingResult> subModuleResults;

	@Before
	public void setUp() {
		dftMetricsComposer = new DFTMetricsComposer();
		
		ModelCheckingResult result1 = new ModelCheckingResult();
		Set<Object> minCut11 = new HashSet<>(Arrays.asList("a"));
		Set<Object> minCut12 = new HashSet<>(Arrays.asList("c", "b"));
		result1.getMinCutSets().add(minCut11);
		result1.getMinCutSets().add(minCut12);
	
		ModelCheckingResult result2 = new ModelCheckingResult();
		Set<Object> minCut21 = new HashSet<>(Arrays.asList("d", "e"));
		result2.getMinCutSets().add(minCut21);
		
		subModuleResults = Arrays.asList(result1, result2);
	}
	
	@Test
	public void testComposeMinimumCutSetOR() {
		ModelCheckingResult composedResult = dftMetricsComposer.compose(subModuleResults, null, 1, MinimumCutSet.MINCUTSET);
	
		final int EXPECTED_COUNT_MINCUTS = 3;
		assertEquals("Number of MinCut sets correct", EXPECTED_COUNT_MINCUTS, composedResult.getMinCutSets().size());
		assertThat("MinCut sets correct", composedResult.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList("a"))));
		assertThat("MinCut sets correct", composedResult.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList("c", "b"))));
		assertThat("MinCut sets correct", composedResult.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList("d", "e"))));
	}
	
	@Test
	public void testComposeMinimumCutSetAND() {
		ModelCheckingResult composedResult = dftMetricsComposer.compose(subModuleResults, null, subModuleResults.size(), MinimumCutSet.MINCUTSET);
	
		final int EXPECTED_COUNT_MINCUTS = 2;
		assertEquals("Number of MinCut sets correct", EXPECTED_COUNT_MINCUTS, composedResult.getMinCutSets().size());
		assertThat("MinCut sets correct", composedResult.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList("a", "d", "e"))));
		assertThat("MinCut sets correct", composedResult.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList("c", "b", "d", "e"))));
	}
	
	@Test
	public void testComposeMinimumCutSetANDRestricted() {
		final int MAX_MINCUT_SIZE = 3;
		ModelCheckingResult composedResult = dftMetricsComposer.compose(subModuleResults, null, subModuleResults.size(), new MinimumCutSet(MAX_MINCUT_SIZE));
	
		final int EXPECTED_COUNT_MINCUTS = 1;
		assertEquals("Number of MinCut sets correct", EXPECTED_COUNT_MINCUTS, composedResult.getMinCutSets().size());
		assertThat("MinCut sets correct", composedResult.getMinCutSets(), hasItem(new HashSet<>(Arrays.asList("a", "d", "e"))));
	}
}
