package sos.connection;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import sos.util.SOSStandardLogger;

public class SOSConnectionTest {

    public static void main(String[] args) throws Exception {
        String hibernateFile = "D:\\config\\reporting.hibernate.cfg.xml";
        Path sqlFile = Paths.get("D:\\test.sql");

        SOSConnection conn = null;

        try {
            conn = new SOSMySQLConnection(hibernateFile, new SOSStandardLogger(9));
            conn.connect();

            List<String> commands = conn.getStatements(new String(Files.readAllBytes(sqlFile)));

            for (String command : commands) {
                System.out.println("##############");
                System.out.println(command);
                System.out.println("##############");
            }
        } catch (Exception ex) {
            throw ex;

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
