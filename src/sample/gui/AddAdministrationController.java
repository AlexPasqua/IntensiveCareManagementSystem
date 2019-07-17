package sample.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Administration;
import sample.Datastore;
import sample.Patient;
import sample.Prescription;
import java.util.*;


public class AddAdministrationController {

    private Patient currentPatient = null;
    @FXML private ComboBox<String> comboboxPrescriptions;
    @FXML private TextField textboxDose;
    @FXML private TextArea textareaExtra;


    public void setCurrentPatient(Patient currentPatient){
        this.currentPatient = currentPatient;
        loadComboBox();
    }

    private void loadComboBox(){
        ArrayList<String> items = new ArrayList<>();
        for(Prescription prescription: currentPatient.getPrescriptions()) {
            items.add(prescription.forComboBox());
        }
        System.out.println(items);
        comboboxPrescriptions.setItems(FXCollections.observableArrayList(items));
    }

    @FXML
    private void addAdministration(ActionEvent event) {
        int dose = 0;
        try{ dose = Integer.parseInt(textboxDose.getText().trim()); }
        catch (NumberFormatException e){
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Il campo \"Dose somministrata\" deve contenere un numero");
            return;
        }

        Prescription presc = currentPatient.getPrescriptions().get(comboboxPrescriptions.getSelectionModel().getSelectedIndex());

        currentPatient.addAdministration(new Administration(presc, dose, textareaExtra.getText()));
        GUI.showDialog(Alert.AlertType.INFORMATION, "Info", "Somministrazione aggiunta");
        Datastore.write();
        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();
    }
}
