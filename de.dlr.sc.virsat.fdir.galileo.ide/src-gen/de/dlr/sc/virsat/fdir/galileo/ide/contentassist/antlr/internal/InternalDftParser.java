package de.dlr.sc.virsat.fdir.galileo.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import de.dlr.sc.virsat.fdir.galileo.services.DftGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalDftParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_XOFY", "RULE_INT", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'and'", "'or'", "'pand'", "'pand_i'", "'por'", "'por_i'", "'sand'", "'hsp'", "'wsp'", "'csp'", "'seq'", "'fdep'", "'rdep'", "'delay'", "'toplevel'", "';'", "'lambda'", "'='", "'prob'", "'dorm'", "'repair'", "'observations'", "'observer'", "'obsRate'", "'-'", "'.'", "'e'"
    };
    public static final int RULE_XOFY=4;
    public static final int RULE_STRING=6;
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
    public static final int RULE_INT=5;
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

    	public void setGrammarAccess(DftGrammarAccess grammarAccess) {
    		this.grammarAccess = grammarAccess;
    	}

    	@Override
    	protected Grammar getGrammar() {
    		return grammarAccess.getGrammar();
    	}

    	@Override
    	protected String getValueForTokenName(String tokenName) {
    		return tokenName;
    	}



    // $ANTLR start "entryRuleGalileoDft"
    // InternalDft.g:53:1: entryRuleGalileoDft : ruleGalileoDft EOF ;
    public final void entryRuleGalileoDft() throws RecognitionException {
        try {
            // InternalDft.g:54:1: ( ruleGalileoDft EOF )
            // InternalDft.g:55:1: ruleGalileoDft EOF
            {
             before(grammarAccess.getGalileoDftRule()); 
            pushFollow(FOLLOW_1);
            ruleGalileoDft();

            state._fsp--;

             after(grammarAccess.getGalileoDftRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleGalileoDft"


    // $ANTLR start "ruleGalileoDft"
    // InternalDft.g:62:1: ruleGalileoDft : ( ( rule__GalileoDft__Group__0 ) ) ;
    public final void ruleGalileoDft() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:66:2: ( ( ( rule__GalileoDft__Group__0 ) ) )
            // InternalDft.g:67:2: ( ( rule__GalileoDft__Group__0 ) )
            {
            // InternalDft.g:67:2: ( ( rule__GalileoDft__Group__0 ) )
            // InternalDft.g:68:3: ( rule__GalileoDft__Group__0 )
            {
             before(grammarAccess.getGalileoDftAccess().getGroup()); 
            // InternalDft.g:69:3: ( rule__GalileoDft__Group__0 )
            // InternalDft.g:69:4: rule__GalileoDft__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getGalileoDftAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleGalileoDft"


    // $ANTLR start "entryRuleGalileoGate"
    // InternalDft.g:78:1: entryRuleGalileoGate : ruleGalileoGate EOF ;
    public final void entryRuleGalileoGate() throws RecognitionException {
        try {
            // InternalDft.g:79:1: ( ruleGalileoGate EOF )
            // InternalDft.g:80:1: ruleGalileoGate EOF
            {
             before(grammarAccess.getGalileoGateRule()); 
            pushFollow(FOLLOW_1);
            ruleGalileoGate();

            state._fsp--;

             after(grammarAccess.getGalileoGateRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleGalileoGate"


    // $ANTLR start "ruleGalileoGate"
    // InternalDft.g:87:1: ruleGalileoGate : ( ( rule__GalileoGate__Group__0 ) ) ;
    public final void ruleGalileoGate() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:91:2: ( ( ( rule__GalileoGate__Group__0 ) ) )
            // InternalDft.g:92:2: ( ( rule__GalileoGate__Group__0 ) )
            {
            // InternalDft.g:92:2: ( ( rule__GalileoGate__Group__0 ) )
            // InternalDft.g:93:3: ( rule__GalileoGate__Group__0 )
            {
             before(grammarAccess.getGalileoGateAccess().getGroup()); 
            // InternalDft.g:94:3: ( rule__GalileoGate__Group__0 )
            // InternalDft.g:94:4: rule__GalileoGate__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__GalileoGate__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getGalileoGateAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleGalileoGate"


    // $ANTLR start "entryRuleGalileoBasicEvent"
    // InternalDft.g:103:1: entryRuleGalileoBasicEvent : ruleGalileoBasicEvent EOF ;
    public final void entryRuleGalileoBasicEvent() throws RecognitionException {
        try {
            // InternalDft.g:104:1: ( ruleGalileoBasicEvent EOF )
            // InternalDft.g:105:1: ruleGalileoBasicEvent EOF
            {
             before(grammarAccess.getGalileoBasicEventRule()); 
            pushFollow(FOLLOW_1);
            ruleGalileoBasicEvent();

            state._fsp--;

             after(grammarAccess.getGalileoBasicEventRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleGalileoBasicEvent"


    // $ANTLR start "ruleGalileoBasicEvent"
    // InternalDft.g:112:1: ruleGalileoBasicEvent : ( ( rule__GalileoBasicEvent__Group__0 ) ) ;
    public final void ruleGalileoBasicEvent() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:116:2: ( ( ( rule__GalileoBasicEvent__Group__0 ) ) )
            // InternalDft.g:117:2: ( ( rule__GalileoBasicEvent__Group__0 ) )
            {
            // InternalDft.g:117:2: ( ( rule__GalileoBasicEvent__Group__0 ) )
            // InternalDft.g:118:3: ( rule__GalileoBasicEvent__Group__0 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getGroup()); 
            // InternalDft.g:119:3: ( rule__GalileoBasicEvent__Group__0 )
            // InternalDft.g:119:4: rule__GalileoBasicEvent__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleGalileoBasicEvent"


    // $ANTLR start "entryRuleGalileoRepairAction"
    // InternalDft.g:128:1: entryRuleGalileoRepairAction : ruleGalileoRepairAction EOF ;
    public final void entryRuleGalileoRepairAction() throws RecognitionException {
        try {
            // InternalDft.g:129:1: ( ruleGalileoRepairAction EOF )
            // InternalDft.g:130:1: ruleGalileoRepairAction EOF
            {
             before(grammarAccess.getGalileoRepairActionRule()); 
            pushFollow(FOLLOW_1);
            ruleGalileoRepairAction();

            state._fsp--;

             after(grammarAccess.getGalileoRepairActionRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleGalileoRepairAction"


    // $ANTLR start "ruleGalileoRepairAction"
    // InternalDft.g:137:1: ruleGalileoRepairAction : ( ( rule__GalileoRepairAction__Group__0 ) ) ;
    public final void ruleGalileoRepairAction() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:141:2: ( ( ( rule__GalileoRepairAction__Group__0 ) ) )
            // InternalDft.g:142:2: ( ( rule__GalileoRepairAction__Group__0 ) )
            {
            // InternalDft.g:142:2: ( ( rule__GalileoRepairAction__Group__0 ) )
            // InternalDft.g:143:3: ( rule__GalileoRepairAction__Group__0 )
            {
             before(grammarAccess.getGalileoRepairActionAccess().getGroup()); 
            // InternalDft.g:144:3: ( rule__GalileoRepairAction__Group__0 )
            // InternalDft.g:144:4: rule__GalileoRepairAction__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getGalileoRepairActionAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleGalileoRepairAction"


    // $ANTLR start "entryRuleGalileoNodeType"
    // InternalDft.g:153:1: entryRuleGalileoNodeType : ruleGalileoNodeType EOF ;
    public final void entryRuleGalileoNodeType() throws RecognitionException {
        try {
            // InternalDft.g:154:1: ( ruleGalileoNodeType EOF )
            // InternalDft.g:155:1: ruleGalileoNodeType EOF
            {
             before(grammarAccess.getGalileoNodeTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleGalileoNodeType();

            state._fsp--;

             after(grammarAccess.getGalileoNodeTypeRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleGalileoNodeType"


    // $ANTLR start "ruleGalileoNodeType"
    // InternalDft.g:162:1: ruleGalileoNodeType : ( ( rule__GalileoNodeType__Alternatives ) ) ;
    public final void ruleGalileoNodeType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:166:2: ( ( ( rule__GalileoNodeType__Alternatives ) ) )
            // InternalDft.g:167:2: ( ( rule__GalileoNodeType__Alternatives ) )
            {
            // InternalDft.g:167:2: ( ( rule__GalileoNodeType__Alternatives ) )
            // InternalDft.g:168:3: ( rule__GalileoNodeType__Alternatives )
            {
             before(grammarAccess.getGalileoNodeTypeAccess().getAlternatives()); 
            // InternalDft.g:169:3: ( rule__GalileoNodeType__Alternatives )
            // InternalDft.g:169:4: rule__GalileoNodeType__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__GalileoNodeType__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getGalileoNodeTypeAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleGalileoNodeType"


    // $ANTLR start "entryRuleNamed"
    // InternalDft.g:178:1: entryRuleNamed : ruleNamed EOF ;
    public final void entryRuleNamed() throws RecognitionException {
        try {
            // InternalDft.g:179:1: ( ruleNamed EOF )
            // InternalDft.g:180:1: ruleNamed EOF
            {
             before(grammarAccess.getNamedRule()); 
            pushFollow(FOLLOW_1);
            ruleNamed();

            state._fsp--;

             after(grammarAccess.getNamedRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleNamed"


    // $ANTLR start "ruleNamed"
    // InternalDft.g:187:1: ruleNamed : ( ( rule__Named__Group__0 ) ) ;
    public final void ruleNamed() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:191:2: ( ( ( rule__Named__Group__0 ) ) )
            // InternalDft.g:192:2: ( ( rule__Named__Group__0 ) )
            {
            // InternalDft.g:192:2: ( ( rule__Named__Group__0 ) )
            // InternalDft.g:193:3: ( rule__Named__Group__0 )
            {
             before(grammarAccess.getNamedAccess().getGroup()); 
            // InternalDft.g:194:3: ( rule__Named__Group__0 )
            // InternalDft.g:194:4: rule__Named__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Named__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getNamedAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleNamed"


    // $ANTLR start "entryRuleObserver"
    // InternalDft.g:203:1: entryRuleObserver : ruleObserver EOF ;
    public final void entryRuleObserver() throws RecognitionException {
        try {
            // InternalDft.g:204:1: ( ruleObserver EOF )
            // InternalDft.g:205:1: ruleObserver EOF
            {
             before(grammarAccess.getObserverRule()); 
            pushFollow(FOLLOW_1);
            ruleObserver();

            state._fsp--;

             after(grammarAccess.getObserverRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleObserver"


    // $ANTLR start "ruleObserver"
    // InternalDft.g:212:1: ruleObserver : ( ( rule__Observer__Group__0 ) ) ;
    public final void ruleObserver() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:216:2: ( ( ( rule__Observer__Group__0 ) ) )
            // InternalDft.g:217:2: ( ( rule__Observer__Group__0 ) )
            {
            // InternalDft.g:217:2: ( ( rule__Observer__Group__0 ) )
            // InternalDft.g:218:3: ( rule__Observer__Group__0 )
            {
             before(grammarAccess.getObserverAccess().getGroup()); 
            // InternalDft.g:219:3: ( rule__Observer__Group__0 )
            // InternalDft.g:219:4: rule__Observer__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Observer__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getObserverAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleObserver"


    // $ANTLR start "entryRuleParametrized"
    // InternalDft.g:228:1: entryRuleParametrized : ruleParametrized EOF ;
    public final void entryRuleParametrized() throws RecognitionException {
        try {
            // InternalDft.g:229:1: ( ruleParametrized EOF )
            // InternalDft.g:230:1: ruleParametrized EOF
            {
             before(grammarAccess.getParametrizedRule()); 
            pushFollow(FOLLOW_1);
            ruleParametrized();

            state._fsp--;

             after(grammarAccess.getParametrizedRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleParametrized"


    // $ANTLR start "ruleParametrized"
    // InternalDft.g:237:1: ruleParametrized : ( ( rule__Parametrized__Group__0 ) ) ;
    public final void ruleParametrized() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:241:2: ( ( ( rule__Parametrized__Group__0 ) ) )
            // InternalDft.g:242:2: ( ( rule__Parametrized__Group__0 ) )
            {
            // InternalDft.g:242:2: ( ( rule__Parametrized__Group__0 ) )
            // InternalDft.g:243:3: ( rule__Parametrized__Group__0 )
            {
             before(grammarAccess.getParametrizedAccess().getGroup()); 
            // InternalDft.g:244:3: ( rule__Parametrized__Group__0 )
            // InternalDft.g:244:4: rule__Parametrized__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Parametrized__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getParametrizedAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleParametrized"


    // $ANTLR start "entryRuleFloat"
    // InternalDft.g:253:1: entryRuleFloat : ruleFloat EOF ;
    public final void entryRuleFloat() throws RecognitionException {
        try {
            // InternalDft.g:254:1: ( ruleFloat EOF )
            // InternalDft.g:255:1: ruleFloat EOF
            {
             before(grammarAccess.getFloatRule()); 
            pushFollow(FOLLOW_1);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getFloatRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleFloat"


    // $ANTLR start "ruleFloat"
    // InternalDft.g:262:1: ruleFloat : ( ( rule__Float__Group__0 ) ) ;
    public final void ruleFloat() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:266:2: ( ( ( rule__Float__Group__0 ) ) )
            // InternalDft.g:267:2: ( ( rule__Float__Group__0 ) )
            {
            // InternalDft.g:267:2: ( ( rule__Float__Group__0 ) )
            // InternalDft.g:268:3: ( rule__Float__Group__0 )
            {
             before(grammarAccess.getFloatAccess().getGroup()); 
            // InternalDft.g:269:3: ( rule__Float__Group__0 )
            // InternalDft.g:269:4: rule__Float__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Float__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getFloatAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleFloat"


    // $ANTLR start "rule__GalileoDft__Alternatives_3"
    // InternalDft.g:277:1: rule__GalileoDft__Alternatives_3 : ( ( ( rule__GalileoDft__Group_3_0__0 ) ) | ( ( rule__GalileoDft__Group_3_1__0 ) ) );
    public final void rule__GalileoDft__Alternatives_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:281:1: ( ( ( rule__GalileoDft__Group_3_0__0 ) ) | ( ( rule__GalileoDft__Group_3_1__0 ) ) )
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==RULE_STRING) ) {
                int LA1_1 = input.LA(2);

                if ( (LA1_1==28||LA1_1==30) ) {
                    alt1=2;
                }
                else if ( (LA1_1==RULE_XOFY||(LA1_1>=12 && LA1_1<=25)||LA1_1==34) ) {
                    alt1=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // InternalDft.g:282:2: ( ( rule__GalileoDft__Group_3_0__0 ) )
                    {
                    // InternalDft.g:282:2: ( ( rule__GalileoDft__Group_3_0__0 ) )
                    // InternalDft.g:283:3: ( rule__GalileoDft__Group_3_0__0 )
                    {
                     before(grammarAccess.getGalileoDftAccess().getGroup_3_0()); 
                    // InternalDft.g:284:3: ( rule__GalileoDft__Group_3_0__0 )
                    // InternalDft.g:284:4: rule__GalileoDft__Group_3_0__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoDft__Group_3_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getGalileoDftAccess().getGroup_3_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalDft.g:288:2: ( ( rule__GalileoDft__Group_3_1__0 ) )
                    {
                    // InternalDft.g:288:2: ( ( rule__GalileoDft__Group_3_1__0 ) )
                    // InternalDft.g:289:3: ( rule__GalileoDft__Group_3_1__0 )
                    {
                     before(grammarAccess.getGalileoDftAccess().getGroup_3_1()); 
                    // InternalDft.g:290:3: ( rule__GalileoDft__Group_3_1__0 )
                    // InternalDft.g:290:4: rule__GalileoDft__Group_3_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoDft__Group_3_1__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getGalileoDftAccess().getGroup_3_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Alternatives_3"


    // $ANTLR start "rule__GalileoBasicEvent__Alternatives_1"
    // InternalDft.g:298:1: rule__GalileoBasicEvent__Alternatives_1 : ( ( ( rule__GalileoBasicEvent__Group_1_0__0 ) ) | ( ( rule__GalileoBasicEvent__Group_1_1__0 ) ) );
    public final void rule__GalileoBasicEvent__Alternatives_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:302:1: ( ( ( rule__GalileoBasicEvent__Group_1_0__0 ) ) | ( ( rule__GalileoBasicEvent__Group_1_1__0 ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==28) ) {
                alt2=1;
            }
            else if ( (LA2_0==30) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // InternalDft.g:303:2: ( ( rule__GalileoBasicEvent__Group_1_0__0 ) )
                    {
                    // InternalDft.g:303:2: ( ( rule__GalileoBasicEvent__Group_1_0__0 ) )
                    // InternalDft.g:304:3: ( rule__GalileoBasicEvent__Group_1_0__0 )
                    {
                     before(grammarAccess.getGalileoBasicEventAccess().getGroup_1_0()); 
                    // InternalDft.g:305:3: ( rule__GalileoBasicEvent__Group_1_0__0 )
                    // InternalDft.g:305:4: rule__GalileoBasicEvent__Group_1_0__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoBasicEvent__Group_1_0__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getGalileoBasicEventAccess().getGroup_1_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalDft.g:309:2: ( ( rule__GalileoBasicEvent__Group_1_1__0 ) )
                    {
                    // InternalDft.g:309:2: ( ( rule__GalileoBasicEvent__Group_1_1__0 ) )
                    // InternalDft.g:310:3: ( rule__GalileoBasicEvent__Group_1_1__0 )
                    {
                     before(grammarAccess.getGalileoBasicEventAccess().getGroup_1_1()); 
                    // InternalDft.g:311:3: ( rule__GalileoBasicEvent__Group_1_1__0 )
                    // InternalDft.g:311:4: rule__GalileoBasicEvent__Group_1_1__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoBasicEvent__Group_1_1__0();

                    state._fsp--;


                    }

                     after(grammarAccess.getGalileoBasicEventAccess().getGroup_1_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Alternatives_1"


    // $ANTLR start "rule__GalileoNodeType__Alternatives"
    // InternalDft.g:319:1: rule__GalileoNodeType__Alternatives : ( ( ruleNamed ) | ( ruleParametrized ) | ( ruleObserver ) );
    public final void rule__GalileoNodeType__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:323:1: ( ( ruleNamed ) | ( ruleParametrized ) | ( ruleObserver ) )
            int alt3=3;
            switch ( input.LA(1) ) {
            case RULE_XOFY:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                {
                alt3=1;
                }
                break;
            case 24:
            case 25:
                {
                alt3=2;
                }
                break;
            case 34:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // InternalDft.g:324:2: ( ruleNamed )
                    {
                    // InternalDft.g:324:2: ( ruleNamed )
                    // InternalDft.g:325:3: ruleNamed
                    {
                     before(grammarAccess.getGalileoNodeTypeAccess().getNamedParserRuleCall_0()); 
                    pushFollow(FOLLOW_2);
                    ruleNamed();

                    state._fsp--;

                     after(grammarAccess.getGalileoNodeTypeAccess().getNamedParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalDft.g:330:2: ( ruleParametrized )
                    {
                    // InternalDft.g:330:2: ( ruleParametrized )
                    // InternalDft.g:331:3: ruleParametrized
                    {
                     before(grammarAccess.getGalileoNodeTypeAccess().getParametrizedParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
                    ruleParametrized();

                    state._fsp--;

                     after(grammarAccess.getGalileoNodeTypeAccess().getParametrizedParserRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalDft.g:336:2: ( ruleObserver )
                    {
                    // InternalDft.g:336:2: ( ruleObserver )
                    // InternalDft.g:337:3: ruleObserver
                    {
                     before(grammarAccess.getGalileoNodeTypeAccess().getObserverParserRuleCall_2()); 
                    pushFollow(FOLLOW_2);
                    ruleObserver();

                    state._fsp--;

                     after(grammarAccess.getGalileoNodeTypeAccess().getObserverParserRuleCall_2()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoNodeType__Alternatives"


    // $ANTLR start "rule__Named__TypeNameAlternatives_1_0"
    // InternalDft.g:346:1: rule__Named__TypeNameAlternatives_1_0 : ( ( 'and' ) | ( 'or' ) | ( RULE_XOFY ) | ( 'pand' ) | ( 'pand_i' ) | ( 'por' ) | ( 'por_i' ) | ( 'sand' ) | ( 'hsp' ) | ( 'wsp' ) | ( 'csp' ) | ( 'seq' ) | ( 'fdep' ) );
    public final void rule__Named__TypeNameAlternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:350:1: ( ( 'and' ) | ( 'or' ) | ( RULE_XOFY ) | ( 'pand' ) | ( 'pand_i' ) | ( 'por' ) | ( 'por_i' ) | ( 'sand' ) | ( 'hsp' ) | ( 'wsp' ) | ( 'csp' ) | ( 'seq' ) | ( 'fdep' ) )
            int alt4=13;
            switch ( input.LA(1) ) {
            case 12:
                {
                alt4=1;
                }
                break;
            case 13:
                {
                alt4=2;
                }
                break;
            case RULE_XOFY:
                {
                alt4=3;
                }
                break;
            case 14:
                {
                alt4=4;
                }
                break;
            case 15:
                {
                alt4=5;
                }
                break;
            case 16:
                {
                alt4=6;
                }
                break;
            case 17:
                {
                alt4=7;
                }
                break;
            case 18:
                {
                alt4=8;
                }
                break;
            case 19:
                {
                alt4=9;
                }
                break;
            case 20:
                {
                alt4=10;
                }
                break;
            case 21:
                {
                alt4=11;
                }
                break;
            case 22:
                {
                alt4=12;
                }
                break;
            case 23:
                {
                alt4=13;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // InternalDft.g:351:2: ( 'and' )
                    {
                    // InternalDft.g:351:2: ( 'and' )
                    // InternalDft.g:352:3: 'and'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameAndKeyword_1_0_0()); 
                    match(input,12,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameAndKeyword_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalDft.g:357:2: ( 'or' )
                    {
                    // InternalDft.g:357:2: ( 'or' )
                    // InternalDft.g:358:3: 'or'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameOrKeyword_1_0_1()); 
                    match(input,13,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameOrKeyword_1_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalDft.g:363:2: ( RULE_XOFY )
                    {
                    // InternalDft.g:363:2: ( RULE_XOFY )
                    // InternalDft.g:364:3: RULE_XOFY
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameXOFYTerminalRuleCall_1_0_2()); 
                    match(input,RULE_XOFY,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameXOFYTerminalRuleCall_1_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalDft.g:369:2: ( 'pand' )
                    {
                    // InternalDft.g:369:2: ( 'pand' )
                    // InternalDft.g:370:3: 'pand'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNamePandKeyword_1_0_3()); 
                    match(input,14,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNamePandKeyword_1_0_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalDft.g:375:2: ( 'pand_i' )
                    {
                    // InternalDft.g:375:2: ( 'pand_i' )
                    // InternalDft.g:376:3: 'pand_i'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNamePand_iKeyword_1_0_4()); 
                    match(input,15,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNamePand_iKeyword_1_0_4()); 

                    }


                    }
                    break;
                case 6 :
                    // InternalDft.g:381:2: ( 'por' )
                    {
                    // InternalDft.g:381:2: ( 'por' )
                    // InternalDft.g:382:3: 'por'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNamePorKeyword_1_0_5()); 
                    match(input,16,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNamePorKeyword_1_0_5()); 

                    }


                    }
                    break;
                case 7 :
                    // InternalDft.g:387:2: ( 'por_i' )
                    {
                    // InternalDft.g:387:2: ( 'por_i' )
                    // InternalDft.g:388:3: 'por_i'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNamePor_iKeyword_1_0_6()); 
                    match(input,17,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNamePor_iKeyword_1_0_6()); 

                    }


                    }
                    break;
                case 8 :
                    // InternalDft.g:393:2: ( 'sand' )
                    {
                    // InternalDft.g:393:2: ( 'sand' )
                    // InternalDft.g:394:3: 'sand'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameSandKeyword_1_0_7()); 
                    match(input,18,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameSandKeyword_1_0_7()); 

                    }


                    }
                    break;
                case 9 :
                    // InternalDft.g:399:2: ( 'hsp' )
                    {
                    // InternalDft.g:399:2: ( 'hsp' )
                    // InternalDft.g:400:3: 'hsp'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameHspKeyword_1_0_8()); 
                    match(input,19,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameHspKeyword_1_0_8()); 

                    }


                    }
                    break;
                case 10 :
                    // InternalDft.g:405:2: ( 'wsp' )
                    {
                    // InternalDft.g:405:2: ( 'wsp' )
                    // InternalDft.g:406:3: 'wsp'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameWspKeyword_1_0_9()); 
                    match(input,20,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameWspKeyword_1_0_9()); 

                    }


                    }
                    break;
                case 11 :
                    // InternalDft.g:411:2: ( 'csp' )
                    {
                    // InternalDft.g:411:2: ( 'csp' )
                    // InternalDft.g:412:3: 'csp'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameCspKeyword_1_0_10()); 
                    match(input,21,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameCspKeyword_1_0_10()); 

                    }


                    }
                    break;
                case 12 :
                    // InternalDft.g:417:2: ( 'seq' )
                    {
                    // InternalDft.g:417:2: ( 'seq' )
                    // InternalDft.g:418:3: 'seq'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameSeqKeyword_1_0_11()); 
                    match(input,22,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameSeqKeyword_1_0_11()); 

                    }


                    }
                    break;
                case 13 :
                    // InternalDft.g:423:2: ( 'fdep' )
                    {
                    // InternalDft.g:423:2: ( 'fdep' )
                    // InternalDft.g:424:3: 'fdep'
                    {
                     before(grammarAccess.getNamedAccess().getTypeNameFdepKeyword_1_0_12()); 
                    match(input,23,FOLLOW_2); 
                     after(grammarAccess.getNamedAccess().getTypeNameFdepKeyword_1_0_12()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Named__TypeNameAlternatives_1_0"


    // $ANTLR start "rule__Parametrized__TypeNameAlternatives_1_0"
    // InternalDft.g:433:1: rule__Parametrized__TypeNameAlternatives_1_0 : ( ( 'rdep' ) | ( 'delay' ) );
    public final void rule__Parametrized__TypeNameAlternatives_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:437:1: ( ( 'rdep' ) | ( 'delay' ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==24) ) {
                alt5=1;
            }
            else if ( (LA5_0==25) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalDft.g:438:2: ( 'rdep' )
                    {
                    // InternalDft.g:438:2: ( 'rdep' )
                    // InternalDft.g:439:3: 'rdep'
                    {
                     before(grammarAccess.getParametrizedAccess().getTypeNameRdepKeyword_1_0_0()); 
                    match(input,24,FOLLOW_2); 
                     after(grammarAccess.getParametrizedAccess().getTypeNameRdepKeyword_1_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalDft.g:444:2: ( 'delay' )
                    {
                    // InternalDft.g:444:2: ( 'delay' )
                    // InternalDft.g:445:3: 'delay'
                    {
                     before(grammarAccess.getParametrizedAccess().getTypeNameDelayKeyword_1_0_1()); 
                    match(input,25,FOLLOW_2); 
                     after(grammarAccess.getParametrizedAccess().getTypeNameDelayKeyword_1_0_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__TypeNameAlternatives_1_0"


    // $ANTLR start "rule__GalileoDft__Group__0"
    // InternalDft.g:454:1: rule__GalileoDft__Group__0 : rule__GalileoDft__Group__0__Impl rule__GalileoDft__Group__1 ;
    public final void rule__GalileoDft__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:458:1: ( rule__GalileoDft__Group__0__Impl rule__GalileoDft__Group__1 )
            // InternalDft.g:459:2: rule__GalileoDft__Group__0__Impl rule__GalileoDft__Group__1
            {
            pushFollow(FOLLOW_3);
            rule__GalileoDft__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group__0"


    // $ANTLR start "rule__GalileoDft__Group__0__Impl"
    // InternalDft.g:466:1: rule__GalileoDft__Group__0__Impl : ( 'toplevel' ) ;
    public final void rule__GalileoDft__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:470:1: ( ( 'toplevel' ) )
            // InternalDft.g:471:1: ( 'toplevel' )
            {
            // InternalDft.g:471:1: ( 'toplevel' )
            // InternalDft.g:472:2: 'toplevel'
            {
             before(grammarAccess.getGalileoDftAccess().getToplevelKeyword_0()); 
            match(input,26,FOLLOW_2); 
             after(grammarAccess.getGalileoDftAccess().getToplevelKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group__0__Impl"


    // $ANTLR start "rule__GalileoDft__Group__1"
    // InternalDft.g:481:1: rule__GalileoDft__Group__1 : rule__GalileoDft__Group__1__Impl rule__GalileoDft__Group__2 ;
    public final void rule__GalileoDft__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:485:1: ( rule__GalileoDft__Group__1__Impl rule__GalileoDft__Group__2 )
            // InternalDft.g:486:2: rule__GalileoDft__Group__1__Impl rule__GalileoDft__Group__2
            {
            pushFollow(FOLLOW_4);
            rule__GalileoDft__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group__1"


    // $ANTLR start "rule__GalileoDft__Group__1__Impl"
    // InternalDft.g:493:1: rule__GalileoDft__Group__1__Impl : ( ( rule__GalileoDft__RootAssignment_1 ) ) ;
    public final void rule__GalileoDft__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:497:1: ( ( ( rule__GalileoDft__RootAssignment_1 ) ) )
            // InternalDft.g:498:1: ( ( rule__GalileoDft__RootAssignment_1 ) )
            {
            // InternalDft.g:498:1: ( ( rule__GalileoDft__RootAssignment_1 ) )
            // InternalDft.g:499:2: ( rule__GalileoDft__RootAssignment_1 )
            {
             before(grammarAccess.getGalileoDftAccess().getRootAssignment_1()); 
            // InternalDft.g:500:2: ( rule__GalileoDft__RootAssignment_1 )
            // InternalDft.g:500:3: rule__GalileoDft__RootAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__GalileoDft__RootAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getGalileoDftAccess().getRootAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group__1__Impl"


    // $ANTLR start "rule__GalileoDft__Group__2"
    // InternalDft.g:508:1: rule__GalileoDft__Group__2 : rule__GalileoDft__Group__2__Impl rule__GalileoDft__Group__3 ;
    public final void rule__GalileoDft__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:512:1: ( rule__GalileoDft__Group__2__Impl rule__GalileoDft__Group__3 )
            // InternalDft.g:513:2: rule__GalileoDft__Group__2__Impl rule__GalileoDft__Group__3
            {
            pushFollow(FOLLOW_3);
            rule__GalileoDft__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group__2"


    // $ANTLR start "rule__GalileoDft__Group__2__Impl"
    // InternalDft.g:520:1: rule__GalileoDft__Group__2__Impl : ( ';' ) ;
    public final void rule__GalileoDft__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:524:1: ( ( ';' ) )
            // InternalDft.g:525:1: ( ';' )
            {
            // InternalDft.g:525:1: ( ';' )
            // InternalDft.g:526:2: ';'
            {
             before(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_2()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group__2__Impl"


    // $ANTLR start "rule__GalileoDft__Group__3"
    // InternalDft.g:535:1: rule__GalileoDft__Group__3 : rule__GalileoDft__Group__3__Impl ;
    public final void rule__GalileoDft__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:539:1: ( rule__GalileoDft__Group__3__Impl )
            // InternalDft.g:540:2: rule__GalileoDft__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group__3"


    // $ANTLR start "rule__GalileoDft__Group__3__Impl"
    // InternalDft.g:546:1: rule__GalileoDft__Group__3__Impl : ( ( rule__GalileoDft__Alternatives_3 )* ) ;
    public final void rule__GalileoDft__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:550:1: ( ( ( rule__GalileoDft__Alternatives_3 )* ) )
            // InternalDft.g:551:1: ( ( rule__GalileoDft__Alternatives_3 )* )
            {
            // InternalDft.g:551:1: ( ( rule__GalileoDft__Alternatives_3 )* )
            // InternalDft.g:552:2: ( rule__GalileoDft__Alternatives_3 )*
            {
             before(grammarAccess.getGalileoDftAccess().getAlternatives_3()); 
            // InternalDft.g:553:2: ( rule__GalileoDft__Alternatives_3 )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==RULE_STRING) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalDft.g:553:3: rule__GalileoDft__Alternatives_3
            	    {
            	    pushFollow(FOLLOW_5);
            	    rule__GalileoDft__Alternatives_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

             after(grammarAccess.getGalileoDftAccess().getAlternatives_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group__3__Impl"


    // $ANTLR start "rule__GalileoDft__Group_3_0__0"
    // InternalDft.g:562:1: rule__GalileoDft__Group_3_0__0 : rule__GalileoDft__Group_3_0__0__Impl rule__GalileoDft__Group_3_0__1 ;
    public final void rule__GalileoDft__Group_3_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:566:1: ( rule__GalileoDft__Group_3_0__0__Impl rule__GalileoDft__Group_3_0__1 )
            // InternalDft.g:567:2: rule__GalileoDft__Group_3_0__0__Impl rule__GalileoDft__Group_3_0__1
            {
            pushFollow(FOLLOW_4);
            rule__GalileoDft__Group_3_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group_3_0__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group_3_0__0"


    // $ANTLR start "rule__GalileoDft__Group_3_0__0__Impl"
    // InternalDft.g:574:1: rule__GalileoDft__Group_3_0__0__Impl : ( ( rule__GalileoDft__GatesAssignment_3_0_0 ) ) ;
    public final void rule__GalileoDft__Group_3_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:578:1: ( ( ( rule__GalileoDft__GatesAssignment_3_0_0 ) ) )
            // InternalDft.g:579:1: ( ( rule__GalileoDft__GatesAssignment_3_0_0 ) )
            {
            // InternalDft.g:579:1: ( ( rule__GalileoDft__GatesAssignment_3_0_0 ) )
            // InternalDft.g:580:2: ( rule__GalileoDft__GatesAssignment_3_0_0 )
            {
             before(grammarAccess.getGalileoDftAccess().getGatesAssignment_3_0_0()); 
            // InternalDft.g:581:2: ( rule__GalileoDft__GatesAssignment_3_0_0 )
            // InternalDft.g:581:3: rule__GalileoDft__GatesAssignment_3_0_0
            {
            pushFollow(FOLLOW_2);
            rule__GalileoDft__GatesAssignment_3_0_0();

            state._fsp--;


            }

             after(grammarAccess.getGalileoDftAccess().getGatesAssignment_3_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group_3_0__0__Impl"


    // $ANTLR start "rule__GalileoDft__Group_3_0__1"
    // InternalDft.g:589:1: rule__GalileoDft__Group_3_0__1 : rule__GalileoDft__Group_3_0__1__Impl ;
    public final void rule__GalileoDft__Group_3_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:593:1: ( rule__GalileoDft__Group_3_0__1__Impl )
            // InternalDft.g:594:2: rule__GalileoDft__Group_3_0__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group_3_0__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group_3_0__1"


    // $ANTLR start "rule__GalileoDft__Group_3_0__1__Impl"
    // InternalDft.g:600:1: rule__GalileoDft__Group_3_0__1__Impl : ( ';' ) ;
    public final void rule__GalileoDft__Group_3_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:604:1: ( ( ';' ) )
            // InternalDft.g:605:1: ( ';' )
            {
            // InternalDft.g:605:1: ( ';' )
            // InternalDft.g:606:2: ';'
            {
             before(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_0_1()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group_3_0__1__Impl"


    // $ANTLR start "rule__GalileoDft__Group_3_1__0"
    // InternalDft.g:616:1: rule__GalileoDft__Group_3_1__0 : rule__GalileoDft__Group_3_1__0__Impl rule__GalileoDft__Group_3_1__1 ;
    public final void rule__GalileoDft__Group_3_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:620:1: ( rule__GalileoDft__Group_3_1__0__Impl rule__GalileoDft__Group_3_1__1 )
            // InternalDft.g:621:2: rule__GalileoDft__Group_3_1__0__Impl rule__GalileoDft__Group_3_1__1
            {
            pushFollow(FOLLOW_4);
            rule__GalileoDft__Group_3_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group_3_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group_3_1__0"


    // $ANTLR start "rule__GalileoDft__Group_3_1__0__Impl"
    // InternalDft.g:628:1: rule__GalileoDft__Group_3_1__0__Impl : ( ( rule__GalileoDft__BasicEventsAssignment_3_1_0 ) ) ;
    public final void rule__GalileoDft__Group_3_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:632:1: ( ( ( rule__GalileoDft__BasicEventsAssignment_3_1_0 ) ) )
            // InternalDft.g:633:1: ( ( rule__GalileoDft__BasicEventsAssignment_3_1_0 ) )
            {
            // InternalDft.g:633:1: ( ( rule__GalileoDft__BasicEventsAssignment_3_1_0 ) )
            // InternalDft.g:634:2: ( rule__GalileoDft__BasicEventsAssignment_3_1_0 )
            {
             before(grammarAccess.getGalileoDftAccess().getBasicEventsAssignment_3_1_0()); 
            // InternalDft.g:635:2: ( rule__GalileoDft__BasicEventsAssignment_3_1_0 )
            // InternalDft.g:635:3: rule__GalileoDft__BasicEventsAssignment_3_1_0
            {
            pushFollow(FOLLOW_2);
            rule__GalileoDft__BasicEventsAssignment_3_1_0();

            state._fsp--;


            }

             after(grammarAccess.getGalileoDftAccess().getBasicEventsAssignment_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group_3_1__0__Impl"


    // $ANTLR start "rule__GalileoDft__Group_3_1__1"
    // InternalDft.g:643:1: rule__GalileoDft__Group_3_1__1 : rule__GalileoDft__Group_3_1__1__Impl ;
    public final void rule__GalileoDft__Group_3_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:647:1: ( rule__GalileoDft__Group_3_1__1__Impl )
            // InternalDft.g:648:2: rule__GalileoDft__Group_3_1__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoDft__Group_3_1__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group_3_1__1"


    // $ANTLR start "rule__GalileoDft__Group_3_1__1__Impl"
    // InternalDft.g:654:1: rule__GalileoDft__Group_3_1__1__Impl : ( ';' ) ;
    public final void rule__GalileoDft__Group_3_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:658:1: ( ( ';' ) )
            // InternalDft.g:659:1: ( ';' )
            {
            // InternalDft.g:659:1: ( ';' )
            // InternalDft.g:660:2: ';'
            {
             before(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_1_1()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__Group_3_1__1__Impl"


    // $ANTLR start "rule__GalileoGate__Group__0"
    // InternalDft.g:670:1: rule__GalileoGate__Group__0 : rule__GalileoGate__Group__0__Impl rule__GalileoGate__Group__1 ;
    public final void rule__GalileoGate__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:674:1: ( rule__GalileoGate__Group__0__Impl rule__GalileoGate__Group__1 )
            // InternalDft.g:675:2: rule__GalileoGate__Group__0__Impl rule__GalileoGate__Group__1
            {
            pushFollow(FOLLOW_6);
            rule__GalileoGate__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoGate__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__Group__0"


    // $ANTLR start "rule__GalileoGate__Group__0__Impl"
    // InternalDft.g:682:1: rule__GalileoGate__Group__0__Impl : ( ( rule__GalileoGate__NameAssignment_0 ) ) ;
    public final void rule__GalileoGate__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:686:1: ( ( ( rule__GalileoGate__NameAssignment_0 ) ) )
            // InternalDft.g:687:1: ( ( rule__GalileoGate__NameAssignment_0 ) )
            {
            // InternalDft.g:687:1: ( ( rule__GalileoGate__NameAssignment_0 ) )
            // InternalDft.g:688:2: ( rule__GalileoGate__NameAssignment_0 )
            {
             before(grammarAccess.getGalileoGateAccess().getNameAssignment_0()); 
            // InternalDft.g:689:2: ( rule__GalileoGate__NameAssignment_0 )
            // InternalDft.g:689:3: rule__GalileoGate__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__GalileoGate__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getGalileoGateAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__Group__0__Impl"


    // $ANTLR start "rule__GalileoGate__Group__1"
    // InternalDft.g:697:1: rule__GalileoGate__Group__1 : rule__GalileoGate__Group__1__Impl rule__GalileoGate__Group__2 ;
    public final void rule__GalileoGate__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:701:1: ( rule__GalileoGate__Group__1__Impl rule__GalileoGate__Group__2 )
            // InternalDft.g:702:2: rule__GalileoGate__Group__1__Impl rule__GalileoGate__Group__2
            {
            pushFollow(FOLLOW_3);
            rule__GalileoGate__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoGate__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__Group__1"


    // $ANTLR start "rule__GalileoGate__Group__1__Impl"
    // InternalDft.g:709:1: rule__GalileoGate__Group__1__Impl : ( ( rule__GalileoGate__TypeAssignment_1 ) ) ;
    public final void rule__GalileoGate__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:713:1: ( ( ( rule__GalileoGate__TypeAssignment_1 ) ) )
            // InternalDft.g:714:1: ( ( rule__GalileoGate__TypeAssignment_1 ) )
            {
            // InternalDft.g:714:1: ( ( rule__GalileoGate__TypeAssignment_1 ) )
            // InternalDft.g:715:2: ( rule__GalileoGate__TypeAssignment_1 )
            {
             before(grammarAccess.getGalileoGateAccess().getTypeAssignment_1()); 
            // InternalDft.g:716:2: ( rule__GalileoGate__TypeAssignment_1 )
            // InternalDft.g:716:3: rule__GalileoGate__TypeAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__GalileoGate__TypeAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getGalileoGateAccess().getTypeAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__Group__1__Impl"


    // $ANTLR start "rule__GalileoGate__Group__2"
    // InternalDft.g:724:1: rule__GalileoGate__Group__2 : rule__GalileoGate__Group__2__Impl ;
    public final void rule__GalileoGate__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:728:1: ( rule__GalileoGate__Group__2__Impl )
            // InternalDft.g:729:2: rule__GalileoGate__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoGate__Group__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__Group__2"


    // $ANTLR start "rule__GalileoGate__Group__2__Impl"
    // InternalDft.g:735:1: rule__GalileoGate__Group__2__Impl : ( ( rule__GalileoGate__ChildrenAssignment_2 )* ) ;
    public final void rule__GalileoGate__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:739:1: ( ( ( rule__GalileoGate__ChildrenAssignment_2 )* ) )
            // InternalDft.g:740:1: ( ( rule__GalileoGate__ChildrenAssignment_2 )* )
            {
            // InternalDft.g:740:1: ( ( rule__GalileoGate__ChildrenAssignment_2 )* )
            // InternalDft.g:741:2: ( rule__GalileoGate__ChildrenAssignment_2 )*
            {
             before(grammarAccess.getGalileoGateAccess().getChildrenAssignment_2()); 
            // InternalDft.g:742:2: ( rule__GalileoGate__ChildrenAssignment_2 )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==RULE_STRING) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalDft.g:742:3: rule__GalileoGate__ChildrenAssignment_2
            	    {
            	    pushFollow(FOLLOW_5);
            	    rule__GalileoGate__ChildrenAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

             after(grammarAccess.getGalileoGateAccess().getChildrenAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__Group__2__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group__0"
    // InternalDft.g:751:1: rule__GalileoBasicEvent__Group__0 : rule__GalileoBasicEvent__Group__0__Impl rule__GalileoBasicEvent__Group__1 ;
    public final void rule__GalileoBasicEvent__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:755:1: ( rule__GalileoBasicEvent__Group__0__Impl rule__GalileoBasicEvent__Group__1 )
            // InternalDft.g:756:2: rule__GalileoBasicEvent__Group__0__Impl rule__GalileoBasicEvent__Group__1
            {
            pushFollow(FOLLOW_7);
            rule__GalileoBasicEvent__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group__0"


    // $ANTLR start "rule__GalileoBasicEvent__Group__0__Impl"
    // InternalDft.g:763:1: rule__GalileoBasicEvent__Group__0__Impl : ( ( rule__GalileoBasicEvent__NameAssignment_0 ) ) ;
    public final void rule__GalileoBasicEvent__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:767:1: ( ( ( rule__GalileoBasicEvent__NameAssignment_0 ) ) )
            // InternalDft.g:768:1: ( ( rule__GalileoBasicEvent__NameAssignment_0 ) )
            {
            // InternalDft.g:768:1: ( ( rule__GalileoBasicEvent__NameAssignment_0 ) )
            // InternalDft.g:769:2: ( rule__GalileoBasicEvent__NameAssignment_0 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getNameAssignment_0()); 
            // InternalDft.g:770:2: ( rule__GalileoBasicEvent__NameAssignment_0 )
            // InternalDft.g:770:3: rule__GalileoBasicEvent__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group__0__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group__1"
    // InternalDft.g:778:1: rule__GalileoBasicEvent__Group__1 : rule__GalileoBasicEvent__Group__1__Impl rule__GalileoBasicEvent__Group__2 ;
    public final void rule__GalileoBasicEvent__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:782:1: ( rule__GalileoBasicEvent__Group__1__Impl rule__GalileoBasicEvent__Group__2 )
            // InternalDft.g:783:2: rule__GalileoBasicEvent__Group__1__Impl rule__GalileoBasicEvent__Group__2
            {
            pushFollow(FOLLOW_8);
            rule__GalileoBasicEvent__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group__1"


    // $ANTLR start "rule__GalileoBasicEvent__Group__1__Impl"
    // InternalDft.g:790:1: rule__GalileoBasicEvent__Group__1__Impl : ( ( rule__GalileoBasicEvent__Alternatives_1 ) ) ;
    public final void rule__GalileoBasicEvent__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:794:1: ( ( ( rule__GalileoBasicEvent__Alternatives_1 ) ) )
            // InternalDft.g:795:1: ( ( rule__GalileoBasicEvent__Alternatives_1 ) )
            {
            // InternalDft.g:795:1: ( ( rule__GalileoBasicEvent__Alternatives_1 ) )
            // InternalDft.g:796:2: ( rule__GalileoBasicEvent__Alternatives_1 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getAlternatives_1()); 
            // InternalDft.g:797:2: ( rule__GalileoBasicEvent__Alternatives_1 )
            // InternalDft.g:797:3: rule__GalileoBasicEvent__Alternatives_1
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Alternatives_1();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getAlternatives_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group__1__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group__2"
    // InternalDft.g:805:1: rule__GalileoBasicEvent__Group__2 : rule__GalileoBasicEvent__Group__2__Impl rule__GalileoBasicEvent__Group__3 ;
    public final void rule__GalileoBasicEvent__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:809:1: ( rule__GalileoBasicEvent__Group__2__Impl rule__GalileoBasicEvent__Group__3 )
            // InternalDft.g:810:2: rule__GalileoBasicEvent__Group__2__Impl rule__GalileoBasicEvent__Group__3
            {
            pushFollow(FOLLOW_8);
            rule__GalileoBasicEvent__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group__2"


    // $ANTLR start "rule__GalileoBasicEvent__Group__2__Impl"
    // InternalDft.g:817:1: rule__GalileoBasicEvent__Group__2__Impl : ( ( rule__GalileoBasicEvent__Group_2__0 )? ) ;
    public final void rule__GalileoBasicEvent__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:821:1: ( ( ( rule__GalileoBasicEvent__Group_2__0 )? ) )
            // InternalDft.g:822:1: ( ( rule__GalileoBasicEvent__Group_2__0 )? )
            {
            // InternalDft.g:822:1: ( ( rule__GalileoBasicEvent__Group_2__0 )? )
            // InternalDft.g:823:2: ( rule__GalileoBasicEvent__Group_2__0 )?
            {
             before(grammarAccess.getGalileoBasicEventAccess().getGroup_2()); 
            // InternalDft.g:824:2: ( rule__GalileoBasicEvent__Group_2__0 )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==31) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // InternalDft.g:824:3: rule__GalileoBasicEvent__Group_2__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoBasicEvent__Group_2__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGalileoBasicEventAccess().getGroup_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group__2__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group__3"
    // InternalDft.g:832:1: rule__GalileoBasicEvent__Group__3 : rule__GalileoBasicEvent__Group__3__Impl ;
    public final void rule__GalileoBasicEvent__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:836:1: ( rule__GalileoBasicEvent__Group__3__Impl )
            // InternalDft.g:837:2: rule__GalileoBasicEvent__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group__3"


    // $ANTLR start "rule__GalileoBasicEvent__Group__3__Impl"
    // InternalDft.g:843:1: rule__GalileoBasicEvent__Group__3__Impl : ( ( rule__GalileoBasicEvent__RepairActionsAssignment_3 )* ) ;
    public final void rule__GalileoBasicEvent__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:847:1: ( ( ( rule__GalileoBasicEvent__RepairActionsAssignment_3 )* ) )
            // InternalDft.g:848:1: ( ( rule__GalileoBasicEvent__RepairActionsAssignment_3 )* )
            {
            // InternalDft.g:848:1: ( ( rule__GalileoBasicEvent__RepairActionsAssignment_3 )* )
            // InternalDft.g:849:2: ( rule__GalileoBasicEvent__RepairActionsAssignment_3 )*
            {
             before(grammarAccess.getGalileoBasicEventAccess().getRepairActionsAssignment_3()); 
            // InternalDft.g:850:2: ( rule__GalileoBasicEvent__RepairActionsAssignment_3 )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==32) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalDft.g:850:3: rule__GalileoBasicEvent__RepairActionsAssignment_3
            	    {
            	    pushFollow(FOLLOW_9);
            	    rule__GalileoBasicEvent__RepairActionsAssignment_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

             after(grammarAccess.getGalileoBasicEventAccess().getRepairActionsAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group__3__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_0__0"
    // InternalDft.g:859:1: rule__GalileoBasicEvent__Group_1_0__0 : rule__GalileoBasicEvent__Group_1_0__0__Impl rule__GalileoBasicEvent__Group_1_0__1 ;
    public final void rule__GalileoBasicEvent__Group_1_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:863:1: ( rule__GalileoBasicEvent__Group_1_0__0__Impl rule__GalileoBasicEvent__Group_1_0__1 )
            // InternalDft.g:864:2: rule__GalileoBasicEvent__Group_1_0__0__Impl rule__GalileoBasicEvent__Group_1_0__1
            {
            pushFollow(FOLLOW_10);
            rule__GalileoBasicEvent__Group_1_0__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_1_0__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_0__0"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_0__0__Impl"
    // InternalDft.g:871:1: rule__GalileoBasicEvent__Group_1_0__0__Impl : ( 'lambda' ) ;
    public final void rule__GalileoBasicEvent__Group_1_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:875:1: ( ( 'lambda' ) )
            // InternalDft.g:876:1: ( 'lambda' )
            {
            // InternalDft.g:876:1: ( 'lambda' )
            // InternalDft.g:877:2: 'lambda'
            {
             before(grammarAccess.getGalileoBasicEventAccess().getLambdaKeyword_1_0_0()); 
            match(input,28,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getLambdaKeyword_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_0__0__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_0__1"
    // InternalDft.g:886:1: rule__GalileoBasicEvent__Group_1_0__1 : rule__GalileoBasicEvent__Group_1_0__1__Impl rule__GalileoBasicEvent__Group_1_0__2 ;
    public final void rule__GalileoBasicEvent__Group_1_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:890:1: ( rule__GalileoBasicEvent__Group_1_0__1__Impl rule__GalileoBasicEvent__Group_1_0__2 )
            // InternalDft.g:891:2: rule__GalileoBasicEvent__Group_1_0__1__Impl rule__GalileoBasicEvent__Group_1_0__2
            {
            pushFollow(FOLLOW_11);
            rule__GalileoBasicEvent__Group_1_0__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_1_0__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_0__1"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_0__1__Impl"
    // InternalDft.g:898:1: rule__GalileoBasicEvent__Group_1_0__1__Impl : ( '=' ) ;
    public final void rule__GalileoBasicEvent__Group_1_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:902:1: ( ( '=' ) )
            // InternalDft.g:903:1: ( '=' )
            {
            // InternalDft.g:903:1: ( '=' )
            // InternalDft.g:904:2: '='
            {
             before(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_1_0_1()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_1_0_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_0__1__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_0__2"
    // InternalDft.g:913:1: rule__GalileoBasicEvent__Group_1_0__2 : rule__GalileoBasicEvent__Group_1_0__2__Impl ;
    public final void rule__GalileoBasicEvent__Group_1_0__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:917:1: ( rule__GalileoBasicEvent__Group_1_0__2__Impl )
            // InternalDft.g:918:2: rule__GalileoBasicEvent__Group_1_0__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_1_0__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_0__2"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_0__2__Impl"
    // InternalDft.g:924:1: rule__GalileoBasicEvent__Group_1_0__2__Impl : ( ( rule__GalileoBasicEvent__LambdaAssignment_1_0_2 ) ) ;
    public final void rule__GalileoBasicEvent__Group_1_0__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:928:1: ( ( ( rule__GalileoBasicEvent__LambdaAssignment_1_0_2 ) ) )
            // InternalDft.g:929:1: ( ( rule__GalileoBasicEvent__LambdaAssignment_1_0_2 ) )
            {
            // InternalDft.g:929:1: ( ( rule__GalileoBasicEvent__LambdaAssignment_1_0_2 ) )
            // InternalDft.g:930:2: ( rule__GalileoBasicEvent__LambdaAssignment_1_0_2 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getLambdaAssignment_1_0_2()); 
            // InternalDft.g:931:2: ( rule__GalileoBasicEvent__LambdaAssignment_1_0_2 )
            // InternalDft.g:931:3: rule__GalileoBasicEvent__LambdaAssignment_1_0_2
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__LambdaAssignment_1_0_2();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getLambdaAssignment_1_0_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_0__2__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_1__0"
    // InternalDft.g:940:1: rule__GalileoBasicEvent__Group_1_1__0 : rule__GalileoBasicEvent__Group_1_1__0__Impl rule__GalileoBasicEvent__Group_1_1__1 ;
    public final void rule__GalileoBasicEvent__Group_1_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:944:1: ( rule__GalileoBasicEvent__Group_1_1__0__Impl rule__GalileoBasicEvent__Group_1_1__1 )
            // InternalDft.g:945:2: rule__GalileoBasicEvent__Group_1_1__0__Impl rule__GalileoBasicEvent__Group_1_1__1
            {
            pushFollow(FOLLOW_10);
            rule__GalileoBasicEvent__Group_1_1__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_1_1__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_1__0"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_1__0__Impl"
    // InternalDft.g:952:1: rule__GalileoBasicEvent__Group_1_1__0__Impl : ( 'prob' ) ;
    public final void rule__GalileoBasicEvent__Group_1_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:956:1: ( ( 'prob' ) )
            // InternalDft.g:957:1: ( 'prob' )
            {
            // InternalDft.g:957:1: ( 'prob' )
            // InternalDft.g:958:2: 'prob'
            {
             before(grammarAccess.getGalileoBasicEventAccess().getProbKeyword_1_1_0()); 
            match(input,30,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getProbKeyword_1_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_1__0__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_1__1"
    // InternalDft.g:967:1: rule__GalileoBasicEvent__Group_1_1__1 : rule__GalileoBasicEvent__Group_1_1__1__Impl rule__GalileoBasicEvent__Group_1_1__2 ;
    public final void rule__GalileoBasicEvent__Group_1_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:971:1: ( rule__GalileoBasicEvent__Group_1_1__1__Impl rule__GalileoBasicEvent__Group_1_1__2 )
            // InternalDft.g:972:2: rule__GalileoBasicEvent__Group_1_1__1__Impl rule__GalileoBasicEvent__Group_1_1__2
            {
            pushFollow(FOLLOW_11);
            rule__GalileoBasicEvent__Group_1_1__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_1_1__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_1__1"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_1__1__Impl"
    // InternalDft.g:979:1: rule__GalileoBasicEvent__Group_1_1__1__Impl : ( '=' ) ;
    public final void rule__GalileoBasicEvent__Group_1_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:983:1: ( ( '=' ) )
            // InternalDft.g:984:1: ( '=' )
            {
            // InternalDft.g:984:1: ( '=' )
            // InternalDft.g:985:2: '='
            {
             before(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_1_1_1()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_1_1_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_1__1__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_1__2"
    // InternalDft.g:994:1: rule__GalileoBasicEvent__Group_1_1__2 : rule__GalileoBasicEvent__Group_1_1__2__Impl ;
    public final void rule__GalileoBasicEvent__Group_1_1__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:998:1: ( rule__GalileoBasicEvent__Group_1_1__2__Impl )
            // InternalDft.g:999:2: rule__GalileoBasicEvent__Group_1_1__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_1_1__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_1__2"


    // $ANTLR start "rule__GalileoBasicEvent__Group_1_1__2__Impl"
    // InternalDft.g:1005:1: rule__GalileoBasicEvent__Group_1_1__2__Impl : ( ( rule__GalileoBasicEvent__ProbAssignment_1_1_2 ) ) ;
    public final void rule__GalileoBasicEvent__Group_1_1__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1009:1: ( ( ( rule__GalileoBasicEvent__ProbAssignment_1_1_2 ) ) )
            // InternalDft.g:1010:1: ( ( rule__GalileoBasicEvent__ProbAssignment_1_1_2 ) )
            {
            // InternalDft.g:1010:1: ( ( rule__GalileoBasicEvent__ProbAssignment_1_1_2 ) )
            // InternalDft.g:1011:2: ( rule__GalileoBasicEvent__ProbAssignment_1_1_2 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getProbAssignment_1_1_2()); 
            // InternalDft.g:1012:2: ( rule__GalileoBasicEvent__ProbAssignment_1_1_2 )
            // InternalDft.g:1012:3: rule__GalileoBasicEvent__ProbAssignment_1_1_2
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__ProbAssignment_1_1_2();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getProbAssignment_1_1_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_1_1__2__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_2__0"
    // InternalDft.g:1021:1: rule__GalileoBasicEvent__Group_2__0 : rule__GalileoBasicEvent__Group_2__0__Impl rule__GalileoBasicEvent__Group_2__1 ;
    public final void rule__GalileoBasicEvent__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1025:1: ( rule__GalileoBasicEvent__Group_2__0__Impl rule__GalileoBasicEvent__Group_2__1 )
            // InternalDft.g:1026:2: rule__GalileoBasicEvent__Group_2__0__Impl rule__GalileoBasicEvent__Group_2__1
            {
            pushFollow(FOLLOW_10);
            rule__GalileoBasicEvent__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_2__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_2__0"


    // $ANTLR start "rule__GalileoBasicEvent__Group_2__0__Impl"
    // InternalDft.g:1033:1: rule__GalileoBasicEvent__Group_2__0__Impl : ( 'dorm' ) ;
    public final void rule__GalileoBasicEvent__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1037:1: ( ( 'dorm' ) )
            // InternalDft.g:1038:1: ( 'dorm' )
            {
            // InternalDft.g:1038:1: ( 'dorm' )
            // InternalDft.g:1039:2: 'dorm'
            {
             before(grammarAccess.getGalileoBasicEventAccess().getDormKeyword_2_0()); 
            match(input,31,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getDormKeyword_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_2__0__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_2__1"
    // InternalDft.g:1048:1: rule__GalileoBasicEvent__Group_2__1 : rule__GalileoBasicEvent__Group_2__1__Impl rule__GalileoBasicEvent__Group_2__2 ;
    public final void rule__GalileoBasicEvent__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1052:1: ( rule__GalileoBasicEvent__Group_2__1__Impl rule__GalileoBasicEvent__Group_2__2 )
            // InternalDft.g:1053:2: rule__GalileoBasicEvent__Group_2__1__Impl rule__GalileoBasicEvent__Group_2__2
            {
            pushFollow(FOLLOW_11);
            rule__GalileoBasicEvent__Group_2__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_2__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_2__1"


    // $ANTLR start "rule__GalileoBasicEvent__Group_2__1__Impl"
    // InternalDft.g:1060:1: rule__GalileoBasicEvent__Group_2__1__Impl : ( '=' ) ;
    public final void rule__GalileoBasicEvent__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1064:1: ( ( '=' ) )
            // InternalDft.g:1065:1: ( '=' )
            {
            // InternalDft.g:1065:1: ( '=' )
            // InternalDft.g:1066:2: '='
            {
             before(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_2_1()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_2__1__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_2__2"
    // InternalDft.g:1075:1: rule__GalileoBasicEvent__Group_2__2 : rule__GalileoBasicEvent__Group_2__2__Impl ;
    public final void rule__GalileoBasicEvent__Group_2__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1079:1: ( rule__GalileoBasicEvent__Group_2__2__Impl )
            // InternalDft.g:1080:2: rule__GalileoBasicEvent__Group_2__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_2__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_2__2"


    // $ANTLR start "rule__GalileoBasicEvent__Group_2__2__Impl"
    // InternalDft.g:1086:1: rule__GalileoBasicEvent__Group_2__2__Impl : ( ( rule__GalileoBasicEvent__DormAssignment_2_2 ) ) ;
    public final void rule__GalileoBasicEvent__Group_2__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1090:1: ( ( ( rule__GalileoBasicEvent__DormAssignment_2_2 ) ) )
            // InternalDft.g:1091:1: ( ( rule__GalileoBasicEvent__DormAssignment_2_2 ) )
            {
            // InternalDft.g:1091:1: ( ( rule__GalileoBasicEvent__DormAssignment_2_2 ) )
            // InternalDft.g:1092:2: ( rule__GalileoBasicEvent__DormAssignment_2_2 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getDormAssignment_2_2()); 
            // InternalDft.g:1093:2: ( rule__GalileoBasicEvent__DormAssignment_2_2 )
            // InternalDft.g:1093:3: rule__GalileoBasicEvent__DormAssignment_2_2
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__DormAssignment_2_2();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getDormAssignment_2_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__Group_2__2__Impl"


    // $ANTLR start "rule__GalileoRepairAction__Group__0"
    // InternalDft.g:1102:1: rule__GalileoRepairAction__Group__0 : rule__GalileoRepairAction__Group__0__Impl rule__GalileoRepairAction__Group__1 ;
    public final void rule__GalileoRepairAction__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1106:1: ( rule__GalileoRepairAction__Group__0__Impl rule__GalileoRepairAction__Group__1 )
            // InternalDft.g:1107:2: rule__GalileoRepairAction__Group__0__Impl rule__GalileoRepairAction__Group__1
            {
            pushFollow(FOLLOW_10);
            rule__GalileoRepairAction__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__0"


    // $ANTLR start "rule__GalileoRepairAction__Group__0__Impl"
    // InternalDft.g:1114:1: rule__GalileoRepairAction__Group__0__Impl : ( 'repair' ) ;
    public final void rule__GalileoRepairAction__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1118:1: ( ( 'repair' ) )
            // InternalDft.g:1119:1: ( 'repair' )
            {
            // InternalDft.g:1119:1: ( 'repair' )
            // InternalDft.g:1120:2: 'repair'
            {
             before(grammarAccess.getGalileoRepairActionAccess().getRepairKeyword_0()); 
            match(input,32,FOLLOW_2); 
             after(grammarAccess.getGalileoRepairActionAccess().getRepairKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__0__Impl"


    // $ANTLR start "rule__GalileoRepairAction__Group__1"
    // InternalDft.g:1129:1: rule__GalileoRepairAction__Group__1 : rule__GalileoRepairAction__Group__1__Impl rule__GalileoRepairAction__Group__2 ;
    public final void rule__GalileoRepairAction__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1133:1: ( rule__GalileoRepairAction__Group__1__Impl rule__GalileoRepairAction__Group__2 )
            // InternalDft.g:1134:2: rule__GalileoRepairAction__Group__1__Impl rule__GalileoRepairAction__Group__2
            {
            pushFollow(FOLLOW_11);
            rule__GalileoRepairAction__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__1"


    // $ANTLR start "rule__GalileoRepairAction__Group__1__Impl"
    // InternalDft.g:1141:1: rule__GalileoRepairAction__Group__1__Impl : ( '=' ) ;
    public final void rule__GalileoRepairAction__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1145:1: ( ( '=' ) )
            // InternalDft.g:1146:1: ( '=' )
            {
            // InternalDft.g:1146:1: ( '=' )
            // InternalDft.g:1147:2: '='
            {
             before(grammarAccess.getGalileoRepairActionAccess().getEqualsSignKeyword_1()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getGalileoRepairActionAccess().getEqualsSignKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__1__Impl"


    // $ANTLR start "rule__GalileoRepairAction__Group__2"
    // InternalDft.g:1156:1: rule__GalileoRepairAction__Group__2 : rule__GalileoRepairAction__Group__2__Impl rule__GalileoRepairAction__Group__3 ;
    public final void rule__GalileoRepairAction__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1160:1: ( rule__GalileoRepairAction__Group__2__Impl rule__GalileoRepairAction__Group__3 )
            // InternalDft.g:1161:2: rule__GalileoRepairAction__Group__2__Impl rule__GalileoRepairAction__Group__3
            {
            pushFollow(FOLLOW_12);
            rule__GalileoRepairAction__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__2"


    // $ANTLR start "rule__GalileoRepairAction__Group__2__Impl"
    // InternalDft.g:1168:1: rule__GalileoRepairAction__Group__2__Impl : ( ( rule__GalileoRepairAction__RepairAssignment_2 ) ) ;
    public final void rule__GalileoRepairAction__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1172:1: ( ( ( rule__GalileoRepairAction__RepairAssignment_2 ) ) )
            // InternalDft.g:1173:1: ( ( rule__GalileoRepairAction__RepairAssignment_2 ) )
            {
            // InternalDft.g:1173:1: ( ( rule__GalileoRepairAction__RepairAssignment_2 ) )
            // InternalDft.g:1174:2: ( rule__GalileoRepairAction__RepairAssignment_2 )
            {
             before(grammarAccess.getGalileoRepairActionAccess().getRepairAssignment_2()); 
            // InternalDft.g:1175:2: ( rule__GalileoRepairAction__RepairAssignment_2 )
            // InternalDft.g:1175:3: rule__GalileoRepairAction__RepairAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__RepairAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getGalileoRepairActionAccess().getRepairAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__2__Impl"


    // $ANTLR start "rule__GalileoRepairAction__Group__3"
    // InternalDft.g:1183:1: rule__GalileoRepairAction__Group__3 : rule__GalileoRepairAction__Group__3__Impl rule__GalileoRepairAction__Group__4 ;
    public final void rule__GalileoRepairAction__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1187:1: ( rule__GalileoRepairAction__Group__3__Impl rule__GalileoRepairAction__Group__4 )
            // InternalDft.g:1188:2: rule__GalileoRepairAction__Group__3__Impl rule__GalileoRepairAction__Group__4
            {
            pushFollow(FOLLOW_12);
            rule__GalileoRepairAction__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__3"


    // $ANTLR start "rule__GalileoRepairAction__Group__3__Impl"
    // InternalDft.g:1195:1: rule__GalileoRepairAction__Group__3__Impl : ( ( rule__GalileoRepairAction__NameAssignment_3 )? ) ;
    public final void rule__GalileoRepairAction__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1199:1: ( ( ( rule__GalileoRepairAction__NameAssignment_3 )? ) )
            // InternalDft.g:1200:1: ( ( rule__GalileoRepairAction__NameAssignment_3 )? )
            {
            // InternalDft.g:1200:1: ( ( rule__GalileoRepairAction__NameAssignment_3 )? )
            // InternalDft.g:1201:2: ( rule__GalileoRepairAction__NameAssignment_3 )?
            {
             before(grammarAccess.getGalileoRepairActionAccess().getNameAssignment_3()); 
            // InternalDft.g:1202:2: ( rule__GalileoRepairAction__NameAssignment_3 )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==RULE_STRING) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalDft.g:1202:3: rule__GalileoRepairAction__NameAssignment_3
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoRepairAction__NameAssignment_3();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGalileoRepairActionAccess().getNameAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__3__Impl"


    // $ANTLR start "rule__GalileoRepairAction__Group__4"
    // InternalDft.g:1210:1: rule__GalileoRepairAction__Group__4 : rule__GalileoRepairAction__Group__4__Impl ;
    public final void rule__GalileoRepairAction__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1214:1: ( rule__GalileoRepairAction__Group__4__Impl )
            // InternalDft.g:1215:2: rule__GalileoRepairAction__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__Group__4__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__4"


    // $ANTLR start "rule__GalileoRepairAction__Group__4__Impl"
    // InternalDft.g:1221:1: rule__GalileoRepairAction__Group__4__Impl : ( ( rule__GalileoRepairAction__Group_4__0 )? ) ;
    public final void rule__GalileoRepairAction__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1225:1: ( ( ( rule__GalileoRepairAction__Group_4__0 )? ) )
            // InternalDft.g:1226:1: ( ( rule__GalileoRepairAction__Group_4__0 )? )
            {
            // InternalDft.g:1226:1: ( ( rule__GalileoRepairAction__Group_4__0 )? )
            // InternalDft.g:1227:2: ( rule__GalileoRepairAction__Group_4__0 )?
            {
             before(grammarAccess.getGalileoRepairActionAccess().getGroup_4()); 
            // InternalDft.g:1228:2: ( rule__GalileoRepairAction__Group_4__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==33) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalDft.g:1228:3: rule__GalileoRepairAction__Group_4__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoRepairAction__Group_4__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGalileoRepairActionAccess().getGroup_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group__4__Impl"


    // $ANTLR start "rule__GalileoRepairAction__Group_4__0"
    // InternalDft.g:1237:1: rule__GalileoRepairAction__Group_4__0 : rule__GalileoRepairAction__Group_4__0__Impl rule__GalileoRepairAction__Group_4__1 ;
    public final void rule__GalileoRepairAction__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1241:1: ( rule__GalileoRepairAction__Group_4__0__Impl rule__GalileoRepairAction__Group_4__1 )
            // InternalDft.g:1242:2: rule__GalileoRepairAction__Group_4__0__Impl rule__GalileoRepairAction__Group_4__1
            {
            pushFollow(FOLLOW_3);
            rule__GalileoRepairAction__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__Group_4__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group_4__0"


    // $ANTLR start "rule__GalileoRepairAction__Group_4__0__Impl"
    // InternalDft.g:1249:1: rule__GalileoRepairAction__Group_4__0__Impl : ( 'observations' ) ;
    public final void rule__GalileoRepairAction__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1253:1: ( ( 'observations' ) )
            // InternalDft.g:1254:1: ( 'observations' )
            {
            // InternalDft.g:1254:1: ( 'observations' )
            // InternalDft.g:1255:2: 'observations'
            {
             before(grammarAccess.getGalileoRepairActionAccess().getObservationsKeyword_4_0()); 
            match(input,33,FOLLOW_2); 
             after(grammarAccess.getGalileoRepairActionAccess().getObservationsKeyword_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group_4__0__Impl"


    // $ANTLR start "rule__GalileoRepairAction__Group_4__1"
    // InternalDft.g:1264:1: rule__GalileoRepairAction__Group_4__1 : rule__GalileoRepairAction__Group_4__1__Impl ;
    public final void rule__GalileoRepairAction__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1268:1: ( rule__GalileoRepairAction__Group_4__1__Impl )
            // InternalDft.g:1269:2: rule__GalileoRepairAction__Group_4__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoRepairAction__Group_4__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group_4__1"


    // $ANTLR start "rule__GalileoRepairAction__Group_4__1__Impl"
    // InternalDft.g:1275:1: rule__GalileoRepairAction__Group_4__1__Impl : ( ( rule__GalileoRepairAction__ObservartionsAssignment_4_1 )* ) ;
    public final void rule__GalileoRepairAction__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1279:1: ( ( ( rule__GalileoRepairAction__ObservartionsAssignment_4_1 )* ) )
            // InternalDft.g:1280:1: ( ( rule__GalileoRepairAction__ObservartionsAssignment_4_1 )* )
            {
            // InternalDft.g:1280:1: ( ( rule__GalileoRepairAction__ObservartionsAssignment_4_1 )* )
            // InternalDft.g:1281:2: ( rule__GalileoRepairAction__ObservartionsAssignment_4_1 )*
            {
             before(grammarAccess.getGalileoRepairActionAccess().getObservartionsAssignment_4_1()); 
            // InternalDft.g:1282:2: ( rule__GalileoRepairAction__ObservartionsAssignment_4_1 )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==RULE_STRING) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalDft.g:1282:3: rule__GalileoRepairAction__ObservartionsAssignment_4_1
            	    {
            	    pushFollow(FOLLOW_5);
            	    rule__GalileoRepairAction__ObservartionsAssignment_4_1();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

             after(grammarAccess.getGalileoRepairActionAccess().getObservartionsAssignment_4_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__Group_4__1__Impl"


    // $ANTLR start "rule__Named__Group__0"
    // InternalDft.g:1291:1: rule__Named__Group__0 : rule__Named__Group__0__Impl rule__Named__Group__1 ;
    public final void rule__Named__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1295:1: ( rule__Named__Group__0__Impl rule__Named__Group__1 )
            // InternalDft.g:1296:2: rule__Named__Group__0__Impl rule__Named__Group__1
            {
            pushFollow(FOLLOW_13);
            rule__Named__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Named__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Named__Group__0"


    // $ANTLR start "rule__Named__Group__0__Impl"
    // InternalDft.g:1303:1: rule__Named__Group__0__Impl : ( () ) ;
    public final void rule__Named__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1307:1: ( ( () ) )
            // InternalDft.g:1308:1: ( () )
            {
            // InternalDft.g:1308:1: ( () )
            // InternalDft.g:1309:2: ()
            {
             before(grammarAccess.getNamedAccess().getNamedAction_0()); 
            // InternalDft.g:1310:2: ()
            // InternalDft.g:1310:3: 
            {
            }

             after(grammarAccess.getNamedAccess().getNamedAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Named__Group__0__Impl"


    // $ANTLR start "rule__Named__Group__1"
    // InternalDft.g:1318:1: rule__Named__Group__1 : rule__Named__Group__1__Impl ;
    public final void rule__Named__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1322:1: ( rule__Named__Group__1__Impl )
            // InternalDft.g:1323:2: rule__Named__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Named__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Named__Group__1"


    // $ANTLR start "rule__Named__Group__1__Impl"
    // InternalDft.g:1329:1: rule__Named__Group__1__Impl : ( ( rule__Named__TypeNameAssignment_1 ) ) ;
    public final void rule__Named__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1333:1: ( ( ( rule__Named__TypeNameAssignment_1 ) ) )
            // InternalDft.g:1334:1: ( ( rule__Named__TypeNameAssignment_1 ) )
            {
            // InternalDft.g:1334:1: ( ( rule__Named__TypeNameAssignment_1 ) )
            // InternalDft.g:1335:2: ( rule__Named__TypeNameAssignment_1 )
            {
             before(grammarAccess.getNamedAccess().getTypeNameAssignment_1()); 
            // InternalDft.g:1336:2: ( rule__Named__TypeNameAssignment_1 )
            // InternalDft.g:1336:3: rule__Named__TypeNameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Named__TypeNameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getNamedAccess().getTypeNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Named__Group__1__Impl"


    // $ANTLR start "rule__Observer__Group__0"
    // InternalDft.g:1345:1: rule__Observer__Group__0 : rule__Observer__Group__0__Impl rule__Observer__Group__1 ;
    public final void rule__Observer__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1349:1: ( rule__Observer__Group__0__Impl rule__Observer__Group__1 )
            // InternalDft.g:1350:2: rule__Observer__Group__0__Impl rule__Observer__Group__1
            {
            pushFollow(FOLLOW_6);
            rule__Observer__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Observer__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__0"


    // $ANTLR start "rule__Observer__Group__0__Impl"
    // InternalDft.g:1357:1: rule__Observer__Group__0__Impl : ( () ) ;
    public final void rule__Observer__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1361:1: ( ( () ) )
            // InternalDft.g:1362:1: ( () )
            {
            // InternalDft.g:1362:1: ( () )
            // InternalDft.g:1363:2: ()
            {
             before(grammarAccess.getObserverAccess().getObserverAction_0()); 
            // InternalDft.g:1364:2: ()
            // InternalDft.g:1364:3: 
            {
            }

             after(grammarAccess.getObserverAccess().getObserverAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__0__Impl"


    // $ANTLR start "rule__Observer__Group__1"
    // InternalDft.g:1372:1: rule__Observer__Group__1 : rule__Observer__Group__1__Impl rule__Observer__Group__2 ;
    public final void rule__Observer__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1376:1: ( rule__Observer__Group__1__Impl rule__Observer__Group__2 )
            // InternalDft.g:1377:2: rule__Observer__Group__1__Impl rule__Observer__Group__2
            {
            pushFollow(FOLLOW_14);
            rule__Observer__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Observer__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__1"


    // $ANTLR start "rule__Observer__Group__1__Impl"
    // InternalDft.g:1384:1: rule__Observer__Group__1__Impl : ( 'observer' ) ;
    public final void rule__Observer__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1388:1: ( ( 'observer' ) )
            // InternalDft.g:1389:1: ( 'observer' )
            {
            // InternalDft.g:1389:1: ( 'observer' )
            // InternalDft.g:1390:2: 'observer'
            {
             before(grammarAccess.getObserverAccess().getObserverKeyword_1()); 
            match(input,34,FOLLOW_2); 
             after(grammarAccess.getObserverAccess().getObserverKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__1__Impl"


    // $ANTLR start "rule__Observer__Group__2"
    // InternalDft.g:1399:1: rule__Observer__Group__2 : rule__Observer__Group__2__Impl rule__Observer__Group__3 ;
    public final void rule__Observer__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1403:1: ( rule__Observer__Group__2__Impl rule__Observer__Group__3 )
            // InternalDft.g:1404:2: rule__Observer__Group__2__Impl rule__Observer__Group__3
            {
            pushFollow(FOLLOW_14);
            rule__Observer__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Observer__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__2"


    // $ANTLR start "rule__Observer__Group__2__Impl"
    // InternalDft.g:1411:1: rule__Observer__Group__2__Impl : ( ( rule__Observer__ObservablesAssignment_2 )* ) ;
    public final void rule__Observer__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1415:1: ( ( ( rule__Observer__ObservablesAssignment_2 )* ) )
            // InternalDft.g:1416:1: ( ( rule__Observer__ObservablesAssignment_2 )* )
            {
            // InternalDft.g:1416:1: ( ( rule__Observer__ObservablesAssignment_2 )* )
            // InternalDft.g:1417:2: ( rule__Observer__ObservablesAssignment_2 )*
            {
             before(grammarAccess.getObserverAccess().getObservablesAssignment_2()); 
            // InternalDft.g:1418:2: ( rule__Observer__ObservablesAssignment_2 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==RULE_STRING) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalDft.g:1418:3: rule__Observer__ObservablesAssignment_2
            	    {
            	    pushFollow(FOLLOW_5);
            	    rule__Observer__ObservablesAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

             after(grammarAccess.getObserverAccess().getObservablesAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__2__Impl"


    // $ANTLR start "rule__Observer__Group__3"
    // InternalDft.g:1426:1: rule__Observer__Group__3 : rule__Observer__Group__3__Impl rule__Observer__Group__4 ;
    public final void rule__Observer__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1430:1: ( rule__Observer__Group__3__Impl rule__Observer__Group__4 )
            // InternalDft.g:1431:2: rule__Observer__Group__3__Impl rule__Observer__Group__4
            {
            pushFollow(FOLLOW_10);
            rule__Observer__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Observer__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__3"


    // $ANTLR start "rule__Observer__Group__3__Impl"
    // InternalDft.g:1438:1: rule__Observer__Group__3__Impl : ( 'obsRate' ) ;
    public final void rule__Observer__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1442:1: ( ( 'obsRate' ) )
            // InternalDft.g:1443:1: ( 'obsRate' )
            {
            // InternalDft.g:1443:1: ( 'obsRate' )
            // InternalDft.g:1444:2: 'obsRate'
            {
             before(grammarAccess.getObserverAccess().getObsRateKeyword_3()); 
            match(input,35,FOLLOW_2); 
             after(grammarAccess.getObserverAccess().getObsRateKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__3__Impl"


    // $ANTLR start "rule__Observer__Group__4"
    // InternalDft.g:1453:1: rule__Observer__Group__4 : rule__Observer__Group__4__Impl rule__Observer__Group__5 ;
    public final void rule__Observer__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1457:1: ( rule__Observer__Group__4__Impl rule__Observer__Group__5 )
            // InternalDft.g:1458:2: rule__Observer__Group__4__Impl rule__Observer__Group__5
            {
            pushFollow(FOLLOW_11);
            rule__Observer__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Observer__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__4"


    // $ANTLR start "rule__Observer__Group__4__Impl"
    // InternalDft.g:1465:1: rule__Observer__Group__4__Impl : ( '=' ) ;
    public final void rule__Observer__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1469:1: ( ( '=' ) )
            // InternalDft.g:1470:1: ( '=' )
            {
            // InternalDft.g:1470:1: ( '=' )
            // InternalDft.g:1471:2: '='
            {
             before(grammarAccess.getObserverAccess().getEqualsSignKeyword_4()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getObserverAccess().getEqualsSignKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__4__Impl"


    // $ANTLR start "rule__Observer__Group__5"
    // InternalDft.g:1480:1: rule__Observer__Group__5 : rule__Observer__Group__5__Impl ;
    public final void rule__Observer__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1484:1: ( rule__Observer__Group__5__Impl )
            // InternalDft.g:1485:2: rule__Observer__Group__5__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Observer__Group__5__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__5"


    // $ANTLR start "rule__Observer__Group__5__Impl"
    // InternalDft.g:1491:1: rule__Observer__Group__5__Impl : ( ( rule__Observer__ObservationRateAssignment_5 ) ) ;
    public final void rule__Observer__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1495:1: ( ( ( rule__Observer__ObservationRateAssignment_5 ) ) )
            // InternalDft.g:1496:1: ( ( rule__Observer__ObservationRateAssignment_5 ) )
            {
            // InternalDft.g:1496:1: ( ( rule__Observer__ObservationRateAssignment_5 ) )
            // InternalDft.g:1497:2: ( rule__Observer__ObservationRateAssignment_5 )
            {
             before(grammarAccess.getObserverAccess().getObservationRateAssignment_5()); 
            // InternalDft.g:1498:2: ( rule__Observer__ObservationRateAssignment_5 )
            // InternalDft.g:1498:3: rule__Observer__ObservationRateAssignment_5
            {
            pushFollow(FOLLOW_2);
            rule__Observer__ObservationRateAssignment_5();

            state._fsp--;


            }

             after(grammarAccess.getObserverAccess().getObservationRateAssignment_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__Group__5__Impl"


    // $ANTLR start "rule__Parametrized__Group__0"
    // InternalDft.g:1507:1: rule__Parametrized__Group__0 : rule__Parametrized__Group__0__Impl rule__Parametrized__Group__1 ;
    public final void rule__Parametrized__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1511:1: ( rule__Parametrized__Group__0__Impl rule__Parametrized__Group__1 )
            // InternalDft.g:1512:2: rule__Parametrized__Group__0__Impl rule__Parametrized__Group__1
            {
            pushFollow(FOLLOW_15);
            rule__Parametrized__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Parametrized__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__Group__0"


    // $ANTLR start "rule__Parametrized__Group__0__Impl"
    // InternalDft.g:1519:1: rule__Parametrized__Group__0__Impl : ( () ) ;
    public final void rule__Parametrized__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1523:1: ( ( () ) )
            // InternalDft.g:1524:1: ( () )
            {
            // InternalDft.g:1524:1: ( () )
            // InternalDft.g:1525:2: ()
            {
             before(grammarAccess.getParametrizedAccess().getParametrizedAction_0()); 
            // InternalDft.g:1526:2: ()
            // InternalDft.g:1526:3: 
            {
            }

             after(grammarAccess.getParametrizedAccess().getParametrizedAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__Group__0__Impl"


    // $ANTLR start "rule__Parametrized__Group__1"
    // InternalDft.g:1534:1: rule__Parametrized__Group__1 : rule__Parametrized__Group__1__Impl rule__Parametrized__Group__2 ;
    public final void rule__Parametrized__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1538:1: ( rule__Parametrized__Group__1__Impl rule__Parametrized__Group__2 )
            // InternalDft.g:1539:2: rule__Parametrized__Group__1__Impl rule__Parametrized__Group__2
            {
            pushFollow(FOLLOW_10);
            rule__Parametrized__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Parametrized__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__Group__1"


    // $ANTLR start "rule__Parametrized__Group__1__Impl"
    // InternalDft.g:1546:1: rule__Parametrized__Group__1__Impl : ( ( rule__Parametrized__TypeNameAssignment_1 ) ) ;
    public final void rule__Parametrized__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1550:1: ( ( ( rule__Parametrized__TypeNameAssignment_1 ) ) )
            // InternalDft.g:1551:1: ( ( rule__Parametrized__TypeNameAssignment_1 ) )
            {
            // InternalDft.g:1551:1: ( ( rule__Parametrized__TypeNameAssignment_1 ) )
            // InternalDft.g:1552:2: ( rule__Parametrized__TypeNameAssignment_1 )
            {
             before(grammarAccess.getParametrizedAccess().getTypeNameAssignment_1()); 
            // InternalDft.g:1553:2: ( rule__Parametrized__TypeNameAssignment_1 )
            // InternalDft.g:1553:3: rule__Parametrized__TypeNameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Parametrized__TypeNameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getParametrizedAccess().getTypeNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__Group__1__Impl"


    // $ANTLR start "rule__Parametrized__Group__2"
    // InternalDft.g:1561:1: rule__Parametrized__Group__2 : rule__Parametrized__Group__2__Impl rule__Parametrized__Group__3 ;
    public final void rule__Parametrized__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1565:1: ( rule__Parametrized__Group__2__Impl rule__Parametrized__Group__3 )
            // InternalDft.g:1566:2: rule__Parametrized__Group__2__Impl rule__Parametrized__Group__3
            {
            pushFollow(FOLLOW_11);
            rule__Parametrized__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Parametrized__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__Group__2"


    // $ANTLR start "rule__Parametrized__Group__2__Impl"
    // InternalDft.g:1573:1: rule__Parametrized__Group__2__Impl : ( '=' ) ;
    public final void rule__Parametrized__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1577:1: ( ( '=' ) )
            // InternalDft.g:1578:1: ( '=' )
            {
            // InternalDft.g:1578:1: ( '=' )
            // InternalDft.g:1579:2: '='
            {
             before(grammarAccess.getParametrizedAccess().getEqualsSignKeyword_2()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getParametrizedAccess().getEqualsSignKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__Group__2__Impl"


    // $ANTLR start "rule__Parametrized__Group__3"
    // InternalDft.g:1588:1: rule__Parametrized__Group__3 : rule__Parametrized__Group__3__Impl ;
    public final void rule__Parametrized__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1592:1: ( rule__Parametrized__Group__3__Impl )
            // InternalDft.g:1593:2: rule__Parametrized__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Parametrized__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__Group__3"


    // $ANTLR start "rule__Parametrized__Group__3__Impl"
    // InternalDft.g:1599:1: rule__Parametrized__Group__3__Impl : ( ( rule__Parametrized__ParameterAssignment_3 ) ) ;
    public final void rule__Parametrized__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1603:1: ( ( ( rule__Parametrized__ParameterAssignment_3 ) ) )
            // InternalDft.g:1604:1: ( ( rule__Parametrized__ParameterAssignment_3 ) )
            {
            // InternalDft.g:1604:1: ( ( rule__Parametrized__ParameterAssignment_3 ) )
            // InternalDft.g:1605:2: ( rule__Parametrized__ParameterAssignment_3 )
            {
             before(grammarAccess.getParametrizedAccess().getParameterAssignment_3()); 
            // InternalDft.g:1606:2: ( rule__Parametrized__ParameterAssignment_3 )
            // InternalDft.g:1606:3: rule__Parametrized__ParameterAssignment_3
            {
            pushFollow(FOLLOW_2);
            rule__Parametrized__ParameterAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getParametrizedAccess().getParameterAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__Group__3__Impl"


    // $ANTLR start "rule__Float__Group__0"
    // InternalDft.g:1615:1: rule__Float__Group__0 : rule__Float__Group__0__Impl rule__Float__Group__1 ;
    public final void rule__Float__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1619:1: ( rule__Float__Group__0__Impl rule__Float__Group__1 )
            // InternalDft.g:1620:2: rule__Float__Group__0__Impl rule__Float__Group__1
            {
            pushFollow(FOLLOW_11);
            rule__Float__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Float__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group__0"


    // $ANTLR start "rule__Float__Group__0__Impl"
    // InternalDft.g:1627:1: rule__Float__Group__0__Impl : ( ( '-' )? ) ;
    public final void rule__Float__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1631:1: ( ( ( '-' )? ) )
            // InternalDft.g:1632:1: ( ( '-' )? )
            {
            // InternalDft.g:1632:1: ( ( '-' )? )
            // InternalDft.g:1633:2: ( '-' )?
            {
             before(grammarAccess.getFloatAccess().getHyphenMinusKeyword_0()); 
            // InternalDft.g:1634:2: ( '-' )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==36) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // InternalDft.g:1634:3: '-'
                    {
                    match(input,36,FOLLOW_2); 

                    }
                    break;

            }

             after(grammarAccess.getFloatAccess().getHyphenMinusKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group__0__Impl"


    // $ANTLR start "rule__Float__Group__1"
    // InternalDft.g:1642:1: rule__Float__Group__1 : rule__Float__Group__1__Impl rule__Float__Group__2 ;
    public final void rule__Float__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1646:1: ( rule__Float__Group__1__Impl rule__Float__Group__2 )
            // InternalDft.g:1647:2: rule__Float__Group__1__Impl rule__Float__Group__2
            {
            pushFollow(FOLLOW_16);
            rule__Float__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Float__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group__1"


    // $ANTLR start "rule__Float__Group__1__Impl"
    // InternalDft.g:1654:1: rule__Float__Group__1__Impl : ( RULE_INT ) ;
    public final void rule__Float__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1658:1: ( ( RULE_INT ) )
            // InternalDft.g:1659:1: ( RULE_INT )
            {
            // InternalDft.g:1659:1: ( RULE_INT )
            // InternalDft.g:1660:2: RULE_INT
            {
             before(grammarAccess.getFloatAccess().getINTTerminalRuleCall_1()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getFloatAccess().getINTTerminalRuleCall_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group__1__Impl"


    // $ANTLR start "rule__Float__Group__2"
    // InternalDft.g:1669:1: rule__Float__Group__2 : rule__Float__Group__2__Impl rule__Float__Group__3 ;
    public final void rule__Float__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1673:1: ( rule__Float__Group__2__Impl rule__Float__Group__3 )
            // InternalDft.g:1674:2: rule__Float__Group__2__Impl rule__Float__Group__3
            {
            pushFollow(FOLLOW_16);
            rule__Float__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Float__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group__2"


    // $ANTLR start "rule__Float__Group__2__Impl"
    // InternalDft.g:1681:1: rule__Float__Group__2__Impl : ( ( rule__Float__Group_2__0 )? ) ;
    public final void rule__Float__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1685:1: ( ( ( rule__Float__Group_2__0 )? ) )
            // InternalDft.g:1686:1: ( ( rule__Float__Group_2__0 )? )
            {
            // InternalDft.g:1686:1: ( ( rule__Float__Group_2__0 )? )
            // InternalDft.g:1687:2: ( rule__Float__Group_2__0 )?
            {
             before(grammarAccess.getFloatAccess().getGroup_2()); 
            // InternalDft.g:1688:2: ( rule__Float__Group_2__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==37) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalDft.g:1688:3: rule__Float__Group_2__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Float__Group_2__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getFloatAccess().getGroup_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group__2__Impl"


    // $ANTLR start "rule__Float__Group__3"
    // InternalDft.g:1696:1: rule__Float__Group__3 : rule__Float__Group__3__Impl ;
    public final void rule__Float__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1700:1: ( rule__Float__Group__3__Impl )
            // InternalDft.g:1701:2: rule__Float__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Float__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group__3"


    // $ANTLR start "rule__Float__Group__3__Impl"
    // InternalDft.g:1707:1: rule__Float__Group__3__Impl : ( ( rule__Float__Group_3__0 )? ) ;
    public final void rule__Float__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1711:1: ( ( ( rule__Float__Group_3__0 )? ) )
            // InternalDft.g:1712:1: ( ( rule__Float__Group_3__0 )? )
            {
            // InternalDft.g:1712:1: ( ( rule__Float__Group_3__0 )? )
            // InternalDft.g:1713:2: ( rule__Float__Group_3__0 )?
            {
             before(grammarAccess.getFloatAccess().getGroup_3()); 
            // InternalDft.g:1714:2: ( rule__Float__Group_3__0 )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==38) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // InternalDft.g:1714:3: rule__Float__Group_3__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Float__Group_3__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getFloatAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group__3__Impl"


    // $ANTLR start "rule__Float__Group_2__0"
    // InternalDft.g:1723:1: rule__Float__Group_2__0 : rule__Float__Group_2__0__Impl rule__Float__Group_2__1 ;
    public final void rule__Float__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1727:1: ( rule__Float__Group_2__0__Impl rule__Float__Group_2__1 )
            // InternalDft.g:1728:2: rule__Float__Group_2__0__Impl rule__Float__Group_2__1
            {
            pushFollow(FOLLOW_17);
            rule__Float__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Float__Group_2__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_2__0"


    // $ANTLR start "rule__Float__Group_2__0__Impl"
    // InternalDft.g:1735:1: rule__Float__Group_2__0__Impl : ( '.' ) ;
    public final void rule__Float__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1739:1: ( ( '.' ) )
            // InternalDft.g:1740:1: ( '.' )
            {
            // InternalDft.g:1740:1: ( '.' )
            // InternalDft.g:1741:2: '.'
            {
             before(grammarAccess.getFloatAccess().getFullStopKeyword_2_0()); 
            match(input,37,FOLLOW_2); 
             after(grammarAccess.getFloatAccess().getFullStopKeyword_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_2__0__Impl"


    // $ANTLR start "rule__Float__Group_2__1"
    // InternalDft.g:1750:1: rule__Float__Group_2__1 : rule__Float__Group_2__1__Impl ;
    public final void rule__Float__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1754:1: ( rule__Float__Group_2__1__Impl )
            // InternalDft.g:1755:2: rule__Float__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Float__Group_2__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_2__1"


    // $ANTLR start "rule__Float__Group_2__1__Impl"
    // InternalDft.g:1761:1: rule__Float__Group_2__1__Impl : ( RULE_INT ) ;
    public final void rule__Float__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1765:1: ( ( RULE_INT ) )
            // InternalDft.g:1766:1: ( RULE_INT )
            {
            // InternalDft.g:1766:1: ( RULE_INT )
            // InternalDft.g:1767:2: RULE_INT
            {
             before(grammarAccess.getFloatAccess().getINTTerminalRuleCall_2_1()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getFloatAccess().getINTTerminalRuleCall_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_2__1__Impl"


    // $ANTLR start "rule__Float__Group_3__0"
    // InternalDft.g:1777:1: rule__Float__Group_3__0 : rule__Float__Group_3__0__Impl rule__Float__Group_3__1 ;
    public final void rule__Float__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1781:1: ( rule__Float__Group_3__0__Impl rule__Float__Group_3__1 )
            // InternalDft.g:1782:2: rule__Float__Group_3__0__Impl rule__Float__Group_3__1
            {
            pushFollow(FOLLOW_11);
            rule__Float__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Float__Group_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_3__0"


    // $ANTLR start "rule__Float__Group_3__0__Impl"
    // InternalDft.g:1789:1: rule__Float__Group_3__0__Impl : ( 'e' ) ;
    public final void rule__Float__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1793:1: ( ( 'e' ) )
            // InternalDft.g:1794:1: ( 'e' )
            {
            // InternalDft.g:1794:1: ( 'e' )
            // InternalDft.g:1795:2: 'e'
            {
             before(grammarAccess.getFloatAccess().getEKeyword_3_0()); 
            match(input,38,FOLLOW_2); 
             after(grammarAccess.getFloatAccess().getEKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_3__0__Impl"


    // $ANTLR start "rule__Float__Group_3__1"
    // InternalDft.g:1804:1: rule__Float__Group_3__1 : rule__Float__Group_3__1__Impl rule__Float__Group_3__2 ;
    public final void rule__Float__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1808:1: ( rule__Float__Group_3__1__Impl rule__Float__Group_3__2 )
            // InternalDft.g:1809:2: rule__Float__Group_3__1__Impl rule__Float__Group_3__2
            {
            pushFollow(FOLLOW_11);
            rule__Float__Group_3__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Float__Group_3__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_3__1"


    // $ANTLR start "rule__Float__Group_3__1__Impl"
    // InternalDft.g:1816:1: rule__Float__Group_3__1__Impl : ( ( '-' )? ) ;
    public final void rule__Float__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1820:1: ( ( ( '-' )? ) )
            // InternalDft.g:1821:1: ( ( '-' )? )
            {
            // InternalDft.g:1821:1: ( ( '-' )? )
            // InternalDft.g:1822:2: ( '-' )?
            {
             before(grammarAccess.getFloatAccess().getHyphenMinusKeyword_3_1()); 
            // InternalDft.g:1823:2: ( '-' )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==36) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // InternalDft.g:1823:3: '-'
                    {
                    match(input,36,FOLLOW_2); 

                    }
                    break;

            }

             after(grammarAccess.getFloatAccess().getHyphenMinusKeyword_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_3__1__Impl"


    // $ANTLR start "rule__Float__Group_3__2"
    // InternalDft.g:1831:1: rule__Float__Group_3__2 : rule__Float__Group_3__2__Impl ;
    public final void rule__Float__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1835:1: ( rule__Float__Group_3__2__Impl )
            // InternalDft.g:1836:2: rule__Float__Group_3__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Float__Group_3__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_3__2"


    // $ANTLR start "rule__Float__Group_3__2__Impl"
    // InternalDft.g:1842:1: rule__Float__Group_3__2__Impl : ( RULE_INT ) ;
    public final void rule__Float__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1846:1: ( ( RULE_INT ) )
            // InternalDft.g:1847:1: ( RULE_INT )
            {
            // InternalDft.g:1847:1: ( RULE_INT )
            // InternalDft.g:1848:2: RULE_INT
            {
             before(grammarAccess.getFloatAccess().getINTTerminalRuleCall_3_2()); 
            match(input,RULE_INT,FOLLOW_2); 
             after(grammarAccess.getFloatAccess().getINTTerminalRuleCall_3_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Float__Group_3__2__Impl"


    // $ANTLR start "rule__GalileoDft__RootAssignment_1"
    // InternalDft.g:1858:1: rule__GalileoDft__RootAssignment_1 : ( ( RULE_STRING ) ) ;
    public final void rule__GalileoDft__RootAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1862:1: ( ( ( RULE_STRING ) ) )
            // InternalDft.g:1863:2: ( ( RULE_STRING ) )
            {
            // InternalDft.g:1863:2: ( ( RULE_STRING ) )
            // InternalDft.g:1864:3: ( RULE_STRING )
            {
             before(grammarAccess.getGalileoDftAccess().getRootGalileoFaultTreeNodeCrossReference_1_0()); 
            // InternalDft.g:1865:3: ( RULE_STRING )
            // InternalDft.g:1866:4: RULE_STRING
            {
             before(grammarAccess.getGalileoDftAccess().getRootGalileoFaultTreeNodeSTRINGTerminalRuleCall_1_0_1()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getGalileoDftAccess().getRootGalileoFaultTreeNodeSTRINGTerminalRuleCall_1_0_1()); 

            }

             after(grammarAccess.getGalileoDftAccess().getRootGalileoFaultTreeNodeCrossReference_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__RootAssignment_1"


    // $ANTLR start "rule__GalileoDft__GatesAssignment_3_0_0"
    // InternalDft.g:1877:1: rule__GalileoDft__GatesAssignment_3_0_0 : ( ruleGalileoGate ) ;
    public final void rule__GalileoDft__GatesAssignment_3_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1881:1: ( ( ruleGalileoGate ) )
            // InternalDft.g:1882:2: ( ruleGalileoGate )
            {
            // InternalDft.g:1882:2: ( ruleGalileoGate )
            // InternalDft.g:1883:3: ruleGalileoGate
            {
             before(grammarAccess.getGalileoDftAccess().getGatesGalileoGateParserRuleCall_3_0_0_0()); 
            pushFollow(FOLLOW_2);
            ruleGalileoGate();

            state._fsp--;

             after(grammarAccess.getGalileoDftAccess().getGatesGalileoGateParserRuleCall_3_0_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__GatesAssignment_3_0_0"


    // $ANTLR start "rule__GalileoDft__BasicEventsAssignment_3_1_0"
    // InternalDft.g:1892:1: rule__GalileoDft__BasicEventsAssignment_3_1_0 : ( ruleGalileoBasicEvent ) ;
    public final void rule__GalileoDft__BasicEventsAssignment_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1896:1: ( ( ruleGalileoBasicEvent ) )
            // InternalDft.g:1897:2: ( ruleGalileoBasicEvent )
            {
            // InternalDft.g:1897:2: ( ruleGalileoBasicEvent )
            // InternalDft.g:1898:3: ruleGalileoBasicEvent
            {
             before(grammarAccess.getGalileoDftAccess().getBasicEventsGalileoBasicEventParserRuleCall_3_1_0_0()); 
            pushFollow(FOLLOW_2);
            ruleGalileoBasicEvent();

            state._fsp--;

             after(grammarAccess.getGalileoDftAccess().getBasicEventsGalileoBasicEventParserRuleCall_3_1_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoDft__BasicEventsAssignment_3_1_0"


    // $ANTLR start "rule__GalileoGate__NameAssignment_0"
    // InternalDft.g:1907:1: rule__GalileoGate__NameAssignment_0 : ( RULE_STRING ) ;
    public final void rule__GalileoGate__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1911:1: ( ( RULE_STRING ) )
            // InternalDft.g:1912:2: ( RULE_STRING )
            {
            // InternalDft.g:1912:2: ( RULE_STRING )
            // InternalDft.g:1913:3: RULE_STRING
            {
             before(grammarAccess.getGalileoGateAccess().getNameSTRINGTerminalRuleCall_0_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getGalileoGateAccess().getNameSTRINGTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__NameAssignment_0"


    // $ANTLR start "rule__GalileoGate__TypeAssignment_1"
    // InternalDft.g:1922:1: rule__GalileoGate__TypeAssignment_1 : ( ruleGalileoNodeType ) ;
    public final void rule__GalileoGate__TypeAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1926:1: ( ( ruleGalileoNodeType ) )
            // InternalDft.g:1927:2: ( ruleGalileoNodeType )
            {
            // InternalDft.g:1927:2: ( ruleGalileoNodeType )
            // InternalDft.g:1928:3: ruleGalileoNodeType
            {
             before(grammarAccess.getGalileoGateAccess().getTypeGalileoNodeTypeParserRuleCall_1_0()); 
            pushFollow(FOLLOW_2);
            ruleGalileoNodeType();

            state._fsp--;

             after(grammarAccess.getGalileoGateAccess().getTypeGalileoNodeTypeParserRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__TypeAssignment_1"


    // $ANTLR start "rule__GalileoGate__ChildrenAssignment_2"
    // InternalDft.g:1937:1: rule__GalileoGate__ChildrenAssignment_2 : ( ( RULE_STRING ) ) ;
    public final void rule__GalileoGate__ChildrenAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1941:1: ( ( ( RULE_STRING ) ) )
            // InternalDft.g:1942:2: ( ( RULE_STRING ) )
            {
            // InternalDft.g:1942:2: ( ( RULE_STRING ) )
            // InternalDft.g:1943:3: ( RULE_STRING )
            {
             before(grammarAccess.getGalileoGateAccess().getChildrenGalileoFaultTreeNodeCrossReference_2_0()); 
            // InternalDft.g:1944:3: ( RULE_STRING )
            // InternalDft.g:1945:4: RULE_STRING
            {
             before(grammarAccess.getGalileoGateAccess().getChildrenGalileoFaultTreeNodeSTRINGTerminalRuleCall_2_0_1()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getGalileoGateAccess().getChildrenGalileoFaultTreeNodeSTRINGTerminalRuleCall_2_0_1()); 

            }

             after(grammarAccess.getGalileoGateAccess().getChildrenGalileoFaultTreeNodeCrossReference_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoGate__ChildrenAssignment_2"


    // $ANTLR start "rule__GalileoBasicEvent__NameAssignment_0"
    // InternalDft.g:1956:1: rule__GalileoBasicEvent__NameAssignment_0 : ( RULE_STRING ) ;
    public final void rule__GalileoBasicEvent__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1960:1: ( ( RULE_STRING ) )
            // InternalDft.g:1961:2: ( RULE_STRING )
            {
            // InternalDft.g:1961:2: ( RULE_STRING )
            // InternalDft.g:1962:3: RULE_STRING
            {
             before(grammarAccess.getGalileoBasicEventAccess().getNameSTRINGTerminalRuleCall_0_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getNameSTRINGTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__NameAssignment_0"


    // $ANTLR start "rule__GalileoBasicEvent__LambdaAssignment_1_0_2"
    // InternalDft.g:1971:1: rule__GalileoBasicEvent__LambdaAssignment_1_0_2 : ( ruleFloat ) ;
    public final void rule__GalileoBasicEvent__LambdaAssignment_1_0_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1975:1: ( ( ruleFloat ) )
            // InternalDft.g:1976:2: ( ruleFloat )
            {
            // InternalDft.g:1976:2: ( ruleFloat )
            // InternalDft.g:1977:3: ruleFloat
            {
             before(grammarAccess.getGalileoBasicEventAccess().getLambdaFloatParserRuleCall_1_0_2_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getGalileoBasicEventAccess().getLambdaFloatParserRuleCall_1_0_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__LambdaAssignment_1_0_2"


    // $ANTLR start "rule__GalileoBasicEvent__ProbAssignment_1_1_2"
    // InternalDft.g:1986:1: rule__GalileoBasicEvent__ProbAssignment_1_1_2 : ( ruleFloat ) ;
    public final void rule__GalileoBasicEvent__ProbAssignment_1_1_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1990:1: ( ( ruleFloat ) )
            // InternalDft.g:1991:2: ( ruleFloat )
            {
            // InternalDft.g:1991:2: ( ruleFloat )
            // InternalDft.g:1992:3: ruleFloat
            {
             before(grammarAccess.getGalileoBasicEventAccess().getProbFloatParserRuleCall_1_1_2_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getGalileoBasicEventAccess().getProbFloatParserRuleCall_1_1_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__ProbAssignment_1_1_2"


    // $ANTLR start "rule__GalileoBasicEvent__DormAssignment_2_2"
    // InternalDft.g:2001:1: rule__GalileoBasicEvent__DormAssignment_2_2 : ( ruleFloat ) ;
    public final void rule__GalileoBasicEvent__DormAssignment_2_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2005:1: ( ( ruleFloat ) )
            // InternalDft.g:2006:2: ( ruleFloat )
            {
            // InternalDft.g:2006:2: ( ruleFloat )
            // InternalDft.g:2007:3: ruleFloat
            {
             before(grammarAccess.getGalileoBasicEventAccess().getDormFloatParserRuleCall_2_2_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getGalileoBasicEventAccess().getDormFloatParserRuleCall_2_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__DormAssignment_2_2"


    // $ANTLR start "rule__GalileoBasicEvent__RepairActionsAssignment_3"
    // InternalDft.g:2016:1: rule__GalileoBasicEvent__RepairActionsAssignment_3 : ( ruleGalileoRepairAction ) ;
    public final void rule__GalileoBasicEvent__RepairActionsAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2020:1: ( ( ruleGalileoRepairAction ) )
            // InternalDft.g:2021:2: ( ruleGalileoRepairAction )
            {
            // InternalDft.g:2021:2: ( ruleGalileoRepairAction )
            // InternalDft.g:2022:3: ruleGalileoRepairAction
            {
             before(grammarAccess.getGalileoBasicEventAccess().getRepairActionsGalileoRepairActionParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
            ruleGalileoRepairAction();

            state._fsp--;

             after(grammarAccess.getGalileoBasicEventAccess().getRepairActionsGalileoRepairActionParserRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoBasicEvent__RepairActionsAssignment_3"


    // $ANTLR start "rule__GalileoRepairAction__RepairAssignment_2"
    // InternalDft.g:2031:1: rule__GalileoRepairAction__RepairAssignment_2 : ( ruleFloat ) ;
    public final void rule__GalileoRepairAction__RepairAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2035:1: ( ( ruleFloat ) )
            // InternalDft.g:2036:2: ( ruleFloat )
            {
            // InternalDft.g:2036:2: ( ruleFloat )
            // InternalDft.g:2037:3: ruleFloat
            {
             before(grammarAccess.getGalileoRepairActionAccess().getRepairFloatParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getGalileoRepairActionAccess().getRepairFloatParserRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__RepairAssignment_2"


    // $ANTLR start "rule__GalileoRepairAction__NameAssignment_3"
    // InternalDft.g:2046:1: rule__GalileoRepairAction__NameAssignment_3 : ( RULE_STRING ) ;
    public final void rule__GalileoRepairAction__NameAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2050:1: ( ( RULE_STRING ) )
            // InternalDft.g:2051:2: ( RULE_STRING )
            {
            // InternalDft.g:2051:2: ( RULE_STRING )
            // InternalDft.g:2052:3: RULE_STRING
            {
             before(grammarAccess.getGalileoRepairActionAccess().getNameSTRINGTerminalRuleCall_3_0()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getGalileoRepairActionAccess().getNameSTRINGTerminalRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__NameAssignment_3"


    // $ANTLR start "rule__GalileoRepairAction__ObservartionsAssignment_4_1"
    // InternalDft.g:2061:1: rule__GalileoRepairAction__ObservartionsAssignment_4_1 : ( ( RULE_STRING ) ) ;
    public final void rule__GalileoRepairAction__ObservartionsAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2065:1: ( ( ( RULE_STRING ) ) )
            // InternalDft.g:2066:2: ( ( RULE_STRING ) )
            {
            // InternalDft.g:2066:2: ( ( RULE_STRING ) )
            // InternalDft.g:2067:3: ( RULE_STRING )
            {
             before(grammarAccess.getGalileoRepairActionAccess().getObservartionsGalileoFaultTreeNodeCrossReference_4_1_0()); 
            // InternalDft.g:2068:3: ( RULE_STRING )
            // InternalDft.g:2069:4: RULE_STRING
            {
             before(grammarAccess.getGalileoRepairActionAccess().getObservartionsGalileoFaultTreeNodeSTRINGTerminalRuleCall_4_1_0_1()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getGalileoRepairActionAccess().getObservartionsGalileoFaultTreeNodeSTRINGTerminalRuleCall_4_1_0_1()); 

            }

             after(grammarAccess.getGalileoRepairActionAccess().getObservartionsGalileoFaultTreeNodeCrossReference_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__GalileoRepairAction__ObservartionsAssignment_4_1"


    // $ANTLR start "rule__Named__TypeNameAssignment_1"
    // InternalDft.g:2080:1: rule__Named__TypeNameAssignment_1 : ( ( rule__Named__TypeNameAlternatives_1_0 ) ) ;
    public final void rule__Named__TypeNameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2084:1: ( ( ( rule__Named__TypeNameAlternatives_1_0 ) ) )
            // InternalDft.g:2085:2: ( ( rule__Named__TypeNameAlternatives_1_0 ) )
            {
            // InternalDft.g:2085:2: ( ( rule__Named__TypeNameAlternatives_1_0 ) )
            // InternalDft.g:2086:3: ( rule__Named__TypeNameAlternatives_1_0 )
            {
             before(grammarAccess.getNamedAccess().getTypeNameAlternatives_1_0()); 
            // InternalDft.g:2087:3: ( rule__Named__TypeNameAlternatives_1_0 )
            // InternalDft.g:2087:4: rule__Named__TypeNameAlternatives_1_0
            {
            pushFollow(FOLLOW_2);
            rule__Named__TypeNameAlternatives_1_0();

            state._fsp--;


            }

             after(grammarAccess.getNamedAccess().getTypeNameAlternatives_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Named__TypeNameAssignment_1"


    // $ANTLR start "rule__Observer__ObservablesAssignment_2"
    // InternalDft.g:2095:1: rule__Observer__ObservablesAssignment_2 : ( ( RULE_STRING ) ) ;
    public final void rule__Observer__ObservablesAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2099:1: ( ( ( RULE_STRING ) ) )
            // InternalDft.g:2100:2: ( ( RULE_STRING ) )
            {
            // InternalDft.g:2100:2: ( ( RULE_STRING ) )
            // InternalDft.g:2101:3: ( RULE_STRING )
            {
             before(grammarAccess.getObserverAccess().getObservablesGalileoFaultTreeNodeCrossReference_2_0()); 
            // InternalDft.g:2102:3: ( RULE_STRING )
            // InternalDft.g:2103:4: RULE_STRING
            {
             before(grammarAccess.getObserverAccess().getObservablesGalileoFaultTreeNodeSTRINGTerminalRuleCall_2_0_1()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getObserverAccess().getObservablesGalileoFaultTreeNodeSTRINGTerminalRuleCall_2_0_1()); 

            }

             after(grammarAccess.getObserverAccess().getObservablesGalileoFaultTreeNodeCrossReference_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__ObservablesAssignment_2"


    // $ANTLR start "rule__Observer__ObservationRateAssignment_5"
    // InternalDft.g:2114:1: rule__Observer__ObservationRateAssignment_5 : ( ruleFloat ) ;
    public final void rule__Observer__ObservationRateAssignment_5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2118:1: ( ( ruleFloat ) )
            // InternalDft.g:2119:2: ( ruleFloat )
            {
            // InternalDft.g:2119:2: ( ruleFloat )
            // InternalDft.g:2120:3: ruleFloat
            {
             before(grammarAccess.getObserverAccess().getObservationRateFloatParserRuleCall_5_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getObserverAccess().getObservationRateFloatParserRuleCall_5_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Observer__ObservationRateAssignment_5"


    // $ANTLR start "rule__Parametrized__TypeNameAssignment_1"
    // InternalDft.g:2129:1: rule__Parametrized__TypeNameAssignment_1 : ( ( rule__Parametrized__TypeNameAlternatives_1_0 ) ) ;
    public final void rule__Parametrized__TypeNameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2133:1: ( ( ( rule__Parametrized__TypeNameAlternatives_1_0 ) ) )
            // InternalDft.g:2134:2: ( ( rule__Parametrized__TypeNameAlternatives_1_0 ) )
            {
            // InternalDft.g:2134:2: ( ( rule__Parametrized__TypeNameAlternatives_1_0 ) )
            // InternalDft.g:2135:3: ( rule__Parametrized__TypeNameAlternatives_1_0 )
            {
             before(grammarAccess.getParametrizedAccess().getTypeNameAlternatives_1_0()); 
            // InternalDft.g:2136:3: ( rule__Parametrized__TypeNameAlternatives_1_0 )
            // InternalDft.g:2136:4: rule__Parametrized__TypeNameAlternatives_1_0
            {
            pushFollow(FOLLOW_2);
            rule__Parametrized__TypeNameAlternatives_1_0();

            state._fsp--;


            }

             after(grammarAccess.getParametrizedAccess().getTypeNameAlternatives_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__TypeNameAssignment_1"


    // $ANTLR start "rule__Parametrized__ParameterAssignment_3"
    // InternalDft.g:2144:1: rule__Parametrized__ParameterAssignment_3 : ( ruleFloat ) ;
    public final void rule__Parametrized__ParameterAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:2148:1: ( ( ruleFloat ) )
            // InternalDft.g:2149:2: ( ruleFloat )
            {
            // InternalDft.g:2149:2: ( ruleFloat )
            // InternalDft.g:2150:3: ruleFloat
            {
             before(grammarAccess.getParametrizedAccess().getParameterFloatParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getParametrizedAccess().getParameterFloatParserRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Parametrized__ParameterAssignment_3"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000403FFF010L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000050000000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000001000000020L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000200000040L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000FFF010L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000800000040L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000003000000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000000020L});

}