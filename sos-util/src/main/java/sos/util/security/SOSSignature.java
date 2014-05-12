package sos.util.security;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

public class SOSSignature {

    private static String provider = "BC";

    private static String hashAlgorithm = "SHA1";

    /** Hash Algorithmus */
    public final static String SHA1 = "SHA1";

    public final static String MD2 = "MD2";

    public final static String MD5 = "MD5";

    /**
     * @param fileToSign
     * @param signFile
     * @param privKey
     * @throws Exception
     */
    public static void signToFile(String fileToSign, String signFile,
            PrivateKey privKey) throws Exception {

        try {
            if (SOSSignature.provider.equalsIgnoreCase("BC")
                    && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }

            String algo = SOSSignature.hashAlgorithm + "With"
                    + privKey.getAlgorithm();

            /* Signature */
            Signature sign = Signature.getInstance(algo, SOSSignature.provider);

            /* initialis. mit prived key */
            sign.initSign(privKey);

            /* lesen aus fileToSign und update() */
            FileInputStream fis = new FileInputStream(fileToSign);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sign.update(buffer, 0, len);
            }
            bufin.close();

            /* generate unterschrift */
            byte[] realSig = sign.sign();

            /* save in to file “signature” */
            saveToFile(realSig, signFile);
        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (Exception e) {
            throw new Exception("exp : " + e);
        }
    }

    /**
     * @param dataFile
     * @param signFile
     * @param pubKey
     * @throws Exception
     */
    public static boolean verify(String dataFile, String signFile,
            PublicKey pubKey) throws Exception {

        try {
            /* unterschrift from file signature lesen */
            byte[] sigToVerify = readFromFile(signFile);

            if (SOSSignature.provider.equalsIgnoreCase("BC")
                    && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }

            /*
             * Signature Objekt erzeugen mit mit public key verify
             */
            Signature sig = Signature.getInstance(SOSSignature.hashAlgorithm
                    + "With" + pubKey.getAlgorithm(), SOSSignature.provider);
            sig.initVerify(pubKey);

            /* lesen from file data update() */
            FileInputStream datafis = new FileInputStream(dataFile);
            BufferedInputStream bufin = new BufferedInputStream(datafis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
            }
            bufin.close();

            /* verify */
            boolean verifies = sig.verify(sigToVerify);
            //System.out.println("Signature verifies: " + verifies);

            return verifies;

        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (Exception e) {
            throw new Exception("exp : " + e);
        }
    }

    /**
     * save byte array in to file
     */
    public static void saveToFile(byte[] info, String filename)
            throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(info);
        fos.close();
    }

    /**
     * lesen from file in byte array
     *  
     */
    public static byte[] readFromFile(String fileName) throws Exception {
        byte[] info;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            info = new byte[fis.available()];
            fis.read(info);
            fis.close();
        } catch (Exception e) {
            info = new byte[0];
            throw new Exception(e);
        }
        return (info);
    }

    /**
     * @return Returns the hashAlgorithm.
     */
    public static String getHashAlgorithm() {
        return SOSSignature.hashAlgorithm;
    }

    /**
     * @param hashAlgorithm
     *            The hashAlgorithm to set.
     */
    public static void setHashAlgorithm(String hashAlgorithm) {
        SOSSignature.hashAlgorithm = hashAlgorithm;
    }

    /**
     * @return Returns the provider.
     */
    public static String getProvider() {
        return SOSSignature.provider;
    }

    /**
     * @param provider
     *            The provider to set.
     */
    public static void setProvider(String provider) {
        SOSSignature.provider = provider;
    }

    public static void main(String[] args) {

        String path = "J:/E/java/re/sos.test/signature/";

        String keyAlgorithmName = "RSA";
        String provider = "BC";

        String fileToSign = path + "data.rtf";
        String signFile = path + "data.signature";

        String privateKeyFileName = path + "new_" + keyAlgorithmName + "="
                + provider + ".privatekey";
        String publicKeyFileName = path + "new_" + keyAlgorithmName + "="
                + provider + ".publickey";

        try {
            PrivateKey privKey = SOSKeyGenerator
                    .getPrivateKeyFromFile(privateKeyFileName);

            SOSSignature.setHashAlgorithm(SOSSignature.SHA1);
            SOSSignature.setProvider(provider);

            System.out.println(privKey.getFormat());

            //Signatur Datei erstellen
            SOSSignature.signToFile(fileToSign, signFile, privKey);

            // Signatur verifizieren
            PublicKey pubKey = SOSKeyGenerator
                    .getPublicKeyFromFile(publicKeyFileName);
            boolean verifies = SOSSignature.verify(fileToSign, signFile, pubKey);

            System.out.println("Signature verifies: " + verifies);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}