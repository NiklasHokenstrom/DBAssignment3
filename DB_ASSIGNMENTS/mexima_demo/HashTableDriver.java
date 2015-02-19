/*-*******************************************************************
 * AMOS2
 *
 * Author: (c) 2010 Thanh Truong, UDBL
 * $RCSfile: AMOSQL,v $
 * $Revision: 1.1 $ $Date: 2010/02/12 20:01:08 $
 * $State: Exp $ $Locker:  $
 *
 * Description:  
 *  DEMONTRATION ON HOW TO INTRODUCE A NEW INDEXING STRUCTURE TO AMOS II ***
 * 
 *  In this demontration, we make the built-in HashTable in Java as an external
 *  index of AMOS II. 
 *  This is done by implementing a set of foreign functions in  Java:
 *  make, put, get, delete, mapper and clear respectively.
 *
 *  Run this demo in JavaAmos.
 *
 ****************************************************************************
 * $Log: HashTableDriver,v $
 *
 ****************************************************************************/

import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import callin.*;
import callout.*;
public class HashTableDriver {

    // A list of hash tables(s) with unique id for each.
    private  static ArrayList  m_lhashes;

    // Id base, starts from 0
    private  int idgen = 0;

    // Main entry
    public static void main(String argv[]) throws AmosException {

    }

    /**
       Default constructor.
    */
    public HashTableDriver() {

    }
    
    /*-----------------------------------------------------------------
      Find Hash table given its identifier
      Note that the first PUT always constructs the JHashTable
     -----------------------------------------------------------------*/
    private Hashtable<Integer, Oid> locateJHashTable(int id) {
	// Initialize the list if needed.
	if (m_lhashes == null) {
	    m_lhashes = new ArrayList();
 	} 

	Hashtable<Integer, Oid> m = null;
	if (id < m_lhashes.size()) { m = (Hashtable<Integer, Oid>) m_lhashes.get(id);}

	// If there is no such JHashTable
	if (m == null) {
	    // Construct a new one
	    m = new Hashtable<Integer, Oid>(); 
	    // Put it into our managed list.
	    m_lhashes.add(id, m);
	}
	return m;

    }
    /*-----------------------------------------------------------------
      MAKE simply returns an id     
     -----------------------------------------------------------------*/
    public void JHashTable_make(CallContext cxt, Tuple tpl)throws AmosException{
	tpl.setElem(0, idgen);
	idgen += 1;
	cxt.emit(tpl);
    }

    /*-----------------------------------------------------------------
      PUT puts <key, val> into a Hash table given its Id.
     -----------------------------------------------------------------*/
    public void JHashTable_put(CallContext cxt, Tuple tpl)throws AmosException{
	// Get the id 
	int id = tpl.getIntElem(0);
	int keyOid = tpl.getIntElem(1);
	Oid val =  tpl.getOidElem(2);
	Integer key = new Integer(keyOid);
	
	Hashtable<Integer, Oid>  m = locateJHashTable(id);

	if (m != null){
	    m.put(key, val);
	}
	// Emit
	tpl.setElem(3, val);
	cxt.emit(tpl);
    }
    /*-----------------------------------------------------------------
      GET returns val associated with the given key
     -----------------------------------------------------------------*/
    public void JHashTable_get(CallContext cxt, Tuple tpl)throws AmosException{
	// Get the id 
	int id = tpl.getIntElem(0);
	int keyOid = tpl.getIntElem(1);
	Oid val = null;
	Integer key = new Integer(keyOid);
	Hashtable<Integer, Oid> m = locateJHashTable(id);
	if (m != null){
	   val = m.get(key);
	   if (val != null) {
	       tpl.setElem(2, val);
	   }
	}
	cxt.emit(tpl);
    }
    /*-----------------------------------------------------------------
      DELETE deletes (key,val) pair
     -----------------------------------------------------------------*/
    public void JHashTable_delete(CallContext cxt, Tuple tpl)throws AmosException{
	int id = tpl.getIntElem(0);
	int keyOid = tpl.getIntElem(1);
	Integer key = new Integer(keyOid);

	Hashtable<Integer, Oid> m = locateJHashTable(id);
	if (m != null){
	    m.remove(key);
	}
	cxt.emit(tpl);
    }
    /*-----------------------------------------------------------------
      MAPPER iterates through a Hash table and returns (key, val)
     -----------------------------------------------------------------*/
    public void JHashTable_mapper(CallContext cxt, Tuple tpl)throws AmosException{
	int id = tpl.getIntElem(0);
	Integer key;
	Oid val;
	Tuple res = new Tuple(2);
	/*Do the iteration and emit the result*/
	Hashtable m = locateJHashTable(id);
	if (m != null){
	    Enumeration keys = m.keys();
	    while (keys.hasMoreElements()){
		key = (Integer)keys.nextElement();
		val = (Oid)m.get(key);
		res.setElem(0, val);
		res.setElem(1, key);
		tpl.setElem(1, res);
		cxt.emit(tpl);	
	    } 
	}
	return;
    }
    /*-----------------------------------------------------------------
      CLEAR flushes away entire JHashTable given its Id
     -----------------------------------------------------------------*/
    public void JHashTable_clear(CallContext cxt, Tuple tpl)throws AmosException{
	int id = tpl.getIntElem(0);

	Hashtable m = locateJHashTable(id);
	if (m != null){
	    m_lhashes.remove(m);
	}

	cxt.emit(tpl);
    }    
}
