package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Datastore;
import sample.Patient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class patientPageController implements Initializable {

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddAdministration.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Aggiungi Somministrazione");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void handleAddDiagnosis(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addDiagnosis.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Aggiungi Diagnosi");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void handleAddPrescription(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addPrescription.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Aggiungi Prescrizione");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void handleGenerateReport(ActionEvent event) {

    }

    @FXML
    void handleRelease(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(getClass().getResourceAsStream("/imgs/user.png"));
        imageUser.setImage(image);
    }


    public void loadPatient(Integer patientId){
        ArrayList<Patient> patients = Datastore.getPatients();

        labelCodFis.setText(patients.get(patientId).getCodFis());
        labelName.setText(patients.get(patientId).getFullName());
        labelBirthDate.setText(patients.get(patientId).getDate().toString());
        labelBirthTown.setText(patients.get(patientId).getBirthTown());

    }
}
