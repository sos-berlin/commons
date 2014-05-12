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

import org.bouncycastle.util.encoders.Base64Encoder;

/**
 * @author re
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class SOSKeyGenerator {

   //private String alg = "DSA";
   //private String provider = "SUN";
   //  private int keyLenght = 1024;

    private static String keyAlgorithmName = "RSA";

    private static String provider = "BC";

    private static int keyLenght = 1024;

    /**
     * Prived und Public Keys generieren und in den ensprechenden Dateien
     * abspeichern
     * 
     * @throws Exception
     */
    public static void generateKeys(String privateKeyFileName,String publicKeyFileName) throws Exception {

        KeyPair keys = SOSKeyGenerator.createKeys();

        if (keys != null) {
            SOSKeyGenerator.saveKeyPair(keys,privateKeyFileName,publicKeyFileName);
        } else {
            throw new Exception("Keys are empty");
        }
    }

    /**
     * Prived und Public Keys generieren und KeyPair zurückgeben Keys können
     * dann per keyPair.getPrivate() bzw keyPair.getPublic() abgeholt werden
     * 
     * @return KeyPair
     * @throws Exception
     */
    public static KeyPair createKeys() throws Exception {

        if (SOSKeyGenerator.provider == null) { throw new Exception("Provider is null"); }
        if (SOSKeyGenerator.keyAlgorithmName == null) { throw new Exception(
                "keyAlgorithmName is null"); }
        if (SOSKeyGenerator.keyLenght == 0) { throw new Exception("keyLenght cannot be 0"); }
        try {

            if (SOSKeyGenerator.provider.equalsIgnoreCase("BC")
                    && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }

            KeyPairGenerator generator = KeyPairGenerator.getInstance(
                    SOSKeyGenerator.keyAlgorithmName, SOSKeyGenerator.provider);
           
            //SecureRandom random = SecureRandom.getInstance("SHA1PRNG",
            // "SUN");
            //initialize(int keysize, SecureRandom random)

            generator.initialize(SOSKeyGenerator.keyLenght, new SecureRandom());
            //generator.initialize(this.keyLenght);

            KeyPair keys = generator.generateKeyPair();

            return keys;
        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (java.security.NoSuchProviderException e) {
            throw new Exception("no such provider " + SOSKeyGenerator.provider + " : " + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new Exception("no such Key Algorithm "
                    + SOSKeyGenerator.keyAlgorithmName + " : " + e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Private und Public Keys in die ensprechenden Dateien schreiben
     * 
     * @param keyPair
     * @throws Exception
     */
    public static void saveKeyPair(KeyPair keyPair,String privateKeyFileName,String publicKeyFileName) throws Exception {
        SOSKeyGenerator.writePublicKeyFile(keyPair.getPublic(),publicKeyFileName);
        SOSKeyGenerator.writePrivateKeyFile(keyPair.getPrivate(),privateKeyFileName);
        System.out.println("private: "+keyPair.getPrivate().toString());
    }

    /**
     * Public Key in eine Datei schreiben
     * 
     * @param key
     *            Public Key
     * @throws Exception
     */
    public static void writePublicKeyFile(PublicKey key,String publicKeyFileName) throws Exception {
        SOSKeyGenerator.writeKey(publicKeyFileName, key.getEncoded());
    }

    /**
     * Private Key in eine Datei schreiben
     * 
     * @param key
     *            Private Key
     * @throws Exception
     */
    public static void writePrivateKeyFile(PrivateKey key,String privateKeyFileName) throws Exception {
        SOSKeyGenerator.writeKey(privateKeyFileName, key.getEncoded());

    }

    /**
     * Key Inhalt ein eine Datei schreiben
     * 
     * @param fileName
     *            Key File Name
     * @param data
     *            Key Data
     * @throws Exception
     */
    public static void writeKey(String fileName, byte[] data) throws Exception {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(data);
            fos.close();

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Private Key aus einem ByteArray erzeugen
     * 
     * @param encKey
     *            Array das den encodierten Schlüssel enthält
     * @return @throws
     *         Exception
     */
    public static PrivateKey getPrivateKeyFromBytes(byte[] encKey) throws Exception {

        if (SOSKeyGenerator.provider == null) { throw new Exception("Provider is null"); }
        if (SOSKeyGenerator.keyAlgorithmName == null) { throw new Exception(
                "keyAlgorithmName is null"); }
        

        PrivateKey priv = null;

        try {
            if (SOSKeyGenerator.provider.equalsIgnoreCase("BC")
                    && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }

            

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encKey);
            KeyFactory factory = KeyFactory.getInstance(SOSKeyGenerator.keyAlgorithmName,
                    SOSKeyGenerator.provider);

            priv = factory.generatePrivate(keySpec);
       
        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
       
        } catch (java.security.spec.InvalidKeySpecException e) {
            throw new Exception("Invalide Private Key : " + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new Exception("no such Key Algorithm "
                    + SOSKeyGenerator.keyAlgorithmName + " : " + e);
        } catch (java.security.NoSuchProviderException e) {
            throw new Exception("no such provider " + SOSKeyGenerator.provider + " : " + e);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return priv;
    }
    
    /**
     * Private Key aus einer Datei auslesen
     * 
     * @param keyFile
     *            Name der Private Key Datei
     * @return @throws
     *         Exception
     */
    public static PrivateKey getPrivateKeyFromFile(String keyFile) throws Exception {
    	if (keyFile == null) { throw new Exception("Private Key File is null"); }
    	try {
    		byte[] encKey = readFromFile(keyFile);
    		return getPrivateKeyFromBytes(encKey);
    	 } catch (IOException e) {
            throw new Exception("Private Key File " + keyFile + " not found : "
                    + e);
    	 }
    }

    /**
     * Public Key aus einer Datei auslesen
     * 
     * @param keyFile
     * @return @throws
     *         Exception
     */
    public static PublicKey getPublicKeyFromFile(String keyFile) throws Exception {

        if (SOSKeyGenerator.provider == null) { throw new Exception("Provider is null"); }
        if (SOSKeyGenerator.keyAlgorithmName == null) { throw new Exception(
                "keyAlgorithmName is null"); }
        if (keyFile == null) { throw new Exception("Private Key File is null"); }

        PublicKey pubKey = null;

        try {
            if (SOSKeyGenerator.provider.equalsIgnoreCase("BC")
                    && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }

            byte[] encKey = readFromFile(keyFile);
            
            /* erzeugen key spezifikation */
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
            
            KeyFactory keyFactory = KeyFactory.getInstance(
                    SOSKeyGenerator.keyAlgorithmName, SOSKeyGenerator.provider);
            
            pubKey = keyFactory.generatePublic(pubKeySpec);

        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (IOException e) {
            throw new Exception("Public Key File " + keyFile + " not found : "
                    + e);
        } catch (java.security.spec.InvalidKeySpecException e) {
            throw new Exception("Invalide Public Key : " + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new Exception("no such Key Algorithm "
                    + SOSKeyGenerator.keyAlgorithmName + " : " + e);
        } catch (java.security.NoSuchProviderException e) {
            throw new Exception("no such provider " + SOSKeyGenerator.provider + " : " + e);
        } catch (Exception e) {
            throw new Exception(e);
        }

        return pubKey;
    }

    /**
     * Lesen from file in byte array
     * 
     * @param fileName
     * @return @throws
     *         Exception
     */
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
        return (info);
    }
    
    /**
     * @return Returns the keyAlgorithmName.
     */
    public static String getKeyAlgorithmName() {
        return keyAlgorithmName;
    }

    /**
     * @param keyAlgorithmName
     *            The keyAlgorithmName to set.
     */
    public static void setKeyAlgorithmName(String keyAlgorithmName) {
        SOSKeyGenerator.keyAlgorithmName = keyAlgorithmName;
    }

    /**
     * @return Returns the keyLenght.
     */
    public static int getKeyLenght() {
        return SOSKeyGenerator.keyLenght;
    }

    /**
     * @param keyLenght
     *            The keyLenght to set.
     */
    public static void setKeyLenght(int keyLenght) {
        SOSKeyGenerator.keyLenght = keyLenght;
    }

    /**
     * @return Returns the provider.
     */
    public static String getProvider() {
        return provider;
    }

    /**
     * @param provider
     *            The provider to set.
     */
    public static void setProvider(String provider) {
        SOSKeyGenerator.provider = provider;
    }
    
    

    
    public static void main(String args[]) {
        try {

            String path = "J:/E/java/al/sos.util/signature/";

            
            String keyAlgorithmName = "RSA";
            String provider			= "BC";
            
            String privateKeyFileName = path + "new_"+keyAlgorithmName+"="+provider+".privatekey";
            String publicKeyFileName = path + "new_"+keyAlgorithmName+"="+provider+".publickey";
            
            
            
            
            
            SOSKeyGenerator.setKeyAlgorithmName(keyAlgorithmName);
            SOSKeyGenerator.setProvider(provider);
                        
            SOSKeyGenerator.generateKeys(privateKeyFileName,publicKeyFileName);

            System.out.println("Keys wurden erstellt");
            System.out.println("privKey = "+privateKeyFileName);
            System.out.println("pubKey = "+publicKeyFileName);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}