package sample;

import java.util.Date;

public class HeartBeat {

    private final int heartBeat;
    private final Date timestamp;

    public HeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
        this.timestamp = new Date();
    }

    public HeartBeat(int heartBeat, int millis) {
        this.heartBeat = heartBeat;
        this.timestamp = new Date(millis);
    }

    public int getHeartBeat(){ return heartBeat; }

    public Date getTimestamp(){ return timestamp; }

}
