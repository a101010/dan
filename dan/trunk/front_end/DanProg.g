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
import java.util.HashMap;
import dan.types.*;
}

@members 
{
	public HashMap<String, DanType> types = new HashMap<String, DanType>();
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

bundleDec 	: 'bundle' ID bundle_body 
	{
	BundleType.ValidateName($ID);
	BundleEndType readerEnd = new BundleEndType($ID, BundleEndType.Directions.Reader);
	BundleEndType writerEnd = new BundleEndType($ID, BundleEndType.Directions.Writer);
	BundleType bundle = new BundleType($ID, writerEnd, readerEnd);
	if(types.containsKey($ID)){
		throw new TypeException($ID, "type already declared");
	} else {
		types.put(bundle.getName(), bundle);
		types.put(readerEnd.getName(), readerEnd);
		types.put(writerEnd.getName(), writerEnd);
	}
	} -> ^('bundle' ID bundle_body);

bundle_body 	: '{' channel_dec+ '}' -> ^(BUNDLE_CHANNELS channel_dec+);
	
// TODO in the future the channel types can be defined in the bundle protocol specification to allow a channel to carry a sequence of types
// TODO add channel buffer specification; i.e. blocking (default), overflowing, overwriting, buffer depth (int or unbounded (default is zero depth (synchronous))
// overflowing and overwriting can only be used with fixed depth channels with depth > 0, not unbounded buffers 
// depth 0 channels are always blocking
channel_dec 	: 'channel' proto_type=ID name=ID channel_dir STMT_END 
	{
	// TODO need to figure out how buffer size fits into this...
	// can a bundle type have arbitrarily different buffer characteristics for each channel?
	// is buffer size a constructor parameter, or part of the type?
	// Would like bundle and channel end types to be independent of buffer characteristics,
	// but would prefer not to have arbitrary buffering for a bundle type.
	// Would it make sense for all elements of the bundle to have the same buffer characteristics?
	// For single type protocols it probably makes most sense to have a constructor param for
	// each single type channel.  More complex protocols may have buffer parameters that can be
	// customized at runtime.  (Think of a sliding window protocol, where the receive buffer can
	// be arbitrary, and where the ack channel may be buffered also.)
	
	// TODO the generic version of these types should be builtin types.
	// For now we'll just search for the final type and add it if it isn't present yet,
	// but reuse it if it is.
	String readerEndName = "ChannelReader<" + $proto_type + ">";
	String writerEndName = "ChannelWriter<" + $proto_type + ">";
	String channelName = "SyncChannel<" + $proto_type + ">";
	// all three should either be defined or not; it would be an error for one of them
	// to be in the symbol table but not the others
	ChannelReaderType readerType;
	ChannelWriterType writerType;
	ChannelType channelType;
	if(types.containsKey(channelName)){
		channelType = (ChannelType) types.get(channelName);
		if(types.containsKey(readerEndName)){
			readerType = (ChannelReaderType) types.get(readerEndName);
			
		} else{
			throw new RuntimeException("inconsistent channel type presence in types table");
		}
		if(types.containsKey(writerEndName)){
			writerType = (ChannelWriterType) types.get(writerEndName);
		} else {
			throw new RuntimeException("inconsistent channel type presence in types table");
		}
	}
	else {
		// sanity check; none of them should have been defined
		if(types.containsKey(readerEndName) || types.containsKey(writerEndName))
			throw new RuntimeException("inconsistent channel type presence in types table");
	
		readerType = new ChannelReaderType($proto_type);
		writerType = new ChannelWriterType($proto_type);
		// TODO right now all channels are synchronous with zero depth
		channelType = new ChannelType($proto_type);
		
		
		
		types.put(channelType.getName(), channelType);
		types.put(readerType.getName(), readerType);
		types.put(writerType.getName(), writerType);
		
	}
	// TODO we need to add these types to the enclosing bundle types
	// TODO eventually we need to handle the case where there is no enclosing bundle type
	// (which may require a rule without the channel_dir)
	
	
	} -> ^('channel' $proto_type $name channel_dir);
	
channel_dir 	: '->' | '<-';

attrib 		: '[' ID ']' -> ID;

attribAdorn 	: attrib+ -> ^(ADORNMENTS attrib+);

procDec 	: 'proc' ret=ID name=ID '(' paramList ')' block 
		{
		if(types.containsKey($name))
			throw new TypeException($name, "proc type is already defined");
		
		
		DanType returnType = types.get($ret.getText());
		if(returnType == null){
			// TODO add a type lookahead
			throw new TypeException($ret, "return type is not defined");
		}
		
		// TODO add the parameters
		ProcType procType = new ProcType($name, returnType);	
		types.put(procType.getName(), procType);	
		} -> ^('proc' ID ID paramList block);

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
