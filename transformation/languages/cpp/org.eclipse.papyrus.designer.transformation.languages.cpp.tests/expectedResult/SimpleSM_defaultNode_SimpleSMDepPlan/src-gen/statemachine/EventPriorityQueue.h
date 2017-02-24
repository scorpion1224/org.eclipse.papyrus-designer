// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#ifndef STATEMACHINE_EVENTPRIORITYQUEUE_H
#define STATEMACHINE_EVENTPRIORITYQUEUE_H

/************************************************************
 EventPriorityQueue class header
 ************************************************************/

#include "statemachine/Pkg_statemachine.h"

#include "AnsiCLibrary/Pkg_AnsiCLibrary.h"
#include "pthread.h"
#include "statemachine/Event_t.h"

// Include from Include stereotype (header)
#include "pthread.h"
#include "stdio.h"
#include "string.h"

#define defSize (10)
// End of Include stereotype (header)

namespace statemachine {

/************************************************************/
/**
 * 
 */
class EventPriorityQueue {
public:
	/**
	 * 
	 */
	unsigned int size;
	/**
	 * 
	 */
	unsigned int numberOfElements;
	/**
	 * 
	 */
	unsigned int readPos;
	/**
	 * 
	 */
	unsigned int writePos;
	/**
	 * 
	 */
	pthread_mutex_t mutex;
	/**
	 * 
	 */
	pthread_cond_t cond;
	/**
	 * 
	 */
	bool isLock;
	/**
	 * 
	 */
	Event_t completionEvents[4];
	/**
	 * 
	 */
	unsigned int compSize;
	/**
	 * 
	 */
	unsigned int compNumbers;
	/**
	 * 
	 */
	unsigned int compReadPos;
	/**
	 * 
	 */
	unsigned int compWritePos;
	/**
	 * 
	 */
	Event_t deferreds[defSize];
	/**
	 * 
	 */
	unsigned int numberOfDeferreds;
	/**
	 * 
	 */
	unsigned int readDef;
	/**
	 * 
	 */
	unsigned int writeDef;

	/**
	 * 
	 */
	EventPriorityQueue();

	/**
	 * 
	 * @param priority 
	 * @param data 
	 * @param eventID 
	 * @param eventType 
	 * @param associatedState 
	 * @param dataSize 
	 */
	void push(EventPriority_t /*in*/priority, void* /*in*/data,
			unsigned int /*in*/eventID, EventType_t /*in*/eventType,
			unsigned int /*in*/associatedState, int /*in*/dataSize = 0);

	/**
	 * 
	 * @return ret 
	 * @param popDeferred 
	 */
	Event_t* pop(bool /*in*/popDeferred = false);

	/**
	 * 
	 * @param defe 
	 */
	void saveDeferred(Event_t& /*in*/defe);

private:
	/**
	 * 
	 */
	Event_t data[10];

	/**
	 * 
	 */
	void heapify();
};
/************************************************************/
/* External declarations (package visibility)               */
/************************************************************/

/* Inline functions                                         */

} // of namespace statemachine

/************************************************************
 End of EventPriorityQueue class header
 ************************************************************/

#endif