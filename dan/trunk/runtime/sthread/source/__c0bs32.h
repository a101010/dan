// __c0bs32.c

// Copyright 2009, Alan Grover
// Implementation of channel< >(0, blocking) for a single type (TODO evaluate whether we care about having single type variants...) 
// The type must be 32 bits or less

#include <stdlib.h>
#include "__cType.h"

struct __c0bs32Type_tag
{
    // TODO may also need a lock
    int initialized;
    __ChanEndType reader;
    __ChanEndType writer;
};

void __c0bs32_OpPoison(__Channel32 *chan, scheduler *s);
int __c0bs32_OpIsPoisoned(__Channel32 *chan);
char * __c0bs32_ChanW32_OpToString(__Channel32 *chan);
char * __c0bs32_ChanR32_OpToString(__Channel32 *chan);

void __c0bs32Type_init();

// this is channel< >(0, blocking) for a single type
typedef struct __c0bs32_tag
{
    __Channel32		ends;
	char *			chanRId; 
	char *			chanWId; 
	int				is_poisoned; // 0 = false, 1 = true
	Type			valueType; // the type of the value; supports channels that can carry multiple types
	uint32			value; // pointer to storage for the value (which will be allocated directly after this structure)
	int32			valueSize; // size in bytes of the value
	uint32			valid_value; // 0 if invalid, else the number of the type for the value of c that is valid (a channel can carry multiple types) but in this case 1 is int32
								 // TODO can probably use valueType for this
	proc *			reader_waiting; // reader waiting to syncronize on the channel
	proc *			writer_waiting; // writer waiting to syncronize on the channel
} __c0bs32;

// there doesn't seem to be any general way to do channel read and write ops
// returns 0 if it wrote the value and synchronized; 1 if it blocked; and 2 if an exception was thrown
int __c0bs32_OpRead(__Channel32 *chan, 
					Type *valueType,
                    uint32 *value, 
                    char **exception, 
                    proc *p, 
                    scheduler *s);

// there doesn't seem to be any general way to do channel read and write ops
// returns 0 if it got a valid value; 1 if it blocked; and 2 if an exception was thrown
int __c0bs32_OpWrite(__Channel32 *chan,
					 Type valueType,
					 uint32 value, 
					 void **exception, 
					 proc *p, 
					 scheduler *s);

// In this case the value has already been written, we're just checking to make sure it has been read
int __c0bs32_OpWriteHandshake(__Channel32 *chan,
							void **exception, 
							proc *p, 
							scheduler *s);

__c0bs32 * __c0bs32_ctor(__c0bs32 * channel, char * chanRId, char * chanWId);