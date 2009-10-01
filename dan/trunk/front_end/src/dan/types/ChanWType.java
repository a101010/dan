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
        ChanWType resolvedType = typeMap.get(tRef.)
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
            strRep = "chanw<";
            for(int i = 0; i < genericArgs.size(); ++i){
                strRep += genericArgs.get(i).getName();
                if(i != genericArgs.size() - 1){
                    strRep += ", ";
                }
            }
            strRep += ">";
        }
        return strRep;
    }

    @Override
    public String getEmittedType(){
        if(emittedTypeRep == null){
            emittedTypeRep = "ChanW_";
            for(int i = 0; i < genericArgs.size(); ++i){
                emittedTypeRep += genericArgs.get(i).getEmittedType();
                if(i != genericArgs.size() - 1)
                    emittedTypeRep += "_";
            }
        }
        return emittedTypeRep;
    }
}