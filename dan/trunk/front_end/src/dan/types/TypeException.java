package dan.types;

import org.antlr.runtime.Token;

public class TypeException extends Exception {
    // TODO probably want to override toString

    public final Token token;

    public TypeException(Token t) {
        token = t;
    }
}