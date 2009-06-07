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
	CONSTRUCTOR;
	DECLARATION;
	EXP;
	GENERIC_TYPE;
	GENERIC_ARGLIST;
	IMPORTS;
	NO_ARG;
	NO_INIT;
	PARAM;
	PARAMLIST;
	PROGRAM;
	VARDEC;
	VARINIT;
}

// this has to be a global scope because typeId isn't always called from genericArcList
// and there doesn't seem to be any way to harvest the return value of typeId in a list
// context (the += operator just collects tokens or trees if output=AST is enabled)
// TODO file a bug or enhancement request to allow accessing a list of return values from
// the += operator
scope TypeIdScope 
{
	ArrayList<TypeRef> typeRefs;
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
public HashMap<String, ArrayList<TypeRef>> typeRefs = new HashMap<String, ArrayList<TypeRef>>();

public int errorCount = 0;

void addTypeRef(TypeRef t){
	ArrayList<TypeRef> refs = typeRefs.get(t.toString());
	if(refs == null){
		refs = new ArrayList<TypeRef>();
		typeRefs.put(t.toString(), refs);
	}
	refs.add(t);
}

/* TODO these may belong on DanType; however needs to be updated to use TypeRef and can't use it on the first pass
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
*/
	
}



prog		scope TypeIdScope; // so there will always be a TypeIdScope for typeId to add to, even if we ignore it
		@init
		{
			$TypeIdScope::typeRefs = new ArrayList<TypeRef>();
		}
		: imports decs;


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

bundle_body 	: '{' channelDec+ '}' -> ^(BUNDLE_CHANNELS channelDec+);

// an ID is only valid as a channel depth in a bundle declaration; the id must be a parameter in a
// bundle constructor
channel_depth	returns [ChannelType.ChanDepth cd1, int cd2, Token cd3]
	: 'unbounded' 
	{
		$cd1 = ChannelType.ChanDepth.unbounded;
		$cd2 = 0;
		$cd3 = null;
	}
	| INT_LIT 
	{
		$cd1 = ChannelType.ChanDepth.finite;
		try {
			//$cd2 = Integer.parseInt($INT_LIT);
			if($cd2 < 0)
				throw new IllegalArgumentException("must be a whole number (i.e. {0, 1, 2, ...})");
		} catch (Exception ex) {
			System.out.println("Invalid chan depth: " + $INT_LIT + " : must be a whole number (i.e. {0, 1, 2, ...})");
			++errorCount;
		}
		$cd3 = null;
	}
	| ID
	{
		$cd1 = ChannelType.ChanDepth.id;
		$cd2 = 0;
		$cd3 = $ID;
	};

channel_behavior   returns [ChannelType.ChanBehavior b]
	: 'block'
	{
		$b = ChannelType.ChanBehavior.block;
	} 
	| 'overflow' 
	{
		$b = ChannelType.ChanBehavior.overflow;
	} 
	| 'overwrite' 
	{
		$b = ChannelType.ChanBehavior.overwrite;
	} 
	| 'priority'
	{
		$b = ChannelType.ChanBehavior.priority;
	};

channel_args	returns [ChannelType.ChanDepth cd1, int cd2, Token cd3, ChannelType.ChanBehavior b]
		: channel_depth ',' channel_behavior 
		{ 
			$cd1 = $channel_depth.cd1;
			$cd2 = $channel_depth.cd2;
			$cd3 = $channel_depth.cd3;
			$b = $channel_behavior.b;
		} -> ^(CHAN_PARAMS channel_depth channel_behavior)
		| channel_depth 
		{
			$cd1 = $channel_depth.cd1;
			$cd2 = $channel_depth.cd2;
			$cd3 = $channel_depth.cd3;
			$b = ChannelType.ChanBehavior.block;
		} -> ^(CHAN_PARAMS channel_depth 'block')
		| 
		{
			$cd1 = ChannelType.ChanDepth.finite;
			$cd2 = 0;
			$cd3 = null;
			$b = ChannelType.ChanBehavior.block;
		} -> ^(CHAN_PARAMS CHAN_NOBUFFER 'block'); 
	

