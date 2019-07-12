package sample;

import java.util.Date;

public class Temperature {

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

}
