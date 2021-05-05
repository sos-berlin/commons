package sos.connection;

public class SOSConnectionTest {

    public static void main(String[] args) throws Exception {
        String configFile = "./src/test/resources/hibernate.cs.cfg.xml";

        SOSConnection conn = null;

        try {
            conn = new SOSMySQLConnection(configFile);
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
