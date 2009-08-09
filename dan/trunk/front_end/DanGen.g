// A tree grammar for the Dangerous Language's dan compiler
// Copyright 2009, Alan Grover, all rights reserved

tree grammar DanGen;

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
public HashMap<String, DanType> types;
	
}

prog		: imports decs -> danModule(frontMatter={"// TODO frontmatter"},
					    imports={$imports.st}, 
					    decs={$decs.st});


imports 	: ^(IMPORTS (imp+=importStmt)*) -> template(imports={$imp}) "<imports>";
    
importStmt 	: ^('import' library=ID symbol=ID) 
			-> importStmt(library={$library.text}, symbol={$symbol.text})
		| ^('import' library=ID ALL)
			-> importStmt(library={$library.text}, symbol={$ALL.text});
		
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
		: ^('bundle' ID bundle_body)
		{
			StringTemplate cd = templateLib.getInstanceOf("statementList", new STAttrMap().put("statements", $bundleDec::channelDeclarations));
			StringTemplate cc = templateLib.getInstanceOf("statementList", new STAttrMap().put("statements", $bundleDec::channelConstructors));
			StringTemplate re = templateLib.getInstanceOf("statementList", new STAttrMap().put("statements", $bundleDec::readEnds));
			StringTemplate we = templateLib.getInstanceOf("statementList", new STAttrMap().put("statements", $bundleDec::writeEnds));
			retval.st = templateLib.getInstanceOf("simpleBundleDec",
							      new STAttrMap().put("bundleType", $ID.text)
							                     .put("channelDeclarations", cd)
							                     .put("channelConstructors", cc)
							                     .put("readEnds", re)
							                     .put("writeEnds", we));
							      
		};	

bundle_body 	: ^(BUNDLE_CHANNELS channel_dec+);
	
channel_dec 	:  ^('channel' genericArgList name=ID channel_dir)
		{
			// TODO add a mapping to the channel type
			$bundleDec::channelDeclarations.add(templateLib.getInstanceOf("localValueDec",
					new STAttrMap().put("type", "__c0bs32")
					               .put("name", $name)));
			$bundleDec::channelConstructors.add(templateLib.getInstanceOf("channelConstructorCall",
					new STAttrMap().put("chanType", "__c0bs32")
					               .put("chanName", $name)
					               .put("readEndId", "\"readEndId\"")
					               .put("writeEndId", "\"writeEndId\"")));
			if($channel_dir.forward)
			{
				// TODO add a mapping from channel types to channel end types
				$bundleDec::readEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanR32")
					               .put("name", $name)));
				$bundleDec::writeEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanW32")
					               .put("name", $name)));
			}
			else
			{
				$bundleDec::readEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanW32")
					               .put("name", $name)));
				$bundleDec::writeEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanR32")
					               .put("name", $name)));
			}
		};
	
channel_dir 	returns [boolean forward]
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
			ArrayList<StringTemplate> params;
			ArrayList<StringTemplate> initLocals;
			ArrayList<StringTemplate> cleanup;
			ArrayList<StringTemplate> scratchInit;
			boolean hasIo;
			boolean hasPar;
		}
		@init
		{
			$procDec::type = null;
			// TODO at the moment cleanup is only at the end of a proc
			// and always occurs
			// Need to allow ref types that are passed as messages or 
			// one-way parameters to skip cleanup
			// Would be nice if cleanup occurs on last use, rather than 
			// at the end of the proc
			$procDec::cleanup = new ArrayList<StringTemplate>();
			$procDec::scratchInit = new ArrayList<StringTemplate>();
			$procDec::hasIo = false;
			$procDec::hasPar = false;
		}
		: ^('proc' returnType=ID name=ID { $procDec::type = (ProcType) types.get($name.text); } paramList block) 
		{
			ProcType type = $procDec::type;
			ArrayList<StringTemplate> scratchInit = new ArrayList<StringTemplate>(3);
			
			if($procDec::hasIo){
				scratchInit.add(new StringTemplate(templateLib, "<scratchInit>",
	                        	new STAttrMap().put("scratchInit", "int result = 0; // for the result of read and write ops\n")));
                        }
                        if($procDec::hasPar){
				scratchInit.add(new StringTemplate(templateLib, "<scratchInit>",
	                        	new STAttrMap().put("scratchInit", "int finished = 0; // for the number of procs in a par that have finished\n")));
	                        scratchInit.add(new StringTemplate(templateLib, "<scratchInit>",
	                        	new STAttrMap().put("scratchInit", "int exceptions = 0; // for the number of procs in a par that threw exceptions\n")));                        	
                        }
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
                        
                        
                        retval.st = templateLib.getInstanceOf("procDec",
					new STAttrMap().put("procType", $name)
					               .put("locals", locals)
					               .put("params", $paramList.st)
					               .put("initLocals", "<initLocals>")
					               .put("procBodyScratchInit", scratchInit)
					               .put("statements", "<block>")
					               .put("cleanup", "<cleanup>")
					               );
		};

