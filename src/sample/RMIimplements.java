package sample;

import java.beans.Expression;
import java.util.Map;

public class RMIimplements implements RMIinterface {


    @Override
    public void updateHeartbeats(Map <Patient, HeartBeat> heartbeats) {
        /*
        for(Patient p: heartbeats.keySet()){
            System.out.print("Heartbeat received. Patient:" + p.getFullName());
            System.out.println(" Value:"+ heartbeats.get(p).getHeartBeat());
        }
        */
        for(Patient p: heartbeats.keySet())
            Datastore.getPatients().get(Datastore.getPatients().indexOf(p)).addHeartBeat(heartbeats.get(p));


    }

    @Override
    public void updatePressures(Map<Patient, Pressure> pressures) {
        /*
        for(Patient p: pressures.keySet()){
            System.out.print("Pressions received. Patient:" + p.getFullName());
            System.out.println(" MIN: "+ pressures.get(p).getPressMin() + " MAX: " +pressures.get(p).getPressMax());
        }
        */
        for(Patient p: pressures.keySet())
            Datastore.getPatients().get(Datastore.getPatients().indexOf(p)).addPressure(pressures.get(p));

    }

    @Override
    public void updateTemperatures(Map<Patient, Temperature> temps) {
        /*
        for(Patient p: temps.keySet()){
            System.out.print("Temperature received. Patient:" + p.getFullName());
            System.out.println(" Value:"+ temps.get(p).getTemperature());
        }
        */
        for(Patient p: temps.keySet())
            Datastore.getPatients().get(Datastore.getPatients().indexOf(p)).addTemperature(temps.get(p));


    }
}
