// Copyright 2007, Alan Grover

// This .c file represents the expected output (more or less; comments and constants may differ) of the dan compiler for commstime.dan

// A single processor version.  TODO Need to add locks for SMP systems.

#define
#define READY_TO_RUN 2

typedef int int32;
typedef unsigned int uint;

typedef struct Exception_tag
{
	char * info;
	int info_length;
} Exception;

extern proc;

typedef void (*OpSchedule)(proc * p);

typedef struct scheduler_tag
{
	OpSchedule schedule;
} scheduler;

typedef void (*OpProcBody)(proc * p, scheduler * s);


#define _PS_EXCEPTION_ 0
#define _PS_CLEAN_EXIT_ 1
#define _PS_READY_TO_RUN_ 2


//	state values
//			exception - 0
//			exited normally - 1
//			ready to run - 2
//			blocked - 3 +
typedef struct proc_tag 
{
	proc * parent;
	void * locals;
	uint state; 
	Exception * exception;
	OpProcBody body;
} proc;

// constructor
void proc_con(proc * p, proc * parent, void * locals, OpProcBody body)
{
	p->parent = parent;
	p->locals = locals;
	p->exception = 0;
	p->state = _PS_READY_TO_RUN_;
	p->body = body;
}


typedef void (*OpPoison)(void * chans);
typedef int (*OpIsPoisoned)(void * chans); 

typedef struct bundle_end_tag
{
	OpPoison poison;
	OpIsPoisoned isPoisoned;
	void * ops;  // this layer of indirection could probably be removed, but at the cost of a lot of extra custom code  TODO consider as an optimization
	void * chans;
} bundle_end;

typedef struct BInt_chans_tag
{
	int is_poisoned; // 0 = false, 1 = true
	int c_value; // will be a union if multiple types on the channel
	int c_valid_value; // 0 if invalid, else the number of the type for the value of c that is valid (a channel can carry multiple types)
	proc * reader_waiting; // reader waiting to syncronize on the channel
	proc * writer_waiting; // writer waiting to syncronize on the channel
} BInt_chans;

// returns 0 if it got a valid value; 1 if it blocked; and 2 if an exception was thrown
typedef int (*OpChanRead_BInt_c_0)(BInt_chans * chans, int32 * value, exception ** ex, proc * p, scheduler * s);

int ChanRead_BInt_c_0(BInt_chans * chans, int32 * value, void ** exception, proc * p, scheduler * s)
{
	int ret_val = 2;
	// TODO get the lock
	if(chans->is_poisoned)
	{
		// TODO make exceptions something other than just plain strings (& null terminated at that!)
		// need dynamic memory allocation strategy to use Exception
		*exception = (void*) "channel poison";
	}
	else
	{
		if(chans->c_valid_value)
		{
			*value = chans->c_value;
			chans->c_valid_value = 0;
			if(chans->writer_waiting != null)
			{
				s->schedule(chans->writer_waiting);
				chans->writer_waiting = null;
			}
			ret_val = 0;
		}
		else
		{
			chans->waiting_reader = p;
			ret_val = 1;
		}
	}

	// TODO release the lock
	return ret_val;
}

// returns 0 if it wrote the value and synchronized; 1 if it blocked; and 2 if an exception was thrown
typedef int (*OpChanWrite_BInt_c_0)(BInt_chans * chans, int32 value, void ** exception, proc * p, scheduler * s);

int ChanWrite_BInt_c_0(BInt_chans * chans, int32 value, void ** exception, proc * p, scheduler * s)
{
	int ret_val = 2;
	// TODO get the lock
	if(chans->is_poisoned)
	{
		// TODO make exceptions something other than just plain strings (& null terminated at that!)
		*exception = (void*) "channel poison";
	}
	else
	{
		// TODO right now the writer allways unconditionally writes; allow to withdraw if in ALT
		chans->c_value = value;
		chans->c_value_valid = 1;
		if(chans->reader_waiting != null)
		{
			s->schedule(chans->reader_waiting);
			chans->reader_waiting = null;
			ret_val = 0;
		}
		else
		{
			chans->waiting_writer = p;
			ret_val = 1;
		}
		
	}

	// TODO release the lock
	return ret_val;
}

