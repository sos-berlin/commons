package com.sos.classes;

import java.nio.charset.StandardCharsets;

public class Latin1ToUtf8 {
    
    public static String convert(String str) {
        if (str == null) {
            return null;
        }
        byte[] bs = str.getBytes();
        if (containsMultiByteLatin1Char(bs)) {
            if (containsUTF8decodedLatin1Char(bs)) {
                return new String(bs, StandardCharsets.UTF_8);
            } else {
                return new String(str.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            }
        } else {
            return str;
        }
    }

    private static boolean containsUTF8decodedLatin1Char(byte[] bs) {
        for (int i = 0; i < bs.length - 1; i++) {
            //char between c280 - c2bf or c380 - c3bf found
            if ((bs[i] == -61 || bs[i] == -62) && bs[i+1] >= -128 && bs[i+1] <= -65) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean containsMultiByteLatin1Char(byte[] bs) {
        for (int i = 0; i < bs.length; i++) {
            //char between a0 - ff found, which has 2 bytes in UTF-8
            if (bs[i] >= -96 && bs[i] <= -1) {
                return true;
            }
        }
        return false;
    }
}
