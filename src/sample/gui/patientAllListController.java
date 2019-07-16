package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Datastore;
import sample.Patient;
import sample.Prescription;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class patientAllListController implements Initializable {

    @FXML
    private TableView<Patient> patientsList;

    @FXML
    private TableColumn<Patient, String> colFullName;

    @FXML
    private TableColumn<Patient, String> colCod;

    @FXML
    private TableColumn<Patient, String> colBirth;

    @FXML
    private TableColumn<Patient, String> colCity;

    @FXML
    private TableColumn<Patient, String> colHospitalized;

    @FXML
    private TextField textSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Patient> patients = Datastore.getPatients();
        colFullName.setCellValueFactory(new PropertyValueFactory<>("FullName"));
        colCod.setCellValueFactory(new PropertyValueFactory<>("CodFis"));
        colBirth.setCellValueFactory(new PropertyValueFactory<>("BirthDate"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("BirthTown"));
        colHospitalized.setCellValueFactory(new PropertyValueFactory<>("Hospitalization"));

        if (!patients.isEmpty()){
            ObservableList<Patient> data = FXCollections.observableArrayList(Datastore.getPatients());
            patientsList.setItems(data);
            patientsList.getSelectionModel().selectFirst();
        }


    }

    @FXML
    private void handleSearch(){
        String text = textSearch.getText();
        Boolean found = false;
        for (Patient patient: patientsList.getItems()){
            if (patient.getFullName().contains(text) || patient.getCodFis().contains(text)){
                found = true;
                //TODO; finisci

            }
        }
    }
}