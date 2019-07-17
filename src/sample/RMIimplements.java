package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.gui.alertController;
import sample.gui.homeController;

import java.beans.Expression;
import java.io.IOException;
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
    public void allarm(Patient patient, String event, int severity) {
        try{
            homeController controller = Datastore.allLoaders.get("dashboard").getController();
            controller.runAlarmLater(patient, event, severity);
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }
}
