package sos.util.security;

import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;

/** @author re */
public class SOSPDFSignatur {

    private static String reason = "";
    private static String contact = "";
    private static String location = "";
    private static boolean visible = false;

    public static void createSignatur(PrivateKey privateKey, Certificate[] chain, String originalPdfName, String outputPdfName) throws Exception {
        PdfReader reader = new PdfReader(originalPdfName);
        FileOutputStream fout = new FileOutputStream(outputPdfName);
        PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        sap.setCrypto(privateKey, chain, null, PdfSignatureAppearance.SELF_SIGNED);
        sap.setReason(SOSPDFSignatur.reason);
        sap.setContact(SOSPDFSignatur.contact);
        sap.setLocation(SOSPDFSignatur.location);
        stp.close();

    }

    public static void setReason(String reason) {
        SOSPDFSignatur.reason = reason;
    }

    public static void setContact(String contact) {
        SOSPDFSignatur.contact = contact;
    }

    public static void setLocation(String location) {
        SOSPDFSignatur.location = location;
    }

    public static void setVisible(boolean visible) {
        SOSPDFSignatur.visible = visible;
    }

    public static void main(String[] args) throws Exception {
        try {
            String path = "J:/E/java/al/sos.util/signature/";
            String keyAlgorithmName = "RSA";
            String provider = "BC";
            String signatureAlgo = "SHA1";
            String privateKeyFileName = path + "new_" + keyAlgorithmName + "=" + provider + ".privatekey";
            PrivateKey privKey = SOSKeyGenerator.getPrivateKeyFromFile(privateKeyFileName);
            String certFile = path + privKey.getAlgorithm() + "(" + provider + ")=" + signatureAlgo + ".cer";
            Certificate[] chain = SOSCertificate.getCertificateChain(certFile);
            SOSPDFSignatur.setReason("Das ist eine Test signatur");
            SOSPDFSignatur.setContact("al@sos-berlin.com");
            SOSPDFSignatur.setLocation("Berlin");
            String signPdfFile = privKey.getAlgorithm() + "(" + provider + ")=" + signatureAlgo + ".pdf";
            SOSPDFSignatur.createSignatur(privKey, chain, path + "scheduler_install.pdf", path + signPdfFile);
            System.out.println("Sign. PDF wurde erstellt");
            System.out.println("privateKey = " + privateKeyFileName);
            System.out.println("certFile = " + certFile);
            System.out.println("pdfFile = " + path + signPdfFile);
        } catch (Exception e) {
            System.out.println("da + " + e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }

}