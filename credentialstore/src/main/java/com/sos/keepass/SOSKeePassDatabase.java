package com.sos.keepass;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.kdb.KdbCredentials;
import org.linguafranca.pwdb.kdb.KdbDatabase;
import org.linguafranca.pwdb.kdb.KdbEntry;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.util.SOSString;

public class SOSKeePassDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSKeePassDatabase.class);
    private static final boolean isDebugEnabled = LOGGER.isDebugEnabled();
    private static final boolean isTraceEnabled = LOGGER.isTraceEnabled();
    /* see KdbDatabase constructor: KDB files don't have a single root group, this is a synthetic surrogate */
    public static final String KDB_ROOT_GROUP_NAME = "Root";
    public static final String KDB_GROUP_TITLE = "Meta-Info";
    public static final String STANDARD_PROPERTY_NAME_ATTACHMENT = "Attachment";

    private Path _file;
    private boolean _isKdbx;
    private Database<?, ?, ?, ?> _database;

    public SOSKeePassDatabase(final Path file) throws SOSKeePassDatabaseException {
        if (file == null) {
            throw new SOSKeePassDatabaseException("KeePass file is null");
        }
        _file = file;
        _isKdbx = file.toString().toLowerCase().endsWith(".kdbx");
        if (isDebugEnabled) {
            LOGGER.debug(String.format("file=%s, isKdbx=%s", _file, _isKdbx));
        }
    }

    public void load(final String password) throws SOSKeePassDatabaseException {
        load(password, null);
    }

    public void load(final String password, final Path keyFile) throws SOSKeePassDatabaseException {
        if (isTraceEnabled) {
            String pass = password == null ? "" : "pass=?, ";
            LOGGER.trace(String.format("%skeyFile=%s", pass, keyFile));
        }
        if (_isKdbx) {
            _database = getKDBXDatabase(password, keyFile);
        } else {
            _database = getKDBDatabase(password, keyFile);
        }
    }

    public List<? extends Entry<?, ?, ?, ?>> getEntries() {
        LOGGER.debug("getEntries");

        if (_database == null) {
            return null;
        }
        if (_isKdbx) {
            return _database.findEntries("");
        } else {
            return _database.findEntries(new Entry.Matcher() {

                public boolean matches(@SuppressWarnings("rawtypes") Entry entry) {
                    return !entry.getTitle().equals(KDB_GROUP_TITLE);
                }
            });
        }
    }

    public List<? extends Entry<?, ?, ?, ?>> getEntriesByTitle(final String match) {
        if (isDebugEnabled) {
            LOGGER.debug(String.format("match=%s", match));
        }
        if (_database == null) {
            return null;
        }
        return _database.findEntries(new Entry.Matcher() {

            public boolean matches(@SuppressWarnings("rawtypes") Entry entry) {
                return entry.getTitle().matches(match == null ? "" : match);
            }
        });
    }

    public List<? extends Entry<?, ?, ?, ?>> getEntriesByUsername(final String match) {
        if (isDebugEnabled) {
            LOGGER.debug(String.format("match=%s", match));
        }
        if (_database == null) {
            return null;
        }
        return _database.findEntries(new Entry.Matcher() {

            public boolean matches(@SuppressWarnings("rawtypes") Entry entry) {
                return entry.getUsername().matches(match == null ? "" : match);
            }
        });
    }

    public List<? extends Entry<?, ?, ?, ?>> getEntriesByUrl(final String match) {
        if (isDebugEnabled) {
            LOGGER.debug(String.format("match=%s", match));
        }
        if (_database == null) {
            return null;
        }
        return _database.findEntries(new Entry.Matcher() {

            public boolean matches(@SuppressWarnings("rawtypes") Entry entry) {
                return entry.getUrl().matches(match == null ? "" : match);
            }
        });
    }

    public Entry<?, ?, ?, ?> getEntryByPath(final String path) {
        if (isDebugEnabled) {
            LOGGER.debug(String.format("path=%s", path));
        }
        if (_database == null || path == null) {
            return null;
        }
        List<? extends Entry<?, ?, ?, ?>> l = _database.findEntries(new Entry.Matcher() {

            public boolean matches(@SuppressWarnings("rawtypes") Entry entry) {
                String p = path.startsWith("/") ? path : "/" + path;
                return _isKdbx ? entry.getPath().equals(p) : entry.getPath().equals("/" + KDB_ROOT_GROUP_NAME + p);
            }
        });
        return l == null || l.size() == 0 ? null : l.get(0);
    }

    /** V1 KDB - returns the attachment data, V2 KDBX - returns the first attachment data */
    public byte[] getAttachment(final String entryPath) throws SOSKeePassDatabaseException {
        return getAttachment(getEntryByPath(entryPath), null);
    }

    /** V1 KDB - returns the attachment data, V2 KDBX - returns the attachment data of the propertyName */
    public byte[] getAttachment(final String entryPath, final String propertyName) throws SOSKeePassDatabaseException {
        return getAttachment(getEntryByPath(entryPath), propertyName);
    }

    /** V1 KDB - returns the attachment data, V2 KDBX - returns the first attachment data */
    public byte[] getAttachment(final Entry<?, ?, ?, ?> entry) throws SOSKeePassDatabaseException {
        return getAttachment(entry, null);
    }

    /** V1 KDB - returns the attachment data, V2 KDBX - returns the attachment data of the propertyName */
    public byte[] getAttachment(final Entry<?, ?, ?, ?> entry, String propertyName) throws SOSKeePassDatabaseException {
        if (entry == null) {
            throw new SOSKeePassDatabaseException("entry is null");
        }
        if (isDebugEnabled) {
            LOGGER.debug(String.format("entryPath=%s, propertyName=%s", entry.getPath(), propertyName));
        }
        if (propertyName != null && propertyName.equalsIgnoreCase(STANDARD_PROPERTY_NAME_ATTACHMENT)) {
            propertyName = null;
        }
        byte[] data = null;
        try {
            if (_isKdbx) {
                if (propertyName == null) {
                    List<String> l = entry.getBinaryPropertyNames();
                    if (l != null && l.size() > 0) {
                        data = entry.getBinaryProperty(l.get(0));
                    }
                } else {
                    data = entry.getBinaryProperty(propertyName);
                }
            } else {
                data = ((KdbEntry) entry).getBinaryData();
            }
        } catch (Throwable e) {
            throw new SOSKeePassDatabaseException(e);
        }
        if (data == null || data.length == 0) {
            if (propertyName == null) {
                throw new SOSKeePassDatabaseException(String.format("[%s]attachment not found or is 0 bytes", entry.getPath()));
            } else {
                throw new SOSKeePassDatabaseException(String.format("[%s]attachment not found or is 0 bytes, property=%s", entry.getPath(),
                        propertyName));
            }
        }
        return data;
    }

    /** V1 KDB - exports the attachment, V2 KDBX - exports the first attachment */
    public void exportAttachment2File(final String entryPath, final Path targetFile) throws SOSKeePassDatabaseException {
        exportAttachment2File(entryPath, targetFile, null);
    }

    /** V1 KDB - exports the attachment, V2 KDBX - exports the attachment of the propertyName */
    public void exportAttachment2File(final String entryPath, final Path targetFile, final String propertyName) throws SOSKeePassDatabaseException {
        exportAttachment2File(getEntryByPath(entryPath), targetFile, propertyName);
    }

    /** V1 KDB - exports the attachment, V2 KDBX - exports the first attachment */
    public void exportAttachment2File(final Entry<?, ?, ?, ?> entry, final Path targetFile) throws SOSKeePassDatabaseException {
        exportAttachment2File(entry, targetFile, null);
    }

    /** V1 KDB - exports the attachment, V2 KDBX - exports the attachment of the propertyName */
    public void exportAttachment2File(final Entry<?, ?, ?, ?> entry, final Path targetFile, final String propertyName)
            throws SOSKeePassDatabaseException {
        LOGGER.debug(String.format("entryPath=%s, targetFile=%s, propertyName=%s", entry.getPath(), targetFile, propertyName));

        byte[] data = getAttachment(entry, propertyName);

        try (FileOutputStream fos = new FileOutputStream(targetFile.toFile())) {
            fos.write(data);
        } catch (Throwable e) {
            throw new SOSKeePassDatabaseException(String.format("[%s][%s][%s]can't write attachment to file: %s", entry.getPath(), propertyName,
                    targetFile, e.toString()), e);
        }
    }

    public static String getPropertyName(String propertyName) {
        if (SOSString.isEmpty(propertyName)) {
            return propertyName;
        }
        switch (propertyName.toLowerCase()) {
        case "title":
            return Entry.STANDARD_PROPERTY_NAME_TITLE;
        case "user":
        case "username":
            return Entry.STANDARD_PROPERTY_NAME_USER_NAME;
        case "password":
            return Entry.STANDARD_PROPERTY_NAME_PASSWORD;
        case "url":
            return Entry.STANDARD_PROPERTY_NAME_URL;
        case "notes":
            return Entry.STANDARD_PROPERTY_NAME_NOTES;
        case "attach":
        case "attachment":
            return STANDARD_PROPERTY_NAME_ATTACHMENT;
        default:
            return propertyName;
        }
    }

    private Credentials getKDBCredentials(final String pass, final Path keyFile) throws SOSKeePassDatabaseException {
        Credentials cred = null;
        String password = pass == null ? "" : pass;
        if (keyFile == null) {
            try {
                cred = new KdbCredentials.Password(password.getBytes());
            } catch (Throwable e) {
                throw new SOSKeePassDatabaseException(e);
            }
        } else {
            InputStream is = null;
            try {
                is = new FileInputStream(keyFile.toFile());
                cred = new KdbCredentials.KeyFile(password.getBytes(), is);
            } catch (Throwable e) {
                throw new SOSKeePassDatabaseException(e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Throwable te) {
                    }
                }
            }
        }
        return cred;
    }

    private KdbDatabase getKDBDatabase(final String pass, final Path keyFile) throws SOSKeePassDatabaseException {
        Credentials cred = getKDBCredentials(pass, keyFile);
        KdbDatabase database = null;
        InputStream is = null;
        try {
            if (isDebugEnabled) {
                LOGGER.debug(String.format("KdbDatabase.load: file=%s", _file));
            }
            is = new FileInputStream(_file.toFile());
            database = KdbDatabase.load(cred, is);
        } catch (Throwable e) {
            throw new SOSKeePassDatabaseException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable te) {
                }
            }
        }
        return database;
    }

    private Credentials getKDBXCredentials(final String pass, final Path keyFile) throws SOSKeePassDatabaseException {
        KdbxCreds cred = null;
        if (keyFile == null) {
            try {
                cred = new KdbxCreds(pass == null ? "".getBytes() : pass.getBytes());
            } catch (Throwable e) {
                throw new SOSKeePassDatabaseException(e);
            }
        } else {
            InputStream is = null;
            try {
                is = new FileInputStream(keyFile.toFile());
                if (pass == null) {
                    cred = new KdbxCreds(is);
                } else {
                    cred = new KdbxCreds(pass.getBytes(), is);
                }
            } catch (Throwable e) {
                throw new SOSKeePassDatabaseException(e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Throwable te) {
                    }
                }
            }
        }
        return cred;
    }

    private SimpleDatabase getKDBXDatabase(final String pass, final Path keyFile) throws SOSKeePassDatabaseException {
        Credentials cred = getKDBXCredentials(pass, keyFile);
        SimpleDatabase database = null;
        InputStream is = null;
        try {
            if (isDebugEnabled) {
                LOGGER.debug(String.format("SimpleDatabase.load: file=%s", _file));
            }
            is = new FileInputStream(_file.toFile());
            database = SimpleDatabase.load(cred, is);
        } catch (Throwable e) {
            throw new SOSKeePassDatabaseException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable te) {
                }
            }
        }
        return database;
    }

    public Path getFile() {
        return _file;
    }

    public Database<?, ?, ?, ?> getDatabase() {
        return _database;
    }

    public boolean isKDBX() {
        return _isKdbx;
    }

}
