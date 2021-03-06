// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#define ComplexSM_classes_ComplexSM_BODY

/************************************************************
 ComplexSM class body
 ************************************************************/

// include associated header file
#include "ComplexSM/classes/ComplexSM.h"

// Derived includes directives
#include "ComplexSM/classes/Interfaces/ICompute.h"
#include "statemachine/Event_t.h"
#include "statemachine/Pkg_statemachine.h"
#include "sysinterfaces/IStart.h"

// Include from Include declaration (body)
#include <iostream>
using namespace std;

#include <unistd.h>

// End of Include declaration (body)

namespace ComplexSM {
namespace classes {

// static attributes (if any)

/**
 * 
 */
void ComplexSM::dispatchEvent() {
	bool popDeferred = false;
	while (true) {
		//run-to-completion: need to have a mutex here
		currentEvent = eventQueue.pop(popDeferred);
		dispatchFlag = true;
		if (currentEvent != NULL) {
			COMPLEXSM_GET_CONTROL
			switch (currentEvent->eventID) {
			case TE_VALUE_50_UNIT_MS__ID:
				processTE_value_50_unit_ms_();
				break;
			case TE_VALUE_500_UNIT_MS__ID:
				processTE_value_500_unit_ms_();
				break;
			case TE_VALUE_25_UNIT_MS__ID:
				processTE_value_25_unit_ms_();
				break;
			case COMPLETIONEVENT_ID:
				processCompletionEvent();
				break;
			}
			if (systemState == statemachine::EVENT_DEFERRED) {
				eventQueue.saveDeferred(*currentEvent);
			}
			popDeferred = (systemState != statemachine::EVENT_DEFERRED);
			systemState = statemachine::IDLE;
			COMPLEXSM_RELEASE_CONTROL
		}
	}
}

/**
 * 
 * @param a 
 * @param b 
 * @return res 
 */
long ComplexSM::add(long /*in*/a, long /*in*/b) {
	this->processCE_CServer_impl_add(a, b);
	// original method code
	cout << "a=" << a << " b=" << b << " a+b=" << a + b << endl;
	return a + b;
}

/**
 * 
 * @param a 
 * @param b 
 * @return res 
 */
long ComplexSM::mult(long /*in*/a, long /*in*/b) {
	cout << "a=" << a << " b=" << b << " a*b=" << a * b << endl;
	return a * b;
}

/**
 * 
 */
void ComplexSM::run() {
	cout << "call add (2, 3);" << endl;
	cout << "result: " << add(2, 3) << endl;
	cout << "sleeping for 15 seconds (statemachine remains active);" << endl;
	sleep(15);
}

/**
 * 
 * @param enter_mode 
 */
void ComplexSM::SMSimple_Region0_Enter(char /*in*/enter_mode) {
	switch (enter_mode) {
	case SMSIMPLE_REGION0_DEFAULT:
		activeStateID = STATE0_ID;

		setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_50_UNIT_MS__ID),
				statemachine::TF_TIME_EVENT, true);
		setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_25_UNIT_MS__ID),
				statemachine::TF_TIME_EVENT, true);

		//TODO: set systemState to EVENT_CONSUMED
		break;
	case SMSIMPLE_REGION0_STATE1:
		activeStateID = STATE1_ID;
		//starting the counters for time events
		setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
				statemachine::TF_TIME_EVENT, true);
		//TODO: set systemState to EVENT_CONSUMED
		break;
	}
}

/**
 * 
 * @param enter_mode 
 */
void ComplexSM::State3_Region1_Enter(char /*in*/enter_mode) {
	switch (enter_mode) {
	case STATE3_REGION1_SHALLOWHISTORY1:
		unsigned int loc_ActiveId;
		if (states[STATE3_ID].previousStates[0] != STATE_MAX) {
			loc_ActiveId = states[STATE3_ID].previousStates[0];
		} else {
			//for the first time, the history should be initialized
			cout << "From Shallow history to State 3_1" << endl;
			loc_ActiveId = STATE3_1_ID;
		}
		states[STATE3_ID].actives[0] = loc_ActiveId;
		(this->*states[loc_ActiveId].entry)();
		if (STATE3_1_ID == loc_ActiveId) {
			setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
					statemachine::TF_TIME_EVENT, true);
		} else if (STATE3_2_ID == loc_ActiveId) {
			setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
					statemachine::TF_TIME_EVENT, true);
		}
		setFlag(loc_ActiveId, statemachine::TF_DO_ACTIVITY, true);
		//TODO: set systemState to EVENT_CONSUMED
		break;
	}
}

