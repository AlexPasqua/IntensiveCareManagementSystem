package sample.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.Datastore;
import sample.Patient;
import java.util.Map;


public class AddDischargeLetterController {

    Patient currentPatient;
    @FXML private TextArea textarea;


    @FXML
    private void saveDischargeLetter(ActionEvent event) {
        String letter = textarea.getText();

        if (letter.isEmpty()) {
            GUI.showDialog(Alert.AlertType.WARNING, "Warning", "Per salvare è necessario prima inserire del testo");
            return;
        }

        currentPatient.setDischargeLetter(letter);
        currentPatient.setHospitalization(false);
        Datastore.write();
        //updating all other windows
        for(Map.Entry<String, FXMLLoader> entry: Datastore.allLoaders.entrySet()){
            switch (entry.getKey()){
                case "dashboard":{
                    homeController controller = entry.getValue().getController();
                    controller.reset();
                    controller.loadList();
                    break;
                }
                case "patientslist":{
                    patientListController controller = entry.getValue().getController();
                    controller.reset();
                    controller.loadList();
                }
            }
        }

        Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
        stage.close();
    }


    void setCurrentPatient(Patient currentPatient){ this.currentPatient = currentPatient; }
}
