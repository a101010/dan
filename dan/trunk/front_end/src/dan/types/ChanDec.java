/*
 * ChanDec
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import java.io.Serializable;
import org.antlr.runtime.Token;

/**
 * Represents a channel declaration
 * @author Alan
 */
public class ChanDec implements Serializable {
    public final ChannelType ChanType;
    public final Token ChanName;
    public final Token ChanDir;
    
    public ChanDec(ChannelType chanType, Token chanName, Token chanDir){
        ChanType = chanType;
        ChanName = chanName;
        ChanDir = chanDir;
    }
}
