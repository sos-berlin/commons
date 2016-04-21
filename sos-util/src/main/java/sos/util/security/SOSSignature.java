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
    public final static String SHA1 = "SHA1";
    public final static String MD2 = "MD2";
    public final static String MD5 = "MD5";

    public static void signToFile(String fileToSign, String signFile, PrivateKey privKey) throws Exception {
        try {
            if ("BC".equalsIgnoreCase(SOSSignature.provider) && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }
            String algo = SOSSignature.hashAlgorithm + "With" + privKey.getAlgorithm();
            Signature sign = Signature.getInstance(algo, SOSSignature.provider);
            sign.initSign(privKey);
            FileInputStream fis = new FileInputStream(fileToSign);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sign.update(buffer, 0, len);
            }
            bufin.close();
            byte[] realSig = sign.sign();
            saveToFile(realSig, signFile);
        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (Exception e) {
            throw new Exception("exp : " + e);
        }
    }

    public static boolean verify(String dataFile, String signFile, PublicKey pubKey) throws Exception {
        try {
            byte[] sigToVerify = readFromFile(signFile);
            if ("BC".equalsIgnoreCase(SOSSignature.provider) && Security.getProvider("BC") == null) {
                Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
                Security.addProvider(bp);
            }
            Signature sig = Signature.getInstance(SOSSignature.hashAlgorithm + "With" + pubKey.getAlgorithm(), SOSSignature.provider);
            sig.initVerify(pubKey);
            FileInputStream datafis = new FileInputStream(dataFile);
            BufferedInputStream bufin = new BufferedInputStream(datafis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
            }
            bufin.close();
            boolean verifies = sig.verify(sigToVerify);
            return verifies;
        } catch (NoClassDefFoundError e) {
            throw new Exception("no such Definition : " + e);
        } catch (Exception e) {
            throw new Exception("exp : " + e);
        }
    }

    public static void saveToFile(byte[] info, String filename) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(info);
        fos.close();
    }

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
        return info;
    }

    public static String getHashAlgorithm() {
        return SOSSignature.hashAlgorithm;
    }

    public static void setHashAlgorithm(String hashAlgorithm) {
        SOSSignature.hashAlgorithm = hashAlgorithm;
    }

    public static String getProvider() {
        return SOSSignature.provider;
    }

    public static void setProvider(String provider) {
        SOSSignature.provider = provider;
    }

    public static void main(String[] args) {
        String path = "J:/E/java/re/sos.test/signature/";
        String keyAlgorithmName = "RSA";
        String provider = "BC";
        String fileToSign = path + "data.rtf";
        String signFile = path + "data.signature";
        String privateKeyFileName = path + "new_" + keyAlgorithmName + "=" + provider + ".privatekey";
        String publicKeyFileName = path + "new_" + keyAlgorithmName + "=" + provider + ".publickey";
        try {
            PrivateKey privKey = SOSKeyGenerator.getPrivateKeyFromFile(privateKeyFileName);
            SOSSignature.setHashAlgorithm(SOSSignature.SHA1);
            SOSSignature.setProvider(provider);
            System.out.println(privKey.getFormat());
            SOSSignature.signToFile(fileToSign, signFile, privKey);
            PublicKey pubKey = SOSKeyGenerator.getPublicKeyFromFile(publicKeyFileName);
            boolean verifies = SOSSignature.verify(fileToSign, signFile, pubKey);
            System.out.println("Signature verifies: " + verifies);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}