/**
 * 
 */
void ComplexSM::State3_Region1_Exit() {
	//exiting region Region1
	if (states[STATE3_ID].actives[0] != STATE_MAX) {
		//signal to exit the doActivity of sub-state of State3
		setFlag(states[STATE3_ID].actives[0], statemachine::TF_DO_ACTIVITY,
				false);
		if (STATE3_1_ID == states[STATE3_ID].actives[0]) {
			setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
					statemachine::TF_TIME_EVENT, false);
		} else if (STATE3_2_ID == states[STATE3_ID].actives[0]) {
			setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
					statemachine::TF_TIME_EVENT, false);
		}
		//exit action of sub-state of State3
		(this->*states[states[STATE3_ID].actives[0]].exit)();
		//save history region Region1 of state State3
		states[STATE3_ID].previousStates[0] = states[STATE3_ID].actives[0];
		//set active sub-state of State3 to STATE_MAX meaning NULL
		states[STATE3_ID].actives[0] = STATE_MAX;
	}
}

/**
 * 
 */
ComplexSM::ComplexSM() {
	startBehavior();
}

/**
 * 
 */
void ComplexSM::startBehavior() {
	systemState = statemachine::IDLE;
	doActivityTable[STATE0_ID] = states[STATE0_ID].doActivity;
	doActivityTable[STATE1_ID] = states[STATE1_ID].doActivity;
	states[STATE2_ID].entry = &ComplexSM::State2_entry;
	doActivityTable[STATE2_ID] = states[STATE2_ID].doActivity;
	doActivityTable[STATE3_ID] = states[STATE3_ID].doActivity;
	doActivityTable[STATE3_1_ID] = states[STATE3_1_ID].doActivity;
	doActivityTable[STATE3_2_ID] = states[STATE3_2_ID].doActivity;

	// initialize all threads, the threads wait until the associated flag is set
	for (int i = 0; i < (int) STATE_MAX; i++) {
		if (states[i].doActivity != &ComplexSM::doActivity_dft) {
			threadStructs[i].id = i;
			threadStructs[i].ptr = this;
			threadStructs[i].func_type = statemachine::TF_DO_ACTIVITY;
			mutexes[i] = PTHREAD_MUTEX_INITIALIZER;
			conds[i] = PTHREAD_COND_INITIALIZER;
			pthread_create(&threads[i], NULL, &ComplexSM::thread_func_wrapper,
					&threadStructs[i]);
		}
	}

	timeEventThreadStructs[COMPLEXSM_TE_INDEX(TE_VALUE_50_UNIT_MS__ID)].duration =
			50;
	timeEventThreadStructs[COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID)].duration =
			500;
	timeEventThreadStructs[COMPLEXSM_TE_INDEX(TE_VALUE_25_UNIT_MS__ID)].duration =
			25;
	for (int i = COMPLEXSM_TIME_EVENT_LOWER_BOUND; i < 3; i++) {
		timeEventThreadStructs[COMPLEXSM_TE_INDEX(i)].id = i;
		timeEventThreadStructs[COMPLEXSM_TE_INDEX(i)].ptr = this;
		timeEventThreadStructs[COMPLEXSM_TE_INDEX(i)].func_type =
				statemachine::TF_TIME_EVENT;
		timeEventMutexes[COMPLEXSM_TE_INDEX(i)] = PTHREAD_MUTEX_INITIALIZER;
		timeEventConds[COMPLEXSM_TE_INDEX(i)] = PTHREAD_COND_INITIALIZER;
		pthread_create(&timeEventThreads[COMPLEXSM_TE_INDEX(i)], NULL,
				&ComplexSM::thread_func_wrapper,
				&timeEventThreadStructs[COMPLEXSM_TE_INDEX(i)]);
		while (timeEventFlags[COMPLEXSM_TE_INDEX(i)]) {
		}
	}

	runToCompletionMutex = PTHREAD_MUTEX_INITIALIZER;
	runToCompletionCond = PTHREAD_COND_INITIALIZER;

	dispatchStruct = statemachine::StructForThread_t(this, 0, 0,
			statemachine::TF_STATE_MACHINE_TYPE, 0);
	ComplexSM_THREAD_CREATE(dispatchThread, dispatchStruct)
	while (!dispatchFlag) {
	}

	//initialze root active state
	//execute initial effect
	SMSimple_Region0_Enter (SMSIMPLE_REGION0_DEFAULT);
}

