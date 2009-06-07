/*
 * ChanRType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import org.antlr.runtime.CommonToken;
import java.util.ArrayList;

public class ChanRType extends DanType {

    static public int ChanrTokenId = 0;

    protected String strRep;
    protected String emittedTypeRep;

    /**
     *
     * @param p The channel protocol
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