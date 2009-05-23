// A Grammar for the Dangerous Language's dan compiler
// Copyright 2009, Alan Grover, all rights reserved

grammar DanProg;
options {output=AST;}
tokens 
{
	ADORNMENTS;
	ALL;
	ARGLIST;
	BLOCK;
	BUNDLE_TYPEDEF;
	BUNDLE_CHANNELS;
	BUNDLE_PROTOCOL;
	CALL;
	CHAN_DEF;
	CHAN_PARAMS;
	CHAN_NOBUFFER;
	CHAN_TYPE;
	CONSTRUCTOR;
	DECLARATION;
	EXP;
	GENERIC_TYPE;
	GENERIC_PARAMLIST;
	IMPORTS;
	MOBILE_PARAM;
	PARAMLIST;
	PROGRAM;
	STATIC_PARAM;
	VARDEC;
}


@header 
{
import java.util.HashMap;
import dan.types.*;
import dan.system.*;
}

@members 
{
public HashMap<String, DanType> types = new HashMap<String, DanType>();

public int errorCount = 0;


/**
  * Get the type of the symbol; null if not defined.
  */
DanType getSymbolType(String id){
	String[] splitId = id.split("\\.");
	DanType t = searchForSymbolInBlock(splitId);
	if(t != null)
		return t;
	t = searchForSymbolInParamList(splitId);
	if(t != null)
		return t;
//	t = searchForSymbolInStaticMembers(splitId);
	return t;
}

// TODO quite a bit of common code here; find a way to simplify
DanType searchForSymbolInBlock(String[] splitId){
	for(int i = $block.size() - 1; i >= 0; --i){
		for(Vardec v : $block[i]::symbols.values()){
			if(v.Name.getText().equals(splitId[0])){
				if(splitId.length == 1)
					return v.Type;
				return v.Type.getMemberType(ArrayUtils.tail(splitId));
			}
		}
	}
	return null;
}

DanType searchForSymbolInParamList(String[] splitId){
	for(Vardec v : $procDec::paramList){
		if(v.Name.getText().equals(splitId[0])){
			if(splitId.length == 1)
					return v.Type;
			return v.Type.getMemberType(ArrayUtils.tail(splitId));
		}
	}
	return null;
}

DanType searchForSymbolInStaticMembers(String id){
	throw new NotImplementedException();
}

	
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

bundleDec 	
	scope 
	{
	ArrayList<ChanDec> channels;
	ArrayList<Vardec> writerEnds;
	ArrayList<Vardec> readerEnds;
	}
	@init
	{
	$bundleDec::channels = new ArrayList<ChanDec>();
	$bundleDec::writerEnds = new ArrayList<Vardec>();
	$bundleDec::readerEnds = new ArrayList<Vardec>();
	}
	: 'bundle' ID bundle_body 
	
	{
	BundleType.ValidateName($ID);
	BundleEndType readerEnd = new BundleEndType($ID, BundleEndType.Directions.Read);
	BundleEndType writerEnd = new BundleEndType($ID, BundleEndType.Directions.Write);
	BundleType bundle = new BundleType($ID, writerEnd, readerEnd);
	if(types.containsKey($ID)){
		throw new TypeException($ID, "type already declared");
	} 
	else {
		types.put(bundle.getName(), bundle);
		types.put(readerEnd.getName(), readerEnd);
		types.put(writerEnd.getName(), writerEnd);
		bundle.Channels = $bundleDec::channels;
		writerEnd.ChanEnds = $bundleDec::writerEnds;
		readerEnd.ChanEnds = $bundleDec::readerEnds;
	}
	} -> ^('bundle' ID bundle_body);

bundle_body 	: '{' channel_dec+ '}' -> ^(BUNDLE_CHANNELS channel_dec+);

// an ID is only valid as a channel depth in a bundle declaration; the id must be a parameter in a
// bundle constructor
channel_depth	: 'unbounded' | INT_LIT | ID;

channel_behavior
	:	'block' | 'overflow' | 'overwrite' | 'priority';

channel_params	: channel_depth ',' channel_behavior -> ^(CHAN_PARAMS channel_depth channel_behavior)
		| channel_depth -> ^(CHAN_PARAMS channel_depth 'block')
		| -> ^(CHAN_PARAMS CHAN_NOBUFFER 'block'); 
	

channel_dec 	: 'channel' '<' proto_type=ID '>' '(' channel_params ')' name=ID channel_dir STMT_END 
	{
	
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
	ChanDec chanDec = new ChanDec(channelType, $name, $channel_dir.start);
	Vardec writerDec = new Vardec(Vardec.StgClass.Static, writerType, $name);
	Vardec readerDec = new Vardec(Vardec.StgClass.Static, readerType, $name);
	
	$bundleDec::channels.add(chanDec);
	
	if($channel_dir.start.getText() == "->"){
		$bundleDec::writerEnds.add(writerDec);
		$bundleDec::readerEnds.add(readerDec);
	}
	else {
		$bundleDec::writerEnds.add(readerDec);
		$bundleDec::readerEnds.add(writerDec);
	}
	
	// TODO eventually we need to handle the case where there is no enclosing bundle type
	// (which may require a rule without the channel_dir)
	
	
	} -> ^('channel' $proto_type $name channel_dir);
	
channel_dir 	: '->' | '<-';

attrib 		: '[' ID ']' -> ID;

attribAdorn 	: attrib+ -> ^(ADORNMENTS attrib+);

procDec 	scope
		{
		ArrayList<Vardec> paramList;
		}
		@init
		{
		$procDec::paramList = new ArrayList<Vardec>();
		}
		: 'proc' ret=ID name=ID '(' paramList ')' block 
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
		procType.Params = $procDec::paramList;	
		types.put(procType.getName(), procType);	
		} -> ^('proc' ID ID paramList block);

