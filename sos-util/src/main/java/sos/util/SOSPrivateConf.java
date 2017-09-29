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

    private void init() throws Exception {
        if (config == null) {
            Path path = Paths.get(filename);
            if (Files.exists(path)) {
                config = ConfigFactory.parseFile(path.toFile());
            } else {
                String s = String.format("File %s not found", path);
                LOGGER.warn(s);
                throw new Exception(s);
            }
        }
    }

    public String getValue(String key) throws Exception {
        LOGGER.debug("reading key: " + key);
        init();
        String value = null;
        value = config.getString(key);
        return value;
    }

    public String getValue(String objectId, String key) throws Exception {
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

    public String getEncodedValue(String objectId, String key) throws Exception {
        String s = getValue(objectId, key);
        if (s != null) {
            return new String(Base64.getDecoder().decode(s.getBytes("UTF-8")), "UTF-8");
        } else {
            return null;
        }
    }

    public String getEncodedValue(String key) throws Exception {
        String s = getValue(key);
        if (s != null) {
            return new String(Base64.getDecoder().decode(s.getBytes("UTF-8")), "UTF-8");
        } else {
            return null;
        }
    }

}
