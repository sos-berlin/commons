package sos.util.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/** @author re */
public class SOSKeyGenerator {

    private static String keyAlgorithmName = "RSA";
    private static String provider = "BC";
    private static int keyLenght = 1024;

    public static void generateKeys(String privateKeyFileName, String publicKeyFileName) throws Exception {
        KeyPair keys = SOSKeyGenerator.createKeys();
        if (keys != null) {
            SOSKeyGenerator.saveKeyPair(keys, privateKeyFileName, publicKeyFileName);
        } else {
            throw new Exception("Keys are empty");
        }
    }

    public static KeyPair createKeys() throws Exception {
        if (SOSKeyGenerator.provider == null) {
            throw new Exception("Provider is null");
        }
        if (SOSKeyGenerator.keyAlgorithmName == null) {
            throw new Exception("keyAlgorithmName is null");
        }
        if (SOSKeyGenerator.keyLenght == 0) {
            throw new Exception("keyLenght cannot be 0");
        }
        try {
            if ("BC".equalsIgnoreCase(SOSKeyGenerator.provider) && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }
            KeyPairGenerator generator = KeyPairGenerator.getInstance(SOSKeyGenerator.keyAlgorithmName, SOSKeyGenerator.provider);
            generator.initialize(SOSKeyGenerator.keyLenght, new SecureRandom());
            KeyPair keys = generator.generateKeyPair();
            return keys;
        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (java.security.NoSuchProviderException e) {
            throw new Exception("no such provider " + SOSKeyGenerator.provider + " : " + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new Exception("no such Key Algorithm " + SOSKeyGenerator.keyAlgorithmName + " : " + e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static void saveKeyPair(KeyPair keyPair, String privateKeyFileName, String publicKeyFileName) throws Exception {
        SOSKeyGenerator.writePublicKeyFile(keyPair.getPublic(), publicKeyFileName);
        SOSKeyGenerator.writePrivateKeyFile(keyPair.getPrivate(), privateKeyFileName);
        System.out.println("private: " + keyPair.getPrivate().toString());
    }

    public static void writePublicKeyFile(PublicKey key, String publicKeyFileName) throws Exception {
        SOSKeyGenerator.writeKey(publicKeyFileName, key.getEncoded());
    }

    public static void writePrivateKeyFile(PrivateKey key, String privateKeyFileName) throws Exception {
        SOSKeyGenerator.writeKey(privateKeyFileName, key.getEncoded());
    }

    public static void writeKey(String fileName, byte[] data) throws Exception {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static PrivateKey getPrivateKeyFromBytes(byte[] encKey) throws Exception {
        if (SOSKeyGenerator.provider == null) {
            throw new Exception("Provider is null");
        }
        if (SOSKeyGenerator.keyAlgorithmName == null) {
            throw new Exception("keyAlgorithmName is null");
        }
        PrivateKey priv = null;
        try {
            if ("BC".equalsIgnoreCase(SOSKeyGenerator.provider) && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encKey);
            KeyFactory factory = KeyFactory.getInstance(SOSKeyGenerator.keyAlgorithmName, SOSKeyGenerator.provider);
            priv = factory.generatePrivate(keySpec);
        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (java.security.spec.InvalidKeySpecException e) {
            throw new Exception("Invalide Private Key : " + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new Exception("no such Key Algorithm " + SOSKeyGenerator.keyAlgorithmName + " : " + e);
        } catch (java.security.NoSuchProviderException e) {
            throw new Exception("no such provider " + SOSKeyGenerator.provider + " : " + e);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return priv;
    }

    public static PrivateKey getPrivateKeyFromFile(String keyFile) throws Exception {
        if (keyFile == null) {
            throw new Exception("Private Key File is null");
        }
        try {
            byte[] encKey = readFromFile(keyFile);
            return getPrivateKeyFromBytes(encKey);
        } catch (IOException e) {
            throw new Exception("Private Key File " + keyFile + " not found : " + e);
        }
    }

    public static PublicKey getPublicKeyFromFile(String keyFile) throws Exception {
        if (SOSKeyGenerator.provider == null) {
            throw new Exception("Provider is null");
        }
        if (SOSKeyGenerator.keyAlgorithmName == null) {
            throw new Exception("keyAlgorithmName is null");
        }
        if (keyFile == null) {
            throw new Exception("Private Key File is null");
        }
        PublicKey pubKey = null;
        try {
            if ("BC".equalsIgnoreCase(SOSKeyGenerator.provider) && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }
            byte[] encKey = readFromFile(keyFile);
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance(SOSKeyGenerator.keyAlgorithmName, SOSKeyGenerator.provider);
            pubKey = keyFactory.generatePublic(pubKeySpec);
        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (IOException e) {
            throw new Exception("Public Key File " + keyFile + " not found : " + e);
        } catch (java.security.spec.InvalidKeySpecException e) {
            throw new Exception("Invalide Public Key : " + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new Exception("no such Key Algorithm " + SOSKeyGenerator.keyAlgorithmName + " : " + e);
        } catch (java.security.NoSuchProviderException e) {
            throw new Exception("no such provider " + SOSKeyGenerator.provider + " : " + e);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return pubKey;
    }

    public static byte[] readFromFile(String fileName) throws Exception {
        byte[] info;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            info = new byte[fis.available()];
            fis.read(info);
            fis.close();
        } catch (Exception e) {
            System.err.println("exception " + e.toString());
            info = new byte[0];
        }
        return info;
    }

    public static String getKeyAlgorithmName() {
        return keyAlgorithmName;
    }

    public static void setKeyAlgorithmName(String keyAlgorithmName) {
        SOSKeyGenerator.keyAlgorithmName = keyAlgorithmName;
    }

    public static int getKeyLenght() {
        return SOSKeyGenerator.keyLenght;
    }

    public static void setKeyLenght(int keyLenght) {
        SOSKeyGenerator.keyLenght = keyLenght;
    }

    public static String getProvider() {
        return provider;
    }

    public static void setProvider(String provider) {
        SOSKeyGenerator.provider = provider;
    }

    public static void main(String[] args) {
        try {
            String path = "J:/E/java/al/sos.util/signature/";
            String keyAlgorithmName = "RSA";
            String provider = "BC";
            String privateKeyFileName = path + "new_" + keyAlgorithmName + "=" + provider + ".privatekey";
            String publicKeyFileName = path + "new_" + keyAlgorithmName + "=" + provider + ".publickey";
            SOSKeyGenerator.setKeyAlgorithmName(keyAlgorithmName);
            SOSKeyGenerator.setProvider(provider);
            SOSKeyGenerator.generateKeys(privateKeyFileName, publicKeyFileName);
            System.out.println("Keys wurden erstellt");
            System.out.println("privKey = " + privateKeyFileName);
            System.out.println("pubKey = " + publicKeyFileName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}