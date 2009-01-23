// A Grammar for the Dangerous Language's dan compiler
// Copyright 2008, Alan Grover, all rights reserved

grammar DanProg;
options {output=AST;}
tokens 
{
	PROGRAM;
	IMPORTS;
	BUNDLE_TYPEDEF;
	BUNDLE_CHANNELS;
	BUNDLE_PROTOCOL;
	CHAN_DEF;
	ALL;
	ADORNMENTS;
	DECLARATION;
	PARAMLIST;
	PARAM;
	BLOCK;
	VARDEC;
	CALL;
	ARGLIST;
	EXP;
}


@header 
{

}

@members 
{
}

prog		: imports decs;


imports 	: importStmt* -> ^(IMPORTS importStmt*);
    
// TODO could probably import a list of symbols from a library
importStmt 	: 'import' ID STMT_END -> ^('import' ID ALL)
			| 'import' symbol=ID 'from' library=ID STMT_END 
				-> ^('import' $library $symbol);
	
	
decs 		: declaration+;
                
declaration	:   decChoice -> ^(DECLARATION decChoice) 
	| attribAdorn decChoice -> ^(DECLARATION attribAdorn decChoice);
    
decChoice 	: procDec | bundleDec;

bundleDec 	: 'bundle' ID bundle_body -> ^('bundle' ID bundle_body);

bundle_body 	: '{' channel_dec+ '}' -> ^(BUNDLE_CHANNELS channel_dec+);
	
// TODO in the future the channel types can be defined in the bundle protocol specification to allow a channel to carry a sequence of types
// TODO add channel buffer specification; i.e. blocking (default), overflowing, overwriting, buffer depth (int or unbounded (default is zero depth (synchronous))
// overflowing and overwriting can only be used with fixed depth channels with depth > 0, not unbounded buffers 
// depth 0 channels are always blocking
channel_dec 	: 'channel' proto_type=ID name=ID channel_dir STMT_END 
			-> ^('channel' $proto_type $name channel_dir);
	
channel_dir 	: '->' | '<-';

attrib 		: '[' ID ']' -> ID;

attribAdorn 	: attrib+ -> ^(ADORNMENTS attrib+);

procDec 	: 'proc' ID ID '(' paramList ')' block 
				-> ^('proc' ID ID paramList block);

paramList 	: param  (',' param)* -> ^(PARAMLIST param+)
			| -> PARAMLIST;


param 		: type=ID name=ID -> ^(PARAM $type $name);

statement 	: (while_stmt | if_stmt | cif_stmt | par_stmt | succ_stmt | block | simple_statement);

simple_statement 
		: vardec_stmt STMT_END -> vardec_stmt
			| send_stmt STMT_END -> send_stmt
			| receive_stmt STMT_END -> receive_stmt
			| assign_stmt STMT_END -> assign_stmt
			| return_stmt STMT_END -> return_stmt
			| call STMT_END -> call;

block 		: BLOCK_BEGIN statement+ BLOCK_END -> ^(BLOCK statement+);

while_stmt 	: 'while' '(' exp ')' statement
			-> ^('while' exp statement);

if_stmt		: 'if' '(' exp ')' statement
			-> ^('if' exp statement);

cif_stmt 	: 'cif' '(' ID ')' statement
			-> ^('cif' ID statement);

par_stmt	: 'par' block -> ^('par' block);

succ_stmt	: 'succ' block -> ^('succ' block);

vardec_stmt 	: ID var_init (',' var_init)* -> ^(VARDEC ID var_init)+;

var_init 	: ID -> ID
		 | ID '='^ exp;

send_stmt 	: ID '!' exp -> ^('!' ID exp);

receive_stmt	: from=ID '?' target=ID -> ^('?' $from $target);

assign_stmt 	: ID '='^ exp;

return_stmt	: 'return' exp -> ^('return' exp);

exp	 	: exp1 -> ^(EXP exp1);

exp1		: bool_exp (comp_op^ bool_exp)*;
 	
comp_op 	: '<' | '>' | '<=' | '>=' | '==' | '!=';

bool_exp	: add_exp (bool_op^ add_exp)*;

bool_op 	: 'or' | 'and' | 'xor';

add_exp		: mult_exp (add_op^ mult_exp)*;

add_op 		: '+' | '-';

mult_exp	: unary_exp (mult_op^ unary_exp)*;

mult_op 	: '*' | '/';

unary_exp	: unary_prefix_op^ atom
			| atom;

unary_prefix_op : 'not' | '+' | '-';

 	
atom 		: literal | ID | call | '(' exp ')' -> exp;

call 		: ID '(' arg_list ')' -> ^(CALL ID arg_list);

arg_list 	: exp?  (',' exp)* -> ^(ARGLIST exp+);

literal 	: bool_lit | FLOAT_LIT | INT_LIT;





BLOCK_BEGIN : '{';

BLOCK_END : '}';

STMT_END : ';';

ID : SIMPLE_ID (NEXT_ID)*;

INT_LIT : ('0'..'9')+;

FLOAT_LIT 
	: INT_LIT ('.' INT_LIT )? (('e' | 'E')('+' | '-') INT_LIT)? ;

bool_lit 
	: 'true' | 'false';


fragment
SIMPLE_ID : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ;

fragment
NEXT_ID :  '.' SIMPLE_ID;

COMMENT : '//' ~('\n'|'\r')*  ('\n'|'\r'|'\r''\n') {$channel=HIDDEN;} ;

WS  :   (' '|'\t'|'\n'|'\r')+ {$channel=HIDDEN;} ;
