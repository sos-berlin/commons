package sos.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import com.sos.exception.SOSMissingDataException;

public class SOSKeyStoreReader {

    public enum Type {
        KeyStrore, TrustStore, KeyTrustStore
    }

    private static final String SYSTEM_PROPERTY_KEYSTORE_PATH = "javax.net.ssl.keyStore";
    private static final String SYSTEM_PROPERTY_TRUSTSTORE_PATH = "javax.net.ssl.trustStore";

    private static final String SYSTEM_PROPERTY_KEYSTORE_PASSWORD = "javax.net.ssl.keyStorePassword";
    private static final String SYSTEM_PROPERTY_TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword";

    private final Type type;
    private final String storeType;

    private Path path;
    private String password;

    public SOSKeyStoreReader(Type type) {
        this(type, null, null, null);
    }

    public SOSKeyStoreReader(Type type, Path path) {
        this(type, path, null, null);
    }

    public SOSKeyStoreReader(Type type, Path path, String password) {
        this(type, path, password, null);
    }

    public SOSKeyStoreReader(Type type, Path path, String password, String storeType) {
        this.type = type;
        this.path = path;
        this.password = password;
        this.storeType = storeType == null ? KeyStore.getDefaultType() : storeType;
    }

    public KeyStore read() throws SOSMissingDataException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
        switch (type) {
        case KeyStrore:
            if (path == null) {
                String p = System.getProperty(SYSTEM_PROPERTY_KEYSTORE_PATH);
                if (!SOSString.isEmpty(p)) {
                    path = Paths.get(p);
                }
            }
            if (password == null) {
                String p = System.getProperty(SYSTEM_PROPERTY_KEYSTORE_PASSWORD);
                if (p != null) {
                    password = p;
                }
            }
            break;
        case TrustStore:
            if (path == null) {
                String p = System.getProperty(SYSTEM_PROPERTY_TRUSTSTORE_PATH);
                if (!SOSString.isEmpty(p)) {
                    path = Paths.get(p);
                }
            }
            if (password == null) {
                String p = System.getProperty(SYSTEM_PROPERTY_TRUSTSTORE_PASSWORD);
                if (p != null) {
                    password = p;
                }
            }
            break;
        case KeyTrustStore:
            if (path == null) {
                String p = System.getProperty(SYSTEM_PROPERTY_KEYSTORE_PATH);
                if (SOSString.isEmpty(p)) {
                    p = System.getProperty(SYSTEM_PROPERTY_TRUSTSTORE_PATH);
                }
                if (!SOSString.isEmpty(p)) {
                    path = Paths.get(p);
                }
            }
            if (password == null) {
                String p = System.getProperty(SYSTEM_PROPERTY_KEYSTORE_PASSWORD);
                if (p == null) {
                    p = System.getProperty(SYSTEM_PROPERTY_TRUSTSTORE_PASSWORD);
                }
                if (p != null) {
                    password = p;
                }
            }
            break;
        }
        if (path == null) {
            return null;
        } else {
            try (InputStream is = Files.newInputStream(path)) {
                KeyStore ks = KeyStore.getInstance(storeType);
                ks.load(is, getPassword());
                return ks;
            } catch (Throwable e) {
                throw new IOException(String.format("[%s][%s]%s", path, storeType, e.toString()), e);
            }
        }
    }

    public char[] getPassword() {
        return password == null ? (char[]) null : password.toCharArray();
    }

    public Path getPath() {
        return path;
    }
}
