#ifndef __PROC_H_
#define __PROC_H_

#include "__builtins.h"
#include "__list.h"

// some predefined special values for proc.state
#define _PS_EXCEPTION_ 0
#define _PS_CLEAN_EXIT_ 1
#define _PS_READY_TO_RUN_ 2

char * proc_state_to_string(uint32 state);

typedef enum sched_state_tag 
{
	_FREE_ = 0,
	_RUNNING_,
	_READY_,
	_BLOCKED_,
	_FINISHED_ 
} scheduler_state;

char * scheduler_state_to_string(scheduler_state state);

struct scheduler_tag;
struct proc_tag;

typedef int (*OpSchedule)(struct scheduler_tag * ctxt, struct proc_tag * p);

typedef void * sched_context;

// scheduler 
// 
// This is the proc's interface to the scheduler 
typedef struct scheduler_tag
{
	sched_context ctxt; // this is opaque to the proc, and that opacity needs to be enforced, probably by the installer
	OpSchedule schedule;
} scheduler;

typedef void (*OpProcBody)(struct proc_tag * p, scheduler * s);

typedef struct proc_tag 
{
	// TODO what can we do to shrink this?
    list_node node;
	char * proc_name;   // this may ultimately evolve into a pointer to a proc type structure 
                        // (similar to a type class in Java or .NET) For now we need it for exeptions 
                        // and debugging
	struct proc_tag * parent; // we need it so we can schedule our parent when we exit
	//void * locals;
	uint32 state; // corresponds to the instruction pointer 
                // see saveState and restoreState, and the special defines above
	char * exception; // TODO eventually we need an exception structure
	OpProcBody body;
	char * spinlock; // protects the sched_state TODO need to protect anything else?
	scheduler_state sched_state; // state in the scheduler TODO can combine with the spin lock?
} proc;

// constructor
void proc_ctor(proc * p, char * proc_name, proc * parent, /* void * locals,*/ OpProcBody body);

#endif // __PROC_H_