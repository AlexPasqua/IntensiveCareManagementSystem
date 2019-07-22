package therapy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import therapy.Datastore;
import therapy.Patient;
import therapy.Prescription;
import java.io.IOException;

public class PrescriptionListController {
    private Patient currentPatient;
    @FXML private TableView<Prescription> prescriptionList;
    @FXML private TableColumn<Prescription, String> pTimestamp;
    @FXML private TableColumn<Prescription, String> pDoctor;
    @FXML private TableColumn<Prescription, String> pMed;
    @FXML private TableColumn<Prescription, String> pDuration;
    @FXML private TableColumn<Prescription, String> pDaily;
    @FXML private TableColumn<Prescription, String> pQuantity;
    @FXML private Button addNewButton;
    @FXML private Button deleteButton;

    @FXML
    void handleNewPrescription(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Prescrizione", "../gui/addPrescription.fxml", event);
            AddPrescriptionController controller = fxmlLoader.<AddPrescriptionController>getController();
            controller.setCurrentPatient(currentPatient);
        } catch (IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile aggiungere una prescrizione");
        }
    }

    @FXML
    void handleDeletePrescription(ActionEvent event) {
        for (Prescription p: currentPatient.getPrescriptions()){
            if(p.equals(prescriptionList.getSelectionModel().getSelectedItem())) {
                currentPatient.getPrescriptions().remove(prescriptionList.getSelectionModel().getSelectedItem());
                break;
            }
        }
        Datastore.write();
        loadList();
    }

    void setCurrentPatient(Patient patient){

        currentPatient = patient;

        pTimestamp.setCellValueFactory(new PropertyValueFactory<Prescription,String>("timestamp"));
        pDoctor.setCellValueFactory(new PropertyValueFactory<Prescription,String>("doctor"));
        pMed.setCellValueFactory(new PropertyValueFactory<Prescription,String>("medicine"));
        pDuration.setCellValueFactory(new PropertyValueFactory<Prescription,String>("therapyDuration"));
        pDaily.setCellValueFactory(new PropertyValueFactory<Prescription,String>("dailyDoses"));
        pQuantity.setCellValueFactory(new PropertyValueFactory<Prescription,String>("mgDose"));

        if (!patient.getPrescriptions().isEmpty())
            loadList();

        if (!patient.getHospitalization()) {
            addNewButton.setDisable(true);
            deleteButton.setDisable(true);
        }


    }

    FXMLLoader openPopupWindow(String title, String fxml, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        Window thiswindow = ((Node)event.getTarget()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thiswindow);
        stage.setScene(new Scene(root));
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_HIDDEN, this::closeWindowEvent);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
        stage.show();

        return fxmlLoader;
    }

    private void closeWindowEvent(WindowEvent event) {
        loadList();
    }

    private void loadList() {
        ObservableList<Prescription> data = FXCollections.observableArrayList(currentPatient.getPrescriptions());
        prescriptionList.setItems(data);
        prescriptionList.getSelectionModel().selectFirst();
    }

}
