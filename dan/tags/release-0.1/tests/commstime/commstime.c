// Copyright 2009, Alan Grover

// This .c file represents the expected output (more or less) of the dan compiler for commstime.dan

// A single processor version. 

#include <stdio.h>
#include <time.h>
#include "__struntime.h"

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

    BundleR_BInt                *read;
    BundleW_BInt                *write;
    BundleR_BInt                readStore;
    BundleW_BInt                writeStore;
    __c0bs32				    c;
} Bundle_BInt;



// BInt_chans constructor
Bundle_BInt * Bundle_BInt_ctor(Bundle_BInt * chans)
{
	// TODO generate meaningful channel end ID's
    __c0bs32_ctor( &(chans->c), "chanRId", "chanWId" );
    chans->readStore.c = chans->c.ends.read;
    chans->c.ends.read = 0; // because it is a mobile type
    chans->writeStore.c = chans->c.ends.write;
    chans->c.ends.write = 0;
    chans->read = &(chans->readStore);
    chans->write = &(chans->writeStore);
    return chans;
}

typedef struct Delta_locals_tag
{
	int32       value;
	BundleR_BInt * in;
    BundleW_BInt * out1;
    BundleW_BInt * out2;
} Delta_locals;

typedef struct Delta_proc_tag
{
    proc            p;
    Delta_locals    locals;
} Delta_proc;

// Delta_locals constructor
Delta_locals * Delta_locals_ctor(Delta_locals * locals, BundleR_BInt * in, BundleW_BInt * out1, BundleW_BInt * out2)
{
	locals->in = in;
	locals->out1 = out1;
	locals->out2 = out2;
	return locals;
}

void Delta_body(proc * p, scheduler * s)
{
	int result = 0;
    Delta_proc *dp = (Delta_proc *) p;
	Delta_locals *locals = &(dp->locals);
    if(p->state != _PS_READY_TO_RUN_)
    {
        restoreState(p->state);
    }
	while(1)
	{
        __ChanR32 *in_r;
        __ChanW32 *out1_w;
        __ChanW32 *out2_w;
		Type type;

        saveState(p->state, Delta_S1);
        in_r = locals->in->c;

		// TODO use the type parameter
		
		result = in_r->read( in_r->channel, &type, &(locals->value), &p->exception, p, s);
		switch(result)
		{
		case 0 : // read value
			printf("Delta: read value %d\n", locals->value);
			break;
		case 1 : // blocked
			goto EXIT;
		case 2 : // exception
			p->state = _PS_EXCEPTION_;
			goto EXIT;
		default:
			printf("unexpected result from ChanRead_BInt_c_0 in Delta_body: %d\n", result);
			p->state = _PS_EXCEPTION_;
			p->exception = "unexpected result from ChanRead_BInt_c_0 in Delta_body"; // TODO find a way to allocate constructed string that doesn't use malloc
			goto EXIT;
		}
        
        out1_w = locals->out1->c;
		// TODO use type parameter
		result = out1_w->write( out1_w->channel, 0, locals->value, (void**) &p->exception, p, s);
		switch(result)
		{
		case 0 : // wrote value
			printf("Delta: wrote value %d to out1\n", locals->value);
			break;
		case 1 : // blocked
            saveState(p->state, Delta_S2);
            out1_w = locals->out1->c;
            if(result == 1)
                goto EXIT;
            result = out1_w->writeHandshake( out1_w->channel, (void **) &p->exception, p, s);
			switch(result)
			{
		    case 0 : // wrote value
			    printf("Delta: wrote value %d to out1\n", locals->value);
			    break;
		    case 1 : // blocked
			    goto EXIT;
		    case 2 : // exception
			    p->state = _PS_EXCEPTION_;
			    goto EXIT;
		    default:
			    printf("unexpected result from ChanWrite_BInt_c_0 in Delta_body: %d\n", result);
			    p->state = _PS_EXCEPTION_;
			    p->exception = "unexpected result from ChanWrite_BInt_c_0 in Delta_body"; // TODO find a way to allocate constructed string that doesn't use malloc
			    goto EXIT;
			}
			break;
		case 2 : // exception
			p->state = _PS_EXCEPTION_;
			goto EXIT;
		default:
			printf("unexpected result from ChanWrite_BInt_c_0 in Delta_body: %d\n", result);
			p->state = _PS_EXCEPTION_;
			p->exception = "unexpected result from ChanWrite_BInt_c_0 in Delta_body"; // TODO find a way to allocate constructed string that doesn't use malloc
			goto EXIT;
		}
		
        out2_w = locals->out2->c;
		// TODO use type parameter
		result = out2_w->write( out2_w->channel, 0, locals->value, (void **) &p->exception, p, s);
		switch(result)
		{
		case 0 : // wrote value
			printf("Delta: wrote value %d to out2\n", locals->value);
			p->state = _PS_READY_TO_RUN_;
			break;
		case 1 : // blocked
			saveState(p->state, Delta_S3);
            out2_w = locals->out2->c;
            if(result == 1)
                goto EXIT;
            result = out2_w->writeHandshake( out2_w->channel, (void **) &p->exception, p, s);
			switch(result)
			{
			case 0 : // wrote value
				printf("Delta: wrote value %d to out2\n", locals->value);
				break;
			case 1 : // blocked
				goto EXIT;
			case 2 : // exception
				p->state = _PS_EXCEPTION_;
				goto EXIT;
			default:
				printf("unexpected result from ChanWrite_BInt_c_0 in Delta_body: %d\n", result);
				p->state = _PS_EXCEPTION_;
				p->exception = "unexpected result from ChanWrite_BInt_c_0 in Delta_body"; // TODO find a way to allocate constructed string that doesn't use malloc
				goto EXIT;
			}
			break;
		case 2 : // exception
			p->state = _PS_EXCEPTION_;
			goto EXIT;
		default:
			printf("unexpected result from ChanWrite_BInt_c_0 in Delta_body: %d\n", result);
			p->state = _PS_EXCEPTION_;
			p->exception = "unexpected result from ChanWrite_BInt_c_0 in Delta_body"; // TODO find a way to allocate constructed string that doesn't use malloc
			goto EXIT;
		}
	} // while(1)

    // since we have an infinite loop above, we'll never get here
    // but this is where we'd set _PS_CLEAN_EXIT_ if we didn't
    // have an infinite loop
    p->state = _PS_CLEAN_EXIT_;
EXIT:
    // if it is a real exit, clean up our channels
    // otherwise we're just blocked and returning to the scheduler
	if((p->state == _PS_EXCEPTION_) || (p->state == _PS_CLEAN_EXIT_) )
	{
        
        printf("Delta: cleaning up self. Final state is %s\n", 
            proc_state_to_string(p->state));
		locals->in->c->type->poison(locals->in->c->channel, s);
		locals->out1->c->type->poison(locals->out1->c->channel, s);
		locals->out2->c->type->poison(locals->out2->c->channel, s);
	}
}

