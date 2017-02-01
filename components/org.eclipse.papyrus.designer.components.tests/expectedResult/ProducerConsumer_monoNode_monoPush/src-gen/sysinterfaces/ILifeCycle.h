// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#ifndef SYSINTERFACES_ILIFECYCLE_H
#define SYSINTERFACES_ILIFECYCLE_H

/************************************************************
 ILifeCycle class header
 ************************************************************/

#include "sysinterfaces/Pkg_sysinterfaces.h"

namespace sysinterfaces {

/************************************************************/
/**
 * 
 */
class ILifeCycle {
public:

	/**
	 * 
	 */
	virtual void activate() = 0;

	/**
	 * 
	 */
	virtual void deactivate() = 0;

	/**
	 * 
	 */
	virtual void configuration_complete() = 0;

};
/************************************************************/
/* External declarations (package visibility)               */
/************************************************************/

/* Inline functions                                         */

} // of namespace sysinterfaces

/************************************************************
 End of ILifeCycle class header
 ************************************************************/

#endif
