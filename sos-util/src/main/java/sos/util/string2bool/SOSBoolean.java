package sos.util.string2bool;

final class SOSBoolean implements SOSIBoolean {

    private boolean booleanValue;

    SOSBoolean(final boolean newBooleanValue) {
        this.booleanValue = newBooleanValue;
    }

    public boolean booleanValue() {
        return this.booleanValue;
    }

    public String toString() {
        return "" + this.booleanValue;
    }

}
