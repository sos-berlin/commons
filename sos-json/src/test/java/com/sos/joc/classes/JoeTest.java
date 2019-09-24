package com.sos.joc.classes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.JSObjectEdit;
import com.sos.joc.model.joe.common.Param;
import com.sos.joc.model.joe.common.Params;
import com.sos.joc.model.joe.job.Commands;
import com.sos.joc.model.joe.job.EnviromentVariable;
import com.sos.joc.model.joe.job.EnviromentVariables;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.JobEdit;
import com.sos.joc.model.joe.job.Login;
import com.sos.joc.model.joe.job.Script;
import com.sos.joc.model.joe.job.Settings;
import com.sos.joc.model.joe.job.StartJob;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.order.AddOrder;

public class JoeTest {

    private ObjectMapper xmlMapper = new XmlMapper().configure(SerializationFeature.INDENT_OUTPUT, true).configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private ObjectMapper objMapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true).configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private Path resourceDirectory = Paths.get("src","test","resources");

    @Test
    public void testJob() throws JsonProcessingException {
        Job job = new Job();
        job.setTitle("myJob");
        job.setIsOrderJob("yes");
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
        params.setParamList(Arrays.asList(param, param2));
        job.setParams(params);
        Login login = new Login();
        login.setUser("me");
        login.setPassword("secret");
        job.setLogin(login);
        Script script = new Script();
        script.setLanguage("shell");
        script.setContent("\necho hallo\necho welt\n");
        job.setScript(script);
        //job.setRunTime("<period from=\"00:00\" to=\"24:00\"/>");
        Commands commands = new Commands();
        StartJob startJob = new StartJob();
        startJob.setAt("now");
        startJob.setJob("/path/to/myOtherJob");
        StartJob startJob2 = new StartJob();
        startJob2.setAt("now");
        startJob2.setJob("/path/to/myOtherJob2");
        commands.setOnExitCode("success");
        commands.setStartJobs(Arrays.asList(startJob, startJob2));
        Commands commands2 = new Commands();
        AddOrder addOrder = new AddOrder();
        addOrder.setAt("now");
        addOrder.setJobChain("myJobChain");
        addOrder.setId("42");
        commands2.setOnExitCode("error");
        commands2.setStartJobs(Arrays.asList(startJob));
        commands2.setAddOrders(Arrays.asList(addOrder));
        job.setCommands(Arrays.asList(commands, commands2));
        writeValueAsXMLString(job);
    }
    
    @Test
    public void testJob2() throws JsonParseException, JsonMappingException, IOException {
        Path testFile = resourceDirectory.resolve("job1.json");
        Job job = objMapper.readValue(testFile.toFile(), Job.class);
        writeValueAsXMLString(job);
    }
    
    @Test
    public void textJSObjEditTest() throws IOException {
        Path testFile = resourceDirectory.resolve("job1store.json");
        JSObjectEdit jsObj = objMapper.readValue(testFile.toFile(), JSObjectEdit.class);
        IJSObject jobI = jsObj.getConfiguration();
        JobEdit jobEdit = jsObj.cast();
        Job job = jobEdit.getConfiguration();
        writeValueAsXMLString(jobI);
        writeValueAsXMLString(job);
        writeValueAsJsonString(jobI);
    }
    
    @Test
    public void testJobChainWithReturnCodes() throws JsonParseException, JsonMappingException, IOException {
        Path testFile = resourceDirectory.resolve("jobChainWithReturnCodes.job_chain.xml");
        JobChain jobChain = xmlMapper.readValue(testFile.toFile(), JobChain.class);
        writeValueAsXMLString(jobChain);
        writeValueAsJsonString(jobChain);
    }
    
    @Test
    public void testJobWithCommands() throws JsonParseException, JsonMappingException, IOException {
        Path testFile = resourceDirectory.resolve("jobWithCommands.job.xml");
        Job job = xmlMapper.readValue(testFile.toFile(), Job.class);
        writeValueAsXMLString(job);
        writeValueAsJsonString(job);
    }
    
    @Test
    public void testJobWithRunTime() throws JsonParseException, JsonMappingException, IOException {
        Path testFile = resourceDirectory.resolve("jobWithRuntime.job.xml");
        Job job = xmlMapper.readValue(testFile.toFile(), Job.class);
        writeValueAsXMLString(job);
        writeValueAsJsonString(job);
    }
    
    private String writeValueAsXMLString(Object obj) throws JsonProcessingException {
        String s = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n\n" + xmlMapper.writeValueAsString(obj);
        System.out.println(s);
        return s;
    }
    
    private String writeValueAsJsonString(Object obj) throws JsonProcessingException {
        String s = objMapper.writeValueAsString(obj);
        System.out.println(s);
        return s;
    }

}
