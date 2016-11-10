package sos.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class SOSDurationsTest {
    
    private SOSDurations durations = new SOSDurations(3000);
    
    private void addDuration(int d){
        
        SOSDuration duration = new SOSDuration();
        duration.setStartTime(new Date(2016,1,1,0,0,0) );
        duration.setEndTime(new Date(2016,1,1,0,0,d));
        
        durations.add(duration);
        
    }

    @Test
    public void testAverage() {

        addDuration(2);
        addDuration(4);
        addDuration(5);
        addDuration(6);
        addDuration(7);
        addDuration(8);
        addDuration(9);
        addDuration(10);
        addDuration(11);
        addDuration(12);
        addDuration(13);
        
        long average = durations.average();
        
        assertEquals("testAverage", 8500, average);

    }

}
