package sos.util.string2bool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class SOSBooleanUtil {

    static String validAndformat(final String booleanExpression) throws SOSMalformedBooleanException {
        if (booleanExpression == null || "".equals(booleanExpression)) {
            throw new IllegalArgumentException("Argument: booleanExpression is null or void");
        }
        validRegexp(booleanExpression);
        validParenthesis(booleanExpression);
        return booleanExpression.toUpperCase().replaceAll("TRUE", "T").replaceAll("FALSE", "F").replaceAll("\\|\\|", "|").replaceAll("&&", "&");
    }

    private static void validRegexp(final String booleanExpression) throws SOSMalformedBooleanException {
        String regexp = "(\\(|\\)|\\|{2}|\\&{2}|!|(false)|(true)|\\s)+";
        if (!booleanExpression.matches("^" + regexp + "$")) {
            Matcher matcher = Pattern.compile(regexp).matcher(booleanExpression);
            List<Integer> errorIndexes = new ArrayList<Integer>();
            while (matcher.find()) {
                int start = matcher.start();
                if (start != 0) {
                    errorIndexes.add(new Integer(start));
                }
                int end = matcher.end();
                if (end != booleanExpression.length()) {
                    errorIndexes.add(new Integer(end));
                }
            }
            if (errorIndexes.isEmpty()) {
                errorIndexes.add(new Integer(0));
            }
            throw new SOSMalformedBooleanException("Expected [ ' ' ( ) || && ! true false ]", errorIndexes, booleanExpression);
        }
    }

    private static void validParenthesis(final String booleanExpression) throws SOSMalformedBooleanException {
        int length = booleanExpression.length();
        int openParenthesis = 0;
        int closeParenthesis = 0;
        int lastOpenParenthesisIndex = 0;
        for (int i = 0; i < length; i++) {
            char charAt = booleanExpression.charAt(i);
            switch (charAt) {
            case '(':
                lastOpenParenthesisIndex = i;
                openParenthesis++;
                break;
            case ')':
                closeParenthesis++;
                if (openParenthesis < closeParenthesis) {
                    throw new SOSMalformedBooleanException("Have a close parenthesis without an open parenthesis", i, booleanExpression);
                }
                break;
            default:
                break;
            }
        }
        if (closeParenthesis < openParenthesis) {
            throw new SOSMalformedBooleanException("Have an open parenthesis without a close parenthesis", lastOpenParenthesisIndex,
                    booleanExpression);
        }
    }

}