paramList 	: ^(PARAMLIST rameses+=param*) -> paramList(params={$rameses});

genericArgList
	:	^(GENERIC_ARGLIST tIds+=typeId+) -> genericArgList(args={$tIds});
	
	typeId		: SIMPLE_TYPE ID { System.out.println("type is " + $ID); } -> typeId(id={$ID})
		| 'channel' genericArgList { System.out.println("type is channel<>"); } -> genericTypeId(id={"channel"}, ga={$genericArgList.st})
		| 'chanr' genericArgList { System.out.println("type is chanr<>"); } -> genericTypeId(id={"chanr"}, ga={$genericArgList.st})
		| 'chanw' genericArgList { System.out.println("type is chanw<>"); } -> genericTypeId(id={"chanw"}, ga={$genericArgList.st})
		| GENERIC_TYPE ID genericArgList {System.out.println("type is " + $ID + "<>"); } -> genericTypeId(id={$ID}, ga={$genericArgList.st}); 
		
paramStorageClass
	:	'static' | 'mobile';

param 		: ^(PARAM paramStorageClass typeId name=ID) 
		{
			/*StringTemplate paramTemplate;
			DanType paramType = types.get($typeId.text);
			if(paramType.isByRef()){
				paramTemplate = templateLib.getInstanceOf("refParameter",
					new STAttrMap().put("type", paramType.getEmittedType()).put("name", $name));
			} else {
				paramTemplate = templateLib.getInstanceOf("valueParameter", 
					new STAttrMap().put("type", paramType.getEmittedType()).put("name", $name));
			}
			retval.st = paramTemplate;*/
		} -> param(type={$typeId.st}, name={$name});

statement 	: (while_stmt | if_stmt | cif_stmt | par_stmt | succ_stmt | block | simple_statement);

simple_statement 
		: vardec_stmt
			| send_stmt
			| receive_stmt
			| assign_stmt
			| return_stmt
			| call;

block 		: ^(BLOCK statement+) -> template(statements={"<statements>"}) "<statements>";

while_stmt 	: ^('while' exp statement);

if_stmt		: ^('if' exp statement);

cif_stmt 	: ^('cif' ID statement);

par_stmt	: ^('par' block) { $procDec::hasPar = true;} ;

succ_stmt	: ^('succ' block);

storageClass	: 'static' | 'local' | 'mobile';

vardec_stmt 	: ^(VARDEC storageClass typeId name=ID varInit);

varInit		: '=' exp | NO_INIT;

send_stmt 	: ^('!' ID exp) { $procDec::hasIo = true; };

receive_stmt	: ^('?' from=ID target=ID) { $procDec::hasIo = true; };

assign_stmt 	: ^('=' ID exp);

return_stmt	: ^('return' exp);

exp	 	: literal
		| ID
		| constructor
		| call
		| ^('<' exp exp) 
		| ^('>' exp exp)
		| ^('<=' exp exp) 
		| ^('>=' exp exp)
		| ^('==' exp exp)
		| ^('!=' exp exp)
		| ^('or' exp exp)
		| ^('and' exp exp)
		| ^('xor' exp exp)
		| (^('+' exp exp))=> ^('+' exp exp)
		| (^('-' exp exp))=> ^('-' exp exp)
		| ^('*' exp exp)
		| ^('/' exp exp)
		| ^('not' exp)
		| ^('+' exp)
		| ^('-' exp);

pool		: 'static' | 'local' | ID;

constructor	: ^(CONSTRUCTOR pool typeId arg_list);
		
call		: ^(CALL ID arg_list);

arg_list 	: ^(ARGLIST exp+)
		| ^(ARGLIST NO_ARG);

literal 	: 'true' | 'false' | FLOAT_LIT | INT_LIT;
