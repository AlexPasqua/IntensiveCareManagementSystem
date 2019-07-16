package sample.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import sample.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Timer;

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
    @FXML private GridPane gridCharts;
    @FXML private Label labelLetter;

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
            showHospitalizedView();
            System.out.println("Patient discharged");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(Datastore.getActiveUser());
        User activeUser = Datastore.getActiveUser();
        System.out.println("Active User: " + activeUser.getClass().getSimpleName());

        Image image = new Image(getClass().getResourceAsStream("/imgs/user.png"));
        imageUser.setImage(image);

        enableButtons();
        chartHeartBeat.getXAxis().setAutoRanging(true);

    }


    public void loadPatient(Integer patientId){
        ArrayList<Patient> patients = Datastore.getPatients();
        currentPatient = patients.get(patientId);

        labelCodFis.setText(currentPatient.getCodFis());
        labelName.setText(currentPatient.getFullName());
        labelBirthDate.setText(currentPatient.getDate().toString());
        labelBirthTown.setText(currentPatient.getBirthTown());

        if (!currentPatient.getHospitalization()) showHospitalizedView();
        //load charts and update them automaticaly updates charts every x time
        loadCharts();
        updateCharts();
    }

    private void loadCharts(){
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
        chartHeartBeat.setTitle("Battito");
        chartHeartBeat.setAnimated(false);

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
        chartTemperature.setAnimated(false);

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
        chartPressure.setAnimated(false);
    }

    public void enableButtons(){
        switch (Datastore.getActiveUser().getUserType()) {
            case CHIEFDOCTOR: {
                buttonReport.setDisable(false);
                buttonDischarge.setDisable(false);
            }
            case DOCTOR: {
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

    private void showHospitalizedView(){
        gridCharts.setVisible(false);
        buttonDiagnosis.setDisable(true);
        buttonDischarge.setDisable(true);
        labelLetter.setText(currentPatient.getDischargeLetter());
    }
    /*
    private void removeOld(LineChart chart){
        XYChart.Series<String, Number> currentSeries = (XYChart.Series<String, Number>) chart.getData().get(0);
        try{
            Date first = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(currentSeries.getData().get(0).getXValue());
            if (first.before(new Date(System.currentTimeMillis() - 7200 * 1000))){
                currentSeries.getData().remove(0);
                System.out.println("Removed firse");
                removeOld(chart);
            }
        } catch (Exception e) { System.out.println(e.getMessage());}

    }
    */ //TODO cancellare se dopo controllo ottimizzazione non serve più

    public void updateCharts() {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), ev -> {
                System.out.println("Updating charts!");
                chartHeartBeat.getData().clear();
                chartPressure.getData().clear();
                chartTemperature.getData().clear();
                loadCharts();
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

    }

}
