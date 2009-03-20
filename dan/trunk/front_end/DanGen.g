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

bundleDec 	scope
		{
			ArrayList<StringTemplate> channelDecs;
			ArrayList<StringTemplate> wakeWaiting;
			ArrayList<StringTemplate> channelCtors;
			ArrayList<StringTemplate> channelOps;
			String bundleType;
		}
		@init
		{
			$bundleDec::channelDecs = new ArrayList<StringTemplate>();
			$bundleDec::wakeWaiting = new ArrayList<StringTemplate>();
			$bundleDec::channelCtors = new ArrayList<StringTemplate>();
			$bundleDec::channelOps = new ArrayList<StringTemplate>();
		}
		: ^('bundle' ID { $bundleDec::bundleType = $ID.text; }
		bundle_body) -> simpleBundleDec(bundleType={$ID.text},
								channelDecs={$bundleDec::channelDecs},
								wakeWaiting={$bundleDec::wakeWaiting},
								channelCtors={$bundleDec::channelCtors},
								channelOps={$bundleDec::channelOps} );

bundle_body 	: ^(BUNDLE_CHANNELS channel_dec+);
	
channel_dec 	:  ^('channel' proto_type=ID name=ID channel_dir)
		{
			STAttrMap channelDecMap = new STAttrMap();
			// TODO can't directly use the proto_type.text; need a map function
			channelDecMap.put("chanType", $proto_type.text);
			channelDecMap.put("chanName", $name.text);
		
			StringTemplate channelDec 
				= templateLib.getInstanceOf("simpleChanDec", channelDecMap);
			$bundleDec::channelDecs.add(channelDec);
			
			STAttrMap wakeWaitingMap = new STAttrMap();
			wakeWaitingMap.put("chanName", $name.text);
			
			StringTemplate wakeReader
				= templateLib.getInstanceOf("simpleChanWakeupReader", wakeWaitingMap);
			
			$bundleDec::wakeWaiting.add(wakeReader);
				
			StringTemplate wakeWriter
				= templateLib.getInstanceOf("simpleChanWakeupWriter", wakeWaitingMap);
				
			$bundleDec::wakeWaiting.add(wakeWriter);
			
			STAttrMap chanCtorMap = new STAttrMap();
			chanCtorMap.put("chanName", $name.text);
			
			// TODO need a way to lookup default values by type
			// TODO for ref types will have to call a constructor
			chanCtorMap.put("defaultValue", "0");
			
			StringTemplate chanCtor
				= templateLib.getInstanceOf("simpleChanCtor", chanCtorMap);
				
			$bundleDec::channelCtors.add(chanCtor);
			
			STAttrMap chanOpsMap = new STAttrMap();
			chanOpsMap.put("chanName", $name.text);
			chanOpsMap.put("chanType", $proto_type.text);
			chanOpsMap.put("bundleType", $bundleDec::bundleType);
			chanOpsMap.put("wakeupWriter", wakeWriter);
			
			StringTemplate chanOps
				= templateLib.getInstanceOf("simpleChanOps", chanOpsMap);
				
			$bundleDec::channelOps.add(chanOps);
							    
		};
	
channel_dir 	: '->' | '<-';

attrib 		: ID -> attrib(attribId={$ID.text});

attribAdorn 	: ^(ADORNMENTS (a+=attrib)+) -> template(attribs={$a}) "<attribs>";

procDec 	scope
		{
			ArrayList<StringTemplate> procDefines;
			ArrayList<StringTemplate> locals;
			ArrayList<StringTemplate> params;
			ArrayList<StringTemplate> initLocals;
			ArrayList<StringTemplate> stateLabels;
			ArrayList<StringTemplate> cleanup;
			
		}
		@init
		{
			$procDec::procDefines = new ArrayList<StringTemplate>();
			$procDec::locals = new ArrayList<StringTemplate>();
			$procDec::params = new ArrayList<StringTemplate>();
			$procDec::initLocals = new ArrayList<StringTemplate>();
			$procDec::stateLabels = new ArrayList<StringTemplate>();
			// TODO at the moment cleanup is only at the end of a proc
			// and always occurs
			// Need to allow ref types that are passed as messages or 
			// one-way parameters to skip cleanup
			// Would be nice if cleanup occurs on last use, rather than 
			// at the end of the proc
			$procDec::cleanup = new ArrayList<StringTemplate>();
		}
		: ^('proc' returnType=ID name=ID paramList block) 
			-> procDec(procType={$name.text},
				   procDefines={"// TODO procDefines"},
				   locals={$procDec::locals},
				   params={$procDec::params},
				   initLocals={$procDec::initLocals},
				   stateLables={"// TODO stateLabels"},
				   cleanup={$procDec::cleanup});

paramList 	: ^(PARAMLIST param*);

param 		: ^(PARAM type=ID name=ID)
		{	
			DanType paramType = types.get($type.text);	
			STAttrMap typeNameMap = new STAttrMap();
			typeNameMap.put("type", paramType.getEmittedType());
			typeNameMap.put("name", $name.text);
			
			StringTemplate local = null;
			StringTemplate param = null;
			if (paramType.isByRef()) {
				local = templateLib.getInstanceOf("localByRefDec", typeNameMap);
				param = templateLib.getInstanceOf("refParam", typeNameMap);
			}
			else {
				local = templateLib.getInstanceOf("localValueDec", typeNameMap);
				param = templateLib.getInstanceOf("valueParam", typeNameMap);
			}
			$procDec::locals.add(local);
			$procDec::params.add(param);
			
			STAttrMap initLocalsMap = new STAttrMap();
			initLocalsMap.put("name", $name.text);
			initLocalsMap.put("value", $name.text);
			StringTemplate initLocals
				= templateLib.getInstanceOf("initLocal", initLocalsMap);
			$procDec::initLocals.add(initLocals);	
			
			StringTemplate cleanup 
				= templateLib.getInstanceOf(types.get( $type.text).getCleanupTemplateName(),
							    typeNameMap);
			$procDec::cleanup.add(cleanup);
		};

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

vardec_stmt 	: ^(VARDEC ID var_init)
		{
			/*DanType varType = types.get($type.text);	
			STAttrMap typeNameMap = new STAttrMap();
			typeNameMap.put("type", varType.getEmittedType());
			typeNameMap.put("name", $name.text);
			
			StringTemplate local = null;
			// TODO when we can allocate with new we'll need this again.
			// Although it would be nice to be able to allocate locally
			// if something need not be mobile because it is never communicated.
			//if (varType.isByRef()) {
			//	local = templateLib.getInstanceOf("localByRefDec", typeNameMap);
			//}
			//else {
				local = templateLib.getInstanceOf("localValueDec", typeNameMap);
			//}
			$procDec::locals.add(local);
			
			STAttrMap initLocalsMap = new STAttrMap();
			StringTemplate initLocal;
			if (varType.isByRef()) {
				initLocal = templateLib.getInstanceOf("constructLocal", initLocalsMap);
			}
			else {
				// TODO need to deal with var_init
				initLocalsMap.put("name", $name.text);
				initLocalsMap.put("value", "0");
				initLocal = templateLib.getInstanceOf("initLocal", initLocalsMap);
			}
			
			$procDec::initLocals.add(initLocal);	
			
			StringTemplate cleanup 
				= templateLib.getInstanceOf(types.get( $type.text).getCleanupTemplateName(),
							    typeNameMap);
			$procDec::cleanup.add(cleanup);
			*/
		};

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
