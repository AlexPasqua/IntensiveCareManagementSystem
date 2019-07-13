package sample.gui;

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
import sample.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class reportPageController {

    private Report report;
    private Date[] dates = new Date[2];

    @FXML
    private Label labelFullName;

    @FXML
    private Label labelCod;

    @FXML
    private Label labelBirthDate;

    @FXML
    private Label labelBirthTown;

    @FXML
    private TableView<HeartBeat> tableHB;

    @FXML
    private TableColumn<HeartBeat, LocalDate> colTimestamp;

    @FXML
    private TableColumn<HeartBeat, Integer> colHB;

    @FXML
    private LineChart<String, Number> linechartHB;

    @FXML
    private TableView<Temperature> tableTemp;

    @FXML
    private TableColumn<Temperature, LocalDate> colTimestamp1;

    @FXML
    private TableColumn<Temperature, Integer> colTemp;

    @FXML
    private LineChart<?, ?> linechartTemp;

    @FXML
    private TableView<Pressure> tablePressure;

    @FXML
    private TableColumn<Pressure, LocalDate> colTimestamp2;

    @FXML
    private TableColumn<Pressure, Integer> colPressMin;

    @FXML
    private TableColumn<Pressure, Integer> colPressMax;

    @FXML
    private LineChart<?, ?> linechartPressure;

    @FXML
    void handleExportPDF(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Documento PDF", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            if (report.createPDF(file.toString())) {
                showDialog(Alert.AlertType.INFORMATION, "Documento esportato con successo!");
                Desktop.getDesktop().open(file);
            } else {
                showDialog(Alert.AlertType.ERROR, "Impossibile salvare il Report come PDF. Assicurati di avere i permessi per salvare il file in quella posizione.");
            }
        }

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
        linechartHB.getData().addAll(series1, series2);

        //TODO: temp
        //TODO: pression
    }

    void showDialog(Alert.AlertType type, String msg){
        Alert alert = new Alert(type);
        alert.setTitle("Esporta come PDF");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
