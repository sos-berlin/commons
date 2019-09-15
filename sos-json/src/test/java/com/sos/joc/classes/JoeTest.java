package com.sos.joc.classes;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sos.joc.model.joe.common.Param;
import com.sos.joc.model.joe.common.Params;
import com.sos.joc.model.joe.job.EnviromentVariable;
import com.sos.joc.model.joe.job.EnviromentVariables;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.Login;
import com.sos.joc.model.joe.job.Script;
import com.sos.joc.model.joe.job.Settings;

public class JoeTest {

    private ObjectMapper xmlMapper = new XmlMapper().configure(SerializationFeature.INDENT_OUTPUT, true).configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void testJob() throws JsonProcessingException {
        Job job = new Job();
        job.setTitle("myJob");
        job.setOrder("yes");
        job.setStopOnError("no");
        Settings settings = new Settings();
        settings.setLogLevel("debug9");
        job.setSettings(settings);
        EnviromentVariables env = new EnviromentVariables();
        EnviromentVariable envVar = new EnviromentVariable();
        envVar.setName("myEnv");
        envVar.setValue("myEnvVal");
        env.setVariables(Arrays.asList(envVar));
        job.setEnvironment(env);
        Params params = new Params();
        Param param = new Param();
        param.setName("myParam");
        param.setValue("myParamVal");
        Param param2 = new Param();
        param2.setName("myParam2");
        param2.setValue("myParamVal2");
        params.setParams(Arrays.asList(param, param2));
        job.setParams(params);
        Login login = new Login();
        login.setUser("me");
        login.setPassword("secret");
        job.setLogin(login);
        Script script = new Script();
        script.setLanguage("shell");
        script.setContent("\necho hallo\necho welt\n");
        job.setScript(script);
        writeValueAsString(job);
    }
    
    private String writeValueAsString(Object obj) throws JsonProcessingException {
        String s = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n\n" + xmlMapper.writeValueAsString(obj);
        System.out.println(s);
        return s;
    }

}
