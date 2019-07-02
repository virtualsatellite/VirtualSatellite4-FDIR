/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
grammar InternalDft;

options {
	superClass=AbstractInternalAntlrParser;
}

@lexer::header {
package de.dlr.sc.virsat.fdir.galileo.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package de.dlr.sc.virsat.fdir.galileo.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.dlr.sc.virsat.fdir.galileo.services.DftGrammarAccess;

}

@parser::members {

 	private DftGrammarAccess grammarAccess;

    public InternalDftParser(TokenStream input, DftGrammarAccess grammarAccess) {
        this(input);
        this.grammarAccess = grammarAccess;
        registerRules(grammarAccess.getGrammar());
    }

    @Override
    protected String getFirstRuleName() {
    	return "GalileoDft";
   	}

   	@Override
   	protected DftGrammarAccess getGrammarAccess() {
   		return grammarAccess;
   	}

}

@rulecatch {
    catch (RecognitionException re) {
        recover(input,re);
        appendSkippedTokens();
    }
}

// Entry rule entryRuleGalileoDft
entryRuleGalileoDft returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getGalileoDftRule()); }
	iv_ruleGalileoDft=ruleGalileoDft
	{ $current=$iv_ruleGalileoDft.current; }
	EOF;

// Rule GalileoDft
ruleGalileoDft returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		otherlv_0='toplevel'
		{
			newLeafNode(otherlv_0, grammarAccess.getGalileoDftAccess().getToplevelKeyword_0());
		}
		(
			(
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getGalileoDftRule());
					}
				}
				otherlv_1=RULE_STRING
				{
					newLeafNode(otherlv_1, grammarAccess.getGalileoDftAccess().getRootGalileoFaultTreeNodeCrossReference_1_0());
				}
			)
		)
		otherlv_2=';'
		{
			newLeafNode(otherlv_2, grammarAccess.getGalileoDftAccess().getSemicolonKeyword_2());
		}
		(
			(
				(
					(
						{
							newCompositeNode(grammarAccess.getGalileoDftAccess().getGatesGalileoGateParserRuleCall_3_0_0_0());
						}
						lv_gates_3_0=ruleGalileoGate
						{
							if ($current==null) {
								$current = createModelElementForParent(grammarAccess.getGalileoDftRule());
							}
							add(
								$current,
								"gates",
								lv_gates_3_0,
								"de.dlr.sc.virsat.fdir.galileo.Dft.GalileoGate");
							afterParserOrEnumRuleCall();
						}
					)
				)
				otherlv_4=';'
				{
					newLeafNode(otherlv_4, grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_0_1());
				}
			)
			    |
			(
				(
					(
						{
							newCompositeNode(grammarAccess.getGalileoDftAccess().getBasicEventsGalileoBasicEventParserRuleCall_3_1_0_0());
						}
						lv_basicEvents_5_0=ruleGalileoBasicEvent
						{
							if ($current==null) {
								$current = createModelElementForParent(grammarAccess.getGalileoDftRule());
							}
							add(
								$current,
								"basicEvents",
								lv_basicEvents_5_0,
								"de.dlr.sc.virsat.fdir.galileo.Dft.GalileoBasicEvent");
							afterParserOrEnumRuleCall();
						}
					)
				)
				otherlv_6=';'
				{
					newLeafNode(otherlv_6, grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_1_1());
				}
			)
		)*
	)
;

// Entry rule entryRuleGalileoGate
entryRuleGalileoGate returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getGalileoGateRule()); }
	iv_ruleGalileoGate=ruleGalileoGate
	{ $current=$iv_ruleGalileoGate.current; }
	EOF;

