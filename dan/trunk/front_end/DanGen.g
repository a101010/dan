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
		: ^('bundle' ID bundleBody)
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

bundleBody 	: ^(BUNDLE_STATEMENTS bundleStmt+);

channelDepth	: 'unbounded' | INT_LIT | ID;

channelBehavior : 'block' | 'overflow' | 'overwrite' | 'priority';

/*channelArgs	: ^(CHAN_ARGS1 channelDepth channelBehavior)
		| ^(CHAN_ARGS3 CHAN_NOBUFFER 'block')
		| ^(CHAN_ARGS2 channelDepth 'block');*/

bundleStmt	: ^(BUNDLE_STATEMENT channelDecStmt1 channelDir)
		{
			String name = $channelDecStmt1.name;
			// TODO add a mapping to the channel type
			$bundleDec::channelDeclarations.add(templateLib.getInstanceOf("localValueDec",
					new STAttrMap().put("type", "__c0bs32")
					               .put("name", name)));
			$bundleDec::channelConstructors.add(templateLib.getInstanceOf("channelConstructorCall",
					new STAttrMap().put("chanType", "__c0bs32")
					               .put("chanName", name)
					               .put("readEndId", "\"readEndId\"")
					               .put("writeEndId", "\"writeEndId\"")));
			if($channelDir.forward)
			{
				// TODO add a mapping from channel types to channel end types
				$bundleDec::readEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanR32")
					               .put("name", name)));
				$bundleDec::writeEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanW32")
					               .put("name", name)));
			}
			else
			{
				$bundleDec::readEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanW32")
					               .put("name", name)));
				$bundleDec::writeEnds.add(templateLib.getInstanceOf("localByRefDec",
					new STAttrMap().put("type", "__ChanR32")
					               .put("name", name)));
			}
		};
	
channelDecStmt1	returns [String name]
		:  ^(CHAN_VARDEC_1 chanTypeId ID)
		{
			$name = $ID.text;
		};
		
channelDecStmt2 
	:	 ^(CHAN_VARDEC_2 paramStorageClass chanTypeId ID);
		
	
channelDir 	returns [boolean forward]
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
			// labelNum is for saveState labels
			int labelNum;
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
			$procDec::labelNum = 0;
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
					               .put("initLocals", "// initLocals")
					               .put("procBodyScratchInit", scratchInit)
					               .put("statements", $block.st)
					               .put("cleanup", "// cleanup")
					               );
		};

paramList 	: ^(PARAMLIST rameses+=param*) -> paramList(params={$rameses});

genericArgList	: ^(GENERIC_ARGLIST tIds+=typeId+);
	
chanTypeId  	: ^('channel' /*genericArgList channelArgs*/ CT_REF)
		{
			System.out.println("got a chanTypeId with attached longname: " + $CT_REF.text);
		};

typeId		: SIMPLE_TYPE ID T_REF { System.out.println("DanGen: type is " + $T_REF.text); } -> typeId(id={$ID})
		| 'chanr' genericArgList T_REF { System.out.println("DanGen: type is " + $T_REF.text); }
		| 'chanw' genericArgList T_REF { System.out.println("DanGen: type is " + $T_REF.text); }
		| GENERIC_TYPE ID genericArgList T_REF {System.out.println("type is " + $T_REF.text); }; 
		
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

statement 	: whileStmt { $statement.st = $whileStmt.st; }
			| ifStmt { $statement.st = $ifStmt.st; } 
			| cifStmt  -> template(cif={"// cif statement\n"}) "<cif>" 
			| parStmt  { $statement.st =  $parStmt.st; }
			| succStmt  -> template(succ={"// succ statement\n"}) "<succ>" 
			| block  { $statement.st = $block.st; }
			| simpleStatement { $statement.st = $simpleStatement.st; };

simpleStatement 
		: varDecStmt { $simpleStatement.st = $varDecStmt.st; }
			| channelDecStmt2 -> template(chanDec={"// chanDecStmt2\n"}) "<chanDec>"
			| sendStmt { $simpleStatement.st = $sendStmt.st; }
			| receiveStmt { $simpleStatement.st = $receiveStmt.st; }
			| assignStmt { $simpleStatement.st = $assignStmt.st; }
			| returnStmt -> template(return={"// return statement\n"}) "<return>"
			| call -> template(call={"// call statement\n"}) "<call>";

block 		: ^(BLOCK statements+=statement+) -> template(statements={$statements}) "<statements>";

whileStmt 	: ^('while' exp statement) -> whileStatement(condition={$exp.st}, statements={$statement.st});

ifStmt		: ^('if' exp statement) -> ifStatement(condition={$exp.st}, statements={$statement.st});

cifStmt 	: ^('cif' ID statement);

