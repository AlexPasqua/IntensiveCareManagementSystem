package sample.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.Datastore;
import sample.Patient;


public class AddDischargeLetterController {

    Patient currentPatient;

    @FXML
    private TextArea textarea;

    @FXML
    private void saveDischargeLetter(ActionEvent event) {
        String letter = textarea.getText();

        if (letter.isEmpty()) {
            showDialog(Alert.AlertType.WARNING, "Per salvare è necessario prima inserire del testo");
            return;
        }

        currentPatient.setDischargeLetter(letter);
        currentPatient.setHospitalization(false);
        Datastore.write();


        //showDialog(Alert.AlertType.INFORMATION, "Lettera di dimissioni compilata");

        Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();
        stage.close();
    }

    void setCurrentPatient(Patient currentPatient){ this.currentPatient = currentPatient; }

    private void showDialog(Alert.AlertType type, String msg){
        Alert alert = new Alert(type);
        alert.setTitle("Scrivi lettera di dimissioni");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