typedef struct Succ_locals_tag
{
	int32 value;
	BundleR_BInt *in;
    BundleW_BInt *out;
} Succ_locals;

typedef struct Succ_proc_tag
{
    proc        p;
    Succ_locals locals;
} Succ_proc;

// Succ_locals constructor
Succ_locals * Succ_locals_ctor(Succ_locals * locals, BundleR_BInt* in, BundleW_BInt* out)
{
	locals->in = in;
	locals->out = out;
	return locals;
}

void Succ_body(proc * p, scheduler *s)
{
	int result = 0;
    Succ_proc* ps = (Succ_proc*) p;
	Succ_locals* locals = &(ps->locals);
    if(p->state != _PS_READY_TO_RUN_)
        restoreState(p->state);
	while(1)
	{
        __ChanR32 * in_r;
        __ChanW32 *out_w;
		Type type;

        saveState(p->state, Succ_S1);
		
		in_r = locals->in->c;
		// TODO use type parameter
		result = in_r->read( in_r->channel, &type, &(locals->value), &p->exception, p, s);
		switch(result)
		{
		case 0 : // read value
			printf("Succ: read value %d\n", locals->value);
			break;
		case 1 : // blocked
			// let it read again
			goto EXIT;
		case 2 : // exception
			p->state = _PS_EXCEPTION_;
			goto EXIT;
		default:
			printf("unexpected result from ChanRead_BInt_c_0 in Succ_body: %d\n", result);
			p->state = _PS_EXCEPTION_;
			p->exception = "unexpected result from ChanRead_BInt_c_0 in Succ_body"; // TODO find a way to allocate constructed string that doesn't use malloc
            goto EXIT;
		}
		++(locals->value);
        
		
		out_w = locals->out->c;
		// TODO use type parameter
		result = out_w->write( out_w->channel, 0, locals->value, (void**) &p->exception, p, s);
		switch(result)
		{
		case 0 : // wrote value
			printf("Succ: wrote value %d\n", locals->value);
			break;
		case 1 : // blocked
			saveState(p->state, Succ_S2);
            out_w = locals->out->c;
            if(result == 1)
                goto EXIT;
			result = out_w->writeHandshake( out_w->channel, (void **) &p->exception, p, s);
			switch(result)
			{
			case 0 : // wrote value
				printf("Succ: wrote value %d\n", locals->value);
				break;
			case 1 : // blocked
				// let it try writing again
                goto EXIT;
			case 2 : // exception
				p->state = _PS_EXCEPTION_;
				goto EXIT;
			default:
				printf("unexpected result from ChanWrite_BInt_c_0 in Succ_body: %d\n", result);
				p->state = _PS_EXCEPTION_;
				p->exception = "unexpected result from ChanWrite_BInt_c_0 in Succ_body"; // TODO find a way to allocate constructed string that doesn't use malloc
                goto EXIT;
			}
			break;
		case 2 : // exception
			p->state = _PS_EXCEPTION_;
			goto EXIT;
		default:
			printf("unexpected result from ChanWrite_BInt_c_0 in Succ_body: %d\n", result);
			p->state = _PS_EXCEPTION_;
			p->exception = "unexpected result from ChanWrite_BInt_c_0 in Succ_body"; // TODO find a way to allocate constructed string that doesn't use malloc
            goto EXIT;
		}
	} // while (1)

    // since we have an infinite loop above, we'll never get here
    // but this is where we'd set _PS_CLEAN_EXIT_ if we didn't
    // have an infinite loop
    p->state = _PS_CLEAN_EXIT_;
EXIT:
    // if it is a real exit, clean up our channels
    // otherwise we're just blocked and returning to the scheduler
	if((p->state == _PS_EXCEPTION_) || (p->state == _PS_CLEAN_EXIT_) )
	{
        printf("Succ: cleaning up self. Final state is %s\n", 
            proc_state_to_string(p->state));
		locals->in->c->type->poison(locals->in->c->channel, s);
		locals->out->c->type->poison(locals->out->c->channel, s);
	}
}


