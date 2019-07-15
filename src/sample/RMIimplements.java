package sample;

import java.beans.Expression;
import java.util.Map;

public class RMIimplements implements RMIinterface {

    /*
    * RMI INTERFACE
    * rmi functions declaration
     */
    @Override
    public void updateHeartbeats(Map <Patient, HeartBeat> heartbeats) {
        for(Patient p: heartbeats.keySet()){
            System.out.print("Heartbeat received. Patient:" + p.getFullName());
            System.out.println(" Value:"+ heartbeats.get(p).getHeartBeat());
        }
    }

    @Override
    public void updatePressures(Map<Patient, Pressure> pressures) {
        for(Patient p: pressures.keySet()){
            System.out.print("Pressions received. Patient:" + p.getFullName());
            System.out.println(" MIN: "+ pressures.get(p).getPressMin() + " MAX: " +pressures.get(p).getPressMax());
        }

    }

    @Override
    public void updateTemperatures(Map<Patient, Temperature> temps) {
        for(Patient p: temps.keySet()){
            System.out.print("Temperature received. Patient:" + p.getFullName());
            System.out.println(" Value:"+ temps.get(p).getTemperature());
        }


    }
}
