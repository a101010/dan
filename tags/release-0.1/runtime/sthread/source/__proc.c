#include "__proc.h"

char * proc_state_to_string(uint32 state)
{
    switch(state)
    {
    case _PS_EXCEPTION_:
        return "EXCEPTION";
    case _PS_CLEAN_EXIT_:
        return "CLEAN_EXIT";
    case _PS_READY_TO_RUN_:
        return "READY_TO_RUN";
    default:
        return "RUNNING";
    }
}

char * scheduler_state_to_string(scheduler_state state)
{
	if(state == _FREE_)
		return "FREE";
	if(state == _RUNNING_)
		return "RUNNING";
	if(state == _READY_)
		return "READY";
	if(state == _BLOCKED_)
		return "BLOCKED";
	if(state == _FINISHED_)
		return "FINISHED";
	else
		return "UNKNOWN";
}

// constructor
void proc_ctor(proc * p, char * proc_name, proc * parent, /* void * locals,*/ OpProcBody body)
{
	p->proc_name = proc_name;
	p->parent = parent;
	//p->locals = locals;
	p->exception = 0;
	p->state = _PS_READY_TO_RUN_;
	p->body = body;
	p->sched_state = _FREE_;
	p->spinlock = p->proc_name;
}