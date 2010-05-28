#include <stdio.h> // TODO write string, logging, and io libs for Danger
#include <time.h>  // TODO write a time lib for Danger
#include <__struntime.h>

// ----------------------------------------------
// BInt channel bundle type

typedef struct BundleR_BInt_tag
{
    __ChanR32 *c;


} BundleR_BInt;

typedef struct BundleW_BInt_tag
{
    __ChanW32 *c;


} BundleW_BInt;

// backing store for BInt channel bundle
typedef struct Bundle_BInt_tag
{

    BundleR_BInt   *read;
    BundleW_BInt   *write;
    BundleR_BInt   readStore;
    BundleW_BInt   writeStore;
    __c0bs32 c;


} Bundle_BInt;


// BInt_chans constructor
Bundle_BInt * Bundle_BInt_ctor(Bundle_BInt * chans)
{
    __c0bs32_ctor( &(chans->c), "readEndId", "writeEndId");
    chans->readStore.c = chans->c.ends.read;
    chans->c.ends.read = 0; // because it is a mobile type
    chans->writeStore.c = chans->c.ends.write;
    chans->c.ends.write = 0;

    
    chans->read = &(chans->readStore);
    chans->write = &(chans->writeStore);
    return chans;
}


// End of BInt bundle type
