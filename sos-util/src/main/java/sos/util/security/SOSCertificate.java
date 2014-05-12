package sos.util.security;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

/**
 * Mit BouncyCastle
 */
public  class SOSCertificate { 

    /** ausgestellt für */
    private static String subjectDN = "O=APL/SOS,C=DE";

    /** ausgestellt von CN=Robert Ehrlich,C=DE,O=APL/SOS */
    private static String issuerDN = "O=APL/SOS,C=DE";

    // default aktuelles datum -1
    private static Date validFrom = null;

    // default validFrom + 90 Tage
    private static Date validTo = null;

    private static BigInteger serialNumber = BigInteger.valueOf(1);

    private static String hashAlgorithm = "SHA1";

    private static Calendar gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    
    
    /** Hash Algorithmus */
    public static final String SHA1 = "SHA1";

    public static final String MD2 = "MD2";

    public static final String MD5 = "MD5";

    
    /**
     * Certificate Objekt erzeugen
     * 
     * @param privateKey
     * @param publicKey
     * @throws Exception
     */
    public static Certificate createCertificate(PrivateKey privateKey,
            PublicKey publicKey) throws Exception {

        if (privateKey == null) { throw new Exception("Private Key is null"); }

        if (publicKey == null) { throw new Exception("Public Key is null"); }

        if (SOSCertificate.serialNumber == null) { throw new Exception(
                "Serialnumber is null"); }

        if (SOSCertificate.subjectDN == null || SOSCertificate.subjectDN.length() == 0) { throw new Exception(
                "Subject DN is empty"); }

        if (SOSCertificate.issuerDN == null || SOSCertificate.issuerDN.length() == 0) { throw new Exception(
                "Issuer DN is empty"); }

        long time = gmtCalendar.getTimeInMillis();
        
        if (SOSCertificate.validFrom == null) {
            //this.validFrom = new Date();
            // von gestern
            SOSCertificate.validFrom = new Date(time - 24L*60*60*1000);
        }

        if (SOSCertificate.validTo == null) {
            SOSCertificate.validTo = new Date(SOSCertificate.validFrom.getTime() + 90 * 24 * 60
                    * 60 * 1000L);
            //this.validTo = new
            // Date(System.currentTimeMillis()+90*24*60*60*1000L);
        }

        try {
            if (Security.getProvider("BC") == null) {
                Security
                        .addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            }

            org.bouncycastle.x509.X509V3CertificateGenerator v3CertGen = new org.bouncycastle.x509.X509V3CertificateGenerator();
            // create the certificate - version 3
            v3CertGen.reset();

            v3CertGen.setSerialNumber(SOSCertificate.serialNumber);

            // ausgestellt für
            v3CertGen.setIssuerDN(new org.bouncycastle.asn1.x509.X509Name(
                    SOSCertificate.issuerDN));
            //      ausgestellt von
            //v3CertGen.setSubjectDN(new X509Principal(n));
            v3CertGen.setSubjectDN(new org.bouncycastle.asn1.x509.X509Name(
                    SOSCertificate.subjectDN));

            // gültig ab
            v3CertGen.setNotBefore(SOSCertificate.validFrom);
            //gültig bis
            v3CertGen.setNotAfter(SOSCertificate.validTo);

            v3CertGen.setPublicKey(publicKey);

            //v3CertGen.setSignatureAlgorithm("SHA1WithRSAEncryption");
            v3CertGen.setSignatureAlgorithm(SOSCertificate.hashAlgorithm
                    + "With" + privateKey.getAlgorithm());

            X509Certificate cert = v3CertGen
                    .generateX509Certificate(privateKey);

            return cert;
        } catch (NoClassDefFoundError e) {
            throw new Exception("not found  Definition : " + e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Certificate Objekt erzeugen und in eine Datei schreiben
     * 
     * @param privateKey
     * @param publicKey
     * @param certFileName
     * @throws Exception
     */
    public static void generateCertificate(PrivateKey privateKey, PublicKey publicKey,
            String certFileName) throws Exception {

        if (privateKey == null) { throw new Exception("Private Key is null"); }

        if (publicKey == null) { throw new Exception("Public Key is null"); }

        if (certFileName == null || certFileName.trim().length() == 0) { throw new Exception(
                "Certification file name is empty"); }

        try {

            Certificate cert = createCertificate(privateKey, publicKey);

            byte encoding[] = cert.getEncoded();
            FileOutputStream fos = new FileOutputStream(certFileName);
            fos.write(encoding);
            fos.close();
            
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    /**
     * Cerificate aus einer Datei lesen
     * 
     * @param file		Certification File
     * @return			Certificate Objekt
     */
    public static Certificate importCertificate(File file) throws Exception {
        try {
            FileInputStream is = new FileInputStream(file);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate cert = cf.generateCertificate(is);

            /*
            if (cert instanceof X509Certificate) {
                X509Certificate x509cert = (X509Certificate) cert;

                // Get subject
                Principal principal = x509cert.getSubjectDN();
                String subjectDn = principal.getName();

                // Get issuer
                principal = x509cert.getIssuerDN();
                String issuerDn = principal.getName();
                System.out.println(issuerDn);
            }
            */
            
            return cert;
        } catch (CertificateException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    	catch (Exception e) {
    	    throw new Exception(e);
    	}

    }

    /**
     * Certificate Chain aus einer Datei ermitteln
     * 
     * @param file		Certificate Filename
     * @return			Certificate Chain
     */
    public static Certificate[] getCertificateChain(String fileName)
            throws Exception {
        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream certStream = getStream(fileName);

            Collection c = cf.generateCertificates(certStream);
            Certificate[] certs = new Certificate[c.toArray().length];

            if (c.size() == 1) {
                certStream = getStream(fileName);
                System.out.println("1 certificate");
                Certificate cert = cf.generateCertificate(certStream);
                certs[0] = cert;
            } else {
                System.out.println("Certificate chain length: " + c.size());
                certs = (Certificate[]) c.toArray();
            }

            return certs;

        } catch (CertificateException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
        catch (Exception e) {
            throw new Exception(e);
        }

    }
    
    /**
     * Certificate Chain aus einem Byte Array ermitteln
     * 
     * @param file		Certificate Filename
     * @return			Certificate Chain
     */
    public static Certificate[] getCertificateChain(byte[] bArray)
            throws Exception {
        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream certStream = getStream(bArray);

            Collection c = cf.generateCertificates(certStream);
            Certificate[] certs = new Certificate[c.toArray().length];

            if (c.size() == 1) {
                certStream = getStream(bArray);
                System.out.println("1 certificate");
                Certificate cert = cf.generateCertificate(certStream);
                certs[0] = cert;
            } else {
                System.out.println("Certificate chain length: " + c.size());
                certs = (Certificate[]) c.toArray();
            }

            return certs;

        } catch (CertificateException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
        catch (Exception e) {
            throw new Exception(e);
        }

    }

    
    /**
     * voller Input Stream einer Datei ermitteln
     * 
     * @param fileName
     * @return @throws
     *         IOException
     */
    private static InputStream getStream(String fileName) throws Exception {

        FileInputStream fis = new FileInputStream(fileName);
        DataInputStream dis = new DataInputStream(fis);
        byte[] bytes = new byte[dis.available()];
        dis.readFully(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return bais;
    }
    
    /**
     *Input Stream aus einem Byte Array erzeugen
     * 
     * @param fileName
     * @return @throws
     *         IOException
     */
    private static InputStream getStream(byte[] bytes) throws Exception {
       
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return bais;
    }
 
    /**
     * 
     * @return
     */
    public static String getHashAlgorithm() {
        return SOSCertificate.hashAlgorithm;
    }
   
    /**
     * 
     * @param hashAlgorithm
     */
    public static void setHashAlgorithm(String hashAlgorithm) {
        SOSCertificate.hashAlgorithm = hashAlgorithm;
    }
    /**
     * @return Returns the issuerDN.
     */
    public static String getIssuerDN() {
        return SOSCertificate.issuerDN;
    }
    /**
     * @param issuerDN The issuerDN to set.
     */
    public static void setIssuerDN(String issuerDN) {
        SOSCertificate.issuerDN = issuerDN;
    }
    /**
     * @return Returns the serialNumber.
     */
    public static BigInteger getSerialNumber() {
        return SOSCertificate.serialNumber;
    }
    /**
     * @param serialNumber The serialNumber to set.
     */
    public static void setSerialNumber(BigInteger serialNumber) {
        SOSCertificate.serialNumber = serialNumber;
    }
    /**
     * @return Returns the subjectDN.
     */
    public static String getSubjectDN() {
        return SOSCertificate.subjectDN;
    }
    /**
     * @param subjectDN The subjectDN to set.
     */
    public static void setSubjectDN(String subjectDN) {
        SOSCertificate.subjectDN = subjectDN;
    }
    /**
     * @return Returns the validFrom.
     */
    public static Date getValidFrom() {
        return SOSCertificate.validFrom;
    }
    /**
     * @param validFrom The validFrom to set.
     */
    public static void setValidFrom(Date validFrom) {
        SOSCertificate.validFrom = validFrom;
    }
    /**
     * @return Returns the validTo.
     */
    public static Date getValidTo() {
        return SOSCertificate.validTo;
    }
    /**
     * @param validTo The validTo to set.
     */
    public static void setValidTo(Date validTo) {
        SOSCertificate.validTo = validTo;
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {

        String path = "J:/E/java/al/sos.util/signature/";

        String keyAlgorithmName = "RSA";
        String provider			= "BC";
        
        String privateKeyFileName = path + "new_"+keyAlgorithmName+"="+provider+".privatekey";
        String publicKeyFileName = path + "new_"+keyAlgorithmName+"="+provider+".publickey";
        //String publicKeyFileName = path + "new_DSA=SUN.publickey";

        
        try {
            
        
            PrivateKey privKey = SOSKeyGenerator.getPrivateKeyFromFile(privateKeyFileName);
            PublicKey pubKey   = SOSKeyGenerator.getPublicKeyFromFile(publicKeyFileName);
            
            
            SOSCertificate.setHashAlgorithm(SOSCertificate.SHA1);
            //SOSCertificate.setHashAlgorithm(SOSCertificate.MD2);
            //SOSCertificate.setHashAlgorithm(SOSCertificate.MD5);
            
            //OU,DC=com
            // ohne CN bei SubjectDN - zeigt Signed by Unknown
            
            // ausgestellt für
            //SOSCertificate.setSubjectDN("CN=Robert Ehrlich,C=DE,O=APL/SOS");
            // wenn sich subject von issuer unterscheiden dann ...
            SOSCertificate.setSubjectDN("CN=Andreas Liebert,C=DE,O=APL/SOS");
            //ausgestellt von
            SOSCertificate.setIssuerDN("CN=Andreas Liebert,C=DE,O=APL/SOS");
            SOSCertificate.setSerialNumber(BigInteger.valueOf(100));
            
            String certFile = privKey.getAlgorithm()+"("+provider+")="+SOSCertificate.getHashAlgorithm()+".cer";
            
            SOSCertificate.generateCertificate(privKey, pubKey, path + certFile);
            
            System.out.println("Zertifikate wurde erstellt");
            System.out.println("privKey = "+privateKeyFileName);
            System.out.println("pubKey = "+publicKeyFileName);
            System.out.println("certFile = "+path+certFile);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    
    
    
    
}