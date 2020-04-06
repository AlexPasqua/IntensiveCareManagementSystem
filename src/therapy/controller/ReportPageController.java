package therapy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import therapy.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ReportPageController {
    private Report report;
    @FXML private Label labelFullName;
    @FXML private Label labelCod;
    @FXML private Label labelBirthDate;
    @FXML private Label labelBirthTown;
    @FXML private TableView<HeartBeat> tableHB;
    @FXML private TableColumn<HeartBeat, LocalDate> colTimestamp;
    @FXML private TableColumn<HeartBeat, Integer> colHB;
    @FXML private LineChart<String, Number> linechartHB;
    @FXML private TableView<Temperature> tableTemp;
    @FXML private TableColumn<Temperature, LocalDate> colTimestamp1;
    @FXML private TableColumn<Temperature, Integer> colTemp;
    @FXML private LineChart<String, Number> linechartTemp;
    @FXML private TableView<Pressure> tablePressure;
    @FXML private TableColumn<Pressure, LocalDate> colTimestamp2;
    @FXML private TableColumn<Pressure, Integer> colPressMin;
    @FXML private TableColumn<Pressure, Integer> colPressMax;
    @FXML private LineChart<String, Number> linechartPressure;


    @FXML
    void handleExportPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Documento PDF", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        try {
            if (file != null) {
                if (report.createPDF(file.toString())) {
                    GUI.showDialog(Alert.AlertType.INFORMATION, "Esporta come pdf", "Documento esportato con successo!");
                    Desktop.getDesktop().open(file);
                } else {
                    GUI.showDialog(Alert.AlertType.ERROR, "Esporta come pdf", "Impossibile salvare il Report come PDF. Assicurati di avere i permessi per salvare il file in quella posizione.");
                }
            }
        } catch (IOException e) {}
    }


    public void loadPatient(Report report){
        this.report = report;

        labelFullName.setText(report.getPatient().getFullName());
        labelCod.setText(report.getPatient().getCodFis());
        labelBirthDate.setText(report.getPatient().getBirthDate().toString());
        labelBirthTown.setText(report.getPatient().getBirthTown());

        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colTimestamp1.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colTimestamp2.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colHB.setCellValueFactory(new PropertyValueFactory<>("heartBeat"));
        colTemp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        colPressMax.setCellValueFactory(new PropertyValueFactory<>("pressMax"));
        colPressMin.setCellValueFactory(new PropertyValueFactory<>("pressMin"));

        ObservableList<HeartBeat> data = FXCollections.observableArrayList(report.getHeartBeats());
        tableHB.setItems(data);
        ObservableList<Temperature> data1 = FXCollections.observableArrayList(report.getTemperatures());
        tableTemp.setItems(data1);
        ObservableList<Pressure> data2 = FXCollections.observableArrayList(report.getPressures());
        tablePressure.setItems(data2);

        drawChart();
    }


    private void drawChart() {
        //hb
        TreeMap<String, HeartBeat[]> beats = report.getMaxMinHeartBeat();
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series1.setName("Minimo");
        series2.setName("Massimo");
        for (Map.Entry<String, HeartBeat[]> entry : beats.entrySet()) {
            String tmpString = entry.getKey();
            HeartBeat[] tmpValue = entry.getValue();
            XYChart.Data<String, Number> d1 = new XYChart.Data<>(tmpString, tmpValue[0].getHeartBeat());
            XYChart.Data<String, Number> d2 = new XYChart.Data<>(tmpString, tmpValue[1].getHeartBeat());
            series1.getData().add(d1);
            series2.getData().add(d2);
        }
        linechartHB.setAnimated(false);
        linechartHB.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
        linechartHB.getData().addAll(series1, series2);

        //Temps
        TreeMap<String, Temperature[]> temps = report.getMaxMinTemperature();
        series1 = new XYChart.Series<>();
        series2 = new XYChart.Series<>();
        series1.setName("Minimo");
        series2.setName("Massimo");
        for (Map.Entry<String, Temperature[]> entry : temps.entrySet()) {
            String tmpString = entry.getKey();
            Temperature[] tmpValue = entry.getValue();
            XYChart.Data<String, Number> d1 = new XYChart.Data<>(tmpString, tmpValue[0].getTemperature());
            XYChart.Data<String, Number> d2 = new XYChart.Data<>(tmpString, tmpValue[1].getTemperature());
            series1.getData().add(d1);
            series2.getData().add(d2);
        }
        linechartTemp.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
        linechartTemp.getData().addAll(series1, series2);

        //Pressions
        TreeMap<String, ArrayList<Pressure>> pressures = report.getDailyPressure();
        series1 = new XYChart.Series<>();
        series2 = new XYChart.Series<>();
        series1.setName("Minimo");
        series2.setName("Massimo");
        for (Map.Entry<String, ArrayList<Pressure>> listpress  : pressures.entrySet()){
            int[] maxmin = {0,0};
            for (Pressure press: listpress.getValue()) {
                maxmin[0] += press.getPressMin();
                maxmin[1] += press.getPressMax();
            }
            maxmin[0] /= listpress.getValue().size();
            maxmin[1] /= listpress.getValue().size();
            XYChart.Data<String, Number> d1 = new XYChart.Data<>(listpress.getKey(), maxmin[0]);
            XYChart.Data<String, Number> d2 = new XYChart.Data<>(listpress.getKey(), maxmin[1]);
            series1.getData().add(d1);
            series2.getData().add(d2);
        }
        linechartPressure.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
        linechartPressure.getData().addAll(series1, series2);
    }
}