// the need for an implementation of Poison and IsPoisoned for each bundle type
// can probably be simplified by another layer of indirection, but is it worth it?

void Poison_BInt(void * chans)
{
	((BInt_chans*)chans)->is_poisoned = 1;
}

int IsPoisoned_BInt(void * chans)
{
	return ((BInt_chans*)chans)->is_poisoned;
}


typedef struct BInt_c_reader_tag_0
{
	OpChanReadBInt0 read;
	OpChanPoisonBInt0 poison;
	BInt_chans * chans;
} BInt_c_reader_0;

typedef struct BInt_c_writer_tag_0
{
	OpChanWriterBInt0 write;
	OpChanPoisonBInt0 poison;
} BInt_c_writer_0;



#define NUM_Delta_BUNDLE_ENDS 3
#define BUNDLE_END_Delta_in 0
#define BUNDLE_END_Delta_out1 1
#define BUNDLE_END_Delta_out2 2
typedef struct Delta_locals_tag
{
	int32 value;
	bundle_end * __ends__[3]; 
} Delta_locals;

proc * Delta_body(proc * p, scheduler * s)
{
	Delta_locals * locals = (Delta_locals*) p->locals;
	int i;
	while(1) // while not exiting, blocked, or throwing an exception; rather than setting flags merely returns for those cases
	{
		switch(p->state) 
		{
				 
			case _PS_READY_TO_RUN_ : 
				retval = locals->__ends__[0]->read(__ends__[0]->chans, *(locals->value), p->exception);
				switch(retval)
				{
					case 0 : // got a value
						p->state = _PS_READY_TO_RUN_ + 1;
						break;
					case 1 : // blocked waiting for a value
						return p;
					case 2 : // caught an exception
						// TODO poison channels here? or let the scheduler handle it? pro: we have the locals and could save space in p; con: we save code size if the scheduler handles it for all procs (I'm currently leaning to handling in the proc_body
						return p;
					default:
						// TODO throw an exception?
				}
				break;
			case _PS_READY_TO_RUN_ + 1 : 
				retval = locals->__ends__[1]->write(__ends__[1]->chans, locals->value, p->exception);
				switch(retval)
				{
					case 0 : // sent the value
						p->state = _PS_READY_TO_RUN_ + 2;
						break;
					case 1 : // blocked waiting to send
						return p;
					case 2 : // caught an exception
						// TODO poison channels here? or let the scheduler handle it? pro: we have the locals and could save space in p; con: we save code size if the scheduler handles it for all procs (I'm currently leaning to handling in the proc_body
						return p;
					default:
						// TODO throw an exception?
				}
				break;
			case _PS_READY_TO_RUN_ + 2 : 
				retval = locals->__ends__[2]->write(__ends__[2]->chans, locals->value, p->exception);
				switch(retval)
				{
					case 0 : // sent the value
						p->state = _PS_READY_TO_RUN_;
						break;
					case 1 : // blocked waiting to send
						return p;
					case 2 : // caught an exception
						// TODO poison channels here? or let the scheduler handle it? pro: we have the locals and could save space in p; con: we save code size if the scheduler handles it for all procs (I'm currently leaning to handling in the proc_body
						return p;
					default:
						// TODO throw an exception?
				}
				break;
			default : 
				// TODO throw an exception?
				
		}
	}	
}


// Delta_locals constructor
Delta_locals * Delta_locals_con(Delta_locals * locals, bundle_end ** ends, int num_ends)
{
	int i;
	for(i = 0; i < num_ends; ++i)
	{
		locals->__ends__[i] = ends[i];
	}
}

