package sos.util.string2bool;

final class SOSAnd implements SOSIBoolean {

    private SOSIBoolean iBoolean1;

    private SOSIBoolean iBoolean2;

    SOSAnd(final SOSIBoolean newIBoolean1, final SOSIBoolean newIBoolean2) {
        if (newIBoolean1 == null) {
            throw new IllegalArgumentException("Argument: newIBoolean1 is null");
        }
        this.iBoolean1 = newIBoolean1;
        if (newIBoolean2 == null) {
            throw new IllegalArgumentException("Argument: newIBoolean2 is null");
        }
        this.iBoolean2 = newIBoolean2;

    }

    public boolean booleanValue() {
        return (this.iBoolean1.booleanValue() && this.iBoolean2.booleanValue());
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(");
        buffer.append(this.iBoolean1);
        buffer.append("&&");
        buffer.append(this.iBoolean2);
        buffer.append(")");
        return buffer.toString();
    }

}
