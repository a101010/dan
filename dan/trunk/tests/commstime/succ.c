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
// Succ PROC type
typedef struct Succ_locals_tag
{
    int32 value;
    BundleR_BInt *in;
    BundleW_BInt *out;

} Succ_locals;

typedef struct Succ_proc_tag
{
    proc                        p;
    Succ_locals           locals;
} Succ_proc;

// Succ_locals constructor
Succ_locals * Succ_locals_ctor(Succ_locals * locals, BundleR_BInt *in, BundleW_BInt *out)
{
  locals->in = in; 
  locals->out = out; 

  
  return locals;
}

void Succ_body(proc * ap, scheduler * s)
{
    int result = 0; // for the result of read and write ops
    Type type; //TODO use the Type

    
    Succ_proc* p = (Succ_proc*) ap;
    Succ_locals* locals = &(p->locals);
    if(ap->state != _PS_READY_TO_RUN_)
        restoreState(ap->state);

    while ( 1 )
    {
        // receive statement
        // targetCleanup
        saveState(ap->state, Succ_proc_S0);
        // TODO use the type parameter
        result = locals->in->c->read( locals->in->c->channel, &type, &(locals->value), &ap->exception, ap, s);
        switch(result)
        {
        case 0 : // read value
            printf("Succ_proc: read value %d from in->c\n", locals->value);
            break;
        case 1 : // blocked
            // let it read again
            goto EXIT;
        case 2 : // exception
            ap->state = _PS_EXCEPTION_;
            goto EXIT;
        default:
            printf("unexpected result from read on in->c in Succ_proc_body: %d\n", result);
            ap->state = _PS_EXCEPTION_;
            ap->exception = "unexpected result from read on in->c in Succ_proc_body";
            goto EXIT;
        }
        // send statement
        // TODO use the type parameter
        result = locals->out->c->write( locals->out->c->channel, 0, (locals->value + 1), (void **) &ap->exception, ap, s);
        switch(result)
        {
        case 0 : // wrote value
            printf("Succ_proc: wrote (locals->value + 1) to out->c\n", (locals->value + 1));
            break;
        case 1 : // blocked
            saveState(ap->state, Succ_proc_S1);
            if(result == 1)
                goto EXIT;
            result = locals->out->c->writeHandshake( locals->out->c->channel, (void **) &ap->exception, ap, s);
            switch(result)
            {
            case 0 : // wrote value
                printf("Succ_proc: wrote (locals->value + 1) to out->c\n", (locals->value + 1));
                break;
            case 1 : // blocked
                goto EXIT;
            case 2 : // exception
                ap->state = _PS_EXCEPTION_;
                goto EXIT;
            default:
                printf("unexpected result from write to out->c in Succ_proc_body: %d\n", result);
                ap->state = _PS_EXCEPTION_;
                ap->exception = "unexpected result from write to out->c in Succ_proc_body";
                goto EXIT;
            }
            break;
        case 2 : // exception
            ap->state = _PS_EXCEPTION_;
            goto EXIT;
        default:
            printf("unexpected result from write to out->c in Succ_proc_body: %d\n", result);
            ap->state = _PS_EXCEPTION_;
            ap->exception = "unexpected result from write to out->c in Succ_proc_body";
            goto EXIT;
        }
        // source cleanup

    }

    
    ap->state = _PS_CLEAN_EXIT_;
EXIT:
    if((ap->state == _PS_EXCEPTION_) || (ap->state == _PS_CLEAN_EXIT_) )
    {
        // cleanup
    }
}

// End of Succ PROC type
