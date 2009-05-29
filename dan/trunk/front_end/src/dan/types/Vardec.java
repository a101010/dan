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

    public final DanType Type;
    public final Token Name;
    public final StgClass StorageClass;
    
    
    public Vardec(StgClass storageClass, DanType type, Token name){
        StorageClass = storageClass;
        Type = type;
        Name = name;
    }
}
