/*
 * TypeException
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import org.antlr.runtime.Token;

// TODO not sure RuntimeException is what we want here.  Will depend on 
// error reporting design, which is still to come.
/**
 * 
 * @author Alan
 */
public class TypeException extends RuntimeException {
    public final Token TypeName;
    
    public final String Problem;
    
    public TypeException(Token typeName, String problem){
        super(typeName.getText() 
                + ": " + problem 
                + " at " + typeName.getLine() 
                + ":" + typeName.getCharPositionInLine());
        TypeName = typeName;
        Problem = problem;
    }
    
    
}