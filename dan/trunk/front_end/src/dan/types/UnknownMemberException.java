/*
 * UnknownMemberException
 *
 * Copyright 2011, Alan Grover, All rights reserved
 */

package dan.types;

/**
 *
 * @author Alan
 */
class UnknownMemberException extends RuntimeException {

    public UnknownMemberException(String name, String member) {
        super("Type " + name + " has no member named " + member);

    }

}
