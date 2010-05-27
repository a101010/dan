/*
 * BundleType
 * 
 * Copyright 2009, Alan Grover, All rights reserved
 * 
 */
package dan.types;

import java.util.ArrayList;
import org.antlr.runtime.Token;
import dan.system.*;

// Bundles produce a plethora of types.
// Each bundle end has a type, each channel in the bundle
// has a type and two channel end types.
// In addition, each bundle can have a protocol type.
// Channels may also have a protocol type.
// In a bundle, these types are nested types of the bundle.

// TODO someday might be nice to allow channel types outside a
// bundle.  However, I'd like to focus on bundles first.

// To support the initial commstime3.dan test we only need a subset
// of bundle functionality.  This subset does not include bundle
// protocols or channel protocols other than simple built-in types.

// TODO Channel ends may be detatched from a bundle end.
// They can be returned explicitly as part of a bundle protocol,
// or implicitly when the channel end is released.
// Trying to access a detached channel end in a bundle returns
// null.

// For the initial implementation, a bundle type produces
//  Bundle type
//  Two Bundle-end types
//  Channel types
//  Channel end types
public class BundleType extends DanType {

    public ArrayList<ChanDec> Channels;
    public final BundleEndType Reader;
    public final BundleEndType Writer;
    
    static public void ValidateName(Token token){
        if(token.getText().contains("."))
            throw new TypeException(token, "Bundle type name may not contain '.'");
    }
    
    public BundleType(Token t, BundleEndType writer, BundleEndType reader){
        super(t);
        ValidateName(t);
        // TODO consider making this Read and Write rather than Reader and Writer
        Writer = writer;
        Reader = reader;
        if(Writer.Direction != BundleEndType.Directions.Write
                | Reader.Direction != BundleEndType.Directions.Read)
            throw new RuntimeException("inconsistent bundle end types");
    }
    
    @Override
    public DanType getMemberType(String[] splitId){
        BundleEndType member = null;
        if(splitId[0].equals("Read")){
            
            member = Reader;
        }
        else if(splitId[0].equals("Write")){
            member = Writer;
        }
        
        return getRightmostType(splitId, member);
    }

    @Override
    public String getEmittedType(){
        return "Bundle_" + getName();
    }
    
}