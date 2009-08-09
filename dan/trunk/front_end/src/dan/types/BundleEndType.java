/*
 * BundleEndType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import org.antlr.runtime.Token;
import java.util.ArrayList;

/**
 * Represents a Bundle end structure.
 * @author Alan
 */
public class BundleEndType extends DanType {
    public enum Directions { Read, Write }
    
    public BundleEndType(Token t, Directions d){
        super(t);
        Direction = d;
        emittedType = null;
    }
    
    public final Directions Direction;
    
    @Override
    public String getName(){
        return token.getText() + "." + Direction.toString();
    }
    
    // TODO perhaps these need to be validated; they should be restricted
    // to channel reader and writer types
    public ArrayList<Vardec> ChanEnds;
    private String emittedType;
    
    @Override
    public DanType getMemberType(String[] splitId){
        DanType member = null;
        for(Vardec v : ChanEnds){
            if(v.Name.getText().equals(splitId[0])){
                member = v.Type.getResolvedType();
                break;
            }
        }
        
        return getRightmostType(splitId, member);
    }

    @Override
    public String getEmittedType(){
        if(emittedType == null){
            if(Direction == Directions.Read) {
                emittedType = "BundleR_" + token.getText();
            }
            else {
                emittedType = "BundleW_" + token.getText();
            }
        }
        return emittedType;
    }

    @Override
    public String getCleanupTemplateName(){
        return "bundleEndCleanup";
    }
}
