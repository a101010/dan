/*
 * TypeRef.java
 *
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import java.io.Serializable;
import org.antlr.runtime.Token;
import java.util.ArrayList;

/**
 * A reference to a type.
 *
 * Type references are resolved after the first parsing pass.
 * @author Alan
 */
public class TypeRef implements Serializable {
    
    protected Token token;
    protected ArrayList<TypeRef> genericArgs;
    protected DanType resolvedType;
    protected String name;
    protected String longName;

    public TypeRef(Token t, ArrayList<TypeRef> ga){
        token = t;
        genericArgs = ga;
        name = token.getText();
    }

    public TypeRef(Token t){
        token = t;
        name = token.getText();
    }

    public Token getToken(){
        return token;
    }

    public String getName(){
        return name;
    }

    public String getLongName(){
        if(longName == null){
            longName = name + getGenericArgsAsString();
        }
        return longName;
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
        if(genericArgs == null){
            return "";
        }
        if(genericArgs.size() < 1){
            return "";
        }
        String strRep = "<";
        for(int i = 0; i < genericArgs.size(); ++i){
            strRep += genericArgs.get(i).getLongName();
            if(i != genericArgs.size() - 1){
                strRep += ", ";
            }
        }
        strRep += ">";
        return strRep;
    }
    
    @Override
    public String toString(){
        return getLongName();
    }
}
