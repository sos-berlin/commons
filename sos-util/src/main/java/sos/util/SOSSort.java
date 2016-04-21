package sos.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/** @author Mürüvet Öksüz */
public class SOSSort {

    public static String[] sortArrayOfString(String[] a, boolean desc) throws Exception {
        String[] aa = null;
        int len = -1;
        try {
            for (int i = 0; i < a.length - 1; i++) {
                for (int j = i + 1; j < a.length; j++) {
                    if (a[i].compareTo(a[j]) > 0) {
                        String temp = a[j];
                        a[j] = a[i];
                        a[i] = temp;
                    }
                }
            }
            if (desc) {
                return a;
            } else {
                len = a.length;
                aa = new String[len];
                for (int i = 0; i < len; i++) {
                    aa[len - 1 - i] = a[i];
                }
                return aa;
            }
        } catch (Exception e) {
            throw new Exception("\n -> error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public static ArrayList sortArrayList(ArrayList list, String key) throws Exception {
        try {
            HashMap h = null;
            Object[] o = null;
            int pos = 0;
            ArrayList newList = new ArrayList();
            try {
                o = new Object[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    h = (HashMap) list.get(i);
                    o[i] = h.get(key) + "_@_" + String.valueOf(i);
                }
                Arrays.sort(o);
                for (int i = 0; i < o.length; i++) {
                    pos = Integer.parseInt(o[i].toString().substring(o[i].toString().indexOf("_@_") + 3));
                    newList.add(list.get(pos));
                }
                list = newList;
            } catch (Exception e) {
                throw e;
            }
            return list;
        } catch (Exception e) {
            throw new Exception("..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("////////////////////////////////////////////////////////////");
            System.out.println("/////////////////sortArrayOfString//////////////////////////////");
            System.out.println("////////////////////////////////////////////////////////////");
            String[] s = new String[5];
            s[0] = "Solemon";
            s[1] = "Xena";
            s[2] = "Anton";
            s[3] = "Stephan";
            s[4] = "Arthur";
            String[] erg1 = SOSSort.sortArrayOfString(s, true);
            for (int i = 0; i < erg1.length; i++) {
                System.out.println(i + " " + erg1[i]);
            }
            String[] erg2 = SOSSort.sortArrayOfString(s, false);
            for (int i = 0; i < erg2.length; i++) {
                System.out.println(i + " " + erg2[i]);
            }
            System.out.println("////////////////////////////////////////////////////////////////////////////////");
            System.out.println("/////////////////Testen der Methode: sortArrayList//////////////////////////////");
            System.out.println("////////////////////////////////////////////////////////////////////////////////");
            ArrayList list = new ArrayList();
            HashMap h = new HashMap();
            h.put("object", "x");
            h.put("value", "Herr");
            h.put("type", "String");
            h.put("name", "Anrede");
            list.add(h);
            h = new HashMap();
            h.put("object", "uservar");
            h.put("value", "");
            h.put("type", "String");
            h.put("name", "Gemdarfaendern");
            list.add(h);
            h = new HashMap();
            h.put("object", "uservar");
            h.put("value", "BLZ  290800 10");
            h.put("type", "String");
            h.put("name", "Blz2");
            list.add(h);
            h = new HashMap();
            h.put("object", "uservar");
            h.put("value", "Badensche Str. 29");
            h.put("type", "String");
            h.put("name", "Strasse");
            list.add(h);
            ArrayList sortList = SOSSort.sortArrayList(list, "name");
            System.out.println("Ausgabe der sortierten ArrayListe:");
            for (int i = 0; i < sortList.size(); i++) {
                h = (HashMap) sortList.get(i);
                System.out.println(i + ":");
                System.out.println("object = " + h.get("object"));
                System.out.println("value  = " + h.get("value"));
                System.out.println("type   = " + h.get("type"));
                System.out.println("name   = " + h.get("name"));
            }
        } catch (Exception e) {
            System.err.print(e);
        }
    }

}