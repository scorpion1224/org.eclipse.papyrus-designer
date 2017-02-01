// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#ifndef COMPLEXSM_CLASSES_INTERFACES_ICOMPUTE_H
#define COMPLEXSM_CLASSES_INTERFACES_ICOMPUTE_H

/************************************************************
 ICompute class header
 ************************************************************/

#include "ComplexSM/classes/Interfaces/Pkg_Interfaces.h"

#include "AnsiCLibrary/Pkg_AnsiCLibrary.h"

namespace ComplexSM {
namespace classes {
namespace Interfaces {

/************************************************************/
/**
 * 
 */
class ICompute {
public:

	/**
	 * 
	 * @param a 
	 * @param b 
	 * @return res 
	 */
	virtual long add(long /*in*/a, long /*in*/b) = 0;

	/**
	 * 
	 * @param a 
	 * @param b 
	 * @return res 
	 */
	virtual long mult(long /*in*/a, long /*in*/b) = 0;

};
/************************************************************/
/* External declarations (package visibility)               */
/************************************************************/

/* Inline functions                                         */

} // of namespace Interfaces
} // of namespace classes
} // of namespace ComplexSM

/************************************************************
 End of ICompute class header
 ************************************************************/

#endif
