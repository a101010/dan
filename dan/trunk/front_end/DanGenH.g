// A tree grammar for the Dangerous Language's dan compiler to generage a C header file
// from a .dan source code file
// Copyright 2010, Alan Grover, all rights reserved

tree grammar DanGenH;

options 
{
output=template;
tokenVocab=DanProg;
ASTLabelType=CommonTree;
}

@header 
{
import java.util.HashMap;
import dan.types.*;
import dan.system.*;
}

@members 
{
public StringTemplate importLibs;
public HashMap<String, DanType> types;
public String includeGuard;

// for assignment statements and vardec statements with an initializer, 
// represents the target of the assignment
public String assignTarget = null;
}

prog		: decs -> danHeader(includeGuard={includeGuard},
					    frontMatter={"// TODO frontmatter"},
					    imports={importLibs}, 
					    decs={$decs.st});

		
decs 		: (d+=declaration)+ -> template(decs={$d}) "<decs>";
   
declaration	: ^(DECLARATION decChoice) -> template(dec={$decChoice.st}) "<dec>"
		| ^(DECLARATION attribAdorn decChoice) -> adornedDec(attribs={$attribAdorn.st},
								     dec={$decChoice.st});
    
decChoice 	: procDec -> template(dec={$procDec.st}) "<dec>"
		| bundleDec -> template(dec={$bundleDec.st}) "<dec>"/*-> template(dec={$bundleDec.st}) "<dec>" */;

bundleDec 	scope
		{
			ArrayList<StringTemplate> channelDeclarations; 
			ArrayList<StringTemplate> channelConstructors; 
			ArrayList<StringTemplate> readEnds; 
			ArrayList<StringTemplate> writeEnds; 
		}
		@init
		{
			$bundleDec::channelDeclarations = new ArrayList<StringTemplate>();
			$bundleDec::channelConstructors = new ArrayList<StringTemplate>();
			$bundleDec::readEnds = new ArrayList<StringTemplate>();
			$bundleDec::writeEnds = new ArrayList<StringTemplate>();
		}
		: ^('bundle' ID bundleBody)
		{
			StringTemplate cd = templateLib.getInstanceOf("statementList", new STAttrMap().put("statements", $bundleDec::channelDeclarations));
			StringTemplate cc = templateLib.getInstanceOf("statementList", new STAttrMap().put("statements", $bundleDec::channelConstructors));
			StringTemplate re = templateLib.getInstanceOf("statementList", new STAttrMap().put("statements", $bundleDec::readEnds));
			StringTemplate we = templateLib.getInstanceOf("statementList", new STAttrMap().put("statements", $bundleDec::writeEnds));
			retval.st = templateLib.getInstanceOf("simpleBundleHeaderDec",
							      new STAttrMap().put("bundleType", $ID.text)
							                     .put("channelDeclarations", cd)
							                     .put("readEnds", re)
							                     .put("writeEnds", we));
							      
		};	

bundleBody 	: ^(BUNDLE_STATEMENTS bundleStmt+);

channelDepth	: 'unbounded' | INT_LIT | ID;

channelBehavior : 'block' | 'overflow' | 'overwrite' | 'priority';

/*channelArgs	: ^(CHAN_ARGS1 channelDepth channelBehavior)
		| ^(CHAN_ARGS3 CHAN_NOBUFFER 'block')
		| ^(CHAN_ARGS2 channelDepth 'block');*/

bundleStmt	: ^(BUNDLE_STATEMENT channelDecStmt1 channelDir)
		{
			String name = $channelDecStmt1.name;
			// TODO add a mapping to the channel type
			$bundleDec::channelDeclarations.add(templateLib.getInstanceOf("localValueDec",
					new STAttrMap().put("type", "__c0bs32")
					               .put("name", name)));
			if($channelDir.forward)
			{
				// TODO add a mapping from channel types to channel end types
				$bundleDec::readEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanR32")
					               .put("name", name)));
				$bundleDec::writeEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanW32")
					               .put("name", name)));
			}
			else
			{
				$bundleDec::readEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanW32")
					               .put("name", name)));
				$bundleDec::writeEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanR32")
					               .put("name", name)));
			}
		};
	
channelDecStmt1	returns [String name]
		:  ^(CHAN_VARDEC_1 chanTypeId ID)
		{
			$name = $ID.text;
		};
		
channelDecStmt2 
	:	 ^(CHAN_VARDEC_2 paramStorageClass chanTypeId ID);
		
	
channelDir 	returns [boolean forward]
		: '->' 
		{
			$forward = true;
		}
		| '<-'
		{
			$forward = false;
		};

attrib 		: ID -> attrib(attribId={$ID.text});

attribAdorn 	: ^(ADORNMENTS (a+=attrib)+) -> template(attribs={$a}) "<attribs>";

