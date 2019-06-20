/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.fdir.galileo.ide.contentassist.antlr;

import com.google.inject.Inject;
import de.dlr.sc.virsat.fdir.galileo.ide.contentassist.antlr.internal.InternalDftParser;
import de.dlr.sc.virsat.fdir.galileo.services.DftGrammarAccess;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ide.editor.contentassist.antlr.AbstractContentAssistParser;

public class DftParser extends AbstractContentAssistParser {

	@Inject
	private DftGrammarAccess grammarAccess;

	private Map<AbstractElement, String> nameMappings;

	@Override
	protected InternalDftParser createParser() {
		InternalDftParser result = new InternalDftParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}

	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getGalileoDftAccess().getAlternatives_3(), "rule__GalileoDft__Alternatives_3");
					put(grammarAccess.getGalileoNodeTypeAccess().getAlternatives(), "rule__GalileoNodeType__Alternatives");
					put(grammarAccess.getNamedTypeAccess().getTypeNameAlternatives_0(), "rule__NamedType__TypeNameAlternatives_0");
					put(grammarAccess.getGalileoDftAccess().getGroup(), "rule__GalileoDft__Group__0");
					put(grammarAccess.getGalileoDftAccess().getGroup_3_0(), "rule__GalileoDft__Group_3_0__0");
					put(grammarAccess.getGalileoDftAccess().getGroup_3_1(), "rule__GalileoDft__Group_3_1__0");
					put(grammarAccess.getGalileoGateAccess().getGroup(), "rule__GalileoGate__Group__0");
					put(grammarAccess.getGalileoBasicEventAccess().getGroup(), "rule__GalileoBasicEvent__Group__0");
					put(grammarAccess.getGalileoBasicEventAccess().getGroup_4(), "rule__GalileoBasicEvent__Group_4__0");
					put(grammarAccess.getGalileoBasicEventAccess().getGroup_5(), "rule__GalileoBasicEvent__Group_5__0");
					put(grammarAccess.getObserverTypeAccess().getGroup(), "rule__ObserverType__Group__0");
					put(grammarAccess.getRDEPTypeAccess().getGroup(), "rule__RDEPType__Group__0");
					put(grammarAccess.getFloatAccess().getGroup(), "rule__Float__Group__0");
					put(grammarAccess.getFloatAccess().getGroup_2(), "rule__Float__Group_2__0");
					put(grammarAccess.getFloatAccess().getGroup_3(), "rule__Float__Group_3__0");
					put(grammarAccess.getGalileoDftAccess().getRootAssignment_1(), "rule__GalileoDft__RootAssignment_1");
					put(grammarAccess.getGalileoDftAccess().getGatesAssignment_3_0_0(), "rule__GalileoDft__GatesAssignment_3_0_0");
					put(grammarAccess.getGalileoDftAccess().getBasicEventsAssignment_3_1_0(), "rule__GalileoDft__BasicEventsAssignment_3_1_0");
					put(grammarAccess.getGalileoGateAccess().getNameAssignment_0(), "rule__GalileoGate__NameAssignment_0");
					put(grammarAccess.getGalileoGateAccess().getTypeAssignment_1(), "rule__GalileoGate__TypeAssignment_1");
					put(grammarAccess.getGalileoGateAccess().getChildrenAssignment_2(), "rule__GalileoGate__ChildrenAssignment_2");
					put(grammarAccess.getGalileoBasicEventAccess().getNameAssignment_0(), "rule__GalileoBasicEvent__NameAssignment_0");
					put(grammarAccess.getGalileoBasicEventAccess().getLambdaAssignment_3(), "rule__GalileoBasicEvent__LambdaAssignment_3");
					put(grammarAccess.getGalileoBasicEventAccess().getDormAssignment_4_2(), "rule__GalileoBasicEvent__DormAssignment_4_2");
					put(grammarAccess.getGalileoBasicEventAccess().getRepairAssignment_5_2(), "rule__GalileoBasicEvent__RepairAssignment_5_2");
					put(grammarAccess.getNamedTypeAccess().getTypeNameAssignment(), "rule__NamedType__TypeNameAssignment");
					put(grammarAccess.getObserverTypeAccess().getObservablesAssignment_1(), "rule__ObserverType__ObservablesAssignment_1");
					put(grammarAccess.getObserverTypeAccess().getObservationRateAssignment_4(), "rule__ObserverType__ObservationRateAssignment_4");
					put(grammarAccess.getRDEPTypeAccess().getRateFactorAssignment_2(), "rule__RDEPType__RateFactorAssignment_2");
				}
			};
		}
		return nameMappings.get(element);
	}
			
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}

	public DftGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(DftGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
