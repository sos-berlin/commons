package sos.connection;

import sos.util.SOSStandardLogger;

public class SOSConnectionTest {

    public static void main(String[] args) throws Exception {
        String configFile = "./src/test/resources/hibernate.cs.cfg.xml";

        SOSConnection conn = null;

        try {
            conn = new SOSMySQLConnection(configFile, new SOSStandardLogger(9));
            conn.connect();

        } catch (Exception ex) {
            throw ex;

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
