/*
 * ChanRType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import dan.system.NotImplementedException;
import org.antlr.runtime.CommonToken;
import java.util.ArrayList;
import java.util.HashMap;

public class ChanRType extends DanType {

    static public int ChanrTokenId = 0;

    static public void resolveType(TypeRef tRef, HashMap<String, DanType> typeMap){
        throw new NotImplementedException();
    }

    protected String strRep;
    protected String emittedTypeRep;

    /**
     * A reference to a channel type.
     *
     * Type references are resolved after the first compiler pass.
     * The channel parameters are assigned when the channel is "constructed".
     * Contruction completes the type reference (i.e. has compile time info
     * necessary to select the correct channel type) as well as indicating
     * run-time construction.
     */
    public ChanRType(ArrayList<TypeRef> p){
        super(new CommonToken(ChanrTokenId, "'chanr'"));
        genericArgs = p;
    }
    
    @Override
    public String getName(){
        if(strRep == null){
            strRep = "chanr<";
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
            emittedTypeRep = "ChanR_";
            for(int i = 0; i < genericArgs.size(); ++i){
                emittedTypeRep += genericArgs.get(i).resolvedType.getEmittedType();
                if(i != genericArgs.size() - 1)
                    emittedTypeRep += "_";
            }
        }
        return emittedTypeRep;
    }


}