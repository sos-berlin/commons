/*
 * SOSConnectionVersionLimiter.java Created on 07.06.2006
 */
package sos.connection;

import java.sql.DatabaseMetaData;
import java.util.HashSet;

import sos.util.SOSLogger;

/** * helps to check if a database version is unsupported, untested or supported.
 * 
 * @author Andreas Liebert */
public class SOSConnectionVersionLimiter {

    public class DBVersion implements Comparable {

        private int majorVersion;
        private int minorVersion;

        public DBVersion(int majorVersion, int minorVersion) {
            this.majorVersion = majorVersion;
            this.minorVersion = minorVersion;
        }

        public int getMajorVersion() {
            return majorVersion;
        }

        public int getMinorVersion() {
            return minorVersion;
        }

        public boolean equals(Object obj) {
            if (obj instanceof DBVersion) {
                DBVersion other = (DBVersion) obj;
                return (this.getMajorVersion() == other.getMajorVersion() && this.getMinorVersion() == other.getMinorVersion());
            }
            return super.equals(obj);
        }

        public int compareTo(Object o) {
            DBVersion other = (DBVersion) o;
            if (other.getMajorVersion() > this.getMajorVersion())
                return -1;
            if (other.getMajorVersion() < this.getMajorVersion())
                return 1;
            if (other.getMinorVersion() > this.getMinorVersion())
                return -1;
            if (other.getMinorVersion() < this.getMinorVersion())
                return 1;
            return 0;
        }

        public int hashCode() {
            return majorVersion * 100 + minorVersion;
        }

        public String toString() {
            return getMajorVersion() + "." + getMinorVersion();
        }
    }

    public class BadDatabaseVersionException extends Exception {

        private int reason = VERSION_EXCLUDED;
        private String message = "";

        public BadDatabaseVersionException(int reason, String version) {
            this.reason = reason;
            if (reason == VERSION_EXCLUDED) {
                message = "This database Version (" + version + ") is an " + "excluded version.";
            }
            if (reason == VERSION_UNTESTED) {
                message = "This database Version (" + version + ") is an " + "untested version.";
            }
        }

        /** @return eine der Konstanten:<br/>
         *         VERSION_EXCLUDED<br/>
         *         VERSION_UNTESTED */
        public int getReason() {
            return reason;
        }

        public String getMessage() {
            return message;
        }

    }

    // Konstanten für Kompatibilitätslevel
    public static final int CHECK_OFF = 0;
    public static final int CHECK_NORMAL = 1;
    public static final int CHECK_STRICT = 2;

    // Konstanten für Resultate der check funktion
    public static final int VERSION_EXCLUDED = 0;
    public static final int VERSION_UNTESTED = 1;
    public static final int VERSION_SUPPORTED = 2;

    private HashSet excludedVersions = new HashSet();

    private HashSet supportedVersions = new HashSet();

    /** kleinste unterstützte Version */
    private DBVersion minSupportedVersion = null;

    /** größte unterstützte Version */
    private DBVersion maxSupportedVersion = null;

    /** grösste nicht funktionierende Version alle Versionen oberhalb und
     * inclusive dieser Version werden als nicht funktionierend angenommen */
    private DBVersion excludedFromVersion = null;

    /** kleinste nicht funktionierende Version alle Versionen unterhalb und
     * inclusiver dieser Version werden als nicht funktionierend angenommen */
    private DBVersion excludedThroughVersion = null;

    /** Fügt eine bekannterweise nicht funktionierende Version hinzu. Diese wird
     * auch dann als nicht funktionierend angesehen, wenn sie sich im Bereich
     * der unterstützten Versionen befindet.
     * 
     * @param majorVersion
     * @param minorVersion */
    public void addExcludedVersion(int majorVersion, int minorVersion) {
        DBVersion version = new DBVersion(majorVersion, minorVersion);
        excludedVersions.add(version);
    }

    /** Fügt eine bekannterweise nicht funktionierende Version hinzu. Diese wird
     * auch dann als nicht funktionierend angesehen, wenn sie sich im Bereich
     * der unterstützten Versionen befindet. */
    public void addExcludedVersion(String version) {
        excludedVersions.add(version);
    }

