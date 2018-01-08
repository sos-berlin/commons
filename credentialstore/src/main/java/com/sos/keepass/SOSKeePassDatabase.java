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

public class SOSKeePassDatabase {

    /* see KdbDatabase constructor: KDB files don't have a single root group, this is a synthetic surrogate */
    public static final String KDB_ROOT_GROUP_NAME = "Root";
    public static final String KDB_GROUP_TITLE = "Meta-Info";

    private Path _file;
    private boolean _isKdbx;
    private Database<?, ?, ?, ?> _database;

    public SOSKeePassDatabase(final Path file) throws Exception {
        if (file == null) {
            throw new Exception("file is null");
        }
        _file = file;
        _isKdbx = file.toString().toLowerCase().endsWith(".kdbx");
    }

    public void load(final String password) throws Exception {
        load(password, null);
    }

    public void load(final String password, final Path keyFile) throws Exception {
        if (_isKdbx) {
            _database = getKDBXDatabase(password, keyFile);
        } else {
            _database = getKDBDatabase(password, keyFile);
        }
    }

    public List<? extends Entry<?, ?, ?, ?>> getEntries() {
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

    public List<? extends Entry<?, ?, ?, ?>> getEntriesByTitle(final String regex) {
        if (_database == null) {
            return null;
        }
        return _database.findEntries(new Entry.Matcher() {

            public boolean matches(@SuppressWarnings("rawtypes") Entry entry) {
                return entry.getTitle().matches(regex);
            }
        });
    }

    public List<? extends Entry<?, ?, ?, ?>> getEntriesByUsername(final String regex) {
        if (_database == null) {
            return null;
        }
        return _database.findEntries(new Entry.Matcher() {

            public boolean matches(@SuppressWarnings("rawtypes") Entry entry) {
                return entry.getUsername().matches(regex);
            }
        });
    }

    public List<? extends Entry<?, ?, ?, ?>> getEntriesByUrl(final String regex) {
        if (_database == null) {
            return null;
        }
        return _database.findEntries(new Entry.Matcher() {

            public boolean matches(@SuppressWarnings("rawtypes") Entry entry) {
                return entry.getUrl().matches(regex);
            }
        });
    }

    public Entry<?, ?, ?, ?> getEntryByPath(final String path) throws Exception {
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
    public byte[] getAttachment(final String entryPath) throws Exception {
        return getAttachment(getEntryByPath(entryPath), null);
    }

    /** V1 KDB - returns the attachment data, V2 KDBX - returns the attachment data of the attachmentName */
    public byte[] getAttachment(final String entryPath, final String attachmentName) throws Exception {
        return getAttachment(getEntryByPath(entryPath), attachmentName);
    }

    /** V1 KDB - returns the attachment data, V2 KDBX - returns the first attachment data */
    public byte[] getAttachment(final Entry<?, ?, ?, ?> entry) throws Exception {
        return getAttachment(entry, null);
    }

    /** V1 KDB - returns the attachment data, V2 KDBX - returns the attachment data of the attachmentName */
    public byte[] getAttachment(final Entry<?, ?, ?, ?> entry, final String attachmentName) throws Exception {
        if (entry == null) {
            throw new Exception("entry is null");
        }
        byte[] data = null;

        if (_isKdbx) {
            if (attachmentName == null) {
                List<String> l = entry.getBinaryPropertyNames();
                if (l != null && l.size() > 0) {
                    data = entry.getBinaryProperty(l.get(0));
                }
            } else {
                data = entry.getBinaryProperty(attachmentName);
            }
        } else {
            data = ((KdbEntry) entry).getBinaryData();
        }
        if (data == null) {
            throw new Exception(String.format("[%s]not found attachment=%s", entry.getPath(), attachmentName));
        }
        return data;
    }

    /** V1 KDB - exports the attachment, V2 KDBX - exports the first attachment */
    public void exportAttachment2File(final String entryPath, final Path targetFile) throws Exception {
        exportAttachment2File(entryPath, targetFile, null);
    }

    /** V1 KDB - exports the attachment, V2 KDBX - exports the attachment of the attachmentName */
    public void exportAttachment2File(final String entryPath, final Path targetFile, final String attachmentName) throws Exception {
        exportAttachment2File(getEntryByPath(entryPath), targetFile, attachmentName);
    }

    /** V1 KDB - exports the attachment, V2 KDBX - exports the first attachment */
    public void exportAttachment2File(final Entry<?, ?, ?, ?> entry, final Path targetFile) throws Exception {
        exportAttachment2File(entry, targetFile, null);
    }

    /** V1 KDB - exports the attachment, V2 KDBX - exports the attachment of the attachmentName */
    public void exportAttachment2File(final Entry<?, ?, ?, ?> entry, final Path targetFile, final String attachmentName) throws Exception {
        byte[] data = getAttachment(entry, attachmentName);

        try (FileOutputStream fos = new FileOutputStream(targetFile.toFile())) {
            fos.write(data);
        } catch (Exception e) {
            throw new Exception(String.format("[%s][%s][%s]can't write attachment to file: %s", entry.getPath(), attachmentName, targetFile, e
                    .toString()), e);
        }
    }

    private Credentials getKDBCredentials(final String pass, final Path keyFile) throws Exception {
        Credentials cred = null;
        String password = pass == null ? "" : pass;
        if (keyFile == null) {
            cred = new KdbCredentials.Password(password.getBytes());
        } else {
            InputStream is = null;
            try {
                is = new FileInputStream(keyFile.toFile());
                cred = new KdbCredentials.KeyFile(password.getBytes(), is);
            } catch (Exception e) {
                throw e;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return cred;
    }

    private KdbDatabase getKDBDatabase(final String pass, final Path keyFile) throws Exception {
        Credentials cred = getKDBCredentials(pass, keyFile);
        KdbDatabase database = null;
        InputStream is = null;
        try {
            is = new FileInputStream(_file.toFile());
            database = KdbDatabase.load(cred, is);
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return database;
    }

    private Credentials getKDBXCredentials(final String pass, final Path keyFile) throws Exception {
        KdbxCreds cred = null;
        if (keyFile == null) {
            cred = new KdbxCreds(pass == null ? "".getBytes() : pass.getBytes());
        } else {
            InputStream is = null;
            try {
                is = new FileInputStream(keyFile.toFile());
                if (pass == null) {
                    cred = new KdbxCreds(is);
                } else {
                    cred = new KdbxCreds(pass.getBytes(), is);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return cred;
    }

    private SimpleDatabase getKDBXDatabase(final String pass, final Path keyFile) throws Exception {
        Credentials cred = getKDBXCredentials(pass, keyFile);
        SimpleDatabase database = null;
        InputStream is = null;
        try {
            is = new FileInputStream(_file.toFile());
            database = SimpleDatabase.load(cred, is);
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
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
