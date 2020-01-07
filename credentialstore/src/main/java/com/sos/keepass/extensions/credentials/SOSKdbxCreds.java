package com.sos.keepass.extensions.credentials;

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
import com.sos.keepass.SOSKeePassDatabase;
import com.sos.keepass.exceptions.SOSKeePassCredentialException;
import com.sos.keepass.exceptions.SOSKeePassDatabaseException;

public class SOSKdbxCreds implements Credentials {

    private byte[] key;

    public SOSKdbxCreds() {
    }

    public void load(String password, Path keyFile) throws SOSKeePassDatabaseException {

        FileInputStream is = null;
        try {
            if (keyFile == null) {
                handlePassword(password);
            } else {
                is = new FileInputStream(keyFile.toFile());
                if (isXmlKey(is)) {
                    handleXmlKey(is, password);
                } else {
                    handleBinaryKey(is, password);
                }
            }
        } catch (Throwable e) {
            if (keyFile == null) {
                throw new SOSKeePassCredentialException(e);
            } else {
                throw new SOSKeePassCredentialException(String.format("[%s]%s", SOSKeePassDatabase.getFilePath(keyFile), e.toString()), e);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable te) {
                }
            }
        }
    }

    private boolean isXmlKey(@NotNull FileInputStream is) throws Exception {

        FileChannel fc = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(50);
            fc = is.getChannel();
            fc.read(buffer, 0);
            String preview = new String(buffer.array()).trim().replaceAll(">\\s+<", "><").toLowerCase();
            return preview.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?><keyfile>");
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

    private void handleXmlKey(@NotNull InputStream keyFile, String password) {
        if (password == null) {
            key = new KdbxCreds(keyFile).getKey();
        } else {
            key = new KdbxCreds(password.getBytes(), keyFile).getKey();
        }
    }

    private void handleBinaryKey(@NotNull InputStream keyFile, String password) {
        MessageDigest md = Encryption.getMessageDigestInstance();
        if (password == null) {
            byte[] hash = md.digest(toByteArray(keyFile));
            key = md.digest(hash);
        } else {
            byte[] passHash = md.digest(password.getBytes());
            byte[] keyHash = md.digest(toByteArray(keyFile));
            key = md.digest(Bytes.concat(passHash, keyHash));
        }
    }

    private byte[] toByteArray(@NotNull InputStream keyFile) {
        byte[] ba = null;
        try {
            ba = ByteStreams.toByteArray(keyFile);
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
