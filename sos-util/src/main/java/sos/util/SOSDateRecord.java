package sos.util;

import java.util.Date;

public class SOSDateRecord implements Comparable {

    private Date date = null;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int compareTo(Object o) {
        if (!(o instanceof SOSDateRecord)) {
            throw new RuntimeException("invalid type!");
        }
        SOSDateRecord record = (SOSDateRecord) o;
        return this.date.compareTo(record.getDate());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
