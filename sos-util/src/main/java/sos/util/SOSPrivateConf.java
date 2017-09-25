package sos.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOSPrivateConf {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSPrivateConf.class);
    private String filename = "./config/private/private.conf";
    private Config config = null;

    public SOSPrivateConf(String filename) {
        super();
        this.filename = filename;
    }

    private void init() {
        if (config == null) {
            Path path = Paths.get(filename);
            if (Files.exists(path)) {
                config = ConfigFactory.parseFile(path.toFile());
            }
        }
    }

    public String getValue(String key) {
        LOGGER.debug("reading key: " + key);
        init();
        String value = null;
        value = config.getString(key);
        return value;
    }

    public String getValue(String objectId, String key) {
        LOGGER.debug("reading key: " + key);
        init();
        Config configClass = null;
        String value = null;

        try {
            configClass = config.getConfig(objectId);
        } catch (ConfigException e) {
            LOGGER.warn("The configuration item " + objectId + " is missing in private.conf!");
            LOGGER.warn("see https://kb.sos-berlin.com/x/NwgCAQ for further details on how to setup a secure connection");
        }
        if (configClass != null) {
            value = configClass.getString(key);
        }
        return value;

    }

    private String getAuthFromPrivateConf(String schedulerId) {
        SOSPrivateConf sosPrivateConf = new SOSPrivateConf("./config/private/private.conf");
        String phrase = null;
        try {
            phrase = sosPrivateConf.getValue("jobscheduler.master.auth.users", "schedulerId");
        } catch (ConfigException e) {
            LOGGER.warn(
                    "[inventory] - An credential with the schedulerId as key is missing from configuration item \"jobscheduler.master.auth.users\"!");
            LOGGER.warn("[inventory] - see https://kb.sos-berlin.com/x/NwgCAQ for further details on how to setup a secure connection");
        }
        if (phrase != null && !phrase.isEmpty()) {
            String[] phraseSplit = phrase.split(":", 2);
            byte[] upEncoded = Base64.getEncoder().encode((schedulerId + ":" + phraseSplit[1]).getBytes());
            StringBuilder encoded = new StringBuilder();
            for (byte me : upEncoded) {
                encoded.append((char) me);
            }
            return encoded.toString();
        }
        return phrase;
    }
}
