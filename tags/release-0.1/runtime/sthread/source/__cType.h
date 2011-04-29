#ifndef __CTYPE_H_
#define __CTYPE_H_

#include "__builtins.h"
#include "__Type.h"
#include "__proc.h"

typedef void (*__OpPoison)(void * chans, scheduler * s);
typedef int (*__OpIsPoisoned)(void * chans);

typedef struct __ChanEndType_tag
{
	__ObjectType parent;
    __OpPoison poison;
    __OpIsPoisoned isPoisoned;
} __ChanEndType;

struct __Channel_tag;

typedef int (*__OpChanRead)(struct __Channel_tag* channel, 
							Type valueType,
                            void * value,
							uint32 valueSize,   // for implementations that take multiple types of different sizes
												// TODO do we need variations?
                            char ** exception,	// TODO need real exceptions
                            proc * p, 
                            scheduler * s);

// this is chanr<T>
typedef struct __ChanR_tag
{
    __OpChanRead            read;
    struct __Channel_tag    *channel;
    __ChanEndType           *type;
} __ChanR;


typedef int (*__OpChanWrite)(struct Channel_tag* channel, 
								 Type *valueType,
                                 void * value,
								 int32 valueSize,
                                 void ** exception, 
                                 proc * p, 
                                 scheduler * s);

// TODO see if we can get rid of this one
typedef int (*__OpChanWriteHandshake)(struct Channel_tag* channel,
                                 void ** exception, 
                                 proc * p, 
                                 scheduler * s);

// this is chanw< >
typedef struct __ChanW_tag
{
    __OpChanWrite					write;
    __OpChanWriteHandshake			writeHandshake;
    struct Channel_tag				*channel;
    __ChanEndType					*type;
} __ChanW;

// This may be called Channel, but is is really storage for the 
// channel ends.  It is the first member of the final channel type,
// so that a pointer to one is a pointer to the other.  It is an
// abstract base type for channels.
typedef struct __Channel_tag 
{
    // the ends are mobile types; these pointers are their initial home
    __ChanR     *read;
    __ChanW     *write;
    // but this is where their storage is allocated
    __ChanR     readStore;
    __ChanW     writeStore;
} __Channel;

struct __Channel32_tag;

typedef int (*__OpChan32Read)(struct __Channel32_tag * channel, 
							Type *valueType,
                            uint32 *value,
                            char ** exception,	// TODO need real exceptions
                            proc * p, 
                            scheduler * s);

// this is chanr<T>
typedef struct __ChanR32_tag
{
    __OpChan32Read            read;
    struct __Channel32_tag    *channel;
    __ChanEndType			  *type;
} __ChanR32;


typedef int (*__OpChan32Write)(struct __Channel32_tag* channel, 
								 Type *valueType,
                                 uint32 value,
                                 void ** exception, 
                                 proc * p, 
                                 scheduler * s);

// TODO see if we can get rid of this one
typedef int (*__OpChan32WriteHandshake)(struct __Channel32_tag* channel,
                                 void ** exception, 
                                 proc * p, 
                                 scheduler * s);

// this is chanw< >
typedef struct __ChanW32_tag
{
    __OpChan32Write					write;
    __OpChan32WriteHandshake		writeHandshake;
    struct __Channel32_tag			*channel;
    __ChanEndType					*type;
} __ChanW32;

// This may be called Channel, but is is really storage for the 
// channel ends.  It is the first member of the final channel type,
// so that a pointer to one is a pointer to the other.  It is an
// abstract base type for channels.
typedef struct __Channel32_tag 
{
    // the ends are mobile types; these pointers are their initial home
    __ChanR32     *read;
    __ChanW32     *write;
    // but this is where their storage is allocated
    __ChanR32     readStore;
    __ChanW32     writeStore;
} __Channel32;

#endif //__CTYPE_H_
