package org.eclipse.papyrus.designer.components.modellibs.core.statemachine

class SMCodeGeneratorConstants {
	public static String PROCESSFROM = "processFrom"
	public static String PROCESSEVENT = "processEvent"
	public static String ACTIVE_SUB_STATE = "activeSubState"
	public static String PREVIOUS_SUB_STATE = "previouSubState"
	public static String RESTORE_DEEP_HISTORY_FUNC_NAME = "restoreDeepHistory"
	public static String RESTORE_SHALLOW_HISTORY_FUNC_NAME = "restoreShallowHistory"
	public static String ON_ENTRY_ACTION_FUNC_NAME = "onEntryAction"
	public static String ON_EXIT_ACTION_FUNC_NAME = "onExitAction"
	public static String SET_INIDEFAULT_STATE_FUNC_NAME = "setInitDefaultState"
	public static String SET_SHALLOW_HISTORY_DEFAULT_STATE_FUNC_NAME = "setShallowHistoryDefaultState"
	public static String SET_DEEP_HISTORY_DEFAULT_STATE_FUNC_NAME = "setDeepHistoryDefaultState"
	public static String CONTEXT = "context"
	public static String ANCESTOR = "ancestor"
	public static String ASSIGNMENT_OPERATOR = "operator="
	public static String ORTHOGONAL_STATE_CLASS_NAME = "OrthogonalState"
	public static String ENTRY_COMPOSITE_ON_HISTORY = "entryCompositeOnHistory"
	public static String ENTRY_COMPOSITE_ON_PARTICULAR_SUBSTATE = "entryCompositeOnSubState"
	public static String TIMED_STATE_INTERFACE_NAME = "TimedState"
	public static String TIMER_CLASS_NAME = "Timer"
	public static String TIMEOUT_NAME = "timeout"
	public static String TIMEOUT_MILISECOND_NAME = "milisec"
	public static String TIMED_STATE_ATTR_NAME = "timedState"
	public static String START_THREAD_FUNC_NAME = "startThread"
	public static String WAIT_THREAD_FUNC_NAME = "waitThread"
	public static String START_INTERNAL_THREAD_NAME = "startInternalThread"
	public static String INTERNAL_THREAD_ENTRY_NAME = "startInternalThreadEntry"
	public static String PTHREAD_ATTR_NAME = "mThread"
	public static String EVENT_POOL_NAME = "eventPool"
	public static String TIMER_POOL_NAME = "timerPool"
	public static String MAX_NUMER_EVENT = "50"
	public static String MAX_NUMBER_TIME_EVENT = "50"
	public static String MAX_NUMBER_EVENT_NAME = "MAX_EVENT"
	public static String MAX_NUMBER_TIME_EVENT_NAME = "MAX_TIME_EVENT"
	public static String NUMBER_EVENT_NAME = "numberEvent"
	public static String NUMBER_TIME_EVENT_NAME = "numberTimeEvent"
	public static String EVENT_READ_POS_NAME = "eventReadPos"
	public static String TIME_EVENT_READ_POS_NAME = "timeEventReadPos"
	public static String EVENT_WRITE_POS_NAME = "eventWritePos"
	public static String TIME_EVENT_WRITE_POS_NAME = "timeEventWritePos"
	public static String POP_EVENT = "popEvent"
	public static String POP_TIME_EVENT = "popTimeEvent"
	public static String PROCESSTIMEEVENT = "processTimeEvent"
	public static String SIGNAL_EVENT_NAME = "signal_name"
	public static String CALL_OPERATION_NAME = "operation_name"
	public static String ENTRY_COMPOSITE_ON_POINT = "entryOn_"
	public static String EXIT_COMPOSITE_ON_POINT = "exitOn_"
	public static String TRANSITION_ON_EXP = "transitionOn_"
	public static String ENTRY_ON_FORK = "entryOn_"
	public static String DO_ACTIVITY_NAME = "doActivity"
	public static String ON_ACTIVITY_THREAD = "onActivityThread"
	public static String TRIGGERLESS_TRANSITION = "triggerlessTransition"
	public static String ENTRY_NAME = "entry"
	public static String EXIT_NAME = "exit"
	public static String STATE_STRUCT_NAME = "State_t"

	public static String ON_ACTIVITY_ACTION_NAME = "onActivityAction"
	public static String ON_EXIT_ACTIVITY = "onExitActivity"
	public static String cpp11ThreadAttributeName = "theThread"
	public static String STATE_ARRAY_ATTRIBUTE = "states"
	public static String ACTIVE_ROOT_STATE_ID = "activeStateID"
	public static String STATE_MAX = "STATE_MAX"
	public static String PREVIOUS_STATES = "previousStates"
	public static String ACTIVE_SUB_STATES = "actives"
	public static String COMPLETION_EVENT = "CompletionEvent"
	public static String THREADS = "threads"
	public static String THREADS_TIME_EVENT = "timeEventThreads"
	public static String FLAGS_ACTIVITY = "flags"
	public static String FLAGS_TIME_EVENT = "timeEventFlags"
	
	public static String MUTEXES = "mutexes"
	public static String MUTEXES_TIME_EVENT = "timeEventMutexes"
	public static String CONDITIONS = "conds"
	public static String CONDITIONS_TIME_EVENT = "timeEventConds"
	public static String STRUCT_FOR_THREAD = "StructForThread_t"
	public static String THREAD_STRUCTS = "threadStructs"
	public static String THREAD_STRUCTS_FOR_TIMEEVENT = "timeEventThreadStructs"
	public static String THREAD_FUNC_WRAPPER = "thread_func_wrapper"
	public static String FPT_POINTER_FOR_TABLE = "FptPointer"
	public static String DO_ACTIVITY_TABLE = "doActivityTable"
	public static String TIME_EVENT_TABLE = "timeEventTable"
	public static String REGION_TABLE = "regionTable"
	public static String PARALLEL_TRANSITION_TABLE = "transitionTable"
	public static String REGION_TABLE_EXIT = "regionExitTable"
	public static String REGION_FUNCTION_PTR = "RegionFunctionPtr_t"
	public static String CHANGE_EVENT_TABLE = "changeEventTable"
	public static String DO_CALL_ACTIVITY = "doCallActivity"
	public static String SET_FLAG = "setFlag"
	public static String STATE_ID_ENUM = "StateIDEnum"
	public static String FORK_NAME = "fork"
	public static String JOIN_NAME = "join"
	public static String EVENT_ID = "EventId_t"
	public static String THREAD_FUNC_TIMEEVENT_TYPE = "statemachine::TF_TIME_EVENT"
	public static String THREAD_FUNC_CHANGEEVENT_TYPE = "statemachine::TF_CHANGE_EVENT"
	public static String THREAD_FUNC_DOACTIVITY_TYPE = "statemachine::TF_DO_ACTIVITY"
	public static String THREAD_FUNC_ENTER_REGION_TYPE = "statemachine::TF_ENTER_REGION"
	public static String THREAD_FUNC_EXIT_REGION_TYPE = "statemachine::TF_EXIT_REGION"
	public static String THREAD_FUNC_TRANSITION_TYPE = "statemachine::TF_TRANSITION"
	
	public static String TIME_EVENT_LISTEN_FUNCTION = "listenTimeEvent"
	public static String SYSTEM_STATE_ATTR = "systemState"
}