#define NUM_Succ_BUNDLE_ENDS 2
#define BUNDLE_END_Succ_in 0
#define BUNDLE_END_Succ_out 1
typedef struct Succ_locals_tag
{
	int32 value;
	bundle_end * __ends__[NUM_Succ_BUNDLE_ENDS];
} Succ_locals_tag

void

proc * Succ_body(proc * p, scheduler *s)
{
	Succ_locals * locals = (Succ_locals*) p->locals;
	int i;
	while(1) // while not exiting, blocked, or throwing an exception; rather than setting flags merely returns for those cases
	{
		switch(p->state)
		{
			case _PS_READY_TO_RUN_ : 
				retval = locals->__ends__[0]->read(__ends__[0]->chans, *(locals->value), p->exception);
				switch(retval)
				{
					case 0 : // got a value
						locals->value = locals->value + 1;
						p->state = _PS_READY_TO_RUN_ + 1;
						break;
					case 1 : // blocked waiting for a value
						return p;
					case 2 : // caught an exception
						// TODO poison channels here? or let the scheduler handle it? pro: we have the locals and could save space in p; con: we save code size if the scheduler handles it for all procs (I'm currently leaning to handling in the proc_body
						return p;
					default:
						// TODO throw an exception?
				}
				break;
			case _PS_READY_TO_RUN_ + 1 : 
				retval = locals->__ends__[1]->write(__ends__[1]->chans, locals->value, p->exception);
				switch(retval)
				{
					case 0 : // sent the value
						p->state = _PS_READY_TO_RUN_;
						break;
					case 1 : // blocked waiting to send
						return p;
					case 2 : // caught an exception
						// TODO poison channels here? or let the scheduler handle it? pro: we have the locals and could save space in p; con: we save code size if the scheduler handles it for all procs (I'm currently leaning to handling in the proc_body
						return p;
					default:
						// TODO throw an exception?
				}
				break;
			default : 
				// TODO throw an exception?
				
		}
	}
}



// Succ_locals constructor
Succ_locals * Succ_locals_con(Succ_locals * locals, bundle_end ** ends)
{
	int i;
	for(i = 0; i < NUM_Succ_BUNDLE_ENDS; ++i)
	{
		locals->__ends__[i] = ends[i];
	}
}


#define NUM_Prefix_BUNDLE_ENDS 2
#define BUNDLE_END_Prefix_in 0
#define BUNDLE_END_Prefix_out 1
typedef struct Prefix_locals_tag
{
	int32 value;
	bundle_end * __ends__[NUM_Prefix_BUNDLE_ENDS]; 
} Prefix_locals;

// Prefix_locals constructor
Prefix_locals * Prefix_locals_con(Prefix_locals * locals, bundle_end ** ends, int32 initValue)
{
	int i;
	for(i = 0; i < NUM_Prefix_BUNDLE_ENDS; ++i)
	{
		locals->__ends__[i] = ends[i];
	}
	locals->value = initValue;
}

void Prefix_body(proc * p, scheduler * s)
{
	Prefix_locals * locals = (Prefix_locals*) p->locals;
	int i;
	while(1) // while not exiting, blocked, or throwing an exception; rather than setting flags merely returns for those cases
	{
		switch(p->state)
		{
			case _PS_READY_TO_RUN_ : 
				retval = locals->__ends__[0]->read(__ends__[0]->chans, *(locals->value), p->exception);
				switch(retval)
				{
					case 0 : // got a value
						p->state = _PS_READY_TO_RUN_ + 1;
						break;
					case 1 : // blocked waiting for a value
						return p;
					case 2 : // caught an exception
						// TODO poison channels here? or let the scheduler handle it? pro: we have the locals and could save space in p; con: we save code size if the scheduler handles it for all procs (I'm currently leaning to handling in the proc_body
						return p;
					default:
						// TODO throw an exception?
				}
				break;
			case _PS_READY_TO_RUN_ + 1 : 
				retval = locals->__ends__[1]->write(__ends__[1]->chans, locals->value, p->exception);
				switch(retval)
				{
					case 0 : // sent the value
						p->state = _PS_READY_TO_RUN_;
						break;
					case 1 : // blocked waiting to send
						return p;
					case 2 : // caught an exception
						// TODO poison channels here? or let the scheduler handle it? pro: we have the locals and could save space in p; con: we save code size if the scheduler handles it for all procs (I'm currently leaning to handling in the proc_body
						return p;
					default:
						// TODO throw an exception?
				}
				break;
			default : 
				// TODO throw an exception?
				
		}
	}
}


