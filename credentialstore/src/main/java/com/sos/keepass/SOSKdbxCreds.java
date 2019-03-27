package com.sos.keepass;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.security.MessageDigest;

import org.jetbrains.annotations.NotNull;
import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.security.Encryption;

import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;

public class SOSKdbxCreds implements Credentials {

    private byte[] key;

    public SOSKdbxCreds() {
    }

    public void load(String password, Path keyFile) throws SOSKeePassDatabaseException {
        if (keyFile == null) {
            try {
                handlePassword(password);
            } catch (Throwable e) {
                throw new SOSKeePassDatabaseException(e);
            }
        } else {
            FileInputStream is = null;
            try {
                is = new FileInputStream(keyFile.toFile());
                boolean isXmlKey = isXmlKey(is);

                if (password == null) {
                    if (isXmlKey) {
                        handleXmlKey(is);
                    } else {
                        handleBinaryKey(is);
                    }
                } else {
                    if (isXmlKey) {
                        handleXmlKey(password, is);
                    } else {
                        handleBinaryKey(password, is);
                    }
                }

            } catch (Throwable e) {
                throw new SOSKeePassDatabaseException(String.format("[%s]%s", SOSKeePassDatabase.getFilePath(keyFile), e.toString()), e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Throwable te) {
                    }
                }
            }
        }
    }

    private boolean isXmlKey(@NotNull FileInputStream is) throws Exception {
        FileChannel fc = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(5);

            fc = is.getChannel();
            fc.read(buffer, 0);
            return new String(buffer.array()).trim().toLowerCase().startsWith("<?xml");
        } catch (Exception e) {
            throw e;
        } finally {
            if (fc != null) {
                // fc.position(0);
            }
        }
    }

    private void handlePassword(String password) {
        key = new KdbxCreds(password == null ? "".getBytes() : password.getBytes()).getKey();
    }

    private void handleXmlKey(@NotNull InputStream keyFileInputStream) {
        key = new KdbxCreds(keyFileInputStream).getKey();
    }

    private void handleXmlKey(@NotNull String password, @NotNull InputStream keyFileInputStream) {
        key = new KdbxCreds(password.getBytes(), keyFileInputStream).getKey();
    }

    private void handleBinaryKey(@NotNull InputStream keyFileInputStream) {
        MessageDigest md = Encryption.getMessageDigestInstance();
        byte[] hashedKeyFileData = md.digest(binaryKeyFileToByteArray(keyFileInputStream));
        key = md.digest(hashedKeyFileData);
    }

    private void handleBinaryKey(@NotNull String password, @NotNull InputStream keyFileInputStream) {
        MessageDigest md = Encryption.getMessageDigestInstance();
        byte[] hashedPassword = md.digest(password.getBytes());
        byte[] hashedKeyFileData = md.digest(binaryKeyFileToByteArray(keyFileInputStream));
        key = md.digest(Bytes.concat(hashedPassword, hashedKeyFileData));
    }

    private byte[] binaryKeyFileToByteArray(@NotNull InputStream keyFileInputStream) {
        byte[] ba = null;
        try {
            ba = ByteStreams.toByteArray(keyFileInputStream);

            // if (ba.length == 64) {
            // ba = org.apache.commons.codec.binary.Base64.decodeBase64(ba);
            // }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
        return ba;
    }

    @Override
    public byte[] getKey() {
        return key;
    }

}
