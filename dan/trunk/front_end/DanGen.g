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
		| bundleDec -> template(dec={$bundleDec.st}) "<dec>";

bundleDec 	: ^('bundle' ID bundle_body);	

bundle_body 	: ^(BUNDLE_CHANNELS channel_dec+);
	
channel_dec 	:  ^('channel' genericParamList name=ID channel_dir);
	
channel_dir 	: '->' | '<-';

attrib 		: ID -> attrib(attribId={$ID.text});

attribAdorn 	: ^(ADORNMENTS (a+=attrib)+) -> template(attribs={$a}) "<attribs>";

procDec 	: ^('proc' returnType=ID name=ID paramList block) ;

paramList 	: ^(PARAMLIST param*);

genericParamList
	:	^(GENERIC_PARAMLIST typeId+);
	
typeId		: ID
		| 'channel' genericParamList 
		| GENERIC_TYPE ID genericParamList;
		
paramStorageClass
	:	'static' | 'mobile';

param 		: ^(PARAM paramStorageClass typeId name=ID);

statement 	: (while_stmt | if_stmt | cif_stmt | par_stmt | succ_stmt | block | simple_statement);

simple_statement 
		: vardec_stmt
			| send_stmt
			| receive_stmt
			| assign_stmt
			| return_stmt
			| call;

block 		: ^(BLOCK statement+);

while_stmt 	: ^('while' exp statement);

if_stmt		: ^('if' exp statement);

cif_stmt 	: ^('cif' ID statement);

par_stmt	: ^('par' block);

succ_stmt	: ^('succ' block);

storageClass	: 'static' | 'local' | 'mobile';

vardec_stmt 	: ^(VARDEC storageClass typeId name=ID varInit);

varInit		: '=' exp | NO_INIT;

send_stmt 	: ^('!' ID exp);

receive_stmt	: ^('?' from=ID target=ID);

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
