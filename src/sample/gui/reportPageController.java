package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.*;

import java.time.LocalDate;

public class reportPageController {

    private Patient currentPatient = null;
    private LocalDate[] dates = new LocalDate[2];

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
    private LineChart<?, ?> linechartHB;

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
    void handleExportPDF(ActionEvent event) {

    }

    public void loadPatient(Patient currentPatient, LocalDate from, LocalDate to){
        this.currentPatient = currentPatient;
        dates[0] = from;
        dates[1] = to;

        labelFullName.setText(currentPatient.getFullName());
        labelCod.setText(currentPatient.getCodFis());
        labelBirthDate.setText(currentPatient.getBirthDate().toString());
        labelBirthTown.setText(currentPatient.getBirthTown());

        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colTimestamp1.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colTimestamp2.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colHB.setCellValueFactory(new PropertyValueFactory<>("heartBeat"));
        colTemp.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        colPressMax.setCellValueFactory(new PropertyValueFactory<>("pressMax"));
        colPressMin.setCellValueFactory(new PropertyValueFactory<>("pressMin"));

        ObservableList<HeartBeat> data = FXCollections.observableArrayList(currentPatient.getHeartBeats());
        tableHB.setItems(data);
        tableHB.getSelectionModel().selectFirst();
        ObservableList<Temperature> data1 = FXCollections.observableArrayList(currentPatient.getTemperatures());
        tableTemp.setItems(data1);
        tableTemp.getSelectionModel().selectFirst();
        ObservableList<Pressure> data2 = FXCollections.observableArrayList(currentPatient.getPressures());
        tablePressure.setItems(data2);
        tablePressure.getSelectionModel().selectFirst();

    }

}
