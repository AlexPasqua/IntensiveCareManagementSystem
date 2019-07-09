package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import sample.Patient;
import sample.Prescription;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPrescriptionController {

    private Patient currentPatient = null;

    @FXML
    private ComboBox<String> medList;


    // add a new Medicine
    @FXML
    void handleAddMed(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Aggiunta Farmaco");
        dialog.setHeaderText("Aggiunta Farmaco");
        dialog.setContentText("Nome del farmaco:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            Prescription.availMeds.add(result.toString());
            loadComboBox();
        }

    }

    public void setCurrentPatient(Patient currentpatient){
        this.currentPatient = currentpatient;
        loadComboBox();

    }

    //carico i dati nella combo box
    private void loadComboBox(){
        if (!Prescription.availMeds.isEmpty()){
            medList.getItems().removeAll(medList.getItems());
            ObservableList<String> meds2 = FXCollections.observableArrayList(Prescription.availMeds);
            medList.getItems().addAll(meds2);
            medList.getSelectionModel().selectLast();
        }
    }
}
