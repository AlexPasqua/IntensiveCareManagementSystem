package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import sample.Datastore;
import sample.Patient;
import sample.Prescription;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPrescriptionController {

    private Patient currentPatient = null;

    @FXML
    private ComboBox<String> medList;

    @FXML
    private TextField textboxDuration;

    @FXML
    private TextField textboxDose;

    @FXML
    private TextField textboxDailyDose;

    // add a new Medicine
    @FXML
    void handleAddMed(ActionEvent event)throws Exception{
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
            showDialog(Alert.AlertType.WARNING, "Warning", "Tutti i campi sono obbligatori");
        } else {
            currentPatient.addPrescription(new Prescription(medList.getSelectionModel().getSelectedItem(), Integer.parseInt(textboxDuration.getText()), Integer.parseInt(textboxDailyDose.getText()), Integer.parseInt(textboxDose.getText()), Datastore.getActiveUser()));
            showDialog(Alert.AlertType.INFORMATION, "Info", "Prescrizione Aggiunta");
            Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
            stage.close();
        }
    }

    public void setCurrentPatient(Patient currentpatient){
        this.currentPatient = currentpatient;
        loadComboBox();
    }

    //carico i dati nella combo box
    private void loadComboBox(){
        if (!Datastore.getAvailMeds().isEmpty()){
            medList.getItems().removeAll(medList.getItems());
            ObservableList<String> meds2 = FXCollections.observableArrayList(Datastore.getAvailMeds());
            medList.getItems().addAll(meds2);
            medList.getSelectionModel().selectLast();
        }
    }

    private void showDialog(Alert.AlertType type, String title, String msg){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