channelDec
	: 'channel' '<' genericArgList '>' '(' channel_args ')' name=ID channel_dir STMT_END 
	{
	
	// TODO the generic version of these types should be builtin types.
	// For now we'll just search for the final type and add it if it isn't present yet,
	// but reuse it if it is.
	// TODO simply using the $genericParamList.text may not result in an exact match
	String readerEndName = "chanr<" + $genericArgList.text + ">";
	String writerEndName = "chanw<" + $genericArgList.text + ">";
	// TODO simply using $channel_params.text may not result in an exact match
	String channelName = "channel<" + $genericArgList.text + ">(" + $channel_args.text + ")";
	// all three should either be defined or not; it would be an error for one of them
	// to be in the symbol table but not the others
	ChanRType readerType;
	ChanWType writerType;
	ChannelType channelType;
	
	
	} -> ^('channel' genericArgList $name channel_dir);
	
channel_dir 	: '->' | '<-';

// TODO add constructors
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
		: 'proc' typeId name=ID '(' paramList ')' block // TODO add a generic paramlist
		{
		if(types.containsKey($name)){
			System.out.println("proc type " + $name.text +  " is already defined: " + $name.line + ":" + $name.pos);
			++errorCount;
		} else {
			// TODO add the parameters
			ProcType procType = new ProcType($name, $typeId.t);
			procType.Params = $procDec::paramList;	
			types.put(procType.getName(), procType);	
		}
		} -> ^('proc' ID ID paramList block);

paramList 	: param  (',' param)* -> ^(PARAMLIST param+)
			| -> PARAMLIST;

genericArgList returns [ArrayList<TypeRef> tRefs]
	scope TypeIdScope;
	@init
	{
		$TypeIdScope::typeRefs = new ArrayList<TypeRef>();
	}
	:	typeIds+=typeId (',' typeIds+=typeId)* 
	{
		$tRefs = $TypeIdScope::typeRefs;
	} -> ^(GENERIC_ARGLIST typeId+);

typeId	returns [TypeRef t]
	: token='channel' '<' genericArgList '>' 
	{
		$t = new TypeRef($token, $genericArgList.tRefs);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	} -> 'channel' genericArgList
	| token='chanr' '<' genericArgList '>' 
	{
		$t = new TypeRef($token, $genericArgList.tRefs);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	} -> 'chanr' genericArgList
	| token='chanw' '<' genericArgList '>' 
	{
		$t = new TypeRef($token, $genericArgList.tRefs);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	} -> 'chanw' genericArgList
	| token=ID '<' genericArgList '>' 
	{
		$t = new TypeRef($token, $genericArgList.tRefs);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	} -> GENERIC_TYPE ID genericArgList
	| ID
	{
		$t = new TypeRef($ID);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	};

paramStorageClass 
	:	'static' | 'mobile' | -> 'mobile';

param 		: paramStorageClass typeId name=ID
		{
		$procDec::paramList.add(new Vardec(Vardec.StgClass.Static, $typeId.t, $name));
		} -> ^(PARAM paramStorageClass typeId $name);

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

vardec_stmt 	: storageClass typeId name=ID varInit
		{
		$block::symbols.put($name.text, new Vardec(Vardec.StgClass.Static, $typeId.t, $name));
		} -> ^(VARDEC storageClass typeId $name varInit);
		
varInit		: ('=' exp) -> '=' exp
		| -> NO_INIT;


send_stmt 	: ID '!' exp 
		{
		
		} -> ^('!' ID exp);

receive_stmt	: from=ID '?' target=ID 
		{
		
		} -> ^('?' $from $target);

assign_stmt 	: ID '='^ exp
		{
		
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
		if( ! $pool.text.equals("static") ){
			System.out.println(
				"unknown pool:"
				+ $pool.text
				+ " at "
				+ $pool.start.getLine() + ":" + $pool.start.getCharPositionInLine());
			++errorCount;
		}
		DanType conType = types.get($typeId.text);
		if(conType == null){
			System.out.println(
				"unknown type: "
				+ $typeId.text
				+ " at "
				+ $typeId.start.getLine() + ":" + $typeId.start.getCharPositionInLine());
			++errorCount;
		}
		} -> ^(CONSTRUCTOR pool typeId arg_list);

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

arg_list 	: exp  (',' exp)* -> ^(ARGLIST exp+)
		| -> ^(ARGLIST NO_ARG);

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
