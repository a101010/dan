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

	
}

prog		: imports decs;


imports 	: ^(IMPORTS importStmt*);
    
importStmt 	: ^('import' library=ID symbol=ID)
		| ^('import' library=ID ALL);
		
decs 		: declaration+;
                
declaration	: ^(DECLARATION decChoice) 
		| ^(DECLARATION attribAdorn decChoice);
    
decChoice 	: procDec | bundleDec;

bundleDec 	: ^('bundle' ID bundle_body);

bundle_body 	: ^(BUNDLE_CHANNELS channel_dec+);
	
channel_dec 	:  ^('channel' proto_type=ID name=ID channel_dir);
	
channel_dir 	: '->' | '<-';

attrib 		: ID;

attribAdorn 	: ^(ADORNMENTS attrib+);

procDec 	: ^('proc' returnType=ID name=ID paramList block);

paramList 	: ^(PARAMLIST param*);

param 		: ^(PARAM type=ID name=ID);

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

vardec_stmt 	: ^(VARDEC ID var_init);

var_init 	: ID 
		| ^('=' ID exp);

send_stmt 	: ^('!' ID exp);

receive_stmt	: ^('?' from=ID target=ID);

assign_stmt 	: ^('=' ID exp);

return_stmt	: ^('return' exp);

exp	 	: literal
		| ID
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
		
call		: ^(CALL ID arg_list);

arg_list 	: ^(ARGLIST exp+);

literal 	: 'true' | 'false' | FLOAT_LIT | INT_LIT;
