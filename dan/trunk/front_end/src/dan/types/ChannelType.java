/*
 * ChannelType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */
package dan.types;

import org.antlr.runtime.Token;

/**
 * 
 * @author Alan
 */
public class ChannelType extends DanType {
      
    public ChannelType(Token protocolType){
        super(protocolType);
    }
    
    @Override
    public String getName(){
        return "SyncChannel<" + getToken().getText() + ">";
    }
}