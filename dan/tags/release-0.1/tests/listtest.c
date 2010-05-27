#include <stdio.h>
#include <assert.h>
#include "list.h"

// for test purposes, a proc is just a string
typedef struct proc_tag
{
	char * s;
} proc;


#define NUM_PROCS 5

int main()
{
	int i, list_size;
	proc_node * head, * temp;
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

	assert(head == &proc_nodes[4]);
	assert(head->down == &proc_nodes[3]);
	assert(head->down->down == &proc_nodes[2]);
	assert(head->down->down->down == &proc_nodes[1]);
	assert(head->down->down->down->down == &proc_nodes[0]);
	assert(head->down->down->down->down->down == &proc_nodes[4]);

	assert(head->up == &proc_nodes[0]);
	assert(head->up->up == &proc_nodes[1]);
	assert(head->up->up->up == &proc_nodes[2]);
	assert(head->up->up->up->up = &proc_nodes[3]);
	assert(head->up->up->up->up->up = &proc_nodes[4]);

	for(i = 0; i < NUM_PROCS; ++i)
	{
		assert(head == &proc_nodes[NUM_PROCS - i - 1]);
		for(temp = head, list_size = 1; temp->down != head; temp = temp->down, ++list_size)
		{}
		assert(list_size == NUM_PROCS - i);

		for(temp = head, list_size = 1; temp->up != head; temp = temp->up, ++list_size)
		{}
		assert(list_size == NUM_PROCS - i);

		temp = pl_pop_head(&head);
		assert(temp->up == 0 && temp->down ==0);
		printf("%s\n", (char*) ((proc*) temp->p)->s);
	}

	assert(head == 0);

	printf("\npl_push_tail followed by pl_pop_tail\n");

	for(i = 0; i < NUM_PROCS; ++i)
	{
		pl_push_tail(&head, &proc_nodes[i]);
	}

	assert(head == &proc_nodes[0]);
	assert(head->down == &proc_nodes[1]);
	assert(head->down->down == &proc_nodes[2]);
	assert(head->down->down->down == &proc_nodes[3]);
	assert(head->down->down->down->down == &proc_nodes[4]);
	assert(head->down->down->down->down->down == &proc_nodes[0]);

	assert(head->up == &proc_nodes[4]);
	assert(head->up->up == &proc_nodes[3]);
	assert(head->up->up->up == &proc_nodes[2]);
	assert(head->up->up->up->up = &proc_nodes[1]);
	assert(head->up->up->up->up->up = &proc_nodes[0]);


	for(i = 0; i < NUM_PROCS; ++i)
	{
		assert(head == &proc_nodes[0]);
		for(temp = head, list_size = 1; temp->down != head; temp = temp->down, ++list_size)
		{}
		assert(list_size == NUM_PROCS - i);

		for(temp = head, list_size = 1; temp->up != head; temp = temp->up, ++list_size)
		{}
		assert(list_size == NUM_PROCS - i);

		printf("%s\n", (char*) ((proc*) pl_pop_tail(&head)->p)->s);
	}

	assert(head == 0);

	printf("\npl_push_head followed by pl_pop_tail\n");
	for(i = 0; i < NUM_PROCS; ++i)
	{
		pl_push_head(&head, &proc_nodes[i]);
	}

	for(i = 0; i < NUM_PROCS; ++i)
	{
		printf("%s\n", (char*) ((proc*) pl_pop_tail(&head)->p)->s);
	}

	assert(head == 0);

	printf("\npl_push_tail followed by pl_pop_head\n");

	for(i = 0; i < NUM_PROCS; ++i)
	{
		pl_push_tail(&head, &proc_nodes[i]);
	}

	for(i = 0; i < NUM_PROCS; ++i)
	{
		printf("%s\n", (char*) ((proc*) pl_pop_head(&head)->p)->s);
	}

	assert(head == 0);

	printf("\npl_push_head once followed by pl_remove\n");

	pl_push_head(&head, &proc_nodes[0]);

	pl_remove(&head, &proc_nodes[0]);

	assert(head == 0);

	printf("\npl_push_head followd by pl_remove in order 2 1 0 3 4\n");

	for(i = 0; i < NUM_PROCS; ++i)
	{
		pl_push_head(&head, &proc_nodes[i]);
	}

	pl_remove(&head, &proc_nodes[2]);
	pl_remove(&head, &proc_nodes[1]);
	pl_remove(&head, &proc_nodes[0]);
	pl_remove(&head, &proc_nodes[3]);
	pl_remove(&head, &proc_nodes[4]);


}

