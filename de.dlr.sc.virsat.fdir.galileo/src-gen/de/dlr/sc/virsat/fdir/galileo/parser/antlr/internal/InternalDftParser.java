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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_XOFY", "RULE_INT", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'toplevel'", "';'", "'lambda'", "'='", "'dorm'", "'repair'", "'and'", "'or'", "'pand'", "'pand_i'", "'por'", "'por_i'", "'sand'", "'hsp'", "'wsp'", "'csp'", "'seq'", "'fdep'", "'observer'", "'obsRate'", "'rdep'", "'rateFactor'", "'delay'", "'time'", "'-'", "'.'", "'e'"
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

                    if ( (LA1_2==RULE_XOFY||(LA1_2>=18 && LA1_2<=30)||LA1_2==32||LA1_2==34) ) {
                        alt1=1;
                    }
                    else if ( (LA1_2==14) ) {
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
    // InternalDft.g:233:1: ruleGalileoBasicEvent returns [EObject current=null] : ( ( (lv_name_0_0= RULE_STRING ) ) otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) (otherlv_4= 'dorm' otherlv_5= '=' ( (lv_dorm_6_0= ruleFloat ) ) )? (otherlv_7= 'repair' otherlv_8= '=' ( (lv_repair_9_0= ruleFloat ) ) )? ) ;
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

        AntlrDatatypeRuleToken lv_dorm_6_0 = null;

        AntlrDatatypeRuleToken lv_repair_9_0 = null;



        	enterRule();

        try {
            // InternalDft.g:239:2: ( ( ( (lv_name_0_0= RULE_STRING ) ) otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) (otherlv_4= 'dorm' otherlv_5= '=' ( (lv_dorm_6_0= ruleFloat ) ) )? (otherlv_7= 'repair' otherlv_8= '=' ( (lv_repair_9_0= ruleFloat ) ) )? ) )
            // InternalDft.g:240:2: ( ( (lv_name_0_0= RULE_STRING ) ) otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) (otherlv_4= 'dorm' otherlv_5= '=' ( (lv_dorm_6_0= ruleFloat ) ) )? (otherlv_7= 'repair' otherlv_8= '=' ( (lv_repair_9_0= ruleFloat ) ) )? )
            {
            // InternalDft.g:240:2: ( ( (lv_name_0_0= RULE_STRING ) ) otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) (otherlv_4= 'dorm' otherlv_5= '=' ( (lv_dorm_6_0= ruleFloat ) ) )? (otherlv_7= 'repair' otherlv_8= '=' ( (lv_repair_9_0= ruleFloat ) ) )? )
            // InternalDft.g:241:3: ( (lv_name_0_0= RULE_STRING ) ) otherlv_1= 'lambda' otherlv_2= '=' ( (lv_lambda_3_0= ruleFloat ) ) (otherlv_4= 'dorm' otherlv_5= '=' ( (lv_dorm_6_0= ruleFloat ) ) )? (otherlv_7= 'repair' otherlv_8= '=' ( (lv_repair_9_0= ruleFloat ) ) )?
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

            otherlv_1=(Token)match(input,14,FOLLOW_8); 

            			newLeafNode(otherlv_1, grammarAccess.getGalileoBasicEventAccess().getLambdaKeyword_1());
            		
            otherlv_2=(Token)match(input,15,FOLLOW_9); 

            			newLeafNode(otherlv_2, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_2());
            		
            // InternalDft.g:267:3: ( (lv_lambda_3_0= ruleFloat ) )
            // InternalDft.g:268:4: (lv_lambda_3_0= ruleFloat )
            {
            // InternalDft.g:268:4: (lv_lambda_3_0= ruleFloat )
            // InternalDft.g:269:5: lv_lambda_3_0= ruleFloat
            {

            					newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getLambdaFloatParserRuleCall_3_0());
            				
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

            // InternalDft.g:286:3: (otherlv_4= 'dorm' otherlv_5= '=' ( (lv_dorm_6_0= ruleFloat ) ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==16) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalDft.g:287:4: otherlv_4= 'dorm' otherlv_5= '=' ( (lv_dorm_6_0= ruleFloat ) )
                    {
                    otherlv_4=(Token)match(input,16,FOLLOW_8); 

                    				newLeafNode(otherlv_4, grammarAccess.getGalileoBasicEventAccess().getDormKeyword_4_0());
                    			
                    otherlv_5=(Token)match(input,15,FOLLOW_9); 

                    				newLeafNode(otherlv_5, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_4_1());
                    			
                    // InternalDft.g:295:4: ( (lv_dorm_6_0= ruleFloat ) )
                    // InternalDft.g:296:5: (lv_dorm_6_0= ruleFloat )
                    {
                    // InternalDft.g:296:5: (lv_dorm_6_0= ruleFloat )
                    // InternalDft.g:297:6: lv_dorm_6_0= ruleFloat
                    {

                    						newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getDormFloatParserRuleCall_4_2_0());
                    					
                    pushFollow(FOLLOW_11);
                    lv_dorm_6_0=ruleFloat();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
                    						}
                    						set(
                    							current,
                    							"dorm",
                    							lv_dorm_6_0,
                    							"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            // InternalDft.g:315:3: (otherlv_7= 'repair' otherlv_8= '=' ( (lv_repair_9_0= ruleFloat ) ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==17) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalDft.g:316:4: otherlv_7= 'repair' otherlv_8= '=' ( (lv_repair_9_0= ruleFloat ) )
                    {
                    otherlv_7=(Token)match(input,17,FOLLOW_8); 

                    				newLeafNode(otherlv_7, grammarAccess.getGalileoBasicEventAccess().getRepairKeyword_5_0());
                    			
                    otherlv_8=(Token)match(input,15,FOLLOW_9); 

                    				newLeafNode(otherlv_8, grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_5_1());
                    			
                    // InternalDft.g:324:4: ( (lv_repair_9_0= ruleFloat ) )
                    // InternalDft.g:325:5: (lv_repair_9_0= ruleFloat )
                    {
                    // InternalDft.g:325:5: (lv_repair_9_0= ruleFloat )
                    // InternalDft.g:326:6: lv_repair_9_0= ruleFloat
                    {

                    						newCompositeNode(grammarAccess.getGalileoBasicEventAccess().getRepairFloatParserRuleCall_5_2_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_repair_9_0=ruleFloat();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getGalileoBasicEventRule());
                    						}
                    						set(
                    							current,
                    							"repair",
                    							lv_repair_9_0,
                    							"de.dlr.sc.virsat.fdir.galileo.Dft.Float");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


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
    // $ANTLR end "ruleGalileoBasicEvent"


    // $ANTLR start "entryRuleGalileoNodeType"
    // InternalDft.g:348:1: entryRuleGalileoNodeType returns [EObject current=null] : iv_ruleGalileoNodeType= ruleGalileoNodeType EOF ;
    public final EObject entryRuleGalileoNodeType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGalileoNodeType = null;


        try {
            // InternalDft.g:348:56: (iv_ruleGalileoNodeType= ruleGalileoNodeType EOF )
            // InternalDft.g:349:2: iv_ruleGalileoNodeType= ruleGalileoNodeType EOF
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
    // InternalDft.g:355:1: ruleGalileoNodeType returns [EObject current=null] : (this_NamedType_0= ruleNamedType | this_ObserverType_1= ruleObserverType | this_RDEPType_2= ruleRDEPType | this_DelayType_3= ruleDelayType ) ;
    public final EObject ruleGalileoNodeType() throws RecognitionException {
        EObject current = null;

        EObject this_NamedType_0 = null;

        EObject this_ObserverType_1 = null;

        EObject this_RDEPType_2 = null;

        EObject this_DelayType_3 = null;



        	enterRule();

        try {
            // InternalDft.g:361:2: ( (this_NamedType_0= ruleNamedType | this_ObserverType_1= ruleObserverType | this_RDEPType_2= ruleRDEPType | this_DelayType_3= ruleDelayType ) )
            // InternalDft.g:362:2: (this_NamedType_0= ruleNamedType | this_ObserverType_1= ruleObserverType | this_RDEPType_2= ruleRDEPType | this_DelayType_3= ruleDelayType )
            {
            // InternalDft.g:362:2: (this_NamedType_0= ruleNamedType | this_ObserverType_1= ruleObserverType | this_RDEPType_2= ruleRDEPType | this_DelayType_3= ruleDelayType )
            int alt5=4;
            switch ( input.LA(1) ) {
            case RULE_XOFY:
            case 18:
            case 19:
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
                {
                alt5=1;
                }
                break;
            case 30:
                {
                alt5=2;
                }
                break;
            case 32:
                {
                alt5=3;
                }
                break;
            case 34:
                {
                alt5=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // InternalDft.g:363:3: this_NamedType_0= ruleNamedType
                    {

                    			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getNamedTypeParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_NamedType_0=ruleNamedType();

                    state._fsp--;


                    			current = this_NamedType_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalDft.g:372:3: this_ObserverType_1= ruleObserverType
                    {

                    			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getObserverTypeParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_ObserverType_1=ruleObserverType();

                    state._fsp--;


                    			current = this_ObserverType_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalDft.g:381:3: this_RDEPType_2= ruleRDEPType
                    {

                    			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getRDEPTypeParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_RDEPType_2=ruleRDEPType();

                    state._fsp--;


                    			current = this_RDEPType_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalDft.g:390:3: this_DelayType_3= ruleDelayType
                    {

                    			newCompositeNode(grammarAccess.getGalileoNodeTypeAccess().getDelayTypeParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_DelayType_3=ruleDelayType();

                    state._fsp--;


                    			current = this_DelayType_3;
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


    // $ANTLR start "entryRuleNamedType"
    // InternalDft.g:402:1: entryRuleNamedType returns [EObject current=null] : iv_ruleNamedType= ruleNamedType EOF ;
    public final EObject entryRuleNamedType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNamedType = null;


        try {
            // InternalDft.g:402:50: (iv_ruleNamedType= ruleNamedType EOF )
            // InternalDft.g:403:2: iv_ruleNamedType= ruleNamedType EOF
            {
             newCompositeNode(grammarAccess.getNamedTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNamedType=ruleNamedType();

            state._fsp--;

             current =iv_ruleNamedType; 
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
    // $ANTLR end "entryRuleNamedType"


    // $ANTLR start "ruleNamedType"
    // InternalDft.g:409:1: ruleNamedType returns [EObject current=null] : ( ( (lv_typeName_0_1= 'and' | lv_typeName_0_2= 'or' | lv_typeName_0_3= RULE_XOFY | lv_typeName_0_4= 'pand' | lv_typeName_0_5= 'pand_i' | lv_typeName_0_6= 'por' | lv_typeName_0_7= 'por_i' | lv_typeName_0_8= 'sand' | lv_typeName_0_9= 'hsp' | lv_typeName_0_10= 'wsp' | lv_typeName_0_11= 'csp' | lv_typeName_0_12= 'seq' | lv_typeName_0_13= 'fdep' ) ) ) ;
    public final EObject ruleNamedType() throws RecognitionException {
        EObject current = null;

        Token lv_typeName_0_1=null;
        Token lv_typeName_0_2=null;
        Token lv_typeName_0_3=null;
        Token lv_typeName_0_4=null;
        Token lv_typeName_0_5=null;
        Token lv_typeName_0_6=null;
        Token lv_typeName_0_7=null;
        Token lv_typeName_0_8=null;
        Token lv_typeName_0_9=null;
        Token lv_typeName_0_10=null;
        Token lv_typeName_0_11=null;
        Token lv_typeName_0_12=null;
        Token lv_typeName_0_13=null;


        	enterRule();

        try {
            // InternalDft.g:415:2: ( ( ( (lv_typeName_0_1= 'and' | lv_typeName_0_2= 'or' | lv_typeName_0_3= RULE_XOFY | lv_typeName_0_4= 'pand' | lv_typeName_0_5= 'pand_i' | lv_typeName_0_6= 'por' | lv_typeName_0_7= 'por_i' | lv_typeName_0_8= 'sand' | lv_typeName_0_9= 'hsp' | lv_typeName_0_10= 'wsp' | lv_typeName_0_11= 'csp' | lv_typeName_0_12= 'seq' | lv_typeName_0_13= 'fdep' ) ) ) )
            // InternalDft.g:416:2: ( ( (lv_typeName_0_1= 'and' | lv_typeName_0_2= 'or' | lv_typeName_0_3= RULE_XOFY | lv_typeName_0_4= 'pand' | lv_typeName_0_5= 'pand_i' | lv_typeName_0_6= 'por' | lv_typeName_0_7= 'por_i' | lv_typeName_0_8= 'sand' | lv_typeName_0_9= 'hsp' | lv_typeName_0_10= 'wsp' | lv_typeName_0_11= 'csp' | lv_typeName_0_12= 'seq' | lv_typeName_0_13= 'fdep' ) ) )
            {
            // InternalDft.g:416:2: ( ( (lv_typeName_0_1= 'and' | lv_typeName_0_2= 'or' | lv_typeName_0_3= RULE_XOFY | lv_typeName_0_4= 'pand' | lv_typeName_0_5= 'pand_i' | lv_typeName_0_6= 'por' | lv_typeName_0_7= 'por_i' | lv_typeName_0_8= 'sand' | lv_typeName_0_9= 'hsp' | lv_typeName_0_10= 'wsp' | lv_typeName_0_11= 'csp' | lv_typeName_0_12= 'seq' | lv_typeName_0_13= 'fdep' ) ) )
            // InternalDft.g:417:3: ( (lv_typeName_0_1= 'and' | lv_typeName_0_2= 'or' | lv_typeName_0_3= RULE_XOFY | lv_typeName_0_4= 'pand' | lv_typeName_0_5= 'pand_i' | lv_typeName_0_6= 'por' | lv_typeName_0_7= 'por_i' | lv_typeName_0_8= 'sand' | lv_typeName_0_9= 'hsp' | lv_typeName_0_10= 'wsp' | lv_typeName_0_11= 'csp' | lv_typeName_0_12= 'seq' | lv_typeName_0_13= 'fdep' ) )
            {
            // InternalDft.g:417:3: ( (lv_typeName_0_1= 'and' | lv_typeName_0_2= 'or' | lv_typeName_0_3= RULE_XOFY | lv_typeName_0_4= 'pand' | lv_typeName_0_5= 'pand_i' | lv_typeName_0_6= 'por' | lv_typeName_0_7= 'por_i' | lv_typeName_0_8= 'sand' | lv_typeName_0_9= 'hsp' | lv_typeName_0_10= 'wsp' | lv_typeName_0_11= 'csp' | lv_typeName_0_12= 'seq' | lv_typeName_0_13= 'fdep' ) )
            // InternalDft.g:418:4: (lv_typeName_0_1= 'and' | lv_typeName_0_2= 'or' | lv_typeName_0_3= RULE_XOFY | lv_typeName_0_4= 'pand' | lv_typeName_0_5= 'pand_i' | lv_typeName_0_6= 'por' | lv_typeName_0_7= 'por_i' | lv_typeName_0_8= 'sand' | lv_typeName_0_9= 'hsp' | lv_typeName_0_10= 'wsp' | lv_typeName_0_11= 'csp' | lv_typeName_0_12= 'seq' | lv_typeName_0_13= 'fdep' )
            {
            // InternalDft.g:418:4: (lv_typeName_0_1= 'and' | lv_typeName_0_2= 'or' | lv_typeName_0_3= RULE_XOFY | lv_typeName_0_4= 'pand' | lv_typeName_0_5= 'pand_i' | lv_typeName_0_6= 'por' | lv_typeName_0_7= 'por_i' | lv_typeName_0_8= 'sand' | lv_typeName_0_9= 'hsp' | lv_typeName_0_10= 'wsp' | lv_typeName_0_11= 'csp' | lv_typeName_0_12= 'seq' | lv_typeName_0_13= 'fdep' )
            int alt6=13;
            switch ( input.LA(1) ) {
            case 18:
                {
                alt6=1;
                }
                break;
            case 19:
                {
                alt6=2;
                }
                break;
            case RULE_XOFY:
                {
                alt6=3;
                }
                break;
            case 20:
                {
                alt6=4;
                }
                break;
            case 21:
                {
                alt6=5;
                }
                break;
            case 22:
                {
                alt6=6;
                }
                break;
            case 23:
                {
                alt6=7;
                }
                break;
            case 24:
                {
                alt6=8;
                }
                break;
            case 25:
                {
                alt6=9;
                }
                break;
            case 26:
                {
                alt6=10;
                }
                break;
            case 27:
                {
                alt6=11;
                }
                break;
            case 28:
                {
                alt6=12;
                }
                break;
            case 29:
                {
                alt6=13;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // InternalDft.g:419:5: lv_typeName_0_1= 'and'
                    {
                    lv_typeName_0_1=(Token)match(input,18,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_1, grammarAccess.getNamedTypeAccess().getTypeNameAndKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_1, null);
                    				

                    }
                    break;
                case 2 :
                    // InternalDft.g:430:5: lv_typeName_0_2= 'or'
                    {
                    lv_typeName_0_2=(Token)match(input,19,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_2, grammarAccess.getNamedTypeAccess().getTypeNameOrKeyword_0_1());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_2, null);
                    				

                    }
                    break;
                case 3 :
                    // InternalDft.g:441:5: lv_typeName_0_3= RULE_XOFY
                    {
                    lv_typeName_0_3=(Token)match(input,RULE_XOFY,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_3, grammarAccess.getNamedTypeAccess().getTypeNameXOFYTerminalRuleCall_0_2());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"typeName",
                    						lv_typeName_0_3,
                    						"de.dlr.sc.virsat.fdir.galileo.Dft.XOFY");
                    				

                    }
                    break;
                case 4 :
                    // InternalDft.g:456:5: lv_typeName_0_4= 'pand'
                    {
                    lv_typeName_0_4=(Token)match(input,20,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_4, grammarAccess.getNamedTypeAccess().getTypeNamePandKeyword_0_3());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_4, null);
                    				

                    }
                    break;
                case 5 :
                    // InternalDft.g:467:5: lv_typeName_0_5= 'pand_i'
                    {
                    lv_typeName_0_5=(Token)match(input,21,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_5, grammarAccess.getNamedTypeAccess().getTypeNamePand_iKeyword_0_4());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_5, null);
                    				

                    }
                    break;
                case 6 :
                    // InternalDft.g:478:5: lv_typeName_0_6= 'por'
                    {
                    lv_typeName_0_6=(Token)match(input,22,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_6, grammarAccess.getNamedTypeAccess().getTypeNamePorKeyword_0_5());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_6, null);
                    				

                    }
                    break;
                case 7 :
                    // InternalDft.g:489:5: lv_typeName_0_7= 'por_i'
                    {
                    lv_typeName_0_7=(Token)match(input,23,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_7, grammarAccess.getNamedTypeAccess().getTypeNamePor_iKeyword_0_6());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_7, null);
                    				

                    }
                    break;
                case 8 :
                    // InternalDft.g:500:5: lv_typeName_0_8= 'sand'
                    {
                    lv_typeName_0_8=(Token)match(input,24,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_8, grammarAccess.getNamedTypeAccess().getTypeNameSandKeyword_0_7());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_8, null);
                    				

                    }
                    break;
                case 9 :
                    // InternalDft.g:511:5: lv_typeName_0_9= 'hsp'
                    {
                    lv_typeName_0_9=(Token)match(input,25,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_9, grammarAccess.getNamedTypeAccess().getTypeNameHspKeyword_0_8());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_9, null);
                    				

                    }
                    break;
                case 10 :
                    // InternalDft.g:522:5: lv_typeName_0_10= 'wsp'
                    {
                    lv_typeName_0_10=(Token)match(input,26,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_10, grammarAccess.getNamedTypeAccess().getTypeNameWspKeyword_0_9());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_10, null);
                    				

                    }
                    break;
                case 11 :
                    // InternalDft.g:533:5: lv_typeName_0_11= 'csp'
                    {
                    lv_typeName_0_11=(Token)match(input,27,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_11, grammarAccess.getNamedTypeAccess().getTypeNameCspKeyword_0_10());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_11, null);
                    				

                    }
                    break;
                case 12 :
                    // InternalDft.g:544:5: lv_typeName_0_12= 'seq'
                    {
                    lv_typeName_0_12=(Token)match(input,28,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_12, grammarAccess.getNamedTypeAccess().getTypeNameSeqKeyword_0_11());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_12, null);
                    				

                    }
                    break;
                case 13 :
                    // InternalDft.g:555:5: lv_typeName_0_13= 'fdep'
                    {
                    lv_typeName_0_13=(Token)match(input,29,FOLLOW_2); 

                    					newLeafNode(lv_typeName_0_13, grammarAccess.getNamedTypeAccess().getTypeNameFdepKeyword_0_12());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getNamedTypeRule());
                    					}
                    					setWithLastConsumed(current, "typeName", lv_typeName_0_13, null);
                    				

                    }
                    break;

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
    // $ANTLR end "ruleNamedType"


    // $ANTLR start "entryRuleObserverType"
    // InternalDft.g:571:1: entryRuleObserverType returns [EObject current=null] : iv_ruleObserverType= ruleObserverType EOF ;
    public final EObject entryRuleObserverType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObserverType = null;


        try {
            // InternalDft.g:571:53: (iv_ruleObserverType= ruleObserverType EOF )
            // InternalDft.g:572:2: iv_ruleObserverType= ruleObserverType EOF
            {
             newCompositeNode(grammarAccess.getObserverTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleObserverType=ruleObserverType();

            state._fsp--;

             current =iv_ruleObserverType; 
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
    // $ANTLR end "entryRuleObserverType"


    // $ANTLR start "ruleObserverType"
    // InternalDft.g:578:1: ruleObserverType returns [EObject current=null] : (otherlv_0= 'observer' ( (otherlv_1= RULE_STRING ) )* otherlv_2= 'obsRate' otherlv_3= '=' ( (lv_observationRate_4_0= ruleFloat ) ) ) ;
    public final EObject ruleObserverType() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        AntlrDatatypeRuleToken lv_observationRate_4_0 = null;



        	enterRule();

        try {
            // InternalDft.g:584:2: ( (otherlv_0= 'observer' ( (otherlv_1= RULE_STRING ) )* otherlv_2= 'obsRate' otherlv_3= '=' ( (lv_observationRate_4_0= ruleFloat ) ) ) )
            // InternalDft.g:585:2: (otherlv_0= 'observer' ( (otherlv_1= RULE_STRING ) )* otherlv_2= 'obsRate' otherlv_3= '=' ( (lv_observationRate_4_0= ruleFloat ) ) )
            {
            // InternalDft.g:585:2: (otherlv_0= 'observer' ( (otherlv_1= RULE_STRING ) )* otherlv_2= 'obsRate' otherlv_3= '=' ( (lv_observationRate_4_0= ruleFloat ) ) )
            // InternalDft.g:586:3: otherlv_0= 'observer' ( (otherlv_1= RULE_STRING ) )* otherlv_2= 'obsRate' otherlv_3= '=' ( (lv_observationRate_4_0= ruleFloat ) )
            {
            otherlv_0=(Token)match(input,30,FOLLOW_12); 

            			newLeafNode(otherlv_0, grammarAccess.getObserverTypeAccess().getObserverKeyword_0());
            		
            // InternalDft.g:590:3: ( (otherlv_1= RULE_STRING ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==RULE_STRING) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalDft.g:591:4: (otherlv_1= RULE_STRING )
            	    {
            	    // InternalDft.g:591:4: (otherlv_1= RULE_STRING )
            	    // InternalDft.g:592:5: otherlv_1= RULE_STRING
            	    {

            	    					if (current==null) {
            	    						current = createModelElement(grammarAccess.getObserverTypeRule());
            	    					}
            	    				
            	    otherlv_1=(Token)match(input,RULE_STRING,FOLLOW_12); 

            	    					newLeafNode(otherlv_1, grammarAccess.getObserverTypeAccess().getObservablesGalileoFaultTreeNodeCrossReference_1_0());
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            otherlv_2=(Token)match(input,31,FOLLOW_8); 

            			newLeafNode(otherlv_2, grammarAccess.getObserverTypeAccess().getObsRateKeyword_2());
            		
            otherlv_3=(Token)match(input,15,FOLLOW_9); 

            			newLeafNode(otherlv_3, grammarAccess.getObserverTypeAccess().getEqualsSignKeyword_3());
            		
            // InternalDft.g:611:3: ( (lv_observationRate_4_0= ruleFloat ) )
            // InternalDft.g:612:4: (lv_observationRate_4_0= ruleFloat )
            {
            // InternalDft.g:612:4: (lv_observationRate_4_0= ruleFloat )
            // InternalDft.g:613:5: lv_observationRate_4_0= ruleFloat
            {

            					newCompositeNode(grammarAccess.getObserverTypeAccess().getObservationRateFloatParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_2);
            lv_observationRate_4_0=ruleFloat();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getObserverTypeRule());
            					}
            					set(
            						current,
            						"observationRate",
            						lv_observationRate_4_0,
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
    // $ANTLR end "ruleObserverType"


    // $ANTLR start "entryRuleRDEPType"
    // InternalDft.g:634:1: entryRuleRDEPType returns [EObject current=null] : iv_ruleRDEPType= ruleRDEPType EOF ;
    public final EObject entryRuleRDEPType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRDEPType = null;


        try {
            // InternalDft.g:634:49: (iv_ruleRDEPType= ruleRDEPType EOF )
            // InternalDft.g:635:2: iv_ruleRDEPType= ruleRDEPType EOF
            {
             newCompositeNode(grammarAccess.getRDEPTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRDEPType=ruleRDEPType();

            state._fsp--;

             current =iv_ruleRDEPType; 
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
    // $ANTLR end "entryRuleRDEPType"


    // $ANTLR start "ruleRDEPType"
    // InternalDft.g:641:1: ruleRDEPType returns [EObject current=null] : (otherlv_0= 'rdep' otherlv_1= 'rateFactor' ( (lv_rateFactor_2_0= ruleFloat ) ) ) ;
    public final EObject ruleRDEPType() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_rateFactor_2_0 = null;



        	enterRule();

        try {
            // InternalDft.g:647:2: ( (otherlv_0= 'rdep' otherlv_1= 'rateFactor' ( (lv_rateFactor_2_0= ruleFloat ) ) ) )
            // InternalDft.g:648:2: (otherlv_0= 'rdep' otherlv_1= 'rateFactor' ( (lv_rateFactor_2_0= ruleFloat ) ) )
            {
            // InternalDft.g:648:2: (otherlv_0= 'rdep' otherlv_1= 'rateFactor' ( (lv_rateFactor_2_0= ruleFloat ) ) )
            // InternalDft.g:649:3: otherlv_0= 'rdep' otherlv_1= 'rateFactor' ( (lv_rateFactor_2_0= ruleFloat ) )
            {
            otherlv_0=(Token)match(input,32,FOLLOW_13); 

            			newLeafNode(otherlv_0, grammarAccess.getRDEPTypeAccess().getRdepKeyword_0());
            		
            otherlv_1=(Token)match(input,33,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getRDEPTypeAccess().getRateFactorKeyword_1());
            		
            // InternalDft.g:657:3: ( (lv_rateFactor_2_0= ruleFloat ) )
            // InternalDft.g:658:4: (lv_rateFactor_2_0= ruleFloat )
            {
            // InternalDft.g:658:4: (lv_rateFactor_2_0= ruleFloat )
            // InternalDft.g:659:5: lv_rateFactor_2_0= ruleFloat
            {

            					newCompositeNode(grammarAccess.getRDEPTypeAccess().getRateFactorFloatParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_rateFactor_2_0=ruleFloat();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getRDEPTypeRule());
            					}
            					set(
            						current,
            						"rateFactor",
            						lv_rateFactor_2_0,
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
    // $ANTLR end "ruleRDEPType"


    // $ANTLR start "entryRuleDelayType"
    // InternalDft.g:680:1: entryRuleDelayType returns [EObject current=null] : iv_ruleDelayType= ruleDelayType EOF ;
    public final EObject entryRuleDelayType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDelayType = null;


        try {
            // InternalDft.g:680:50: (iv_ruleDelayType= ruleDelayType EOF )
            // InternalDft.g:681:2: iv_ruleDelayType= ruleDelayType EOF
            {
             newCompositeNode(grammarAccess.getDelayTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDelayType=ruleDelayType();

            state._fsp--;

             current =iv_ruleDelayType; 
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
    // $ANTLR end "entryRuleDelayType"


    // $ANTLR start "ruleDelayType"
    // InternalDft.g:687:1: ruleDelayType returns [EObject current=null] : (otherlv_0= 'delay' otherlv_1= 'time' ( (lv_time_2_0= ruleFloat ) ) ) ;
    public final EObject ruleDelayType() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_time_2_0 = null;



        	enterRule();

        try {
            // InternalDft.g:693:2: ( (otherlv_0= 'delay' otherlv_1= 'time' ( (lv_time_2_0= ruleFloat ) ) ) )
            // InternalDft.g:694:2: (otherlv_0= 'delay' otherlv_1= 'time' ( (lv_time_2_0= ruleFloat ) ) )
            {
            // InternalDft.g:694:2: (otherlv_0= 'delay' otherlv_1= 'time' ( (lv_time_2_0= ruleFloat ) ) )
            // InternalDft.g:695:3: otherlv_0= 'delay' otherlv_1= 'time' ( (lv_time_2_0= ruleFloat ) )
            {
            otherlv_0=(Token)match(input,34,FOLLOW_14); 

            			newLeafNode(otherlv_0, grammarAccess.getDelayTypeAccess().getDelayKeyword_0());
            		
            otherlv_1=(Token)match(input,35,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getDelayTypeAccess().getTimeKeyword_1());
            		
            // InternalDft.g:703:3: ( (lv_time_2_0= ruleFloat ) )
            // InternalDft.g:704:4: (lv_time_2_0= ruleFloat )
            {
            // InternalDft.g:704:4: (lv_time_2_0= ruleFloat )
            // InternalDft.g:705:5: lv_time_2_0= ruleFloat
            {

            					newCompositeNode(grammarAccess.getDelayTypeAccess().getTimeFloatParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_time_2_0=ruleFloat();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDelayTypeRule());
            					}
            					set(
            						current,
            						"time",
            						lv_time_2_0,
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
    // $ANTLR end "ruleDelayType"


    // $ANTLR start "entryRuleFloat"
    // InternalDft.g:726:1: entryRuleFloat returns [String current=null] : iv_ruleFloat= ruleFloat EOF ;
    public final String entryRuleFloat() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleFloat = null;


        try {
            // InternalDft.g:726:45: (iv_ruleFloat= ruleFloat EOF )
            // InternalDft.g:727:2: iv_ruleFloat= ruleFloat EOF
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
    // InternalDft.g:733:1: ruleFloat returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : ( (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )? ) ;
    public final AntlrDatatypeRuleToken ruleFloat() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_INT_1=null;
        Token this_INT_3=null;
        Token this_INT_6=null;


        	enterRule();

        try {
            // InternalDft.g:739:2: ( ( (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )? ) )
            // InternalDft.g:740:2: ( (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )? )
            {
            // InternalDft.g:740:2: ( (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )? )
            // InternalDft.g:741:3: (kw= '-' )? this_INT_1= RULE_INT (kw= '.' this_INT_3= RULE_INT )? (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )?
            {
            // InternalDft.g:741:3: (kw= '-' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==36) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // InternalDft.g:742:4: kw= '-'
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
            		
            // InternalDft.g:755:3: (kw= '.' this_INT_3= RULE_INT )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==37) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalDft.g:756:4: kw= '.' this_INT_3= RULE_INT
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

            // InternalDft.g:769:3: (kw= 'e' (kw= '-' )? this_INT_6= RULE_INT )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==38) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalDft.g:770:4: kw= 'e' (kw= '-' )? this_INT_6= RULE_INT
                    {
                    kw=(Token)match(input,38,FOLLOW_9); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getFloatAccess().getEKeyword_3_0());
                    			
                    // InternalDft.g:775:4: (kw= '-' )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==36) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // InternalDft.g:776:5: kw= '-'
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
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x000000057FFC0020L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000001000000040L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000030002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000080000010L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000006000000002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000004000000002L});

}