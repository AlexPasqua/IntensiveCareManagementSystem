package sample;

import java.beans.Expression;
import java.util.Map;

public class RMIimplements implements RMIinterface {
    @Override
    public void updateHeartbeats(Map <Patient, HeartBeat> heartbeats) {
        System.out.println("heartbeat received: " + heartbeats);
    }

    @Override
    public void updatePressures(Map<Patient, Pressure> pressures) {
        System.out.println("pressions received. MIN: " + pressures);
        //System.exit(0);

    }

    @Override
    public void updateTemperatures(Map<Patient, Temperature> temps) {
        System.out.println("temperature received: " + temps);


    }
}
