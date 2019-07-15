package sample;

import java.io.Serializable;
import java.util.Date;

public class Temperature implements Serializable {

    private final int temp;
    private final Date timestamp;

    public Temperature(int temp) {
        this.temp = temp;
        this.timestamp = new Date();
    }

    public Temperature(int temp, long millis) {
        this.temp = temp;
        this.timestamp = new Date(millis);
    }

    public int getTemperature(){ return temp; }

    public Date getTimestamp(){ return timestamp; }

    public String toString(){ return "Temperature [temp=" + temp + ", timestamp=" + timestamp.toString() + "]";  }

}
