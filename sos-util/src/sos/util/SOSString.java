package sos.util;


/**
 * <p>Title: </p>
 * <p>Description: Stringverarbeitung</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @version $Id$
 */


public class SOSString {


  /**
   * <p>Liefert true falls die eingegebene Zeichenkette leer oder null ist, sonst false</p>
   *
   * <pre>
   * SOSStringisEmpty(null) liefert true
   * SOSStringisEmpty("") liefert true
   * SOSStringisEmpty(" ") liefert false
   * </pre>
   *
   * @param string der geprft werden soll
   * @return <code>true</code> falls string leer oder null, sonst false
   */
  public static boolean  isEmpty( String string ) {
    return ( string == null || string.length() == 0 );
  }
  
  /**
	  * Konvertiert key in Boolean. 
	  * Diese Methode liefert true, wenn der Key  
	  * 	 "1"    oder 
	  * 	 "yes"  oder
	  * 	 "true" ist.
	  * Sonst wird false geliefert 
	  * Dabei werden Gro� und Kleinschreibung nicht beachtet.	
	  *
	  * @author Mrvet �sz
	  *	
	  * @param key 
	  * @return String
	  * @exception Exception
	  */
	  public boolean parseToBoolean(Object key) throws Exception {
		  try {
			  if (key != null) {
				  if (key.toString().equalsIgnoreCase("true")
					  || key.toString().equalsIgnoreCase("yes") 
					  || key.equals("1")) {
					  return true;
				  } else {
					  return false;
				  }
			  } else {
				 return false;
			  }
		  } catch(Exception e) {
			  throw new Exception ("..error in " + SOSClassUtil.getMethodName() + " " + e);
		  }
	  }
	  
	
	/**
	 * Hat die Hashtabelle ein Feld key, so wird der Inhalt dieses Key in String konvertiert
	 * und bergeben. Ist der Hashtabelle leer, dann wird ein Leerstring bergeben.
	 *
	 * @author Mrvet �sz
	 *
	 * @param hash 
	 * @param key 
	 * @return String
	 * @exception Exception
	 */
	public String parseToString(java.util.HashMap hash, String key) throws Exception {
		try {
			if (hash != null && hash.get(key) != null) {
				return hash.get(key).toString();
			}	else {
			  return "";
			}
		} catch(Exception e) {
			throw new Exception ("..error in " + SOSClassUtil.getMethodName() + " " + e);
		}
		 
	}

	/**
	 * Konvertiert key in String, wenn dieser nicht null ist. Sonst wird ein Leerstring 
	 * bergeben. 	  
	 *
	 * @author Mrvet �sz
	 *
	 * @param key 
	 * @return String
	 * @exception Exception
	 */
	public String parseToString(Object key) throws Exception {
		try {
			if (key != null) {
				return key.toString();
			} else {
			  return "";
			}
		} catch(Exception e) {
			throw new Exception ("..error in " + SOSClassUtil.getMethodName() + " " + e);
		}
	}
	
	
	/**
	 * Wenn das Objekt Properties einen key hat, dann wird dieser in String konvertiert
	 * und bergeben.  
	 * Sonst wird ein leerstring bergeben.
	 *
	 * @author Mrvet �sz
	 * 
	 * @param prop
	 * @param key
	 * @exception Exception
	 */
	public String parseToString(java.util.Properties prop, String key) throws Exception{
		try {
			if (prop.get(key) != null) {
				return prop.get(key).toString();
			} else {
				return "";
			}
		} catch(Exception e) {
			throw new Exception ("..error in " + SOSClassUtil.getMethodName() + " " + e);
		}
  }


}
