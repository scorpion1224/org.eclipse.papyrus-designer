// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#ifndef PRODUCERCONSUMER_COMPONENTS_SYSTEM_SYSTEMPULL_IMPL_H
#define PRODUCERCONSUMER_COMPONENTS_SYSTEM_SYSTEMPULL_IMPL_H

/************************************************************
 SystemPull_impl class header
 ************************************************************/

#include "ProducerConsumer/components/System/Pkg_System.h"

#include "ProducerConsumer/DataExchange_PubData/FIFO/FIFO_impl.h"
#include "ProducerConsumer/components/Producer/Producer_impl.h"
#include "ProducerConsumer/components/PullConsumer/PullConsumer_impl.h"
#include "componentlib/ContainerServices/Thread.h"

namespace ProducerConsumer {
namespace components {
namespace System {

/************************************************************/
/**
 * Pull consumer is executed by its own thread. This is specified via a container service (runStartThread)Since producer and consumer call operations (the latter to actively retrieve data), the two ports would be incompatible without using a specific interaction component, in this case. a FIFO.
 * Use the context menu on a selected connector to specify the interaction component.
 */
class SystemPull_impl {
public:
	/**
	 * 
	 */
	::ProducerConsumer::components::PullConsumer::PullConsumer_impl con;
	/**
	 * 
	 */
	::componentlib::ContainerServices::Thread pullConThread;
	/**
	 * 
	 */
	::ProducerConsumer::components::Producer::Producer_impl prod;
	/**
	 * 
	 */
	::componentlib::ContainerServices::Thread main;
	/**
	 * 
	 */
	::ProducerConsumer::DataExchange_PubData::FIFO::FIFO_impl fifoconnector;

	/**
	 * 
	 */
	void createConnections();

};
/************************************************************/
/* External declarations (package visibility)               */
/************************************************************/

/* Inline functions                                         */

} // of namespace System
} // of namespace components
} // of namespace ProducerConsumer

/************************************************************
 End of SystemPull_impl class header
 ************************************************************/

#endif