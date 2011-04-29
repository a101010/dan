#ifndef __TYPE_H_
#define __TYPE_H_

// TODO this will return a Danger string, rather than a C 
// null-terminated string
typedef void* Type;

typedef char* (*__OpToString)(void * object);

typedef struct __ObjectType_tag
{
	__OpToString toString;
    // TODO add other Object type methods
} __ObjectType;

#endif //__TYPE_H_
