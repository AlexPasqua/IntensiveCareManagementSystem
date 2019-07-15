package sample.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.event.ActionEvent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sample.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;


public class homeController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane rowPatient0;

    @FXML
    private LineChart<String, Number> grafico01;

    @FXML
    private LineChart<String, Number> grafico02;

    @FXML
    private LineChart<String, Number> grafico03;

    @FXML
    private AnchorPane rowPatient1;

    @FXML
    LineChart<String, Number> grafico11;

    @FXML
    LineChart<String, Number> grafico12;

    @FXML
    LineChart<String, Number> grafico13;

    @FXML
    private AnchorPane rowPatient2;

    @FXML
    LineChart<String, Number> grafico21;

    @FXML
    LineChart<String, Number> grafico22;

    @FXML
    LineChart<String, Number> grafico23;

    @FXML
    private AnchorPane rowPatient3;

    @FXML
    LineChart<String, Number> grafico31;

    @FXML
    LineChart<String, Number> grafico32;

    @FXML
    LineChart<String, Number> grafico33;

    @FXML
    private AnchorPane rowPatient4;

    @FXML
    LineChart<String, Number> grafico41;

    @FXML
    LineChart<String, Number> grafico42;

    @FXML
    LineChart<String, Number> grafico43;

    @FXML
    private AnchorPane rowPatient5;

    @FXML
    LineChart<String, Number> grafico51;

    @FXML
    LineChart<String, Number> grafico52;

    @FXML
    LineChart<String, Number> grafico53;

    @FXML
    private AnchorPane rowPatient6;

    @FXML
    LineChart<String, Number> grafico61;

    @FXML
    LineChart<String, Number> grafico62;

    @FXML
    LineChart<String, Number> grafico63;

    @FXML
    private AnchorPane rowPatient7;

    @FXML
    LineChart<String, Number> grafico71;

    @FXML
    LineChart<String, Number> grafico72;

    @FXML
    LineChart<String, Number> grafico73;

    @FXML
    private AnchorPane rowPatient8;

    @FXML
    LineChart<String, Number> grafico81;

    @FXML
    LineChart<String, Number> grafico82;

    @FXML
    LineChart<String, Number> grafico83;

    @FXML
    private AnchorPane rowPatient9;

    @FXML
    LineChart<String, Number> grafico91;

    @FXML
    LineChart<String, Number> grafico92;

    @FXML
    LineChart<String, Number> grafico93;

    private AnchorPane[] rows = {rowPatient0, rowPatient1, rowPatient2, rowPatient3, rowPatient4, rowPatient5, rowPatient6,  rowPatient7,  rowPatient8, rowPatient9};

    @FXML
    void handleAddValue(ActionEvent event){
        //pressione max value is 8
        //addDataTo(grafico1,  20, 0);

        //addDataTo(grafico01,  55, 1);
        reset();
    }

    @FXML
    void handleLogin(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(new Scene(root1));
        stage.show();
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
    private void handleQuit() throws Exception {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadList();
    }

    public void loadList(){
        ArrayList<Patient> patients = Datastore.getPatients();
        AnchorPane[] rows = {rowPatient0, rowPatient1, rowPatient2, rowPatient3, rowPatient4, rowPatient5, rowPatient6,  rowPatient7,  rowPatient8, rowPatient9};
        LineChart[] charts = {grafico01, grafico02, grafico03, grafico11, grafico12, grafico13,  grafico21, grafico22, grafico23, grafico31, grafico32, grafico33, grafico41, grafico42,
                grafico43, grafico52, grafico53, grafico61, grafico62, grafico63, grafico71, grafico72, grafico73, grafico81, grafico82, grafico83, grafico91, grafico92, grafico93};
        if (patients.size() > 0){
            scrollPane.setStyle("");
            int rowstatus = 0;
            for (int i=0; i<patients.size(); i++){
                if (!patients.get(i).getHospitalization()) continue;

                rows[rowstatus].setStyle("");
                //HB chart
                LineChart chart = charts[3*rowstatus];
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("HB");
                for (HeartBeat beat: patients.get(i).getHeartBeats()){
                    if (beat.getTimestamp().after(new Date(System.currentTimeMillis() - 900 * 1000)))
                        series.getData().add(new XYChart.Data<>(beat.getTimestamp().toString(), beat.getHeartBeat()));
                }
                chart.getXAxis().setTickLabelsVisible(false);
                chart.getXAxis().setOpacity(0);
                chart.getData().add(series);

                //temp
                chart = charts[(3*rowstatus)+1];
                series = new XYChart.Series<>();
                series.setName("Temp");
                for (Temperature temp: patients.get(i).getTemperatures()){
                    if (temp.getTimestamp().after(new Date(System.currentTimeMillis() - 900 * 1000)))
                        series.getData().add(new XYChart.Data<>(temp.getTimestamp().toString(), temp.getTemperature()));

                }
                chart.getXAxis().setTickLabelsVisible(false);
                chart.getXAxis().setOpacity(0);
                chart.getData().add(series);

                //pressure
                chart = charts[(3*rowstatus)+2];
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
                chart.getData().add(series);
                chart.getData().add(series1);
                rowstatus++;
            }
        }
    }

    public void reset() {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(Datastore.allLoaders.get("dashboard").getRoot(), nodes);
        for (Node node: nodes){
            if (node instanceof LineChart){
                LineChart chart = (LineChart) node;
                chart.getData().removeAll();
            } else if (node instanceof AnchorPane){
                try{
                    if (node.getId().contains("rowPatient"))
                        node.setStyle("visibility: false");
                } catch (Exception e) {}
            }
        }
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }
}