paramList 	: param  (',' param)* -> ^(PARAMLIST param+)
			| -> PARAMLIST;

genericParamList
	:	ID (',' ID)* -> ^(GENERIC_PARAMLIST ID+);

typeId		: ID 
		| type=ID '<' genericParamList '>' -> GENERIC_TYPE $type genericParamList
		| 'channel' '<' protocol=ID '>' -> CHAN_TYPE $protocol;

param 		: 'static' typeId name=ID
		{
		DanType varType = types.get($typeId.getText());
		if(varType == null){
			// TODO add a type lookahead
			throw new TypeException($typeId, "type is not defined");
		}
		$procDec::paramList.add(new Vardec(Vardec.StgClass.Static, varType, name));
		} -> ^(STATIC_PARAM typeId $name)
		| 'mobile'? typeId name=ID 
		{
		DanType varType = types.get($typeId.getText());
		if(varType == null){
			// TODO add a type lookahead
			throw new TypeException($typeId, "type is not defined");
		}
		$procDec::paramList.add(new Vardec(Vardec.StgClass.Mobile, varType, name));
		} -> ^(MOBILE_PARAM $typeId $name);

statement 	: (while_stmt | if_stmt | cif_stmt | par_stmt | succ_stmt | block | simple_statement);

simple_statement 
		: vardec_stmt STMT_END -> vardec_stmt
			| send_stmt STMT_END -> send_stmt
			| receive_stmt STMT_END -> receive_stmt
			| assign_stmt STMT_END -> assign_stmt
			| return_stmt STMT_END -> return_stmt
			| call STMT_END -> call;

block 		scope
		{
		HashMap<String, Vardec> symbols;
		}
		@init
		{
		$block::symbols = new HashMap<String, Vardec>();
		}
		: BLOCK_BEGIN statement+ BLOCK_END 
		{
		System.out.println("Symbols in the current block:");
		for(Vardec v : $block::symbols.values()){
			System.out.println("\t" + v.Type.getName() + " " + v.Name.getText() + "\n");
		}
		} -> ^(BLOCK statement+);

while_stmt 	: 'while' '(' exp ')' statement
			-> ^('while' exp statement);

if_stmt		: 'if' '(' exp ')' statement
			-> ^('if' exp statement);

cif_stmt 	: 'cif' '(' ID ')' statement
			-> ^('cif' ID statement);

par_stmt	: 'par' block -> ^('par' block);

succ_stmt	: 'succ' block -> ^('succ' block);

storageClass	: 'static' | 'local' | 'mobile' | -> 'mobile';

