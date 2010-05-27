#ifndef __SPINLOCK_H_
#define __SPINLOCK_H_


// spin locks are chatty no-ops right now  TODO use a flag for test and set rather than a string
//#define lock(spinlock) printf("lock %s\n", spinlock);
//#define unlock(spinlock) printf("unlock %s\n", spinlock);
#define lock(spinlock)
#define unlock(spinlock)

#endif //__SPINLOCK_H_
