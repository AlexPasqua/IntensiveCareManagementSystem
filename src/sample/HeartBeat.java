package sample;

import java.util.Date;

public class HeartBeat {

    private final int heartBeat;
    private final Date timestamp;

    public HeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
        this.timestamp = new Date();
    }

    public int getHeartBeat(){ return heartBeat; }

    public Date getTimestamp(){ return timestamp; }

}
