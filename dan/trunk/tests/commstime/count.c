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
Bundle_BInt * Bundle_BInt_ctor(Bundle_BInt * chans);

// ----------------------------------------------
// Count PROC type
typedef struct Count_locals_tag
{
    int32 value;
    BundleR_BInt *in;
    int32 step;
    int32 i;
    int32 iteration;

} Count_locals;

typedef struct Count_proc_tag
{
    proc                        p;
    Count_locals           locals;
} Count_proc;

// Count_locals constructor
Count_locals * Count_locals_ctor(Count_locals * locals, BundleR_BInt *in)
{
  locals->in = in; 

  
  return locals;
}

void Count_body(proc * ap, scheduler * s)
{
    int result = 0; // for the result of read and write ops
    Type type; //TODO use the Type

    
    Count_proc* p = (Count_proc*) ap;
    Count_locals* locals = &(p->locals);
    if(ap->state != _PS_READY_TO_RUN_)
        restoreState(ap->state);

    locals->step = 10;
    // targetCleanup
    // sourceCleanup
    locals->iteration = 0;
    // targetCleanup
    // sourceCleanup
    while ( (locals->iteration < 3) )
    {
        locals->i = 0;
        // targetCleanup
        // sourceCleanup
        while ( (locals->i < locals->step) )
        {
            // receive statement
            // targetCleanup
            saveState(ap->state, Count_proc_S0);
            // TODO use the type parameter
            result = locals->in->c->read( locals->in->c->channel, &type, &(locals->value), &ap->exception, ap, s);
            switch(result)
            {
            case 0 : // read value
                printf("Count_proc: read value %d from in->c\n", locals->value);
                break;
            case 1 : // blocked
                // let it read again
                goto EXIT;
            case 2 : // exception
                ap->state = _PS_EXCEPTION_;
                goto EXIT;
            default:
                printf("unexpected result from read on in->c in Count_proc_body: %d\n", result);
                ap->state = _PS_EXCEPTION_;
                ap->exception = "unexpected result from read on in->c in Count_proc_body";
                goto EXIT;
            }
            locals->i = (locals->i + 1);
            // targetCleanup
            // sourceCleanup

        }
        locals->iteration = (locals->iteration + 1);
        // targetCleanup
        // sourceCleanup

    }

    
    ap->state = _PS_CLEAN_EXIT_;
EXIT:
    if((ap->state == _PS_EXCEPTION_) || (ap->state == _PS_CLEAN_EXIT_) )
    {
        // cleanup
    }
}

// End of Count PROC type

