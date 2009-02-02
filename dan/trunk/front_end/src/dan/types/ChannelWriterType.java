/*
 * ChannelWriterType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import org.antlr.runtime.Token;

public class ChannelWriterType extends DanType {
    
    public ChannelWriterType(Token protocolType){
        super(protocolType);
    }
    
    @Override
    public String getName(){
        return "ChannelWriter<" + getToken().getText() + ">";
    }
}