package com.sos.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.jobscheduler.model.event.CustomEventVariables;

public class CustomEventsUtil {

    private ObjectMapper objectMapper;
    private List<CustomEvent> listOfCustomEvents;
    private String source;

    public CustomEventsUtil(String source) {
        this.source = source;
        objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        listOfCustomEvents = new ArrayList<CustomEvent>();
    }

    public String getEventCommandAsXml() throws JsonProcessingException {
        String xmlCommand = "";
        if (!listOfCustomEvents.isEmpty()) {

            if (listOfCustomEvents.size() > 1) {
                xmlCommand += "<commands>";
            }
            for (CustomEvent event : listOfCustomEvents) {
                String command = getEventAsCommand(event);
                xmlCommand += command;
            }
            if (listOfCustomEvents.size() > 1) {
                xmlCommand += "</commands>";
            }
        }
        return xmlCommand;
    }
    
    public void addEvent(CustomEvent customEvent) {
        listOfCustomEvents.add(customEvent);
    }
    
    public CustomEvent createEvent(String key) {
        CustomEvent customEvent = new CustomEvent();
        customEvent.setKey(key);
        CustomEventVariables customEventVariables = new CustomEventVariables();
        customEventVariables.setAdditionalProperty("source", source);
        customEvent.setVariables(customEventVariables);
        return customEvent;
    }
    
    public void addEvent(String key,Map<String,String> parameters) throws JsonProcessingException {
        CustomEvent customEvent = new CustomEvent();
        customEvent.setKey(key);
        CustomEventVariables customEventVariables = new CustomEventVariables();
        customEventVariables.setAdditionalProperty("source", source);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            customEventVariables.setAdditionalProperty(entry.getKey(), entry.getValue());
        }
         
        customEvent.setVariables(customEventVariables);
        listOfCustomEvents.add(customEvent);
    }    

    public void addEvent(String key) throws JsonProcessingException {
        CustomEvent customEvent = new CustomEvent();
        customEvent.setKey(key);
        CustomEventVariables customEventVariables = new CustomEventVariables();
        customEventVariables.setAdditionalProperty("source", source);
        customEvent.setVariables(customEventVariables);
        listOfCustomEvents.add(customEvent);
    }

    private String getEventAsCommand(CustomEvent customEvent) throws JsonProcessingException {
        String xmlCommand = objectMapper.writeValueAsString(customEvent);
        return "<publish_event>" + xmlCommand + "</publish_event>";
    }

}
