package sos.util;

/**
 * Klasse SOSUniqueID.java
 * 
 * Generieren eindeutige Identifier
 * 
 * Der Identifier wird gebildet aus der Umwandlung der Zeit in Milisekunden
 * 
 * @author Mürüvet Öksüz
 *
 * 
 */
@Deprecated // Millisekunden nicht notwendigerweise unique
public class SOSUniqueID {
	
	static long current= System.currentTimeMillis();
		
	/**
	 * liefert eine eindeutige Identifier
	 * 
	 * @return long
	 */
	static public synchronized long get(){	
		return current++ ;
	}
	
	public static void main(String[] args) {
		
		System.out.println("Start at = " + System.currentTimeMillis());
		
		for (int i = 0; i < 100; i++) {
			System.out.println(SOSUniqueID.get());
		}
		System.out.println("End at = " + System.currentTimeMillis());
	}
}


