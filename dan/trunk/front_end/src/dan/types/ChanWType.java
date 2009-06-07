/*
 * ChanWType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import org.antlr.runtime.CommonToken;
import java.util.ArrayList;

/**
 * Represents a channel writer end.
 * @author Alan
 */
public class ChanWType extends DanType {
    
    static public int ChanwTokenId = 0;

    protected String strRep;
    protected String emittedTypeRep;

    /**
     *
     * @param p The channel protocol
     */
    public ChanWType(ArrayList<TypeRef> p){
        super(new CommonToken(ChanwTokenId, "'chanw'"));
        genericArgs = p;
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
                emittedTypeRep += genericArgs.get(i).resolvedType.getEmittedType();
                if(i != genericArgs.size() - 1)
                    emittedTypeRep += "_";
            }
        }
        return emittedTypeRep;
    }
}