/*
 * SOSConnectionVersionLimiter.java Created on 07.06.2006
 */
package sos.connection;

import java.sql.DatabaseMetaData;
import java.util.HashSet;

import sos.util.SOSLogger;

/** @author Andreas Liebert */
public class SOSConnectionVersionLimiter {

    private HashSet excludedVersions = new HashSet();
    private HashSet supportedVersions = new HashSet();
    private DBVersion minSupportedVersion = null;
    private DBVersion maxSupportedVersion = null;
    private DBVersion excludedFromVersion = null;
    private DBVersion excludedThroughVersion = null;
    public static final int CHECK_OFF = 0;
    public static final int CHECK_NORMAL = 1;
    public static final int CHECK_STRICT = 2;
    public static final int VERSION_EXCLUDED = 0;
    public static final int VERSION_UNTESTED = 1;
    public static final int VERSION_SUPPORTED = 2;

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
                return this.getMajorVersion() == other.getMajorVersion() && this.getMinorVersion() == other.getMinorVersion();
            }
            return super.equals(obj);
        }

        public int compareTo(Object o) {
            DBVersion other = (DBVersion) o;
            if (other.getMajorVersion() > this.getMajorVersion()) {
                return -1;
            } else if (other.getMajorVersion() < this.getMajorVersion()) {
                return 1;
            } else if (other.getMinorVersion() > this.getMinorVersion()) {
                return -1;
            } else if (other.getMinorVersion() < this.getMinorVersion()) {
                return 1;
            }
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

        private static final long serialVersionUID = -5129710619578262940L;
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

        public int getReason() {
            return reason;
        }

        public String getMessage() {
            return message;
        }

    }

    public void addExcludedVersion(int majorVersion, int minorVersion) {
        DBVersion version = new DBVersion(majorVersion, minorVersion);
        excludedVersions.add(version);
    }

    public void addExcludedVersion(String version) {
        excludedVersions.add(version);
    }

    public void addSupportedVersion(int majorVersion, int minorVersion) {
        DBVersion version = new DBVersion(majorVersion, minorVersion);
        supportedVersions.add(version);
    }

    public void addSupportedVersion(String version) {
        supportedVersions.add(version);
    }

    public void setMaxSupportedVersion(int majorVersion, int minorVersion) {
        this.maxSupportedVersion = new DBVersion(majorVersion, minorVersion);
    }

    public void setExcludedThroughVersion(int majorVersion, int minorVersion) {
        this.excludedThroughVersion = new DBVersion(majorVersion, minorVersion);
    }

    public void setMinSupportedVersion(int majorVersion, int minorVersion) {
        this.minSupportedVersion = new DBVersion(majorVersion, minorVersion);
    }

    public void setExcludedFromVersion(DBVersion excludedFromVersion) {
        this.excludedFromVersion = excludedFromVersion;
    }

    public int check(SOSConnection conn, SOSLogger log) throws BadDatabaseVersionException {
        return check(conn, log, conn.getCompatibility());
    }

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
            if (major == -1 && sDbVersion.isEmpty()) {
                throw new Exception("Failed to get any version information from" + " the database: " + lastError);
            }
            if (major != -1) {
                db = new DBVersion(major, minor);
            } else {
                try {
                    major = conn.parseMajorVersion(sDbVersion);
                    minor = conn.parseMinorVersion(sDbVersion);
                    conn.setMajorVersion(major);
                    conn.setMinorVersion(minor);
                    db = new DBVersion(major, minor);
                } catch (Exception e) {
                    log.info("Error occured: " + e);
                }
            }
            if (db != null) {
                shortVersion = db.toString();
            } else {
                shortVersion = sDbVersion;
            }
            excluded = isExcludedVersion(db, sDbVersion);
            untested = isUntestedVersion(db, sDbVersion);
            log.debug3("Excluded Database Version: " + excluded);
            log.debug3("Untested Database Version: " + untested);
        } catch (Exception e) {
            try {
                if (compatibility != CHECK_OFF) {
                    log.warn("Error occured checking database version: " + e);
                } else {
                    log.info("Error occured checking database version: " + e);
                }
            } catch (Exception ex) {
                //
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
            if (excludedVersions.contains(db)) {
                return true;
            }
            if (supportedVersions.contains(db)) {
                return false;
            }
            if (excludedFromVersion != null && excludedFromVersion.compareTo(db) <= 0) {
                rc = true;
            }
            if (excludedThroughVersion != null && excludedThroughVersion.compareTo(db) >= 0) {
                rc = true;
            }
        }
        if (sDbVersion != null) {
            if (excludedVersions.contains(sDbVersion)) {
                return true;
            }
            if (supportedVersions.contains(sDbVersion)) {
                return false;
            }
        }
        return rc;
    }

    private boolean isUntestedVersion(DBVersion db, String sDbVersion) {
        boolean rc = false;
        if (db != null) {
            if (supportedVersions.contains(db)) {
                return false;
            }
            if (minSupportedVersion != null && minSupportedVersion.compareTo(db) > 0) {
                rc = true;
            }
            if (maxSupportedVersion != null && maxSupportedVersion.compareTo(db) < 0) {
                rc = true;
            }
        }
        if (sDbVersion != null && supportedVersions.contains(sDbVersion)) {
            return false;
        }
        return rc;
    }

}
