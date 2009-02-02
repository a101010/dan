package dan.types;

import org.antlr.runtime.Token;

// TODO need to figure out where to handle generic types
// It may need to be in DanType, since protocols and channels
// can have generic type arguments.
/**
 * 
 * @author Alan
 */
public class DanType {

    protected Token token;
    
    public DanType(){
        
    }
    
    public DanType(Token t) {
        token = t;
    }

    public void setToken(Token t) throws TypeException {
        token = t;
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
       return token.getText();
    }
}