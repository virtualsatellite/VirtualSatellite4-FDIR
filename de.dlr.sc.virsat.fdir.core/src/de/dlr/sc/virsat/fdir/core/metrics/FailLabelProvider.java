/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.core.metrics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class gives the criterion which labels a state should have to be considered failed
 * @author muel_s8
 *
 */

public class FailLabelProvider {
	
	public static final FailLabelProvider EMPTY_FAIL_LABEL_PROVIDER = new FailLabelProvider();
	public static final FailLabelProvider SINGLETON_FAILED = new FailLabelProvider(FailLabel.FAILED);
	public static final FailLabelProvider SINGLETON_OBSERVED = new FailLabelProvider(FailLabel.OBSERVED);
	public static final FailLabelProvider FAILED_OBSERVED = new FailLabelProvider(FailLabel.FAILED, FailLabel.OBSERVED);
	
	/**
	 * This enum lists the relevant fail labels
	 * @author muel_s8
	 *
	 */
	public enum FailLabel {
		FAILED, OBSERVED
	}

	private Set<FailLabel> failLabels;

	/**
	 * Standard constructor
	 */
	public FailLabelProvider() {
		this.failLabels = new HashSet<>();
	}

	/**
	 * Fail label provider for failure criteria
	 * @param failLabels the fail labels
	 */
	public FailLabelProvider(FailLabel... failLabels) {
		this();
		this.failLabels.addAll(Arrays.asList(failLabels));
	}
	
	/**
	 * Gets the encapsulated fail labels
	 * @return the fail labels
	 */
	public Set<FailLabel> getFailLabels() {
		return failLabels;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FailLabelProvider) {
			return failLabels.equals(((FailLabelProvider) obj).failLabels);
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return failLabels.hashCode();
	}
}
