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
        ChannelType resolvedType;
        resolvedType = (ChannelType) typeMap.get(ctRef.toString());

        if(resolvedType != null){
            ctRef.setResolvedType(resolvedType);
            return;
        }

        if(ctRef.genericArgs == null){
            throw new TypeException(ctRef.getToken(), "protocol not specified");
        }
        if(ctRef.genericArgs.size() == 0){
            throw new TypeException(ctRef.getToken(), "protocol not specified");
        }

        // resolve protocol types
        for(TypeRef gaRef: ctRef.genericArgs){
            DanType.resolveType(gaRef, typeMap);
        }

        if(ctRef.genericArgs.size() != 1){
            // TODO pick a multi-type channel if > 1
            throw new TypeException(ctRef.getToken(), "multi-type channels not supported");
        }
        DanType protocol = ctRef.genericArgs.get(0).getResolvedType();
        // TODO handle case of the single generic arg is a protocol type

        // check the size of the generic arg type; only 32 bits currently supported
        if(protocol.isByRef()){
            if(protocol.getMobileSize() != 32){
                throw new TypeException(ctRef.getToken(), "only 32 bit channel protocol supported");
            }
        }
        else if (protocol.getStaticSize() != 32){
            throw new TypeException(ctRef.getToken(), "only 32 bit channel protocol supported");
        }

        // use __c0bs32 as the emmitted type, but tailor the generic arg list
        resolvedType = emittedChanNameMap.get("__c0bs32");
        resolvedType = new ChannelType(resolvedType, ctRef.getGenericArgs());

        ctRef.setResolvedType(resolvedType);
        typeMap.put(ctRef.toString(), resolvedType);
    }

    // containts a map of emitted channel type names to template instances
    // template instaces must be tailored to the correct protocol type
    static private final HashMap<String, ChannelType> emittedChanNameMap = new HashMap<String, ChannelType>();


    static {
        // TODO get from a file in the directory corresponding to the correct runtime
        ChannelType __c0bs32 = new ChannelType("__c0bs32", ChanDepth.finite, 0, null, ChanBehavior.block);
        emittedChanNameMap.put("__c0bs32", __c0bs32);
    }

    protected ChanDepth chanDepth1;
    protected int chanDepth2;
    protected Token chanDepth3;
    protected ChanBehavior chanBehavior;
    protected String strRep;
    protected String emittedTypeRep;

    public ChannelType(String et, ChanDepth d1, int d2, Token d3, ChanBehavior b){
        super(new CommonToken(ChannelTokenId, "channel"));
        emittedTypeRep = et;
        chanDepth1 = d1;
        chanDepth2 = d2;
        chanDepth3 = d3;
        chanBehavior = b;
    }

    protected ChannelType(ChannelType generic, ArrayList<TypeRef> ga){
        super(new CommonToken(ChannelTokenId, "channel"));
        ArrayList<DanType> newGa = new ArrayList<DanType>(ga.size());
        for(TypeRef tRef: ga){
            newGa.add(tRef.getResolvedType());
        }
        emittedTypeRep = generic.emittedTypeRep;
        chanDepth1 = generic.chanDepth1;
        chanDepth2 = generic.chanDepth2;
        chanDepth3 = generic.chanDepth3;
        chanBehavior = generic.chanBehavior;

    }



    public void setProtocol(ArrayList<DanType> p) {
        if(genericArgs != null){
            throw new RuntimeException("ChannelType protocol already set");
        }
        genericArgs = p;
    }
    
    @Override
    public String getName(){
        return "channel";
    }

    public String getLongName(){
        if(longName == null){
            longName = getName() + getGenericArgsAsString() + getChanParamsAsString();
        }
        return longName;
    }

    public String getChanParamsAsString(){
        String chanDepthStr;
        if(chanDepth1 == ChannelType.ChanDepth.finite){
            return Integer.toString(chanDepth2);
        }
        else if (chanDepth1 == ChannelType.ChanDepth.id){
            throw new NotImplementedException();
            // TODO need a way to look up the id; then as for finite
            // the id must be a compile time constant integer
        }
        else if (chanDepth1 == ChannelType.ChanDepth.unbounded){
            chanDepthStr = "unbounded";
        }
        else{
            throw new RuntimeException("unhandled channel depth: " + chanDepth1);
        }

        return "(" + chanDepthStr + ", " + chanBehavior.toString() + ")";
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

    public ChanRType getChanRType(){
        throw new NotImplementedException();
    }

    public ChanWType getChanWType(){
        throw new NotImplementedException();
    }

    @Override
    public String getEmittedType(){
        return emittedTypeRep;
    }
}