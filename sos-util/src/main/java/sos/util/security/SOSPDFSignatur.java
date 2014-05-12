package sos.util.security;

import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;

/**
 * @author re
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class SOSPDFSignatur {

    private static String reason = "";

    private static String contact = "";

    private static String location = "";
    
    private static boolean visible	= false;
    
    /**
     * PDF Signatur erzeugen
     * 
     * @param privateKey		Private Key
     * @param chain				Certificate Chain
     * @param originalPdfName	Original PDF Datei zur Signierung
     * @param outputPdfName		Output (signierte) PDF Datei
     * @throws Exception
     */
    public static void createSignatur(PrivateKey privateKey,
            Certificate[] chain, String originalPdfName, String outputPdfName)
            throws Exception {

        PdfReader reader = new PdfReader(originalPdfName);
        FileOutputStream fout = new FileOutputStream(outputPdfName);

        //createSignature(PdfReader reader, OutputStream os, char pdfVersion)
        //pdfVersion - the new pdf version or '\0' to keep the same version as
        // the original document

        PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
        PdfSignatureAppearance sap = stp.getSignatureAppearance();

        
        //setCrypto(PrivateKey privKey, Certificate[] certChain, CRL[] crlList, PdfName filter) 
        // CRL - certificate revocation lists (CRLs) that have different formats but important common uses.
        //		 For example, all CRLs share the functionality of listing revoked certificates, and can be queried on whether or not they list a given certificate.
        // PdfName
        // SELF_SIGNED    	- 	The self signed filter
        // VERISIGN_SIGNED  - 	The VeriSign filter
        // WINCER_SIGNED    - 	The Windows Certificate Security
        sap.setCrypto(privateKey, chain, null,PdfSignatureAppearance.SELF_SIGNED);
        //sap.setCrypto(privateKey, chain, null,PdfSignatureAppearance.WINCER_SIGNED);

        sap.setReason(SOSPDFSignatur.reason);
        sap.setContact(SOSPDFSignatur.contact);
        sap.setLocation(SOSPDFSignatur.location);

        //GregorianCalendar cal = new GregorianCalendar();
        //sap.setSignDate(cal);

        //             comment next line to have an invisible signature
        //setVisibleSignature(Rectangle pageRect, int page, String fieldName)

        //sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
        //sap.setVisibleSignature(new Rectangle(100,100,200, 200), 1, null);
        
        if(SOSPDFSignatur.visible){// todo
            //sap.setVisibleSignature(new Rectangle(200, 200, 400, 400), 1, null);
        }
        
        stp.close();

    }

    /**
     * Signatur Description
     * 
     * @param reason
     */
    public static void setReason(String reason) {
        SOSPDFSignatur.reason = reason;
    }

    /**
     * Kontakt
     * 
     * @param contact
     */
    public static void setContact(String contact) {
        SOSPDFSignatur.contact = contact;
    }
    
    /**
     * Location
     * 
     * @param location
     */
    public static void setLocation(String location) {
        SOSPDFSignatur.location = location;
    }

    /**
     * @param visible The visible to set.
     */
    public static void setVisible(boolean visible) {
        SOSPDFSignatur.visible = visible;
    }
    
    
    public static void main(String[] args) throws Exception {

        try {

            
            String path = "J:/E/java/al/sos.util/signature/";
            
            String keyAlgorithmName = "RSA";
            String provider			= "BC";
            String signatureAlgo	= "SHA1";
            
            String privateKeyFileName = path + "new_"+keyAlgorithmName+"="+provider+".privatekey";
            //String privateKeyFileName = path + "new_RSA=BC.privatekey";
            
            
            PrivateKey privKey = SOSKeyGenerator.getPrivateKeyFromFile(privateKeyFileName);
            
            
            //System.out.println("privKey Length : "+privKey.getEncoded().length);
            
            
            String certFile = path + privKey.getAlgorithm()+"("+provider+")="+signatureAlgo+".cer";
            //String certFile = path + "RSA(BC)=SHA1.cer";
            
            
            Certificate[] chain = SOSCertificate.getCertificateChain(certFile);

            
            SOSPDFSignatur.setReason("Das ist eine Test signatur");
            SOSPDFSignatur.setContact("al@sos-berlin.com");
            SOSPDFSignatur.setLocation("Berlin");

            String signPdfFile = privKey.getAlgorithm()+"("+provider+")="+signatureAlgo+".pdf";
            
            SOSPDFSignatur.createSignatur(privKey, chain,
                    path + "scheduler_install.pdf", path + signPdfFile);

            System.out.println("Sign. PDF wurde erstellt");
            System.out.println("privateKey = "+privateKeyFileName);
            System.out.println("certFile = "+certFile);
            System.out.println("pdfFile = "+path+signPdfFile);

        } catch (Exception e) {
            System.out.println("da + "+e.getMessage());
            System.out.println(e.getStackTrace());
        }

    }
    
}