typedef struct Prefix_locals_tag
{
	int32 value;
	BundleR_BInt *in;
    BundleW_BInt *out;
} Prefix_locals;

typedef struct Prefix_proc_tag
{
    proc          p;
    Prefix_locals locals;
} Prefix_proc;

// Prefix_locals constructor
Prefix_locals * Prefix_locals_ctor(Prefix_locals * locals, BundleR_BInt * in, BundleW_BInt * out, int32 initValue)
{
	locals->in = in;
	locals->out = out;
	locals->value = initValue;
	return locals;
}


void Prefix_body(proc * p, scheduler * s)
{
	int result = 0;
    Prefix_proc *pp = (Prefix_proc*) p;
	Prefix_locals * locals = &(pp->locals);
    __ChanW32 *out_w = locals->out->c;
    if(p->state != _PS_READY_TO_RUN_)
        restoreState(p->state);

    printf("Prefix init value: %d\n", locals->value); 
	// TODO use type parameter
	result = out_w->write( out_w->channel, 0, locals->value, (void**) &p->exception, p, s);
	switch(result)
	{
	case 0: // wrote value and synced
		printf("Prefix wrote value %d\n", locals->value);
		break;
	case 1: // wrote value and blocked
        saveState(p->state, Prefix_S1);
        if(result == 1)
            goto EXIT;
		result = out_w->writeHandshake( out_w->channel, (void **) &p->exception, p, s);
		switch(result)
		{
		case 0: // wrote value and synced
			printf("Prefix wrote value %d\n", locals->value);
			break;
		case 1: // wrote value and blocked
			goto EXIT;
		case 2: // exception was thrown
			p->state = _PS_EXCEPTION_;
			goto EXIT;
		default:
			printf("unexpected result from ChanWrite_BInt_c_0 in Prefix_body: %d\n", result);
			p->state = _PS_EXCEPTION_;
			p->exception = "unexpected result from ChanWrite_BInt_c_0 in Prefix_body"; // TODO find a way to allocate constructed string that doesn't use malloc
            goto EXIT;
		}
		break;
	case 2: // exception was thrown
		p->state = _PS_EXCEPTION_;
		goto EXIT;
	default:
		printf("unexpected result from ChanWrite_BInt_c_0 in Prefix_body: %d\n", result);
		p->state = _PS_EXCEPTION_;
		p->exception = "unexpected result from ChanWrite_BInt_c_0 in Prefix_body"; // TODO find a way to allocate constructed string that doesn't use malloc
        goto EXIT;
	}

	while(1)
	{
        __ChanR32 * in_r;
		Type type;
        saveState(p->state, Prefix_S2);
		in_r = locals->in->c;
		// TODO use type parameter
		result = in_r->read( in_r->channel, &type, &(locals->value), &p->exception, p, s);
		switch(result)
		{
		case 0 : // read value
			printf("Prefix: read value %d\n", locals->value);
			break;
		case 1 : // blocked
			// let it read again
			goto EXIT;
		case 2 : // exception
			p->state = _PS_EXCEPTION_;
			goto EXIT;
		default:
			printf("unexpected result from ChanRead_BInt_c_0 in Prefix_body: %d\n", result);
			p->state = _PS_EXCEPTION_; 
			p->exception = "unexpected result from ChanRead_BInt_c_0 in Prefix_body"; // TODO find a way to allocate constructed string that doesn't use malloc
            goto EXIT;
		}
		
		//__ChanW32 *out_w = locals->out->c;
		// TODO use type parameter
		result = out_w->write( out_w->channel, 0, locals->value, (void**) &p->exception, p, s);
		switch(result)
		{
		case 0 : // wrote value
			printf("Prefix: wrote value %d\n", locals->value);
			break;
		case 1: // wrote value and blocked
            saveState(p->state, Prefix_S3);
            if(result == 1)
                goto EXIT;
		    result = out_w->writeHandshake( out_w->channel, (void **) &p->exception, p, s);
		    switch(result)
		    {
		    case 0: // wrote value and synced
			    printf("Prefix wrote value %d\n", locals->value);
			    break;
		    case 1: // wrote value and blocked
			    goto EXIT;
		    case 2: // exception was thrown
			    p->state = _PS_EXCEPTION_;
			    goto EXIT;
		    default:
			    printf("unexpected result from ChanWrite_BInt_c_0 in Prefix_body: %d\n", result);
			    p->state = _PS_EXCEPTION_;
			    p->exception = "unexpected result from ChanWrite_BInt_c_0 in Prefix_body"; // TODO find a way to allocate constructed string that doesn't use malloc
                goto EXIT;
		    }
		    break;
		case 2 : // exception
			p->state = _PS_EXCEPTION_;
			goto EXIT;
		default:
			printf("unexpected result from ChanWrite_BInt_c_0 in Prefix_body: %d\n", result);
			p->state = _PS_EXCEPTION_;
			p->exception = "unexpected result from ChanWrite_BInt_c_0 in Prefix_body"; // TODO find a way to allocate constructed string that doesn't use malloc
            goto EXIT;
		}
	} // while (1)

    // since we have an infinite loop above, we'll never get here
    // but this is where we'd set _PS_CLEAN_EXIT_ if we didn't
    // have an infinite loop
    p->state = _PS_CLEAN_EXIT_;
EXIT:
    // if it is a real exit, clean up our channels
    // otherwise we're just blocked and returning to the scheduler
	if((p->state == _PS_EXCEPTION_) || (p->state == _PS_CLEAN_EXIT_) )
	{
        printf("Prefix: cleaning up self. Final state is %s\n", 
            proc_state_to_string(p->state));
		locals->in->c->type->poison(locals->in->c->channel, s);
		locals->out->c->type->poison(locals->out->c->channel, s);
	}
}