parStmt		: ^('par' block) { $procDec::hasPar = true;} 
			-> parStatement(procType={$procDec::type.getEmittedType()}, 
					constructors={"// proc constuctors and schedule calls"}, 
					lablelNum={$procDec::labelNum}, 
					checkTerms={"// check that procs exited"}, 
					numProcs={"<number of procs>"}, 
					checkCleanExit={"<check that procs exited cleanly>"});

succStmt	: ^('succ' block);

storageClass	: 'static' | 'local' | 'mobile';

varDecStmt 	: ^(VARDEC storageClass typeId name=ID varInit[$name.text]) { $varDecStmt.st = $varInit.st; };

varInit		[String name]
		: '=' exp -> assignmentStatement(target={$name},
						 targetCleanup={"// targetCleanup"},
						 source={$exp.st},
						 sourceCleanup={"// sourceCleanup"})
			| NO_INIT {$varInit.st = new StringTemplate();};

sendStmt 	@after
  		{
  			$procDec::labelNum++;
  		}
		: ^('!' ID exp) { $procDec::hasIo = true; } 
			-> sendStatement(procType={$procDec::type.getEmittedType()}, 
			                 chanw={$ID.text}, 
			                 source={$exp.st}, 
			                 labelNum={$procDec::labelNum}, 
			                 sourceCleanup={"// source cleanup"});

receiveStmt	@after
  		{
  			$procDec::labelNum++;
  		}
  		: ^('?' from=ID target=ID) { $procDec::hasIo = true; }
			-> receiveStatement(procType={$procDec::type.getEmittedType()}, 
			                    chanr={$from.text}, 
			                    target={$target.text}, 
			                    labelNum={$procDec::labelNum}, 
			                    targetCleanup={"// targetCleanup"});

assignStmt 	: ^('=' ID exp) -> assignmentStatement(target={$ID.text}, 
					               targetCleanup={"// targetCleanup"},
					               source={$exp.st},
					               sourceCleanup={"// sourceCleanup"});

returnStmt	: ^('return' exp);


exp	 	: literal { $exp.st = $literal.st; }
		| ID  -> template(id={$ID.text}) "locals-><id>"
		| constructor -> template(construct={"// constructor\n"}) "<construct>"
		| call  { $exp.st = $call.st; }
		| ^('<' left=exp right=exp) -> binaryOp(left={$left.st}, right={$right.st}, op={"<"})
		| ^('>' left=exp right=exp) -> binaryOp(left={$left.st}, right={$right.st}, op={">"})
		| ^('<=' left=exp right=exp) -> binaryOp(left={$left.st}, right={$right.st}, op={"<="})
		| ^('>=' left=exp right=exp) -> binaryOp(left={$left.st}, right={$right.st}, op={">="})
		| ^('==' left=exp right=exp)-> binaryOp(left={$left.st}, right={$right.st}, op={"=="})
		| ^('!=' left=exp right=exp) -> binaryOp(left={$left.st}, right={$right.st}, op={"!="})
		| ^('or' left=exp right=exp) -> binaryOp(left={$left.st}, right={$right.st}, op={"||"})
		| ^('and' left=exp right=exp) -> binaryOp(left={$left.st}, right={$right.st}, op={"&&"})
		| ^('xor' left=exp right=exp) -> xorOp(left={$left.st}, right={$right.st})
		| (^('+' exp exp))=> ^('+' left=exp right=exp) 
			-> binaryOp(left={$left.st}, right={$right.st}, op={"+"})
		| (^('-' exp exp))=> ^('-' left=exp right=exp)
			-> binaryOp(left={$left.st}, right={$right.st}, op={"-"})
		| ^('*' left=exp right=exp)-> binaryOp(left={$left.st}, right={$right.st}, op={"*"})
		| ^('/' left=exp right=exp) -> binaryOp(left={$left.st}, right={$right.st}, op={"/"})
		| ^('not' operand=exp) -> unaryOp(op={"!"}, operand={$operand.st})
		| ^('+' operand=exp) ->  unaryOp(op={""}, operand={$operand.st})
		| ^('-' operand=exp) ->  unaryOp(op={"-"}, operand={$operand.st});
// TODO using binaryOp this way hard codes the operators here rather than making them part of the template
// Need to either make the operator part of the template or load a file of operator symbols.
// Really would like templates that can support various asm flavors, but will leave that for a future effort.		


pool		: 'static' | 'local' | ID;

constructor	: ^(CONSTRUCTOR pool typeId argList);
		
call		: ^(CALL ID argList) -> template(call={"// call\n"}) "<call>";

argList 	: ^(ARGLIST exp+)
		| ^(ARGLIST NO_ARG);

literal 	: 'true' { $literal.st = new StringTemplate("1"); } 
			| 'false' { $literal.st = new StringTemplate("0"); } 
			| FLOAT_LIT { $literal.st = new StringTemplate($FLOAT_LIT.text); }
			| INT_LIT {$literal.st = new StringTemplate($INT_LIT.text); };
