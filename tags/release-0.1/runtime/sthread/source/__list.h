//
// __list.h
//
// A doubly linked list
//

#ifndef __LIST_H_
#define __LIST_H_

// doubly linked list of procs
typedef struct list_node_tag 
{
	struct list_node_tag * up; 
    struct list_node_tag * down;
} list_node;

// push n to the front of the list, n becomes (*head)
// if (*head) is 0, n becomes (*head)
// no effect if n == 0 or head == 0
void list_push_head(list_node ** head, list_node * n);

// push n onto the back of the list
// if (*head) is 0, n becomes (*head)
// no effect if n == 0 or head == 0
void list_push_tail(list_node ** head, list_node * n);

// pop the (*head) from the list
// if only one node in the list, (*head) will == 0
// no effect if head or (*head) == 0
// returns: the head of the list or 0
list_node * list_pop_head(list_node ** head);

// pop the tail from the list
// if only one node in the list, (*head) will == 0
// no effect if head or (*head) == 0
// returns: the tail of the list or 0
list_node * list_pop_tail(list_node ** head);

list_node * list_remove(list_node ** head, list_node * n);

#endif __LIST_H_