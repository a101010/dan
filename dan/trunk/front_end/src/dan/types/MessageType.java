// Message types are invariant; they can only be instantiated
// That being the case, I'd like to have an easy way to clone
// one and modify only a few fields.

// Message types have no methods; only fields.  Fields are always
// value types, because message types are self contained and an
// array of bytes of the proper length can always be cast to a 
// message type.

// TODO in the future I'd like to be able to support message types
// that are not a fixed length, somewhat in the style of Jaus 
// message types.  In other words, an instance is a fixed length,
// but a type can have instances of varying lengths.