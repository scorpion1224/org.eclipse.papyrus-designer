// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#define ProducerConsumer_components_System_SystemPush_impl_BODY

/************************************************************
 SystemPush_impl class body
 ************************************************************/

// include associated header file
#include "ProducerConsumer/components/System/SystemPush_impl.h"

// Derived includes directives

namespace ProducerConsumer {
namespace components {
namespace System {

// static attributes (if any)

/**
 * 
 */
void SystemPush_impl::createConnections() {
	// realization of connector <qpconnector>
	prod.connect_q(con.get_p());

}

} // of namespace System
} // of namespace components
} // of namespace ProducerConsumer

/************************************************************
 End of SystemPush_impl class body
 ************************************************************/