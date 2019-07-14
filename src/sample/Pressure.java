package sample;

import java.io.Serializable;
import java.util.Date;

public class Pressure implements Serializable {

    private final int[] pressure;
    private final Date timestamp;

    public Pressure(int min, int max){
        int[] pressure = {min, max};
        this.pressure = pressure;
        this.timestamp = new Date();
    }

    public Pressure(int min, int max, long millis){
        int[] pressure = {min, max};
        this.pressure = pressure;
        this.timestamp = new Date(millis);
    }

    public int[] getPressure() { return pressure; }

    public int getPressMin() { return pressure[0]; }

    public int getPressMax() { return pressure[1]; }

    public Date getTimestamp() { return timestamp; }

    public String formatted() { return pressure[0] + "-" + pressure[1]; }

    public String toString(){ return "Pressure [pressure=" + pressure.toString() + ", timestamp=" + timestamp.toString() + "]";  }

}
