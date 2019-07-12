package sample;

import java.io.Serializable;
import java.util.Date;

public class HeartBeat implements Serializable {

    private final int heartBeat;
    private final Date timestamp;

    public HeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
        this.timestamp = new Date();
    }

    public HeartBeat(int heartBeat, long millis) {
        this.heartBeat = heartBeat;
        this.timestamp = new Date(millis);
    }

    public int getHeartBeat(){ return heartBeat; }

    public Date getTimestamp(){ return timestamp; }

    public String toString(){ return "HeartBeat [heartBeat=" + heartBeat + ", timestamp=" + timestamp.toString() + "]";  }

}
