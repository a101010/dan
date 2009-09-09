package dan.types;

import java.util.ArrayList;
import org.antlr.runtime.Token;
import dan.system.*;
import java.util.HashMap;

// TODO need to figure out where to handle generic types
// It may need to be in DanType, since protocols and channels
// can have generic type arguments.
/**
 * 
 * @author Alan
 */
public class DanType {

    static public void resolveType(TypeRef tRef, HashMap<String, DanType> typeMap){
        DanType retVal = tRef.getResolvedType();
        if(retVal != null){
            return ;
        }
        if(tRef.getName().getText().equals("channel")){
            ChanTypeRef ctRef = (ChanTypeRef) tRef;
            ChannelType.resolveType(ctRef, typeMap);
            return;
        }
        for(TypeRef tRefA: tRef.genArgs){
            resolveType(tRefA, typeMap);
        }
        DanType resolvedType = typeMap.get(tRef.getName().getText());
        if(resolvedType == null){
            throw new TypeException(tRef.getName(), "type definition not found");
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

    // in bits
    public int getStaticSize(){
        return 0;
    }

    // in bits
    public int getMobileSize(){
        return 32;
    }
    
}