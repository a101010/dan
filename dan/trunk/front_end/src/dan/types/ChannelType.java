/*
 * ChannelType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */
package dan.types;

import java.util.ArrayList;
import org.antlr.runtime.Token;
import org.antlr.runtime.CommonToken;

/**
 * 
 * @author Alan
 */
public class ChannelType extends DanType {

    public enum ChanBehavior { block, overflow, overwrite, priority }
    public enum ChanDepth { unbounded, finite }

    static public int ChannelTokenId = 0;

    protected ChanDepth chanDepth1;
    protected int chanDepth2;
    protected Token chanDepth3;
    protected ChanBehavior chanBehavior;
    protected String strRep;
    protected String emittedTypeRep;


    public ChannelType(ArrayList<TypeRef> p, ChanDepth d1, int d2, Token d3, ChanBehavior b){
        super(new CommonToken(ChannelTokenId, "'channel'"));
        genericArgs = p;
        chanDepth1 = d1;
        chanDepth2 = d2;
        chanDepth3 = d3;
        chanBehavior = b;
    }
    
    @Override
    public String getName(){
        if(strRep == null){
            strRep = "channel<";
            for(int i = 0; i < genericArgs.size(); ++i){
                strRep += genericArgs.get(i).getName();
                if(i != genericArgs.size() - 1){
                    strRep += ", ";
                }
            }
            strRep += ">(" 
                    + (chanDepth1 == ChanDepth.unbounded ? chanDepth1 : chanDepth2).toString()
                    + ", "
                    + chanBehavior.toString()
                    + ")";
        }
        return strRep;
    }

    public ChanDepth getChanDepth1(){
        return chanDepth1;
    }

    public int getChanDepth2(){
        return chanDepth2;
    }

    public ChanBehavior getChanBehavior(){
        return chanBehavior;
    }

    @Override
    public String getEmittedType(){
        if(emittedTypeRep == null){
            emittedTypeRep = "Channel_";
            for(int i = 0; i < genericArgs.size(); ++i){
                emittedTypeRep += genericArgs.get(i).getResolvedType().getEmittedType();
                emittedTypeRep += "_";
            }
            emittedTypeRep += (chanDepth1 == ChanDepth.unbounded ? chanDepth1 : chanDepth2).toString()
                    + "_"
                    + chanBehavior.toString();
        }
        return emittedTypeRep;
    }
}