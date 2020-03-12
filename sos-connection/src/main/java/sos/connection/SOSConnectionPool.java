package sos.connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

/** @author Ghassan Beydoun */
public class SOSConnectionPool {

    private InitialContext initCtx = null;
    private DataSource dataSource = null;
    private String dataSourceName = null;
    private String sosConnectionClassName = null;
    private boolean lookupCalled = false;

    public SOSConnectionPool(String dataSourceName) throws Exception {
        if (dataSourceName == null || dataSourceName.isEmpty()) {
            throw new Exception("missing datasource name!!");
        }
        this.dataSourceName = dataSourceName;
        lookup();
        this.lookupCalled = true;
    }

    public SOSConnectionPool(String dataSourceName, String sosConnectionClassName) throws Exception {
        this(dataSourceName);
        if (sosConnectionClassName == null || sosConnectionClassName.isEmpty()) {
            throw new Exception("missing sosconnection class name!!");
        }
        this.sosConnectionClassName = sosConnectionClassName;
        this.lookupCalled = true;
    }

    public SOSConnectionPool() {
        this.lookupCalled = false;
    }

    private void lookup() throws Exception {
        try {
            initCtx = new InitialContext();
            dataSource = (DataSource) initCtx.lookup("java:comp/env/" + dataSourceName);
        } catch (Exception e) {
            throw new Exception("Could not find datasource for [" + dataSourceName + "], " + e);
        }
    }

    public SOSConnection getConnection() throws Exception {
        if (!this.lookupCalled) {
            if (this.dataSourceName == null || this.dataSourceName.isEmpty()) {
                throw new Exception("missing datasource name!!");
            }
            if (this.sosConnectionClassName == null || this.sosConnectionClassName.isEmpty()) {
                throw new Exception("missing sosconnection class name!!");
            }
            lookup();
        }
        synchronized (this.getDataSource()) {
            SOSConnection retConnection = null;
            retConnection = SOSConnection.createInstance(this.getSosConnectionClassName(), this.getDataSource().getConnection());

            retConnection.prepare(retConnection.getConnection());
            return retConnection;
        }
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void close() {
        try {
            if (initCtx != null) {
                initCtx.close();
            }
        } catch (Exception e) {
            //
        }
    }

    public String getSosConnectionClassName() {
        return sosConnectionClassName;
    }

    public void setSosConnectionClassName(String sosConnectionClassName) {
        this.sosConnectionClassName = sosConnectionClassName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

}