package dan.types;

import java.util.HashMap;
import java.util.ArrayList;
import org.antlr.runtime.Token;

public class DanType {

    protected Token token;

    public void setToken(Token n)
            throws TypeException {
        token = n;
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
       return token.getText();
    }
}