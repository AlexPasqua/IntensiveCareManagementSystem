package therapy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import therapy.Datastore;
import therapy.Patient;
import therapy.Prescription;
import java.util.Optional;

public class AddPrescriptionController {
    private Patient currentPatient = null;
    @FXML private ComboBox<String> medList;
    @FXML private TextField textboxDuration;
    @FXML private TextField textboxDose;
    @FXML private TextField textboxDailyDose;


    @FXML
    void handleAddMed(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Aggiunta Farmaco");
        dialog.setHeaderText("Aggiunta Farmaco");
        dialog.setContentText("Nome del farmaco:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            Datastore.addMed(result.get());
            Datastore.write();
            loadComboBox();
        }
    }


    @FXML
    void handleConfirm(ActionEvent event){
        if (textboxDuration.getText().isEmpty() || textboxDose.getText().isEmpty() || medList.getSelectionModel().getSelectedItem().isEmpty()){
            GUI.showDialog(Alert.AlertType.WARNING, "Warning", "Tutti i campi sono obbligatori");
        } else {
            currentPatient.addPrescription(new Prescription(medList.getSelectionModel().getSelectedItem(), Integer.parseInt(textboxDuration.getText()), Integer.parseInt(textboxDailyDose.getText()), Integer.parseInt(textboxDose.getText()), Datastore.getActiveUser()));
            Datastore.write();
            GUI.showDialog(Alert.AlertType.INFORMATION, "Info", "Prescrizione aggiunta correttamente");
            Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
            stage.close();
        }
    }


    public void setCurrentPatient(Patient currentpatient){
        this.currentPatient = currentpatient;
        loadComboBox();
    }


    // load data into the combo box
    private void loadComboBox(){
        if (!Datastore.getAvailMeds().isEmpty()){
            medList.getItems().removeAll(medList.getItems());
            ObservableList<String> meds2 = FXCollections.observableArrayList(Datastore.getAvailMeds());
            medList.getItems().addAll(meds2);
            medList.getSelectionModel().selectLast();
        }
    }
}
