// __c0bs32.c

// Copyright 2009, Alan Grover
// Implementation of channel< >(0, blocking) for a single type (TODO evaluate whether we care about having single type variants...) 
// The type must be 32 bits or less

#include <stdlib.h>
#include "__c0bs32.h"


void __c0bs32Type_init()
{
    struct __c0bs32Type_tag *type =
        &__c0bs32Type;
    if(0 == type->initialized)
    {
        // TODO can factor out everything but the toString operations if we want
        type->reader.poison = __c0bs32_OpPoison;
        type->reader.isPoisoned = __c0bs32_OpIsPoisoned;
        //type->reader.toString = __c0bs32_ChanR_OpToString;
        type->writer.poison = __c0bs32_OpPoison;
        type->writer.isPoisoned = __c0bs32_OpIsPoisoned;
        //type->writer.toString = __c0bs32_ChanW_OpToString;
        type->initialized = 1;
    }
}

void __c0bs32_OpPoison(__Channel32 *chan, scheduler *s)
{
	__c0bs32 * c = (__c0bs32*) chan;
	c->is_poisoned = 1;
}

int __c0bs32_OpIsPoisoned(__Channel32 *chan)
{
	__c0bs32 * c = (__c0bs32*) chan;
	return c->is_poisoned;
}

char * __c0bs32_ChanW32_OpToString(__Channel32 *chan)
{
	__c0bs32 * c = (__c0bs32*) chan;
	return c->chanWId;
}

char * __c0bs32_ChanR32_OpToString(__Channel32 *chan)
{
	__c0bs32 * c = (__c0bs32*) chan;
	return c->chanRId;
}

// there doesn't seem to be any general way to do channel read and write ops
// returns 0 if it wrote the value and synchronized; 1 if it blocked; and 2 if an exception was thrown
int __c0bs32_OpRead(  __Channel32 *chan, 
					Type *valueType,
                    uint32 *value, 
                    char **exception, 
                    proc *p, 
                    scheduler *s)
{
    __c0bs32 * c = (__c0bs32*) chan;
	int ret_val = 2;
	// TODO get the lock
	if(c->is_poisoned)
	{
		*exception = "channel poison";
	}
	else
	{
		if(c->valid_value)
		{
			*valueType = c->valueType;
			*value = c->value;
			c->valid_value = 0;
			if(c->writer_waiting != 0)
			{
				s->schedule(s, c->writer_waiting);
				c->writer_waiting = 0;
			}
			ret_val = 0;
		}
		else
		{
			c->reader_waiting = p;
			ret_val = 1;
		}
	}

	// TODO release the lock
	return ret_val;
}


// there doesn't seem to be any general way to do channel read and write ops
// returns 0 if it got a valid value; 1 if it blocked; and 2 if an exception was thrown
int __c0bs32_OpWrite(__Channel32 *chan, 
					 Type valueType,
					 uint32 value, 
					 void **exception, 
					 proc *p, 
					 scheduler *s)
{
    __c0bs32 * c = (__c0bs32*) chan;
	int ret_val = 2;
	// TODO get the lock
	if(c->is_poisoned)
	{
		// TODO make exceptions something other than just plain strings (& null terminated at that!)
		*exception = (void*) "channel poison";
	}
	else
	{
		// TODO right now the writer allways unconditionally writes; allow to withdraw if in ALT
		c->valueType = valueType;
		c->value = value;
		c->valid_value = 1;
		if(c->reader_waiting != 0)
		{
			s->schedule(s, c->reader_waiting);
			c->reader_waiting = 0;
			ret_val = 0;
		}
		else
		{
			c->writer_waiting = p;
			ret_val = 1;
		}
		
	}

	// TODO release the lock
	return ret_val;
}

// In this case the value has already been written, we're just checking to make sure it has been read
int __c0bs32_OpWriteHandshake(__Channel32 *chan,
							void **exception, 
							proc *p, 
							scheduler *s)
{
    __c0bs32 * c = (__c0bs32*) chan;
	int ret_val = 2;
	// TODO get the lock
	if(c->is_poisoned)
	{
		// TODO make exceptions something other than just plain strings (& null terminated at that!)
		*exception = (void*) "channel poison";
	}
	else
	{
		if(c->reader_waiting != 0)
		{
			s->schedule(s, c->reader_waiting);
			c->reader_waiting = 0;
			ret_val = 0;
		}
		else
		{
			if(c->valid_value == 0)
			{
				ret_val = 0;
			}
			else
			{
				c->writer_waiting = p;
				ret_val = 1;
			}
		}
	}

	// TODO release the lock
	return ret_val;
}

__c0bs32 * __c0bs32_ctor(__c0bs32 * channel, char * chanRId, char * chanWId)
{
    __c0bs32Type_init();
    channel->ends.readStore.read = __c0bs32_OpRead;
    channel->ends.readStore.channel = (__Channel32*) channel;
    channel->ends.readStore.type = &(__c0bs32Type.reader);
    channel->ends.writeStore.write = __c0bs32_OpWrite;
    channel->ends.writeStore.writeHandshake = __c0bs32_OpWriteHandshake;
    channel->ends.writeStore.channel = (__Channel32*) channel;
    channel->ends.writeStore.type = &(__c0bs32Type.writer);
    channel->ends.read = &(channel->ends.readStore);
    channel->ends.write = &(channel->ends.writeStore);
	channel->chanRId = chanRId;
	channel->chanWId = chanWId;
    channel->is_poisoned = 0;
    channel->valid_value = 0;
	channel->valueType = 0;
	channel->value = 0xfeedbeef;
    channel->reader_waiting = 0;
    channel->writer_waiting = 0;
    return channel;
}