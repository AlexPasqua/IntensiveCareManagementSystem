package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.Datastore;
import sample.Patient;
import sample.User;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class patientPageController implements Initializable {

    Patient currentPatient = null;

    @FXML
    private Label labelName;

    @FXML
    private Label labelBirthTown;

    @FXML
    private Label labelCodFis;

    @FXML
    private Label labelBirthDate;

    @FXML
    private LineChart<?, ?> chartPressure;

    @FXML
    private LineChart<?, ?> chartHeartBeat;

    @FXML
    private LineChart<?, ?> chartTemperature;

    @FXML
    private ImageView imageUser;

    @FXML
    void handleAddAdministration(ActionEvent event) throws Exception {
        openPopupWindow("Aggiungi Somministrazione", "AddAdministration.fxml", event);
    }

    @FXML
    void handleAddDiagnosis(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Diagnosi", "addDiagnosis.fxml", event);
        addDiagnosisController controller = fxmlLoader.<addDiagnosisController>getController();
        controller.setCurrentPatient(currentPatient);
    }

    @FXML
    void handleAddPrescription(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Prescrizione", "addPrescription.fxml", event);
        AddPrescriptionController controller = fxmlLoader.<AddPrescriptionController>getController();
        controller.setCurrentPatient(currentPatient);
    }

    @FXML
    void handleGenerateReport(ActionEvent event) {

    }

    @FXML
    void handleRelease(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User activeUser = Datastore.getActiveUser();
        System.out.println("Active User: " + activeUser);

        Image image = new Image(getClass().getResourceAsStream("/imgs/user.png"));
        imageUser.setImage(image);
    }


    public void loadPatient(Integer patientId){
        ArrayList<Patient> patients = Datastore.getPatients();
        currentPatient = patients.get(patientId);

        labelCodFis.setText(currentPatient.getCodFis());
        labelName.setText(currentPatient.getFullName());
        labelBirthDate.setText(currentPatient.getDate().toString());
        labelBirthTown.setText(currentPatient.getBirthTown());

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
        stage.show();

        return fxmlLoader;
    }
}