typedef struct Count_locals_tag
{
	int32 value;
	int32 i;
	int32 step;
	int32 time;
	time_t time1;
	time_t time2;
	BundleR_BInt *in;
} Count_locals;

typedef struct Count_proc_tag
{
    proc            p;
    Count_locals    locals;
} Count_proc;

// Count_locals constructor
Count_locals * Count_locals_ctor(Count_locals * locals, BundleR_BInt * in)
{
	locals->in = in;
	locals->step = 1000;
	return locals;
}


void Count_body(proc * p, scheduler * s)
{
	int result = 0;
    Count_proc *cp = (Count_proc*) p;
	Count_locals * locals = &(cp->locals);
    if(p->state != _PS_READY_TO_RUN_)
        restoreState(p->state);

	printf("Count step: %d\n", locals->step);
	while(1)
	{
		locals->i = 0;
        // TODO use high-performance counters
		time( &(locals->time1));
        while(locals->i < locals->step)
        {
            __ChanR32 * in_r;
			Type type;
			saveState(p->state, Count_S1);
			in_r = locals->in->c;
			// TODO use type parameter
		    result = in_r->read( in_r->channel, &type, &(locals->value), &p->exception, p, s);
			switch(result)
			{
			case 0 : // read value
				printf("Count: read value %d\n", locals->value);
				break;
			case 1 : // blocked
				goto EXIT;
			case 2 : // exception
				p->state = _PS_EXCEPTION_;
				goto EXIT;
			default:
				printf("unexpected result from ChanRead_BInt_c_0 in Count_body: %d\n", result);
				p->state = _PS_EXCEPTION_;
				p->exception = "unexpected result from ChanRead_BInt_c_0 in Count_body"; // TODO find a way to allocate constructed string that doesn't use malloc
				goto EXIT;
			}
            locals->i = locals->i + 1;
        } // while(locals->i < locals->step)

		time( &(locals->time2));
		locals->time = (int32) (locals->time2 - locals->time1);	
		printf("got %d iterations in %d seconds\n", locals->step, locals->time);
		// TODO print traditional commstime stats

    	// for now, just exit cleanly
		p->state = _PS_CLEAN_EXIT_;
		goto EXIT;        
	} // while (1)

    // since we have an infinite loop above, we'll never get here
    // but this is where we'd set _PS_CLEAN_EXIT_ if we didn't
    // have an infinite loop
    p->state = _PS_CLEAN_EXIT_;
EXIT:
    // if it is a real exit, clean up our channels
    // otherwise we're just blocked and returning to the scheduler
	if((p->state == _PS_EXCEPTION_) || (p->state == _PS_CLEAN_EXIT_) )
	{
        printf("Count: cleaning up self. Final state is %s\n", 
            proc_state_to_string(p->state));
		locals->in->c->type->poison(locals->in->c->channel, s);
	}
}



