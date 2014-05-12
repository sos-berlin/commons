
package sos.util;

import java.io.Serializable;



/**
 * <p>Title: SOSPrefixLogger</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @version $Id$
 */

public class SOSPrefixLogger extends SOSStandardLogger implements Serializable{

	
	/**
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	private String prefix = "";
	
	
	/**
	 * @param logger
	 * @param logLevel
	 * @throws Exception
	 */
	public SOSPrefixLogger(SOSStandardLogger logger , int logLevel, String prefix)
			throws Exception {
		super( logger.getWriter(),logLevel );
		this.prefix = prefix;

	}
	
	/**
	 * @param logger
	 * @param logLevel
	 * @throws Exception
	 */
	public SOSPrefixLogger(SOSStandardLogger logger , int logLevel)
			throws Exception {
		super( logger.getWriter(),logLevel );
	}
	

	/**
	 * @param logger
	 * @throws Exception
	 */
	public SOSPrefixLogger(  SOSStandardLogger logger )
			throws Exception {
	  super(logger.getWriter(),logger.getLogLevel());
	}
	

	
	  /**
	   * write the header to the stream
	   *
	   * @throws java.lang.Exception
	   */
	  public void writeHeader() throws Exception {
	  	writeHeader( prefix );
	  }	
	  
	  /**
	   * write the str to the stream
	   * @param level is the loglevel 
	   * @param str is the string to log
	   * 
	   * @throws java.lang.Exception 
	   */
	  protected void log( int level, String str) throws Exception {
	  	super.log( level , prefix +  (char)(32) + str );
	  }

}
