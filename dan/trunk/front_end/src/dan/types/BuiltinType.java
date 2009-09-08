/*
 * BuiltinType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */
package dan.types;

import java.util.HashMap;
import org.antlr.runtime.Token;

// TODO consider chaning to PrimitiveType (only covers static fixed size builtin types)
public class BuiltinType extends DanType {

    public enum Builtins {

        Void,
        Bool,
        Int32,
        UInt32,
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
    //public static final int Int = 30; // TODO move to a mobile builtins class
    //public static final int Num = 31; // TODO move to a mobile builtins class
    public static final int Float32 = 40;
    //public static final int Float64 = 41;
     */

    private static final HashMap<String, BuiltinType> typeMap = new HashMap<String, BuiltinType>();

    static {
        // TODO load from a file representing the targeted runtime instead
        // for instance, is it a runtime in which bools are 64 bits?
        typeMap.put(Builtins.Void.toString().toLowerCase(), new BuiltinType(Builtins.Void, 0, "void"));
        typeMap.put(Builtins.Bool.toString().toLowerCase(), new BuiltinType(Builtins.Bool, 32, "uint32"));
        typeMap.put(Builtins.Int32.toString().toLowerCase(), new BuiltinType(Builtins.Int32, 32, "int32"));
        typeMap.put(Builtins.UInt32.toString().toLowerCase(), new BuiltinType(Builtins.UInt32, 32, "uint32"));
        typeMap.put(Builtins.Float32.toString().toLowerCase(), new BuiltinType(Builtins.Float32, 32, "float"));
    }

    public static BuiltinType getBuiltinType(String t){
        return typeMap.get(t.toLowerCase());
    }

    public final Builtins type;
    public final int staticSize;
    public final String emittedType;

    protected BuiltinType(Builtins t, int ssize, String et) {
        type = t;
        staticSize = ssize;
        emittedType = et;
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

    @Override
    public int getStaticSize(){
        return staticSize;
    }

    @Override
    public int getMobileSize(){
        // TODO other possibilities include
        //      same as static size
        //      throw an exception
        return 0;
    }

}