// Rule GalileoGate
ruleGalileoGate returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				lv_name_0_0=RULE_STRING
				{
					newLeafNode(lv_name_0_0, grammarAccess.getGalileoGateAccess().getNameSTRINGTerminalRuleCall_0_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getGalileoGateRule());
					}
					setWithLastConsumed(
						$current,
						"name",
						lv_name_0_0,
						"org.eclipse.xtext.common.Terminals.STRING");
				}
			)
		)
		(
			(
				{
					newCompositeNode(grammarAccess.getGalileoGateAccess().getTypeGalileoNodeTypeParserRuleCall_1_0());
				}
				lv_type_1_0=ruleGalileoNodeType
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getGalileoGateRule());
					}
					set(
						$current,
						"type",
						lv_type_1_0,
						"de.dlr.sc.virsat.fdir.galileo.Dft.GalileoNodeType");
					afterParserOrEnumRuleCall();
				}
			)
		)
		(
			(
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getGalileoGateRule());
					}
				}
				otherlv_2=RULE_STRING
				{
					newLeafNode(otherlv_2, grammarAccess.getGalileoGateAccess().getChildrenGalileoFaultTreeNodeCrossReference_2_0());
				}
			)
		)*
	)
;

// Entry rule entryRuleGalileoBasicEvent
entryRuleGalileoBasicEvent returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getGalileoBasicEventRule()); }
	iv_ruleGalileoBasicEvent=ruleGalileoBasicEvent
	{ $current=$iv_ruleGalileoBasicEvent.current; }
	EOF;

// Rule GalileoBasicEvent
ruleGalileoBasicEvent returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				lv_name_0_0=RULE_STRING
				{
					newLeafNode(lv_name_0_0, grammarAccess.getGalileoBasicEventAccess().getNameSTRINGTerminalRuleCall_0_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getGalileoBasicEventRule());
					}
					setWithLastConsumed(
						$current,
						"name",
						lv_name_0_0,
						"org.eclipse.xtext.common.Terminals.STRING");
				}
			)
		)
		otherlv_1='lambda'
		{
			newLeafNode(otherlv_1, grammarAccess.getGalileoBasicEventAccess().getLambdaKeyword_1());
		}
		otherlv_2='='
		{
			newLeafNode(otherlv_2, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_2());
		}
		(
			(
				{
					newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getLambdaFloatParserRuleCall_3_0());
				}
				lv_lambda_3_0=ruleFloat
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
					}
					set(
						$current,
						"lambda",
						lv_lambda_3_0,
						"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
					afterParserOrEnumRuleCall();
				}
			)
		)
		(
			otherlv_4='dorm'
			{
				newLeafNode(otherlv_4, grammarAccess.getGalileoBasicEventAccess().getDormKeyword_4_0());
			}
			otherlv_5='='
			{
				newLeafNode(otherlv_5, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_4_1());
			}
			(
				(
					{
						newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getDormFloatParserRuleCall_4_2_0());
					}
					lv_dorm_6_0=ruleFloat
					{
						if ($current==null) {
							$current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
						}
						set(
							$current,
							"dorm",
							lv_dorm_6_0,
							"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
						afterParserOrEnumRuleCall();
					}
				)
			)
		)?
		(
			otherlv_7='repair'
			{
				newLeafNode(otherlv_7, grammarAccess.getGalileoBasicEventAccess().getRepairKeyword_5_0());
			}
			otherlv_8='='
			{
				newLeafNode(otherlv_8, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_5_1());
			}
			(
				(
					{
						newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getRepairFloatParserRuleCall_5_2_0());
					}
					lv_repair_9_0=ruleFloat
					{
						if ($current==null) {
							$current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
						}
						set(
							$current,
							"repair",
							lv_repair_9_0,
							"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
						afterParserOrEnumRuleCall();
					}
				)
			)
		)?
	)
;

// Entry rule entryRuleGalileoNodeType
entryRuleGalileoNodeType returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getGalileoNodeTypeRule()); }
	iv_ruleGalileoNodeType=ruleGalileoNodeType
	{ $current=$iv_ruleGalileoNodeType.current; }
	EOF;

// Rule GalileoNodeType
ruleGalileoNodeType returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		{
			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getNamedTypeParserRuleCall_0());
		}
		this_NamedType_0=ruleNamedType
		{
			$current = $this_NamedType_0.current;
			afterParserOrEnumRuleCall();
		}
		    |
		{
			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getObserverTypeParserRuleCall_1());
		}
		this_ObserverType_1=ruleObserverType
		{
			$current = $this_ObserverType_1.current;
			afterParserOrEnumRuleCall();
		}
		    |
		{
			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getRDEPTypeParserRuleCall_2());
		}
		this_RDEPType_2=ruleRDEPType
		{
			$current = $this_RDEPType_2.current;
			afterParserOrEnumRuleCall();
		}
	)
