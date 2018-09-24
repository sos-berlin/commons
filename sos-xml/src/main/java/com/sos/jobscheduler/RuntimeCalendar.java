package com.sos.jobscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sos.joc.model.calendar.Period;

public class RuntimeCalendar {

    @JsonProperty("path")
    private String path = null;
    @JsonProperty("dates")
    private List<String> dates = null;
    @JsonProperty("holidays")
    private List<String> holidays = null;
    @JsonProperty("periods")
    private List<Period> periods = null;

    public RuntimeCalendar() {
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("dates")
    public List<String> getDates() {
        return dates;
    }

    @JsonProperty("dates")
    public void setDates(List<String> dates) {
        this.dates = dates;
    }
    
    public void setDates(Set<String> dates) {
        this.dates = new ArrayList<String>(dates);
    }

    @JsonProperty("holidays")
    public List<String> getHolidays() {
        return holidays;
    }

    @JsonProperty("holidays")
    public void setHolidays(List<String> holidays) {
        this.holidays = holidays;
    }
    
    public void setHolidays(Set<String> holidays) {
        this.holidays = new ArrayList<String>(holidays);
    }

    @JsonProperty("periods")
    public List<Period> getPeriods() {
        return periods;
    }

    @JsonProperty("periods")
    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(path).append(dates).append(holidays).append(periods).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RuntimeCalendar) == false) {
            return false;
        }
        RuntimeCalendar rhs = ((RuntimeCalendar) other);
        return new EqualsBuilder().append(path, rhs.path).append(dates, rhs.dates).append(holidays, rhs.holidays).append(periods, rhs.periods).isEquals();
    }
    
}
