package sample.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.event.ActionEvent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import sample.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;


public class homeController implements Initializable {
    @FXML private ScrollPane scrollPane;
    @FXML LineChart<String, Number> grafico11;
    @FXML LineChart<String, Number> grafico12;
    @FXML LineChart<String, Number> grafico13;
    @FXML LineChart<String, Number> grafico21;
    @FXML LineChart<String, Number> grafico22;
    @FXML LineChart<String, Number> grafico23;
    @FXML LineChart<String, Number> grafico31;
    @FXML LineChart<String, Number> grafico32;
    @FXML LineChart<String, Number> grafico33;
    @FXML LineChart<String, Number> grafico41;
    @FXML LineChart<String, Number> grafico42;
    @FXML LineChart<String, Number> grafico43;
    @FXML LineChart<String, Number> grafico51;
    @FXML LineChart<String, Number> grafico52;
    @FXML LineChart<String, Number> grafico53;
    @FXML LineChart<String, Number> grafico61;
    @FXML LineChart<String, Number> grafico62;
    @FXML LineChart<String, Number> grafico63;
    @FXML LineChart<String, Number> grafico71;
    @FXML LineChart<String, Number> grafico72;
    @FXML LineChart<String, Number> grafico73;
    @FXML LineChart<String, Number> grafico81;
    @FXML LineChart<String, Number> grafico82;
    @FXML LineChart<String, Number> grafico83;
    @FXML LineChart<String, Number> grafico91;
    @FXML LineChart<String, Number> grafico92;
    @FXML LineChart<String, Number> grafico93;
    @FXML GridPane gridRows;


    @FXML
    void handleAddValue(ActionEvent event){
        //pressione max value is 8
        //addDataTo(grafico1,  20, 0);

        //addDataTo(grafico01,  55, 1);
        reset();
    }


    @FXML
    void handleLogin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (IOException e){
            GUI.showDialog(Alert.AlertType.ERROR, "Login error", "Impossibile eseguire login");
            handleQuit();
        }

    }


    @FXML
    void handleAbout(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("Ciao!\nSoftware sviluppato da\nFrancesco Fattori, Giacomo Frigo e Alex Pasquali");
        alert.showAndWait();
    }


    //method to help
    @SuppressWarnings("unchecked")
    private static void addDataTo(LineChart linechart, Integer y, Integer serieIndex){
        XYChart.Series currentSeries = (XYChart.Series) linechart.getData().get(serieIndex);
        //delete first data
        currentSeries.getData().remove(0);
        int numOfPoint = currentSeries.getData().size();

        for(int i=0; i < numOfPoint; i++) {
            //reduce XValue
            XYChart.Data<Number, Number> data = (XYChart.Data<Number, Number>)currentSeries.getData().get(i);
            int x = (int) data.getXValue();
            System.out.println("X Value: " + x);
            data.setXValue(x - 1);
        }
        currentSeries.getData().add(new XYChart.Data<>(numOfPoint, y));
    }


    @FXML
    private void handleQuit() {
        Datastore.write();
        Platform.exit();
        System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateCharts();
    }


    public void loadList(){
        ArrayList<Patient> patients = Datastore.getHospitalizedPatients();
        ObservableList<Node> rows = gridRows.getChildren();

        if (patients.size() > 0){
            scrollPane.setStyle("");
            for (int i = 0; i<patients.size(); i++){
                AnchorPane thisrow = (AnchorPane) rows.get(i);
                thisrow.setStyle("");
                //HB chart
                LineChart chart =  getChartByRow(thisrow, 0);
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("HB");
                for (HeartBeat beat: patients.get(i).getHeartBeats()){
                    if (beat.getTimestamp().after(new Date(System.currentTimeMillis() - 900 * 1000)))
                        series.getData().add(new XYChart.Data<>(beat.getTimestamp().toString(), beat.getHeartBeat()));
                }
                chart.getXAxis().setTickLabelsVisible(false);
                chart.getXAxis().setOpacity(0);
                chart.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
                chart.getData().add(series);

                //temp
                chart = getChartByRow(thisrow, 1);
                series = new XYChart.Series<>();
                series.setName("Temp");
                for (Temperature temp: patients.get(i).getTemperatures()){
                    if (temp.getTimestamp().after(new Date(System.currentTimeMillis() - 900 * 1000)))
                        series.getData().add(new XYChart.Data<>(temp.getTimestamp().toString(), temp.getTemperature()));

                }
                chart.getXAxis().setTickLabelsVisible(false);
                chart.getXAxis().setOpacity(0);
                chart.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
                chart.getData().add(series);

                //pressure
                chart = getChartByRow(thisrow, 2);
                series = new XYChart.Series<>();
                XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                series.setName("Minima");
                series.setName("Massima");
                for (Pressure press: patients.get(i).getPressures()){
                    if (press.getTimestamp().after(new Date(System.currentTimeMillis() - 900 * 1000))) {
                        series.getData().add(new XYChart.Data<>(press.getTimestamp().toString(), press.getPressMin()));
                        series1.getData().add(new XYChart.Data<>(press.getTimestamp().toString(), press.getPressMax()));
                    }
                }
                chart.getXAxis().setTickLabelsVisible(false);
                chart.getXAxis().setOpacity(0);
                chart.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
                chart.getData().add(series);
                chart.getData().add(series1);
            }
        }
    }


    public void reset() {
        for (Node node : gridRows.getChildren()){
            if (node instanceof AnchorPane){
                node.setStyle("visibility: false");
                GridPane grid = (GridPane) ((AnchorPane) node).getChildren().get(1);
                for (Node childnode: grid.getChildren()){
                    if (childnode instanceof LineChart){
                        LineChart chart = (LineChart) childnode;
                        chart.getData().clear();
                        chart.setAnimated(false);
                    }
                }
            }
        }
    }


    private LineChart getChartByRow(AnchorPane pane, int index){
        int currentindex = 0;
        GridPane grid = (GridPane) pane.getChildren().get(1);
        for(Node node: grid.getChildren()){
            if (node instanceof LineChart){
                if (currentindex == index)
                    return (LineChart) node;
                currentindex++;
            }
        }
        return null;
    }


    public void updateCharts(){
        loadList();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), ev -> {
            reset();
            loadList();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }


    public void runAlarmLater(Patient patient, String event, int severity){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alert.fxml"));
                    Parent root1 = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("ALLARME");
                    stage.setScene(new Scene(root1));
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.show();
                    stage.setFullScreen(true);

                    alertController controller = fxmlLoader.getController();
                    controller.loadData(patient, severity, event);
                }
                catch (IOException e) {
                    String message = "Il paziente " + patient.getFullName() + " ha generato un'allarme!";
                    GUI.showDialog(Alert.AlertType.WARNING, "ATTENTION!", message);
                }
            }
        });
    }
}