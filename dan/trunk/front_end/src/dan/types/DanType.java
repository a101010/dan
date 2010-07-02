package dan.types;

import java.util.ArrayList;
import org.antlr.runtime.Token;
import dan.system.*;
import java.io.Serializable;
import java.util.HashMap;

// TODO need to figure out where to handle generic types
// It may need to be in DanType, since protocols and channels
// can have generic type arguments.
/**
 * 
 * @author Alan
 */
public class DanType implements Serializable {

    static public void resolveType(TypeRef tRef, HashMap<String, DanType> typeMap){
        DanType resolvedType = tRef.getResolvedType();

        // check if we've already resolved it; TODO should this be an exception?
        if(resolvedType != null){
            return ;
        }

        // resolve channel types
        if(tRef.getToken().getText().equals("channel")){
            ChanTypeRef ctRef = (ChanTypeRef) tRef;
            ChannelType.resolveType(ctRef, typeMap);
            return;
        }

        // make sure we've resolved all of the generic arg types before we
        // resolve the type as a whole; 
        if(tRef.genericArgs != null){
            for(TypeRef tRefA: tRef.genericArgs){
                resolveType(tRefA, typeMap);
            }
        }

        // resolve chanr and chanw
        if(tRef.getToken().getText().equals("chanw")){
            ChanWType.resolveType(tRef, typeMap);
            return;
        }

        if(tRef.getToken().getText().equals("chanr")){
            ChanRType.resolveType(tRef, typeMap);
            return;
        }


        // is it a builtin type?
        resolvedType = BuiltinType.getBuiltinType(tRef.getToken().getText());
        if(resolvedType != null){
            tRef.setResolvedType(resolvedType);
            return;
        }

        // resolve from the map, which should include all types
        // defined in a file or imported by the file
        // TODO does this correctly handle generic args?
        resolvedType = typeMap.get(tRef.getToken().getText());
        if(resolvedType == null){
            throw new TypeException(tRef.getToken(), "type definition not found");
        }
        tRef.setResolvedType(resolvedType);
    }
    
    /**
     * In a recursive symbol, gets the rightmost type.
     * 
     * E.g. in "foo.bar.baz", returns the type of baz.
     * in "foo" returns the type of foo.
     * 
     * @param splitId
     * @param member
     * @return
     */
    static protected DanType getRightmostType(String[] splitId, DanType member){
        if(member == null)
            return null;
        if(splitId.length > 1){
            return member.getMemberType(ArrayUtils.tail(splitId));
        }
        else{
            return member;
        }
    }

    protected String name;
    protected String longName;
    protected Token token;
    protected ArrayList<DanType> genericArgs;
    
    public DanType(){
       
    }
    
    public DanType(Token t) {
        name = t.getText();
        token = t;
    }

    public DanType(String n) {
        name = n;
    }

    public void setToken(Token t) throws TypeException {
        name = t.getText();
        token = t;
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
       return name;
    }
    
    public final DanType getMemberType(String id){
        return getMemberType(id.split("."));
    }
    
    public DanType getMemberType(String [] splitId){
        return null;
    }

    public String getEmittedType(){
        return getName();
    }

    public String getCleanupTemplateName(){
        return "defaultCleanup";
    }

    public boolean isByRef(){
        return true;
    }

    public ArrayList<DanType> getGenericArgs(){
        return genericArgs;
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
            strRep += genericArgs.get(i).getName();
            if(i != genericArgs.size() - 1){
                strRep += ", ";
            }
        }
        strRep += ">";
        return strRep;
    }

    // in bits
    public int getStaticSize(){
        return 0;
    }

    // in bits
    public int getMobileSize(){
        return 32;
    }
    
}