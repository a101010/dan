/*
 * ScopeException
 *
 * Copyright 2011, Alan Grover, All rights reserved
 */

package dan.types;

import org.antlr.runtime.Token;

// TODO not sure RuntimeException is what we want here.  Will depend on
// error reporting design, which is still to come.
/**
 *
 * @author Alan
 */
public class ScopeException extends RuntimeException {
    public final Token SymbolName;

    public final String Problem;

    public ScopeException(Token symbolName, String problem){
        super(symbolName.getText()
                + ": " + problem
                + " at " + symbolName.getLine()
                + ":" + symbolName.getCharPositionInLine());
        SymbolName = symbolName;
        Problem = problem;
    }
}
