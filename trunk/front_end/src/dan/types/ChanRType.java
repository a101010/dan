/*
 * ChanRType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import java.io.Serializable;
import org.antlr.runtime.CommonToken;
import java.util.ArrayList;
import java.util.HashMap;

public class ChanRType extends DanType implements Serializable {

    static public int ChanrTokenId = 0;

    static public void resolveType(TypeRef tRef, HashMap<String, DanType> typeMap){

        ChanRType resolvedType = (ChanRType) typeMap.get(tRef.toString());
        if(resolvedType == null){
            // use GenArgsUtils.getMaxSizeByRef to find out:
            //  - what our biggest size is (TODO 32 or 64, need to support arbitrary sized statically allocated also,
            //    TODO only 32 bit supported right now)
            //  - whether we can use simpler 1 type channels or need to use channels that
            //    handle multiple types (TODO only single type supported right now)

            // Heh, right now we could just spit out the only combination we support, but lets at least put
            // the plumbing in place and verify it works for that case.

            // First, resolve the TypeRef generic type arg list
            ArrayList<TypeRef> genArgs = tRef.getGenericArgs();
            if(genArgs == null){
                // This should never happen; it should be caught by the parser, not at type resolution time.
                throw new RuntimeException("fix the parser; a chanw type must have generic args and this should be caught in parsing");
            }
            if(genArgs.size() == 0){
                // This should never happen; it should be caught by the parser, not at type resolution time.
                throw new RuntimeException("fix the parser; a chanw type must have generic args and this should be caught in parsing");
            }
            ArrayList<DanType> resolvedGenArgs = new ArrayList<DanType>(genArgs.size());
            for(TypeRef tr: genArgs){
                DanType.resolveType(tr, typeMap);
                resolvedGenArgs.add(tr.getResolvedType());
            }

            int largestType;
            boolean singleType;
            ArrayList<Integer> sizeAndNumber = GenArgsUtils.getMaxSizeByRef(resolvedGenArgs);
            largestType = sizeAndNumber.get(0);
            if(sizeAndNumber.get(1) == 1){
                singleType = true;
            }
            else{
                singleType = false;
            }
            if(largestType > 64){
                throw new RuntimeException("types with mobile (byRef) size larger than 64 not supported");
            }
            if(largestType > 32){
                throw new RuntimeException("types with mobile (byRef) size larger than 32 not supported");
            }
            if(largestType < 32){
                // we can fit smaller sizes into a 32 bit slot
                largestType = 32;
            }
            if(largestType == 32){
                if(singleType){
                    resolvedType = emittedChanRNameMap.get("__ChanR32");
                    tRef.setResolvedType(resolvedType);
                }
                else{
                    throw new RuntimeException("protocols with multilple types not supported");
                }
            }
        }
    }

    // containts a map of emitted channel reader end type names to template instances
    // template instaces must be tailored to the correct protocol type
    static private final HashMap<String, ChanRType> emittedChanRNameMap = new HashMap<String, ChanRType>();


    static {
        // TODO get from a file in the directory corresponding to the correct runtime
        ChanRType __ChanR32 = new ChanRType("__ChanR32");
        emittedChanRNameMap.put("__ChanR32", __ChanR32);
    }

    protected String strRep;
    protected String emittedTypeName;

    /**
     * Create a generic ChanRType that needs to have its generic arg list
     * specified still.
     * @param etn The emitted type name.
     */
    protected ChanRType(String etn){
        super(new CommonToken(ChanrTokenId, "chanr"));
        emittedTypeName = etn;
        genericArgs = null;
    }

    /**
     * Create a ChanRType tailored from a generic emitted type.
     * @param generic - the generic ChanRType to tailor
     * @param ga - the channel protocol to tailor to
     */
    public ChanRType(ChanRType generic, ArrayList<DanType> ga){
        super(generic.getToken());
        emittedTypeName = generic.emittedTypeName;
        genericArgs = ga;
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

        return emittedTypeName;
    }


}