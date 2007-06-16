// Copyright 2007, Alan Grover

// This .c file represents the expected output (more or less) of the dan compiler for commstime.dan

// A single processor version. 

#include <stdio.h>

// spin locks are chatty no-ops right now  TODO use a flag for test and set rather than a string
#define lock(spinlock) printf("lock %s\n", spinlock);
#define unlock(spinlock) printf("unlock %s\n", spinlock);

typedef int int32;
typedef unsigned int uint;

extern struct proc_tag;
typedef struct proc_tag proc;

// doubly linked list of procs
typedef struct proc_node_tag 
{
	struct proc_node_tag * up, * down;
	struct proc_tag * p;
} proc_node;

// push n to the front of the list, n becomes (*head)
// if (*head) is 0, n becomes (*head)
// no effect if n == 0 or head == 0
void pl_push_head(proc_node ** head, proc_node * n)
{
	if(0 == n || 0 == head)
		return;

	if(0 == (*head))
	{
		n->up = n;
		n->down = n;
	}
	else
	{
		proc_node * old_tail = (*head)->up;
		(*head)->up = n;
		n->down = (*head);
		n->up = old_tail;
	}
	(*head) = n;
}

// push n onto the back of the list
// if (*head) is 0, n becomes (*head)
// no effect if n == 0 or head == 0
void pl_push_tail(proc_node ** head, proc_node * n)
{
	if(0 == n || 0 == head)
		return;

	if(0 == (*head))
	{
		n->up = n;
		n->down = n;
		(*head) = n;
	}
	else
	{
		proc_node * old_tail;
		old_tail = (*head)->up;
		(*head)->up = n;
		n->up = old_tail;
		n->down = (*head);
		old_tail->down = n;
	}
}

// pop the (*head) from the list
// if only one node in the list, (*head) will == 0
// no effect if head or (*head) == 0
// returns: the head of the list or 0
proc_node * pl_pop_head(proc_node ** head)
{
	proc_node * ret_val;

	if(head == 0)
		return 0;

	if((*head) == 0)
		return 0;

	ret_val = (*head);

	// if nothing else in the list, the list is empty
	if(ret_val->down == ret_val)
	{
		(*head) = 0;
	}
	else
	{
		(*head) = ret_val->down;
		(*head)->up = ret_val->up;
	}
	ret_val->up = 0;
	ret_val->down = 0;
	return ret_val;
}

// pop the tail from the list
// if only one node in the list, (*head) will == 0
// no effect if head or (*head) == 0
// returns: the tail of the list or 0
proc_node * pl_pop_tail(proc_node ** head)
{
	proc_node * ret_val;

	if(head == 0)
		return 0;

	if((*head) == 0)
		return 0;

	ret_val = (*head)->up;

	// if nothing else in the list, the list is empty
	if(ret_val->up == ret_val)
	{
		(*head) = 0;
	}
	else 
	{
		(*head)->up = ret_val->up;
		(*head)->up->down = (*head);
	}
	ret_val->up = 0;
	ret_val->down = 0;
	return ret_val;
}

extern struct scheduler_tag;
typedef struct scheduler_tag scheduler;

typedef void (*OpSchedule)(scheduler * ctxt, struct proc_tag * p);

typedef void * sched_context;

// scheduler 
// 
// This is the proc's interface to the scheduler 
typedef struct scheduler_tag
{
	sched_context ctxt; // this is opaque to the proc, and that opacity needs to be enforced, probably by the installer
	OpSchedule schedule;
} scheduler;

typedef void (*OpProcBody)(proc * p, scheduler * s);

// some predefined special values for proc.state
#define _PS_EXCEPTION_ 0
#define _PS_CLEAN_EXIT_ 1
#define _PS_READY_TO_RUN_ 2

enum SCHED_STATE 
{
	_FREE_ = 0,
	_RUNNING_,
	_READY_,
	_BLOCKED_,
	_FINISHED_ // duplicates proc.state values?  TODO make sure we really need in proc.sched_state
};

