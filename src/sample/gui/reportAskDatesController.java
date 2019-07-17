package sample.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Patient;
import sample.Report;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

public class reportAskDatesController {

    private Patient currentPatient = null;
    private Window parentWindow = null;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    void handleShow(ActionEvent event) throws IOException {

        if (dateFrom.getValue() == null || dateTo.getValue() != null){
            showDialog(Alert.AlertType.ERROR, "Genera Report", "Tutti i campi sono obbligatori");
            return;
        }

        if (dateFrom.getValue().isAfter(dateTo.getValue())){
            showDialog(Alert.AlertType.ERROR, "Genera Report", "La prima data deve essere minore della seconda!");
            return;
        }


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reportPage.fxml"));
        Parent root1 = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle("Genera Report");
        stage.setScene(new Scene(root1));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentWindow);
        stage.show();

        Date parsedDateFrom = Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date parsedDataTo = Date.from(dateTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().plusSeconds(86399));
        Report currentReport = new Report(currentPatient, parsedDateFrom, parsedDataTo);

        reportPageController controller = fxmlLoader.getController();
        controller.loadPatient(currentReport);

        Stage thisstage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        thisstage.close();
    }

    public void setCurrentPatient(Patient currentPatient, Window parentWindow) {
        this.currentPatient = currentPatient;
        this.parentWindow = parentWindow;
    }

    void showDialog(Alert.AlertType type, String title, String msg){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
