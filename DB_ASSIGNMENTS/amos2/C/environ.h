/*****************************************************************************
 * AMOS2
 *
 * Author: (c) 2011 Tore Risch, UDBL
 * $RCSfile: environ.h,v $
 * $Revision: 1.17 $ $Date: 2014/12/19 09:13:53 $
 * $State: Exp $ $Locker:  $
 *
 * Description: Macros to handle different compilation environments
 * ===========================================================================
 * $Log: environ.h,v $
 * Revision 1.17  2014/12/19 09:13:53  torer
 * Revert .h files
 *
 * Revision 1.15  2014/05/09 18:18:32  torer
 * *** empty log message ***
 *
 * Revision 1.14  2014/05/09 16:53:34  torer
 * Testing for 64 bits architecture under Unix
 *
 * Revision 1.13  2014/05/03 14:03:30  torer
 * FP removed
 *
 * Revision 1.12  2014/05/03 13:31:25  torer
 * 32 bits file pointers under Windows, 64 bits on Mac
 *
 * Revision 1.11  2014/04/23 20:18:57  torer
 * *** empty log message ***
 *
 * Revision 1.10  2014/04/23 20:03:47  torer
 * 64 bits integers
 *
 * Revision 1.9  2014/04/18 13:21:07  torer
 * Representing integers with 64 bits
 *
 * Revision 1.8  2014/01/26 16:24:27  torer
 * Defined INLINE macro
 *
 * Revision 1.7  2013/04/29 17:08:36  torer
 * Macro #ZU for printing pointers defined
 *
 * Revision 1.6  2013/02/13 18:39:27  torer
 * Datatype LONGINT defined
 *
 * Revision 1.5  2012/12/21 11:42:52  torer
 * malloc.h obsolete under Unix
 *
 * Revision 1.4  2011/07/06 20:02:30  torer
 * UNIX -> LINUX
 *
 * Revision 1.3  2011/04/28 20:13:31  torer
 * More general DLL environment test
 *
 * Revision 1.2  2011/03/24 18:01:22  torer
 * error message
 *
 * Revision 1.1  2011/03/09 12:29:15  torer
 * Macros to set compilation environments
 *
 ****************************************************************************/

#ifndef _environ_h_
#define _environ_h_

//-----------------------------
#if defined(__cplusplus)
    #define EXTLANG extern "C"
#else
    #define EXTLANG extern
#endif
//-----------------------------
#if defined(WIN32)
    #include <io.h>
    #define NT 1
#elif defined(LINUX)
    #define O_BINARY 0
#else 
    #error Unsupported operating system
#endif
//-----------------------------
#if defined(KERNEL_DLL) // Compiling kernel DLL
    #define EXPORT __declspec(dllexport)
    #define EXTERN EXPORT
#elif defined(_DLL) || defined(_USRDLL) // Compiling DLL on top of kernel DLL
    #define EXPORT __declspec(dllexport)
    #define EXTERN EXTLANG __declspec(dllimport)
#elif defined(WIN32) // Compiling non-DLL under Windows
    #define EXPORT
    #define EXTERN EXTLANG __declspec(dllimport)
#elif defined(LINUX)
    #define EXPORT
    #define EXTERN EXTLANG
#else 
    #error Unsupported operating system
#endif
//-----------------------------
#define LONGCHARS 30
#ifdef NT
// Win32
    #include <malloc.h>
    #define INLINE __inline
    #define LONGINT __int64
    #define LongIntToString(x, buff)sprintf(buff,"%I64d",x)
    #define LONGINTMIN _I64_MIN
    #define LONGINTMAX _I64_MAX
    #define ATOI64 _atoi64 
    #define ZU "%u"
    #define ZI "%d"
#else
// Unix
    #include <stdint.h>
    #define INLINE static inline
    #define LONGINT long long
    #define LongIntToString(x, buff)sprintf(buff,"%lld",x)
    #define LONGINTMIN LLONG_MIN
    #define LONGINTMAX LLONG_MAX
    #define ATOI64 atoll 

    #if UINTPTR_MAX == 0xffffffffffffffff
// 64 bits Unix
      #define B64 1
      #define ZU "%zu"
      #define ZI "%lld"
    #else
// 32 bits Unix
      #define ZU "%lu"
      #define ZI "%d"
    #endif
#endif
//-----------------------------

#endif
