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
// Delta PROC type
typedef struct Delta_locals_tag
{
    int32 value;
    BundleW_BInt *out2;
    BundleR_BInt *in;
    BundleW_BInt *out1;

} Delta_locals;

typedef struct Delta_proc_tag
{
    proc                        p;
    Delta_locals           locals;
} Delta_proc;

// Delta_locals constructor
Delta_locals * Delta_locals_ctor(Delta_locals * locals, BundleR_BInt *in, BundleW_BInt *out1, BundleW_BInt *out2)
{
  locals->in = in; 
  locals->out1 = out1; 
  locals->out2 = out2; 

  
  return locals;
}

void Delta_body(proc * ap, scheduler * s)
{
    int result = 0; // for the result of read and write ops
    Type type; //TODO use the Type

    
    Delta_proc* p = (Delta_proc*) ap;
    Delta_locals* locals = &(p->locals);
    if(ap->state != _PS_READY_TO_RUN_)
        restoreState(ap->state);

    while ( 1 )
    {
        // receive statement
        // targetCleanup
        saveState(ap->state, Delta_proc_S0);
        // TODO use the type parameter
        result = locals->in->c->read( locals->in->c->channel, &type, &(locals->value), &ap->exception, ap, s);
        switch(result)
        {
        case 0 : // read value
            printf("Delta_proc: read value %d from in->c\n", locals->value);
            break;
        case 1 : // blocked
            // let it read again
            goto EXIT;
        case 2 : // exception
            ap->state = _PS_EXCEPTION_;
            goto EXIT;
        default:
            printf("unexpected result from read on in->c in Delta_proc_body: %d\n", result);
            ap->state = _PS_EXCEPTION_;
            ap->exception = "unexpected result from read on in->c in Delta_proc_body";
            goto EXIT;
        }
        // send statement
        // TODO use the type parameter
        result = locals->out1->c->write( locals->out1->c->channel, 0, locals->value, (void **) &ap->exception, ap, s);
        switch(result)
        {
        case 0 : // wrote value
            printf("Delta_proc: wrote locals->value to out1->c\n", locals->value);
            break;
        case 1 : // blocked
            saveState(ap->state, Delta_proc_S1);
            if(result == 1)
                goto EXIT;
            result = locals->out1->c->writeHandshake( locals->out1->c->channel, (void **) &ap->exception, ap, s);
            switch(result)
            {
            case 0 : // wrote value
                printf("Delta_proc: wrote locals->value to out1->c\n", locals->value);
                break;
            case 1 : // blocked
                goto EXIT;
            case 2 : // exception
                ap->state = _PS_EXCEPTION_;
                goto EXIT;
            default:
                printf("unexpected result from write to out1->c in Delta_proc_body: %d\n", result);
                ap->state = _PS_EXCEPTION_;
                ap->exception = "unexpected result from write to out1->c in Delta_proc_body";
                goto EXIT;
            }
            break;
        case 2 : // exception
            ap->state = _PS_EXCEPTION_;
            goto EXIT;
        default:
            printf("unexpected result from write to out1->c in Delta_proc_body: %d\n", result);
            ap->state = _PS_EXCEPTION_;
            ap->exception = "unexpected result from write to out1->c in Delta_proc_body";
            goto EXIT;
        }
        // source cleanup
        // send statement
        // TODO use the type parameter
        result = locals->out2->c->write( locals->out2->c->channel, 0, locals->value, (void **) &ap->exception, ap, s);
        switch(result)
        {
        case 0 : // wrote value
            printf("Delta_proc: wrote locals->value to out2->c\n", locals->value);
            break;
        case 1 : // blocked
            saveState(ap->state, Delta_proc_S2);
            if(result == 1)
                goto EXIT;
            result = locals->out2->c->writeHandshake( locals->out2->c->channel, (void **) &ap->exception, ap, s);
            switch(result)
            {
            case 0 : // wrote value
                printf("Delta_proc: wrote locals->value to out2->c\n", locals->value);
                break;
            case 1 : // blocked
                goto EXIT;
            case 2 : // exception
                ap->state = _PS_EXCEPTION_;
                goto EXIT;
            default:
                printf("unexpected result from write to out2->c in Delta_proc_body: %d\n", result);
                ap->state = _PS_EXCEPTION_;
                ap->exception = "unexpected result from write to out2->c in Delta_proc_body";
                goto EXIT;
            }
            break;
        case 2 : // exception
            ap->state = _PS_EXCEPTION_;
            goto EXIT;
        default:
            printf("unexpected result from write to out2->c in Delta_proc_body: %d\n", result);
            ap->state = _PS_EXCEPTION_;
            ap->exception = "unexpected result from write to out2->c in Delta_proc_body";
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

// End of Delta PROC type