;

// Entry rule entryRuleNamedType
entryRuleNamedType returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getNamedTypeRule()); }
	iv_ruleNamedType=ruleNamedType
	{ $current=$iv_ruleNamedType.current; }
	EOF;

// Rule NamedType
ruleNamedType returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				lv_typeName_0_1='and'
				{
					newLeafNode(lv_typeName_0_1, grammarAccess.getNamedTypeAccess().getTypeNameAndKeyword_0_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_1, null);
				}
				    |
				lv_typeName_0_2='or'
				{
					newLeafNode(lv_typeName_0_2, grammarAccess.getNamedTypeAccess().getTypeNameOrKeyword_0_1());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_2, null);
				}
				    |
				lv_typeName_0_3=RULE_XOFY
				{
					newLeafNode(lv_typeName_0_3, grammarAccess.getNamedTypeAccess().getTypeNameXOFYTerminalRuleCall_0_2());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed(
						$current,
						"typeName",
						lv_typeName_0_3,
						"de.dlr.sc.virsat.fdir.galileo.Dft.XOFY");
				}
				    |
				lv_typeName_0_4='pand'
				{
					newLeafNode(lv_typeName_0_4, grammarAccess.getNamedTypeAccess().getTypeNamePandKeyword_0_3());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_4, null);
				}
				    |
				lv_typeName_0_5='pand_i'
				{
					newLeafNode(lv_typeName_0_5, grammarAccess.getNamedTypeAccess().getTypeNamePand_iKeyword_0_4());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_5, null);
				}
				    |
				lv_typeName_0_6='por'
				{
					newLeafNode(lv_typeName_0_6, grammarAccess.getNamedTypeAccess().getTypeNamePorKeyword_0_5());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_6, null);
				}
				    |
				lv_typeName_0_7='por_i'
				{
					newLeafNode(lv_typeName_0_7, grammarAccess.getNamedTypeAccess().getTypeNamePor_iKeyword_0_6());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_7, null);
				}
				    |
				lv_typeName_0_8='sand'
				{
					newLeafNode(lv_typeName_0_8, grammarAccess.getNamedTypeAccess().getTypeNameSandKeyword_0_7());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_8, null);
				}
				    |
				lv_typeName_0_9='hsp'
				{
					newLeafNode(lv_typeName_0_9, grammarAccess.getNamedTypeAccess().getTypeNameHspKeyword_0_8());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_9, null);
				}
				    |
				lv_typeName_0_10='wsp'
				{
					newLeafNode(lv_typeName_0_10, grammarAccess.getNamedTypeAccess().getTypeNameWspKeyword_0_9());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_10, null);
				}
				    |
				lv_typeName_0_11='csp'
				{
					newLeafNode(lv_typeName_0_11, grammarAccess.getNamedTypeAccess().getTypeNameCspKeyword_0_10());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_11, null);
				}
				    |
				lv_typeName_0_12='seq'
				{
					newLeafNode(lv_typeName_0_12, grammarAccess.getNamedTypeAccess().getTypeNameSeqKeyword_0_11());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_12, null);
				}
				    |
				lv_typeName_0_13='fdep'
				{
					newLeafNode(lv_typeName_0_13, grammarAccess.getNamedTypeAccess().getTypeNameFdepKeyword_0_12());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getNamedTypeRule());
					}
					setWithLastConsumed($current, "typeName", lv_typeName_0_13, null);
				}
			)
		)
	)
;

// Entry rule entryRuleObserverType
entryRuleObserverType returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getObserverTypeRule()); }
	iv_ruleObserverType=ruleObserverType
	{ $current=$iv_ruleObserverType.current; }
	EOF;

