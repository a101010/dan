// Copyright 2007, Alan Grover

// This .c file represents the expected output (plus comments) of the dan compiler for commstime.dan

// A single processor version.  TODO Need to add locks for SMP systems.

typedef int int32;

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
} BInt_chans;

// returns 0 if it got a valid value; 1 if it blocked; and 2 if an exception was thrown
typedef int (*OpChanRead_BInt_c_0)(BInt_chans * chans, int32 * value, void * exception);

int ChanRead_BInt_c_0(BInt_chans * chans, int32 * value, void * exception)
{
}

// returns 0 if it wrote the value; 1 if it blocked; and 2 if an exception was thrown
typedef int (*OpChanWrite_BInt_c_0)(BInt_chans * chans, int32 value, void * exception);

int ChanWrite_BInt_c_0(BInt_chans * chans, int32 value, void * exception)
{

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


extern proc;

typedef void (*OpSchedule)(proc * p);

typedef struct scheduler_tag
{
	OpSchedule schedule;
} scheduler;

typedef void (*OpProcBody)(proc * p, scheduler * s);

typedef struct Exception_tag
{
	char * info;
	int info_length;
} Exception;

typedef struct proc_tag 
{
	proc * parent;
	void * locals;
	int state; // TODO long int?
	int num_bundle_ends;
	bundle_end * bundle_ends; // may not need this, may be able to do in the proc_body  (which is better?)
	Exception * exception;
	OpProcBody body;
} proc;


typedef struct Delta_locals_tag
{
	int32 value;
	bundle_end __ends__[3]; // 0 = in; 1 = out1; 2 = out2;
} Delta_locals;

proc * Delta_body(proc * p, scheduler * s)
{
	Delta_locals * locals = (Delta_locals*) p->locals;
	int i;
	while(1) // while not exiting, blocked, or throwing an exception; rather than setting flags merely returns for those cases
	{
		switch(p->state) // TODO use Duff's Device?
		{
			/*case 0 : // initialization*/
			/* TODO may need a seperate constructor, so that state gets set correctly */
				 
			case 1 : 
				retval = locals->__ends__[0]->read(__ends__[0]->chans, *(locals->value), p->exception);
				switch(retval)
				{
					case 0 : // got a value
						p->state = 2;
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
			case 2 : 
				retval = locals->__ends__[1]->write(__ends__[1]->chans, locals->value, p->exception);
				switch(retval)
				{
					case 0 : // sent the value
						p->state = 3;
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
			case 3 : 
				retval = locals->__ends__[2]->write(__ends__[2]->chans, locals->value, p->exception);
				switch(retval)
				{
					case 0 : // sent the value
						p->state = 1;
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

typedef struct Succ_locals_tag
{
	int32 value;
	bundle_end __ends__[2]; // 0 = in; 1 = out
} Succ_locals_tag

proc * Succ_body(proc * p, scheduler *s)
{
	Succ_locals * locals = (Succ_locals*) p->locals;
	int i;
	while(1) // while not exiting, blocked, or throwing an exception; rather than setting flags merely returns for those cases
	{
		switch(p->state)
		{
			/*case 0 : // initialization*/
			/* TODO may need a seperate constructor, so that state gets set correctly */
				 
			case 1 : 
				retval = locals->__ends__[0]->read(__ends__[0]->chans, *(locals->value), p->exception);
				switch(retval)
				{
					case 0 : // got a value
						locals->value = locals->value + 1;
						p->state = 2;
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
			case 2 : 
				retval = locals->__ends__[1]->write(__ends__[1]->chans, locals->value, p->exception);
				switch(retval)
				{
					case 0 : // sent the value
						p->state = 1;
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

typedef struct Prefix_locals_tag
{
	int32 value;
	bundle_ends __ends__[2]; // 0 = in; 1 = out
} Prefix_locals;

void Prefix_body(proc * p, scheduler * s)
{
Delta_locals * locals = (Delta_locals*) p->locals;
	int i;
	while(1) // while not exiting, blocked, or throwing an exception; rather than setting flags merely returns for those cases
	{
		switch(p->state)
		{
			/*case 0 : // initialization*/
			/* TODO may need a seperate constructor, so that state gets set correctly */
				 
			case 1 : 
				retval = locals->__ends__[0]->read(__ends__[0]->chans, *(locals->value), p->exception);
				switch(retval)
				{
					case 0 : // got a value
						++(p->state);
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
			case 2 : 
				retval = locals->__ends__[1]->write(__ends__[1]->chans, locals->value, p->exception);
				switch(retval)
				{
					case 0 : // sent the value
						++(p->state);
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

typedef struct Count_locals_tag
{
	int32 value;
	int32 time;
	bundle_ends __ends__[1]; // 0 = in
} Count_locals;

void Count_body(proc * p, scheduler * s)
{

}

typedef struct Commstime_locals_tag
{
	bundle_ends __ends__[4];
} Commstime_locals;

void Commstime_body(proc * p, scheduler * s)
{

}

void Schedule_default(proc * p)
{

}

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
		if(result->exception != null)
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
	mproc.bundle_ends = &mlocals.__ends__;
	mproc.body = Commstime_body;
	
	return default_scheduler(&mproc);
}