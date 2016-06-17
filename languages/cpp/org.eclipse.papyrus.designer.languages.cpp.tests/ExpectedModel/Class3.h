// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#ifndef CPPCODEGENTEST_CLASS3_H
#define CPPCODEGENTEST_CLASS3_H

/************************************************************
 Class3 class header
 ************************************************************/

#include "CppCodegenTest/Pkg_CppCodegenTest.h"

#include "AnsiCLibrary/Pkg_AnsiCLibrary.h"
#include "CppCodegenTest/Class2.h"

/************************************************************/
/**
 * 
 */
class Class3: public Class2 {
public:

	/**
	 * Inherited method overriding.
	 * @param a 
	 * @param b 
	 * @return  
	 */
	double virtualOp(double /*in*/a, double /*in*/b);

	/**
	 * Inherited virtual destructor
	 */
	~Class3();

	/**
	 * Default constructor
	 */
	Class3();

};
/************************************************************/
/* External declarations (package visibility)               */
/************************************************************/

/* Inline functions                                         */

/************************************************************
 End of Class3 class header
 ************************************************************/

#endif