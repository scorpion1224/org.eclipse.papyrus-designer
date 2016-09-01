// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#define ClientServerZMQ_components_Client_CClient_impl_BODY

/************************************************************
 CClient_impl class body
 ************************************************************/

// include associated header file
#include <ClientServerZMQ/components/Client/CClient_impl.h>

// Derived includes directives
#include <sysinterfaces/IStart.h>

namespace components {
namespace Client {

// static attributes (if any)

/**
 * 
 * 
 */
void CClient_impl::run() {
	cout << "call via port q: add (2, 3);" << endl;
	cout << "result: " << q->add(2, 3) << endl;
}

} // of namespace Client
} // of namespace components

/************************************************************
 End of CClient_impl class body
 ************************************************************/