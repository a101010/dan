/*
 * Vardec
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;
import org.antlr.runtime.Token;

/**
 * Represents a variable declaration.
 * @author Alan
 */
public class Vardec {
    static public enum StgClass{
        Static,
        Local,
        Mobile
    }

    public final TypeRef Type;
    public final Token Name;
    public final String EmittedName;
    public final StgClass StorageClass;
    
    
    public Vardec(StgClass storageClass, TypeRef type, Token name, String emittedName){
        StorageClass = storageClass;
        Type = type;
        Name = name;
        EmittedName = emittedName;
    }
}