    /** Fügt eine bekannterweise funktionierende Version hinzu. Diese wird auch
     * dann als funktionierend angesehen, wenn sie sich im Bereich der excluded
     * oder unsupported Versions befindet.
     * 
     * @param majorVersion
     * @param minorVersion */
    public void addSupportedVersion(int majorVersion, int minorVersion) {
        DBVersion version = new DBVersion(majorVersion, minorVersion);
        supportedVersions.add(version);
    }

    /** Fügt eine bekannterweise funktionierende Version hinzu. Diese wird auch
     * dann als funktionierend angesehen, wenn sie sich im Bereich der excluded
     * oder unsupported Versions befindet. */
    public void addSupportedVersion(String version) {
        supportedVersions.add(version);
    }

    /** Setzt die höchste unterstützte Version. Alle Versionen oberhalb dieser
     * Version gelten als nicht getestet. */
    public void setMaxSupportedVersion(int majorVersion, int minorVersion) {
        this.maxSupportedVersion = new DBVersion(majorVersion, minorVersion);
    }

    /** Setzt die höchste nicht unterstützte Version. Alle Versionen unterhalb
     * und inklusive dieser Version gelten als nicht unterstützt. */
    public void setExcludedThroughVersion(int majorVersion, int minorVersion) {
        this.excludedThroughVersion = new DBVersion(majorVersion, minorVersion);
    }

    /** Setzt die kleinste unterstützte Version. Alle Versionen unterhalb dieser
     * Version gelten als nicht getestet. */
    public void setMinSupportedVersion(int majorVersion, int minorVersion) {
        this.minSupportedVersion = new DBVersion(majorVersion, minorVersion);
    }

    /** Setzt die kleinste nicht unterstützte Version. Alle Versionen oberhalb
     * dieser Version gelten als nicht unterstützt. */
    public void setExcludedFromVersion(DBVersion excludedFromVersion) {
        this.excludedFromVersion = excludedFromVersion;
    }

    /** Prüft ob eine Connection aufgrund der Versionsnummer ihrer Datenbank
     * benutzt werden darf. Die Datenbank muss bereits verbunden sein. Der
     * Kompatibilitätslevel wird aus der Connection gelesen
     * 
     * @param conn SOSConnection, die geprüft werden soll
     * @param log SOSLogger Objekt
     * @return Wenn es keine Exception gab, wird je nach Ausgang der Prüfung
     *         eine der folgenden Konstanten geliefert:<br/>
     *         VERSION_EXCLUDED - Die Datenbankversion wird nicht unterstützt<br/>
     *         VERSION_UNTESTED - Die Datenbankversion wurde nicht getestet<br/>
     *         VERSION_SUPPORTED - Die Datenbankversion wird unterstützt */
    public int check(SOSConnection conn, SOSLogger log) throws BadDatabaseVersionException {
        return check(conn, log, conn.getCompatibility());
    }

