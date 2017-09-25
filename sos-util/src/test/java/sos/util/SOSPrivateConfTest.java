package sos.util;

import static org.junit.Assert.*;
import org.junit.Test;

import com.typesafe.config.ConfigException;

public class SOSPrivateConfTest {

    @Test
    public void testPrivateConf() {
        SOSPrivateConf sosPrivateConf = new SOSPrivateConf(
                "C:\\Users\\ur\\Documents\\sos-berlin.com\\jobscheduler\\scheduler_joc_cockpit\\config\\private\\private.conf");
        String phrase = sosPrivateConf.getValue("jobscheduler.master.auth.users", "jobscheduler_prod");
        String password = sosPrivateConf.getValue("jobscheduler.master.webserver.https.keystore", "password");
        String jocUrl = sosPrivateConf.getValue("joc.url");
        String test;
        try {
            test = sosPrivateConf.getValue("joc.webservice.jitl", "joc.test");
        } catch (ConfigException e) {
            test = sosPrivateConf.getValue("joc.test");
        }
        assertEquals("TestPrivateConf.getValue", test, "test");
        assertEquals("TestPrivateConf.getValue", phrase, "plain:secret");
        assertEquals("TestPrivateConf.getValue", password, "secret");
        assertEquals("TestPrivateConf.getValue", jocUrl, "http://localhost:4446");

    }

}
