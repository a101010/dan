/*
 * Scope
 *
 * Copyright 2009, Alan Grover, all rights reserved
 *
 */

package dan.types;

import dan.system.ArrayUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.antlr.runtime.Token;

/**
 * Represents a block scope in a ProcType.
 * @author Alan
 */
public class Scope implements Serializable {
    // TODO can add a list of statements here also, if needed
    public final HashMap<String, Vardec> Symbols;

    public final ArrayList<Scope> Children;

    public final Scope Parent;

    // IsVisited is only used by the code generator to guide its walk through
    // scopes. Do not use for any other purpose.
    public transient boolean IsVisited;

    public Scope(Scope parent){
        Symbols = new HashMap<String, Vardec>(); // key is the name
        Children = new ArrayList<Scope>();
        Parent = parent;
        IsVisited = false;
    }

    /**
     * Checks if symbol is in scope and returns the correct emitted
     * name. E.g., if a channel chan1 (type _c0bs32) is accessed as follows:
     * chan1.write
     * member 'write' is a member of __Channel32, so the correct code to
     * emit is ((__Channel32)(locals->chan1)).write
     * If not in scope, throws a ScopeException.
     * If in scope but member does not exist, throws FieldDoesNotExistException.
     * TODO does not work with methods, just fields.
     * @param symbol A Danger symbol.
     * @return The correct emitted name.
     */
    public String GetEmittedName(Token symbol){
        String symbolString = symbol.getText();
        String[] chunks = symbol.getText().split("\\.");
        Vardec v = null;
        if(chunks.length == 1){
            v = IsInScope(symbolString);
        }
        else {
            v = IsInScope(chunks[0]);
        }
        if(v == null) {
            throw new ScopeException(symbol, " has not been declared (it is out of scope)");
        }
        if(chunks.length == 1){
            return symbolString;
        }
        return v.Type.getResolvedType().getMemberWithCast(chunks[0], ArrayUtils.tail(chunks)).toString();
    }

    private Vardec IsInScope(String symbolName){
        if(Symbols.containsKey(symbolName)){
            return (Vardec) Symbols.get(symbolName);
        }
        else if (Parent != null) {
            return Parent.IsInScope(symbolName);
        }
        else {
            return null;
        }
    }
}
