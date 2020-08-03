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



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalDftParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_XOFY", "RULE_INT", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'toplevel'", "';'", "'lambda'", "'='", "'prob'", "'dorm'", "'repair'", "'observations'", "'and'", "'or'", "'pand'", "'pand_i'", "'por'", "'por_i'", "'sand'", "'hsp'", "'wsp'", "'csp'", "'seq'", "'fdep'", "'observer'", "'obsRate'", "'rdep'", "'delay'", "'-'", "'.'", "'e'"
    };
    public static final int RULE_XOFY=5;
    public static final int RULE_STRING=4;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__37=37;
    public static final int T__16=16;
    public static final int T__38=38;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__33=33;
    public static final int T__12=12;
    public static final int T__34=34;
    public static final int T__13=13;
    public static final int T__35=35;
    public static final int T__14=14;
    public static final int T__36=36;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_ID=7;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=6;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;

    // delegates
    // delegators


        public InternalDftParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalDftParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalDftParser.tokenNames; }
    public String getGrammarFileName() { return "InternalDft.g"; }



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




    // $ANTLR start "entryRuleGalileoDft"
    // InternalDft.g:64:1: entryRuleGalileoDft returns [EObject current=null] : iv_ruleGalileoDft= ruleGalileoDft EOF ;
    public final EObject entryRuleGalileoDft() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGalileoDft = null;


        try {
            // InternalDft.g:64:51: (iv_ruleGalileoDft= ruleGalileoDft EOF )
            // InternalDft.g:65:2: iv_ruleGalileoDft= ruleGalileoDft EOF
            {
             newCompositeNode(grammarAccess.getGalileoDftRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGalileoDft=ruleGalileoDft();

            state._fsp--;

             current =iv_ruleGalileoDft; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGalileoDft"


    // $ANTLR start "ruleGalileoDft"
    // InternalDft.g:71:1: ruleGalileoDft returns [EObject current=null] : (otherlv_0= 'toplevel' ( (otherlv_1= RULE_STRING ) ) otherlv_2= ';' ( ( ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';' ) | ( ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';' ) )* ) ;
    public final EObject ruleGalileoDft() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_gates_3_0 = null;

        EObject lv_basicEvents_5_0 = null;



        	enterRule();

        try {
            // InternalDft.g:77:2: ( (otherlv_0= 'toplevel' ( (otherlv_1= RULE_STRING ) ) otherlv_2= ';' ( ( ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';' ) | ( ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';' ) )* ) )
            // InternalDft.g:78:2: (otherlv_0= 'toplevel' ( (otherlv_1= RULE_STRING ) ) otherlv_2= ';' ( ( ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';' ) | ( ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';' ) )* )
            {
            // InternalDft.g:78:2: (otherlv_0= 'toplevel' ( (otherlv_1= RULE_STRING ) ) otherlv_2= ';' ( ( ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';' ) | ( ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';' ) )* )
            // InternalDft.g:79:3: otherlv_0= 'toplevel' ( (otherlv_1= RULE_STRING ) ) otherlv_2= ';' ( ( ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';' ) | ( ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';' ) )*
            {
            otherlv_0=(Token)match(input,12,FOLLOW_3); 

            			newLeafNode(otherlv_0, grammarAccess.getGalileoDftAccess().getToplevelKeyword_0());
            		
            // InternalDft.g:83:3: ( (otherlv_1= RULE_STRING ) )
            // InternalDft.g:84:4: (otherlv_1= RULE_STRING )
            {
            // InternalDft.g:84:4: (otherlv_1= RULE_STRING )
            // InternalDft.g:85:5: otherlv_1= RULE_STRING
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getGalileoDftRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_STRING,FOLLOW_4); 

            					newLeafNode(otherlv_1, grammarAccess.getGalileoDftAccess().getRootGalileoFaultTreeNodeCrossReference_1_0());
            				

            }


            }

            otherlv_2=(Token)match(input,13,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getGalileoDftAccess().getSemicolonKeyword_2());
            		
            // InternalDft.g:100:3: ( ( ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';' ) | ( ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';' ) )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_STRING) ) {
                    int LA1_2 = input.LA(2);

                    if ( (LA1_2==RULE_XOFY||(LA1_2>=20 && LA1_2<=32)||(LA1_2>=34 && LA1_2<=35)) ) {
                        alt1=1;
                    }
                    else if ( (LA1_2==14||LA1_2==16) ) {
                        alt1=2;
                    }


                }


                switch (alt1) {
            	case 1 :
            	    // InternalDft.g:101:4: ( ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';' )
            	    {
            	    // InternalDft.g:101:4: ( ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';' )
            	    // InternalDft.g:102:5: ( (lv_gates_3_0= ruleGalileoGate ) ) otherlv_4= ';'
            	    {
            	    // InternalDft.g:102:5: ( (lv_gates_3_0= ruleGalileoGate ) )
            	    // InternalDft.g:103:6: (lv_gates_3_0= ruleGalileoGate )
            	    {
            	    // InternalDft.g:103:6: (lv_gates_3_0= ruleGalileoGate )
            	    // InternalDft.g:104:7: lv_gates_3_0= ruleGalileoGate
            	    {

            	    							newCompositeNode(grammarAccess.getGalileoDftAccess().getGatesGalileoGateParserRuleCall_3_0_0_0());
            	    						
            	    pushFollow(FOLLOW_4);
            	    lv_gates_3_0=ruleGalileoGate();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getGalileoDftRule());
            	    							}
            	    							add(
            	    								current,
            	    								"gates",
            	    								lv_gates_3_0,
            	    								"de.dlr.sc.virsat.fdir.galileo.Dft.GalileoGate");
            	    							afterParserOrEnumRuleCall();
            	    						

            	    }


            	    }

            	    otherlv_4=(Token)match(input,13,FOLLOW_5); 

            	    					newLeafNode(otherlv_4, grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_0_1());
            	    				

            	    }


            	    }
            	    break;
            	case 2 :
            	    // InternalDft.g:127:4: ( ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';' )
            	    {
            	    // InternalDft.g:127:4: ( ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';' )
            	    // InternalDft.g:128:5: ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) ) otherlv_6= ';'
            	    {
            	    // InternalDft.g:128:5: ( (lv_basicEvents_5_0= ruleGalileoBasicEvent ) )
            	    // InternalDft.g:129:6: (lv_basicEvents_5_0= ruleGalileoBasicEvent )
            	    {
            	    // InternalDft.g:129:6: (lv_basicEvents_5_0= ruleGalileoBasicEvent )
            	    // InternalDft.g:130:7: lv_basicEvents_5_0= ruleGalileoBasicEvent
            	    {

            	    							newCompositeNode(grammarAccess.getGalileoDftAccess().getBasicEventsGalileoBasicEventParserRuleCall_3_1_0_0());
            	    						
            	    pushFollow(FOLLOW_4);
            	    lv_basicEvents_5_0=ruleGalileoBasicEvent();

            	    state._fsp--;


            	    							if (current==null) {
            	    								current = createModelElementForParent(grammarAccess.getGalileoDftRule());
            	    							}
            	    							add(
            	    								current,
            	    								"basicEvents",
            	    								lv_basicEvents_5_0,
            	    								"de.dlr.sc.virsat.fdir.galileo.Dft.GalileoBasicEvent");
            	    							afterParserOrEnumRuleCall();
            	    						

            	    }


            	    }

            	    otherlv_6=(Token)match(input,13,FOLLOW_5); 

            	    					newLeafNode(otherlv_6, grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_1_1());
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGalileoDft"


    // $ANTLR start "entryRuleGalileoGate"
    // InternalDft.g:157:1: entryRuleGalileoGate returns [EObject current=null] : iv_ruleGalileoGate= ruleGalileoGate EOF ;
    public final EObject entryRuleGalileoGate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGalileoGate = null;


        try {
            // InternalDft.g:157:52: (iv_ruleGalileoGate= ruleGalileoGate EOF )
            // InternalDft.g:158:2: iv_ruleGalileoGate= ruleGalileoGate EOF
            {
             newCompositeNode(grammarAccess.getGalileoGateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGalileoGate=ruleGalileoGate();

            state._fsp--;

             current =iv_ruleGalileoGate; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGalileoGate"


    // $ANTLR start "ruleGalileoGate"
    // InternalDft.g:164:1: ruleGalileoGate returns [EObject current=null] : ( ( (lv_name_0_0= RULE_STRING ) ) ( (lv_type_1_0= ruleGalileoNodeType ) ) ( (otherlv_2= RULE_STRING ) )* ) ;
    public final EObject ruleGalileoGate() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_2=null;
        EObject lv_type_1_0 = null;



        	enterRule();

        try {
            // InternalDft.g:170:2: ( ( ( (lv_name_0_0= RULE_STRING ) ) ( (lv_type_1_0= ruleGalileoNodeType ) ) ( (otherlv_2= RULE_STRING ) )* ) )
            // InternalDft.g:171:2: ( ( (lv_name_0_0= RULE_STRING ) ) ( (lv_type_1_0= ruleGalileoNodeType ) ) ( (otherlv_2= RULE_STRING ) )* )
            {
            // InternalDft.g:171:2: ( ( (lv_name_0_0= RULE_STRING ) ) ( (lv_type_1_0= ruleGalileoNodeType ) ) ( (otherlv_2= RULE_STRING ) )* )
            // InternalDft.g:172:3: ( (lv_name_0_0= RULE_STRING ) ) ( (lv_type_1_0= ruleGalileoNodeType ) ) ( (otherlv_2= RULE_STRING ) )*
            {
            // InternalDft.g:172:3: ( (lv_name_0_0= RULE_STRING ) )
            // InternalDft.g:173:4: (lv_name_0_0= RULE_STRING )
            {
            // InternalDft.g:173:4: (lv_name_0_0= RULE_STRING )
            // InternalDft.g:174:5: lv_name_0_0= RULE_STRING
            {
            lv_name_0_0=(Token)match(input,RULE_STRING,FOLLOW_6); 

            					newLeafNode(lv_name_0_0, grammarAccess.getGalileoGateAccess().getNameSTRINGTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getGalileoGateRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            // InternalDft.g:190:3: ( (lv_type_1_0= ruleGalileoNodeType ) )
            // InternalDft.g:191:4: (lv_type_1_0= ruleGalileoNodeType )
            {
            // InternalDft.g:191:4: (lv_type_1_0= ruleGalileoNodeType )
            // InternalDft.g:192:5: lv_type_1_0= ruleGalileoNodeType
            {

            					newCompositeNode(grammarAccess.getGalileoGateAccess().getTypeGalileoNodeTypeParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_5);
            lv_type_1_0=ruleGalileoNodeType();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getGalileoGateRule());
            					}
            					set(
            						current,
            						"type",
            						lv_type_1_0,
            						"de.dlr.sc.virsat.fdir.galileo.Dft.GalileoNodeType");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalDft.g:209:3: ( (otherlv_2= RULE_STRING ) )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==RULE_STRING) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalDft.g:210:4: (otherlv_2= RULE_STRING )
            	    {
            	    // InternalDft.g:210:4: (otherlv_2= RULE_STRING )
            	    // InternalDft.g:211:5: otherlv_2= RULE_STRING
            	    {

            	    					if (current==null) {
            	    						current = createModelElement(grammarAccess.getGalileoGateRule());
            	    					}
            	    				
            	    otherlv_2=(Token)match(input,RULE_STRING,FOLLOW_5); 

            	    					newLeafNode(otherlv_2, grammarAccess.getGalileoGateAccess().getChildrenGalileoFaultTreeNodeCrossReference_2_0());
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGalileoGate"


    // $ANTLR start "entryRuleGalileoBasicEvent"
    // InternalDft.g:226:1: entryRuleGalileoBasicEvent returns [EObject current=null] : iv_ruleGalileoBasicEvent= ruleGalileoBasicEvent EOF ;
    public final EObject entryRuleGalileoBasicEvent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGalileoBasicEvent = null;


        try {
            // InternalDft.g:226:58: (iv_ruleGalileoBasicEvent= ruleGalileoBasicEvent EOF )
            // InternalDft.g:227:2: iv_ruleGalileoBasicEvent= ruleGalileoBasicEvent EOF
            {
             newCompositeNode(grammarAccess.getGalileoBasicEventRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGalileoBasicEvent=ruleGalileoBasicEvent();

            state._fsp--;

             current =iv_ruleGalileoBasicEvent; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGalileoBasicEvent"


    // $ANTLR start "ruleGalileoBasicEvent"
    // InternalDft.g:233:1: ruleGalileoBasicEvent returns [EObject current=null] : ( ( (lv_name_0_0= RULE_STRING ) ) ( (otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) ) | (otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) ) ) ) (otherlv_7= 'dorm' otherlv_8= '=' ( (lv_dorm_9_0= ruleFloat ) ) )? ( (lv_repairActions_10_0= ruleGalileoRepairAction ) )* ) ;
    public final EObject ruleGalileoBasicEvent() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        Token otherlv_8=null;
        AntlrDatatypeRuleToken lv_lambda_3_0 = null;

        AntlrDatatypeRuleToken lv_prob_6_0 = null;

        AntlrDatatypeRuleToken lv_dorm_9_0 = null;

        EObject lv_repairActions_10_0 = null;



        	enterRule();

        try {
            // InternalDft.g:239:2: ( ( ( (lv_name_0_0= RULE_STRING ) ) ( (otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) ) | (otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) ) ) ) (otherlv_7= 'dorm' otherlv_8= '=' ( (lv_dorm_9_0= ruleFloat ) ) )? ( (lv_repairActions_10_0= ruleGalileoRepairAction ) )* ) )
            // InternalDft.g:240:2: ( ( (lv_name_0_0= RULE_STRING ) ) ( (otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) ) | (otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) ) ) ) (otherlv_7= 'dorm' otherlv_8= '=' ( (lv_dorm_9_0= ruleFloat ) ) )? ( (lv_repairActions_10_0= ruleGalileoRepairAction ) )* )
            {
            // InternalDft.g:240:2: ( ( (lv_name_0_0= RULE_STRING ) ) ( (otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) ) | (otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) ) ) ) (otherlv_7= 'dorm' otherlv_8= '=' ( (lv_dorm_9_0= ruleFloat ) ) )? ( (lv_repairActions_10_0= ruleGalileoRepairAction ) )* )
            // InternalDft.g:241:3: ( (lv_name_0_0= RULE_STRING ) ) ( (otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) ) | (otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) ) ) ) (otherlv_7= 'dorm' otherlv_8= '=' ( (lv_dorm_9_0= ruleFloat ) ) )? ( (lv_repairActions_10_0= ruleGalileoRepairAction ) )*
            {
            // InternalDft.g:241:3: ( (lv_name_0_0= RULE_STRING ) )
            // InternalDft.g:242:4: (lv_name_0_0= RULE_STRING )
            {
            // InternalDft.g:242:4: (lv_name_0_0= RULE_STRING )
            // InternalDft.g:243:5: lv_name_0_0= RULE_STRING
            {
            lv_name_0_0=(Token)match(input,RULE_STRING,FOLLOW_7); 

            					newLeafNode(lv_name_0_0, grammarAccess.getGalileoBasicEventAccess().getNameSTRINGTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getGalileoBasicEventRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            // InternalDft.g:259:3: ( (otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) ) | (otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) ) ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==14) ) {
                alt3=1;
            }
            else if ( (LA3_0==16) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // InternalDft.g:260:4: (otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) )
                    {
                    // InternalDft.g:260:4: (otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) )
                    // InternalDft.g:261:5: otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) )
                    {
                    otherlv_1=(Token)match(input,14,FOLLOW_8); 

                    					newLeafNode(otherlv_1, grammarAccess.getGalileoBasicEventAccess().getLambdaKeyword_1_0_0());
                    				
                    otherlv_2=(Token)match(input,15,FOLLOW_9); 

                    					newLeafNode(otherlv_2, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_1_0_1());
                    				
                    // InternalDft.g:269:5: ( (lv_lambda_3_0= ruleFloat ) )
                    // InternalDft.g:270:6: (lv_lambda_3_0= ruleFloat )
                    {
                    // InternalDft.g:270:6: (lv_lambda_3_0= ruleFloat )
                    // InternalDft.g:271:7: lv_lambda_3_0= ruleFloat
                    {

                    							newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getLambdaFloatParserRuleCall_1_0_2_0());
                    						
                    pushFollow(FOLLOW_10);
                    lv_lambda_3_0=ruleFloat();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
                    							}
                    							set(
                    								current,
                    								"lambda",
                    								lv_lambda_3_0,
                    								"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalDft.g:290:4: (otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) ) )
                    {
                    // InternalDft.g:290:4: (otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) ) )
                    // InternalDft.g:291:5: otherlv_4= 'prob' otherlv_5= '=' ( (lv_prob_6_0= ruleFloat ) )
                    {
                    otherlv_4=(Token)match(input,16,FOLLOW_8); 

                    					newLeafNode(otherlv_4, grammarAccess.getGalileoBasicEventAccess().getProbKeyword_1_1_0());
                    				
                    otherlv_5=(Token)match(input,15,FOLLOW_9); 

                    					newLeafNode(otherlv_5, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_1_1_1());
                    				
                    // InternalDft.g:299:5: ( (lv_prob_6_0= ruleFloat ) )
                    // InternalDft.g:300:6: (lv_prob_6_0= ruleFloat )
                    {
                    // InternalDft.g:300:6: (lv_prob_6_0= ruleFloat )
                    // InternalDft.g:301:7: lv_prob_6_0= ruleFloat
                    {

                    							newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getProbFloatParserRuleCall_1_1_2_0());
                    						
                    pushFollow(FOLLOW_10);
                    lv_prob_6_0=ruleFloat();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
                    							}
                    							set(
                    								current,
                    								"prob",
                    								lv_prob_6_0,
                    								"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }


                    }


                    }
                    break;

            }

            // InternalDft.g:320:3: (otherlv_7= 'dorm' otherlv_8= '=' ( (lv_dorm_9_0= ruleFloat ) ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==17) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalDft.g:321:4: otherlv_7= 'dorm' otherlv_8= '=' ( (lv_dorm_9_0= ruleFloat ) )
                    {
                    otherlv_7=(Token)match(input,17,FOLLOW_8); 

                    				newLeafNode(otherlv_7, grammarAccess.getGalileoBasicEventAccess().getDormKeyword_2_0());
                    			
                    otherlv_8=(Token)match(input,15,FOLLOW_9); 

                    				newLeafNode(otherlv_8, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_2_1());
                    			
                    // InternalDft.g:329:4: ( (lv_dorm_9_0= ruleFloat ) )
                    // InternalDft.g:330:5: (lv_dorm_9_0= ruleFloat )
                    {
                    // InternalDft.g:330:5: (lv_dorm_9_0= ruleFloat )
                    // InternalDft.g:331:6: lv_dorm_9_0= ruleFloat
                    {

                    						newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getDormFloatParserRuleCall_2_2_0());
                    					
                    pushFollow(FOLLOW_11);
                    lv_dorm_9_0=ruleFloat();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
                    						}
                    						set(
                    							current,
                    							"dorm",
                    							lv_dorm_9_0,
                    							"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalDft.g:349:3: ( (lv_repairActions_10_0= ruleGalileoRepairAction ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==18) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalDft.g:350:4: (lv_repairActions_10_0= ruleGalileoRepairAction )
            	    {
            	    // InternalDft.g:350:4: (lv_repairActions_10_0= ruleGalileoRepairAction )
            	    // InternalDft.g:351:5: lv_repairActions_10_0= ruleGalileoRepairAction
            	    {

            	    					newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getRepairActionsGalileoRepairActionParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_11);
            	    lv_repairActions_10_0=ruleGalileoRepairAction();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
            	    					}
            	    					add(
            	    						current,
            	    						"repairActions",
            	    						lv_repairActions_10_0,
            	    						"de.dlr.sc.virsat.fdir.galileo.Dft.GalileoRepairAction");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGalileoBasicEvent"


    // $ANTLR start "entryRuleGalileoRepairAction"
    // InternalDft.g:372:1: entryRuleGalileoRepairAction returns [EObject current=null] : iv_ruleGalileoRepairAction= ruleGalileoRepairAction EOF ;
    public final EObject entryRuleGalileoRepairAction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGalileoRepairAction = null;


        try {
            // InternalDft.g:372:60: (iv_ruleGalileoRepairAction= ruleGalileoRepairAction EOF )
            // InternalDft.g:373:2: iv_ruleGalileoRepairAction= ruleGalileoRepairAction EOF
            {
             newCompositeNode(grammarAccess.getGalileoRepairActionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGalileoRepairAction=ruleGalileoRepairAction();

            state._fsp--;

             current =iv_ruleGalileoRepairAction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGalileoRepairAction"


    // $ANTLR start "ruleGalileoRepairAction"
    // InternalDft.g:379:1: ruleGalileoRepairAction returns [EObject current=null] : (otherlv_0= 'repair' otherlv_1= '=' ( (lv_repair_2_0= ruleFloat ) ) ( (lv_name_3_0= RULE_STRING ) )? (otherlv_4= 'observations' ( (otherlv_5= RULE_STRING ) )* )? ) ;
    public final EObject ruleGalileoRepairAction() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_name_3_0=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        AntlrDatatypeRuleToken lv_repair_2_0 = null;



        	enterRule();

        try {
            // InternalDft.g:385:2: ( (otherlv_0= 'repair' otherlv_1= '=' ( (lv_repair_2_0= ruleFloat ) ) ( (lv_name_3_0= RULE_STRING ) )? (otherlv_4= 'observations' ( (otherlv_5= RULE_STRING ) )* )? ) )
            // InternalDft.g:386:2: (otherlv_0= 'repair' otherlv_1= '=' ( (lv_repair_2_0= ruleFloat ) ) ( (lv_name_3_0= RULE_STRING ) )? (otherlv_4= 'observations' ( (otherlv_5= RULE_STRING ) )* )? )
            {
            // InternalDft.g:386:2: (otherlv_0= 'repair' otherlv_1= '=' ( (lv_repair_2_0= ruleFloat ) ) ( (lv_name_3_0= RULE_STRING ) )? (otherlv_4= 'observations' ( (otherlv_5= RULE_STRING ) )* )? )
            // InternalDft.g:387:3: otherlv_0= 'repair' otherlv_1= '=' ( (lv_repair_2_0= ruleFloat ) ) ( (lv_name_3_0= RULE_STRING ) )? (otherlv_4= 'observations' ( (otherlv_5= RULE_STRING ) )* )?
            {
            otherlv_0=(Token)match(input,18,FOLLOW_8); 

            			newLeafNode(otherlv_0, grammarAccess.getGalileoRepairActionAccess().getRepairKeyword_0());
            		
            otherlv_1=(Token)match(input,15,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getGalileoRepairActionAccess().getEqualsSignKeyword_1());
            		
            // InternalDft.g:395:3: ( (lv_repair_2_0= ruleFloat ) )
            // InternalDft.g:396:4: (lv_repair_2_0= ruleFloat )
            {
            // InternalDft.g:396:4: (lv_repair_2_0= ruleFloat )
            // InternalDft.g:397:5: lv_repair_2_0= ruleFloat
            {

            					newCompositeNode(grammarAccess.getGalileoRepairActionAccess().getRepairFloatParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_12);
            lv_repair_2_0=ruleFloat();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getGalileoRepairActionRule());
            					}
            					set(
            						current,
            						"repair",
            						lv_repair_2_0,
            						"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalDft.g:414:3: ( (lv_name_3_0= RULE_STRING ) )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==RULE_STRING) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // InternalDft.g:415:4: (lv_name_3_0= RULE_STRING )
                    {
                    // InternalDft.g:415:4: (lv_name_3_0= RULE_STRING )
                    // InternalDft.g:416:5: lv_name_3_0= RULE_STRING
                    {
                    lv_name_3_0=(Token)match(input,RULE_STRING,FOLLOW_13); 

                    					newLeafNode(lv_name_3_0, grammarAccess.getGalileoRepairActionAccess().getNameSTRINGTerminalRuleCall_3_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getGalileoRepairActionRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"name",
                    						lv_name_3_0,
                    						"org.eclipse.xtext.common.Terminals.STRING");
                    				

                    }


                    }
                    break;

            }

            // InternalDft.g:432:3: (otherlv_4= 'observations' ( (otherlv_5= RULE_STRING ) )* )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==19) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // InternalDft.g:433:4: otherlv_4= 'observations' ( (otherlv_5= RULE_STRING ) )*
                    {
                    otherlv_4=(Token)match(input,19,FOLLOW_5); 

                    				newLeafNode(otherlv_4, grammarAccess.getGalileoRepairActionAccess().getObservationsKeyword_4_0());
                    			
                    // InternalDft.g:437:4: ( (otherlv_5= RULE_STRING ) )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==RULE_STRING) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // InternalDft.g:438:5: (otherlv_5= RULE_STRING )
                    	    {
                    	    // InternalDft.g:438:5: (otherlv_5= RULE_STRING )
                    	    // InternalDft.g:439:6: otherlv_5= RULE_STRING
                    	    {

                    	    						if (current==null) {
                    	    							current = createModelElement(grammarAccess.getGalileoRepairActionRule());
                    	    						}
                    	    					
                    	    otherlv_5=(Token)match(input,RULE_STRING,FOLLOW_5); 

                    	    						newLeafNode(otherlv_5, grammarAccess.getGalileoRepairActionAccess().getObservartionsGalileoFaultTreeNodeCrossReference_4_1_0());
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGalileoRepairAction"


    // $ANTLR start "entryRuleGalileoNodeType"
    // InternalDft.g:455:1: entryRuleGalileoNodeType returns [EObject current=null] : iv_ruleGalileoNodeType= ruleGalileoNodeType EOF ;
    public final EObject entryRuleGalileoNodeType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGalileoNodeType = null;


        try {
            // InternalDft.g:455:56: (iv_ruleGalileoNodeType= ruleGalileoNodeType EOF )
            // InternalDft.g:456:2: iv_ruleGalileoNodeType= ruleGalileoNodeType EOF
            {
             newCompositeNode(grammarAccess.getGalileoNodeTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGalileoNodeType=ruleGalileoNodeType();

            state._fsp--;

             current =iv_ruleGalileoNodeType; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGalileoNodeType"


    // $ANTLR start "ruleGalileoNodeType"
    // InternalDft.g:462:1: ruleGalileoNodeType returns [EObject current=null] : (this_Named_0= ruleNamed | this_Parametrized_1= ruleParametrized | this_Observer_2= ruleObserver ) ;
    public final EObject ruleGalileoNodeType() throws RecognitionException {
        EObject current = null;

        EObject this_Named_0 = null;

        EObject this_Parametrized_1 = null;

        EObject this_Observer_2 = null;



        	enterRule();

        try {
            // InternalDft.g:468:2: ( (this_Named_0= ruleNamed | this_Parametrized_1= ruleParametrized | this_Observer_2= ruleObserver ) )
            // InternalDft.g:469:2: (this_Named_0= ruleNamed | this_Parametrized_1= ruleParametrized | this_Observer_2= ruleObserver )
            {
            // InternalDft.g:469:2: (this_Named_0= ruleNamed | this_Parametrized_1= ruleParametrized | this_Observer_2= ruleObserver )
            int alt9=3;
            switch ( input.LA(1) ) {
            case RULE_XOFY:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
                {
                alt9=1;
                }
                break;
            case 34:
            case 35:
                {
                alt9=2;
                }
                break;
            case 32:
                {
                alt9=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // InternalDft.g:470:3: this_Named_0= ruleNamed
                    {

                    			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getNamedParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_Named_0=ruleNamed();

                    state._fsp--;


                    			current = this_Named_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalDft.g:479:3: this_Parametrized_1= ruleParametrized
                    {

                    			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getParametrizedParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Parametrized_1=ruleParametrized();

                    state._fsp--;


                    			current = this_Parametrized_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalDft.g:488:3: this_Observer_2= ruleObserver
                    {

                    			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getObserverParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Observer_2=ruleObserver();

                    state._fsp--;


                    			current = this_Observer_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGalileoNodeType"


    // $ANTLR start "entryRuleNamed"
    // InternalDft.g:500:1: entryRuleNamed returns [EObject current=null] : iv_ruleNamed= ruleNamed EOF ;
    public final EObject entryRuleNamed() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNamed = null;


        try {
            // InternalDft.g:500:46: (iv_ruleNamed= ruleNamed EOF )
            // InternalDft.g:501:2: iv_ruleNamed= ruleNamed EOF
            {
             newCompositeNode(grammarAccess.getNamedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNamed=ruleNamed();

            state._fsp--;

             current =iv_ruleNamed; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNamed"


    // $ANTLR start "ruleNamed"
    // InternalDft.g:507:1: ruleNamed returns [EObject current=null] : ( () ( ( (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' ) ) ) ) ;
    public final EObject ruleNamed() throws RecognitionException {
        EObject current = null;

        Token lv_typeName_1_1=null;
        Token lv_typeName_1_2=null;
        Token lv_typeName_1_3=null;
        Token lv_typeName_1_4=null;
        Token lv_typeName_1_5=null;
        Token lv_typeName_1_6=null;
        Token lv_typeName_1_7=null;
        Token lv_typeName_1_8=null;
        Token lv_typeName_1_9=null;
        Token lv_typeName_1_10=null;
        Token lv_typeName_1_11=null;
        Token lv_typeName_1_12=null;
        Token lv_typeName_1_13=null;


        	enterRule();

        try {
            // InternalDft.g:513:2: ( ( () ( ( (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' ) ) ) ) )
            // InternalDft.g:514:2: ( () ( ( (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' ) ) ) )
            {
            // InternalDft.g:514:2: ( () ( ( (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' ) ) ) )
            // InternalDft.g:515:3: () ( ( (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' ) ) )
            {
            // InternalDft.g:515:3: ()
            // InternalDft.g:516:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNamedAccess().getNamedAction_0(),
            					current);
            			

            }

            // InternalDft.g:522:3: ( ( (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' ) ) )
            // InternalDft.g:523:4: ( (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' ) )
            {
            // InternalDft.g:523:4: ( (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' ) )
            // InternalDft.g:524:5: (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' )
            {
            // InternalDft.g:524:5: (lv_typeName_1_1= 'and' | lv_typeName_1_2= 'or' | lv_typeName_1_3= RULE_XOFY | lv_typeName_1_4= 'pand' | lv_typeName_1_5= 'pand_i' | lv_typeName_1_6= 'por' | lv_typeName_1_7= 'por_i' | lv_typeName_1_8= 'sand' | lv_typeName_1_9= 'hsp' | lv_typeName_1_10= 'wsp' | lv_typeName_1_11= 'csp' | lv_typeName_1_12= 'seq' | lv_typeName_1_13= 'fdep' )
            int alt10=13;
            switch ( input.LA(1) ) {
            case 20:
                {
                alt10=1;
                }
                break;
            case 21:
                {
                alt10=2;
                }
                break;
            case RULE_XOFY:
                {
                alt10=3;
                }
                break;
            case 22:
                {
                alt10=4;
                }
                break;
            case 23:
                {
                alt10=5;
                }
                break;
            case 24:
                {
                alt10=6;
                }
                break;
            case 25:
                {
                alt10=7;
                }
                break;
            case 26:
                {
                alt10=8;
                }
                break;
            case 27:
                {
                alt10=9;
                }
                break;
            case 28:
                {
                alt10=10;
                }
                break;
            case 29:
                {
                alt10=11;
                }
                break;
            case 30:
                {
                alt10=12;
                }
                break;
            case 31:
                {
                alt10=13;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // InternalDft.g:525:6: lv_typeName_1_1= 'and'
                    {
                    lv_typeName_1_1=(Token)match(input,20,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_1, grammarAccess.getNamedAccess().getTypeNameAndKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalDft.g:536:6: lv_typeName_1_2= 'or'
                    {
                    lv_typeName_1_2=(Token)match(input,21,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_2, grammarAccess.getNamedAccess().getTypeNameOrKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_2, null);
                    					

                    }
                    break;
                case 3 :
                    // InternalDft.g:547:6: lv_typeName_1_3= RULE_XOFY
                    {
                    lv_typeName_1_3=(Token)match(input,RULE_XOFY,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_3, grammarAccess.getNamedAccess().getTypeNameXOFYTerminalRuleCall_1_0_2());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"typeName",
                    							lv_typeName_1_3,
                    							"de.dlr.sc.virsat.fdir.galileo.Dft.XOFY");
                    					

                    }
                    break;
                case 4 :
                    // InternalDft.g:562:6: lv_typeName_1_4= 'pand'
                    {
                    lv_typeName_1_4=(Token)match(input,22,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_4, grammarAccess.getNamedAccess().getTypeNamePandKeyword_1_0_3());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_4, null);
                    					

                    }
                    break;
                case 5 :
                    // InternalDft.g:573:6: lv_typeName_1_5= 'pand_i'
                    {
                    lv_typeName_1_5=(Token)match(input,23,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_5, grammarAccess.getNamedAccess().getTypeNamePand_iKeyword_1_0_4());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_5, null);
                    					

                    }
                    break;
                case 6 :
                    // InternalDft.g:584:6: lv_typeName_1_6= 'por'
                    {
                    lv_typeName_1_6=(Token)match(input,24,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_6, grammarAccess.getNamedAccess().getTypeNamePorKeyword_1_0_5());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_6, null);
                    					

                    }
                    break;
                case 7 :
                    // InternalDft.g:595:6: lv_typeName_1_7= 'por_i'
                    {
                    lv_typeName_1_7=(Token)match(input,25,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_7, grammarAccess.getNamedAccess().getTypeNamePor_iKeyword_1_0_6());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_7, null);
                    					

                    }
                    break;
                case 8 :
                    // InternalDft.g:606:6: lv_typeName_1_8= 'sand'
                    {
                    lv_typeName_1_8=(Token)match(input,26,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_8, grammarAccess.getNamedAccess().getTypeNameSandKeyword_1_0_7());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_8, null);
                    					

                    }
                    break;
                case 9 :
                    // InternalDft.g:617:6: lv_typeName_1_9= 'hsp'
                    {
                    lv_typeName_1_9=(Token)match(input,27,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_9, grammarAccess.getNamedAccess().getTypeNameHspKeyword_1_0_8());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_9, null);
                    					

                    }
                    break;
                case 10 :
                    // InternalDft.g:628:6: lv_typeName_1_10= 'wsp'
                    {
                    lv_typeName_1_10=(Token)match(input,28,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_10, grammarAccess.getNamedAccess().getTypeNameWspKeyword_1_0_9());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_10, null);
                    					

                    }
                    break;
                case 11 :
                    // InternalDft.g:639:6: lv_typeName_1_11= 'csp'
                    {
                    lv_typeName_1_11=(Token)match(input,29,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_11, grammarAccess.getNamedAccess().getTypeNameCspKeyword_1_0_10());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_11, null);
                    					

                    }
                    break;
                case 12 :
                    // InternalDft.g:650:6: lv_typeName_1_12= 'seq'
                    {
                    lv_typeName_1_12=(Token)match(input,30,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_12, grammarAccess.getNamedAccess().getTypeNameSeqKeyword_1_0_11());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_12, null);
                    					

                    }
                    break;
                case 13 :
                    // InternalDft.g:661:6: lv_typeName_1_13= 'fdep'
                    {
                    lv_typeName_1_13=(Token)match(input,31,FOLLOW_2); 

                    						newLeafNode(lv_typeName_1_13, grammarAccess.getNamedAccess().getTypeNameFdepKeyword_1_0_12());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getNamedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_13, null);
                    					

                    }
                    break;

            }


            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNamed"


    // $ANTLR start "entryRuleObserver"
    // InternalDft.g:678:1: entryRuleObserver returns [EObject current=null] : iv_ruleObserver= ruleObserver EOF ;
    public final EObject entryRuleObserver() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObserver = null;


        try {
            // InternalDft.g:678:49: (iv_ruleObserver= ruleObserver EOF )
            // InternalDft.g:679:2: iv_ruleObserver= ruleObserver EOF
            {
             newCompositeNode(grammarAccess.getObserverRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleObserver=ruleObserver();

            state._fsp--;

             current =iv_ruleObserver; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleObserver"


    // $ANTLR start "ruleObserver"
    // InternalDft.g:685:1: ruleObserver returns [EObject current=null] : ( () otherlv_1= 'observer' ( (otherlv_2= RULE_STRING ) )* otherlv_3= 'obsRate' otherlv_4= '=' ( (lv_observationRate_5_0= ruleFloat ) ) ) ;
    public final EObject ruleObserver() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        AntlrDatatypeRuleToken lv_observationRate_5_0 = null;



        	enterRule();

        try {
            // InternalDft.g:691:2: ( ( () otherlv_1= 'observer' ( (otherlv_2= RULE_STRING ) )* otherlv_3= 'obsRate' otherlv_4= '=' ( (lv_observationRate_5_0= ruleFloat ) ) ) )
            // InternalDft.g:692:2: ( () otherlv_1= 'observer' ( (otherlv_2= RULE_STRING ) )* otherlv_3= 'obsRate' otherlv_4= '=' ( (lv_observationRate_5_0= ruleFloat ) ) )
            {
            // InternalDft.g:692:2: ( () otherlv_1= 'observer' ( (otherlv_2= RULE_STRING ) )* otherlv_3= 'obsRate' otherlv_4= '=' ( (lv_observationRate_5_0= ruleFloat ) ) )
            // InternalDft.g:693:3: () otherlv_1= 'observer' ( (otherlv_2= RULE_STRING ) )* otherlv_3= 'obsRate' otherlv_4= '=' ( (lv_observationRate_5_0= ruleFloat ) )
            {
            // InternalDft.g:693:3: ()
            // InternalDft.g:694:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getObserverAccess().getObserverAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,32,FOLLOW_14); 

            			newLeafNode(otherlv_1, grammarAccess.getObserverAccess().getObserverKeyword_1());
            		
            // InternalDft.g:704:3: ( (otherlv_2= RULE_STRING ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==RULE_STRING) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalDft.g:705:4: (otherlv_2= RULE_STRING )
            	    {
            	    // InternalDft.g:705:4: (otherlv_2= RULE_STRING )
            	    // InternalDft.g:706:5: otherlv_2= RULE_STRING
            	    {

            	    					if (current==null) {
            	    						current = createModelElement(grammarAccess.getObserverRule());
            	    					}
            	    				
            	    otherlv_2=(Token)match(input,RULE_STRING,FOLLOW_14); 

            	    					newLeafNode(otherlv_2, grammarAccess.getObserverAccess().getObservablesGalileoFaultTreeNodeCrossReference_2_0());
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

            otherlv_3=(Token)match(input,33,FOLLOW_8); 

            			newLeafNode(otherlv_3, grammarAccess.getObserverAccess().getObsRateKeyword_3());
            		
            otherlv_4=(Token)match(input,15,FOLLOW_9); 

            			newLeafNode(otherlv_4, grammarAccess.getObserverAccess().getEqualsSignKeyword_4());
            		
            // InternalDft.g:725:3: ( (lv_observationRate_5_0= ruleFloat ) )
            // InternalDft.g:726:4: (lv_observationRate_5_0= ruleFloat )
            {
            // InternalDft.g:726:4: (lv_observationRate_5_0= ruleFloat )
            // InternalDft.g:727:5: lv_observationRate_5_0= ruleFloat
            {

            					newCompositeNode(grammarAccess.getObserverAccess().getObservationRateFloatParserRuleCall_5_0());
            				
            pushFollow(FOLLOW_2);
            lv_observationRate_5_0=ruleFloat();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getObserverRule());
            					}
            					set(
            						current,
            						"observationRate",
            						lv_observationRate_5_0,
            						"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleObserver"


    // $ANTLR start "entryRuleParametrized"
    // InternalDft.g:748:1: entryRuleParametrized returns [EObject current=null] : iv_ruleParametrized= ruleParametrized EOF ;
    public final EObject entryRuleParametrized() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParametrized = null;


        try {
            // InternalDft.g:748:53: (iv_ruleParametrized= ruleParametrized EOF )
            // InternalDft.g:749:2: iv_ruleParametrized= ruleParametrized EOF
            {
             newCompositeNode(grammarAccess.getParametrizedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleParametrized=ruleParametrized();

            state._fsp--;

             current =iv_ruleParametrized; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParametrized"


    // $ANTLR start "ruleParametrized"
    // InternalDft.g:755:1: ruleParametrized returns [EObject current=null] : ( () ( ( (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' ) ) ) otherlv_2= '=' ( (lv_parameter_3_0= ruleFloat ) ) ) ;
    public final EObject ruleParametrized() throws RecognitionException {
        EObject current = null;

        Token lv_typeName_1_1=null;
        Token lv_typeName_1_2=null;
        Token otherlv_2=null;
        AntlrDatatypeRuleToken lv_parameter_3_0 = null;



        	enterRule();

        try {
            // InternalDft.g:761:2: ( ( () ( ( (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' ) ) ) otherlv_2= '=' ( (lv_parameter_3_0= ruleFloat ) ) ) )
            // InternalDft.g:762:2: ( () ( ( (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' ) ) ) otherlv_2= '=' ( (lv_parameter_3_0= ruleFloat ) ) )
            {
            // InternalDft.g:762:2: ( () ( ( (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' ) ) ) otherlv_2= '=' ( (lv_parameter_3_0= ruleFloat ) ) )
            // InternalDft.g:763:3: () ( ( (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' ) ) ) otherlv_2= '=' ( (lv_parameter_3_0= ruleFloat ) )
            {
            // InternalDft.g:763:3: ()
            // InternalDft.g:764:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getParametrizedAccess().getParametrizedAction_0(),
            					current);
            			

            }

            // InternalDft.g:770:3: ( ( (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' ) ) )
            // InternalDft.g:771:4: ( (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' ) )
            {
            // InternalDft.g:771:4: ( (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' ) )
            // InternalDft.g:772:5: (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' )
            {
            // InternalDft.g:772:5: (lv_typeName_1_1= 'rdep' | lv_typeName_1_2= 'delay' )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==34) ) {
                alt12=1;
            }
            else if ( (LA12_0==35) ) {
                alt12=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // InternalDft.g:773:6: lv_typeName_1_1= 'rdep'
                    {
                    lv_typeName_1_1=(Token)match(input,34,FOLLOW_8); 

                    						newLeafNode(lv_typeName_1_1, grammarAccess.getParametrizedAccess().getTypeNameRdepKeyword_1_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getParametrizedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_1, null);
                    					

                    }
                    break;
                case 2 :
                    // InternalDft.g:784:6: lv_typeName_1_2= 'delay'
                    {
                    lv_typeName_1_2=(Token)match(input,35,FOLLOW_8); 

                    						newLeafNode(lv_typeName_1_2, grammarAccess.getParametrizedAccess().getTypeNameDelayKeyword_1_0_1());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getParametrizedRule());
                    						}
                    						setWithLastConsumed(current, "typeName", lv_typeName_1_2, null);
                    					

                    }
                    break;

            }


            }


            }

            otherlv_2=(Token)match(input,15,FOLLOW_9); 

            			newLeafNode(otherlv_2, grammarAccess.getParametrizedAccess().getEqualsSignKeyword_2());
            		
            // InternalDft.g:801:3: ( (lv_parameter_3_0= ruleFloat ) )
            // InternalDft.g:802:4: (lv_parameter_3_0= ruleFloat )
            {
            // InternalDft.g:802:4: (lv_parameter_3_0= ruleFloat )
            // InternalDft.g:803:5: lv_parameter_3_0= ruleFloat
            {

            					newCompositeNode(grammarAccess.getParametrizedAccess().getParameterFloatParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_parameter_3_0=ruleFloat();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getParametrizedRule());
            					}
            					set(
            						current,
            						"parameter",
            						lv_parameter_3_0,
            						"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParametrized"


    // $ANTLR start "entryRuleFloat"
    // InternalDft.g:824:1: entryRuleFloat returns [String current=null] : iv_ruleFloat= ruleFloat EOF ;
    public final String entryRuleFloat() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFloat = null;


        try {
            // InternalDft.g:824:45: (iv_ruleFloat= ruleFloat EOF )
            // InternalDft.g:825:2: iv_ruleFloat= ruleFloat EOF
            {
             newCompositeNode(grammarAccess.getFloatRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFloat=ruleFloat();

            state._fsp--;

             current =iv_ruleFloat.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFloat"


    // $ANTLR start "ruleFloat"
    // InternalDft.g:831:1: ruleFloat returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : ( (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )? ) ;
    public final AntlrDatatypeRuleToken ruleFloat() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_INT_1=null;
        Token this_INT_3=null;
        Token this_INT_6=null;


        	enterRule();

        try {
            // InternalDft.g:837:2: ( ( (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )? ) )
            // InternalDft.g:838:2: ( (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )? )
            {
            // InternalDft.g:838:2: ( (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )? )
            // InternalDft.g:839:3: (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )?
            {
            // InternalDft.g:839:3: (kw= '-' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==36) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalDft.g:840:4: kw= '-'
                    {
                    kw=(Token)match(input,36,FOLLOW_15); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getFloatAccess().getHyphenMinusKeyword_0());
                    			

                    }
                    break;

            }

            this_INT_1=(Token)match(input,RULE_INT,FOLLOW_16); 

            			current.merge(this_INT_1);
            		

            			newLeafNode(this_INT_1, grammarAccess.getFloatAccess().getINTTerminalRuleCall_1());
            		
            // InternalDft.g:853:3: (kw= '.' this_INT_3= RULE_INT )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==37) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalDft.g:854:4: kw= '.' this_INT_3= RULE_INT
                    {
                    kw=(Token)match(input,37,FOLLOW_15); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getFloatAccess().getFullStopKeyword_2_0());
                    			
                    this_INT_3=(Token)match(input,RULE_INT,FOLLOW_17); 

                    				current.merge(this_INT_3);
                    			

                    				newLeafNode(this_INT_3, grammarAccess.getFloatAccess().getINTTerminalRuleCall_2_1());
                    			

                    }
                    break;

            }

            // InternalDft.g:867:3: (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==38) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalDft.g:868:4: kw= 'e' (kw= '-' )? this_INT_6= RULE_INT
                    {
                    kw=(Token)match(input,38,FOLLOW_9); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getFloatAccess().getEKeyword_3_0());
                    			
                    // InternalDft.g:873:4: (kw= '-' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==36) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // InternalDft.g:874:5: kw= '-'
                            {
                            kw=(Token)match(input,36,FOLLOW_15); 

                            					current.merge(kw);
                            					newLeafNode(kw, grammarAccess.getFloatAccess().getHyphenMinusKeyword_3_1());
                            				

                            }
                            break;

                    }

                    this_INT_6=(Token)match(input,RULE_INT,FOLLOW_2); 

                    				current.merge(this_INT_6);
                    			

                    				newLeafNode(this_INT_6, grammarAccess.getFloatAccess().getINTTerminalRuleCall_3_2());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFloat"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000DFFF00020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000001000000040L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000060002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000080012L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000200000010L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000006000000002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000004000000002L});

}