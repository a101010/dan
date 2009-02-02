/*
 * Vardec
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

/**
 * Represents a variable declaration.
 * @author Alan
 */
public class Vardec {
    public final DanType Type;
    public final String Name;
    
    public Vardec(DanType type, String name){
        Type = type;
        Name = name;
    }
}
