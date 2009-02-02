/*
 * ChannelReaderType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import org.antlr.runtime.Token;

public class ChannelReaderType extends DanType {
    
    public ChannelReaderType(Token protocolType){
        super(protocolType);
    }
    
    @Override
    public String getName(){
        return "ChannelReader<" + getToken().getText() + ">";
    }
}