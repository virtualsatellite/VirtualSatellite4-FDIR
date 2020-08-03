/*******************************************************************************
 * Copyright (c) 2020 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.extension.fdir.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dlr.sc.virsat.fdir.core.util.IStatistics;
import de.dlr.sc.virsat.model.extension.fdir.model.FaultTreeNodeType;

public class FaultTreeStatistics implements IStatistics {
	//CHECKSTYLE:OFF
	public int countNodes;
	public boolean dynamic;
	public boolean repair;
	public boolean partialObservable;
	public int[] countNodeType = new int[FaultTreeNodeType.values().length];
	//CHECKSTYLE:ON
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Fault Tree Statistics: \n");
		sb.append("\t* #Nodes   		:\t\t" 		+ getPrintValue(countNodes) + "\n");
		sb.append("\t* Dynamic   		:\t\t" 		+ dynamic + "\n");
		sb.append("\t* Repair   		:\t\t" 		+ repair + "\n");
		sb.append("\t* PartialObservable:\t\t" 		+ partialObservable + "\n");
		for (int i = 0; i < countNodeType.length; ++i) {
			String nodeType = FaultTreeNodeType.values()[i].name();
			sb.append("\t* #" + nodeType + "   		:\t\t" 		+ getPrintValue(countNodeType[i]) + "\n");
		}
		return sb.toString();
	}
	
	public static List<String> getColumns() {
		List<String> columns = new ArrayList<>(Arrays.asList("countNodes", "dynamic", "repair", "partialObservable"));
		for (FaultTreeNodeType nodeType : FaultTreeNodeType.values()) {
			columns.add(nodeType.name());
		}
		return columns;
	}
	
	@Override
	public List<String> getValues() {
		List<String> values = new ArrayList<>(Arrays.asList(getPrintValue(countNodes),
				String.valueOf(dynamic), String.valueOf(repair), String.valueOf(partialObservable)));
		for (int i = 0; i < countNodeType.length; ++i) {
			values.add(getPrintValue(countNodeType[i]));
		}
		return values;
	}
}
