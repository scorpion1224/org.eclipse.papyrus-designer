// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#define CppCodegenTest_Class1_BODY

/************************************************************
 Class1 class body
 ************************************************************/

// include associated header file
#include "CppCodegenTest/Class1.h"

// Derived includes directives

namespace CppCodegenTest {

// static attributes (if any)

/**
 * Assignment operator
 * @param cSource 
 * @return this 
 */
Class1& Class1::operator =(const Class1& /*in*/cSource) {

	if (this == &cSource)
		return *this;

	iVal = cSource.iVal;

	return *this;
}

/**
 * Destructor
 */
Class1::~Class1() {

}

/**
 * Copy constructor
 * @param cSource 
 */
Class1::Class1(const Class1& /*in*/cSource) {

	iVal = cSource.iVal;
}

} // of namespace CppCodegenTest

/************************************************************
 End of Class1 class body
 ************************************************************/
