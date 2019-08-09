package com.sos.joc.classes;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;

public class CustomEventsUtilTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetEventAsCommand() throws JsonProcessingException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil("CustomEventsUtilTest");
        customEventsUtil.addEvent("CustomEventAdded");
        String s = customEventsUtil.getEventCommandAsXml();
        System.out.println(s);
    }

}
