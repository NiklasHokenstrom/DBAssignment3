/*****************************************************************************
 * AMOS2
 *
 * Author: 2014 Tore Risch, UDBL
 * $RCSfile: Foreign.java,v $
 * $Revision: 1.4 $ $Date: 2014/02/28 16:03:14 $
 * $State: Exp $ $Locker:  $
 *
 * Description: Examples of foreign function definitions in Java
 *
 ****************************************************************************/

/***************************************************************************
 * NOTICE:                                                                 *
 * To include the Amos foreign function definitions below in an Amos       *
 * database image 'mydb.dmp' you have to:                                  *
 * 1. Compile the program with the command:                                *
 *      javac -cp "../bin/javaamos.jar" Foreign.java                       *
 * 2. Make a database image 'mydb.dmp' by running the AmosQL script        *
 *      amos2 -O mydbdef.amosql                                            *
 * 3. Run javaamos (Amos under Java) with database image mydb.dmp:         *
 *      javaamos mydb.dmp                                                  *
 *    Test the functions in AmosQL queries:                                *
 *      mysqrt(2);                                                         *
 *      mysqrt(0);                                                         *
 *      mysqrt(-1);                                                        *
 *      myabs(-1.22);                                                      *
 *      select x from Number x where myabs(x)=2.3;                         *
 *      set :b = jota(1,1000000);                                          *
 *      count(:b);                                                         *
 **************************************************************************/

import callin.*;
import callout.*;

public class Foreign 
{

    /**
     * Must have default constructor:
     */
    public Foreign()
    {
	// Put initializations here...
    }

    /**
     * AmosQL definition:
     *
     *   create function mysqrt(Number x) -> Bag of Real
     *     as foreign "JAVA:Foreign/mysqrtBF";
     *
     *  Returns bag containing  1 or 2 results if x>=0
     */
    public void mysqrtBF(CallContext cxt, Tuple tpl) throws AmosException
    {

	double x;

	x = tpl.getDoubleElem(0);	// Pick up the real argument
	if (x < 0.0) 
	    {
		// Don't return any value if x < 0
	    }
	else if (x==0)
	    {
		// One root if x is zero
		tpl.setElem(1, 0.0);
		cxt.emit(tpl);
	    }
	else
	    {
		// Two roots
		tpl.setElem(1, Math.sqrt(x));
		cxt.emit(tpl);
		tpl.setElem(1, -Math.sqrt(x));
		cxt.emit(tpl);
	    }
    }

    /**
     * myabs(x) is an example of the implementation of a multidirectional
     * foreign function in Java.
     *
     * AmosQL definition:
     *   create function myabs(Number x) -> Number 
     *     as multidirectional ("bf" foreign "JAVA:Foreign/myabsBF")
     *                         ("fb" foreign "JAVA:Foreign/myabsFB");
     */

    public void myabsBF(CallContext cxt, Tuple tpl) throws AmosException
    {

	double x;
     
	x = tpl.getDoubleElem(0); // pick up first argument
	if(x<0) tpl.setElem(1,-x);
	else    tpl.setElem(1,x);
	cxt.emit(tpl);
    }

    public void myabsFB(CallContext cxt, Tuple tpl) throws AmosException 
    {
	// Inverse of abs(x)
	double x;
     
	x = tpl.getDoubleElem(1); // pick up result
	if(x==0.0)
	    {
		tpl.setElem(0,x);
		cxt.emit(tpl);
	    }
	else
	    {
		tpl.setElem(0,x);
		cxt.emit(tpl);
		tpl.setElem(0,-x);
		cxt.emit(tpl);
	    }
    }
}
