package sos.settings;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @resource sos.util.jar
 * @version 1.0
 * @author <a href="mailto:andreas.pueschel@sos-berlin.com">Andreas Püschel</a>
 * @since 2005-01-25
 * @version 1.1
 */

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSString;

public abstract class SOSSettings {
  
  /** Name der Datenquelle ( Tabelle oder Dateiname) */
  protected String source               = "";

  /** Sektionname */
  protected String section              = "";
  
  /** logger objekt */
  protected SOSLogger logger;

  /** Bezeichnung des Names der Einstellung */
  protected String entry                = "";

  /** Voreinstellung für created_name, modified_name bei Änderung von Einstellungen */
  protected String author               = "SOS";
  
  /** Datenquelle: Array der Tabellen und Titel */
  protected Hashtable sources           = new Hashtable();
  
  protected String entryApplication     = "APPLICATION";
  
  /** Feldname für die Sektion eines Eintrags */
  protected String entrySection         = "SECTION";

  /** Feldname für den Namen eines Eintrags */
  protected String entryName            = "NAME";
  
  /** Feldname für den Wert eines Eintrags */
  protected String entryValue           = "VALUE";
  
  /** Feldname für den Titel eines Eintrags */
  protected String entryTitle           = "TITLE";

  /** Groß-/Kleinschreibung berücksichtigen */
  protected boolean ignoreCase          = false;
  

  /**
   * Konstruktor
   *
   * @param source Name der Datenquelle
   * @param logger Das Logger-Objekt
   *
   * @throws java.lang.Exception
   */
  public SOSSettings( String source ) throws Exception {
    if ( SOSString.isEmpty(source))
      throw (new Exception(SOSClassUtil.getMethodName() +
                           " invalid source name !!."));
    this.source = source;
  }


  /**
   * Konstruktor
   *
   * @param source Name der Datenquelle
   * @param logger Das Logger-Objekt
   *
   * @throws java.lang.Exception
   */
  public SOSSettings( String source, SOSLogger logger ) throws Exception {
    if ( SOSString.isEmpty(source))
      throw (new Exception(SOSClassUtil.getMethodName() +
                           " invalid source name !!."));
    if (logger == null)
      throw (new Exception(SOSClassUtil.getMethodName() +
                           " invalid logger object !!."));
    this.logger = logger;
    this.source = source;
  }


  /**
   * Konstruktor
   *
   * @param source Name der Datenquelle
   * @param section Name der Sektion
   * @param logger Das Logger-Objekt
   *
   * @throws java.lang.Exception
   */
  public SOSSettings( String source,
                      String section
                     ) throws Exception {

    if ( SOSString.isEmpty(source))
      throw (new Exception(SOSClassUtil.getMethodName() +
                           " invalid source name !!."));

    if ( SOSString.isEmpty(section))
      throw (new Exception(SOSClassUtil.getMethodName() +
                           " invalid section name !!."));

    this.source = source;
    this.section = section;

  }


  /**
   * Konstruktor
   *
   * @param source Name der Datenquelle
   * @param section Name der Sektion
   * @param logger Das Logger-Objekt
   *
   * @throws java.lang.Exception
   */
  public SOSSettings( String source,
                      String section,
                      SOSLogger logger
                     ) throws Exception {

    if ( SOSString.isEmpty(source))
      throw (new Exception(SOSClassUtil.getMethodName() +
                           " invalid source name !!."));

    if ( SOSString.isEmpty(section))
      throw (new Exception(SOSClassUtil.getMethodName() +
                           " invalid section name !!."));

    if (logger == null) {
      throw (new Exception(SOSClassUtil.getMethodName() + " sosLogger object must not be null."));
    }
    this.source = source;
    this.section = section;
    this.logger = logger;

  }


  /**
   * Liefert alle Einträge einer Sektion zurück.
   *
   * @return Properties Objekt, das alle Einträge der Sektion darstellt.
   * @throws java.lang.Exception
   * @see #getSections
   */
  public abstract Properties getSection() throws Exception;


  /**
   * Liefert alle Einträge der eingegebenen Sektion zurück.
   *
   * @return Properties Objekt, das alle Einträge der Sektion darstellt.
   * @throws java.lang.Exception
   * @see #getSections
   */
  public abstract Properties getSection( String section ) throws Exception;


  /**
   * Liefert alle Einträge der eingegebenen Sektion zurück.
   *
   * @return Properties Objekt, das alle Einträge der Sektion darstellt.
   * @throws java.lang.Exception
   * @see #getSections
   */
  public abstract Properties getSection( String application, String section ) throws Exception;



  /**
   * Liefert alle Sektionen einer Anwendung zurück
   *
   * @return ArrayList die alle Sektionen beinhaltet
   * @throws java.lang.Exception
   */
  public abstract ArrayList getSections() throws Exception;


  /**
   * Liefert den wert eines Eintrages zurück.
   *
   * @param entry Name des Eintrages
   * @return String der Wert eines Eintrages
   * @throws java.lang.Exception
   */
  public abstract String getSectionEntry(String entry) throws Exception;


  /**
   * Aktiviert die Kleinschreibung für Feldnamen
   *
   * @see #setKeysToUpperCase
   */
  public abstract void setKeysToLowerCase() throws Exception;


  /**
   * Aktiviert die Kleinschreibung für Feldnamen
   *
   * @see #setKeysToLowerCase
   */
  public abstract void setKeysToUpperCase() throws Exception;


  /**
   * Setzt den Schalter für die Berücksichtigung von Groß/Kleinschreibung
   * @param ignoreCase
   */
  public abstract void setIgnoreCase( boolean ignoreCase );
  
  /**
   * liefert den Schalter für die Berücksichtigung von Groß/Kleinschreibung
   * @param ignoreCase
   */
  public abstract boolean getIgnoreCase();
}
