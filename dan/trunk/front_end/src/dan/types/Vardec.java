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
    public final DanType Type;
    public final Token Name;
    
    public Vardec(DanType type, Token name){
        Type = type;
        Name = name;
    }
}
