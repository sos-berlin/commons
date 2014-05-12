package sos.connection.util;

/**
 * <p>Title: SOSProfiler</p>
 * <p>Description: Messen der Zugriffszeiten.
 * Die Profiler kann direkt über ein Methodenaufruf eingeschaltet.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS-Berlin GmbH</p>
 * @author Mueruevet Oeksuez
 * @version 1.0
 */

import java.io.PrintWriter;
import java.io.StringWriter;

import sos.connection.SOSConnection;

public class SOSProfiler {

  /* Datenbanktabelle Name für Profiler-Einstellungen */
  private static String table_settings 		= "PROFILER_SETTINGS";
  /* Datenbanktabelle Name für Profiler-Historie */
  private static String table_history 		= "PROFILER_HISTORY";
  /* Name des Eintrags für Sequenzzähler der Historieneinträge in Tabelle profiler_settings */
  private static String entry_history_id 	= "profiler_history.id";
  /* Name des Eintrags für Sequenzzähler der Dialogschritte in Tabelle profiler_settings */
  private static String entry_history_step 	= "profiler_history.step";
  /* sequence für PROFILER_HISTORY*/
  private int history_id;
  /* Zeitstempel fur Starten*/
  private long startTimeStamp;
  /* Name der Applikation */
  private static String application;
  /* Step */
  private static int step;
  /* job id*/
  private static int session;
  /* Anzahl der Aufrufe der process*/
  public int scope 						= 100; //Strukturtiefe
  /* Attribut: jobname */
  private static String scriptname;
  /* Attribut: Workflowpackage */
  private static String context;
  /* Name der Klasse, die die Methode start aufgerufen hat*/
  private String classname 				= new String();
  /* Name der Methode, die die Methode start aufgerufen hat*/
  private String methodname 			= new String();
  /* Profiler Connection */
  public SOSConnection conn;
  /* Profiling eingeschaltet */
  public static boolean profilingAllowed = true;
  /* Strukturtiefe */
  public static String profilingDepth = "profiling_depth";
  /* Strukturtiefe*/
  public static int allowedDepth = 100;

  /**
   * Konstruktor
   * Der Parameter conn_ ist von der sos.connection.SOSConnection - Connectionklassen
   * @param conn_
   */
  public SOSProfiler(SOSConnection conn_) {
  	
  	try {
    	conn = conn_;
    	step = getSequence(entry_history_step);
	}
	catch(Exception e){
		// ignore all errors
	}
  }


  /**
   * Methode zum starten der Profiler. Übergeben wird ein Statement als String, falls vorhanden
   * @param sqlStatement
   */
  public void start(String sqlStatement) {

    try {

      if (! (profilingAllowed)) {
        return;
      }
      String insStr = new String();

      getMethodName(); //Klassenname, Methodenname, Strukturtiefe bestimmen
      if (scope > allowedDepth) {
        return;
      } //Ueberpruefung, ob in dieser Strukturtiefe Profiling erlaubt ist.

      sqlStatement = sqlStatement.replaceAll("'", "''"); //SQL Quoten
      setStartTimeStamp(System.currentTimeMillis()); //ZeitStempel in Milisek setzen.

      //System.out.println("\t\tProfiling start Begin " + getStartTimeStamp());
      history_id = this.getSequence(entry_history_id);
      
      insStr = " INSERT INTO " + table_history + " ( " +
          "  \"ID\" " + /* C Primärschlüssel */
          ", \"APPLICATION\" " + /* s Name der application */
          ", \"SESSION\" " + /* s ID der Sitzung */
          ", \"STEP\" " + /* s ID des Dialogschritts */
          ", \"CONTEXT\" " + /* s Name des Kontextes */
          ", \"SCOPE\" " + /* ? Strukturtiefe */
          ", \"CLASS\" " + /* OK Name der Klasse */
          ", \"FUNCTION\" " + /* OK Name der Funktion */
          ", \"SCRIPT_NAME\" " + /* Name des Scripts */
          //", \"QUERY_STRING\" " + /* Inhalt des Query-Strings */
          //", \"REMOTE_ADDR\" " + /* IP-Adresse des Clients */
          //", \"USER_NAME\" " + /* Name des Benutzers */
          ", \"START_TIME\" " + /* Zeitstempel des Beginns der Ausführung */
          //", \"END_TIME\" " + /* Zeitstempel des Endes der Ausführung */
          ", \"START_TIMESTAMP\" " +
          /* Zeitstempel in Millisekunden des Beginns der Ausführung */
          //", \"END_TIMESTAMP\" " + /* Zeitstempel in Millisekunden des Endes der Ausführung */
          //", \"ELAPSED\" " +/* Anzahl Millisekunden zwischen start_time und end_time */
          ", \"STATEMENT\" " + /* Inhalt des SQL-Statements */
          //", \"ERROR\" " + /* Boolean: 0=ok, 1=Fehler */
          //", \"ERROR_CODE\" " + /* Fehlercode der Klasse */
          //", \"ERROR_TEXT\" " + /* Fehlermeldung der Klasse */
          " ) VALUES ( " + history_id + //historie_id
          ", '" + application + "'" +
          ", " + session +
          ", " + step + //step
          ", '" + context + "'" +
          ", " + scope +
          ", '" + classname + "'" +
          ", '" + methodname + "'" +
          ", '" + scriptname + "'" +
          //", NULL" + /* QueryString */
          //", NULL" + /* RemoteAddress */
          //", NULL" + /* UserName*/
          ", %now " + /* StartDate*/
          //", NULL " + /* EndDate*/
          ", " + getStartTimeStamp() +
          //", NULL" + /*Endtimestamp*/
          //", NULL" +
          ", '" + sqlStatement + "'" +
          //", 0" + /* error */
          //", NULL" + /* error-code */
          //", NULL" + /* error-text */
          " )";

      //System.out.println("------------------------------------------------------");
      //    System.out.println("start Statement called: " + insStr);
      //    System.out.println("getStartTimeStamp: " + getStartTimeStamp());
      //System.out.println("------------------------------------------------------");

      profilingAllowed = false; //vermeiden von gegenseitigen Aufruf mit SOSConnection.execute
      conn.execute(insStr);
      conn.commit();
      profilingAllowed = true;
    }
    catch (Exception e) {
      //System.out.println("Error in Profiler.start() " + e);
    }
    //System.out.print("\t\tProfiling start end " + (test));
    //System.out.println("\t\tProfiling start durchschnitt: " + (test-startTimeStamp));
  }


