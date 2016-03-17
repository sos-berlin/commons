package sos.util.string2bool;

public abstract class SOSBooleanExpression implements SOSIBoolean {

    public static SOSBooleanExpression readLeftToRight(final String booleanExpression) throws SOSMalformedBooleanException {
        return new SOSBoolLeftRight(booleanExpression);
    }

    public static SOSBooleanExpression readRightToLeft(final String booleanExpression) throws SOSMalformedBooleanException {
        return new SOSBoolRightLeft(booleanExpression);
    }

}
