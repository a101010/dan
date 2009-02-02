/*
 * DanType
 * 
 * Copyright 2009, Alan Grover, all rights reserved 
 * 
 */
package dan.types;

import java.util.ArrayList;
import org.antlr.runtime.Token;

// The user-definable parts of a proc type are
// parameter values
// return value
// automatic variables
// call chains (used to calculate the total memory needed 
//    for a proc instance) 
// 
// constructors etc are generated automatically
// TODO need to be able to generate Lambdas
// TODO all procs can make tail-recursive calls and deterministically deep calls, 
// but in the future procs will be able to do arbitrary recursion if they use the recursive modifier
// TODO in the hazy distant future, procs may be able to have the modifier mobile 
// but that may only work in the context of a bytecode system
// 
public class ProcType extends DanType {

    static public void ValidateName(Token t){
        if(t.getText().contains("."))
            throw new TypeException(t, "Proc type names may not contain '.'");
    }
    public ArrayList<ParamInfo> params;
    public DanType returnType;
    public ArrayList<DanType> locals;
    //public ArrayList<CallNode> callChains;  // no call chains in commstime3 example
    
    public ProcType(Token name, DanType returnType){
        super(name);
        ValidateName(name);
        returnType = returnType;
    }
}