/**
 * 
 */
void ComplexSM::processTE_value_50_unit_ms_() {
	systemState = statemachine::EVENT_PROCESSING;
	if (systemState == statemachine::EVENT_PROCESSING) {
		switch (activeStateID) {
		case STATE0_ID:
			//from State0 to State1
			if (true) {
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_50_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, false);
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_25_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, false);
				activeStateID = STATE1_ID;
				//starting the counters for time events
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, true);
				systemState = statemachine::EVENT_CONSUMED;
			}
			break;
		default:
			//do nothing
			break;
		}
	}
}

/**
 * 
 */
void ComplexSM::processTE_value_500_unit_ms_() {
	systemState = statemachine::EVENT_PROCESSING;
	if (states[STATE3_ID].actives[0] == STATE3_1_ID) {
		//from State3_1 to State3_2
		if (true) {
			setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
					statemachine::TF_TIME_EVENT, false);
			cout << "From State 3_1 to State 3_2" << endl;
			states[STATE3_ID].actives[0] = STATE3_2_ID;
			//starting the counters for time events
			setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
					statemachine::TF_TIME_EVENT, true);
			systemState = statemachine::EVENT_CONSUMED;
		}
	} else if (states[STATE3_ID].actives[0] == STATE3_2_ID) {
		//from State3_2 to State1
		if (true) {
			State3_Region1_Exit();
			cout << "From State 5 to State 1" << endl;
			activeStateID = STATE1_ID;
			//starting the counters for time events
			setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
					statemachine::TF_TIME_EVENT, true);
			systemState = statemachine::EVENT_CONSUMED;
		}
	}
	if (systemState == statemachine::EVENT_PROCESSING) {
		switch (activeStateID) {
		case STATE1_ID:
			//from State1 to State0
			if (true) {
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, false);
				mult(3, 5);
				std::cout << "From State1 to State0 \n";
				activeStateID = STATE0_ID;
				//starting the counters for time events
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_50_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, true);
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_25_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, true);
				systemState = statemachine::EVENT_CONSUMED;
			}
			break;
		case STATE2_ID:
			//from State2 to Junction1
			if (true) {
				Junction1 = 0;
				if (x == 5) {
					Junction1 = 1;
				}
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, false);
				x = 6;
				std::cout << "From State 2 to Junction1\n";
				if (Junction1 == 0) {
					activeStateID = STATE0_ID;
					//starting the counters for time events
					setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_50_UNIT_MS__ID),
							statemachine::TF_TIME_EVENT, true);
					setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_25_UNIT_MS__ID),
							statemachine::TF_TIME_EVENT, true);
				} else if (Junction1 == 1) {
					cout << "From Junction1 to Shallow history" << endl;
					activeStateID = STATE3_ID;
					//starting the counters for time events
					State3_Region1_Enter (STATE3_REGION1_SHALLOWHISTORY1);
				}
				systemState = statemachine::EVENT_CONSUMED;
			}
			break;
		default:
			//do nothing
			break;
		}
	}
}

/**
 * 
 * @param a 
 * @param b 
 */
void ComplexSM::processCE_CServer_impl_add(long /*in*/a, long /*in*/b) {
	COMPLEXSM_GET_CONTROL systemState = statemachine::EVENT_PROCESSING;
	if (systemState == statemachine::EVENT_PROCESSING) {
		switch (activeStateID) {
		case STATE0_ID:
			//from State0 to State1
			if (true) {
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_50_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, false);
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_25_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, false);
				activeStateID = STATE1_ID;
				//starting the counters for time events
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, true);
				systemState = statemachine::EVENT_CONSUMED;
			}
			break;
		default:
			//do nothing
			break;
		}
	}
	COMPLEXSM_RELEASE_CONTROL
}

/**
 * 
 */
void ComplexSM::processTE_value_25_unit_ms_() {
	systemState = statemachine::EVENT_PROCESSING;
	if (systemState == statemachine::EVENT_PROCESSING) {
		switch (activeStateID) {
		case STATE0_ID:
			//from State0 to State2
			if (true) {
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_50_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, false);
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_25_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, false);
				cout << "From State 0 to State 2" << endl;
				activeStateID = STATE2_ID;
				(this->*states[STATE2_ID].entry)();
				//starting the counters for time events
				setFlag(COMPLEXSM_TE_INDEX(TE_VALUE_500_UNIT_MS__ID),
						statemachine::TF_TIME_EVENT, true);
				systemState = statemachine::EVENT_CONSUMED;
			}
			break;
		default:
			//do nothing
			break;
		}
	}
}

