 package  sos.util.string2bool;
 
final class SOSNot implements SOSIBoolean {

	 
	private SOSIBoolean iBoolean;

	 
	SOSNot(final SOSIBoolean newIBoolean) {
		if (newIBoolean == null) {
			throw new IllegalArgumentException("Argument: newIBoolean is null");
		}
		this.iBoolean = newIBoolean;
	}
 
	public boolean booleanValue() {
		return (!this.iBoolean.booleanValue());
	}
 
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(!");
		buffer.append(this.iBoolean);
		buffer.append(")");
		return buffer.toString();
	}
}
