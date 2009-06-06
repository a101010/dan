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
    protected ArrayList<TypeRef> genArgs;
    protected DanType resolvedType;
    protected String stringRep;

    public TypeRef(Token n, ArrayList<TypeRef> ga){
        name = n;
        genArgs = ga;
    }

    public Token getName(){
        return name;
    }

    public ArrayList<TypeRef> getGenericArgs(){
        return genArgs;
    }

    public DanType getResolvedType(){
        return resolvedType;
    }

    public void setResolvedType(DanType t){
        resolvedType = t;
    }

    @Override
    public String toString(){
        if(stringRep == null){
            stringRep = name.getText();
            if(genArgs != null){
                stringRep += "<";
                for(int i = 0; i < genArgs.size(); ++i){
                    stringRep += genArgs.get(i).toString();
                    if(i != genArgs.size() - 1){
                        stringRep += ", ";
                    }
                }
                stringRep +=">";
            }
        }
        return stringRep;
    }
}
