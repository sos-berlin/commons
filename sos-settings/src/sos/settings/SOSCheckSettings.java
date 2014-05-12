
package sos.settings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSString;

/**
 * 
 * Diese Klasse interpretiert boolsche Ausdrücke.
 * Die Boolschen Ausdrücke bestehen aus den Operanten = "and" und "or". Die Klein- und Großschreibung
 * der Operanden ist unwichtig.
 * 
 * 
 * ***************************************************************************
 * ***************************************************************************
 * Es existieren drei Konstruktoren. 
 * ***************************************************************************
 * ***************************************************************************
 * 
 * Konstruktor 1 und 2:
 * --------------------- 
 * zu den boolischen Ausdruck wird überprüft, ob ein Wert in der Hashtabelle existiert. Wenn ja, dann
 * wird dieses feld als true interpretiert. Sonst als False.
 * Z.B. 	boolische Austdruck: (name and vorname and strasse and plz) or kundennummer
 * 	   		HashMap besteht aus = (name="otto", vorname="hans", plz ="", kundennummer="123")
 *         
 *          -> das entspricht: (true and true and false(*) and false) or true 
 *                  bzw.        (1 and 1 and 0 and 0) or 1 
 *          => das Resultat ist eine 1 (bzw. true)
 * 
 *         (*)da strasse nicht in der HashMap vorhanden ist, wird dieser auch als false interpretiert.
 * 
 * 1. Konstruktor besteht aus 3 Parametern:
 * -----------------------------------------
 * - Property sektion mandantory als Property
 * - HashMap Werte zu den Pflichtfelder
 * - sosLogger Objekt
 * 
 * 
 * 2. Konstruktor besteht aus 3 Parametern:
 * -----------------------------------------
 * - String Name der Konfigurationsdatei mit Verzeichnisangaben
 * - HashMap Werte zu den Pflichtfelder
 * - sosLogger Objekt
 * 
 * 
 * 3. Konstruktor besteht aus 2 Parametern:
 * ----------------------------------------
 * - String condition in der Form (true and false) or false bzw. ((1 and 0) or 0)
 * - sosLogger Objekt
 * 
 * 
 * @author Mürüvet Öksüz
 *
 */
public class SOSCheckSettings {
	
	/** Pfad und Name der Datenquelle ( Konfigurationstabelle) */
	private String source				= "";
	
	/** logger objekt */
	private SOSLogger logger			= null;
	
	/** Properties der Sektion mandantory */
	private Properties sectionMandatory = null;
	
	/** SOSString Object */
	private SOSString sosString    		= new SOSString();;
	
	/** Attribut Ausgelesenen Text der Pflichtfelder aus der Sektion mandantory */
	private String condition 			= "";
	
	/** Attribut: Werte zu den Pflichtfelder. */
	private HashMap values				= null;
	
	/** Attribut: Liste der Regeln */
	private HashMap rules = new HashMap();
	
	/** Attribut: boolische Operator */
	private String operator = " and | or | xor ";
	
	
		
	/**
	 * Konstruktor
	 * @deprecated aus der Properties sectionMandatory_ wir nur ein Eintrag "mandatory" ausgelesen. In 
	 * Zukunft kann nur der String als Parameter übergeben werden. Der Eintrag in sectionMandatory gibt  
	 * den boolischen Ausdruck an.
	 * @param Properties der Sektion Mandantory 
	 * @param SOSLogger Objekt
	 * @param HashMap values -> eventuell Vorhandene Daten zu der zu den Pflichtfelder.
	 *   
	 */
	public SOSCheckSettings(Properties sectionMandatory_, HashMap values_, SOSLogger logger_) throws Exception {
		try {
			this.sectionMandatory = sectionMandatory_;
			this.values = values_;
			logger = logger_;
			condition = sosString.parseToString(sectionMandatory,"mandatory"); 
			init();            			
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}       
	}
	
	/**
	 * Konstruktor
	 * @param Properties der Sektion Mandantory 
	 * @param SOSLogger Objekt
	 * @param HashMap values -> eventuell Vorhandene Daten zu der zu den Pflichtfelder.
	 *   
	 */
	public SOSCheckSettings(String condition_, HashMap values_, SOSLogger logger_) throws Exception {
		try {			
			this.values = values_;
			logger = logger_;
			condition = condition_; 
			init();            			
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}       
	}
	
	
	/**
	 * Konstruktor
	 * @param String -> condition_ in der Form (true and/or false) bzw. (1 and/or 0)mit Klammern.
	 */
	public SOSCheckSettings(String condition_, SOSLogger logger_) throws Exception {
		try {
			this.condition = condition_;      
			logger = logger_;
			values = new HashMap();
			values.put("true", "1");            
			values.put("1", "1");            
			init();            
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}       
	}
	
