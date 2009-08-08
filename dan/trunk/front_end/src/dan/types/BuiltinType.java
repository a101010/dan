/*
 * BuiltinType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */
package dan.types;

import org.antlr.runtime.Token;

public class BuiltinType extends DanType {

    public enum Builtins {

        Void,
        Bool,
        Int32,
        Uint32,
        Float32
    }
    /* 
    public static final int Void = 0;
    public static final int Bool = 1;
    //public static final int Byte = 2;
    //public static final int Uint8 = 10;
    //public static final int Uint16 = 11;
    //public static final int Uint32 = 12;
    //public static final int Uint64 = 13;
    //public static final int Int8 = 20;
    //public static final int Int16 = 21;
    public static final int Int32 = 22;
    //public static final int Int64 = 23;
    //public static final int Int = 30;
    //public static final int Num = 31;
    public static final int Float32 = 40;
    //public static final int Float64 = 41;
    //public static final int String8 = 50;
    //public static final int String16 = 51;
    //public static final int String32 = 52;
    // TODO add builtin array and channel types
    public static final int 
    // TODO add builtin exception types
    
    // TODO add base Object type
    // TODO add base CustomObject type 
     */
    public final Builtins type;

    public BuiltinType(Builtins t) {
        // TODO this is only true for the primitive types, 
        // not array and channel types
        type = t;
    }
    
    
    @Override
    public void setToken(Token t){
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getName(){
        return type.toString().toLowerCase();
    }

    // TODO some builtins will be byRef (e.g. strings)
    // will also have to override getEmittedType and getCleanupTemplate
    @Override
    public boolean isByRef(){
        return false;
    }

}