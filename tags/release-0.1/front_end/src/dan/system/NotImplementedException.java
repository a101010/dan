/*
 * NotImplementedException
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.system;

/**
 *
 * @author Alan
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException(){
        super("This code is not yet implemented");
    }
}
