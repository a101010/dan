//
// list.h
//
// A doubly linked list
//

// doubly linked list of procs
typedef struct list_node_tag 
{
	struct list_node_tag * up; 
    struct list_node_tag * down;
} list_node;

// push n to the front of the list, n becomes (*head)
// if (*head) is 0, n becomes (*head)
// no effect if n == 0 or head == 0
void list_push_head(list_node ** head, list_node * n)
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
		list_node * tail = (*head)->up;
		(*head)->up = n;
		n->down = (*head);
		n->up = tail;
		tail->down = n;
	}
	(*head) = n;
}

// push n onto the back of the list
// if (*head) is 0, n becomes (*head)
// no effect if n == 0 or head == 0
void list_push_tail(list_node ** head, list_node * n)
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
		list_node * old_tail;
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
list_node * list_pop_head(list_node ** head)
{
	list_node * ret_val;

	if(head == 0)
		return 0;

	if((*head) == 0)
		return 0;

	ret_val = (*head);

	// if nothing else in the list, the list is empty
	if(ret_val->down == ret_val && ret_val->up == ret_val)
	{
		(*head) = 0;
	}
	else
	{
		(*head) = ret_val->down;
		(*head)->up = ret_val->up;
		(*head)->up->down = (*head);
	}
	ret_val->up = 0;
	ret_val->down = 0;
	return ret_val;
}

// pop the tail from the list
// if only one node in the list, (*head) will == 0
// no effect if head or (*head) == 0
// returns: the tail of the list or 0
list_node * list_pop_tail(list_node ** head)
{
	list_node * ret_val;

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

list_node * list_remove(list_node ** head, list_node * n)
{
	list_node * ret_val;

	if(head == 0)
		return 0;

	if((*head) == 0)
		return 0;

	if(n == 0)
		return 0;

	ret_val = n;

	if(n->down == n && n->up == n)
	{
		// last one in the list
		(*head) = 0;
	}
	else
	{
		n->down->up = n->up;
		n->up->down = n->down;
		if( (*head) == n)
		{
			(*head) = n->down;
		}

	}

	n->down = 0;
	n->up = 0;

	return ret_val;
}