/**
 * 
 */
void ComplexSM::processCompletionEvent() {
	systemState = statemachine::EVENT_PROCESSING;
}

/**
 * 
 */
void ComplexSM::State2_entry() {
	x = 5;
}

/**
 * 
 * @param data 
 * @return ret 
 */
void* ComplexSM::thread_func_wrapper(void* /*in*/data) {
	statemachine::StructForThread_t* cptr =
			(statemachine::StructForThread_t*) data;
	ComplexSM* ptr = (ComplexSM*) cptr->ptr;
	switch (cptr->func_type) {
	case statemachine::TF_TIME_EVENT:
		ptr->listenTimeEvent(COMPLEXSM_TE_INDEX(cptr->id), cptr->duration);
		break;
	case statemachine::TF_STATE_MACHINE_TYPE:
		ptr->dispatchEvent();
		break;
	}
	return NULL;
}

/**
 * 
 * @param id 
 */
void ComplexSM::doCallActivity(int /*in*/id) {
	flags[id] = false;
	while (true) {
		pthread_mutex_lock (&mutexes[id]);
		while (!flags[id]) {
			pthread_cond_wait(&conds[id], &mutexes[id]);
		}
		(this->*doActivityTable[id])();
		bool commitEvent = false;
		if (flags[id]) {
			commitEvent = true;
			flags[id] = false;
		}
		pthread_cond_signal (&conds[id]);
		pthread_mutex_unlock(&mutexes[id]);
	}
}

/**
 * 
 * @param id 
 * @param func_type 
 * @param value 
 */
void ComplexSM::setFlag(int /*in*/id, char /*in*/func_type, bool /*in*/value) {
	//value = true => start activity
	//value = false => stop activity
	if (func_type == statemachine::TF_TIME_EVENT) {
		pthread_mutex_lock (&timeEventMutexes[COMPLEXSM_TE_INDEX(id)]);
		timeEventFlags[COMPLEXSM_TE_INDEX(id)] = value;
		pthread_cond_signal (&timeEventConds[COMPLEXSM_TE_INDEX(id)]);
		pthread_mutex_unlock(&timeEventMutexes[COMPLEXSM_TE_INDEX(id)]);
		return;
	}
	if (func_type == statemachine::TF_DO_ACTIVITY) {
	}
}

/**
 * 
 * @param id 
 * @param duration 
 */
void ComplexSM::listenTimeEvent(int /*in*/id, int /*in*/duration) {
	struct timeval tv;
	struct timespec ts;
	int timedWaitResult;
	while (true) {
		pthread_mutex_lock (&timeEventMutexes[id]);
		while (!timeEventFlags[id]) {
			pthread_cond_wait(&timeEventConds[id], &timeEventMutexes[id]);
		}

		gettimeofday(&tv, NULL);
		ts.tv_sec = time(NULL) + duration / 1000;
		ts.tv_nsec = tv.tv_usec * 1000 + 1000 * 1000 * (duration % 1000);
		ts.tv_sec += ts.tv_nsec / (1000 * 1000 * 1000);
		ts.tv_nsec %= (1000 * 1000 * 1000);

		timedWaitResult = pthread_cond_timedwait(&timeEventConds[id],
				&timeEventMutexes[id], &ts);

		bool commitEvent = false;
		if (timedWaitResult != 0) {
			//timeout
			commitEvent = true;
		}
		timeEventFlags[id] = false;
		pthread_cond_signal (&timeEventConds[id]);
		pthread_mutex_unlock(&timeEventMutexes[id]);
		if (commitEvent) {
			//the state does not change, push time event to the queue
			eventQueue.push(statemachine::PRIORITY_2, NULL, id,
					statemachine::TIME_EVENT, id);
		}
	}
}

/**
 * 
 */
void ComplexSM::entry_dft() {
}

/**
 * 
 */
void ComplexSM::exit_dft() {
}

/**
 * 
 */
void ComplexSM::doActivity_dft() {
}

} // of namespace classes
} // of namespace ComplexSM

/************************************************************
 End of ComplexSM class body
 ************************************************************/