    /** Prüft ob eine Connection aufgrund der Versionsnummer ihrer Datenbank
     * benutzt werden darf. Die Datenbank muss bereits verbunden sein.
     * 
     * @param conn SOSConnection, die geprüft werden soll
     * @param log SOSLogger Objekt
     * @param compatibility Eine der Konstanten:<br/>
     *            CHECK_OFF - Es wird nur auf info Level geloggt, keine
     *            Exception wird geworfen <br/>
     *            CHECK_NORMAL - Bei nicht unterstützten Versionen wird eine
     *            Exception geworfen <br/>
     *            CHECK_STRICT - Bei nicht unterstützten und nicht getesteten
     *            Versionen wird eine Exception geworfen
     * @return Wenn es keine Exception gab, wird je nach Ausgang der Prüfung
     *         eine der folgenden Konstanten geliefert:<br/>
     *         VERSION_EXCLUDED - Die Datenbankversion wird nicht unterstützt<br/>
     *         VERSION_UNTESTED - Die Datenbankversion wurde nicht getestet<br/>
     *         VERSION_SUPPORTED - Die Datenbankversion wird unterstützt */
    public int check(SOSConnection conn, SOSLogger log, int compatibility) throws BadDatabaseVersionException {
        int major = -1;
        int minor = 0;
        boolean excluded = false;
        boolean untested = false;
        String sDbVersion = "";
        DBVersion db = null;
        Exception lastError = null;
        String shortVersion = "";
        try {
            DatabaseMetaData meta = conn.getConnection().getMetaData();
            try {
                major = meta.getDatabaseMajorVersion();
                minor = meta.getDatabaseMinorVersion();
                conn.setMajorVersion(major);
                conn.setMinorVersion(minor);
                log.debug3("DatabaseMajorVersion: " + major);
                log.debug3("DatabaseMinorVersion: " + minor);
            } catch (AbstractMethodError ex) {
                lastError = new Exception(ex);
            } catch (Exception ex) {
                lastError = ex;
            }
            try {
                sDbVersion = meta.getDatabaseProductVersion();
                conn.setProductVersion(sDbVersion);
                log.debug3("DatabaseProductVersion: " + sDbVersion);
            } catch (Exception ex) {
                lastError = ex;
            }
            if (major == -1 && sDbVersion.length() == 0)
                throw new Exception("Failed to get any version information from" + " the database: " + lastError);
            if (major != -1) {
                db = new DBVersion(major, minor);
            } else {// String ist also da, parsen versuchen
                try {
                    /*
                     * String sDbVersion2 = sDbVersion; // Bei Oracle
                     * funktioniert je nach jdbc Treiber
                     * getDatabaseMajorVersion() // nicht. sDBVersion enthält
                     * sehr langen String mit zu vielen Zahlen
                     * if(sDbVersion2.startsWith("Oracle")){ String[] oraSplit =
                     * sDbVersion2.split("Release"); if (oraSplit.length>1)
                     * sDbVersion2=oraSplit[2]; } sDbVersion2 =
                     * sDbVersion2.replaceAll("[^0-9\\.]",""); String[] split =
                     * sDbVersion2.split("\\."); if (split.length<=2) throw new
                     * Exception();
                     */
                    major = conn.parseMajorVersion(sDbVersion);
                    minor = conn.parseMinorVersion(sDbVersion);
                    conn.setMajorVersion(major);
                    conn.setMinorVersion(minor);
                    db = new DBVersion(major, minor);
                } catch (Exception e) {
                    log.info("Error occured: " + e);
                }
            }
            if (db != null)
                shortVersion = db.toString();
            else
                shortVersion = sDbVersion;

            // Versionsinformationen liegen vor, ab hier vergleichen
            excluded = isExcludedVersion(db, sDbVersion);
            untested = isUntestedVersion(db, sDbVersion);
            log.debug3("Excluded Database Version: " + excluded);
            log.debug3("Untested Database Version: " + untested);

        } catch (Exception e) {
            try {
                if (compatibility != CHECK_OFF)
                    log.warn("Error occured checking database version: " + e);
                else
                    log.info("Error occured checking database version: " + e);
            } catch (Exception ex) {
            }
        }
        if (excluded) {
            if (compatibility == CHECK_STRICT || compatibility == CHECK_NORMAL) {
                throw new BadDatabaseVersionException(VERSION_EXCLUDED, shortVersion);
            }
            return SOSConnectionVersionLimiter.VERSION_EXCLUDED;
        }
        if (untested) {
            if (compatibility == CHECK_STRICT) {
                throw new BadDatabaseVersionException(VERSION_UNTESTED, shortVersion);
            }
            return SOSConnectionVersionLimiter.VERSION_UNTESTED;
        }
        return SOSConnectionVersionLimiter.VERSION_SUPPORTED;
    }

    private boolean isExcludedVersion(DBVersion db, String sDbVersion) {
        boolean rc = false;
        if (db != null) {
            if (excludedVersions.contains(db))
                return true;
            if (supportedVersions.contains(db))
                return false;
            if (excludedFromVersion != null) {
                if (excludedFromVersion.compareTo(db) <= 0)
                    rc = true;
            }
            if (excludedThroughVersion != null) {
                if (excludedThroughVersion.compareTo(db) >= 0)
                    rc = true;
            }
        }
        if (sDbVersion != null) {
            if (excludedVersions.contains(sDbVersion))
                return true;
            if (supportedVersions.contains(sDbVersion))
                return false;
        }
        return rc;
    }

    private boolean isUntestedVersion(DBVersion db, String sDbVersion) {
        boolean rc = false;
        if (db != null) {
            if (supportedVersions.contains(db))
                return false;
            if (minSupportedVersion != null) {
                if (minSupportedVersion.compareTo(db) > 0)
                    rc = true;
            }
            if (maxSupportedVersion != null) {
                if (maxSupportedVersion.compareTo(db) < 0)
                    rc = true;
            }
        }
        if (sDbVersion != null) {
            if (supportedVersions.contains(sDbVersion))
                return false;
        }
        return rc;
    }
}