// Rule ObserverType
ruleObserverType returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		otherlv_0='observer'
		{
			newLeafNode(otherlv_0, grammarAccess.getObserverTypeAccess().getObserverKeyword_0());
		}
		(
			(
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getObserverTypeRule());
					}
				}
				otherlv_1=RULE_STRING
				{
					newLeafNode(otherlv_1, grammarAccess.getObserverTypeAccess().getObservablesGalileoFaultTreeNodeCrossReference_1_0());
				}
			)
		)*
		otherlv_2='obsRate'
		{
			newLeafNode(otherlv_2, grammarAccess.getObserverTypeAccess().getObsRateKeyword_2());
		}
		otherlv_3='='
		{
			newLeafNode(otherlv_3, grammarAccess.getObserverTypeAccess().getEqualsSignKeyword_3());
		}
		(
			(
				{
					newCompositeNode(grammarAccess.getObserverTypeAccess().getObservationRateFloatParserRuleCall_4_0());
				}
				lv_observationRate_4_0=ruleFloat
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getObserverTypeRule());
					}
					set(
						$current,
						"observationRate",
						lv_observationRate_4_0,
						"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
					afterParserOrEnumRuleCall();
				}
			)
		)
	)
;

// Entry rule entryRuleRDEPType
entryRuleRDEPType returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getRDEPTypeRule()); }
	iv_ruleRDEPType=ruleRDEPType
	{ $current=$iv_ruleRDEPType.current; }
	EOF;

// Rule RDEPType
ruleRDEPType returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		otherlv_0='rdep'
		{
			newLeafNode(otherlv_0, grammarAccess.getRDEPTypeAccess().getRdepKeyword_0());
		}
		otherlv_1='rateFactor'
		{
			newLeafNode(otherlv_1, grammarAccess.getRDEPTypeAccess().getRateFactorKeyword_1());
		}
		(
			(
				{
					newCompositeNode(grammarAccess.getRDEPTypeAccess().getRateFactorFloatParserRuleCall_2_0());
				}
				lv_rateFactor_2_0=ruleFloat
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getRDEPTypeRule());
					}
					set(
						$current,
						"rateFactor",
						lv_rateFactor_2_0,
						"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
					afterParserOrEnumRuleCall();
				}
			)
		)
	)
;

// Entry rule entryRuleFloat
entryRuleFloat returns [String current=null]:
	{ newCompositeNode(grammarAccess.getFloatRule()); }
	iv_ruleFloat=ruleFloat
	{ $current=$iv_ruleFloat.current.getText(); }
	EOF;

// Rule Float
ruleFloat returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			kw='-'
			{
				$current.merge(kw);
				newLeafNode(kw, grammarAccess.getFloatAccess().getHyphenMinusKeyword_0());
			}
		)?
		this_INT_1=RULE_INT
		{
			$current.merge(this_INT_1);
		}
		{
			newLeafNode(this_INT_1, grammarAccess.getFloatAccess().getINTTerminalRuleCall_1());
		}
		(
			kw='.'
			{
				$current.merge(kw);
				newLeafNode(kw, grammarAccess.getFloatAccess().getFullStopKeyword_2_0());
			}
			this_INT_3=RULE_INT
			{
				$current.merge(this_INT_3);
			}
			{
				newLeafNode(this_INT_3, grammarAccess.getFloatAccess().getINTTerminalRuleCall_2_1());
			}
		)?
		(
			kw='e'
			{
				$current.merge(kw);
				newLeafNode(kw, grammarAccess.getFloatAccess().getEKeyword_3_0());
			}
			(
				kw='-'
				{
					$current.merge(kw);
					newLeafNode(kw, grammarAccess.getFloatAccess().getHyphenMinusKeyword_3_1());
				}
			)?
			this_INT_6=RULE_INT
			{
				$current.merge(this_INT_6);
			}
			{
				newLeafNode(this_INT_6, grammarAccess.getFloatAccess().getINTTerminalRuleCall_3_2());
			}
		)?
	)
;

RULE_XOFY : RULE_INT 'of' RULE_INT;

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_INT : ('0'..'9')+;

RULE_STRING : ('"' ('\\' .|~(('\\'|'"')))* '"'|'\'' ('\\' .|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;
