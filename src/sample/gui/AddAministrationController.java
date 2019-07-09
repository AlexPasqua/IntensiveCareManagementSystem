package sample.gui;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.Patient;
import sample.Prescription;

import java.util.*;

public class AddAministrationController {

    private Patient currentPatient = null;

    @FXML
    private ComboBox<String> comboboxPrescriptions;

    @FXML
    private TextField textboxDose;

    @FXML
    private TextArea textareaExtra;


    public void setCurrentPatient(Patient currentPatient){
        this.currentPatient = currentPatient;
        loadComboBox();
    }

    private void loadComboBox(){
        ArrayList<String> items = new ArrayList<>();
        for(Prescription prescription: currentPatient.getPrescriptions()) {
            items.add(prescription.forComboBox());
        }
        comboboxPrescriptions.setItems(FXCollections.observableArrayList(items));
    }
}
