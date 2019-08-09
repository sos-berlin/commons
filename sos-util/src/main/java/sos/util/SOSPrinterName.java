/*
 * Created on 28.04.2004 To change the template for this generated file go to Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.util;

import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;

/** @author mo
 *
 *         To change the template for this generated type comment go to Window - Preferences - Java - Code Generation - Code and Comments */
public class SOSPrinterName {

    /** Schreibt alle Namen der Drucker auf standard output. Manche Namen differenzieren sich im Gegensatz zu dem, was im Druckerdialog steht. */
    public static void getPrinterName() {
        try {
            System.out.println("");
            System.out.println("Default Printername is: ");
            System.out.println(java.awt.print.PrinterJob.getPrinterJob().getPrintService().getName());
            System.out.println("");
            System.out.println("List of All Printername: ");
            PrintService[] printService = PrinterJob.lookupPrintServices();
            for (int i = 0; i < printService.length; i++) {
                System.out.println(i + " Printer name: " + printService[i].getName());
            }
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    /** Liefert alle Namen der Default Drucker auf den Rechner.
     * 
     * @return String */
    public static String getDefaultPrinterName() {
        try {
            return java.awt.print.PrinterJob.getPrinterJob().getPrintService().getName();
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }

    /** Liefert alle Namen der Drucker in einer ArrayListe
     * 
     * @return String */
    public static List<String> getAllPrinterName() throws Exception {
        List<String> retVal = new ArrayList<String>();
        try {
            PrintService[] printService = PrinterJob.lookupPrintServices();
            for (int i = 0; i < printService.length; i++) {
                retVal.add(printService[i].getName());
            }
            return retVal;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}