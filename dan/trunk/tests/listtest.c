#include <stdio.h>

// for test purposes, a proc is just a string
typedef struct proc_tag
{
	char * s;
} proc;

// doubly linked list of procs
typedef struct proc_node_tag 
{
	struct proc_node_tag * up, * down;
	proc * p;
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


#define NUM_PROCS 5
int main()
{
	int i;
	proc_node * head;
	proc_node proc_nodes[NUM_PROCS];
	proc procs[NUM_PROCS];
	procs[0].s = "ZERO";
	procs[1].s = "ONE";
	procs[2].s = "TWO";
	procs[3].s = "THREE";
	procs[4].s = "FOUR";

	head = 0;

	for(i = 0; i < NUM_PROCS; ++i)
	{
		proc_nodes[i].p = &procs[i];
		proc_nodes[i].up = 0;
		proc_nodes[i].down = 0;
	}

	printf("\npl_push_head followed by pl_pop_head\n");
	for(i = 0; i < NUM_PROCS; ++i)
	{
		pl_push_head(&head, &proc_nodes[i]);
	}

	for(i = 0; i < NUM_PROCS; ++i)
	{
		printf("%s\n", pl_pop_head(&head)->p->s);
	}
	printf("\npl_push_tail followed by pl_pop_tail\n");

	for(i = 0; i < NUM_PROCS; ++i)
	{
		pl_push_tail(&head, &proc_nodes[i]);
	}

	for(i = 0; i < NUM_PROCS; ++i)
	{
		printf("%s\n", pl_pop_tail(&head)->p->s);
	}

	printf("\npl_push_head followed by pl_pop_tail\n");
	for(i = 0; i < NUM_PROCS; ++i)
	{
		pl_push_head(&head, &proc_nodes[i]);
	}

	for(i = 0; i < NUM_PROCS; ++i)
	{
		printf("%s\n", pl_pop_tail(&head)->p->s);
	}
	printf("\npl_push_tail followed by pl_pop_head\n");

	for(i = 0; i < NUM_PROCS; ++i)
	{
		pl_push_tail(&head, &proc_nodes[i]);
	}

	for(i = 0; i < NUM_PROCS; ++i)
	{
		printf("%s\n", pl_pop_head(&head)->p->s);
	}
}

