package com.sos.keepass;

public class SOSKeePassDatabaseTest {

    public static void main(String[] args) throws Exception {

        String uri = "cs://server/SFTP/my_server@user?file=my_file.kdbx&key_file=my_keyfile.png";

        SOSKeePassDatabase.main(new String[] { uri });
    }
}
