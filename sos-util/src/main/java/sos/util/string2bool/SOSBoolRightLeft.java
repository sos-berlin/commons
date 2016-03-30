package sos.util.string2bool;

final class SOSBoolRightLeft extends SOSBooleanExpression {

    private String booleanExpression;

    private SOSIBoolean iBoolean;

    SOSBoolRightLeft(final String newBooleanExpression) throws SOSMalformedBooleanException {
        this.booleanExpression = newBooleanExpression;
        this.iBoolean = toIBoolean(SOSBooleanUtil.validAndformat(newBooleanExpression), 0);
    }

    public boolean booleanValue() {
        return this.iBoolean.booleanValue();
    }

    private SOSIBoolean toIBoolean(final String formatedBooleanExpression, final int index) throws SOSMalformedBooleanException {
        char firstChar = getFirstChar(formatedBooleanExpression);
        if (new Character(firstChar).toString().matches("\\s")) {
            firstChar = ' ';
        }
        String substring = getSubstringWithoutFirstChar(formatedBooleanExpression);
        switch (firstChar) {
        case ' ':
            SOSIBoolean boolWhitespace = toIBoolean(substring, index + 1);
            return boolWhitespace;
        case '(':
            String beginToClose = getFromBeginToCloseParenthesis(substring, index + 1);
            String closeToEnd = getFromCloseParenthesisToEnd(substring, index + 1);
            SOSIBoolean boolBeginToClose = toIBoolean(beginToClose, index + 1);
            SOSIBoolean boolOpen = toIBoolean(boolBeginToClose, closeToEnd, index + 1);
            return boolOpen;
        case 'T':
            SOSIBoolean boolTrue = toIBoolean(new SOSBoolean(true), substring, index + 4);
            return boolTrue;
        case 'F':
            SOSIBoolean boolFalse = toIBoolean(new SOSBoolean(false), substring, index + 5);
            return boolFalse;
        case '!':
            SOSIBoolean boolAll = toIBoolean(substring, index + 1);
            SOSIBoolean boolNot = new SOSNot(boolAll);
            return boolNot;
        default:
            throw new SOSMalformedBooleanException("Expected [ (, true, flase, ! ]", index, this.booleanExpression);
        }
    }

    private SOSIBoolean toIBoolean(final SOSIBoolean lastIBoolean, final String formatedBooleanExpression, final int index)
            throws SOSMalformedBooleanException {
        char firstChar = getFirstChar(formatedBooleanExpression);
        if (new Character(firstChar).toString().matches("\\s")) {
            firstChar = ' ';
        }
        String substring = getSubstringWithoutFirstChar(formatedBooleanExpression);
        switch (firstChar) {
        case ' ':
            return toIBoolean(lastIBoolean, substring, index + 1);
        case '.':
            return lastIBoolean;
        case ')':
            return toIBoolean(lastIBoolean, substring, index + 1);
        case '|':
            return new SOSOrOperation(lastIBoolean, toIBoolean(substring, index + 2));
        case '&':
            return new SOSAnd(lastIBoolean, toIBoolean(substring, index + 2));
        default:
            throw new SOSMalformedBooleanException("Expected [ ' ', ), ||, && ]", index, this.booleanExpression);
        }
    }

    private char getFirstChar(final String formatedBooleanExpression) {
        if (formatedBooleanExpression.length() == 0) {
            return '.';
        }
        return formatedBooleanExpression.charAt(0);
    }

    private String getSubstringWithoutFirstChar(final String formatedBooleanExpression) {
        if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
            return "";
        }
        return formatedBooleanExpression.substring(1, formatedBooleanExpression.length());
    }

    private String getFromBeginToCloseParenthesis(final String formatedBooleanExpression, final int index) throws SOSMalformedBooleanException {
        if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
            return "";
        }
        int fromIndex = 0;
        int toIndex = getIndexOfCloseParenthesis(formatedBooleanExpression, index);
        return formatedBooleanExpression.substring(fromIndex, toIndex);
    }

    private String getFromCloseParenthesisToEnd(final String formatedBooleanExpression, final int index) throws SOSMalformedBooleanException {
        if (formatedBooleanExpression == null || formatedBooleanExpression.length() == 0) {
            return "";
        }
        int fromIndex = getIndexOfCloseParenthesis(formatedBooleanExpression, index);
        int toIndex = formatedBooleanExpression.length();
        return formatedBooleanExpression.substring(fromIndex, toIndex);
    }

    private int getIndexOfCloseParenthesis(final String formatedBooleanExpression, final int index) throws SOSMalformedBooleanException {
        int lastIndexOfOpenParenthesis = getIndexOf(formatedBooleanExpression, "(", -1);
        int lastIndexOfCloseParenthesis = getIndexOf(formatedBooleanExpression, ")", -1);
        while (lastIndexOfOpenParenthesis != -1 && lastIndexOfOpenParenthesis < lastIndexOfCloseParenthesis) {
            lastIndexOfOpenParenthesis = getIndexOf(formatedBooleanExpression, "(", lastIndexOfOpenParenthesis);
            lastIndexOfCloseParenthesis = getIndexOf(formatedBooleanExpression, ")", lastIndexOfCloseParenthesis);
        }
        if (lastIndexOfCloseParenthesis == -1) {
            int parenthesisIndex = index + lastIndexOfOpenParenthesis;
            throw new SOSMalformedBooleanException("Have a open parenthesis without a close parenthesis", parenthesisIndex, this.booleanExpression);
        }
        return lastIndexOfCloseParenthesis;
    }

    private int getIndexOf(final String formatedBooleanExpression, final String searchedString, final int fromIndex) {
        int newFromIndex = fromIndex;
        if (newFromIndex == -1) {
            return formatedBooleanExpression.indexOf(searchedString);
        }
        newFromIndex++;
        int length = formatedBooleanExpression.length();
        if (newFromIndex > length) {
            return -1;
        }
        return formatedBooleanExpression.indexOf(searchedString, newFromIndex);
    }

    public String toString() {
        return this.iBoolean.toString();
    }

}
