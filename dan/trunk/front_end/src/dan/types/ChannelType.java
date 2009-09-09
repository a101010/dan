/*
 * ChannelType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */
package dan.types;

import dan.system.NotImplementedException;
import java.util.ArrayList;
import java.util.HashMap;
import org.antlr.runtime.Token;
import org.antlr.runtime.CommonToken;

/**
 * 
 * @author Alan
 */
public class ChannelType extends DanType {

    public enum ChanBehavior {
        block,      // when the channel is full, the writer must block
                    //(Note: this is the only valid value if chanDepth1 is finite
                    // and chanDepth2 is zero, or chanDepth1 is parameterized and
                    // zero is supplied as the argument in the bundle constructor)
        overflow,   // when the channel is full, additional messages are lost
        overwrite,  // when the channel is full, the oldest message is overwritten
        priority    // when the channel is full, the lowest priority message is overwritten
                    // Note: this requires a prioritized protocol
    }
    public enum ChanDepth { 
        unbounded,  // the channel does not have a finite buffer; the buffer
                    // may grow without bound (this is always dynamically
                    // allocated
        finite,     // the channel has a pre-allocated buffer of fixed size
                    // the size is given in chanDepth2
        id          // the channel's depth is given by an argument
                    // which can be a symbolic constant, a bundle
                    // constructor parameter, or a runtime variable of
                    // integer type.  A bundle constructor parameter
                    // must resolve to either a constant or an integer.
                    // A runtime value can only be used with a dynamically
                    // allocated channel.
    }

    static public int ChannelTokenId = 0;

    static public void resolveType(ChanTypeRef ctRef, HashMap<String, DanType> typeMap){
        throw new NotImplementedException();
    }

    // containts a map of emitted channel type names to template instances
    // template instaces must be tailored to the correct protocol type
    static private final HashMap<String, ChannelType> emittedChanNameMap = new HashMap<String, ChannelType>();

    static {
        // TODO get from a file in the directory corresponding to the correct runtime
        emittedChanNameMap.put("__c0bs32", new ChannelType("__c0bs32", ChanDepth.finite, 0, null, ChanBehavior.block));
    }

    protected ChanDepth chanDepth1;
    protected int chanDepth2;
    protected Token chanDepth3;
    protected ChanBehavior chanBehavior;
    protected String strRep;
    protected String emittedTypeRep;

    public ChannelType(String et, ChanDepth d1, int d2, Token d3, ChanBehavior b){
        super(new CommonToken(ChannelTokenId, "'channel'"));
        emittedTypeRep = et;
        chanDepth1 = d1;
        chanDepth2 = d2;
        chanDepth3 = d3;
        chanBehavior = b;
    }



    public void setProtocol(ArrayList<DanType> p) {
        if(genericArgs != null){
            throw new RuntimeException("ChannelType protocol already set");
        }
        genericArgs = p;
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

    /**
     * Gets chanDepth2.
     * chanDepth2 is an integer representing the size of the channel buffer.
     * Zero means the channel is fully synchronous; the writer must block
     * until the reader has read the channel.
     * Only valid if chanDepth1 == finite.
     * @return
     */
    public int getChanDepth2(){
        return chanDepth2;
    }

    /**
     * Gets chanDepth3.
     * chanDepth3 represents the token of the bundle parameter
     * which will be used to specify either an integer or the keyword
     * 'unbounded'.  Only valid in channel bundle declarations.  Only valid
     * if chanDepth1 == parameterized.
     * @return
     */
    public Token getChanDepth3(){
        return chanDepth3;
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