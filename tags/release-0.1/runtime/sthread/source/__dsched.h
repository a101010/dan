#ifndef __DSCHED_H_
#define __DSCHED_H_

// The default scheduler

// Copyright 2009, Alan Grover

// this is the default implementation of the scheduler
// scheduler->ctxt should point to this
typedef struct Scheduler_default_tag
{
	scheduler s; // interface to pass to processes
	int num_ready;
	list_node * ready;  // the ready list
	int num_unready;
	list_node * unready; // the list of procs that are not ready but have not exited
} Scheduler_default;

// this is a default implementation of OpSchedule
// return = 0 if successful
int Schedule_default(scheduler * s, proc * p);


// default_scheduler 
// 
// default scheduler constructor
void default_scheduler_ctor(Scheduler_default * ctxt);

//int default_scheduler_finish_proc(Scheduler_default * ctxt, proc * p)
int default_scheduler_finish_proc(proc * p);

void default_scheduler_block_proc(Scheduler_default * ctxt, proc * p);

// default scheduler run loop
// returns 0 if all process have exited; 1 if exception or deadlock
int default_scheduler_run(Scheduler_default * ctxt);

#endif // __DSCHED_H_
