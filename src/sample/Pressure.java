package sample;

import java.util.Date;

public class Pressure {

    private final int[] pressure;
    private final Date timestamp;

    public Pressure(int min, int max){
        int[] pressure = {min, max};
        this.pressure = pressure;
        this.timestamp = new Date();
    }

    public Pressure(int min, int max, int millis){
        int[] pressure = {min, max};
        this.pressure = pressure;
        this.timestamp = new Date(millis);
    }

    public int[] getPressure() { return pressure; }

    public Date getTimestamp() { return timestamp; }
}
