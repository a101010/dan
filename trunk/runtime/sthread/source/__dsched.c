// The default scheduler

// Copyright 2009, Alan Grover

#include <stdio.h>
#include "__spinlock.h"
#include "__proc.h"
#include "__dsched.h"


// this is a default implementation of OpSchedule
// return = 0 if successful
int Schedule_default(scheduler * s, proc * p)
{
	Scheduler_default * _ctxt;
	list_node * result_node;

	lock(p->spinlock);
	if(p->sched_state == _RUNNING_ || p->sched_state == _READY_)
	{
		unlock(p->spinlock);
		return 0;
	}
	
	printf("Schedule_default: Scheduling process %s\n", p->proc_name);

	_ctxt = (Scheduler_default*) s->ctxt;
	if(p->sched_state == _BLOCKED_)
	{
		p->sched_state = _READY_;
		result_node = list_remove( &(_ctxt->unready), &(p->node));
		if(result_node == 0)
		{
			printf("Schedule_default error: node could not be removed from the unready list\n");
			unlock(p->spinlock);
			return -1;
		}
		--(_ctxt->num_unready);
		list_push_tail( &(_ctxt->ready), &(p->node));
		++(_ctxt->num_ready);
	}
	else if(p->sched_state == _FREE_)
	{
		p->sched_state = _READY_;
		list_push_tail( &(_ctxt->ready), &(p->node));
		++(_ctxt->num_ready);
	}
	else
	{
		printf("Schedule_default: process %s in unexpected sched_state: %s\n", p->proc_name, scheduler_state_to_string(p->sched_state));
		return -1;
	}
	unlock(p->spinlock);
	return 0;
}


// default_scheduler 
// 
// default scheduler constructor
void default_scheduler_ctor(Scheduler_default * ctxt)
{
	ctxt->s.ctxt = ctxt;
	ctxt->s.schedule =  Schedule_default;
	ctxt->num_ready = 0;
	ctxt->num_unready = 0;
	ctxt->ready = 0;
	ctxt->unready = 0;
}

//int default_scheduler_finish_proc(Scheduler_default * ctxt, proc * p)
int default_scheduler_finish_proc(proc * p)
{
    printf("scheduler: cleaning up process %s\n", p->proc_name);
	p->sched_state = _FINISHED_;
	return 0;
}

void default_scheduler_block_proc(Scheduler_default * ctxt, proc * p)
{
    printf("scheduler: blocking process %s\n", p->proc_name);
	lock(p->spinlock);
	p->sched_state = _BLOCKED_;
	unlock(p->spinlock);

	list_push_tail( &(ctxt->unready), &(p->node));
	++(ctxt->num_unready);
}

// default scheduler run loop
// returns 0 if all process have exited; 1 if exception or deadlock
int default_scheduler_run(Scheduler_default * ctxt)
{
	int ret_val = 1;
	list_node * pn;
	proc * p;
	while(ctxt->num_ready > 0)
	{
		pn = list_pop_head( &(ctxt->ready));
		--(ctxt->num_ready);

		p = (proc*) pn;

		lock(p->spinlock);
		p->sched_state = _RUNNING_;
		unlock(p->spinlock);

        printf("scheduler: running process %s\n", p->proc_name);
		p->body(p, &(ctxt->s));



		if(p->state == _PS_EXCEPTION_)
		{
			if(0 != (ret_val = default_scheduler_finish_proc(p)))
			{
				return ret_val;
			}


			// TODO need to handle list_node and any other cleanup

			// if there is a parent, schedule it so it has a chance to handle the exception
			if(p->parent != 0)
			{
                printf("scheduler: first chance exception from %s: %s\n", p->proc_name, p->exception);
				if(0 != (ret_val = Schedule_default( &(ctxt->s), p->parent)))
				{
					printf("Fatal error from Schedule_default while trying to schedule parent %s of proc %s with unhandled exception\n", p->parent, p);
					return ret_val;
				}
			}
			else
			{
                printf("scheduler: Unhandled exception from %s: %s\n", p->proc_name, p->exception);
				return 1;
			}
		}
		else if(p->state == _PS_CLEAN_EXIT_)
		{

			if(0 != (ret_val = default_scheduler_finish_proc(p)))
			{
				return ret_val;
			}
			
			// if there is a parent, schedule it so it has a chance to see if all its children have completed
			// TODO possible race condition if parent is running and doesn't check that this child exited
			if(p->parent != 0)
			{
				if(0 != (ret_val = Schedule_default( &(ctxt->s), p->parent)))
				{
                    printf("scheduler: Fatal error from Schedule_default while trying to schedule parent %s of exiting proc %s\n", p->parent, p);
					return ret_val;
				}
			}
		}
		else
		{
			// This is cooperative multitasking, so if it returns, it is blocked and must be rescheduled by another process
			default_scheduler_block_proc(ctxt, p);
		}

		
	}
	if(ctxt->num_unready > 0)
		return 1;

	return 0;
}