#define NUM_Count_BUNDLE_ENDS 1
#define BUNDLE_END_Count_in 0
#define _COUNT_STEP_ 100000
typedef struct Count_locals_tag
{
	int32 value;
	int32 time;
	bundle_end * __ends__[NUM_Count_BUNDLE_ENDS]; 
} Count_locals;

// Count_locals constructor
Count_locals * Count_locals_con(Count_locals * locals, bundle_end ** ends)
{
	int i;
	for(i = 0; i < NUM_Count_BUNDLE_ENDS; ++i)
	{
		locals->__ends__[i] = ends[i];
	}

}

void Count_body(proc * p, scheduler * s)
{
	Count_locals * locals = (Count_locals*) p->locals;
	while(1)
	{
		switch(locals->state)
		{
		}
	}
}


typedef struct Commstime_locals_tag
{
	BInt_chans a;
	BInt_chans b;
	BInt_chans c;
	BInt_chans d;
	proc pDelta1;
	proc pSucc1;
	proc pPrefix1;
	proc pCount1;

} Commstime_locals;

void Commstime_body(proc * p, scheduler * s)
{
	Commstime_locals * locals = (Commstime_locals*) p->locals;
}

void Schedule_default(proc * p)
{

}


// TODO we need to understand
// how a parent is notified (and can safely check) that a child has completed
//		when a child exits, only the scheduler has access to its state
//		the child's state must convey whether it is done
//		special values of state (document by proc (should state be an unsigned int?)):
//			exception - 0
//			exited normally - 1
//			ready to run/blocked - 2+
//			
// how a parent can safely inject an exception into a child that is blocked 
// how return values are comunicated 
//		If we generate custom code for these cases, we don't need to worry about exposing the channels/parameters directly via proc (and that would probably be best)
//		Poisoning channels and getting return values should not have race conditions 
//			(poison is one way, so the only danger is if it can ever be lost, but once a process is scheduled it is never assigned to be false)
//			(once a channel marks itself as completed (either normally or with exception) only its parent should ever touch its state again)
//
//		When a child gets an exception, it is responsible to poison all its channels and schedule the processes that are connected to those channels
//		When a parent injects an exception, it must do so by poisoning all the child's channels and scheduling it
//
// Implementing par and seq (and fork, join) - although they are standard patterns, they should be emitted as part of the parent's code
// Join is the hardest, since the parent won't get the child's info until much later
//		however, it can be implemented with a buffered channel of the same length as the number of children it is expecting (or an unbounded buffer if the number is unbounded; this would pose a problem for real-time determanism)

int default_scheduler(proc * init_p)
{
	int result;
	int running_procs = 1;
	// TODO just a round robin scheduler for now; more sophistication later
	proc * ready = init_p;
	OpSchedule si = Schedule_default;
	while(running_procs > 0)
	{
		result = ready->body(ready, si);
		if(result->exception != 0)
		{
			printf(result->exception->info); //TODO plenty to add here
			return 1;
		}
		// TODO need to figure out the list structure
	}
	return 0;
}

int main(int argc, char ** argv)
{
	proc mproc;
	Commstime_locals mlocals;
	mproc.parent = 0;
	mproc.locals = &mlocals;
	mproc.state = 0;
	mproc.exception = null;
	mproc.num_bundle_ends = 4;
	mproc.bundle_end = &mlocals.__ends__;
	mproc.body = Commstime_body;
	
	return default_scheduler(&mproc);
}