	private void init() throws Exception {
		try {			
			rules = new HashMap();
			
			//VerOderung
			if ((condition.toLowerCase().indexOf(" or ") > -1) || 
					(condition.toLowerCase().indexOf("(or") > -1) ||
					(condition.toLowerCase().indexOf(")or") > -1) ||
					(condition.toLowerCase().indexOf(" or(") > -1) ||
					(condition.toLowerCase().indexOf(" or)") > -1) ) {
				rules.put("0or0", "0");
				rules.put("0or1", "1");
				rules.put("1or0", "1");
				rules.put("1or1", "1");
				rules.put("\\(0or0\\)", "0");
				rules.put("\\(0or1\\)", "1");
				rules.put("\\(1or0\\)", "1");
				rules.put("\\(1or1\\)", "1");
			}
			
			
			//VerUNDung
			if ((condition.toLowerCase().indexOf(" and ") > -1) || 
					(condition.toLowerCase().indexOf("(and") > -1) ||
					(condition.toLowerCase().indexOf(")and") > -1) ||
					(condition.toLowerCase().indexOf("and(") > -1) ||
					(condition.toLowerCase().indexOf("and)") > -1) ) {
				rules.put("0and0", "0");
				rules.put("0and1", "0");
				rules.put("1and0", "0");
				rules.put("1and1", "1");
				rules.put("\\(0and0\\)", "0");
				rules.put("\\(0and1\\)", "0");
				rules.put("\\(1and0\\)", "0");
				rules.put("\\(1and1\\)", "1");
			}
			//XODER
			if ((condition.toLowerCase().indexOf(" xor ") > -1) || 
					(condition.toLowerCase().indexOf("(xor") > -1) ||
					(condition.toLowerCase().indexOf(")xor") > -1) ||
					(condition.toLowerCase().indexOf("xor(") > -1) ||
					(condition.toLowerCase().indexOf("xor)") > -1) ) {
				rules.put("0xor0", "0");
				rules.put("0xor1", "1");
				rules.put("1xor0", "1");
				rules.put("1xor1", "0");
				rules.put("\\(0xor0\\)", "0");
				rules.put("\\(0xor1\\)", "1");
				rules.put("\\(1xor0\\)", "1");
				rules.put("\\(1xor1\\)", "0");
			}
			
			//NOT
			/*if ((condition.toLowerCase().trim().indexOf("not(") > -1) || 
					(condition.toLowerCase().indexOf(" not ") > -1)) {					
				rules.put("not1", "0");
				rules.put("not0", "1");
				rules.put("not\\(1\\)", "1");
				rules.put("not\\(0\\)", "0");
			}*/
			
//			allgemein 
			rules.put("\\(1\\)", "1");
			rules.put("\\(0\\)", "0");
			
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
	}
	
	/** 
	 * Überprüft, ob das element in der condition existiert und einen
	 * Wert besitzt.
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public boolean check(String element) throws Exception {
		boolean retVal = false;
		try {
			if (sosString.parseToString(values, element).length() > 0) {
				return true; 
			} else {
				return false;
			}            
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
	}      
	
	
	
	/**
	 * Auslesen der Bedingung, die in der Konfigurationsdatei stehen.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String getCondition() throws Exception {                            
		try {            
			return condition;
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
		
	}
	
	/**
	 * Setzen der Werte der Condition. Die keys in der HashTable sind die Platzhalter 
	 * in der condition. 
	 * @param HashMap, dessen Keys Platzhalter für die Condition ist.  
	 * @throws Exception
	 */
	public void setConditionValues(HashMap values_) throws Exception {
		try {
			values = values_; 
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
	}
	
	
	
	/**
	 * normalisiert den String
	 * @param key
	 * @return
	 * @throws Exception
	 */   
	private String normalizedString(String key) throws Exception {
		try {
			key = key.replaceAll("\\(", "");
			key = key.replaceAll("\\)", "");            
			key = key.replaceAll("  "," ");
			return key;
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		} 
	}
	
	/**
	 * normalisiert den And und Or in der String hCond in kleinbuchstaben
	 * @param key
	 * @return String
	 * @throws Exception
	 */   
	private String normailzedANDOR(String hCond) throws Exception {
		try {
			hCond = hCond.replaceAll(" AND ", " and ");
			hCond = hCond.replaceAll(" aND ", " and ");			
			hCond = hCond.replaceAll(" anD ", " and ");
			hCond = hCond.replaceAll(" and ", " and ");
			hCond = hCond.replaceAll(" AnD ", " and ");
			hCond = hCond.replaceAll(" aNd ", " and ");			
			hCond = hCond.replaceAll(" ANd ", " and ");
			hCond = hCond.replaceAll(" And ", " and ");
			
			hCond = hCond.replaceAll(" XOR ", " xor ");
			hCond = hCond.replaceAll(" xOR ", " xor ");
			hCond = hCond.replaceAll(" xoR ", " xor ");			
			hCond = hCond.replaceAll(" xor ", " xor ");
			hCond = hCond.replaceAll(" XoR ", " xor ");
			hCond = hCond.replaceAll(" xOr ", " xor ");
			hCond = hCond.replaceAll(" XOr ", " xor ");			
			hCond = hCond.replaceAll(" Xor ", " xor ");									
			
			/*hCond = hCond.replaceAll(" NOT ", " not ");
			hCond = hCond.replaceAll(" nOT ", " not ");
			hCond = hCond.replaceAll(" noT ", " not ");			
			hCond = hCond.replaceAll(" not ", " not ");			
			hCond = hCond.replaceAll(" NoT ", " not ");			
			hCond = hCond.replaceAll(" nOt ", " not ");			
			hCond = hCond.replaceAll(" NOt ", " not ");
			hCond = hCond.replaceAll(" Not ", " not ");
			*/
			
			
			hCond = hCond.replaceAll(" OR ", " or ");
			hCond = hCond.replaceAll(" Or ", " or ");
			hCond = hCond.replaceAll(" oR ", " or ");
			
			
			return hCond;
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		} 
	}
	
	
	/**
	 * Die Einträge der Condition werden durch die Keys der conditionalValues ersetz.
	 * 
	 * Ist ein Key nicht vorhanden, dann ist es ein false.
	 * Ist der Key leer bzw. null, dann ist der Wert false
	 * @return
	 * @throws Exception
	 */
	public boolean process() throws Exception {
		boolean retVal = false;
		String hCond = ""; //hilfsvariable
		String[] cond = null; //hilfsvariable
		String result = "";
		String msg = "";
		try {
			checkCondition();
			checkValues();
			hCond = condition;          
			//hCond = normailzedANDOR(hCond);            
			cond = normalizedString(hCond).split(operator); 
			cond = this.sortDesc(cond, false);
			for (int i = 0; i < cond.length; i++) {
				if (sosString.parseToString(values,cond[i].trim()).length() == 0) {
					hCond = hCond.replaceAll(cond[i].trim(),"0"); 
				} else {
					hCond = hCond.replaceAll(cond[i].trim(),"1");
				}
			}
			//Pflichtfelder zum Auswerten ist
			logger.debug("..Condition: " + condition);
			logger.debug("..is equal : " + hCond);                                                
			hCond = hCond.trim();
			hCond = hCond.replaceAll(" ", "");
			
			while (hCond.length() != 1) {                
				Iterator keys = rules.keySet().iterator();
				Iterator vals = rules.values().iterator();            
				String key = "";
				String val = "";				
				while (keys.hasNext() && hCond.length() != 1) {
					key = keys.next().toString().trim();									
					val = vals.next().toString().trim();					
					msg = ".. put rules [" + key + "=" + val + "] to condition = " + hCond ;
					hCond = hCond.replaceAll("  ", " ").replaceAll(key, val);
					//logger.debug9(msg + " and result is = " + hCond);
					
				}
			}
			if (hCond.equalsIgnoreCase("1")) {
				retVal = true;
			} else {
				retVal = false;
			}
			logger.debug("..result is : " + retVal);
			return retVal;
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
		
	}
	
	/**
	 * Section mandantory auslesen auslesen
	 * @throws Exception
	 */
	public void getSection() throws Exception{        
		SOSProfileSettings settings = null;                
		try {                                               
			logger.debug4("reading settings ..." + source);                               
			settings	= new SOSProfileSettings(source, logger);            
			sectionMandatory = settings.getSection("mandatory"); 
			
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
	}
	
	/**
	 * 	Sort a String array using selection sort.
	 *  desc boolean = true ist für das Aufsteigende Sortierung
	 *  desc boolean = false ist für das Absteigende Sortierung
	 * @param String[] eine Menge unsortierte String
	 * @return String[] eine Menge der Sortierten String
	 */  
	private String[] sortDesc(String[] a, boolean desc) throws Exception {
		String[] aa = null; //Hilfsvariable, wenn absteigen Sortiert werden soll
		int len = -1; //Hilfsvariable, länge der Array
		try {
			for (int i=0; i < a.length-1; i++) {
				for (int j=i+1; j<a.length; j++) {
					if (a[i].compareTo(a[j]) > 0) {
						String temp=a[j]; 
						a[j]=a[i]; 
						a[i]=temp;
					}
				}
			}
			if (desc) {
				return a;
			} else {
				len = a.length;
				aa = new String[len];
				for (int i = 0; i < len; i++) {
					aa[len-1 - i] = a[i];
				}
				return aa;
			}
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
	}
	
	/**
	 * Überprüft, ob der gegebene boolische Ausdruck ein gültiger Ausdruck ist.
	 * 1. Überprüfung: Anzahl der geöffneten Klammer == Anzahl der geschlossene Klammern
	 * 2. Grosschreibung in Kleinschreibung umwandeln
	 */
	private void checkCondition() throws Exception {
		String hcon = null; // hilfsvariable
		try {
			condition = condition.toLowerCase();	
			hcon = condition;
			if (condition != null && condition.length() > 0) {
				hcon = hcon.replaceAll("\\(", " \\( ");
				hcon = hcon.replaceAll("\\)", " \\) ");			
				if (hcon.split("\\(").length != hcon.split("\\)").length) {			   
					throw new Exception("\n ..count of \"(\" is not equal of count of \")\" for condition : " + condition); 
				} 						
			}
			
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
		
	}
	
	
	/**
	 * 
	 * 2. Grosschreibung (key) in Kleinschreibung umwandeln
	 */
	private void checkValues() throws Exception {
		String hcon = null; // hilfsvariable
		HashMap hValues = new HashMap();
		try {		    
			Iterator keys = values.keySet().iterator();
			Iterator vals = values.values().iterator();            
			String key = "";
			Object val = "";
			while (keys.hasNext()) {
				key = keys.next().toString().toLowerCase();
				val = vals.next();
				logger.debug5(".. " + key + "=" + val);
				hValues.put(key, val);
			}
			if (values == null) {
				values = new HashMap();
			} else {
				values.clear();
			}
			values.putAll(hValues);
		} catch (Exception e){
			throw new Exception ("\n -> error in " + SOSClassUtil.getMethodName() + " " + e); 
		}
		
	}
	
	public static void main(String[] args) {		
		
		HashMap values4condition  	=  null;
		SOSCheckSettings checkSettings = null;
		Properties sectionMandatory = null;	
		sos.util.SOSStandardLogger sosLogger = null;
		try {
			sosLogger = new sos.util.SOSStandardLogger(9);
			
			/*******************************************************************************
			 *****************            Beispiel 1                 ***********************
			 ********************************************************************************/
			/*sectionMandatory = new Properties();
			 sectionMandatory.put("mandatory","(name AND vorname AND adresse) or kundennummer");						
			 values4condition  = new HashMap(); 
			 values4condition.put("vorname", "Hans");
			 values4condition.put("name", "Otto");
			 values4condition.put("adresse", "Giesebrechtstr. 14"); 
			 values4condition.put("kundennummer", "12345678");
			 
			 checkSettings = new SOSCheckSettings(sectionMandatory, values4condition, sosLogger);
			 if (checkSettings.process()) {
			 sosLogger.debug4("boolischer Ausdruck ist OK");
			 }	else {
			 sosLogger.debug4("boolischer Ausdruck ist nicht OK");				
			 }
			 sosLogger.debug4("..checked mandatory fields: " + checkSettings.getCondition());
			 */
			
			/*******************************************************************************
			 *****************            Beispiel 2          *******************************
			 ********************************************************************************/
			values4condition  = new HashMap(); 
			values4condition.put("vorname", "Hans");
			values4condition.put("name", "ss");
			values4condition.put("adresse", "Giesebrechtstr. 14"); 
			values4condition.put("kundennummer", "12345678");
			
			checkSettings = new SOSCheckSettings("(name and vorname) and kundennummer", values4condition, sosLogger);
			if (checkSettings.process()) {
				sosLogger.debug4("boolischer Ausdruck ist OK");
			}	else {
				sosLogger.debug4("boolischer Ausdruck ist nicht OK");				
			}
			sosLogger.debug4("..checked mandatory fields: " + checkSettings.getCondition());
			
			
		} catch (Exception e) {
			System.err.print("\n -> ..error occurred in SOSCheckSettings " + ": " + e);
		}
	}
	
	
}