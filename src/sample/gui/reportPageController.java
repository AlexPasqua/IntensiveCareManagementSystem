package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class reportPageController {

    private Patient currentPatient = null;
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
    void handleExportPDF(ActionEvent event) throws Exception{
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Documento PDF", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            CreatePDF builder = new CreatePDF(currentPatient, dates[0], dates[1]);
            builder.createPDF(file.toString());
        }

    }

    public void loadPatient(Patient currentPatient, Date from, Date to){
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

        //TODO: cerca di fare una funzione invece di ripetere il codice
        ArrayList<HeartBeat> heartbeats = new ArrayList<>();
        for (HeartBeat beat : currentPatient.getHeartBeats()){
            if (beat.getTimestamp().after(from)){
                if (beat.getTimestamp().before(to))
                    heartbeats.add(beat);
                else break;
            }
        }

        ArrayList<Temperature> temperatures = new ArrayList<>();
        for (Temperature temperature : currentPatient.getTemperatures()){
            if (temperature.getTimestamp().after(from)){
                if (temperature.getTimestamp().before(to))
                    temperatures.add(temperature);
                else break;
            }
        }

        ArrayList<Pressure> pressures = new ArrayList<>();
        for (Pressure pressure : currentPatient.getPressures()){
            if (pressure.getTimestamp().after(from)){
                if (pressure.getTimestamp().before(to))
                    pressures.add(pressure);
                else break;
            }
        }

        ObservableList<HeartBeat> data = FXCollections.observableArrayList(heartbeats);
        tableHB.setItems(data);
        tableHB.getSelectionModel().selectFirst();
        ObservableList<Temperature> data1 = FXCollections.observableArrayList(temperatures);
        tableTemp.setItems(data1);
        tableTemp.getSelectionModel().selectFirst();
        ObservableList<Pressure> data2 = FXCollections.observableArrayList(pressures);
        tablePressure.setItems(data2);
        tablePressure.getSelectionModel().selectFirst();

    }

}
