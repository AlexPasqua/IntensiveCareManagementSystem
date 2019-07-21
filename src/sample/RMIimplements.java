package sample;

import sample.gui.HomeController;
import java.util.Map;


public class RMIimplements implements RMIinterface {

    @Override
    public void updateHeartbeats(Map <Patient, HeartBeat> heartbeats) {
        for(Patient p: heartbeats.keySet())
            Datastore.getPatients().get(Datastore.getPatients().indexOf(p)).addHeartBeat(heartbeats.get(p));
        Datastore.write();
    }

    @Override
    public void updatePressures(Map<Patient, Pressure> pressures) {
        for(Patient p: pressures.keySet())
            Datastore.getPatients().get(Datastore.getPatients().indexOf(p)).addPressure(pressures.get(p));
        Datastore.write();
    }

    @Override
    public void updateTemperatures(Map<Patient, Temperature> temps) {
        for(Patient p: temps.keySet())
            Datastore.getPatients().get(Datastore.getPatients().indexOf(p)).addTemperature(temps.get(p));
        Datastore.write();
    }

    @Override
    public void alarm(Patient patient, String event, int severity) {
        HomeController controller = Datastore.allLoaders.get("dashboard").getController();
        controller.runAlarmLater(patient, event, severity);

        //simulo dati
        switch (event){
            case "Ipertermia":{
                patient.addTemperature(new Temperature(40));
                break;
            }
            case "Ipotermia":{
                patient.addTemperature(new Temperature(34));
                break;
            }
        }
        Datastore.write();
    }
}
