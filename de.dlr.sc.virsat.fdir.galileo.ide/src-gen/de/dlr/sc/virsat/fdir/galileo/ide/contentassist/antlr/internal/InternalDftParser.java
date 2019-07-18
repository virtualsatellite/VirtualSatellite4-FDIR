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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_XOFY", "RULE_INT", "RULE_STRING", "RULE_ID", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'and'", "'or'", "'pand'", "'pand_i'", "'por'", "'por_i'", "'sand'", "'hsp'", "'wsp'", "'csp'", "'seq'", "'fdep'", "'toplevel'", "';'", "'lambda'", "'='", "'dorm'", "'repair'", "'observer'", "'obsRate'", "'rdep'", "'rateFactor'", "'delay'", "'time'", "'-'", "'.'", "'e'"
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


    // $ANTLR start "entryRuleGalileoNodeType"
    // InternalDft.g:128:1: entryRuleGalileoNodeType : ruleGalileoNodeType EOF ;
    public final void entryRuleGalileoNodeType() throws RecognitionException {
        try {
            // InternalDft.g:129:1: ( ruleGalileoNodeType EOF )
            // InternalDft.g:130:1: ruleGalileoNodeType EOF
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
    // InternalDft.g:137:1: ruleGalileoNodeType : ( ( rule__GalileoNodeType__Alternatives ) ) ;
    public final void ruleGalileoNodeType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:141:2: ( ( ( rule__GalileoNodeType__Alternatives ) ) )
            // InternalDft.g:142:2: ( ( rule__GalileoNodeType__Alternatives ) )
            {
            // InternalDft.g:142:2: ( ( rule__GalileoNodeType__Alternatives ) )
            // InternalDft.g:143:3: ( rule__GalileoNodeType__Alternatives )
            {
             before(grammarAccess.getGalileoNodeTypeAccess().getAlternatives()); 
            // InternalDft.g:144:3: ( rule__GalileoNodeType__Alternatives )
            // InternalDft.g:144:4: rule__GalileoNodeType__Alternatives
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


    // $ANTLR start "entryRuleNamedType"
    // InternalDft.g:153:1: entryRuleNamedType : ruleNamedType EOF ;
    public final void entryRuleNamedType() throws RecognitionException {
        try {
            // InternalDft.g:154:1: ( ruleNamedType EOF )
            // InternalDft.g:155:1: ruleNamedType EOF
            {
             before(grammarAccess.getNamedTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleNamedType();

            state._fsp--;

             after(grammarAccess.getNamedTypeRule()); 
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
    // $ANTLR end "entryRuleNamedType"


    // $ANTLR start "ruleNamedType"
    // InternalDft.g:162:1: ruleNamedType : ( ( rule__NamedType__TypeNameAssignment ) ) ;
    public final void ruleNamedType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:166:2: ( ( ( rule__NamedType__TypeNameAssignment ) ) )
            // InternalDft.g:167:2: ( ( rule__NamedType__TypeNameAssignment ) )
            {
            // InternalDft.g:167:2: ( ( rule__NamedType__TypeNameAssignment ) )
            // InternalDft.g:168:3: ( rule__NamedType__TypeNameAssignment )
            {
             before(grammarAccess.getNamedTypeAccess().getTypeNameAssignment()); 
            // InternalDft.g:169:3: ( rule__NamedType__TypeNameAssignment )
            // InternalDft.g:169:4: rule__NamedType__TypeNameAssignment
            {
            pushFollow(FOLLOW_2);
            rule__NamedType__TypeNameAssignment();

            state._fsp--;


            }

             after(grammarAccess.getNamedTypeAccess().getTypeNameAssignment()); 

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
    // $ANTLR end "ruleNamedType"


    // $ANTLR start "entryRuleObserverType"
    // InternalDft.g:178:1: entryRuleObserverType : ruleObserverType EOF ;
    public final void entryRuleObserverType() throws RecognitionException {
        try {
            // InternalDft.g:179:1: ( ruleObserverType EOF )
            // InternalDft.g:180:1: ruleObserverType EOF
            {
             before(grammarAccess.getObserverTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleObserverType();

            state._fsp--;

             after(grammarAccess.getObserverTypeRule()); 
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
    // $ANTLR end "entryRuleObserverType"


    // $ANTLR start "ruleObserverType"
    // InternalDft.g:187:1: ruleObserverType : ( ( rule__ObserverType__Group__0 ) ) ;
    public final void ruleObserverType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:191:2: ( ( ( rule__ObserverType__Group__0 ) ) )
            // InternalDft.g:192:2: ( ( rule__ObserverType__Group__0 ) )
            {
            // InternalDft.g:192:2: ( ( rule__ObserverType__Group__0 ) )
            // InternalDft.g:193:3: ( rule__ObserverType__Group__0 )
            {
             before(grammarAccess.getObserverTypeAccess().getGroup()); 
            // InternalDft.g:194:3: ( rule__ObserverType__Group__0 )
            // InternalDft.g:194:4: rule__ObserverType__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__ObserverType__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getObserverTypeAccess().getGroup()); 

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
    // $ANTLR end "ruleObserverType"


    // $ANTLR start "entryRuleRDEPType"
    // InternalDft.g:203:1: entryRuleRDEPType : ruleRDEPType EOF ;
    public final void entryRuleRDEPType() throws RecognitionException {
        try {
            // InternalDft.g:204:1: ( ruleRDEPType EOF )
            // InternalDft.g:205:1: ruleRDEPType EOF
            {
             before(grammarAccess.getRDEPTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleRDEPType();

            state._fsp--;

             after(grammarAccess.getRDEPTypeRule()); 
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
    // $ANTLR end "entryRuleRDEPType"


    // $ANTLR start "ruleRDEPType"
    // InternalDft.g:212:1: ruleRDEPType : ( ( rule__RDEPType__Group__0 ) ) ;
    public final void ruleRDEPType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:216:2: ( ( ( rule__RDEPType__Group__0 ) ) )
            // InternalDft.g:217:2: ( ( rule__RDEPType__Group__0 ) )
            {
            // InternalDft.g:217:2: ( ( rule__RDEPType__Group__0 ) )
            // InternalDft.g:218:3: ( rule__RDEPType__Group__0 )
            {
             before(grammarAccess.getRDEPTypeAccess().getGroup()); 
            // InternalDft.g:219:3: ( rule__RDEPType__Group__0 )
            // InternalDft.g:219:4: rule__RDEPType__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__RDEPType__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getRDEPTypeAccess().getGroup()); 

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
    // $ANTLR end "ruleRDEPType"


    // $ANTLR start "entryRuleDelayType"
    // InternalDft.g:228:1: entryRuleDelayType : ruleDelayType EOF ;
    public final void entryRuleDelayType() throws RecognitionException {
        try {
            // InternalDft.g:229:1: ( ruleDelayType EOF )
            // InternalDft.g:230:1: ruleDelayType EOF
            {
             before(grammarAccess.getDelayTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleDelayType();

            state._fsp--;

             after(grammarAccess.getDelayTypeRule()); 
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
    // $ANTLR end "entryRuleDelayType"


    // $ANTLR start "ruleDelayType"
    // InternalDft.g:237:1: ruleDelayType : ( ( rule__DelayType__Group__0 ) ) ;
    public final void ruleDelayType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:241:2: ( ( ( rule__DelayType__Group__0 ) ) )
            // InternalDft.g:242:2: ( ( rule__DelayType__Group__0 ) )
            {
            // InternalDft.g:242:2: ( ( rule__DelayType__Group__0 ) )
            // InternalDft.g:243:3: ( rule__DelayType__Group__0 )
            {
             before(grammarAccess.getDelayTypeAccess().getGroup()); 
            // InternalDft.g:244:3: ( rule__DelayType__Group__0 )
            // InternalDft.g:244:4: rule__DelayType__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__DelayType__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getDelayTypeAccess().getGroup()); 

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
    // $ANTLR end "ruleDelayType"


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

                if ( (LA1_1==26) ) {
                    alt1=2;
                }
                else if ( (LA1_1==RULE_XOFY||(LA1_1>=12 && LA1_1<=23)||LA1_1==30||LA1_1==32||LA1_1==34) ) {
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


    // $ANTLR start "rule__GalileoNodeType__Alternatives"
    // InternalDft.g:298:1: rule__GalileoNodeType__Alternatives : ( ( ruleNamedType ) | ( ruleObserverType ) | ( ruleRDEPType ) | ( ruleDelayType ) );
    public final void rule__GalileoNodeType__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:302:1: ( ( ruleNamedType ) | ( ruleObserverType ) | ( ruleRDEPType ) | ( ruleDelayType ) )
            int alt2=4;
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
                alt2=1;
                }
                break;
            case 30:
                {
                alt2=2;
                }
                break;
            case 32:
                {
                alt2=3;
                }
                break;
            case 34:
                {
                alt2=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // InternalDft.g:303:2: ( ruleNamedType )
                    {
                    // InternalDft.g:303:2: ( ruleNamedType )
                    // InternalDft.g:304:3: ruleNamedType
                    {
                     before(grammarAccess.getGalileoNodeTypeAccess().getNamedTypeParserRuleCall_0()); 
                    pushFollow(FOLLOW_2);
                    ruleNamedType();

                    state._fsp--;

                     after(grammarAccess.getGalileoNodeTypeAccess().getNamedTypeParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalDft.g:309:2: ( ruleObserverType )
                    {
                    // InternalDft.g:309:2: ( ruleObserverType )
                    // InternalDft.g:310:3: ruleObserverType
                    {
                     before(grammarAccess.getGalileoNodeTypeAccess().getObserverTypeParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
                    ruleObserverType();

                    state._fsp--;

                     after(grammarAccess.getGalileoNodeTypeAccess().getObserverTypeParserRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalDft.g:315:2: ( ruleRDEPType )
                    {
                    // InternalDft.g:315:2: ( ruleRDEPType )
                    // InternalDft.g:316:3: ruleRDEPType
                    {
                     before(grammarAccess.getGalileoNodeTypeAccess().getRDEPTypeParserRuleCall_2()); 
                    pushFollow(FOLLOW_2);
                    ruleRDEPType();

                    state._fsp--;

                     after(grammarAccess.getGalileoNodeTypeAccess().getRDEPTypeParserRuleCall_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalDft.g:321:2: ( ruleDelayType )
                    {
                    // InternalDft.g:321:2: ( ruleDelayType )
                    // InternalDft.g:322:3: ruleDelayType
                    {
                     before(grammarAccess.getGalileoNodeTypeAccess().getDelayTypeParserRuleCall_3()); 
                    pushFollow(FOLLOW_2);
                    ruleDelayType();

                    state._fsp--;

                     after(grammarAccess.getGalileoNodeTypeAccess().getDelayTypeParserRuleCall_3()); 

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


    // $ANTLR start "rule__NamedType__TypeNameAlternatives_0"
    // InternalDft.g:331:1: rule__NamedType__TypeNameAlternatives_0 : ( ( 'and' ) | ( 'or' ) | ( RULE_XOFY ) | ( 'pand' ) | ( 'pand_i' ) | ( 'por' ) | ( 'por_i' ) | ( 'sand' ) | ( 'hsp' ) | ( 'wsp' ) | ( 'csp' ) | ( 'seq' ) | ( 'fdep' ) );
    public final void rule__NamedType__TypeNameAlternatives_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:335:1: ( ( 'and' ) | ( 'or' ) | ( RULE_XOFY ) | ( 'pand' ) | ( 'pand_i' ) | ( 'por' ) | ( 'por_i' ) | ( 'sand' ) | ( 'hsp' ) | ( 'wsp' ) | ( 'csp' ) | ( 'seq' ) | ( 'fdep' ) )
            int alt3=13;
            switch ( input.LA(1) ) {
            case 12:
                {
                alt3=1;
                }
                break;
            case 13:
                {
                alt3=2;
                }
                break;
            case RULE_XOFY:
                {
                alt3=3;
                }
                break;
            case 14:
                {
                alt3=4;
                }
                break;
            case 15:
                {
                alt3=5;
                }
                break;
            case 16:
                {
                alt3=6;
                }
                break;
            case 17:
                {
                alt3=7;
                }
                break;
            case 18:
                {
                alt3=8;
                }
                break;
            case 19:
                {
                alt3=9;
                }
                break;
            case 20:
                {
                alt3=10;
                }
                break;
            case 21:
                {
                alt3=11;
                }
                break;
            case 22:
                {
                alt3=12;
                }
                break;
            case 23:
                {
                alt3=13;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // InternalDft.g:336:2: ( 'and' )
                    {
                    // InternalDft.g:336:2: ( 'and' )
                    // InternalDft.g:337:3: 'and'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameAndKeyword_0_0()); 
                    match(input,12,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameAndKeyword_0_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalDft.g:342:2: ( 'or' )
                    {
                    // InternalDft.g:342:2: ( 'or' )
                    // InternalDft.g:343:3: 'or'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameOrKeyword_0_1()); 
                    match(input,13,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameOrKeyword_0_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalDft.g:348:2: ( RULE_XOFY )
                    {
                    // InternalDft.g:348:2: ( RULE_XOFY )
                    // InternalDft.g:349:3: RULE_XOFY
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameXOFYTerminalRuleCall_0_2()); 
                    match(input,RULE_XOFY,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameXOFYTerminalRuleCall_0_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalDft.g:354:2: ( 'pand' )
                    {
                    // InternalDft.g:354:2: ( 'pand' )
                    // InternalDft.g:355:3: 'pand'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNamePandKeyword_0_3()); 
                    match(input,14,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNamePandKeyword_0_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalDft.g:360:2: ( 'pand_i' )
                    {
                    // InternalDft.g:360:2: ( 'pand_i' )
                    // InternalDft.g:361:3: 'pand_i'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNamePand_iKeyword_0_4()); 
                    match(input,15,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNamePand_iKeyword_0_4()); 

                    }


                    }
                    break;
                case 6 :
                    // InternalDft.g:366:2: ( 'por' )
                    {
                    // InternalDft.g:366:2: ( 'por' )
                    // InternalDft.g:367:3: 'por'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNamePorKeyword_0_5()); 
                    match(input,16,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNamePorKeyword_0_5()); 

                    }


                    }
                    break;
                case 7 :
                    // InternalDft.g:372:2: ( 'por_i' )
                    {
                    // InternalDft.g:372:2: ( 'por_i' )
                    // InternalDft.g:373:3: 'por_i'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNamePor_iKeyword_0_6()); 
                    match(input,17,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNamePor_iKeyword_0_6()); 

                    }


                    }
                    break;
                case 8 :
                    // InternalDft.g:378:2: ( 'sand' )
                    {
                    // InternalDft.g:378:2: ( 'sand' )
                    // InternalDft.g:379:3: 'sand'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameSandKeyword_0_7()); 
                    match(input,18,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameSandKeyword_0_7()); 

                    }


                    }
                    break;
                case 9 :
                    // InternalDft.g:384:2: ( 'hsp' )
                    {
                    // InternalDft.g:384:2: ( 'hsp' )
                    // InternalDft.g:385:3: 'hsp'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameHspKeyword_0_8()); 
                    match(input,19,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameHspKeyword_0_8()); 

                    }


                    }
                    break;
                case 10 :
                    // InternalDft.g:390:2: ( 'wsp' )
                    {
                    // InternalDft.g:390:2: ( 'wsp' )
                    // InternalDft.g:391:3: 'wsp'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameWspKeyword_0_9()); 
                    match(input,20,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameWspKeyword_0_9()); 

                    }


                    }
                    break;
                case 11 :
                    // InternalDft.g:396:2: ( 'csp' )
                    {
                    // InternalDft.g:396:2: ( 'csp' )
                    // InternalDft.g:397:3: 'csp'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameCspKeyword_0_10()); 
                    match(input,21,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameCspKeyword_0_10()); 

                    }


                    }
                    break;
                case 12 :
                    // InternalDft.g:402:2: ( 'seq' )
                    {
                    // InternalDft.g:402:2: ( 'seq' )
                    // InternalDft.g:403:3: 'seq'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameSeqKeyword_0_11()); 
                    match(input,22,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameSeqKeyword_0_11()); 

                    }


                    }
                    break;
                case 13 :
                    // InternalDft.g:408:2: ( 'fdep' )
                    {
                    // InternalDft.g:408:2: ( 'fdep' )
                    // InternalDft.g:409:3: 'fdep'
                    {
                     before(grammarAccess.getNamedTypeAccess().getTypeNameFdepKeyword_0_12()); 
                    match(input,23,FOLLOW_2); 
                     after(grammarAccess.getNamedTypeAccess().getTypeNameFdepKeyword_0_12()); 

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
    // $ANTLR end "rule__NamedType__TypeNameAlternatives_0"


    // $ANTLR start "rule__GalileoDft__Group__0"
    // InternalDft.g:418:1: rule__GalileoDft__Group__0 : rule__GalileoDft__Group__0__Impl rule__GalileoDft__Group__1 ;
    public final void rule__GalileoDft__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:422:1: ( rule__GalileoDft__Group__0__Impl rule__GalileoDft__Group__1 )
            // InternalDft.g:423:2: rule__GalileoDft__Group__0__Impl rule__GalileoDft__Group__1
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
    // InternalDft.g:430:1: rule__GalileoDft__Group__0__Impl : ( 'toplevel' ) ;
    public final void rule__GalileoDft__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:434:1: ( ( 'toplevel' ) )
            // InternalDft.g:435:1: ( 'toplevel' )
            {
            // InternalDft.g:435:1: ( 'toplevel' )
            // InternalDft.g:436:2: 'toplevel'
            {
             before(grammarAccess.getGalileoDftAccess().getToplevelKeyword_0()); 
            match(input,24,FOLLOW_2); 
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
    // InternalDft.g:445:1: rule__GalileoDft__Group__1 : rule__GalileoDft__Group__1__Impl rule__GalileoDft__Group__2 ;
    public final void rule__GalileoDft__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:449:1: ( rule__GalileoDft__Group__1__Impl rule__GalileoDft__Group__2 )
            // InternalDft.g:450:2: rule__GalileoDft__Group__1__Impl rule__GalileoDft__Group__2
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
    // InternalDft.g:457:1: rule__GalileoDft__Group__1__Impl : ( ( rule__GalileoDft__RootAssignment_1 ) ) ;
    public final void rule__GalileoDft__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:461:1: ( ( ( rule__GalileoDft__RootAssignment_1 ) ) )
            // InternalDft.g:462:1: ( ( rule__GalileoDft__RootAssignment_1 ) )
            {
            // InternalDft.g:462:1: ( ( rule__GalileoDft__RootAssignment_1 ) )
            // InternalDft.g:463:2: ( rule__GalileoDft__RootAssignment_1 )
            {
             before(grammarAccess.getGalileoDftAccess().getRootAssignment_1()); 
            // InternalDft.g:464:2: ( rule__GalileoDft__RootAssignment_1 )
            // InternalDft.g:464:3: rule__GalileoDft__RootAssignment_1
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
    // InternalDft.g:472:1: rule__GalileoDft__Group__2 : rule__GalileoDft__Group__2__Impl rule__GalileoDft__Group__3 ;
    public final void rule__GalileoDft__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:476:1: ( rule__GalileoDft__Group__2__Impl rule__GalileoDft__Group__3 )
            // InternalDft.g:477:2: rule__GalileoDft__Group__2__Impl rule__GalileoDft__Group__3
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
    // InternalDft.g:484:1: rule__GalileoDft__Group__2__Impl : ( ';' ) ;
    public final void rule__GalileoDft__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:488:1: ( ( ';' ) )
            // InternalDft.g:489:1: ( ';' )
            {
            // InternalDft.g:489:1: ( ';' )
            // InternalDft.g:490:2: ';'
            {
             before(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_2()); 
            match(input,25,FOLLOW_2); 
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
    // InternalDft.g:499:1: rule__GalileoDft__Group__3 : rule__GalileoDft__Group__3__Impl ;
    public final void rule__GalileoDft__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:503:1: ( rule__GalileoDft__Group__3__Impl )
            // InternalDft.g:504:2: rule__GalileoDft__Group__3__Impl
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
    // InternalDft.g:510:1: rule__GalileoDft__Group__3__Impl : ( ( rule__GalileoDft__Alternatives_3 )* ) ;
    public final void rule__GalileoDft__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:514:1: ( ( ( rule__GalileoDft__Alternatives_3 )* ) )
            // InternalDft.g:515:1: ( ( rule__GalileoDft__Alternatives_3 )* )
            {
            // InternalDft.g:515:1: ( ( rule__GalileoDft__Alternatives_3 )* )
            // InternalDft.g:516:2: ( rule__GalileoDft__Alternatives_3 )*
            {
             before(grammarAccess.getGalileoDftAccess().getAlternatives_3()); 
            // InternalDft.g:517:2: ( rule__GalileoDft__Alternatives_3 )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==RULE_STRING) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalDft.g:517:3: rule__GalileoDft__Alternatives_3
            	    {
            	    pushFollow(FOLLOW_5);
            	    rule__GalileoDft__Alternatives_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
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
    // InternalDft.g:526:1: rule__GalileoDft__Group_3_0__0 : rule__GalileoDft__Group_3_0__0__Impl rule__GalileoDft__Group_3_0__1 ;
    public final void rule__GalileoDft__Group_3_0__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:530:1: ( rule__GalileoDft__Group_3_0__0__Impl rule__GalileoDft__Group_3_0__1 )
            // InternalDft.g:531:2: rule__GalileoDft__Group_3_0__0__Impl rule__GalileoDft__Group_3_0__1
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
    // InternalDft.g:538:1: rule__GalileoDft__Group_3_0__0__Impl : ( ( rule__GalileoDft__GatesAssignment_3_0_0 ) ) ;
    public final void rule__GalileoDft__Group_3_0__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:542:1: ( ( ( rule__GalileoDft__GatesAssignment_3_0_0 ) ) )
            // InternalDft.g:543:1: ( ( rule__GalileoDft__GatesAssignment_3_0_0 ) )
            {
            // InternalDft.g:543:1: ( ( rule__GalileoDft__GatesAssignment_3_0_0 ) )
            // InternalDft.g:544:2: ( rule__GalileoDft__GatesAssignment_3_0_0 )
            {
             before(grammarAccess.getGalileoDftAccess().getGatesAssignment_3_0_0()); 
            // InternalDft.g:545:2: ( rule__GalileoDft__GatesAssignment_3_0_0 )
            // InternalDft.g:545:3: rule__GalileoDft__GatesAssignment_3_0_0
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
    // InternalDft.g:553:1: rule__GalileoDft__Group_3_0__1 : rule__GalileoDft__Group_3_0__1__Impl ;
    public final void rule__GalileoDft__Group_3_0__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:557:1: ( rule__GalileoDft__Group_3_0__1__Impl )
            // InternalDft.g:558:2: rule__GalileoDft__Group_3_0__1__Impl
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
    // InternalDft.g:564:1: rule__GalileoDft__Group_3_0__1__Impl : ( ';' ) ;
    public final void rule__GalileoDft__Group_3_0__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:568:1: ( ( ';' ) )
            // InternalDft.g:569:1: ( ';' )
            {
            // InternalDft.g:569:1: ( ';' )
            // InternalDft.g:570:2: ';'
            {
             before(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_0_1()); 
            match(input,25,FOLLOW_2); 
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
    // InternalDft.g:580:1: rule__GalileoDft__Group_3_1__0 : rule__GalileoDft__Group_3_1__0__Impl rule__GalileoDft__Group_3_1__1 ;
    public final void rule__GalileoDft__Group_3_1__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:584:1: ( rule__GalileoDft__Group_3_1__0__Impl rule__GalileoDft__Group_3_1__1 )
            // InternalDft.g:585:2: rule__GalileoDft__Group_3_1__0__Impl rule__GalileoDft__Group_3_1__1
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
    // InternalDft.g:592:1: rule__GalileoDft__Group_3_1__0__Impl : ( ( rule__GalileoDft__BasicEventsAssignment_3_1_0 ) ) ;
    public final void rule__GalileoDft__Group_3_1__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:596:1: ( ( ( rule__GalileoDft__BasicEventsAssignment_3_1_0 ) ) )
            // InternalDft.g:597:1: ( ( rule__GalileoDft__BasicEventsAssignment_3_1_0 ) )
            {
            // InternalDft.g:597:1: ( ( rule__GalileoDft__BasicEventsAssignment_3_1_0 ) )
            // InternalDft.g:598:2: ( rule__GalileoDft__BasicEventsAssignment_3_1_0 )
            {
             before(grammarAccess.getGalileoDftAccess().getBasicEventsAssignment_3_1_0()); 
            // InternalDft.g:599:2: ( rule__GalileoDft__BasicEventsAssignment_3_1_0 )
            // InternalDft.g:599:3: rule__GalileoDft__BasicEventsAssignment_3_1_0
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
    // InternalDft.g:607:1: rule__GalileoDft__Group_3_1__1 : rule__GalileoDft__Group_3_1__1__Impl ;
    public final void rule__GalileoDft__Group_3_1__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:611:1: ( rule__GalileoDft__Group_3_1__1__Impl )
            // InternalDft.g:612:2: rule__GalileoDft__Group_3_1__1__Impl
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
    // InternalDft.g:618:1: rule__GalileoDft__Group_3_1__1__Impl : ( ';' ) ;
    public final void rule__GalileoDft__Group_3_1__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:622:1: ( ( ';' ) )
            // InternalDft.g:623:1: ( ';' )
            {
            // InternalDft.g:623:1: ( ';' )
            // InternalDft.g:624:2: ';'
            {
             before(grammarAccess.getGalileoDftAccess().getSemicolonKeyword_3_1_1()); 
            match(input,25,FOLLOW_2); 
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
    // InternalDft.g:634:1: rule__GalileoGate__Group__0 : rule__GalileoGate__Group__0__Impl rule__GalileoGate__Group__1 ;
    public final void rule__GalileoGate__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:638:1: ( rule__GalileoGate__Group__0__Impl rule__GalileoGate__Group__1 )
            // InternalDft.g:639:2: rule__GalileoGate__Group__0__Impl rule__GalileoGate__Group__1
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
    // InternalDft.g:646:1: rule__GalileoGate__Group__0__Impl : ( ( rule__GalileoGate__NameAssignment_0 ) ) ;
    public final void rule__GalileoGate__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:650:1: ( ( ( rule__GalileoGate__NameAssignment_0 ) ) )
            // InternalDft.g:651:1: ( ( rule__GalileoGate__NameAssignment_0 ) )
            {
            // InternalDft.g:651:1: ( ( rule__GalileoGate__NameAssignment_0 ) )
            // InternalDft.g:652:2: ( rule__GalileoGate__NameAssignment_0 )
            {
             before(grammarAccess.getGalileoGateAccess().getNameAssignment_0()); 
            // InternalDft.g:653:2: ( rule__GalileoGate__NameAssignment_0 )
            // InternalDft.g:653:3: rule__GalileoGate__NameAssignment_0
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
    // InternalDft.g:661:1: rule__GalileoGate__Group__1 : rule__GalileoGate__Group__1__Impl rule__GalileoGate__Group__2 ;
    public final void rule__GalileoGate__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:665:1: ( rule__GalileoGate__Group__1__Impl rule__GalileoGate__Group__2 )
            // InternalDft.g:666:2: rule__GalileoGate__Group__1__Impl rule__GalileoGate__Group__2
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
    // InternalDft.g:673:1: rule__GalileoGate__Group__1__Impl : ( ( rule__GalileoGate__TypeAssignment_1 ) ) ;
    public final void rule__GalileoGate__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:677:1: ( ( ( rule__GalileoGate__TypeAssignment_1 ) ) )
            // InternalDft.g:678:1: ( ( rule__GalileoGate__TypeAssignment_1 ) )
            {
            // InternalDft.g:678:1: ( ( rule__GalileoGate__TypeAssignment_1 ) )
            // InternalDft.g:679:2: ( rule__GalileoGate__TypeAssignment_1 )
            {
             before(grammarAccess.getGalileoGateAccess().getTypeAssignment_1()); 
            // InternalDft.g:680:2: ( rule__GalileoGate__TypeAssignment_1 )
            // InternalDft.g:680:3: rule__GalileoGate__TypeAssignment_1
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
    // InternalDft.g:688:1: rule__GalileoGate__Group__2 : rule__GalileoGate__Group__2__Impl ;
    public final void rule__GalileoGate__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:692:1: ( rule__GalileoGate__Group__2__Impl )
            // InternalDft.g:693:2: rule__GalileoGate__Group__2__Impl
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
    // InternalDft.g:699:1: rule__GalileoGate__Group__2__Impl : ( ( rule__GalileoGate__ChildrenAssignment_2 )* ) ;
    public final void rule__GalileoGate__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:703:1: ( ( ( rule__GalileoGate__ChildrenAssignment_2 )* ) )
            // InternalDft.g:704:1: ( ( rule__GalileoGate__ChildrenAssignment_2 )* )
            {
            // InternalDft.g:704:1: ( ( rule__GalileoGate__ChildrenAssignment_2 )* )
            // InternalDft.g:705:2: ( rule__GalileoGate__ChildrenAssignment_2 )*
            {
             before(grammarAccess.getGalileoGateAccess().getChildrenAssignment_2()); 
            // InternalDft.g:706:2: ( rule__GalileoGate__ChildrenAssignment_2 )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==RULE_STRING) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalDft.g:706:3: rule__GalileoGate__ChildrenAssignment_2
            	    {
            	    pushFollow(FOLLOW_5);
            	    rule__GalileoGate__ChildrenAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
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
    // InternalDft.g:715:1: rule__GalileoBasicEvent__Group__0 : rule__GalileoBasicEvent__Group__0__Impl rule__GalileoBasicEvent__Group__1 ;
    public final void rule__GalileoBasicEvent__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:719:1: ( rule__GalileoBasicEvent__Group__0__Impl rule__GalileoBasicEvent__Group__1 )
            // InternalDft.g:720:2: rule__GalileoBasicEvent__Group__0__Impl rule__GalileoBasicEvent__Group__1
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
    // InternalDft.g:727:1: rule__GalileoBasicEvent__Group__0__Impl : ( ( rule__GalileoBasicEvent__NameAssignment_0 ) ) ;
    public final void rule__GalileoBasicEvent__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:731:1: ( ( ( rule__GalileoBasicEvent__NameAssignment_0 ) ) )
            // InternalDft.g:732:1: ( ( rule__GalileoBasicEvent__NameAssignment_0 ) )
            {
            // InternalDft.g:732:1: ( ( rule__GalileoBasicEvent__NameAssignment_0 ) )
            // InternalDft.g:733:2: ( rule__GalileoBasicEvent__NameAssignment_0 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getNameAssignment_0()); 
            // InternalDft.g:734:2: ( rule__GalileoBasicEvent__NameAssignment_0 )
            // InternalDft.g:734:3: rule__GalileoBasicEvent__NameAssignment_0
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
    // InternalDft.g:742:1: rule__GalileoBasicEvent__Group__1 : rule__GalileoBasicEvent__Group__1__Impl rule__GalileoBasicEvent__Group__2 ;
    public final void rule__GalileoBasicEvent__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:746:1: ( rule__GalileoBasicEvent__Group__1__Impl rule__GalileoBasicEvent__Group__2 )
            // InternalDft.g:747:2: rule__GalileoBasicEvent__Group__1__Impl rule__GalileoBasicEvent__Group__2
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
    // InternalDft.g:754:1: rule__GalileoBasicEvent__Group__1__Impl : ( 'lambda' ) ;
    public final void rule__GalileoBasicEvent__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:758:1: ( ( 'lambda' ) )
            // InternalDft.g:759:1: ( 'lambda' )
            {
            // InternalDft.g:759:1: ( 'lambda' )
            // InternalDft.g:760:2: 'lambda'
            {
             before(grammarAccess.getGalileoBasicEventAccess().getLambdaKeyword_1()); 
            match(input,26,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getLambdaKeyword_1()); 

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
    // InternalDft.g:769:1: rule__GalileoBasicEvent__Group__2 : rule__GalileoBasicEvent__Group__2__Impl rule__GalileoBasicEvent__Group__3 ;
    public final void rule__GalileoBasicEvent__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:773:1: ( rule__GalileoBasicEvent__Group__2__Impl rule__GalileoBasicEvent__Group__3 )
            // InternalDft.g:774:2: rule__GalileoBasicEvent__Group__2__Impl rule__GalileoBasicEvent__Group__3
            {
            pushFollow(FOLLOW_9);
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
    // InternalDft.g:781:1: rule__GalileoBasicEvent__Group__2__Impl : ( '=' ) ;
    public final void rule__GalileoBasicEvent__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:785:1: ( ( '=' ) )
            // InternalDft.g:786:1: ( '=' )
            {
            // InternalDft.g:786:1: ( '=' )
            // InternalDft.g:787:2: '='
            {
             before(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_2()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_2()); 

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
    // InternalDft.g:796:1: rule__GalileoBasicEvent__Group__3 : rule__GalileoBasicEvent__Group__3__Impl rule__GalileoBasicEvent__Group__4 ;
    public final void rule__GalileoBasicEvent__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:800:1: ( rule__GalileoBasicEvent__Group__3__Impl rule__GalileoBasicEvent__Group__4 )
            // InternalDft.g:801:2: rule__GalileoBasicEvent__Group__3__Impl rule__GalileoBasicEvent__Group__4
            {
            pushFollow(FOLLOW_10);
            rule__GalileoBasicEvent__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group__4();

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
    // InternalDft.g:808:1: rule__GalileoBasicEvent__Group__3__Impl : ( ( rule__GalileoBasicEvent__LambdaAssignment_3 ) ) ;
    public final void rule__GalileoBasicEvent__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:812:1: ( ( ( rule__GalileoBasicEvent__LambdaAssignment_3 ) ) )
            // InternalDft.g:813:1: ( ( rule__GalileoBasicEvent__LambdaAssignment_3 ) )
            {
            // InternalDft.g:813:1: ( ( rule__GalileoBasicEvent__LambdaAssignment_3 ) )
            // InternalDft.g:814:2: ( rule__GalileoBasicEvent__LambdaAssignment_3 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getLambdaAssignment_3()); 
            // InternalDft.g:815:2: ( rule__GalileoBasicEvent__LambdaAssignment_3 )
            // InternalDft.g:815:3: rule__GalileoBasicEvent__LambdaAssignment_3
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__LambdaAssignment_3();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getLambdaAssignment_3()); 

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


    // $ANTLR start "rule__GalileoBasicEvent__Group__4"
    // InternalDft.g:823:1: rule__GalileoBasicEvent__Group__4 : rule__GalileoBasicEvent__Group__4__Impl rule__GalileoBasicEvent__Group__5 ;
    public final void rule__GalileoBasicEvent__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:827:1: ( rule__GalileoBasicEvent__Group__4__Impl rule__GalileoBasicEvent__Group__5 )
            // InternalDft.g:828:2: rule__GalileoBasicEvent__Group__4__Impl rule__GalileoBasicEvent__Group__5
            {
            pushFollow(FOLLOW_10);
            rule__GalileoBasicEvent__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group__5();

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
    // $ANTLR end "rule__GalileoBasicEvent__Group__4"


    // $ANTLR start "rule__GalileoBasicEvent__Group__4__Impl"
    // InternalDft.g:835:1: rule__GalileoBasicEvent__Group__4__Impl : ( ( rule__GalileoBasicEvent__Group_4__0 )? ) ;
    public final void rule__GalileoBasicEvent__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:839:1: ( ( ( rule__GalileoBasicEvent__Group_4__0 )? ) )
            // InternalDft.g:840:1: ( ( rule__GalileoBasicEvent__Group_4__0 )? )
            {
            // InternalDft.g:840:1: ( ( rule__GalileoBasicEvent__Group_4__0 )? )
            // InternalDft.g:841:2: ( rule__GalileoBasicEvent__Group_4__0 )?
            {
             before(grammarAccess.getGalileoBasicEventAccess().getGroup_4()); 
            // InternalDft.g:842:2: ( rule__GalileoBasicEvent__Group_4__0 )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==28) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // InternalDft.g:842:3: rule__GalileoBasicEvent__Group_4__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoBasicEvent__Group_4__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGalileoBasicEventAccess().getGroup_4()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__Group__4__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group__5"
    // InternalDft.g:850:1: rule__GalileoBasicEvent__Group__5 : rule__GalileoBasicEvent__Group__5__Impl ;
    public final void rule__GalileoBasicEvent__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:854:1: ( rule__GalileoBasicEvent__Group__5__Impl )
            // InternalDft.g:855:2: rule__GalileoBasicEvent__Group__5__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group__5__Impl();

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
    // $ANTLR end "rule__GalileoBasicEvent__Group__5"


    // $ANTLR start "rule__GalileoBasicEvent__Group__5__Impl"
    // InternalDft.g:861:1: rule__GalileoBasicEvent__Group__5__Impl : ( ( rule__GalileoBasicEvent__Group_5__0 )? ) ;
    public final void rule__GalileoBasicEvent__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:865:1: ( ( ( rule__GalileoBasicEvent__Group_5__0 )? ) )
            // InternalDft.g:866:1: ( ( rule__GalileoBasicEvent__Group_5__0 )? )
            {
            // InternalDft.g:866:1: ( ( rule__GalileoBasicEvent__Group_5__0 )? )
            // InternalDft.g:867:2: ( rule__GalileoBasicEvent__Group_5__0 )?
            {
             before(grammarAccess.getGalileoBasicEventAccess().getGroup_5()); 
            // InternalDft.g:868:2: ( rule__GalileoBasicEvent__Group_5__0 )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==29) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // InternalDft.g:868:3: rule__GalileoBasicEvent__Group_5__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__GalileoBasicEvent__Group_5__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getGalileoBasicEventAccess().getGroup_5()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__Group__5__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_4__0"
    // InternalDft.g:877:1: rule__GalileoBasicEvent__Group_4__0 : rule__GalileoBasicEvent__Group_4__0__Impl rule__GalileoBasicEvent__Group_4__1 ;
    public final void rule__GalileoBasicEvent__Group_4__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:881:1: ( rule__GalileoBasicEvent__Group_4__0__Impl rule__GalileoBasicEvent__Group_4__1 )
            // InternalDft.g:882:2: rule__GalileoBasicEvent__Group_4__0__Impl rule__GalileoBasicEvent__Group_4__1
            {
            pushFollow(FOLLOW_8);
            rule__GalileoBasicEvent__Group_4__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_4__1();

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_4__0"


    // $ANTLR start "rule__GalileoBasicEvent__Group_4__0__Impl"
    // InternalDft.g:889:1: rule__GalileoBasicEvent__Group_4__0__Impl : ( 'dorm' ) ;
    public final void rule__GalileoBasicEvent__Group_4__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:893:1: ( ( 'dorm' ) )
            // InternalDft.g:894:1: ( 'dorm' )
            {
            // InternalDft.g:894:1: ( 'dorm' )
            // InternalDft.g:895:2: 'dorm'
            {
             before(grammarAccess.getGalileoBasicEventAccess().getDormKeyword_4_0()); 
            match(input,28,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getDormKeyword_4_0()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_4__0__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_4__1"
    // InternalDft.g:904:1: rule__GalileoBasicEvent__Group_4__1 : rule__GalileoBasicEvent__Group_4__1__Impl rule__GalileoBasicEvent__Group_4__2 ;
    public final void rule__GalileoBasicEvent__Group_4__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:908:1: ( rule__GalileoBasicEvent__Group_4__1__Impl rule__GalileoBasicEvent__Group_4__2 )
            // InternalDft.g:909:2: rule__GalileoBasicEvent__Group_4__1__Impl rule__GalileoBasicEvent__Group_4__2
            {
            pushFollow(FOLLOW_9);
            rule__GalileoBasicEvent__Group_4__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_4__2();

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_4__1"


    // $ANTLR start "rule__GalileoBasicEvent__Group_4__1__Impl"
    // InternalDft.g:916:1: rule__GalileoBasicEvent__Group_4__1__Impl : ( '=' ) ;
    public final void rule__GalileoBasicEvent__Group_4__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:920:1: ( ( '=' ) )
            // InternalDft.g:921:1: ( '=' )
            {
            // InternalDft.g:921:1: ( '=' )
            // InternalDft.g:922:2: '='
            {
             before(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_4_1()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_4_1()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_4__1__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_4__2"
    // InternalDft.g:931:1: rule__GalileoBasicEvent__Group_4__2 : rule__GalileoBasicEvent__Group_4__2__Impl ;
    public final void rule__GalileoBasicEvent__Group_4__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:935:1: ( rule__GalileoBasicEvent__Group_4__2__Impl )
            // InternalDft.g:936:2: rule__GalileoBasicEvent__Group_4__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_4__2__Impl();

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_4__2"


    // $ANTLR start "rule__GalileoBasicEvent__Group_4__2__Impl"
    // InternalDft.g:942:1: rule__GalileoBasicEvent__Group_4__2__Impl : ( ( rule__GalileoBasicEvent__DormAssignment_4_2 ) ) ;
    public final void rule__GalileoBasicEvent__Group_4__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:946:1: ( ( ( rule__GalileoBasicEvent__DormAssignment_4_2 ) ) )
            // InternalDft.g:947:1: ( ( rule__GalileoBasicEvent__DormAssignment_4_2 ) )
            {
            // InternalDft.g:947:1: ( ( rule__GalileoBasicEvent__DormAssignment_4_2 ) )
            // InternalDft.g:948:2: ( rule__GalileoBasicEvent__DormAssignment_4_2 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getDormAssignment_4_2()); 
            // InternalDft.g:949:2: ( rule__GalileoBasicEvent__DormAssignment_4_2 )
            // InternalDft.g:949:3: rule__GalileoBasicEvent__DormAssignment_4_2
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__DormAssignment_4_2();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getDormAssignment_4_2()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_4__2__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_5__0"
    // InternalDft.g:958:1: rule__GalileoBasicEvent__Group_5__0 : rule__GalileoBasicEvent__Group_5__0__Impl rule__GalileoBasicEvent__Group_5__1 ;
    public final void rule__GalileoBasicEvent__Group_5__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:962:1: ( rule__GalileoBasicEvent__Group_5__0__Impl rule__GalileoBasicEvent__Group_5__1 )
            // InternalDft.g:963:2: rule__GalileoBasicEvent__Group_5__0__Impl rule__GalileoBasicEvent__Group_5__1
            {
            pushFollow(FOLLOW_8);
            rule__GalileoBasicEvent__Group_5__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_5__1();

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_5__0"


    // $ANTLR start "rule__GalileoBasicEvent__Group_5__0__Impl"
    // InternalDft.g:970:1: rule__GalileoBasicEvent__Group_5__0__Impl : ( 'repair' ) ;
    public final void rule__GalileoBasicEvent__Group_5__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:974:1: ( ( 'repair' ) )
            // InternalDft.g:975:1: ( 'repair' )
            {
            // InternalDft.g:975:1: ( 'repair' )
            // InternalDft.g:976:2: 'repair'
            {
             before(grammarAccess.getGalileoBasicEventAccess().getRepairKeyword_5_0()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getRepairKeyword_5_0()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_5__0__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_5__1"
    // InternalDft.g:985:1: rule__GalileoBasicEvent__Group_5__1 : rule__GalileoBasicEvent__Group_5__1__Impl rule__GalileoBasicEvent__Group_5__2 ;
    public final void rule__GalileoBasicEvent__Group_5__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:989:1: ( rule__GalileoBasicEvent__Group_5__1__Impl rule__GalileoBasicEvent__Group_5__2 )
            // InternalDft.g:990:2: rule__GalileoBasicEvent__Group_5__1__Impl rule__GalileoBasicEvent__Group_5__2
            {
            pushFollow(FOLLOW_9);
            rule__GalileoBasicEvent__Group_5__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_5__2();

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_5__1"


    // $ANTLR start "rule__GalileoBasicEvent__Group_5__1__Impl"
    // InternalDft.g:997:1: rule__GalileoBasicEvent__Group_5__1__Impl : ( '=' ) ;
    public final void rule__GalileoBasicEvent__Group_5__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1001:1: ( ( '=' ) )
            // InternalDft.g:1002:1: ( '=' )
            {
            // InternalDft.g:1002:1: ( '=' )
            // InternalDft.g:1003:2: '='
            {
             before(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_5_1()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getGalileoBasicEventAccess().getEqualsSignKeyword_5_1()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_5__1__Impl"


    // $ANTLR start "rule__GalileoBasicEvent__Group_5__2"
    // InternalDft.g:1012:1: rule__GalileoBasicEvent__Group_5__2 : rule__GalileoBasicEvent__Group_5__2__Impl ;
    public final void rule__GalileoBasicEvent__Group_5__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1016:1: ( rule__GalileoBasicEvent__Group_5__2__Impl )
            // InternalDft.g:1017:2: rule__GalileoBasicEvent__Group_5__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__Group_5__2__Impl();

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_5__2"


    // $ANTLR start "rule__GalileoBasicEvent__Group_5__2__Impl"
    // InternalDft.g:1023:1: rule__GalileoBasicEvent__Group_5__2__Impl : ( ( rule__GalileoBasicEvent__RepairAssignment_5_2 ) ) ;
    public final void rule__GalileoBasicEvent__Group_5__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1027:1: ( ( ( rule__GalileoBasicEvent__RepairAssignment_5_2 ) ) )
            // InternalDft.g:1028:1: ( ( rule__GalileoBasicEvent__RepairAssignment_5_2 ) )
            {
            // InternalDft.g:1028:1: ( ( rule__GalileoBasicEvent__RepairAssignment_5_2 ) )
            // InternalDft.g:1029:2: ( rule__GalileoBasicEvent__RepairAssignment_5_2 )
            {
             before(grammarAccess.getGalileoBasicEventAccess().getRepairAssignment_5_2()); 
            // InternalDft.g:1030:2: ( rule__GalileoBasicEvent__RepairAssignment_5_2 )
            // InternalDft.g:1030:3: rule__GalileoBasicEvent__RepairAssignment_5_2
            {
            pushFollow(FOLLOW_2);
            rule__GalileoBasicEvent__RepairAssignment_5_2();

            state._fsp--;


            }

             after(grammarAccess.getGalileoBasicEventAccess().getRepairAssignment_5_2()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__Group_5__2__Impl"


    // $ANTLR start "rule__ObserverType__Group__0"
    // InternalDft.g:1039:1: rule__ObserverType__Group__0 : rule__ObserverType__Group__0__Impl rule__ObserverType__Group__1 ;
    public final void rule__ObserverType__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1043:1: ( rule__ObserverType__Group__0__Impl rule__ObserverType__Group__1 )
            // InternalDft.g:1044:2: rule__ObserverType__Group__0__Impl rule__ObserverType__Group__1
            {
            pushFollow(FOLLOW_11);
            rule__ObserverType__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__ObserverType__Group__1();

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
    // $ANTLR end "rule__ObserverType__Group__0"


    // $ANTLR start "rule__ObserverType__Group__0__Impl"
    // InternalDft.g:1051:1: rule__ObserverType__Group__0__Impl : ( 'observer' ) ;
    public final void rule__ObserverType__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1055:1: ( ( 'observer' ) )
            // InternalDft.g:1056:1: ( 'observer' )
            {
            // InternalDft.g:1056:1: ( 'observer' )
            // InternalDft.g:1057:2: 'observer'
            {
             before(grammarAccess.getObserverTypeAccess().getObserverKeyword_0()); 
            match(input,30,FOLLOW_2); 
             after(grammarAccess.getObserverTypeAccess().getObserverKeyword_0()); 

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
    // $ANTLR end "rule__ObserverType__Group__0__Impl"


    // $ANTLR start "rule__ObserverType__Group__1"
    // InternalDft.g:1066:1: rule__ObserverType__Group__1 : rule__ObserverType__Group__1__Impl rule__ObserverType__Group__2 ;
    public final void rule__ObserverType__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1070:1: ( rule__ObserverType__Group__1__Impl rule__ObserverType__Group__2 )
            // InternalDft.g:1071:2: rule__ObserverType__Group__1__Impl rule__ObserverType__Group__2
            {
            pushFollow(FOLLOW_11);
            rule__ObserverType__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__ObserverType__Group__2();

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
    // $ANTLR end "rule__ObserverType__Group__1"


    // $ANTLR start "rule__ObserverType__Group__1__Impl"
    // InternalDft.g:1078:1: rule__ObserverType__Group__1__Impl : ( ( rule__ObserverType__ObservablesAssignment_1 )* ) ;
    public final void rule__ObserverType__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1082:1: ( ( ( rule__ObserverType__ObservablesAssignment_1 )* ) )
            // InternalDft.g:1083:1: ( ( rule__ObserverType__ObservablesAssignment_1 )* )
            {
            // InternalDft.g:1083:1: ( ( rule__ObserverType__ObservablesAssignment_1 )* )
            // InternalDft.g:1084:2: ( rule__ObserverType__ObservablesAssignment_1 )*
            {
             before(grammarAccess.getObserverTypeAccess().getObservablesAssignment_1()); 
            // InternalDft.g:1085:2: ( rule__ObserverType__ObservablesAssignment_1 )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==RULE_STRING) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalDft.g:1085:3: rule__ObserverType__ObservablesAssignment_1
            	    {
            	    pushFollow(FOLLOW_5);
            	    rule__ObserverType__ObservablesAssignment_1();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

             after(grammarAccess.getObserverTypeAccess().getObservablesAssignment_1()); 

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
    // $ANTLR end "rule__ObserverType__Group__1__Impl"


    // $ANTLR start "rule__ObserverType__Group__2"
    // InternalDft.g:1093:1: rule__ObserverType__Group__2 : rule__ObserverType__Group__2__Impl rule__ObserverType__Group__3 ;
    public final void rule__ObserverType__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1097:1: ( rule__ObserverType__Group__2__Impl rule__ObserverType__Group__3 )
            // InternalDft.g:1098:2: rule__ObserverType__Group__2__Impl rule__ObserverType__Group__3
            {
            pushFollow(FOLLOW_8);
            rule__ObserverType__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__ObserverType__Group__3();

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
    // $ANTLR end "rule__ObserverType__Group__2"


    // $ANTLR start "rule__ObserverType__Group__2__Impl"
    // InternalDft.g:1105:1: rule__ObserverType__Group__2__Impl : ( 'obsRate' ) ;
    public final void rule__ObserverType__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1109:1: ( ( 'obsRate' ) )
            // InternalDft.g:1110:1: ( 'obsRate' )
            {
            // InternalDft.g:1110:1: ( 'obsRate' )
            // InternalDft.g:1111:2: 'obsRate'
            {
             before(grammarAccess.getObserverTypeAccess().getObsRateKeyword_2()); 
            match(input,31,FOLLOW_2); 
             after(grammarAccess.getObserverTypeAccess().getObsRateKeyword_2()); 

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
    // $ANTLR end "rule__ObserverType__Group__2__Impl"


    // $ANTLR start "rule__ObserverType__Group__3"
    // InternalDft.g:1120:1: rule__ObserverType__Group__3 : rule__ObserverType__Group__3__Impl rule__ObserverType__Group__4 ;
    public final void rule__ObserverType__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1124:1: ( rule__ObserverType__Group__3__Impl rule__ObserverType__Group__4 )
            // InternalDft.g:1125:2: rule__ObserverType__Group__3__Impl rule__ObserverType__Group__4
            {
            pushFollow(FOLLOW_9);
            rule__ObserverType__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__ObserverType__Group__4();

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
    // $ANTLR end "rule__ObserverType__Group__3"


    // $ANTLR start "rule__ObserverType__Group__3__Impl"
    // InternalDft.g:1132:1: rule__ObserverType__Group__3__Impl : ( '=' ) ;
    public final void rule__ObserverType__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1136:1: ( ( '=' ) )
            // InternalDft.g:1137:1: ( '=' )
            {
            // InternalDft.g:1137:1: ( '=' )
            // InternalDft.g:1138:2: '='
            {
             before(grammarAccess.getObserverTypeAccess().getEqualsSignKeyword_3()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getObserverTypeAccess().getEqualsSignKeyword_3()); 

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
    // $ANTLR end "rule__ObserverType__Group__3__Impl"


    // $ANTLR start "rule__ObserverType__Group__4"
    // InternalDft.g:1147:1: rule__ObserverType__Group__4 : rule__ObserverType__Group__4__Impl ;
    public final void rule__ObserverType__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1151:1: ( rule__ObserverType__Group__4__Impl )
            // InternalDft.g:1152:2: rule__ObserverType__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__ObserverType__Group__4__Impl();

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
    // $ANTLR end "rule__ObserverType__Group__4"


    // $ANTLR start "rule__ObserverType__Group__4__Impl"
    // InternalDft.g:1158:1: rule__ObserverType__Group__4__Impl : ( ( rule__ObserverType__ObservationRateAssignment_4 ) ) ;
    public final void rule__ObserverType__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1162:1: ( ( ( rule__ObserverType__ObservationRateAssignment_4 ) ) )
            // InternalDft.g:1163:1: ( ( rule__ObserverType__ObservationRateAssignment_4 ) )
            {
            // InternalDft.g:1163:1: ( ( rule__ObserverType__ObservationRateAssignment_4 ) )
            // InternalDft.g:1164:2: ( rule__ObserverType__ObservationRateAssignment_4 )
            {
             before(grammarAccess.getObserverTypeAccess().getObservationRateAssignment_4()); 
            // InternalDft.g:1165:2: ( rule__ObserverType__ObservationRateAssignment_4 )
            // InternalDft.g:1165:3: rule__ObserverType__ObservationRateAssignment_4
            {
            pushFollow(FOLLOW_2);
            rule__ObserverType__ObservationRateAssignment_4();

            state._fsp--;


            }

             after(grammarAccess.getObserverTypeAccess().getObservationRateAssignment_4()); 

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
    // $ANTLR end "rule__ObserverType__Group__4__Impl"


    // $ANTLR start "rule__RDEPType__Group__0"
    // InternalDft.g:1174:1: rule__RDEPType__Group__0 : rule__RDEPType__Group__0__Impl rule__RDEPType__Group__1 ;
    public final void rule__RDEPType__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1178:1: ( rule__RDEPType__Group__0__Impl rule__RDEPType__Group__1 )
            // InternalDft.g:1179:2: rule__RDEPType__Group__0__Impl rule__RDEPType__Group__1
            {
            pushFollow(FOLLOW_12);
            rule__RDEPType__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__RDEPType__Group__1();

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
    // $ANTLR end "rule__RDEPType__Group__0"


    // $ANTLR start "rule__RDEPType__Group__0__Impl"
    // InternalDft.g:1186:1: rule__RDEPType__Group__0__Impl : ( 'rdep' ) ;
    public final void rule__RDEPType__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1190:1: ( ( 'rdep' ) )
            // InternalDft.g:1191:1: ( 'rdep' )
            {
            // InternalDft.g:1191:1: ( 'rdep' )
            // InternalDft.g:1192:2: 'rdep'
            {
             before(grammarAccess.getRDEPTypeAccess().getRdepKeyword_0()); 
            match(input,32,FOLLOW_2); 
             after(grammarAccess.getRDEPTypeAccess().getRdepKeyword_0()); 

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
    // $ANTLR end "rule__RDEPType__Group__0__Impl"


    // $ANTLR start "rule__RDEPType__Group__1"
    // InternalDft.g:1201:1: rule__RDEPType__Group__1 : rule__RDEPType__Group__1__Impl rule__RDEPType__Group__2 ;
    public final void rule__RDEPType__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1205:1: ( rule__RDEPType__Group__1__Impl rule__RDEPType__Group__2 )
            // InternalDft.g:1206:2: rule__RDEPType__Group__1__Impl rule__RDEPType__Group__2
            {
            pushFollow(FOLLOW_9);
            rule__RDEPType__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__RDEPType__Group__2();

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
    // $ANTLR end "rule__RDEPType__Group__1"


    // $ANTLR start "rule__RDEPType__Group__1__Impl"
    // InternalDft.g:1213:1: rule__RDEPType__Group__1__Impl : ( 'rateFactor' ) ;
    public final void rule__RDEPType__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1217:1: ( ( 'rateFactor' ) )
            // InternalDft.g:1218:1: ( 'rateFactor' )
            {
            // InternalDft.g:1218:1: ( 'rateFactor' )
            // InternalDft.g:1219:2: 'rateFactor'
            {
             before(grammarAccess.getRDEPTypeAccess().getRateFactorKeyword_1()); 
            match(input,33,FOLLOW_2); 
             after(grammarAccess.getRDEPTypeAccess().getRateFactorKeyword_1()); 

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
    // $ANTLR end "rule__RDEPType__Group__1__Impl"


    // $ANTLR start "rule__RDEPType__Group__2"
    // InternalDft.g:1228:1: rule__RDEPType__Group__2 : rule__RDEPType__Group__2__Impl ;
    public final void rule__RDEPType__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1232:1: ( rule__RDEPType__Group__2__Impl )
            // InternalDft.g:1233:2: rule__RDEPType__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__RDEPType__Group__2__Impl();

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
    // $ANTLR end "rule__RDEPType__Group__2"


    // $ANTLR start "rule__RDEPType__Group__2__Impl"
    // InternalDft.g:1239:1: rule__RDEPType__Group__2__Impl : ( ( rule__RDEPType__RateFactorAssignment_2 ) ) ;
    public final void rule__RDEPType__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1243:1: ( ( ( rule__RDEPType__RateFactorAssignment_2 ) ) )
            // InternalDft.g:1244:1: ( ( rule__RDEPType__RateFactorAssignment_2 ) )
            {
            // InternalDft.g:1244:1: ( ( rule__RDEPType__RateFactorAssignment_2 ) )
            // InternalDft.g:1245:2: ( rule__RDEPType__RateFactorAssignment_2 )
            {
             before(grammarAccess.getRDEPTypeAccess().getRateFactorAssignment_2()); 
            // InternalDft.g:1246:2: ( rule__RDEPType__RateFactorAssignment_2 )
            // InternalDft.g:1246:3: rule__RDEPType__RateFactorAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__RDEPType__RateFactorAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getRDEPTypeAccess().getRateFactorAssignment_2()); 

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
    // $ANTLR end "rule__RDEPType__Group__2__Impl"


    // $ANTLR start "rule__DelayType__Group__0"
    // InternalDft.g:1255:1: rule__DelayType__Group__0 : rule__DelayType__Group__0__Impl rule__DelayType__Group__1 ;
    public final void rule__DelayType__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1259:1: ( rule__DelayType__Group__0__Impl rule__DelayType__Group__1 )
            // InternalDft.g:1260:2: rule__DelayType__Group__0__Impl rule__DelayType__Group__1
            {
            pushFollow(FOLLOW_13);
            rule__DelayType__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DelayType__Group__1();

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
    // $ANTLR end "rule__DelayType__Group__0"


    // $ANTLR start "rule__DelayType__Group__0__Impl"
    // InternalDft.g:1267:1: rule__DelayType__Group__0__Impl : ( 'delay' ) ;
    public final void rule__DelayType__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1271:1: ( ( 'delay' ) )
            // InternalDft.g:1272:1: ( 'delay' )
            {
            // InternalDft.g:1272:1: ( 'delay' )
            // InternalDft.g:1273:2: 'delay'
            {
             before(grammarAccess.getDelayTypeAccess().getDelayKeyword_0()); 
            match(input,34,FOLLOW_2); 
             after(grammarAccess.getDelayTypeAccess().getDelayKeyword_0()); 

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
    // $ANTLR end "rule__DelayType__Group__0__Impl"


    // $ANTLR start "rule__DelayType__Group__1"
    // InternalDft.g:1282:1: rule__DelayType__Group__1 : rule__DelayType__Group__1__Impl rule__DelayType__Group__2 ;
    public final void rule__DelayType__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1286:1: ( rule__DelayType__Group__1__Impl rule__DelayType__Group__2 )
            // InternalDft.g:1287:2: rule__DelayType__Group__1__Impl rule__DelayType__Group__2
            {
            pushFollow(FOLLOW_9);
            rule__DelayType__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DelayType__Group__2();

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
    // $ANTLR end "rule__DelayType__Group__1"


    // $ANTLR start "rule__DelayType__Group__1__Impl"
    // InternalDft.g:1294:1: rule__DelayType__Group__1__Impl : ( 'time' ) ;
    public final void rule__DelayType__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1298:1: ( ( 'time' ) )
            // InternalDft.g:1299:1: ( 'time' )
            {
            // InternalDft.g:1299:1: ( 'time' )
            // InternalDft.g:1300:2: 'time'
            {
             before(grammarAccess.getDelayTypeAccess().getTimeKeyword_1()); 
            match(input,35,FOLLOW_2); 
             after(grammarAccess.getDelayTypeAccess().getTimeKeyword_1()); 

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
    // $ANTLR end "rule__DelayType__Group__1__Impl"


    // $ANTLR start "rule__DelayType__Group__2"
    // InternalDft.g:1309:1: rule__DelayType__Group__2 : rule__DelayType__Group__2__Impl ;
    public final void rule__DelayType__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1313:1: ( rule__DelayType__Group__2__Impl )
            // InternalDft.g:1314:2: rule__DelayType__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__DelayType__Group__2__Impl();

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
    // $ANTLR end "rule__DelayType__Group__2"


    // $ANTLR start "rule__DelayType__Group__2__Impl"
    // InternalDft.g:1320:1: rule__DelayType__Group__2__Impl : ( ( rule__DelayType__TimeAssignment_2 ) ) ;
    public final void rule__DelayType__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1324:1: ( ( ( rule__DelayType__TimeAssignment_2 ) ) )
            // InternalDft.g:1325:1: ( ( rule__DelayType__TimeAssignment_2 ) )
            {
            // InternalDft.g:1325:1: ( ( rule__DelayType__TimeAssignment_2 ) )
            // InternalDft.g:1326:2: ( rule__DelayType__TimeAssignment_2 )
            {
             before(grammarAccess.getDelayTypeAccess().getTimeAssignment_2()); 
            // InternalDft.g:1327:2: ( rule__DelayType__TimeAssignment_2 )
            // InternalDft.g:1327:3: rule__DelayType__TimeAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__DelayType__TimeAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getDelayTypeAccess().getTimeAssignment_2()); 

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
    // $ANTLR end "rule__DelayType__Group__2__Impl"


    // $ANTLR start "rule__Float__Group__0"
    // InternalDft.g:1336:1: rule__Float__Group__0 : rule__Float__Group__0__Impl rule__Float__Group__1 ;
    public final void rule__Float__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1340:1: ( rule__Float__Group__0__Impl rule__Float__Group__1 )
            // InternalDft.g:1341:2: rule__Float__Group__0__Impl rule__Float__Group__1
            {
            pushFollow(FOLLOW_9);
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
    // InternalDft.g:1348:1: rule__Float__Group__0__Impl : ( ( '-' )? ) ;
    public final void rule__Float__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1352:1: ( ( ( '-' )? ) )
            // InternalDft.g:1353:1: ( ( '-' )? )
            {
            // InternalDft.g:1353:1: ( ( '-' )? )
            // InternalDft.g:1354:2: ( '-' )?
            {
             before(grammarAccess.getFloatAccess().getHyphenMinusKeyword_0()); 
            // InternalDft.g:1355:2: ( '-' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==36) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // InternalDft.g:1355:3: '-'
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
    // InternalDft.g:1363:1: rule__Float__Group__1 : rule__Float__Group__1__Impl rule__Float__Group__2 ;
    public final void rule__Float__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1367:1: ( rule__Float__Group__1__Impl rule__Float__Group__2 )
            // InternalDft.g:1368:2: rule__Float__Group__1__Impl rule__Float__Group__2
            {
            pushFollow(FOLLOW_14);
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
    // InternalDft.g:1375:1: rule__Float__Group__1__Impl : ( RULE_INT ) ;
    public final void rule__Float__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1379:1: ( ( RULE_INT ) )
            // InternalDft.g:1380:1: ( RULE_INT )
            {
            // InternalDft.g:1380:1: ( RULE_INT )
            // InternalDft.g:1381:2: RULE_INT
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
    // InternalDft.g:1390:1: rule__Float__Group__2 : rule__Float__Group__2__Impl rule__Float__Group__3 ;
    public final void rule__Float__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1394:1: ( rule__Float__Group__2__Impl rule__Float__Group__3 )
            // InternalDft.g:1395:2: rule__Float__Group__2__Impl rule__Float__Group__3
            {
            pushFollow(FOLLOW_14);
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
    // InternalDft.g:1402:1: rule__Float__Group__2__Impl : ( ( rule__Float__Group_2__0 )? ) ;
    public final void rule__Float__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1406:1: ( ( ( rule__Float__Group_2__0 )? ) )
            // InternalDft.g:1407:1: ( ( rule__Float__Group_2__0 )? )
            {
            // InternalDft.g:1407:1: ( ( rule__Float__Group_2__0 )? )
            // InternalDft.g:1408:2: ( rule__Float__Group_2__0 )?
            {
             before(grammarAccess.getFloatAccess().getGroup_2()); 
            // InternalDft.g:1409:2: ( rule__Float__Group_2__0 )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==37) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // InternalDft.g:1409:3: rule__Float__Group_2__0
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
    // InternalDft.g:1417:1: rule__Float__Group__3 : rule__Float__Group__3__Impl ;
    public final void rule__Float__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1421:1: ( rule__Float__Group__3__Impl )
            // InternalDft.g:1422:2: rule__Float__Group__3__Impl
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
    // InternalDft.g:1428:1: rule__Float__Group__3__Impl : ( ( rule__Float__Group_3__0 )? ) ;
    public final void rule__Float__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1432:1: ( ( ( rule__Float__Group_3__0 )? ) )
            // InternalDft.g:1433:1: ( ( rule__Float__Group_3__0 )? )
            {
            // InternalDft.g:1433:1: ( ( rule__Float__Group_3__0 )? )
            // InternalDft.g:1434:2: ( rule__Float__Group_3__0 )?
            {
             before(grammarAccess.getFloatAccess().getGroup_3()); 
            // InternalDft.g:1435:2: ( rule__Float__Group_3__0 )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==38) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalDft.g:1435:3: rule__Float__Group_3__0
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
    // InternalDft.g:1444:1: rule__Float__Group_2__0 : rule__Float__Group_2__0__Impl rule__Float__Group_2__1 ;
    public final void rule__Float__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1448:1: ( rule__Float__Group_2__0__Impl rule__Float__Group_2__1 )
            // InternalDft.g:1449:2: rule__Float__Group_2__0__Impl rule__Float__Group_2__1
            {
            pushFollow(FOLLOW_15);
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
    // InternalDft.g:1456:1: rule__Float__Group_2__0__Impl : ( '.' ) ;
    public final void rule__Float__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1460:1: ( ( '.' ) )
            // InternalDft.g:1461:1: ( '.' )
            {
            // InternalDft.g:1461:1: ( '.' )
            // InternalDft.g:1462:2: '.'
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
    // InternalDft.g:1471:1: rule__Float__Group_2__1 : rule__Float__Group_2__1__Impl ;
    public final void rule__Float__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1475:1: ( rule__Float__Group_2__1__Impl )
            // InternalDft.g:1476:2: rule__Float__Group_2__1__Impl
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
    // InternalDft.g:1482:1: rule__Float__Group_2__1__Impl : ( RULE_INT ) ;
    public final void rule__Float__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1486:1: ( ( RULE_INT ) )
            // InternalDft.g:1487:1: ( RULE_INT )
            {
            // InternalDft.g:1487:1: ( RULE_INT )
            // InternalDft.g:1488:2: RULE_INT
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
    // InternalDft.g:1498:1: rule__Float__Group_3__0 : rule__Float__Group_3__0__Impl rule__Float__Group_3__1 ;
    public final void rule__Float__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1502:1: ( rule__Float__Group_3__0__Impl rule__Float__Group_3__1 )
            // InternalDft.g:1503:2: rule__Float__Group_3__0__Impl rule__Float__Group_3__1
            {
            pushFollow(FOLLOW_9);
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
    // InternalDft.g:1510:1: rule__Float__Group_3__0__Impl : ( 'e' ) ;
    public final void rule__Float__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1514:1: ( ( 'e' ) )
            // InternalDft.g:1515:1: ( 'e' )
            {
            // InternalDft.g:1515:1: ( 'e' )
            // InternalDft.g:1516:2: 'e'
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
    // InternalDft.g:1525:1: rule__Float__Group_3__1 : rule__Float__Group_3__1__Impl rule__Float__Group_3__2 ;
    public final void rule__Float__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1529:1: ( rule__Float__Group_3__1__Impl rule__Float__Group_3__2 )
            // InternalDft.g:1530:2: rule__Float__Group_3__1__Impl rule__Float__Group_3__2
            {
            pushFollow(FOLLOW_9);
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
    // InternalDft.g:1537:1: rule__Float__Group_3__1__Impl : ( ( '-' )? ) ;
    public final void rule__Float__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1541:1: ( ( ( '-' )? ) )
            // InternalDft.g:1542:1: ( ( '-' )? )
            {
            // InternalDft.g:1542:1: ( ( '-' )? )
            // InternalDft.g:1543:2: ( '-' )?
            {
             before(grammarAccess.getFloatAccess().getHyphenMinusKeyword_3_1()); 
            // InternalDft.g:1544:2: ( '-' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==36) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // InternalDft.g:1544:3: '-'
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
    // InternalDft.g:1552:1: rule__Float__Group_3__2 : rule__Float__Group_3__2__Impl ;
    public final void rule__Float__Group_3__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1556:1: ( rule__Float__Group_3__2__Impl )
            // InternalDft.g:1557:2: rule__Float__Group_3__2__Impl
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
    // InternalDft.g:1563:1: rule__Float__Group_3__2__Impl : ( RULE_INT ) ;
    public final void rule__Float__Group_3__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1567:1: ( ( RULE_INT ) )
            // InternalDft.g:1568:1: ( RULE_INT )
            {
            // InternalDft.g:1568:1: ( RULE_INT )
            // InternalDft.g:1569:2: RULE_INT
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
    // InternalDft.g:1579:1: rule__GalileoDft__RootAssignment_1 : ( ( RULE_STRING ) ) ;
    public final void rule__GalileoDft__RootAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1583:1: ( ( ( RULE_STRING ) ) )
            // InternalDft.g:1584:2: ( ( RULE_STRING ) )
            {
            // InternalDft.g:1584:2: ( ( RULE_STRING ) )
            // InternalDft.g:1585:3: ( RULE_STRING )
            {
             before(grammarAccess.getGalileoDftAccess().getRootGalileoFaultTreeNodeCrossReference_1_0()); 
            // InternalDft.g:1586:3: ( RULE_STRING )
            // InternalDft.g:1587:4: RULE_STRING
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
    // InternalDft.g:1598:1: rule__GalileoDft__GatesAssignment_3_0_0 : ( ruleGalileoGate ) ;
    public final void rule__GalileoDft__GatesAssignment_3_0_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1602:1: ( ( ruleGalileoGate ) )
            // InternalDft.g:1603:2: ( ruleGalileoGate )
            {
            // InternalDft.g:1603:2: ( ruleGalileoGate )
            // InternalDft.g:1604:3: ruleGalileoGate
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
    // InternalDft.g:1613:1: rule__GalileoDft__BasicEventsAssignment_3_1_0 : ( ruleGalileoBasicEvent ) ;
    public final void rule__GalileoDft__BasicEventsAssignment_3_1_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1617:1: ( ( ruleGalileoBasicEvent ) )
            // InternalDft.g:1618:2: ( ruleGalileoBasicEvent )
            {
            // InternalDft.g:1618:2: ( ruleGalileoBasicEvent )
            // InternalDft.g:1619:3: ruleGalileoBasicEvent
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
    // InternalDft.g:1628:1: rule__GalileoGate__NameAssignment_0 : ( RULE_STRING ) ;
    public final void rule__GalileoGate__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1632:1: ( ( RULE_STRING ) )
            // InternalDft.g:1633:2: ( RULE_STRING )
            {
            // InternalDft.g:1633:2: ( RULE_STRING )
            // InternalDft.g:1634:3: RULE_STRING
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
    // InternalDft.g:1643:1: rule__GalileoGate__TypeAssignment_1 : ( ruleGalileoNodeType ) ;
    public final void rule__GalileoGate__TypeAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1647:1: ( ( ruleGalileoNodeType ) )
            // InternalDft.g:1648:2: ( ruleGalileoNodeType )
            {
            // InternalDft.g:1648:2: ( ruleGalileoNodeType )
            // InternalDft.g:1649:3: ruleGalileoNodeType
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
    // InternalDft.g:1658:1: rule__GalileoGate__ChildrenAssignment_2 : ( ( RULE_STRING ) ) ;
    public final void rule__GalileoGate__ChildrenAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1662:1: ( ( ( RULE_STRING ) ) )
            // InternalDft.g:1663:2: ( ( RULE_STRING ) )
            {
            // InternalDft.g:1663:2: ( ( RULE_STRING ) )
            // InternalDft.g:1664:3: ( RULE_STRING )
            {
             before(grammarAccess.getGalileoGateAccess().getChildrenGalileoFaultTreeNodeCrossReference_2_0()); 
            // InternalDft.g:1665:3: ( RULE_STRING )
            // InternalDft.g:1666:4: RULE_STRING
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
    // InternalDft.g:1677:1: rule__GalileoBasicEvent__NameAssignment_0 : ( RULE_STRING ) ;
    public final void rule__GalileoBasicEvent__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1681:1: ( ( RULE_STRING ) )
            // InternalDft.g:1682:2: ( RULE_STRING )
            {
            // InternalDft.g:1682:2: ( RULE_STRING )
            // InternalDft.g:1683:3: RULE_STRING
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


    // $ANTLR start "rule__GalileoBasicEvent__LambdaAssignment_3"
    // InternalDft.g:1692:1: rule__GalileoBasicEvent__LambdaAssignment_3 : ( ruleFloat ) ;
    public final void rule__GalileoBasicEvent__LambdaAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1696:1: ( ( ruleFloat ) )
            // InternalDft.g:1697:2: ( ruleFloat )
            {
            // InternalDft.g:1697:2: ( ruleFloat )
            // InternalDft.g:1698:3: ruleFloat
            {
             before(grammarAccess.getGalileoBasicEventAccess().getLambdaFloatParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getGalileoBasicEventAccess().getLambdaFloatParserRuleCall_3_0()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__LambdaAssignment_3"


    // $ANTLR start "rule__GalileoBasicEvent__DormAssignment_4_2"
    // InternalDft.g:1707:1: rule__GalileoBasicEvent__DormAssignment_4_2 : ( ruleFloat ) ;
    public final void rule__GalileoBasicEvent__DormAssignment_4_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1711:1: ( ( ruleFloat ) )
            // InternalDft.g:1712:2: ( ruleFloat )
            {
            // InternalDft.g:1712:2: ( ruleFloat )
            // InternalDft.g:1713:3: ruleFloat
            {
             before(grammarAccess.getGalileoBasicEventAccess().getDormFloatParserRuleCall_4_2_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getGalileoBasicEventAccess().getDormFloatParserRuleCall_4_2_0()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__DormAssignment_4_2"


    // $ANTLR start "rule__GalileoBasicEvent__RepairAssignment_5_2"
    // InternalDft.g:1722:1: rule__GalileoBasicEvent__RepairAssignment_5_2 : ( ruleFloat ) ;
    public final void rule__GalileoBasicEvent__RepairAssignment_5_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1726:1: ( ( ruleFloat ) )
            // InternalDft.g:1727:2: ( ruleFloat )
            {
            // InternalDft.g:1727:2: ( ruleFloat )
            // InternalDft.g:1728:3: ruleFloat
            {
             before(grammarAccess.getGalileoBasicEventAccess().getRepairFloatParserRuleCall_5_2_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getGalileoBasicEventAccess().getRepairFloatParserRuleCall_5_2_0()); 

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
    // $ANTLR end "rule__GalileoBasicEvent__RepairAssignment_5_2"


    // $ANTLR start "rule__NamedType__TypeNameAssignment"
    // InternalDft.g:1737:1: rule__NamedType__TypeNameAssignment : ( ( rule__NamedType__TypeNameAlternatives_0 ) ) ;
    public final void rule__NamedType__TypeNameAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1741:1: ( ( ( rule__NamedType__TypeNameAlternatives_0 ) ) )
            // InternalDft.g:1742:2: ( ( rule__NamedType__TypeNameAlternatives_0 ) )
            {
            // InternalDft.g:1742:2: ( ( rule__NamedType__TypeNameAlternatives_0 ) )
            // InternalDft.g:1743:3: ( rule__NamedType__TypeNameAlternatives_0 )
            {
             before(grammarAccess.getNamedTypeAccess().getTypeNameAlternatives_0()); 
            // InternalDft.g:1744:3: ( rule__NamedType__TypeNameAlternatives_0 )
            // InternalDft.g:1744:4: rule__NamedType__TypeNameAlternatives_0
            {
            pushFollow(FOLLOW_2);
            rule__NamedType__TypeNameAlternatives_0();

            state._fsp--;


            }

             after(grammarAccess.getNamedTypeAccess().getTypeNameAlternatives_0()); 

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
    // $ANTLR end "rule__NamedType__TypeNameAssignment"


    // $ANTLR start "rule__ObserverType__ObservablesAssignment_1"
    // InternalDft.g:1752:1: rule__ObserverType__ObservablesAssignment_1 : ( ( RULE_STRING ) ) ;
    public final void rule__ObserverType__ObservablesAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1756:1: ( ( ( RULE_STRING ) ) )
            // InternalDft.g:1757:2: ( ( RULE_STRING ) )
            {
            // InternalDft.g:1757:2: ( ( RULE_STRING ) )
            // InternalDft.g:1758:3: ( RULE_STRING )
            {
             before(grammarAccess.getObserverTypeAccess().getObservablesGalileoFaultTreeNodeCrossReference_1_0()); 
            // InternalDft.g:1759:3: ( RULE_STRING )
            // InternalDft.g:1760:4: RULE_STRING
            {
             before(grammarAccess.getObserverTypeAccess().getObservablesGalileoFaultTreeNodeSTRINGTerminalRuleCall_1_0_1()); 
            match(input,RULE_STRING,FOLLOW_2); 
             after(grammarAccess.getObserverTypeAccess().getObservablesGalileoFaultTreeNodeSTRINGTerminalRuleCall_1_0_1()); 

            }

             after(grammarAccess.getObserverTypeAccess().getObservablesGalileoFaultTreeNodeCrossReference_1_0()); 

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
    // $ANTLR end "rule__ObserverType__ObservablesAssignment_1"


    // $ANTLR start "rule__ObserverType__ObservationRateAssignment_4"
    // InternalDft.g:1771:1: rule__ObserverType__ObservationRateAssignment_4 : ( ruleFloat ) ;
    public final void rule__ObserverType__ObservationRateAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1775:1: ( ( ruleFloat ) )
            // InternalDft.g:1776:2: ( ruleFloat )
            {
            // InternalDft.g:1776:2: ( ruleFloat )
            // InternalDft.g:1777:3: ruleFloat
            {
             before(grammarAccess.getObserverTypeAccess().getObservationRateFloatParserRuleCall_4_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getObserverTypeAccess().getObservationRateFloatParserRuleCall_4_0()); 

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
    // $ANTLR end "rule__ObserverType__ObservationRateAssignment_4"


    // $ANTLR start "rule__RDEPType__RateFactorAssignment_2"
    // InternalDft.g:1786:1: rule__RDEPType__RateFactorAssignment_2 : ( ruleFloat ) ;
    public final void rule__RDEPType__RateFactorAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1790:1: ( ( ruleFloat ) )
            // InternalDft.g:1791:2: ( ruleFloat )
            {
            // InternalDft.g:1791:2: ( ruleFloat )
            // InternalDft.g:1792:3: ruleFloat
            {
             before(grammarAccess.getRDEPTypeAccess().getRateFactorFloatParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getRDEPTypeAccess().getRateFactorFloatParserRuleCall_2_0()); 

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
    // $ANTLR end "rule__RDEPType__RateFactorAssignment_2"


    // $ANTLR start "rule__DelayType__TimeAssignment_2"
    // InternalDft.g:1801:1: rule__DelayType__TimeAssignment_2 : ( ruleFloat ) ;
    public final void rule__DelayType__TimeAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalDft.g:1805:1: ( ( ruleFloat ) )
            // InternalDft.g:1806:2: ( ruleFloat )
            {
            // InternalDft.g:1806:2: ( ruleFloat )
            // InternalDft.g:1807:3: ruleFloat
            {
             before(grammarAccess.getDelayTypeAccess().getTimeFloatParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleFloat();

            state._fsp--;

             after(grammarAccess.getDelayTypeAccess().getTimeFloatParserRuleCall_2_0()); 

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
    // $ANTLR end "rule__DelayType__TimeAssignment_2"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000540FFF010L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000001000000020L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000030000000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000080000040L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000006000000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000000020L});

}