typedef struct proc_tag 
{
	// TODO what can we do to shrink this?
	char * proc_name; // this may ultimately evolve into a pointer to a proc type structure (similar to a type class in Java or .NET) For now we need it for exeptions and debugging
	proc * parent; // we need it so we can schedule our parent when we exit
	void * locals;
	uint state; // corresponds to the instruction pointer
	char * exception; // TODO eventually we need an exception structure
	OpProcBody body;
	char * spinlock; // protects the sched_state TODO need to protect anything else?
	int sched_state; // state in the scheduler TODO can combine with the spin lock?
	proc_node * node; // TODO I don't really like this... it's a pointer to the scheduler's list node for this proc and is needed by OpSchedule; need to carefully consider alternatives (one is to have proc itself be the list node...)
} proc;

// constructor
void proc_ctor(proc * p, char * proc_name, proc * parent, void * locals, OpProcBody body)
{
	p->proc_name = proc_name;
	p->parent = parent;
	p->locals = locals;
	p->exception = 0;
	p->state = _PS_READY_TO_RUN_;
	p->body = body;
	p->sched_state = _FREE_;
	p->spinlock = p->proc_name;
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
typedef int (*OpChanRead_BInt_c_0)(BInt_chans * chans, int32 * value, char ** ex, proc * p, scheduler * s);

int ChanRead_BInt_c_0(BInt_chans * chans, int32 * value, char ** exception, proc * p, scheduler * s)
{
	int ret_val = 2;
	// TODO get the lock
	if(chans->is_poisoned)
	{
		*exception = "channel poison";
	}
	else
	{
		if(chans->c_valid_value)
		{
			*value = chans->c_value;
			chans->c_valid_value = 0;
			if(chans->writer_waiting != 0)
			{
				s->schedule(s, chans->writer_waiting);
				chans->writer_waiting = 0;
			}
			ret_val = 0;
		}
		else
		{
			chans->reader_waiting = p;
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
		chans->c_valid_value = 1;
		if(chans->reader_waiting != 0)
		{
//			s->schedule(chans->reader_waiting);
			chans->reader_waiting = 0;
			ret_val = 0;
		}
		else
		{
			chans->writer_waiting = p;
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

/*
typedef struct BInt_c_reader_tag_0
{
	OpChanReadBInt0 read;
	OpChanPoisonBInt0 poison;
	BInt_chans * chans;
} BInt_c_reader_0;
*/

/*
typedef struct BInt_c_writer_tag_0
{
	OpChanWriterBInt0 write;
	OpChanPoisonBInt0 poison;
} BInt_c_writer_0;
*/


#define NUM_Delta_BUNDLE_ENDS 3
#define BUNDLE_END_Delta_in 0
#define BUNDLE_END_Delta_out1 1
#define BUNDLE_END_Delta_out2 2
typedef struct Delta_locals_tag
{
	int32 value;
	bundle_end * __ends__[3]; 
} Delta_locals;

#ifdef Fool
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
#endif

// Delta_locals constructor
Delta_locals * Delta_locals_ctor(Delta_locals * locals, bundle_end ** ends, int num_ends)
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
} Succ_locals;


#ifdef Fool
proc * Succ_body(proc * p, scheduler *s)
{
	proc * retval;
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
#endif


// Succ_locals constructor
Succ_locals * Succ_locals_ctor(Succ_locals * locals, bundle_end ** ends)
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
Prefix_locals * Prefix_locals_ctor(Prefix_locals * locals, bundle_end ** ends, int32 initValue)
{
	int i;
	for(i = 0; i < NUM_Prefix_BUNDLE_ENDS; ++i)
	{
		locals->__ends__[i] = ends[i];
	}
	locals->value = initValue;
}

#ifdef Fool
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
#endif

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
Count_locals * Count_locals_ctor(Count_locals * locals, bundle_end ** ends)
{
	int i;
	for(i = 0; i < NUM_Count_BUNDLE_ENDS; ++i)
	{
		locals->__ends__[i] = ends[i];
	}

}

#ifdef Fool
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
#endif


typedef struct Commstime_locals_tag
{
	int testInt;
	//BInt_chans a;
	//BInt_chans b;
	//BInt_chans c;
	//BInt_chans d;
	//proc pDelta1;
	//proc pSucc1;
	//proc pPrefix1;
	//proc pCount1;

} Commstime_locals;

Commstime_locals * Commstime_locals_ctor(Commstime_locals * locals, int testInt)
{
	locals->testInt = testInt;
	return locals;
}

void Commstime_body(proc * p, scheduler * s)
{
	Commstime_locals * locals = (Commstime_locals*) p->locals;
	printf("testInt == %d\n", locals->testInt);
	p->state = _PS_CLEAN_EXIT_;
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


// Scheduler notes
// 
// When a proc needs to schedule another proc, it calls the schedule method passed to it
// this could either add the proc to a list of procs to be scheduled, or it could add it directly to the ready list
// We need to be able to find the proc_node to insert it in the ready list, and it must not already be in the ready list
// Or we must be able to see that it is in the ready list and not add it again
// If each proc_node also knows which list it is associated with, we can do that (triplly linked list?), however, that is not scaleable to
// the situation of multiple schedulers (such as one scheduler per processor)
// We don't want a proc to be able to exist in the ready list twice
// We don't want a proc to be able to exist in the ready list of multiple schedulers
// But we want a proc to be able to be run on a processor other than its parents/children
// We could still use the triplly linked lists to confirm whether something is in the other scheduler, but not safely
// Of course, we'll only have this problem (for the case of a child notifying a parent) when the parent forked the child, which is still down the road a bit
// Otherwise, the parent cannot run until all children have exited
// But we can have a race between two exiting children
// We could require a lock, but where?  the scheduler will navigate to the proc from the proc_node and the proc will navigate to the scheduler (to find out if its parent is ready) through its proc node
// and that connection is subject to the race
// We need to solve this for the case of a channel scheduling a proc also
// We can use schedule_state (governed by a lock) in the proc itself - running, scheduled, notscheduled
// We need all three because 'running' is a special case; if it is already running, it may miss our signal and need to be rescheduled 
// not sure how to do that; we need to wait until the state changes from running which means either buisy waiting or adding a command to a queue
// Can we just make sure that any operation where a proc waits (for a channel or completing child) checks if the channel is ready or a child has finished and sets schedule_state to not_running as an atomic sequence?


// TODO make proc.h (for proc, proc_node, and pl functions

// TODO will probably have to put double underscores __around__ all of the identifiers... <sigh>  later!

//extern struct Schedule_default_tag;
//typedef struct Schedule_default_tag Schedule_default;



// this is the default implementation of the scheduler
// scheduler->ctxt should point to this
typedef struct Scheduler_default_tag
{
	int num_ready;
	proc_node * ready;  // the ready list
	int num_unfinished;
	proc_node * unfinished; // the list of procs that are not ready but have not exited
	int proc_node_pool_size;
	proc_node * proc_node_pool; // the pool of unallocated proc_nodes (array of proc_nodes)
	int next_proc_node; // index in to proc_node_pool of the next unallocated proc // TODO this does not recycle proc_nodes, but that isn't important for commstime
} Scheduler_default;

// this is a default implementation of OpSchedule
int Schedule_default(scheduler * ctxt, proc * p)
{
	Scheduler_default * _ctxt;
	proc_node * pn;

	lock(p->spinlock);
	if(p->sched_state == _RUNNING_ || p->sched_state == _READY_)
	{
		unlock(p->spinlock);
		return 0;
	}
	
	_ctxt = (Scheduler_default*) ctxt;
	if(p->sched_state == _BLOCKED_)
	{
		p->sched_state = _READY_;
		pl_push_tail(&(_ctxt->ready), p->node);
		++(_ctxt->num_ready);
	}
	else if(p->sched_state == _FREE_)
	{
		if(_ctxt->next_proc_node >= _ctxt->proc_node_pool_size)
		{
			printf("proc_node pool exhausted\n");
			unlock(p->spinlock);
			return -1;
		}
		pn = _ctxt->proc_node_pool + _ctxt->next_proc_node;
		++(_ctxt->next_proc_node);
		pn->p = p;
		p->node = pn;
		p->sched_state = _READY_;
		pl_push_tail(&(_ctxt->ready), p->node);
		++(_ctxt->num_ready);
		pl_push_tail(&(_ctxt->unfinished), p->node);
		++(_ctxt->num_unfinished);
	}
	unlock(p->spinlock);
	return 0;
}


#define PROC_NODE_POOL_SIZE 2

// default_scheduler 
// 
// default scheduler constructor
void default_scheduler_ctor(Scheduler_default * ctxt, int proc_node_pool_size, proc_node * proc_node_pool)
{
	ctxt->num_ready = 0;
	ctxt->num_unfinished = 0;
	ctxt->proc_node_pool_size = proc_node_pool_size;
	ctxt->proc_node_pool = proc_node_pool;
	ctxt->next_proc_node = 0;
	ctxt->ready = 0;
	ctxt->unfinished = 0;
}

int default_scheduler_finish_proc(Scheduler_default * ctxt, proc * p)
{

	--(ctxt->num_unfinished);
	if(p->node != ctxt->unfinished)
	{
		// TODO add removal from the middle of the list to the pl_ functions
		p->node->up->down = p->node->down;
		p->node->down->up = p->node->up;
		p->node->up = 0;
		p->node->down = 0;
		// TODO recycle the proc_node
		p->node = 0;
	}
	else
	{
		pl_pop_head(&(ctxt->unfinished));
	}
	return 0;
}

// default scheduler run loop
// returns 0 if all process have exited; 1 if exception or deadlock
int default_scheduler_run(Scheduler_default * ctxt)
{
	int ret_val = 1;
	proc * p;
	while(ctxt->num_ready > 0)
	{
		p = ctxt->ready->p;

		lock(p->spinlock);
		p->sched_state = _RUNNING_;
		--(ctxt->num_ready);
		unlock(p->spinlock);

		p->body(p, ctxt);



		if(p->state == _PS_EXCEPTION_)
		{
			default_scheduler_finish_proc(ctxt, p);

			--(ctxt->num_unfinished);
			if(p->node != ctxt->unfinished)
			{
				p->node->up->down = p->node->down;
				p->node->down->up = p->node->up;
			}
			else
			{
				pl_pop_head(&(ctxt->unfinished));
			}


			// TODO need to handle proc_node and any other cleanup
			if(p->parent != 0)
			{
				printf("Unhandled exception from %s: %s\n", p->proc_name, p->exception);
				if(0 != (ret_val = Schedule_default(ctxt, p->parent)))
				{
					printf("Fatal error from Schedule_default while trying to schedule parent of proc with unhandled exception\n");
					return ret_val;
				}
			}
			else
			{
				printf("Unhandled exception from %s: %s\n", p->proc_name, p->exception);
				return 1;
			}
		}
		else if(p->state == _PS_CLEAN_EXIT_)
		{

			default_scheduler_finish_proc(ctxt, p);
			
		}

		
	}
	if(ctxt->num_unfinished > 0)
		return 1;

	return 0;
}



int main(int argc, char ** argv)
{
	Scheduler_default ctxt;
	Commstime_locals mproc_locals;
	proc mproc;
	char buffer[100];
	int ret_val = 0;



	proc_node proc_node_pool[PROC_NODE_POOL_SIZE];
	default_scheduler_ctor(&ctxt, PROC_NODE_POOL_SIZE, proc_node_pool);

	
	Commstime_locals_ctor(&mproc_locals, 400);

	
	proc_ctor(&mproc, "Commstime", 0, &mproc_locals, Commstime_body);

	ret_val = Schedule_default(&ctxt, &mproc); // TODO just exit?  assert: ctxt.num_ready == 0 if ret_val != 0
	

	ret_val = default_scheduler_run(&ctxt);

	if(ctxt.num_unfinished > 0)
	{
		printf("Number of deadlocked processes = %d\n", ctxt.num_unfinished);
		ret_val = 1;
	}

	printf("Press <enter> to exit.\n");
	gets(&buffer);
	return ret_val;
}