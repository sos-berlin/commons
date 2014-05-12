package sos.connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;


/**
 * Title:
 * <p>
 * Description: Helper Class for DB Connection Pool
 * </p>
 * Copyright: Copyright (c) 2003 Company: SOS GmbH
 * 
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun </a>
 * @version $Id$
 */

public class SOSConnectionPool {

    private InitialContext initCtx = null;

    /** datasource for connection pooling */
    private DataSource dataSource = null;

    /** the datasource name */
    private String dataSourceName = null;

    /** the SOSConnection class name */
    private String sosConnectionClassName = null;
    
    /** lookup method already called*/
    private boolean lookupCalled = false;
    

    /**
     * SOSConnectionPool constructor
     * 
     * @param dataSourceName
     *            datasource name
     * @throws Exception
     *  
     */
    public SOSConnectionPool(String dataSourceName) throws Exception {
        if (dataSourceName == null || dataSourceName.length() == 0)
                throw (new Exception("missing datasource name!!"));
        this.dataSourceName = dataSourceName;
        lookup();

        this.lookupCalled = true;
    }
    
 
    /**
     * SOSConnectionPool constructor
     * 
     * @param dataSourceName
     *            datasource name
     * @param sosConnectionClassName
     *            sosConnection class name
     * @throws Exception
     *  
     */
    public SOSConnectionPool(String dataSourceName,
            String sosConnectionClassName) throws Exception {
        this(dataSourceName);
        if (sosConnectionClassName == null
                || sosConnectionClassName.length() == 0)
                throw (new Exception("missing sosconnection class name!!"));

        this.sosConnectionClassName = sosConnectionClassName;

        this.lookupCalled = true;
    }

    /**
     * SOSConnectionPool constructor
     *
     */
    public SOSConnectionPool() {
        this.lookupCalled = false;
    }
 
    
    
    /**
     * @throws Exception
     */
    private void lookup() throws Exception {

        try { // to connect to the pool
            initCtx = new InitialContext();
            dataSource = (DataSource) initCtx.lookup("java:comp/env/"
                    + dataSourceName);

        } catch (Exception e) {
            throw new Exception("Could not find datasource for ["
                    + dataSourceName + "], " + e);
        }
    }

    /**
     * @return the sosconnection
     * @throws Exception
     */
    public SOSConnection getConnection() throws Exception {
        
    return this.getConnection(null);	
    }
    
    /**
     * 
     * @param logger
     * @return
     * @throws Exception
     */
    public SOSConnection getConnection(sos.util.SOSLogger logger) throws Exception {
        
        if(!this.lookupCalled){
	        
	        if ( this.dataSourceName == null || this.dataSourceName.length() == 0){
				throw (new Exception ("missing datasource name!!"));
	        }
	        if ( this.sosConnectionClassName == null || this.sosConnectionClassName.length() == 0){
				throw (new Exception ("missing sosconnection class name!!"));
	        }
	        lookup();
	    }
       
        // try to get a sosConnection
        synchronized (this.getDataSource()) {
        	SOSConnection retConnection=null;
        	if(logger == null){
        		retConnection = SOSConnection.createInstance(this.getSosConnectionClassName(), this.getDataSource().getConnection());
        	}
        	else{
        		retConnection = SOSConnection.createInstance(this.getSosConnectionClassName(), this.getDataSource().getConnection(),logger);
        	}
        	retConnection.prepare(retConnection.getConnection());
        	return retConnection;
        } // synchronized
    }
    
    /**
     * @return the datasource object
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }


    /**
     * close the Initial Context
     *  
     */
    public void close() {
        try {
            if (initCtx != null) initCtx.close();
        } catch (Exception e) {
        }
    }

    /**
     * @return the name of the sosconnection class
     */
    public String getSosConnectionClassName() {
        return sosConnectionClassName;
    }

    /**
     * @param sosConnectionClassName
     *            the sosconnection class
     */
    public void setSosConnectionClassName(String sosConnectionClassName) {
        this.sosConnectionClassName = sosConnectionClassName;
    }

    /**
     * @return Returns the dataSourceName.
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * @param dataSourceName
     *            The dataSourceName to set.
     */
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}