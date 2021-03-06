/*
 * generated by Xtext 2.22.0
 */
package de.dlr.sc.virsat.fdir.galileo.ide.contentassist.antlr;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.dlr.sc.virsat.fdir.galileo.ide.contentassist.antlr.internal.InternalDftParser;
import de.dlr.sc.virsat.fdir.galileo.services.DftGrammarAccess;
import java.util.Map;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ide.editor.contentassist.antlr.AbstractContentAssistParser;

public class DftParser extends AbstractContentAssistParser {

	@Singleton
	public static final class NameMappings {
		
		private final Map<AbstractElement, String> mappings;
		
		@Inject
		public NameMappings(DftGrammarAccess grammarAccess) {
			ImmutableMap.Builder<AbstractElement, String> builder = ImmutableMap.builder();
			init(builder, grammarAccess);
			this.mappings = builder.build();
		}
		
		public String getRuleName(AbstractElement element) {
			return mappings.get(element);
		}
		
		private static void init(ImmutableMap.Builder<AbstractElement, String> builder, DftGrammarAccess grammarAccess) {
			builder.put(grammarAccess.getGalileoDftAccess().getAlternatives_3(), "rule__GalileoDft__Alternatives_3");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getAlternatives_1(), "rule__GalileoBasicEvent__Alternatives_1");
			builder.put(grammarAccess.getGalileoNodeTypeAccess().getAlternatives(), "rule__GalileoNodeType__Alternatives");
			builder.put(grammarAccess.getNamedAccess().getTypeNameAlternatives_1_0(), "rule__Named__TypeNameAlternatives_1_0");
			builder.put(grammarAccess.getParametrizedAccess().getTypeNameAlternatives_1_0(), "rule__Parametrized__TypeNameAlternatives_1_0");
			builder.put(grammarAccess.getGalileoDftAccess().getGroup(), "rule__GalileoDft__Group__0");
			builder.put(grammarAccess.getGalileoDftAccess().getGroup_3_0(), "rule__GalileoDft__Group_3_0__0");
			builder.put(grammarAccess.getGalileoDftAccess().getGroup_3_1(), "rule__GalileoDft__Group_3_1__0");
			builder.put(grammarAccess.getGalileoGateAccess().getGroup(), "rule__GalileoGate__Group__0");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getGroup(), "rule__GalileoBasicEvent__Group__0");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getGroup_1_0(), "rule__GalileoBasicEvent__Group_1_0__0");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getGroup_1_1(), "rule__GalileoBasicEvent__Group_1_1__0");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getGroup_2(), "rule__GalileoBasicEvent__Group_2__0");
			builder.put(grammarAccess.getGalileoRepairActionAccess().getGroup(), "rule__GalileoRepairAction__Group__0");
			builder.put(grammarAccess.getGalileoRepairActionAccess().getGroup_4(), "rule__GalileoRepairAction__Group_4__0");
			builder.put(grammarAccess.getNamedAccess().getGroup(), "rule__Named__Group__0");
			builder.put(grammarAccess.getObserverAccess().getGroup(), "rule__Observer__Group__0");
			builder.put(grammarAccess.getParametrizedAccess().getGroup(), "rule__Parametrized__Group__0");
			builder.put(grammarAccess.getFloatAccess().getGroup(), "rule__Float__Group__0");
			builder.put(grammarAccess.getFloatAccess().getGroup_2(), "rule__Float__Group_2__0");
			builder.put(grammarAccess.getFloatAccess().getGroup_3(), "rule__Float__Group_3__0");
			builder.put(grammarAccess.getGalileoDftAccess().getRootAssignment_1(), "rule__GalileoDft__RootAssignment_1");
			builder.put(grammarAccess.getGalileoDftAccess().getGatesAssignment_3_0_0(), "rule__GalileoDft__GatesAssignment_3_0_0");
			builder.put(grammarAccess.getGalileoDftAccess().getBasicEventsAssignment_3_1_0(), "rule__GalileoDft__BasicEventsAssignment_3_1_0");
			builder.put(grammarAccess.getGalileoGateAccess().getNameAssignment_0(), "rule__GalileoGate__NameAssignment_0");
			builder.put(grammarAccess.getGalileoGateAccess().getTypeAssignment_1(), "rule__GalileoGate__TypeAssignment_1");
			builder.put(grammarAccess.getGalileoGateAccess().getChildrenAssignment_2(), "rule__GalileoGate__ChildrenAssignment_2");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getNameAssignment_0(), "rule__GalileoBasicEvent__NameAssignment_0");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getLambdaAssignment_1_0_2(), "rule__GalileoBasicEvent__LambdaAssignment_1_0_2");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getProbAssignment_1_1_2(), "rule__GalileoBasicEvent__ProbAssignment_1_1_2");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getDormAssignment_2_2(), "rule__GalileoBasicEvent__DormAssignment_2_2");
			builder.put(grammarAccess.getGalileoBasicEventAccess().getRepairActionsAssignment_3(), "rule__GalileoBasicEvent__RepairActionsAssignment_3");
			builder.put(grammarAccess.getGalileoRepairActionAccess().getRepairAssignment_2(), "rule__GalileoRepairAction__RepairAssignment_2");
			builder.put(grammarAccess.getGalileoRepairActionAccess().getNameAssignment_3(), "rule__GalileoRepairAction__NameAssignment_3");
			builder.put(grammarAccess.getGalileoRepairActionAccess().getObservartionsAssignment_4_1(), "rule__GalileoRepairAction__ObservartionsAssignment_4_1");
			builder.put(grammarAccess.getNamedAccess().getTypeNameAssignment_1(), "rule__Named__TypeNameAssignment_1");
			builder.put(grammarAccess.getObserverAccess().getObservablesAssignment_2(), "rule__Observer__ObservablesAssignment_2");
			builder.put(grammarAccess.getObserverAccess().getObservationRateAssignment_5(), "rule__Observer__ObservationRateAssignment_5");
			builder.put(grammarAccess.getParametrizedAccess().getTypeNameAssignment_1(), "rule__Parametrized__TypeNameAssignment_1");
			builder.put(grammarAccess.getParametrizedAccess().getParameterAssignment_3(), "rule__Parametrized__ParameterAssignment_3");
		}
	}
	
	@Inject
	private NameMappings nameMappings;

	@Inject
	private DftGrammarAccess grammarAccess;

	@Override
	protected InternalDftParser createParser() {
		InternalDftParser result = new InternalDftParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}

	@Override
	protected String getRuleName(AbstractElement element) {
		return nameMappings.getRuleName(element);
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
	
	public NameMappings getNameMappings() {
		return nameMappings;
	}
	
	public void setNameMappings(NameMappings nameMappings) {
		this.nameMappings = nameMappings;
	}
}
