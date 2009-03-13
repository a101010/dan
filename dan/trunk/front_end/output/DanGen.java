// $ANTLR 3.1.1 C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g 2009-02-10 16:06:36

import java.util.HashMap;
import dan.types.*;
import dan.system.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
import java.util.HashMap;
public class DanGen extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "PROGRAM", "IMPORTS", "BUNDLE_TYPEDEF", "BUNDLE_CHANNELS", "BUNDLE_PROTOCOL", "CHAN_DEF", "ALL", "ADORNMENTS", "DECLARATION", "PARAMLIST", "PARAM", "BLOCK", "VARDEC", "CALL", "ARGLIST", "EXP", "ID", "STMT_END", "BLOCK_BEGIN", "BLOCK_END", "FLOAT_LIT", "INT_LIT", "SIMPLE_ID", "NEXT_ID", "COMMENT", "WS", "'import'", "'from'", "'bundle'", "'channel'", "'->'", "'<-'", "'['", "']'", "'proc'", "'('", "')'", "','", "'while'", "'if'", "'cif'", "'par'", "'succ'", "'='", "'!'", "'?'", "'return'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'or'", "'and'", "'xor'", "'+'", "'-'", "'*'", "'/'", "'not'", "'true'", "'false'"
    };
    public static final int T__66=66;
    public static final int BUNDLE_CHANNELS=7;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int INT_LIT=25;
    public static final int NEXT_ID=27;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int SIMPLE_ID=26;
    public static final int PARAM=14;
    public static final int ARGLIST=18;
    public static final int T__61=61;
    public static final int ID=20;
    public static final int T__60=60;
    public static final int EOF=-1;
    public static final int DECLARATION=12;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int ADORNMENTS=11;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int CHAN_DEF=9;
    public static final int T__54=54;
    public static final int EXP=19;
    public static final int FLOAT_LIT=24;
    public static final int T__59=59;
    public static final int ALL=10;
    public static final int BLOCK_END=23;
    public static final int COMMENT=28;
    public static final int BLOCK_BEGIN=22;
    public static final int T__50=50;
    public static final int VARDEC=16;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int PARAMLIST=13;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int WS=29;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int STMT_END=21;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int BLOCK=15;
    public static final int PROGRAM=4;
    public static final int IMPORTS=5;
    public static final int CALL=17;
    public static final int BUNDLE_TYPEDEF=6;
    public static final int BUNDLE_PROTOCOL=8;

    // delegates
    // delegators


        public DanGen(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public DanGen(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected StringTemplateGroup templateLib =
      new StringTemplateGroup("DanGenTemplates", AngleBracketTemplateLexer.class);

    public void setTemplateLib(StringTemplateGroup templateLib) {
      this.templateLib = templateLib;
    }
    public StringTemplateGroup getTemplateLib() {
      return templateLib;
    }
    /** allows convenient multi-value initialization:
     *  "new STAttrMap().put(...).put(...)"
     */
    public static class STAttrMap extends HashMap {
      public STAttrMap put(String attrName, Object value) {
        super.put(attrName, value);
        return this;
      }
      public STAttrMap put(String attrName, int value) {
        super.put(attrName, new Integer(value));
        return this;
      }
    }

    public String[] getTokenNames() { return DanGen.tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g"; }



    	


    public static class prog_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "prog"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:26:1: prog : imports decs ;
    public final DanGen.prog_return prog() throws RecognitionException {
        DanGen.prog_return retval = new DanGen.prog_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:26:7: ( imports decs )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:26:9: imports decs
            {
            pushFollow(FOLLOW_imports_in_prog50);
            imports();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_decs_in_prog52);
            decs();

            state._fsp--;
            if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "prog"

    public static class imports_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "imports"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:29:1: imports : ^( IMPORTS ( importStmt )* ) ;
    public final DanGen.imports_return imports() throws RecognitionException {
        DanGen.imports_return retval = new DanGen.imports_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:29:10: ( ^( IMPORTS ( importStmt )* ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:29:12: ^( IMPORTS ( importStmt )* )
            {
            match(input,IMPORTS,FOLLOW_IMPORTS_in_imports63); if (state.failed) return retval;

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); if (state.failed) return retval;
                // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:29:22: ( importStmt )*
                loop1:
                do {
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==30) ) {
                        alt1=1;
                    }


                    switch (alt1) {
                	case 1 :
                	    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:29:22: importStmt
                	    {
                	    pushFollow(FOLLOW_importStmt_in_imports65);
                	    importStmt();

                	    state._fsp--;
                	    if (state.failed) return retval;

                	    }
                	    break;

                	default :
                	    break loop1;
                    }
                } while (true);


                match(input, Token.UP, null); if (state.failed) return retval;
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "imports"

    public static class importStmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "importStmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:31:1: importStmt : ( ^( 'import' library= ID symbol= ID ) | ^( 'import' library= ID ALL ) );
    public final DanGen.importStmt_return importStmt() throws RecognitionException {
        DanGen.importStmt_return retval = new DanGen.importStmt_return();
        retval.start = input.LT(1);

        CommonTree library=null;
        CommonTree symbol=null;

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:31:13: ( ^( 'import' library= ID symbol= ID ) | ^( 'import' library= ID ALL ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==30) ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1==DOWN) ) {
                    int LA2_2 = input.LA(3);

                    if ( (LA2_2==ID) ) {
                        int LA2_3 = input.LA(4);

                        if ( (LA2_3==ID) ) {
                            alt2=1;
                        }
                        else if ( (LA2_3==ALL) ) {
                            alt2=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 2, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:31:15: ^( 'import' library= ID symbol= ID )
                    {
                    match(input,30,FOLLOW_30_in_importStmt81); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    library=(CommonTree)match(input,ID,FOLLOW_ID_in_importStmt85); if (state.failed) return retval;
                    symbol=(CommonTree)match(input,ID,FOLLOW_ID_in_importStmt89); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:32:5: ^( 'import' library= ID ALL )
                    {
                    match(input,30,FOLLOW_30_in_importStmt97); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    library=(CommonTree)match(input,ID,FOLLOW_ID_in_importStmt101); if (state.failed) return retval;
                    match(input,ALL,FOLLOW_ALL_in_importStmt103); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "importStmt"

    public static class decs_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "decs"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:34:1: decs : ( declaration )+ ;
    public final DanGen.decs_return decs() throws RecognitionException {
        DanGen.decs_return retval = new DanGen.decs_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:34:8: ( ( declaration )+ )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:34:10: ( declaration )+
            {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:34:10: ( declaration )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==DECLARATION) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:34:10: declaration
            	    {
            	    pushFollow(FOLLOW_declaration_in_decs116);
            	    declaration();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "decs"

    public static class declaration_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "declaration"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:36:1: declaration : ( ^( DECLARATION decChoice ) | ^( DECLARATION attribAdorn decChoice ) );
    public final DanGen.declaration_return declaration() throws RecognitionException {
        DanGen.declaration_return retval = new DanGen.declaration_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:36:13: ( ^( DECLARATION decChoice ) | ^( DECLARATION attribAdorn decChoice ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==DECLARATION) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==DOWN) ) {
                    int LA4_2 = input.LA(3);

                    if ( (LA4_2==ADORNMENTS) ) {
                        alt4=2;
                    }
                    else if ( (LA4_2==32||LA4_2==38) ) {
                        alt4=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:36:15: ^( DECLARATION decChoice )
                    {
                    match(input,DECLARATION,FOLLOW_DECLARATION_in_declaration142); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_decChoice_in_declaration144);
                    decChoice();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:37:5: ^( DECLARATION attribAdorn decChoice )
                    {
                    match(input,DECLARATION,FOLLOW_DECLARATION_in_declaration153); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_attribAdorn_in_declaration155);
                    attribAdorn();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_decChoice_in_declaration157);
                    decChoice();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "declaration"

    public static class decChoice_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "decChoice"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:39:1: decChoice : ( procDec | bundleDec );
    public final DanGen.decChoice_return decChoice() throws RecognitionException {
        DanGen.decChoice_return retval = new DanGen.decChoice_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:39:12: ( procDec | bundleDec )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==38) ) {
                alt5=1;
            }
            else if ( (LA5_0==32) ) {
                alt5=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:39:14: procDec
                    {
                    pushFollow(FOLLOW_procDec_in_decChoice171);
                    procDec();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:39:24: bundleDec
                    {
                    pushFollow(FOLLOW_bundleDec_in_decChoice175);
                    bundleDec();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "decChoice"

    public static class bundleDec_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "bundleDec"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:41:1: bundleDec : ^( 'bundle' ID bundle_body ) ;
    public final DanGen.bundleDec_return bundleDec() throws RecognitionException {
        DanGen.bundleDec_return retval = new DanGen.bundleDec_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:41:12: ( ^( 'bundle' ID bundle_body ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:41:14: ^( 'bundle' ID bundle_body )
            {
            match(input,32,FOLLOW_32_in_bundleDec185); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            match(input,ID,FOLLOW_ID_in_bundleDec187); if (state.failed) return retval;
            pushFollow(FOLLOW_bundle_body_in_bundleDec189);
            bundle_body();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "bundleDec"

    public static class bundle_body_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "bundle_body"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:43:1: bundle_body : ^( BUNDLE_CHANNELS ( channel_dec )+ ) ;
    public final DanGen.bundle_body_return bundle_body() throws RecognitionException {
        DanGen.bundle_body_return retval = new DanGen.bundle_body_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:43:14: ( ^( BUNDLE_CHANNELS ( channel_dec )+ ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:43:16: ^( BUNDLE_CHANNELS ( channel_dec )+ )
            {
            match(input,BUNDLE_CHANNELS,FOLLOW_BUNDLE_CHANNELS_in_bundle_body200); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:43:34: ( channel_dec )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==33) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:43:34: channel_dec
            	    {
            	    pushFollow(FOLLOW_channel_dec_in_bundle_body202);
            	    channel_dec();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "bundle_body"

    public static class channel_dec_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "channel_dec"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:45:1: channel_dec : ^( 'channel' proto_type= ID name= ID channel_dir ) ;
    public final DanGen.channel_dec_return channel_dec() throws RecognitionException {
        DanGen.channel_dec_return retval = new DanGen.channel_dec_return();
        retval.start = input.LT(1);

        CommonTree proto_type=null;
        CommonTree name=null;

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:45:14: ( ^( 'channel' proto_type= ID name= ID channel_dir ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:45:17: ^( 'channel' proto_type= ID name= ID channel_dir )
            {
            match(input,33,FOLLOW_33_in_channel_dec216); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            proto_type=(CommonTree)match(input,ID,FOLLOW_ID_in_channel_dec220); if (state.failed) return retval;
            name=(CommonTree)match(input,ID,FOLLOW_ID_in_channel_dec224); if (state.failed) return retval;
            pushFollow(FOLLOW_channel_dir_in_channel_dec226);
            channel_dir();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "channel_dec"

    public static class channel_dir_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "channel_dir"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:47:1: channel_dir : ( '->' | '<-' );
    public final DanGen.channel_dir_return channel_dir() throws RecognitionException {
        DanGen.channel_dir_return retval = new DanGen.channel_dir_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:47:14: ( '->' | '<-' )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:
            {
            if ( (input.LA(1)>=34 && input.LA(1)<=35) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "channel_dir"

    public static class attrib_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "attrib"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:49:1: attrib : ID ;
    public final DanGen.attrib_return attrib() throws RecognitionException {
        DanGen.attrib_return retval = new DanGen.attrib_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:49:10: ( ID )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:49:12: ID
            {
            match(input,ID,FOLLOW_ID_in_attrib251); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "attrib"

    public static class attribAdorn_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "attribAdorn"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:51:1: attribAdorn : ^( ADORNMENTS ( attrib )+ ) ;
    public final DanGen.attribAdorn_return attribAdorn() throws RecognitionException {
        DanGen.attribAdorn_return retval = new DanGen.attribAdorn_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:51:14: ( ^( ADORNMENTS ( attrib )+ ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:51:16: ^( ADORNMENTS ( attrib )+ )
            {
            match(input,ADORNMENTS,FOLLOW_ADORNMENTS_in_attribAdorn261); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:51:29: ( attrib )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==ID) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:51:29: attrib
            	    {
            	    pushFollow(FOLLOW_attrib_in_attribAdorn263);
            	    attrib();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "attribAdorn"

    public static class procDec_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "procDec"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:53:1: procDec : ^( 'proc' returnType= ID name= ID paramList block ) ;
    public final DanGen.procDec_return procDec() throws RecognitionException {
        DanGen.procDec_return retval = new DanGen.procDec_return();
        retval.start = input.LT(1);

        CommonTree returnType=null;
        CommonTree name=null;

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:53:10: ( ^( 'proc' returnType= ID name= ID paramList block ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:53:12: ^( 'proc' returnType= ID name= ID paramList block )
            {
            match(input,38,FOLLOW_38_in_procDec275); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            returnType=(CommonTree)match(input,ID,FOLLOW_ID_in_procDec279); if (state.failed) return retval;
            name=(CommonTree)match(input,ID,FOLLOW_ID_in_procDec283); if (state.failed) return retval;
            pushFollow(FOLLOW_paramList_in_procDec285);
            paramList();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_block_in_procDec287);
            block();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "procDec"

    public static class paramList_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "paramList"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:55:1: paramList : ^( PARAMLIST ( param )* ) ;
    public final DanGen.paramList_return paramList() throws RecognitionException {
        DanGen.paramList_return retval = new DanGen.paramList_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:55:12: ( ^( PARAMLIST ( param )* ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:55:14: ^( PARAMLIST ( param )* )
            {
            match(input,PARAMLIST,FOLLOW_PARAMLIST_in_paramList298); if (state.failed) return retval;

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); if (state.failed) return retval;
                // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:55:26: ( param )*
                loop8:
                do {
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==PARAM) ) {
                        alt8=1;
                    }


                    switch (alt8) {
                	case 1 :
                	    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:55:26: param
                	    {
                	    pushFollow(FOLLOW_param_in_paramList300);
                	    param();

                	    state._fsp--;
                	    if (state.failed) return retval;

                	    }
                	    break;

                	default :
                	    break loop8;
                    }
                } while (true);


                match(input, Token.UP, null); if (state.failed) return retval;
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "paramList"

    public static class param_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "param"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:57:1: param : ^( PARAM type= ID name= ID ) ;
    public final DanGen.param_return param() throws RecognitionException {
        DanGen.param_return retval = new DanGen.param_return();
        retval.start = input.LT(1);

        CommonTree type=null;
        CommonTree name=null;

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:57:9: ( ^( PARAM type= ID name= ID ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:57:11: ^( PARAM type= ID name= ID )
            {
            match(input,PARAM,FOLLOW_PARAM_in_param313); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            type=(CommonTree)match(input,ID,FOLLOW_ID_in_param317); if (state.failed) return retval;
            name=(CommonTree)match(input,ID,FOLLOW_ID_in_param321); if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "param"

    public static class statement_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "statement"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:1: statement : ( while_stmt | if_stmt | cif_stmt | par_stmt | succ_stmt | block | simple_statement ) ;
    public final DanGen.statement_return statement() throws RecognitionException {
        DanGen.statement_return retval = new DanGen.statement_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:12: ( ( while_stmt | if_stmt | cif_stmt | par_stmt | succ_stmt | block | simple_statement ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:14: ( while_stmt | if_stmt | cif_stmt | par_stmt | succ_stmt | block | simple_statement )
            {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:14: ( while_stmt | if_stmt | cif_stmt | par_stmt | succ_stmt | block | simple_statement )
            int alt9=7;
            switch ( input.LA(1) ) {
            case 42:
                {
                alt9=1;
                }
                break;
            case 43:
                {
                alt9=2;
                }
                break;
            case 44:
                {
                alt9=3;
                }
                break;
            case 45:
                {
                alt9=4;
                }
                break;
            case 46:
                {
                alt9=5;
                }
                break;
            case BLOCK:
                {
                alt9=6;
                }
                break;
            case VARDEC:
            case CALL:
            case 47:
            case 48:
            case 49:
            case 50:
                {
                alt9=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:15: while_stmt
                    {
                    pushFollow(FOLLOW_while_stmt_in_statement332);
                    while_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:28: if_stmt
                    {
                    pushFollow(FOLLOW_if_stmt_in_statement336);
                    if_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:38: cif_stmt
                    {
                    pushFollow(FOLLOW_cif_stmt_in_statement340);
                    cif_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:49: par_stmt
                    {
                    pushFollow(FOLLOW_par_stmt_in_statement344);
                    par_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:60: succ_stmt
                    {
                    pushFollow(FOLLOW_succ_stmt_in_statement348);
                    succ_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:72: block
                    {
                    pushFollow(FOLLOW_block_in_statement352);
                    block();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 7 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:59:80: simple_statement
                    {
                    pushFollow(FOLLOW_simple_statement_in_statement356);
                    simple_statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "statement"

    public static class simple_statement_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "simple_statement"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:61:1: simple_statement : ( vardec_stmt | send_stmt | receive_stmt | assign_stmt | return_stmt | call );
    public final DanGen.simple_statement_return simple_statement() throws RecognitionException {
        DanGen.simple_statement_return retval = new DanGen.simple_statement_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:62:3: ( vardec_stmt | send_stmt | receive_stmt | assign_stmt | return_stmt | call )
            int alt10=6;
            switch ( input.LA(1) ) {
            case VARDEC:
                {
                alt10=1;
                }
                break;
            case 48:
                {
                alt10=2;
                }
                break;
            case 49:
                {
                alt10=3;
                }
                break;
            case 47:
                {
                alt10=4;
                }
                break;
            case 50:
                {
                alt10=5;
                }
                break;
            case CALL:
                {
                alt10=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:62:5: vardec_stmt
                    {
                    pushFollow(FOLLOW_vardec_stmt_in_simple_statement368);
                    vardec_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:63:6: send_stmt
                    {
                    pushFollow(FOLLOW_send_stmt_in_simple_statement375);
                    send_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:64:6: receive_stmt
                    {
                    pushFollow(FOLLOW_receive_stmt_in_simple_statement382);
                    receive_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:65:6: assign_stmt
                    {
                    pushFollow(FOLLOW_assign_stmt_in_simple_statement389);
                    assign_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:66:6: return_stmt
                    {
                    pushFollow(FOLLOW_return_stmt_in_simple_statement396);
                    return_stmt();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:67:6: call
                    {
                    pushFollow(FOLLOW_call_in_simple_statement403);
                    call();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "simple_statement"

    public static class block_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "block"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:69:1: block : ^( BLOCK ( statement )+ ) ;
    public final DanGen.block_return block() throws RecognitionException {
        DanGen.block_return retval = new DanGen.block_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:69:9: ( ^( BLOCK ( statement )+ ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:69:11: ^( BLOCK ( statement )+ )
            {
            match(input,BLOCK,FOLLOW_BLOCK_in_block414); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:69:19: ( statement )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=BLOCK && LA11_0<=CALL)||(LA11_0>=42 && LA11_0<=50)) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:69:19: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_block416);
            	    statement();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "block"

    public static class while_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "while_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:71:1: while_stmt : ^( 'while' exp statement ) ;
    public final DanGen.while_stmt_return while_stmt() throws RecognitionException {
        DanGen.while_stmt_return retval = new DanGen.while_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:71:13: ( ^( 'while' exp statement ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:71:15: ^( 'while' exp statement )
            {
            match(input,42,FOLLOW_42_in_while_stmt428); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_exp_in_while_stmt430);
            exp();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_statement_in_while_stmt432);
            statement();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "while_stmt"

    public static class if_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "if_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:73:1: if_stmt : ^( 'if' exp statement ) ;
    public final DanGen.if_stmt_return if_stmt() throws RecognitionException {
        DanGen.if_stmt_return retval = new DanGen.if_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:73:10: ( ^( 'if' exp statement ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:73:12: ^( 'if' exp statement )
            {
            match(input,43,FOLLOW_43_in_if_stmt443); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_exp_in_if_stmt445);
            exp();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_statement_in_if_stmt447);
            statement();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "if_stmt"

    public static class cif_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "cif_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:75:1: cif_stmt : ^( 'cif' ID statement ) ;
    public final DanGen.cif_stmt_return cif_stmt() throws RecognitionException {
        DanGen.cif_stmt_return retval = new DanGen.cif_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:75:11: ( ^( 'cif' ID statement ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:75:13: ^( 'cif' ID statement )
            {
            match(input,44,FOLLOW_44_in_cif_stmt458); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            match(input,ID,FOLLOW_ID_in_cif_stmt460); if (state.failed) return retval;
            pushFollow(FOLLOW_statement_in_cif_stmt462);
            statement();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "cif_stmt"

    public static class par_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "par_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:77:1: par_stmt : ^( 'par' block ) ;
    public final DanGen.par_stmt_return par_stmt() throws RecognitionException {
        DanGen.par_stmt_return retval = new DanGen.par_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:77:10: ( ^( 'par' block ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:77:12: ^( 'par' block )
            {
            match(input,45,FOLLOW_45_in_par_stmt472); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_block_in_par_stmt474);
            block();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "par_stmt"

    public static class succ_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "succ_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:79:1: succ_stmt : ^( 'succ' block ) ;
    public final DanGen.succ_stmt_return succ_stmt() throws RecognitionException {
        DanGen.succ_stmt_return retval = new DanGen.succ_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:79:11: ( ^( 'succ' block ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:79:13: ^( 'succ' block )
            {
            match(input,46,FOLLOW_46_in_succ_stmt484); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_block_in_succ_stmt486);
            block();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "succ_stmt"

    public static class vardec_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "vardec_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:81:1: vardec_stmt : ^( VARDEC ID var_init ) ;
    public final DanGen.vardec_stmt_return vardec_stmt() throws RecognitionException {
        DanGen.vardec_stmt_return retval = new DanGen.vardec_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:81:14: ( ^( VARDEC ID var_init ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:81:16: ^( VARDEC ID var_init )
            {
            match(input,VARDEC,FOLLOW_VARDEC_in_vardec_stmt497); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            match(input,ID,FOLLOW_ID_in_vardec_stmt499); if (state.failed) return retval;
            pushFollow(FOLLOW_var_init_in_vardec_stmt501);
            var_init();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "vardec_stmt"

    public static class var_init_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "var_init"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:83:1: var_init : ( ID | ^( '=' ID exp ) );
    public final DanGen.var_init_return var_init() throws RecognitionException {
        DanGen.var_init_return retval = new DanGen.var_init_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:83:11: ( ID | ^( '=' ID exp ) )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ID) ) {
                alt12=1;
            }
            else if ( (LA12_0==47) ) {
                alt12=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:83:13: ID
                    {
                    match(input,ID,FOLLOW_ID_in_var_init511); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:84:5: ^( '=' ID exp )
                    {
                    match(input,47,FOLLOW_47_in_var_init519); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    match(input,ID,FOLLOW_ID_in_var_init521); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_var_init523);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "var_init"

    public static class send_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "send_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:86:1: send_stmt : ^( '!' ID exp ) ;
    public final DanGen.send_stmt_return send_stmt() throws RecognitionException {
        DanGen.send_stmt_return retval = new DanGen.send_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:86:12: ( ^( '!' ID exp ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:86:14: ^( '!' ID exp )
            {
            match(input,48,FOLLOW_48_in_send_stmt534); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            match(input,ID,FOLLOW_ID_in_send_stmt536); if (state.failed) return retval;
            pushFollow(FOLLOW_exp_in_send_stmt538);
            exp();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "send_stmt"

    public static class receive_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "receive_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:88:1: receive_stmt : ^( '?' from= ID target= ID ) ;
    public final DanGen.receive_stmt_return receive_stmt() throws RecognitionException {
        DanGen.receive_stmt_return retval = new DanGen.receive_stmt_return();
        retval.start = input.LT(1);

        CommonTree from=null;
        CommonTree target=null;

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:88:14: ( ^( '?' from= ID target= ID ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:88:16: ^( '?' from= ID target= ID )
            {
            match(input,49,FOLLOW_49_in_receive_stmt548); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            from=(CommonTree)match(input,ID,FOLLOW_ID_in_receive_stmt552); if (state.failed) return retval;
            target=(CommonTree)match(input,ID,FOLLOW_ID_in_receive_stmt556); if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "receive_stmt"

    public static class assign_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "assign_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:90:1: assign_stmt : ^( '=' ID exp ) ;
    public final DanGen.assign_stmt_return assign_stmt() throws RecognitionException {
        DanGen.assign_stmt_return retval = new DanGen.assign_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:90:14: ( ^( '=' ID exp ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:90:16: ^( '=' ID exp )
            {
            match(input,47,FOLLOW_47_in_assign_stmt567); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            match(input,ID,FOLLOW_ID_in_assign_stmt569); if (state.failed) return retval;
            pushFollow(FOLLOW_exp_in_assign_stmt571);
            exp();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "assign_stmt"

    public static class return_stmt_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "return_stmt"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:92:1: return_stmt : ^( 'return' exp ) ;
    public final DanGen.return_stmt_return return_stmt() throws RecognitionException {
        DanGen.return_stmt_return retval = new DanGen.return_stmt_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:92:13: ( ^( 'return' exp ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:92:15: ^( 'return' exp )
            {
            match(input,50,FOLLOW_50_in_return_stmt581); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_exp_in_return_stmt583);
            exp();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "return_stmt"

    public static class exp_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "exp"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:95:1: exp : ( literal | ID | call | ^( '<' exp exp ) | ^( '>' exp exp ) | ^( '<=' exp exp ) | ^( '>=' exp exp ) | ^( '==' exp exp ) | ^( '!=' exp exp ) | ^( 'or' exp exp ) | ^( 'and' exp exp ) | ^( 'xor' exp exp ) | ( ^( '+' exp exp ) )=> ^( '+' exp exp ) | ( ^( '-' exp exp ) )=> ^( '-' exp exp ) | ^( '*' exp exp ) | ^( '/' exp exp ) | ^( 'not' exp ) | ^( '+' exp ) | ^( '-' exp ) );
    public final DanGen.exp_return exp() throws RecognitionException {
        DanGen.exp_return retval = new DanGen.exp_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:95:7: ( literal | ID | call | ^( '<' exp exp ) | ^( '>' exp exp ) | ^( '<=' exp exp ) | ^( '>=' exp exp ) | ^( '==' exp exp ) | ^( '!=' exp exp ) | ^( 'or' exp exp ) | ^( 'and' exp exp ) | ^( 'xor' exp exp ) | ( ^( '+' exp exp ) )=> ^( '+' exp exp ) | ( ^( '-' exp exp ) )=> ^( '-' exp exp ) | ^( '*' exp exp ) | ^( '/' exp exp ) | ^( 'not' exp ) | ^( '+' exp ) | ^( '-' exp ) )
            int alt13=19;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:95:9: literal
                    {
                    pushFollow(FOLLOW_literal_in_exp595);
                    literal();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:96:5: ID
                    {
                    match(input,ID,FOLLOW_ID_in_exp601); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:97:5: call
                    {
                    pushFollow(FOLLOW_call_in_exp607);
                    call();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:98:5: ^( '<' exp exp )
                    {
                    match(input,51,FOLLOW_51_in_exp614); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp616);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp618);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:99:5: ^( '>' exp exp )
                    {
                    match(input,52,FOLLOW_52_in_exp627); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp629);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp631);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:100:5: ^( '<=' exp exp )
                    {
                    match(input,53,FOLLOW_53_in_exp639); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp641);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp643);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 7 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:101:5: ^( '>=' exp exp )
                    {
                    match(input,54,FOLLOW_54_in_exp652); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp654);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp656);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 8 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:102:5: ^( '==' exp exp )
                    {
                    match(input,55,FOLLOW_55_in_exp664); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp666);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp668);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 9 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:103:5: ^( '!=' exp exp )
                    {
                    match(input,56,FOLLOW_56_in_exp676); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp678);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp680);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 10 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:104:5: ^( 'or' exp exp )
                    {
                    match(input,57,FOLLOW_57_in_exp688); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp690);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp692);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 11 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:105:5: ^( 'and' exp exp )
                    {
                    match(input,58,FOLLOW_58_in_exp700); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp702);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp704);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 12 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:106:5: ^( 'xor' exp exp )
                    {
                    match(input,59,FOLLOW_59_in_exp712); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp714);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp716);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 13 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:107:5: ( ^( '+' exp exp ) )=> ^( '+' exp exp )
                    {
                    match(input,60,FOLLOW_60_in_exp735); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp737);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp739);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 14 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:108:5: ( ^( '-' exp exp ) )=> ^( '-' exp exp )
                    {
                    match(input,61,FOLLOW_61_in_exp758); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp760);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp762);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 15 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:109:5: ^( '*' exp exp )
                    {
                    match(input,62,FOLLOW_62_in_exp770); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp772);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp774);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 16 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:110:5: ^( '/' exp exp )
                    {
                    match(input,63,FOLLOW_63_in_exp782); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp784);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp786);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 17 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:111:5: ^( 'not' exp )
                    {
                    match(input,64,FOLLOW_64_in_exp794); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp796);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 18 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:112:5: ^( '+' exp )
                    {
                    match(input,60,FOLLOW_60_in_exp804); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp806);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;
                case 19 :
                    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:113:5: ^( '-' exp )
                    {
                    match(input,61,FOLLOW_61_in_exp814); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_exp_in_exp816);
                    exp();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "exp"

    public static class call_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "call"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:115:1: call : ^( CALL ID arg_list ) ;
    public final DanGen.call_return call() throws RecognitionException {
        DanGen.call_return retval = new DanGen.call_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:115:7: ( ^( CALL ID arg_list ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:115:9: ^( CALL ID arg_list )
            {
            match(input,CALL,FOLLOW_CALL_in_call829); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            match(input,ID,FOLLOW_ID_in_call831); if (state.failed) return retval;
            pushFollow(FOLLOW_arg_list_in_call833);
            arg_list();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "call"

    public static class arg_list_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "arg_list"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:117:1: arg_list : ^( ARGLIST ( exp )+ ) ;
    public final DanGen.arg_list_return arg_list() throws RecognitionException {
        DanGen.arg_list_return retval = new DanGen.arg_list_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:117:11: ( ^( ARGLIST ( exp )+ ) )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:117:13: ^( ARGLIST ( exp )+ )
            {
            match(input,ARGLIST,FOLLOW_ARGLIST_in_arg_list844); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:117:23: ( exp )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==CALL||LA14_0==ID||(LA14_0>=FLOAT_LIT && LA14_0<=INT_LIT)||(LA14_0>=51 && LA14_0<=66)) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:117:23: exp
            	    {
            	    pushFollow(FOLLOW_exp_in_arg_list846);
            	    exp();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    if ( cnt14 >= 1 ) break loop14;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "arg_list"

    public static class literal_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "literal"
    // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:119:1: literal : ( 'true' | 'false' | FLOAT_LIT | INT_LIT );
    public final DanGen.literal_return literal() throws RecognitionException {
        DanGen.literal_return retval = new DanGen.literal_return();
        retval.start = input.LT(1);

        try {
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:119:10: ( 'true' | 'false' | FLOAT_LIT | INT_LIT )
            // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:
            {
            if ( (input.LA(1)>=FLOAT_LIT && input.LA(1)<=INT_LIT)||(input.LA(1)>=65 && input.LA(1)<=66) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "literal"

    // $ANTLR start synpred1_DanGen
    public final void synpred1_DanGen_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:107:5: ( ^( '+' exp exp ) )
        // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:107:6: ^( '+' exp exp )
        {
        match(input,60,FOLLOW_60_in_synpred1_DanGen725); if (state.failed) return ;

        match(input, Token.DOWN, null); if (state.failed) return ;
        pushFollow(FOLLOW_exp_in_synpred1_DanGen727);
        exp();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_exp_in_synpred1_DanGen729);
        exp();

        state._fsp--;
        if (state.failed) return ;

        match(input, Token.UP, null); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_DanGen

    // $ANTLR start synpred2_DanGen
    public final void synpred2_DanGen_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:108:5: ( ^( '-' exp exp ) )
        // C:\\Documents and Settings\\Alan\\My Documents\\Alan\\osource\\dan\\dan\\trunk\\front_end\\DanGen.g:108:6: ^( '-' exp exp )
        {
        match(input,61,FOLLOW_61_in_synpred2_DanGen748); if (state.failed) return ;

        match(input, Token.DOWN, null); if (state.failed) return ;
        pushFollow(FOLLOW_exp_in_synpred2_DanGen750);
        exp();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_exp_in_synpred2_DanGen752);
        exp();

        state._fsp--;
        if (state.failed) return ;

        match(input, Token.UP, null); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_DanGen

    // Delegated rules

    public final boolean synpred1_DanGen() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_DanGen_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_DanGen() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_DanGen_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA13_eotS =
        "\26\uffff";
    static final String DFA13_eofS =
        "\26\uffff";
    static final String DFA13_minS =
        "\1\21\14\uffff\2\0\7\uffff";
    static final String DFA13_maxS =
        "\1\102\14\uffff\2\0\7\uffff";
    static final String DFA13_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\2"+
        "\uffff\1\17\1\20\1\21\1\15\1\22\1\16\1\23";
    static final String DFA13_specialS =
        "\15\uffff\1\0\1\1\7\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\3\2\uffff\1\2\3\uffff\2\1\31\uffff\1\4\1\5\1\6\1\7\1\10"+
            "\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21\2\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "95:1: exp : ( literal | ID | call | ^( '<' exp exp ) | ^( '>' exp exp ) | ^( '<=' exp exp ) | ^( '>=' exp exp ) | ^( '==' exp exp ) | ^( '!=' exp exp ) | ^( 'or' exp exp ) | ^( 'and' exp exp ) | ^( 'xor' exp exp ) | ( ^( '+' exp exp ) )=> ^( '+' exp exp ) | ( ^( '-' exp exp ) )=> ^( '-' exp exp ) | ^( '*' exp exp ) | ^( '/' exp exp ) | ^( 'not' exp ) | ^( '+' exp ) | ^( '-' exp ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TreeNodeStream input = (TreeNodeStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA13_13 = input.LA(1);

                         
                        int index13_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred1_DanGen()) ) {s = 18;}

                        else if ( (true) ) {s = 19;}

                         
                        input.seek(index13_13);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA13_14 = input.LA(1);

                         
                        int index13_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_DanGen()) ) {s = 20;}

                        else if ( (true) ) {s = 21;}

                         
                        input.seek(index13_14);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 13, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_imports_in_prog50 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_decs_in_prog52 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORTS_in_imports63 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_importStmt_in_imports65 = new BitSet(new long[]{0x0000000040000008L});
    public static final BitSet FOLLOW_30_in_importStmt81 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_importStmt85 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_importStmt89 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_30_in_importStmt97 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_importStmt101 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_ALL_in_importStmt103 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_declaration_in_decs116 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_DECLARATION_in_declaration142 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_decChoice_in_declaration144 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DECLARATION_in_declaration153 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attribAdorn_in_declaration155 = new BitSet(new long[]{0x0000004100000000L});
    public static final BitSet FOLLOW_decChoice_in_declaration157 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_procDec_in_decChoice171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bundleDec_in_decChoice175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_bundleDec185 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_bundleDec187 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_bundle_body_in_bundleDec189 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BUNDLE_CHANNELS_in_bundle_body200 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_channel_dec_in_bundle_body202 = new BitSet(new long[]{0x0000000200000008L});
    public static final BitSet FOLLOW_33_in_channel_dec216 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_channel_dec220 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_channel_dec224 = new BitSet(new long[]{0x0000000C00000000L});
    public static final BitSet FOLLOW_channel_dir_in_channel_dec226 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_set_in_channel_dir0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_attrib251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ADORNMENTS_in_attribAdorn261 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_attrib_in_attribAdorn263 = new BitSet(new long[]{0x0000000000100008L});
    public static final BitSet FOLLOW_38_in_procDec275 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_procDec279 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_procDec283 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_paramList_in_procDec285 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_block_in_procDec287 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PARAMLIST_in_paramList298 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_param_in_paramList300 = new BitSet(new long[]{0x0000000000004008L});
    public static final BitSet FOLLOW_PARAM_in_param313 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_param317 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_param321 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_while_stmt_in_statement332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_if_stmt_in_statement336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_cif_stmt_in_statement340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_par_stmt_in_statement344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_succ_stmt_in_statement348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_statement352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_statement_in_statement356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_vardec_stmt_in_simple_statement368 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_send_stmt_in_simple_statement375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_receive_stmt_in_simple_statement382 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assign_stmt_in_simple_statement389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_return_stmt_in_simple_statement396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_call_in_simple_statement403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BLOCK_in_block414 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_block416 = new BitSet(new long[]{0x0007FC0000038008L});
    public static final BitSet FOLLOW_42_in_while_stmt428 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_while_stmt430 = new BitSet(new long[]{0x0007FC0000038008L});
    public static final BitSet FOLLOW_statement_in_while_stmt432 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_43_in_if_stmt443 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_if_stmt445 = new BitSet(new long[]{0x0007FC0000038008L});
    public static final BitSet FOLLOW_statement_in_if_stmt447 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_44_in_cif_stmt458 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_cif_stmt460 = new BitSet(new long[]{0x0007FC0000038008L});
    public static final BitSet FOLLOW_statement_in_cif_stmt462 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_45_in_par_stmt472 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_par_stmt474 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_46_in_succ_stmt484 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_succ_stmt486 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VARDEC_in_vardec_stmt497 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_vardec_stmt499 = new BitSet(new long[]{0x0000800000100000L});
    public static final BitSet FOLLOW_var_init_in_vardec_stmt501 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ID_in_var_init511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_var_init519 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_var_init521 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_var_init523 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_48_in_send_stmt534 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_send_stmt536 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_send_stmt538 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_49_in_receive_stmt548 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_receive_stmt552 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_ID_in_receive_stmt556 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_47_in_assign_stmt567 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_assign_stmt569 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_assign_stmt571 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_50_in_return_stmt581 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_return_stmt583 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_literal_in_exp595 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_exp601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_call_in_exp607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_exp614 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp616 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp618 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_52_in_exp627 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp629 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp631 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_53_in_exp639 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp641 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp643 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_54_in_exp652 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp654 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp656 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_55_in_exp664 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp666 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp668 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_56_in_exp676 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp678 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp680 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_57_in_exp688 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp690 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp692 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_58_in_exp700 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp702 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp704 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_59_in_exp712 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp714 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp716 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_60_in_exp735 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp737 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp739 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_61_in_exp758 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp760 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp762 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_62_in_exp770 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp772 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp774 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_63_in_exp782 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp784 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_exp786 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_64_in_exp794 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp796 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_60_in_exp804 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp806 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_61_in_exp814 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_exp816 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CALL_in_call829 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_call831 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_arg_list_in_call833 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ARGLIST_in_arg_list844 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_arg_list846 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_set_in_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_synpred1_DanGen725 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_synpred1_DanGen727 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_synpred1_DanGen729 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_61_in_synpred2_DanGen748 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_exp_in_synpred2_DanGen750 = new BitSet(new long[]{0xFFFFFC0003138008L,0x0000000000000007L});
    public static final BitSet FOLLOW_exp_in_synpred2_DanGen752 = new BitSet(new long[]{0x0000000000000008L});

}