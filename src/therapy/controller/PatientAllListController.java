package therapy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import therapy.Datastore;
import therapy.Patient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class PatientAllListController implements Initializable {
    @FXML private TableView<Patient> patientsList;
    @FXML private TableColumn<Patient, String> colFullName;
    @FXML private TableColumn<Patient, String> colCod;
    @FXML private TableColumn<Patient, String> colBirth;
    @FXML private TableColumn<Patient, String> colCity;
    @FXML private TableColumn<Patient, String> colHospitalized;
    @FXML private TextField textSearch;

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

        ObservableList<Patient> obpatients = FXCollections.observableArrayList(patients);
        FilteredList<Patient> filteredData = new FilteredList<>(obpatients, p -> true);

        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return (person.getFullName().toLowerCase().contains(lowerCaseFilter) ||
                        person.getCodFis().toLowerCase().contains(lowerCaseFilter));
            });
        });
        SortedList<Patient> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(patientsList.comparatorProperty());
        patientsList.setItems(sortedData);
    }


    @FXML
    void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2 ){
             openPatient(patientsList.getSelectionModel().getSelectedItem());
        }
    }


    void openPatient(Patient patient) {
        if (Datastore.allLoaders.containsKey("patientPage" + patient.getCodFis())){
            GUI.showDialog(Alert.AlertType.WARNING, "Paziente", "La scheramata relativa a questo paziente è già aperta");
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../gui/patientPage.fxml"));
                Parent root1 = fxmlLoader.load();
                PatientPageController controller = fxmlLoader.<PatientPageController>getController();
                controller.loadPatient(patient);

                Stage stage = new Stage();
                stage.setTitle("Paziente");
                stage.setScene(new Scene(root1));
                Datastore.allLoaders.put("patientPage" + patient.getCodFis(), fxmlLoader);
                stage.setOnCloseRequest((WindowEvent event1) -> {
                    Datastore.allLoaders.remove("patientPage" + patient.getCodFis());
                });
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
                stage.show();
            } catch (IOException e) {
                GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente non è possibile aprire la pagina del paziente");
            }
        }
    }
}