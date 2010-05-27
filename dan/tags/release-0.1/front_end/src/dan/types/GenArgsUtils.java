/*
 * GenArgsUtils.java
 *
 * Copyright 2009, Alan Grover, All Rights Reserved
 */

package dan.types;

import java.util.ArrayList;

/**
 * GenArgsUtils contains utility methods for dealing with Generic Type Argument lists.
 *
 *
 * @author Alan
 */
public class GenArgsUtils {

    /**
     * Gets the maxium size of the largest arg (assuming everying is either byRef or primitive) and the number of args.
     * @param ga The array of generic argument types.
     * @return Index 0 is the size of the largest element, index 1 is the number of args.
     */
    static public ArrayList<Integer> getMaxSizeByRef(ArrayList<DanType> ga){
        ArrayList<Integer> retVal = new ArrayList<Integer>(2);
        int biggest = 0;
        int numArgs = 0;
        for(DanType t: ga){
            // TODO a Protocol type needs some special handling, since it is a single arg
            // but implies more than one
            int size = t.getMobileSize();
            if(size > biggest){
                biggest = size;
            }
            ++numArgs;
        }
        retVal.add(biggest);
        retVal.add(numArgs);
        return retVal;
    }

}