  /**
   * Methode zum Stoppen der  Zugriffszeit Mesung
   * @param error_code
   * @param error_text
   */
  public void stop(String error_code, String error_text) {

    try {

      if (! (profilingAllowed)) {
        return;
      }
      long endTimeStamp = System.currentTimeMillis();
      //System.out.println("Profiling stop Begin " + endTimeStamp);
      int error = 0;
      if ( (error_code != null && error_code.length() > 0) ||
        (error_text != null && error_text.length() > 0)) {
        error = 1;
      }

      ////System.out.println("stop -> history_id: " + history_id);
      String updStr = "UPDATE " + table_history +
          " SET \"END_TIME\" = %now, " +
          " \"END_TIMESTAMP\" = " + endTimeStamp + "," +
          //" \"ELAPSED\" = " + diffTimeStamp + "," +
          " \"ELAPSED\" = " + endTimeStamp + " - \"START_TIMESTAMP\"," +
          " \"ERROR\" = " + error;
      if (error_code != null) {
        if (error_code.length() > 50) {
          updStr = updStr.concat(", \"ERROR_CODE\" = '" +
                                 error_code.substring(0, 50) + "'");
        }
        else {
          updStr = updStr.concat(", \"ERROR_CODE\" = '" + error_code + "'");
        }
      }
      if (error_text != null) {
        if (error_text.length() > 250) {
          updStr = updStr.concat(", \"ERROR_TEXT\" = '" +
                                 error_text.substring(0, 250) + "'");
        }
        else {
          updStr = updStr.concat(", \"ERROR_TEXT\" = '" + error_text + "'");
        }
      }
      updStr = updStr.concat(" WHERE \"ID\" = " + history_id);

      //System.out.println("---------------------------------------------");
      //System.out.println("stop Statement called: " + updStr);
      //System.out.println("---------------------------------------------");

      profilingAllowed = false; //vermeiden von gegenseitigen Aufruf mit SOSConnection.execute
      conn.execute(updStr);
      conn.commit();
      profilingAllowed = true;

    }
    catch (Exception e) {
      //System.out.println("Error in Profiler.start() " + e);
    }

    //long test = System.currentTimeMillis();
    //System.out.print("\t\tProfiling stop end " + (test));
    //System.out.println("\t\tProfiling stop durchschnitt: " + (test-endTimeStamp));
  }

  /**
   * Der Profiler kann hier aktiviert werden.
   * @param profilingAllowed_
   */
  public static void setProfilingAllowed(boolean profilingAllowed_) throws
      Exception {
    profilingAllowed = profilingAllowed_;
  }

  /**
   * Auslesen, ob Profiler erlaubt ist.
   * @return boolean
   */
  public static boolean isProfilingAllowed() {
    return profilingAllowed;
  }

  /**
   * Setzen die Strukturtiefe, indem die Profiling erlaubt ist.
   * @param allowedDepth_
   */
  public static void setDepthAllowed(int allowedDepth_) {
    allowedDepth = allowedDepth_;
  }

