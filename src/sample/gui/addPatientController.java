package sample.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Datastore;
import sample.Patient;


import java.io.IOException;



public class addPatientController {
    @FXML
    private TextField textboxCod;

    @FXML
    private TextField textboxSurname;

    @FXML
    private TextField textboxName;

    @FXML
    private TextField textboxBirthTown;

    @FXML
    private DatePicker textboxBirthDate;

    @FXML
    void handleConfirm(ActionEvent e) throws IOException{
        //controllo codice fiscale
        String code = textboxCod.getText().trim();
        if (code.isEmpty() || code.length() != 16) {
            showDialog(Alert.AlertType.ERROR, "Codice Fiscale vuoto o non valido");
            return;
        }
        if (textboxSurname.getText().isEmpty() || textboxName.getText().isEmpty() || textboxBirthTown.getText().isEmpty() || textboxBirthDate.getValue() == null){
            showDialog(Alert.AlertType.WARNING, "Tutti i campi sono obbligatori");
            return;
        }

        Patient newpatient = new Patient(code, textboxName.getText(), textboxSurname.getText(), textboxBirthDate.getValue(), textboxBirthTown.getText());
        Datastore.addPatient(newpatient);
        Datastore.write();

        showDialog(Alert.AlertType.INFORMATION, "Paziente Aggiunto correttamente!");

        Stage stage = (Stage)((Node)e.getTarget()).getScene().getWindow();
        stage.close();
    }

    void showDialog(Alert.AlertType type, String msg){
        Alert alert = new Alert(type);
        alert.setTitle("Nuovo Utente");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
