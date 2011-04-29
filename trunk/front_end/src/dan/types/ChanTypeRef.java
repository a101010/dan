/*
 * ChanTypeRef.java
 *
 * Copyright 2009, Alan Grover, All rights reserved
 */

package dan.types;

import dan.system.NotImplementedException;
import java.io.Serializable;
import java.util.ArrayList;
import org.antlr.runtime.Token;

/**
 *
 * @author Alan
 */
public class ChanTypeRef extends TypeRef implements Serializable {
    protected ChannelType.ChanDepth chanDepth1;
    protected int chanDepth2;
    protected Token chanDepth3;
    protected ChannelType.ChanBehavior chanBehavior;
    protected boolean chanArgsSet = false;

    public ChanTypeRef(Token n, ArrayList<TypeRef> ga){
        super(n, ga);
    }

    public void setChanArgs(ChannelType.ChanDepth cd1, int cd2, Token cd3, ChannelType.ChanBehavior cb) {
        if(chanArgsSet){
            throw new RuntimeException("ChanTypeRef: attempt to setChanArgs twice");
        }
        chanDepth1 = cd1;
        chanDepth2 = cd2;
        chanDepth3 = cd3;
        chanBehavior = cb;
        chanArgsSet = true;
    }

    public ChannelType.ChanDepth getChanDepth1() {
        if(!chanArgsSet){
            throw new RuntimeException("ChanTypeRef: attempt to getChanDepth1 before setChanArgs");
        }
        return chanDepth1;
    }

    /**
     * Gets chanDepth2.
     * chanDepth2 is an integer representing the size of the channel buffer.
     * Zero means the channel is fully synchronous; the writer must block
     * until the reader has read the channel.
     * Only valid if chanDepth1 == finite.
     * @return
     */
    public int getChanDepth2() {
        if(!chanArgsSet){
            throw new RuntimeException("ChanTypeRef: attempt to getChanDepth2 before setChanArgs");
        }
        return chanDepth2;
    }

    /**
     * Gets chanDepth3.
     * chanDepth3 represents the token of the bundle parameter
     * which will be used to specify either an integer or the keyword
     * 'unbounded'.  Only valid in channel bundle declarations.  Only valid
     * if chanDepth1 == parameterized.
     * @return
     */
    public Token getChanDepth3() {
        if(!chanArgsSet){
            throw new RuntimeException("ChanTypeRef: attempt to getChanDepth3 before setChanArgs");
        }
        return chanDepth3;
    }

    public ChannelType.ChanBehavior getChanBehavior() {
        if(!chanArgsSet){
            throw new RuntimeException("ChanTypeRef: attempt to getChanBehavior before setChanArgs");
        }
        return chanBehavior;
    }

    @Override
    public String getName(){
        return "channel";
    }

    @Override
    public String getLongName(){
        if(longName == null){
            longName = "channel" + getGenericArgsAsString() + getChanParamsAsString();
        }
        return longName;
    }

    public String getChanParamsAsString(){
        String chanDepthStr;
        ChannelType.ChanDepth cd1 = getChanDepth1();
        if(cd1 == ChannelType.ChanDepth.finite){
            return Integer.toString(chanDepth2);
        }
        else if (cd1 == ChannelType.ChanDepth.id){
            throw new NotImplementedException();
            // TODO need a way to look up the id; then as for finite
            // the id must be a compile time constant integer
        }
        else if (cd1 == ChannelType.ChanDepth.unbounded){
            chanDepthStr = "unbounded";
        }        
        else{
            throw new RuntimeException("unhandled channel depth: " + chanDepth1);
        }

        return "(" + chanDepthStr + ", " + chanBehavior.toString() + ")";
    }
}
