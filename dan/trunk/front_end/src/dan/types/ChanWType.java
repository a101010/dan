/*
 * ChanWType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import dan.system.NotImplementedException;
import org.antlr.runtime.CommonToken;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a channel writer end.
 * @author Alan
 */
public class ChanWType extends DanType {
    
    static public int ChanwTokenId = 0;

    static public void resolveType(TypeRef tRef, HashMap<String, DanType> typeMap){

        ChanWType resolvedType = (ChanWType) typeMap.get(tRef.toString());
        if(resolvedType == null){
            // TODO this won't work; need to use GenArgsUtils.getMaxSizeByRef
            ChannelType chanType = (ChannelType) typeMap.get("channel" + tRef.getGenericArgsAsString());
            if(chanType == null){


                TypeRef ctRef = new ChanTypeRef(new CommonToken(ChannelType.ChannelTokenId, "channel"), tRef.getGenericArgs());
                ChannelType.resolveType(ctRef, typeMap);
                chanType = (ChannelType) ctRef.getResolvedType();
            }
            resolvedType = ((ChannelType) typeMap.get("channel" + tRef.getGenericArgsAsString())).getChanWType();
        }
    }

    protected String strRep;
    protected String emittedTypeName;

    /**
     * Create a generic ChanWType that needs to have its generic arg list
     * specified still.
     * @param etn The emitted type name.
     */
    protected ChanWType(String etn){
        super(new CommonToken(ChanwTokenId, "chanw"));
        emittedTypeName = etn;
        genericArgs = null;
    }

    public ChanWType(ChanWType generic, ArrayList<DanType> ga){
        super(generic.getToken());
        emittedTypeName = generic.emittedTypeName;
        genericArgs = ga;
    }


    @Override
    public String getName(){
        if(strRep == null){
            strRep = "chanw" + getGenericArgsAsString();
        }
        return strRep;
    }

    @Override
    public String getEmittedType(){
        return emittedTypeName;
    }
}