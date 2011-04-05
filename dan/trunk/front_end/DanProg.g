// A Grammar for the Dangerous Language's dan compiler
// Copyright 2010, Alan Grover, all rights reserved

grammar DanProg;
options {output=AST;}
tokens 
{
	ADORNMENTS;
	ALL;
	ARGLIST;
	BLOCK;
	BUNDLE_TYPEDEF;
	BUNDLE_STATEMENTS;
	BUNDLE_STATEMENT;
	BUNDLE_PROTOCOL;
	CALL;
	CHAN_CONSTRUCTOR;
	CHAN_DEF;
	CHAN_ARGS1;
	CHAN_ARGS2;
	CHAN_ARGS3;
	CHAN_NOBUFFER;
	CHAN_VARDEC_1;
	CHAN_VARDEC_2;
	CONSTRUCTOR;
	CT_REF;
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
	SIMPLE_TYPE;
	T_REF;
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
	public ArrayList<String> importLibs = new ArrayList<String>();
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
}



prog		scope TypeIdScope; // so there will always be a TypeIdScope for typeId to add to, even if we ignore it
		@init
		{
			$TypeIdScope::typeRefs = new ArrayList<TypeRef>();
		}
		: imports decs;


imports 	: importStmt*;
    
// TODO could probably import a list of symbols from a library
// TODO for the C back end, we can only emit #includes, and cannot prevent a symbol from being included
//      for an asm back end, we could take care of that
//      But for now, import will wind up having the same scope as #include
//	The alternative is to generate a separate .h for each symbol, which sounds hard to manage
importStmt 	: 'import' ID STMT_END 
		{
			importLibs.add($ID.text);
		}	
		| 'import' symbol=ID 'from' library=ID STMT_END 
		{
			importLibs.add($library.text);
		};
	
	
decs 		: declaration+;
                
declaration	scope
		{
			ArrayList<String> attributes;
		}
		@init
		{
			$declaration::attributes = new ArrayList<String>();
		}
		:   decChoice -> ^(DECLARATION decChoice) 
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
	: 'bundle' ID bundleBody 
	
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
	} -> ^('bundle' ID bundleBody);

bundleBody 	: '{' bundleStmt+ '}' -> ^(BUNDLE_STATEMENTS bundleStmt+);

// an ID is only valid as a channel depth in a bundle declaration; the id must be a parameter in a
// bundle constructor
channelDepth	returns [ChannelType.ChanDepth cd1, int cd2, Token cd3]
	: 'unbounded' 
	{
		$cd1 = ChannelType.ChanDepth.unbounded;
		$cd2 = 0;
		$cd3 = null;
	} -> 'unbounded'
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
	} -> INT_LIT
	| ID
	{
		$cd1 = ChannelType.ChanDepth.id;
		$cd2 = 0;
		$cd3 = $ID;
	} -> ID;

channelBehavior   returns [ChannelType.ChanBehavior b]
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

channelArgs	returns [ChannelType.ChanDepth cd1, int cd2, Token cd3, ChannelType.ChanBehavior b]
		: channelDepth ',' channelBehavior 
		{ 
			$cd1 = $channelDepth.cd1;
			$cd2 = $channelDepth.cd2;
			$cd3 = $channelDepth.cd3;
			$b = $channelBehavior.b;
		} //-> ^(CHAN_ARGS1 channelDepth channelBehavior)
		| channelDepth 
		{
			$cd1 = $channelDepth.cd1;
			$cd2 = $channelDepth.cd2;
			$cd3 = $channelDepth.cd3;
			$b = ChannelType.ChanBehavior.block;
		} //-> ^(CHAN_ARGS2 channelDepth 'block')
		| 
		{
			$cd1 = ChannelType.ChanDepth.finite;
			$cd2 = 0;
			$cd3 = null;
			$b = ChannelType.ChanBehavior.block;
		/*} -> ^(CHAN_ARGS3 CHAN_NOBUFFER 'block'); */
		};
	

bundleStmt
	: channelDecStmt1 channelDir STMT_END -> ^(BUNDLE_STATEMENT channelDecStmt1 channelDir);


channelDecStmt1
	: chanTypeId  name=ID   
	{
		// channel variables in bundles are always static to the bundle
		Vardec v = new Vardec(Vardec.StgClass.Static, $chanTypeId.ct, $name, $name.text, false);

	} -> ^(CHAN_VARDEC_1 chanTypeId $name);
	

