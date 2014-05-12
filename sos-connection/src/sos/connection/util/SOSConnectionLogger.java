package sos.connection.util;

import sos.connection.SOSConnection;
import sos.util.SOSBufferedLogger;

/**
 * @author Andreas Püschel <andreas.pueschel@sos-berlin.com>
 * @since 2004-04-06
 * @version 1.0
 * 
 * basic logger implementation for connections
 */
public class SOSConnectionLogger extends SOSBufferedLogger {

    private SOSConnection sosConnection = null;
    
    private String tableLogs = "LOGS";
   
    private int objectId = 0;

    /**
     * Konstruktor logging nach stdout
     * 
     * @param logLevel
     *            ein Wert von 1 bis 9
     * @throws Exception
     */
    public SOSConnectionLogger(int logLevel) throws Exception {
    	super(logLevel);
    }

    /**
     * logging in Datei
     * 
     * @param sosConnection
     *            Connection-Objekt
     * @param logLevel
     *            von 1 bis 9
     * @throws Exception
     */
    public SOSConnectionLogger(SOSConnection sosConnection, int logLevel)
            throws Exception {
    	super(logLevel);
        if (sosConnection == null) { throw new Exception(
                "illegal SOSConnection object"); }
        
        this.sosConnection = sosConnection;
        
    }

    

    /**
     * Speichert die Log-Informationen in der Datenbank
     * 
     * @throws java.lang.Exception
     */ 
    public void store(long objectType, long objectId) throws Exception {

        if (sosConnection == null) {
            throw new Exception("illegal SOSConnection object");
        }
        
        try {
            String query = "DELETE FROM " + getTableLogs() 
            	+ " WHERE \"OBJECT\"=" + objectType
            	+ " AND \"ID\"=" + objectId;
            
            sosConnection.execute(query);
            
            query = "INSERT INTO " + getTableLogs()
            	+ " (\"OBJECT\", \"ID\") VALUES (" + objectType + ", " + objectId + ")";
            
            sosConnection.execute(query);
            
            sosConnection.updateBlob(getTableLogs(), "LOG", this.logBuffer.toString().getBytes(), 
                    "\"OBJECT\"=" + objectType + " AND \"ID\"=" + objectId );

        } catch (Exception e) {
            throw new Exception("error occurred while logging: " + e.getMessage());
        }
        
        this.logBuffer = new StringBuffer();
    }

    /**
     * @return Returns the log contents for the rule that was processed.
     */
    public byte[] getLog(long objectType, long objectId) throws Exception {
        
        if (sosConnection == null)
            throw new Exception("invalid SOSConnection object");
        
        return sosConnection.getBlob("SELECT \"LOG\" FROM " + this.getTableLogs() 
                + " WHERE \"OBJECT\"=" + objectType 
                + " AND \"ID\"=" + objectId);

    }
    
    
    /**
     * @return Returns the tableLogs.
     */
    public String getTableLogs() {
        
        return tableLogs;
    }

    /**
     * @param tableLogs
     *            The tableLogs to set.
     */
    public void setTableLogs(String tableLogs) {
        
        this.tableLogs = tableLogs;
    }    
    
}