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
		| bundleDec -> template(dec={"bundleDec"}) "<dec>"/*-> template(dec={$bundleDec.st}) "<dec>" */;

bundleDec 	: ^('bundle' ID bundle_body);	

bundle_body 	: ^(BUNDLE_CHANNELS channel_dec+);
	
channel_dec 	:  ^('channel' genericParamList name=ID channel_dir);
	
channel_dir 	: '->' | '<-';

attrib 		: ID -> attrib(attribId={$ID.text});

attribAdorn 	: ^(ADORNMENTS (a+=attrib)+) -> template(attribs={$a}) "<attribs>";

procDec 	scope
		{
			ArrayList<StringTemplate> locals;
			ArrayList<StringTemplate> params;
			ArrayList<StringTemplate> args;
			ArrayList<StringTemplate> initLocals;
			ArrayList<StringTemplate> cleanup;
			ArrayList<StringTemplate> scratchInit;
			boolean hasIo;
			boolean hasPar;
			
			
		}
		@init
		{
			$procDec::locals = new ArrayList<StringTemplate>();
			$procDec::params = new ArrayList<StringTemplate>();
			$procDec::args = new ArrayList<StringTemplate>();
			$procDec::initLocals = new ArrayList<StringTemplate>();
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
		: ^('proc' returnType=ID name=ID paramList block) 
		{
			if($procDec::hasIo){
				$procDec::scratchInit.add(new StringTemplate(templateLib, "<scratchInit>",
	                        	new STAttrMap().put("scratchInit", "int result = 0; // for the result of read and write ops\n")));
                        }
                        if($procDec::hasPar){
				$procDec::scratchInit.add(new StringTemplate(templateLib, "<scratchInit>",
	                        	new STAttrMap().put("scratchInit", "int finished = 0; // for the number of procs in a par that have finished\n")));
	                        $procDec::scratchInit.add(new StringTemplate(templateLib, "<scratchInit>",
	                        	new STAttrMap().put("scratchInit", "int exceptions = 0; // for the number of procs in a par that threw exceptions\n")));                        	
                        }
		}-> procDec(
			procType={$name},
		        locals={"<locals>"},
		        params={$paramList.st},
		        args={"<args>"},
		        initLocals={"<initLocals>"},
		        procBodyScratchInit={$procDec::scratchInit},
		        statements={$block.st},
		        cleanup={"<cleanup>"}) ;

paramList 	: ^(PARAMLIST rameses+=param*) -> template(params={$rameses}) "<params>";

genericParamList
	:	^(GENERIC_PARAMLIST typeId+);
	
typeId		: ID { System.out.println("type is " + $ID); }
		| 'channel' genericParamList { System.out.println("type is channel<" + $genericParamList.text + ">"); }
		| 'chanr' genericParamList { System.out.println("type is chanr<" + $genericParamList.text + ">"); }
		| 'chanw' genericParamList { System.out.println("type is chanw<" + $genericParamList.text + ">"); }
		| GENERIC_TYPE ID genericParamList {System.out.println("type is " + $ID + "<" + $genericParamList.text + ">"); };
		
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
		};

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