channelDecStmt2
	: paramStorageClass chanTypeId name=ID
	{
		Vardec v;
		if($paramStorageClass.text.equals("static")) {
			v = new Vardec(Vardec.StgClass.Static, $chanTypeId.ct, $name, $name.text, false);
		}
		else {
			System.out.println("only static storage class is supported : " 
				+ $paramStorageClass.start.getText()
				+ " at "
				+  $paramStorageClass.start.getLine() 
				+ ":" + $paramStorageClass.start.getCharPositionInLine());
			v = new Vardec(Vardec.StgClass.Mobile, $chanTypeId.ct, $name, $name.text, false);
			++errorCount;
		}
		$procDec::currentScope.Symbols.put($name.text, v);
		$procDec::locals.put(v.EmittedName, v);
	} -> ^(CHAN_VARDEC_2 paramStorageClass chanTypeId $name);
	
channelDir 	: '->' | '<-';

// TODO add attribute constructors
attrib 		: '[' ID ']' 
		{
			$declaration::attributes.add($ID.text);
		} -> ID;

attribAdorn 	: attrib+ -> ^(ADORNMENTS attrib+);

procDec 	scope
		{
			ArrayList<Vardec> params;
			Scope currentScope;
			HashMap<String, Vardec> locals; // key is emitted name
		}
		@init
		{
			$procDec::params = new ArrayList<Vardec>();
			$procDec::currentScope = null; // will be assigned the root scope in the first block rule
			$procDec::locals = new HashMap<String, Vardec>();
		}
		: 'proc' typeId pname=ID '(' paramList ')' block // TODO add a generic paramlist
		{
			if(types.containsKey($pname)){
				System.out.println("proc type " + $pname.text +  " is already defined: " + $pname.line + ":" + $pname.pos);
				++errorCount;
			} 
			else {
				ProcType procType = new ProcType($pname, $typeId.t, $declaration::attributes, $procDec::params, $procDec::locals, $procDec::currentScope);
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
	
chanTypeId returns [ChanTypeRef ct]
	: token='channel' '<' genericArgList '>' '(' channelArgs ')'
	{
		$ct = new ChanTypeRef($token, $genericArgList.tRefs);
		
		$TypeIdScope::typeRefs.add($ct);
		try {
			$chanTypeId.ct.setChanArgs($channelArgs.cd1, $channelArgs.cd2, $channelArgs.cd3, $channelArgs.b);
		}
		catch (Exception ex) {
			System.out.println("caught " + ex);
			++errorCount;
		}
		addTypeRef($ct);
	} -> ^('channel' /*genericArgList channelArgs*/ CT_REF[$token, $ct.getLongName()]);

typeId	returns [TypeRef t]
	: token='chanr' '<' genericArgList '>' 
	{
		$t = new TypeRef($token, $genericArgList.tRefs);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	} -> 'chanr' genericArgList T_REF[$token, $t.getLongName()]
	| token='chanw' '<' genericArgList '>' 
	{
		$t = new TypeRef($token, $genericArgList.tRefs);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	} -> 'chanw' genericArgList T_REF[$token, $t.getLongName()]
	| token=ID '<' genericArgList '>' 
	{
		$t = new TypeRef($token, $genericArgList.tRefs);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	} -> GENERIC_TYPE ID genericArgList T_REF[$token, $t.getLongName()]
	| token=ID
	{
		$t = new TypeRef($ID);
		addTypeRef($t);
		$TypeIdScope::typeRefs.add($t);
	} -> SIMPLE_TYPE ID  T_REF[$token, $t.getLongName()];

paramStorageClass 
	:	'static' | 'mobile' | -> 'mobile';

param 		: paramStorageClass typeId name=ID
		{
			Vardec v = new Vardec(Vardec.StgClass.Static, $typeId.t, $name, $name.text, true);
			$procDec::params.add(v);
			$procDec::locals.put(v.EmittedName, v);
		} -> ^(PARAM paramStorageClass typeId $name);

statement 	: (whileStmt | ifStmt | cifStmt | parStmt | succStmt | block | simpleStatement);

simpleStatement 
		: varDecStmt STMT_END -> varDecStmt
		| channelDecStmt2 STMT_END -> channelDecStmt2
		| sendStmt STMT_END -> sendStmt
		| receiveStmt STMT_END -> receiveStmt
		| assignStmt STMT_END -> assignStmt
		| returnStmt STMT_END -> returnStmt
		| call STMT_END -> call;

block 		
		@init
		{
			
			$procDec::currentScope = new Scope($procDec::currentScope);
			if($procDec::currentScope.Parent != null){
				$procDec::currentScope.Parent.Children.add($procDec::currentScope);
			}
		}
		: BLOCK_BEGIN statement+ BLOCK_END 
		{		
			// pop the current scope unless it is the root scope
			if($procDec::currentScope.Parent != null){
				$procDec::currentScope = $procDec::currentScope.Parent;
			}
		} -> ^(BLOCK statement+);

whileStmt 	: 'while' '(' exp ')' block
			-> ^('while' exp block);

ifStmt		: 'if' '(' exp ')' block
			-> ^('if' exp block);

cifStmt 	: 'cif' '(' ID ')' block
			-> ^('cif' ID block);

// TODO may want to prohibit one-level nested pars
parStmt	: 'par' block -> ^('par' block);

succStmt	: 'succ' block -> ^('succ' block);

storageClass	: 'static' | 'local' | 'mobile' | -> 'mobile';

varDecStmt 	: storageClass typeId name=ID varInit
		{
			// TODO created emitted name if there is a conflict in procDec::locals
			Vardec v = new Vardec(Vardec.StgClass.Static, $typeId.t, $name, $name.text, false);
			$procDec::currentScope.Symbols.put($name.text, v);
			$procDec::locals.put(v.EmittedName, v);
		} -> ^(VARDEC storageClass typeId $name varInit);
		
varInit		: ('=' exp) -> '=' exp
		| -> NO_INIT;


sendStmt 	: ID '!' exp 
		{
		
		} -> ^('!' ID exp);

receiveStmt	: from=ID '?' target=ID 
		{
		
		} -> ^('?' $from $target);

assignStmt 	: ID '='^ exp
		{
		
		};

returnStmt	: 'return' exp -> ^('return' exp);

exp		: compExp (boolOp^ compExp)*;

compOp 		: '<' | '>' | '<=' | '>=' | '==' | '!=';

boolOp 		: 'or' | 'and' | 'xor';

compExp 	: addExp (compOp^ addExp)*; 
	

addExp		: multExp (addOp^ multExp)*;

addOp 		: '+' | '-';

multExp	: unaryExp (multOp^ unaryExp)*;

multOp 	: '*' | '/';

unaryExp	: unaryPrefixOp^ atom
			| atom;

unaryPrefixOp : '+' | '-' | 'not';

 	
atom 		: literal 
		| ID 
		| call 
		| constructor
		| '(' exp ')' -> exp;
		
pool		: 'static' | 'local' | ID;

		
constructor	: 'new' '(' pool ')' typeId '(' argList ')' 
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
		} ->  ^(CONSTRUCTOR pool typeId argList)
		| 'new' '(' pool ')' chanTypeId -> ^(CHAN_CONSTRUCTOR pool chanTypeId);

call 		: ID '(' argList ')' 
		{
			// ID is either a proc type or a method call on a custom type
			// TODO right now we just search types; add method calls
			TypeRef tRef = new TypeRef($ID);
			// need to allocate storage for the call (only recursive procs are dynamically allocated, so this allocates in the caller's context)
			// TODO what we really need is a way to generically allocate and reuse the same space
			// So, allocate an array of char that is big enough to hold the largest set of procs in a par.
			// And then we need a way to reference those procs.  Could use pointers in the locals structure, but that doesn't really scale.
			// Could also use raw offsets, but that is cryptic.  Will probably win out though.
			String emittedName = "__p" + $ID.text;
			// we use the ID as the token here so we can report error information if the proc type isn't found; 
			// not because it is really the name of the vardec
			Vardec v = new Vardec(Vardec.StgClass.Static, tRef, $ID, emittedName, false);
			$procDec::currentScope.Symbols.put(v.EmittedName, v);
			$procDec::locals.put(v.EmittedName, v);
			addTypeRef(tRef);
			$TypeIdScope::typeRefs.add(tRef);
		
		} -> ^(CALL ID argList);

argList 	: exp  (',' exp)* -> ^(ARGLIST exp+)
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