vardec_stmt 	: storageClass typeId name=ID
		{
		DanType varType = types.get($typeId.getText());
		if(varType == null){
			// TODO add a type lookahead
			System.out.println(
				"unknown type:"
				+ $typeId.text
				+ " at "
				+ $typeId.line + ":" + $typeId.pos
				);
			++errorCount;
		} else {
			$block::symbols.put($name.text, new Vardec(Vardec.StgClass.Static, varType, $name));
		}
		} -> ^(VARDEC storageClass typeId $name)
		| storageClass typeId name=ID '=' exp
		{
		DanType varType = types.get($typeId.getText());
		if(varType == null){
			// TODO add a type lookahead
			System.out.println(
				"unknown type:"
				+ $typeId.text
				+ " at "
				+ $typeId.line + ":" + $typeId.pos
				);
			++errorCount;
		} else {
			$block::symbols.put($name.text, new Vardec(Vardec.StgClass.Static, varType, $name));
		}
		} -> ^(VARDEC storageClass typeId $name exp);


send_stmt 	: ID '!' exp 
		{
		DanType endType = getSymbolType($ID.text);
		if(endType == null){
			System.out.println(
				"target of '!' is not defined or is not in scope: "
				+ $ID.text
				+ " at "
				+ $ID.line + ":" + $ID.pos);
			++errorCount;
		}
		} -> ^('!' ID exp);

receive_stmt	: from=ID '?' target=ID 
		{
		DanType endType = getSymbolType($from.text);
		if(endType == null){
			System.out.println(
				"source channel end of '?' is not defined or is not in scope: "
				+ $from.text
				+ " at "
				+ $from.line + ":" + $from.pos);
			++errorCount;
		}
		DanType targetType = getSymbolType($target.text);
		if(targetType == null){
			System.out.println(
				"target of '?' is not defined or is not in scope: "
				+ $target.text
				+ " at "
				+ $target.line + ":" + $target.pos);
			++errorCount;
		}
		} -> ^('?' $from $target);

assign_stmt 	: ID '='^ exp
		{
		DanType targetType = getSymbolType($ID.text);
		if(targetType == null){
			System.out.println(
				"target of assignment is not defined or is not in scope: "
				+ $ID.text
				+ " at "
				+ $ID.line + ":" + $ID.pos);
			++errorCount;
		}
		};

return_stmt	: 'return' exp -> ^('return' exp);

//exp	 	: exp1 -> ^(EXP exp1);

//exp1		: bool_exp (comp_op^ bool_exp)*;

exp		: bool_exp (comp_op^ bool_exp)*;
 	
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

 	
atom 		: literal 
		| ID 
		{
		DanType varType = getSymbolType($ID.text);
		if(varType == null){
			System.out.println(
				"variable is not defined or is not in scope: "
				+ $ID.text
				+ " at "
				+ $ID.line + ":" + $ID.pos);
			++errorCount;
		}
		}
		| call 
		| constructor
		| '(' exp ')' -> exp;
		
pool		: 'static' | 'local' | ID;

		
constructor	: 'new' '(' pool ')' typeId '(' arg_list ')' 
		{
		// type is a constructable type (right now limited to channels)
		// pool is the location to allocate
		// 	static - static allocation in a proc's context
		//	local - envelope of a MobileObject
		//		alternate ideas 'this'
		//		name of reference if allocating from an enclosing proc with
		//		an envelope reference
		//	msg - system global pool
		//	app - app global pool (similar to traditional heap; in fact will use malloc
		//		at first)
		//	<UserDefinedPool> - A custom pool specified by the user
		// Right now we'll support static only, TODO the rest
		if($pool.text != "static"){
			System.out.println(
				"unknown pool:"
				+ $pool.text
				+ " at "
				+ $pool.start.line + ":" + $pool.start.pos);
			++errorCount;
		}
		DanType conType = types.get($type.text);
		if(conType == null){
			System.out.println(
				"unknown type: "
				+ $type.text
				+ " at "
				+ $type.line + ":" + $type.pos);
			++errorCount;
		}
		} -> ^(CONSTRUCTOR pool $type arg_list);

call 		: ID '(' arg_list ')' 
		{
		// ID is either a proc type or a method call on a custom type
		// TODO right now we just search proc types; add method calls
		// (Can use getSymbolType)
		ProcType procType = null;
		procType = (ProcType) types.get($ID.text);
		if(procType == null){
			System.out.println("proc type undefined: " 
				+ $ID.text
				+ " at "
				+ $ID.line + ":" + $ID.pos);
			++errorCount;
		}
		} -> ^(CALL ID arg_list);

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
