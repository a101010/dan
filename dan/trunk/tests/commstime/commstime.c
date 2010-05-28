// C file generated from Danger source code by the dan compiler
// TODO frontmatter

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
Prefix_locals * Prefix_locals_ctor(Prefix_locals * locals, BundleR_BInt *in, BundleW_BInt *out, int32 initValue);

void Prefix_body(proc * ap, scheduler * s);

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
Delta_locals * Delta_locals_ctor(Delta_locals * locals, BundleR_BInt *in, BundleW_BInt *out1, BundleW_BInt *out2);

void Delta_body(proc * ap, scheduler * s);

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
Count_locals * Count_locals_ctor(Count_locals * locals, BundleR_BInt *in);

void Count_body(proc * ap, scheduler * s);

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
Succ_locals * Succ_locals_ctor(Succ_locals * locals, BundleR_BInt *in, BundleW_BInt *out);

void Succ_body(proc * ap, scheduler * s);

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



// TODO int main(int argc, char ** argv)
int main()
{
    Scheduler_default ctxt;
    // TODO allow main proc to get argv and default channels
    Commstime_proc mainProc;
    int ret_val = 0;

    default_scheduler_ctor(&ctxt);

    
    Commstime_locals_ctor(&(mainProc.locals));

    
    proc_ctor((proc*) (&mainProc), "Commstime", 0, Commstime_body);

    ret_val = Schedule_default( &(ctxt.s), (proc*) (&mainProc)); // TODO just exit?  assert: ctxt.num_ready == 0 if ret_val != 0
    

    ret_val = default_scheduler_run(&ctxt);

    if(ctxt.num_unready > 0)
    {
        printf("Number of deadlocked processes = %d\n", ctxt.num_unready);
        ret_val = 1;
    }

    printf("Press <enter> to exit.\n");
    getchar();
    return ret_val;
}