typedef struct Commstime_locals_tag
{
	Bundle_BInt a;
	Bundle_BInt b;
	Bundle_BInt c;
	Bundle_BInt d;
	Delta_proc pDelta1;
	Succ_proc pSucc1;
	Prefix_proc pPrefix1;
	Count_proc pCount1;
} Commstime_locals;

typedef struct Commstime_proc_tag
{
    proc                p;
    Commstime_locals    locals;
} Commstime_proc;

Commstime_locals * Commstime_locals_ctor(Commstime_locals * locals)
{
	return locals;
}

void Commstime_body(proc * p, scheduler * s)
{
	int finished = 0;
	int exception = 0;
    Commstime_proc *cp = (Commstime_proc*) p;
	Commstime_locals * locals = &(cp->locals);
    if(p->state != _PS_READY_TO_RUN_)
        restoreState(p->state);

	Bundle_BInt_ctor( &(locals->a) );
	Bundle_BInt_ctor( &(locals->b) );
	Bundle_BInt_ctor( &(locals->c) );
	Bundle_BInt_ctor( &(locals->d) );

	Delta_locals_ctor( &(locals->pDelta1.locals), locals->a.read, locals->b.write, locals->c.write);
	proc_ctor( (proc*) &(locals->pDelta1), "Delta", p, Delta_body);

	Count_locals_ctor( &(locals->pCount1.locals), locals->b.read);
	proc_ctor( (proc*) &(locals->pCount1), "Count", p, Count_body);
	
	Prefix_locals_ctor( &(locals->pPrefix1.locals), locals->d.read, locals->a.write, 22);
	proc_ctor( (proc*) &(locals->pPrefix1), "Prefix", p, Prefix_body);

	Succ_locals_ctor( &(locals->pSucc1.locals), locals->c.read, locals->d.write);
	proc_ctor( (proc*) &(locals->pSucc1), "Succ", p, Succ_body);
	
	s->schedule(s, (struct proc_tag*) &(locals->pPrefix1));
	s->schedule(s, (proc*) &(locals->pDelta1));
	s->schedule(s, (proc*) &(locals->pSucc1));
	s->schedule(s, (proc*) &(locals->pCount1));
		
	saveState(p->state, Commstime_S1);
	lock(locals->pCount1.p.spinlock);
	if(locals->pCount1.p.sched_state == _FINISHED_)
	{
		finished += 1;
		if(locals->pCount1.p.state == _PS_EXCEPTION_)
		{
			exception += 1;
			p->state = _PS_EXCEPTION_;
			p->exception = locals->pCount1.p.exception;
			printf("Commstime caught exception from pCount1\n");

		}
		else if(locals->pCount1.p.state !=  _PS_CLEAN_EXIT_)
		{
			exception += 1;
			p->state = _PS_EXCEPTION_;
			p->exception = "Commstime: pCount1 didn't exit cleanly, but didn't throw exception\n";
		}
	}
	unlock(locals->pCount1.p.spinlock);

	lock(locals->pPrefix1.p.spinlock);
	if(locals->pPrefix1.p.sched_state == _FINISHED_)
	{
		finished += 1;
		if(locals->pPrefix1.p.state == _PS_EXCEPTION_)
		{
			exception += 1;
			p->state = _PS_EXCEPTION_;
			p->exception = locals->pPrefix1.p.exception;
			printf("Commstime caught exception from pPrefix1\n");

		}
		else if(locals->pPrefix1.p.state !=  _PS_CLEAN_EXIT_)
		{
			exception += 1;
			p->state = _PS_EXCEPTION_;
			p->exception = "Commstime: pPrefix1 didn't exit cleanly, but didn't throw exception\n";
		}
	}
	unlock(locals->pPrefix1.p.spinlock);

	lock(locals->pSucc1.p.spinlock);
	if(locals->pSucc1.p.sched_state == _FINISHED_)
	{
		finished += 1;
		if(locals->pSucc1.p.state == _PS_EXCEPTION_)
		{
			exception += 1;
			p->state = _PS_EXCEPTION_;
			p->exception = locals->pSucc1.p.exception;
			printf("Commstime caught exception from pSucc1\n");

		}
		else if(locals->pSucc1.p.state !=  _PS_CLEAN_EXIT_)
		{
			exception += 1;
			p->state = _PS_EXCEPTION_;
			p->exception = "Commstime: pSucc1 didn't exit cleanly, but didn't throw exception\n";
		}
	}
	unlock(locals->pSucc1.p.spinlock);

	lock(locals->pDelta1.p.spinlock);
	if(locals->pDelta1.p.sched_state == _FINISHED_)
	{
		finished += 1;
		if(locals->pDelta1.p.state == _PS_EXCEPTION_)
		{
			exception += 1;
			p->state = _PS_EXCEPTION_;
			p->exception = locals->pDelta1.p.exception;
			printf("Commstime caught exception from pDelta1\n");
		}
		else if(locals->pDelta1.p.state !=  _PS_CLEAN_EXIT_)
		{
			exception += 1;
			p->state = _PS_EXCEPTION_;
			p->exception = "Commstime: pDelta1 didn't exit cleanly, but didn't throw exception\n";
		}
	}
	unlock(locals->pDelta1.p.spinlock);

	if(exception > 0)
	{
		printf("Commstime: exceptions from %d processes, retiring the other processes in the par\n", exception);
		// TODO what is the best way to clean up all the processes?
	}

	if(finished == 4)
	{
		if(locals->pCount1.p.state != _PS_CLEAN_EXIT_
			|| locals->pPrefix1.p.state != _PS_CLEAN_EXIT_
			|| locals->pSucc1.p.state != _PS_CLEAN_EXIT_
			|| locals->pDelta1.p.state != _PS_CLEAN_EXIT_)
		{
            p->state = _PS_EXCEPTION_;
            printf("Commstime: four exited PAR, but not all cleanly\n");
            goto EXIT;
		}
        else
        {
            printf("Commstime: PAR exiting cleanly\n");
        }
	}
    else
    {
        goto EXIT;
    }

    p->state = _PS_CLEAN_EXIT_;
EXIT:
    // No cleanup needed here
    return;

}


//int main(int argc, char ** argv)
int main()
{
	Scheduler_default ctxt;
	Commstime_proc cp;
	int ret_val = 0;

	default_scheduler_ctor(&ctxt);

	
	Commstime_locals_ctor(&(cp.locals));

	
	proc_ctor((proc*) (&cp), "Commstime", 0, Commstime_body);

	ret_val = Schedule_default( &(ctxt.s), (proc*) (&cp)); // TODO just exit?  assert: ctxt.num_ready == 0 if ret_val != 0
	

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
