// C file generated from Danger source code by the dan compiler
// TODO frontmatter

#include <stdio.h> // TODO write string, logging, and io libs for Danger
#include <time.h>  // TODO write a time lib for Danger
#include <__struntime.h>

// ---------------------------------------
// I m p o r t s
// ---------------------------------------
// import: library= Time symbol= GetTimeNano


// ---------------------------------------
// D e c l a r a t i o n s
// ---------------------------------------

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


    chans->read = &(chans->readStore);
    chans->write = &(chans->writeStore);
    return chans;
}


// End of BInt bundle type

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

// ----------------------------------------------
// Prefix PROC type
typedef struct Prefix_locals_tag
{
        int32 initValue;
        int32 value;
        BundleR_BInt *in;
        BundleW_BInt *out;

} Prefix_locals;

typedef struct Prefix_proc_tag
{
    proc                        p;
    Prefix_locals           locals;
} Prefix_proc;

// Prefix_locals constructor
Prefix_locals * Prefix_locals_ctor(Prefix_locals * locals, BundleR_BInt *in, BundleW_BInt *out, int32 initValue)
{
  locals->in = in; 
  locals->out = out; 
  locals->initValue = initValue; 

  
  return locals;
}

void Prefix_body(proc * ap, scheduler * s)
{
    int result = 0; // for the result of read and write ops
    Type type; //TODO use the Type

        
    Prefix_proc* p = (Prefix_proc*) ap;
        Prefix_locals* locals = &(p->locals);
    if(ap->state != _PS_READY_TO_RUN_)
        restoreState(ap->state);

    locals->value = locals->initValue;
    // targetCleanup
    // sourceCleanup
    // send statement
    // TODO use the type parameter
    result = locals->out->c->write( locals->out->c->channel, 0, locals->value, (void **) &ap->exception, ap, s);
    switch(result)
    {
    case 0 : // wrote value
        printf("Prefix_proc: wrote locals->value to out->c\n", locals->value);
        break;
    case 1 : // blocked
        saveState(ap->state, Prefix_proc_S0);
        if(result == 1)
            goto EXIT;
        result = locals->out->c->writeHandshake( locals->out->c->channel, (void **) &ap->exception, ap, s);
        switch(result)
        {
        case 0 : // wrote value
            printf("Prefix_proc: wrote locals->value to out->c\n", locals->value);
            break;
        case 1 : // blocked
            goto EXIT;
        case 2 : // exception
            ap->state = _PS_EXCEPTION_;
            goto EXIT;
        default:
            printf("unexpected result from write to out->c in Prefix_proc_body: %d\n", result);
            ap->state = _PS_EXCEPTION_;
            ap->exception = "unexpected result from write to out->c in Prefix_proc_body";
            goto EXIT;
        }
        break;
    case 2 : // exception
        ap->state = _PS_EXCEPTION_;
        goto EXIT;
    default:
        printf("unexpected result from write to out->c in Prefix_proc_body: %d\n", result);
        ap->state = _PS_EXCEPTION_;
        ap->exception = "unexpected result from write to out->c in Prefix_proc_body";
        goto EXIT;
    }
    // source cleanup
    while ( 1 )
    {
        // receive statement
        // targetCleanup
        saveState(ap->state, Prefix_proc_S1);
        // TODO use the type parameter
        result = locals->in->c->read( locals->in->c->channel, &type, &(locals->value), &ap->exception, ap, s);
        switch(result)
        {
        case 0 : // read value
            printf("Prefix_proc: read value %d from in->c\n", locals->value);
            break;
        case 1 : // blocked
            // let it read again
            goto EXIT;
        case 2 : // exception
            ap->state = _PS_EXCEPTION_;
            goto EXIT;
        default:
            printf("unexpected result from read on in->c in Prefix_proc_body: %d\n", result);
            ap->state = _PS_EXCEPTION_;
            ap->exception = "unexpected result from read on in->c in Prefix_proc_body";
            goto EXIT;
        }
        // send statement
        // TODO use the type parameter
        result = locals->out->c->write( locals->out->c->channel, 0, locals->value, (void **) &ap->exception, ap, s);
        switch(result)
        {
        case 0 : // wrote value
            printf("Prefix_proc: wrote locals->value to out->c\n", locals->value);
            break;
        case 1 : // blocked
            saveState(ap->state, Prefix_proc_S2);
            if(result == 1)
                goto EXIT;
            result = locals->out->c->writeHandshake( locals->out->c->channel, (void **) &ap->exception, ap, s);
            switch(result)
            {
            case 0 : // wrote value
                printf("Prefix_proc: wrote locals->value to out->c\n", locals->value);
                break;
            case 1 : // blocked
                goto EXIT;
            case 2 : // exception
                ap->state = _PS_EXCEPTION_;
                goto EXIT;
            default:
                printf("unexpected result from write to out->c in Prefix_proc_body: %d\n", result);
                ap->state = _PS_EXCEPTION_;
                ap->exception = "unexpected result from write to out->c in Prefix_proc_body";
                goto EXIT;
            }
            break;
        case 2 : // exception
            ap->state = _PS_EXCEPTION_;
            goto EXIT;
        default:
            printf("unexpected result from write to out->c in Prefix_proc_body: %d\n", result);
            ap->state = _PS_EXCEPTION_;
            ap->exception = "unexpected result from write to out->c in Prefix_proc_body";
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

// End of Prefix PROC type

// ----------------------------------------------
// Count PROC type
typedef struct Count_locals_tag
{
        int32 value;
        BundleR_BInt *in;
        int32 step;
        int32 i;

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

    locals->step = 100000;
    // targetCleanup
    // sourceCleanup
    while ( 1 )
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

    }

        
    ap->state = _PS_CLEAN_EXIT_;
EXIT:
    if((ap->state == _PS_EXCEPTION_) || (ap->state == _PS_CLEAN_EXIT_) )
    {
        // cleanup
    }
}

// End of Count PROC type


// -------------------------------------
// Adorned declaration
// -------------------------------------
// Attribute section:
// [main]

// ----------------------------------------------
// Commstime PROC type
typedef struct Commstime_locals_tag
{
        Bundle_BInt d;
        Delta_proc __pDelta;
        Bundle_BInt b;
        Bundle_BInt c;
        Bundle_BInt a;
        Succ_proc __pSucc;
        Prefix_proc __pPrefix;
        Count_proc __pCount;

} Commstime_locals;

typedef struct Commstime_proc_tag
{
    proc                        p;
    Commstime_locals           locals;
} Commstime_proc;

// Commstime_locals constructor
Commstime_locals * Commstime_locals_ctor(Commstime_locals * locals)
{
  
  return locals;
}

void Commstime_body(proc * ap, scheduler * s)
{
    int finished = 0; // for the number of procs in a par that have finished
    int exceptions = 0; // for the number of procs in a par that threw exceptions

        
    Commstime_proc* p = (Commstime_proc*) ap;
        Commstime_locals* locals = &(p->locals);
    if(ap->state != _PS_READY_TO_RUN_)
        restoreState(ap->state);

    // constructorStaticNoArgs
    Bundle_BInt_ctor( &(locals->a) );
    // constructorStaticNoArgs
    Bundle_BInt_ctor( &(locals->b) );
    // constructorStaticNoArgs
    Bundle_BInt_ctor( &(locals->c) );
    // constructorStaticNoArgs
    Bundle_BInt_ctor( &(locals->d) );

    // par statement

    Prefix_locals_ctor( &(locals->__pPrefix.locals), locals->d.read, locals->a.write, 0);
    proc_ctor( (proc*) &(locals->__pPrefix), "Prefix", ap, Prefix_body);
    s->schedule(s, (proc*) &(locals->__pPrefix));

    Delta_locals_ctor( &(locals->__pDelta.locals), locals->a.read, locals->b.write, locals->c.write);
    proc_ctor( (proc*) &(locals->__pDelta), "Delta", ap, Delta_body);
    s->schedule(s, (proc*) &(locals->__pDelta));

    Succ_locals_ctor( &(locals->__pSucc.locals), locals->c.read, locals->d.write);
    proc_ctor( (proc*) &(locals->__pSucc), "Succ", ap, Succ_body);
    s->schedule(s, (proc*) &(locals->__pSucc));

    Count_locals_ctor( &(locals->__pCount.locals), locals->b.read);
    proc_ctor( (proc*) &(locals->__pCount), "Count", ap, Count_body);
    s->schedule(s, (proc*) &(locals->__pCount));


    saveState(ap->state, Commstime_proc_S0);


    lock(locals->__pPrefix.p.spinlock);
    if(locals->__pPrefix.p.sched_state == _FINISHED_)
    {
        finished += 1;
        if(locals->__pPrefix.p.state == _PS_EXCEPTION_)
        {
            exceptions += 1;
            ap->state = _PS_EXCEPTION_;
            ap->exception = locals->__pPrefix.p.exception;
            printf("Commstime_proc: caught exception from __pPrefix\n");

        }
        else if(locals->__pPrefix.p.state !=  _PS_CLEAN_EXIT_)
        {
            exceptions += 1;
            ap->state = _PS_EXCEPTION_;
            ap->exception = "Commstime_proc: pPrefix didn't exit cleanly, but didn't throw exception\n";
        }
    }
    unlock(locals->pCount1.p.spinlock);

    lock(locals->__pDelta.p.spinlock);
    if(locals->__pDelta.p.sched_state == _FINISHED_)
    {
        finished += 1;
        if(locals->__pDelta.p.state == _PS_EXCEPTION_)
        {
            exceptions += 1;
            ap->state = _PS_EXCEPTION_;
            ap->exception = locals->__pDelta.p.exception;
            printf("Commstime_proc: caught exception from __pDelta\n");

        }
        else if(locals->__pDelta.p.state !=  _PS_CLEAN_EXIT_)
        {
            exceptions += 1;
            ap->state = _PS_EXCEPTION_;
            ap->exception = "Commstime_proc: pDelta didn't exit cleanly, but didn't throw exception\n";
        }
    }
    unlock(locals->pCount1.p.spinlock);

    lock(locals->__pSucc.p.spinlock);
    if(locals->__pSucc.p.sched_state == _FINISHED_)
    {
        finished += 1;
        if(locals->__pSucc.p.state == _PS_EXCEPTION_)
        {
            exceptions += 1;
            ap->state = _PS_EXCEPTION_;
            ap->exception = locals->__pSucc.p.exception;
            printf("Commstime_proc: caught exception from __pSucc\n");

        }
        else if(locals->__pSucc.p.state !=  _PS_CLEAN_EXIT_)
        {
            exceptions += 1;
            ap->state = _PS_EXCEPTION_;
            ap->exception = "Commstime_proc: pSucc didn't exit cleanly, but didn't throw exception\n";
        }
    }
    unlock(locals->pCount1.p.spinlock);

    lock(locals->__pCount.p.spinlock);
    if(locals->__pCount.p.sched_state == _FINISHED_)
    {
        finished += 1;
        if(locals->__pCount.p.state == _PS_EXCEPTION_)
        {
            exceptions += 1;
            ap->state = _PS_EXCEPTION_;
            ap->exception = locals->__pCount.p.exception;
            printf("Commstime_proc: caught exception from __pCount\n");

        }
        else if(locals->__pCount.p.state !=  _PS_CLEAN_EXIT_)
        {
            exceptions += 1;
            ap->state = _PS_EXCEPTION_;
            ap->exception = "Commstime_proc: pCount didn't exit cleanly, but didn't throw exception\n";
        }
    }
    unlock(locals->pCount1.p.spinlock);


    if(exceptions > 0)
    {
        printf("Commstime_proc: exceptions from %d processes, retiring the other processes in the par\n", exceptions);
        // TODO what is the best way to clean up all the processes?
    }

    if(finished == 4)
    {
        if(locals->__pPrefix.p.state != _PS_CLEAN_EXIT_
            || locals->__pDelta.p.state != _PS_CLEAN_EXIT_
            || locals->__pSucc.p.state != _PS_CLEAN_EXIT_
            || locals->__pCount.p.state != _PS_CLEAN_EXIT_)
        {
            ap->state = _PS_EXCEPTION_;
            printf("Commstime_proc: all procs exited PAR, but not all cleanly\n");
            goto EXIT;
        }
        else
        {
            printf("Commstime_proc: PAR exiting cleanly\n");
        }
    }
    else
    {
        goto EXIT;
    }
    // end of par statement
        
    ap->state = _PS_CLEAN_EXIT_;
EXIT:
    if((ap->state == _PS_EXCEPTION_) || (ap->state == _PS_CLEAN_EXIT_) )
    {
        // cleanup
    }
}

// End of Commstime PROC type