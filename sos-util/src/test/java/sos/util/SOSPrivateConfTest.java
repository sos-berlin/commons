package sos.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class SOSPrivateConfTest {

    @Test
    public void testPrivateConf() throws Exception {
        SOSPrivateConf sosPrivateConf = new SOSPrivateConf(
                "src/test/resources/private.conf");
        String phrase = sosPrivateConf.getValue("jobscheduler.master.auth.users", "jobscheduler_prod");
        String password = sosPrivateConf.getValue("jobscheduler.master.webserver.https.keystore", "password");
        String jocUrl = sosPrivateConf.getValue("joc.url");
        String userAccount = sosPrivateConf.getDecodedValue("joc.webservice.jitl","joc.account");
        assertEquals("TestPrivateConf.getValue", userAccount, "api_user:api");
        assertEquals("TestPrivateConf.getValue", phrase, "plain:secret");
        assertEquals("TestPrivateConf.getValue", password, "secret");
        assertEquals("TestPrivateConf.getValue", jocUrl, "http://localhost:4446");

    }

}
