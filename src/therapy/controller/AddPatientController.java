package therapy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import therapy.Datastore;
import therapy.Patient;

import java.time.LocalDate;
import java.time.ZoneId;


public class AddPatientController {
    @FXML private TextField textboxCod;
    @FXML private TextField textboxSurname;
    @FXML private TextField textboxName;
    @FXML private TextField textboxBirthTown;
    @FXML private DatePicker textboxBirthDate;


    @FXML
    void handleConfirm(ActionEvent e) {
        //controllo codice fiscale
        String code = textboxCod.getText().trim();
        if (code.isEmpty() || code.length() != 16) {
            GUI.showDialog(Alert.AlertType.WARNING, "Warning", "Codice Fiscale vuoto o non valido");
            return;
        }

        if (textboxSurname.getText().isEmpty() || textboxName.getText().isEmpty() || textboxBirthTown.getText().isEmpty() || textboxBirthDate.getValue() == null){
            GUI.showDialog(Alert.AlertType.WARNING, "Warning", "Tutti i campi sono obbligatori");
            return;
        }

        if (textboxBirthDate.getValue().isAfter(LocalDate.now(ZoneId.systemDefault()))) {
            GUI.showDialog(Alert.AlertType.WARNING, "Warning", "La data di nascita del paziente non può essere nel futuro");
            return;
        }

        Patient newpatient = new Patient(code, textboxName.getText(), textboxSurname.getText(), textboxBirthDate.getValue(), textboxBirthTown.getText());
        Datastore.addPatient(newpatient);
        Datastore.write();

        GUI.showDialog(Alert.AlertType.INFORMATION, "Info", "Paziente Aggiunto correttamente!");

        Stage stage = (Stage)((Node)e.getTarget()).getScene().getWindow();
        stage.close();
    }
}
