/*
 * ArrayUtils
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.system;
import java.lang.reflect.Array;

/**
 *
 * @author Alan
 */
public class ArrayUtils {
    static public <T> T[] tail(T[] array){
        T[] array1 = (T[])Array.newInstance(
                array.getClass().getComponentType(), array.length - 1);
        for(int i = 1; i < array.length; ++i){
            array1[i - 1] = array[i];
        }
        return array1;
    }
    
}
