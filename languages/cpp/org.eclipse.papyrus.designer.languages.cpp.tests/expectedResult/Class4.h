// --------------------------------------------------------
// Code generated by Papyrus C++
// --------------------------------------------------------

#ifndef CPPCODEGENTEST_CLASS4_H
#define CPPCODEGENTEST_CLASS4_H

/************************************************************
 Class4 class header
 ************************************************************/

#include "CppCodegenTest/Pkg_CppCodegenTest.h"

#include "AnsiCLibrary/Pkg_AnsiCLibrary.h"

namespace CppCodegenTest {
class Class3;
}

namespace CppCodegenTest {

/************************************************************/
/**
 * 
 */
class Class4 {
public:
	/**
	 * 
	 */
	typedef char * pChar;

	Class4() :
			arrayDoubleDefault( { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 }) {
	}
	/**
	 * 
	 */
	Class3* c3;
	/**
	 * 
	 */
	char charVar;
	/**
	 * 
	 */
	double doubleVar;
	/**
	 * 
	 */
	float floatVar;
	/**
	 * 
	 */
	int intVar;
	/**
	 * 
	 */
	void voidVar;
	/**
	 * 
	 */
	long longVar;
	/**
	 * 
	 */
	long double longDoubleVar;
	/**
	 * 
	 */
	short shortVar;
	/**
	 * 
	 */
	unsigned int unsignedIntVar;
	/**
	 * 
	 */
	unsigned short unsignedShortVar;
	/**
	 * 
	 */
	unsigned char unsignedCharVar;
	/**
	 * 
	 */
	unsigned long unsignedLongVar;
	/**
	 * 
	 */
	bool boolVar;
	/**
	 * 
	 */
	int16_t int16_tVar;
	/**
	 * 
	 */
	uint16_t uint16_tVar;
	/**
	 * 
	 */
	int32_t int32_tVar;
	/**
	 * 
	 */
	uint32_t uint32_tVar;
	/**
	 * 
	 */
	int64_t int64_tVar;
	/**
	 * 
	 */
	uint64_t uint64_tVar;
	/**
	 * 
	 */
	wchar_t wchar_tVar;
	/**
	 * 
	 */
	int8_t int8_tVar;
	/**
	 * 
	 */
	uint8_t uint8_tVar;
	/**
	 * 
	 */
	::CppCodegenTest::Class4::pChar pc;
	/**
	 * 
	 */
	double arrayDoubleDefault[13];
	/**
	 * 
	 */
	char arrayCharInit[11] = { 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l',
			'd' };
	/**
	 * 
	 */
	char* charString;
	/**
	 * 
	 */
	char* ptrChar;
	/**
	 * 
	 */
	static char* charStringStaticInit;
	/**
	 * 
	 */
	static const char* charStringStaticConstInit;

};
/************************************************************/
/* External declarations (package visibility)               */
/************************************************************/

/* Inline functions                                         */

} // of namespace CppCodegenTest

/************************************************************
 End of Class4 class header
 ************************************************************/

#endif
