/*
 * Scope
 *
 * Copyright 2009, Alan Grover, all rights reserved
 *
 */

package dan.types;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a block scope in a ProcType.
 * @author Alan
 */
public class Scope {
    // TODO can add a list of statements here also, if needed
    public final HashMap<String, Vardec> Symbols;

    public final ArrayList<Scope> Children;

    public final Scope Parent;

    public Scope(Scope parent){
        Symbols = new HashMap<String, Vardec>(); // key is the name
        Children = new ArrayList<Scope>();
        Parent = parent;
    }
}
