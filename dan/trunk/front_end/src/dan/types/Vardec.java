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

    public final boolean IsParam;
    public final TypeRef Type;
    public final Token Name;
    public final String EmittedName;
    public final StgClass StorageClass;
    
    
    public Vardec(StgClass storageClass, TypeRef type, Token name, String emittedName, boolean isParam){
        StorageClass = storageClass;
        Type = type;
        Name = name;
        EmittedName = emittedName;
        IsParam = isParam;
    }

    public String getEmittedType(){
        return Type.getResolvedType().getEmittedType();
    }

    public boolean isByRef(){
        // if it is static and not a param we need to allocate storage for it
        // even if it is byRef as a param
        if(StorageClass == StgClass.Static && !IsParam)
            return false;
        return Type.getResolvedType().isByRef();
    }
}
