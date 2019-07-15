package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class patientPageController implements Initializable {

    Patient currentPatient = null;
    Stage thisStage;

    @FXML private Label labelName;
    @FXML private Label labelBirthTown;
    @FXML private Label labelCodFis;
    @FXML private Label labelBirthDate;
    @FXML private LineChart<String, Number> chartPressure;
    @FXML private LineChart<String, Number> chartHeartBeat;
    @FXML private LineChart<String, Number> chartTemperature;
    @FXML private ImageView imageUser;
    @FXML private Button buttonDiagnosis;
    @FXML private Button buttonPrescription;
    @FXML private Button buttonAdministration;
    @FXML private Button buttonReport;
    @FXML private Button buttonDischarge;


    @FXML
    void handleAddAdministration(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Prescrizione", "addAdministration.fxml", event);
        AddAministrationController controller = fxmlLoader.<AddAministrationController>getController();
        controller.setCurrentPatient(currentPatient);
    }

    @FXML
    void handleAddDiagnosis(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Diagnosi", "addDiagnosis.fxml", event);
        addDiagnosisController controller = fxmlLoader.<addDiagnosisController>getController();
        controller.setCurrentPatient(currentPatient);
    }

    @FXML
    void handleAddPrescription(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Prescrizione", "prescriptionList.fxml", event);
        prescriptionListController controller = fxmlLoader.<prescriptionListController>getController();
        controller.setCurrentPatient(currentPatient);
    }

    @FXML
    void handleGenerateReport(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = openPopupWindow("Genera Report", "reportAskDates.fxml", event);
        Window thiswindow = ((Node)event.getTarget()).getScene().getWindow();
        reportAskDatesController controller = fxmlLoader.getController();
        controller.setCurrentPatient(currentPatient, thiswindow);
    }

    @FXML
    void handleDischarge(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addDischargeLetter.fxml"));
        Parent root = fxmlLoader.load();

        Window thisWindow = ((Node)event.getTarget()).getScene().getWindow();
        thisStage = (Stage)((Node)event.getTarget()).getScene().getWindow();

        Stage dischargeStage = new Stage();
        dischargeStage.setTitle("Aggiungi lettera di dimissioni");
        dischargeStage.initModality(Modality.WINDOW_MODAL);
        dischargeStage.initOwner(thisWindow);
        dischargeStage.setScene(new Scene(root));
        dischargeStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_HIDDEN, this::closeWindowEvent);
        dischargeStage.show();

        AddDischargeLetterController controller = fxmlLoader.<AddDischargeLetterController>getController();
        controller.setCurrentPatient(currentPatient);
    }

    private void closeWindowEvent(WindowEvent event) {
        if (!this.currentPatient.getDischargeLetter().isEmpty()) {
            //TODO: sostituire thisStage.close() con il pannello che diceva francesco e togliere tutta la roba per ottenere lo stage di questa pagina, che non serve più
            thisStage.close();
            System.out.println("Patient discharged");
        }
        else
            System.out.println("AAAAAAAAAAAAAAAAA"); //TODO: remove the println
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(Datastore.getActiveUser());
        User activeUser = Datastore.getActiveUser();
        System.out.println("Active User: " + activeUser.getClass().getSimpleName());

        Image image = new Image(getClass().getResourceAsStream("/imgs/user.png"));
        imageUser.setImage(image);

        enableButtons();
    }


    public void loadPatient(Integer patientId){
        ArrayList<Patient> patients = Datastore.getPatients();
        currentPatient = patients.get(patientId);

        labelCodFis.setText(currentPatient.getCodFis());
        labelName.setText(currentPatient.getFullName());
        labelBirthDate.setText(currentPatient.getDate().toString());
        labelBirthTown.setText(currentPatient.getBirthTown());

        //HB chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("HB");
        for (HeartBeat beat: currentPatient.getHeartBeats()){
            if (beat.getTimestamp().after(new Date(System.currentTimeMillis() - 7200 * 1000)))
                series.getData().add(new XYChart.Data<>(beat.getTimestamp().toString(), beat.getHeartBeat()));
        }
        chartHeartBeat.getXAxis().setTickLabelsVisible(false);
        chartHeartBeat.getXAxis().setOpacity(0);
        chartHeartBeat.getData().add(series);

        //temp
        series = new XYChart.Series<>();
        series.setName("Temp");
        for (Temperature temp: currentPatient.getTemperatures()){
            if (temp.getTimestamp().after(new Date(System.currentTimeMillis() - 7200 * 1000)))
                series.getData().add(new XYChart.Data<>(temp.getTimestamp().toString(), temp.getTemperature()));

        }
        chartTemperature.getXAxis().setTickLabelsVisible(false);
        chartTemperature.getXAxis().setOpacity(0);
        chartTemperature.getData().add(series);

        //pressure
        series = new XYChart.Series<>();
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series.setName("Minima");
        series.setName("Massima");
        for (Pressure press: currentPatient.getPressures()){
            if (press.getTimestamp().after(new Date(System.currentTimeMillis() - 7200 * 1000))) {
                series.getData().add(new XYChart.Data<>(press.getTimestamp().toString(), press.getPressMin()));
                series1.getData().add(new XYChart.Data<>(press.getTimestamp().toString(), press.getPressMax()));
            }
        }
        chartPressure.getXAxis().setTickLabelsVisible(false);
        chartPressure.getXAxis().setOpacity(0);
        chartPressure.getData().add(series);
        chartPressure.getData().add(series1);

    }

    public void enableButtons(){
        System.out.println("Current power: " + Datastore.getActiveUser().getUserType());
        switch (Datastore.getActiveUser().getUserType()){
            case CHIEFDOCTOR:{
                buttonReport.setDisable(false);
                buttonDischarge.setDisable(false);
            }
            case DOCTOR:{
                buttonPrescription.setDisable(false);
                buttonDiagnosis.setDisable(false);
            }
            case NURSE: {
                buttonAdministration.setDisable(false);
            }
        }
    }

    @FXML
    void handleGenerateRand(ActionEvent event) {
        currentPatient.generateFakeData();
        showDialog(Alert.AlertType.INFORMATION, "Dati generati corretamente!");
        Datastore.write();
    }

    @FXML
    void handleClearClinicalData(ActionEvent event) {
        currentPatient.clearClinicalData();
        showDialog(Alert.AlertType.INFORMATION, "Dati cancellati!");
        Datastore.write();

    }

    void showDialog(Alert.AlertType type, String msg){
        Alert alert = new Alert(type);
        alert.setTitle("Nuovo Utente");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    FXMLLoader openPopupWindow(String title, String fxml, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        Window thisWindow = ((Node)event.getTarget()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thisWindow);
        stage.setScene(new Scene(root));
        stage.show();

        return fxmlLoader;
    }
}