procDec 	scope
		{
			ProcType type;
			ArrayList<StringTemplate> locals;
			ArrayList<StringTemplate> initLocals;
			ArrayList<StringTemplate> cleanup;
			ArrayList<StringTemplate> scratchInit;
			boolean hasIo;
			boolean hasPar;
			// labelNum is for saveState labels
			int labelNum;
		}
		@init
		{
			$procDec::type = null;
		}
		: ^('proc' returnType=ID name=ID 
		{ 
			$procDec::type = (ProcType) types.get($name.text); 
		} paramList block) 
		{
			ProcType type = $procDec::type;
                        
                        ArrayList<StringTemplate> locals = new ArrayList<StringTemplate>(type.Locals.size());
                        
                        
                        for(Vardec v: type.Locals.values()){
                        	if(v.isByRef()){
                        		locals.add( templateLib.getInstanceOf("localByRefDec",
									      new STAttrMap().put("type", v.getEmittedType())
									                     .put("name", v.EmittedName)));
                        	}
                        	else {
                        		locals.add( templateLib.getInstanceOf("localValueDec",
								              new STAttrMap().put("type", v.getEmittedType())
										             .put("name", v.EmittedName)));
                        	}
                        }
                        
                        ArrayList<StringTemplate> params = new ArrayList<StringTemplate>(type.Params.size());
                        
                        StringTemplate instanceParam = templateLib.getInstanceOf("instanceParam",
					new STAttrMap().put("procType", $name));
			params.add(instanceParam);
                        
                        for(Vardec v: type.Params){
                        if(v.isByRef()){
                        		params.add( templateLib.getInstanceOf("refParam",
									      new STAttrMap().put("type", v.getEmittedType())
									                     .put("name", v.EmittedName)));
                        	}
                        	else {
                        		params.add( templateLib.getInstanceOf("valueParam",
								              new STAttrMap().put("type", v.getEmittedType())
										             .put("name", v.EmittedName)));
                        	}
                        }
                        
                        StringTemplate procDecTemplate = templateLib.getInstanceOf("procHeaderDec",
					new STAttrMap().put("procType", $name)
					               .put("locals", locals)
					               .put("params", params)
					               );
			
			// TODO add check for duplicate main (not here... have to check across all libraries and modules)
			//boolean isMain = false;
			//for(String s: type.Attributes){
			//	if(s.equals("main")){
			//		isMain = true;
			//		break;
			//	}
			//}
			
			//if(!isMain){
				retval.st = procDecTemplate;
			//}
			//else {
			//	retval.st = new StringTemplate("// Don't need a [main] proc in the header TODO verify");
			//}
			
		};

paramList 	: ^(PARAMLIST rameses+=param*) -> paramList(params={$rameses});

genericArgList	: ^(GENERIC_ARGLIST tIds+=typeId+);
	
chanTypeId  	: ^('channel' /*genericArgList channelArgs*/ CT_REF)
		{
			//System.out.println("got a chanTypeId with attached longname: " + $CT_REF.text);
		};

typeId		: SIMPLE_TYPE ID T_REF { /*System.out.println("DanGenH: type is " + $T_REF.text);*/ } -> typeId(id={$ID})
		| 'chanr' genericArgList T_REF { /*System.out.println("DanGenH: type is " + $T_REF.text); */}
		| 'chanw' genericArgList T_REF { /*System.out.println("DanGenH: type is " + $T_REF.text); */ }
		| GENERIC_TYPE ID genericArgList T_REF {/*System.out.println("type is " + $T_REF.text); */ }; 
		
paramStorageClass
	:	'static' | 'mobile';

param 		: ^(PARAM paramStorageClass typeId name=ID) 
		-> param(type={$typeId.st}, name={$name});


statement 	: whileStmt 
			| ifStmt 
			| cifStmt   
			| parStmt  
			| succStmt  
			| block  
			| simpleStatement;

simpleStatement 
		: varDecStmt 
			| channelDecStmt2 
			| sendStmt 
			| receiveStmt 
			| assignStmt 
			| returnStmt 
			| call;

block 		: ^(BLOCK  statements+=statement+);

whileStmt 	: ^('while' exp block);

ifStmt		: ^('if' exp block);

cifStmt 	: ^('cif' ID block);

parStmt		: ^('par' block);

succStmt	: ^('succ' block);

storageClass	: 'static' | 'local' | 'mobile';

varDecStmt 	: ^(VARDEC storageClass typeId ID varInit);

varInit		: '=' exp 
		| NO_INIT;

sendStmt 	: ^('!' ID exp);

receiveStmt	: ^('?' from=ID target=ID);

assignStmt 	: ^('=' ID { assignTarget = $ID.text; } exp);

returnStmt	: ^('return' exp);


exp	 	: literal 
		| ID  
		| constructor
		| call
		| ^('<' left=exp right=exp)
		| ^('>' left=exp right=exp)
		| ^('<=' left=exp right=exp)
		| ^('>=' left=exp right=exp)
		| ^('==' left=exp right=exp)
		| ^('!=' left=exp right=exp)
		| ^('or' left=exp right=exp)
		| ^('and' left=exp right=exp)
		| ^('xor' left=exp right=exp)
		| (^('+' exp exp))=> ^('+' left=exp right=exp)
		| (^('-' exp exp))=> ^('-' left=exp right=exp)
		| ^('*' left=exp right=exp)
		| ^('/' left=exp right=exp)
		| ^('not' operand=exp) 
		| ^('+' operand=exp) 
		| ^('-' operand=exp);		


pool		returns [String poolName] 
		: 'static' { $poolName = "static"; }
		| 'local'  { $poolName = "local"; }
		| ID       { $poolName = $ID.text; };

constructor	: ^(CONSTRUCTOR pool typeId argList);
		
call		: ^(CALL ID argList);

argList 	
		: ^(ARGLIST args+=exp+)
		| ^(ARGLIST NO_ARG);

literal 	: 'true' { $literal.st = new StringTemplate("1"); } 
			| 'false' { $literal.st = new StringTemplate("0"); } 
			| FLOAT_LIT { $literal.st = new StringTemplate($FLOAT_LIT.text); }
			| INT_LIT {$literal.st = new StringTemplate($INT_LIT.text); };