  /**
   * holt den nächsten sequenz
   * @param entry
   * @return int
   */
  private int getSequence(String entry) {
    
    int retVal = 0;

    try {
      String updStr = " update " + table_settings +
          " set \"VALUE\"=\"VALUE\"+1 " +
          " where \"NAME\"='" + entry + "'";
      ////System.out.println("statement for " + entry + " called: " + updStr);
      profilingAllowed = false; //vermeiden von gegenseitigen Aufruf mit SOSConnection.execute
      conn.execute(updStr);
      conn.commit();
      profilingAllowed = true;

    }
    catch (Exception e) {
      //System.out.println("Error in Profiler.getSequence() " + e);
    }
    try {
      String selStr = " select \"VALUE\" from " + table_settings +
          " where \"NAME\"='" + entry + "'";
      profilingAllowed = false; //vermeiden von gegenseitigen Aufruf mit SOSConnection.execute
      String singleValue = conn.getSingleValue(selStr);
      profilingAllowed = true;
      retVal = Integer.parseInt(singleValue);
    }
    catch (Exception e) {
      //System.out.println("Error in Profiler.getSequence() " + e);
    }
    return retVal;
  }

  private long getStartTimeStamp() {
    return startTimeStamp;
  }

  private void setStartTimeStamp(long startTimeStamp_) {
    startTimeStamp = startTimeStamp_;
  }

  /*
   * Bestimmt den Namen der Klasse und den Namen der Methode, die die Methode
   * dataswitch.Profiler.start aufgerufen haben. Zusätzlich wird
   * die Strukturtiefe bestimmt.
   */
  private void getMethodName() {

	try {
    	StringWriter sw = new StringWriter();
    	new Throwable().printStackTrace(new PrintWriter(sw));
    	String callStack = sw.toString();
    	////System.out.println(callStack);
    	////System.out.println("Strukturtiefe" + getCountOfString(callStack));
    	scope = getCountOfString(callStack);
    	int atPos = callStack.indexOf("Profiler.start");
    	atPos = callStack.indexOf("at", atPos);
    	int dotPos = callStack.indexOf("(", atPos);
    	callStack = callStack.substring(atPos + 2, dotPos);
    	int metPos = callStack.lastIndexOf(".");
    	////System.out.println("Klassenname: " + callStack.substring(1, metPos));
    	classname = callStack.substring(1, metPos);
    	////System.out.println("Methodenname: " + callStack.substring(metPos + 1, callStack.length()));
    	methodname = callStack.substring(metPos + 1, callStack.length());
	}
    	catch(Exception e){
    		// ignore all errors
    	}
  }

  /*
   * Liefert die Anzahl der String, die im String str vorkommt.
   * @param str
   */
  private static int getCountOfString(String str) {

	try {
    	int iPos = 0;
    	int count = 0;
    	for (int i = 0; i < str.length(); i++) {
      	iPos = str.indexOf("at ");
      	if (iPos > -1) {
        	str = str.substring(iPos + 1, str.length());
        	count = count + 1;
      	}
    	}
    	return count - 2;
	}
		catch(Exception e){
			return 0;
			// ignore all errors
		}
  }

  /**
   * Applikationname, die von der Spooler geliefert wird
   * @param application_
   */
  public static void setApplication(String application_) {
    application = application_;
  }

  /**
   * Setzen der job id
   * @param session_
   */
  public static void setSession(int session_) {
    session = session_;
  }

  /**
   * Setzen der Job Namen
   * @param scriptname_
   */
  public static void setScriptname(String scriptname_) {
    scriptname = scriptname_;
  }

  /**
   * Setzen der Workflowpackage
   * @param context_
   */
  public static void setContext(String context_) {
    context = context_;
  }

  /**
   *  Datenbanktabelle Name für Profiler-Einstellungen setzen
   *  @param tableSettings_
   */
  public void setName4TableSettings(String tableSettings_) {
    table_settings = tableSettings_;
  }

  /**
   *  Datenbanktabelle Name für Profiler-Einstellungen auslesen
   *  @return String
   */
  public String getName4TableSettings() {
    return table_settings;
  }

  /**
   *  Datenbanktabelle Name für Profiler-Historie setzen
   *  @param tableHistory
   */
  public void setName4TableHistory(String tableHistory) {
    table_history = tableHistory;
  }

  /**
   *  Datenbanktabelle Name für Profiler-Historie auslesen
   *  @return String
   */
  public String getName4TableHistory() {
    return table_history;
  }

  /**
   * Name des Eintrags für Sequenzzähler der Historieneinträge
   * in Tabelle profiler_settings setzen.
   *  @param historyId
   */
  public void setEntryHistoryId(String historyId) {
    entry_history_id = historyId;
  }

  /**
   * Name des Eintrags für Sequenzzähler der Historieneinträge
   * in Tabelle profiler_settings auslesen.
   *  @return String
   */
  public String getEntryHistoryId() {
    return entry_history_id;
  }

  /**
   *  Name des Eintrags für Sequenzzähler der Dialogschritte in Tabelle
   * profiler_settings setzen.
   *  @param entryHistoryStep
   */
  public void setEntryHistoryStep(String entryHistoryStep) {
    entry_history_step = entryHistoryStep;
  }

  /**
   *  Name des Eintrags für Sequenzzähler der Dialogschritte in Tabelle
   *  profiler_settings auslesen.
   *  @return String
   */
  public String getEntryHistoryStep() {
    return entry_history_step;
  }

}