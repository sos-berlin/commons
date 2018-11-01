package sos.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

 
public class TestParameterSubstitutor {

    @Test
    public void testReplace() {
        ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();
        parameterSubstitutor.addKey("par1", "value of par1");
        parameterSubstitutor.addKey("par2", "value of par2");
        String source = "The value of par1 is ${par1} and the value of par2 is ${par2} ${par3} is not set";
        String erg = parameterSubstitutor.replace(source);
        assertEquals("testReplace failed: ", "The value of par1 is value of par1 and the value of par2 is value of par2 ${par3} is not set", erg);
    }

    @Test
    public void replaceSystemProperties() {
        ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();
        String source = "The value of user.name is ${user.name}";
        String erg = parameterSubstitutor.replaceSystemProperties(source);
        assertEquals("testReplace failed: ", "The value of user.name is " + System.getProperty("user.name"), erg);
    }
    
    private void setNewEnvironmentHack(Map<String, String> newenv) throws Exception
    {
      Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
      Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
      theEnvironmentField.setAccessible(true);
      Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
      env.clear();
      env.putAll(newenv);
      Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
      theCaseInsensitiveEnvironmentField.setAccessible(true);
      Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
      cienv.clear();
      cienv.putAll(newenv);
    }

    @Test
    public void replaceEnvVars() throws Exception {
        HashMap<String, String> newenv = new HashMap<String,String>();
        newenv.put("TEST", "12345");
        setNewEnvironmentHack(newenv);
        ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();
        String source = "The value of TEST is ${TEST}";
        String erg = parameterSubstitutor.replaceEnvVars(source);
        assertEquals("testReplace failed: ", "The value of TEST is " + System.getenv("TEST"), erg);
    }
	@Test
	public void testReplaceInFile() throws IOException {
		ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();
		parameterSubstitutor.addKey("SCHEDULER_HOME", String.format("SCHEDULER_HOME=%s", "mySchedulerHome"));
		File in = new File(
				"src/test/resources/jobscheduler_agent_instance_script.txt");
		File out = new File("src/test/resources/1.txt");
		parameterSubstitutor.replaceInFile(in, out);
	}

	@Test
	public void testReplaceWithEnv() throws Exception {
	    HashMap<String, String> newenv = new HashMap<String,String>();
        newenv.put("TEST", "12345");
        setNewEnvironmentHack(newenv);
		ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();

		String s = parameterSubstitutor.replaceEnvVars("${TEST}");
		assertEquals("testReplaceWithEnv", "12345", s);

	}

	@Test
	public void testGetParameterNameFromString() {
		ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();

		List<String> s = parameterSubstitutor.getParameterNameFromString("ab${param1}12${param2}xyz");
		assertEquals("testGetParameterNameFromString", "param1", s.get(0));
		assertEquals("testGetParameterNameFromString", "param2", s.get(1));
		
		for (String p: s) {
			parameterSubstitutor.addKey(p, "value of " + p);
		}
		String substituted = parameterSubstitutor.replace("this is ${param1} and ${param2}");
		assertEquals("testGetParameterNameFromString", "this is value of param1 and value of param2", substituted);
	}
}
