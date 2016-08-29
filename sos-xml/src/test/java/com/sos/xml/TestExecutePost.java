package com.sos.xml;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sos.xml.SOSXmlCommand;

import sos.xml.SOSXMLXPath;

public class TestExecutePost {

    

    @Test
    public void test() throws Exception {
        SOSXmlCommand sosXmlCommand= new SOSXmlCommand("localhost",4000); 
        String answer = sosXmlCommand.excutePost("<subsystem.show what=\"statistics\"/>");
        System.out.println(answer);
        SOSXMLXPath sosxml = new SOSXMLXPath(new StringBuffer(answer));
        Node n = sosxml.selectSingleNode("//subsystem[@name='job']//job.statistic[@job_state='pending']");
        NamedNodeMap map = n.getAttributes();
        for (int j = 0; j < map.getLength(); j++) {
            System.out.println(map.item(j).getNodeName() + "=" + map.item(j).getNodeValue()); 
        }
     

        sosXmlCommand.executeXPath("//subsystem[@name='job']//file_based.statistics");
        System.out.println("jobschedulerJobs.setAny: " + Integer.parseInt(sosXmlCommand.getAttribute("count")));
        sosXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@need_process='true']");
        System.out.println("jobschedulerJobs.setNeedProcess: " + Integer.parseInt(sosXmlCommand.getAttribute("count")));
 
        sosXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@job_state='running']");
        System.out.println("jobschedulerJobs.setRunning: " + Integer.parseInt(sosXmlCommand.getAttribute("count")));

        sosXmlCommand.executeXPath("//subsystem[@name='job']//job.statistic[@job_state='stopped']");
        System.out.println("jobschedulerJobs.setStopped: " + Integer.parseInt(sosXmlCommand.getAttribute("count")));
        
    }
 }
