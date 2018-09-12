package com.sos.jobscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RuntimeCalendar {

    private String path = null;
    private List<String> dates = null;
    private List<String> holidays = null;

    public RuntimeCalendar() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }
    
    public void setDates(Set<String> dates) {
        this.dates = new ArrayList<String>(dates);
    }

    public List<String> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<String> holidays) {
        this.holidays = holidays;
    }
    
    public void setHolidays(Set<String> holidays) {
        this.holidays = new ArrayList<String>(holidays);
    }
    
}
