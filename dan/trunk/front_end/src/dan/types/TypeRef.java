/*
 * TypeRef.java
 *
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import org.antlr.runtime.Token;
import java.util.ArrayList;

/**
 * A reference to a type.
 *
 * Type references are resolved after the first parsing pass.
 * @author Alan
 */
public class TypeRef {
    
    protected Token name;
    protected ArrayList<TypeRef> genericArgs;
    protected DanType resolvedType;
    protected String stringRep;

    public TypeRef(Token n, ArrayList<TypeRef> ga){
        name = n;
        genericArgs = ga;
    }

    public TypeRef(Token n){
        name = n;
    }

    public Token getName(){
        return name;
    }

    public ArrayList<TypeRef> getGenericArgs(){
        return genericArgs;
    }

    public DanType getResolvedType(){
        return resolvedType;
    }

    public void setResolvedType(DanType t){
        resolvedType = t;
    }

    public String getGenericArgsAsString(){
        String strRep = "<";
        for(int i = 0; i < genericArgs.size(); ++i){
            strRep += genericArgs.get(i).getName().getText();
            if(i != genericArgs.size() - 1){
                strRep += ", ";
            }
        }
        strRep += ">";
        return strRep;
    }
    
    @Override
    public String toString(){
        if(stringRep == null){
            stringRep = name.getText();
            if(genericArgs != null){
                stringRep += "<";
                for(int i = 0; i < genericArgs.size(); ++i){
                    stringRep += genericArgs.get(i).toString();
                    if(i != genericArgs.size() - 1){
                        stringRep += ", ";
                    }
                }
                stringRep +=">";
            }
        }
        return stringRep;
    }
}
