#ifndef SAVE_STATE_H_
#define SAVE_STATE_H_

// TODO possibly should make these take uint32* instead of uint32

// saveState saves the current state (instruction pointer)
// state - integer to hold the state
// label - a label name that must be unique in the scope
// Rationale: can't use goto because it doesn't work with
// a saved state. setjmp/longjmp is overkill; we don't need
// a stack context and aren't doing a non-local jump.
// Duff's Device may work for simple cases, but becomes
// unweildy rapidly and overly complicates code generation.
#ifdef _MSC_VER
#define saveState(state, label)				            \
	do                                                  \
	{										            \
		unsigned int tstate = 0xdeadbeef;	            \
		__asm								            \
		{									            \
			__asm mov eax, offset label	                \
			__asm mov tstate, eax			            \
		}									            \
		state = tstate;						            \
label:                                                  \
        /* bug? MSVC Express 2008 freaks out   */       \
        /* if the last statement has a semi-colon */    \
		__asm xor eax, eax					            \
	} while (0)

#elif __GNUC__
#ifdef __i386__
#define saveState(state, label)                             \
    do                                                      \
    {                                                       \
        unsigned int tstate = 0xdeadbeef;                   \
        state = tstate;                                     \
        __asm__ __volatile__("movl $" #label ", %0; " #label ":"      \
            : "=m" (state)                                  \
        );                                                  \
    } while (0)        
#endif // __i386__

#else // unsupported compiler
#barf: unsupported compiler
#endif // compiler brand

// restoreState jumps to the state saved
// in saveState
#ifdef _MSC_VER
#define restoreState(state)					\
	do                                      \
    {									    \
		unsigned int tstate = state;		\
        if(tstate != 0)                     \
		    __asm jmp tstate      			\
	} while (0)								

#elif __GNUC__
#ifdef __i386__
// TODO The GNU version chucks a warning: indirect jmp without `*'
// but the program seems to work fine.  Not sure yet how to placate it
#define restoreState(state)                                         \
    do                                                              \
    {                                                               \
        unsigned int tstate = state;                                \
        __asm__ __volatile__("jmp *%0" : : "m" (state));             \
    } while (0)                
#endif // __i386__

#else // unsupported compiler
#barf: unsupported compiler
#endif // compiler brand

#endif // SAVE_STATE_H_

