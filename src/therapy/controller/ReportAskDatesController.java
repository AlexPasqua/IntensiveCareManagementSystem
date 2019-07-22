package therapy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import therapy.Patient;
import therapy.Report;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;


public class ReportAskDatesController {
    private Patient currentPatient = null;
    private Window parentWindow = null;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;

    @FXML
    void handleShow(ActionEvent event) {
        if (dateFrom.getValue() == null || dateTo.getValue() == null){
            GUI.showDialog(Alert.AlertType.ERROR, "Genera Report", "Tutti i campi sono obbligatori");
            return;
        }

        if (dateFrom.getValue().isAfter(dateTo.getValue())){
            GUI.showDialog(Alert.AlertType.ERROR, "Genera Report", "La prima data deve essere minore della seconda!");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../gui/reportPage.fxml"));
            Parent root1 = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Genera Report");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentWindow);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
            stage.show();

            Date parsedDateFrom = Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date parsedDataTo = Date.from(dateTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().plusSeconds(86399));
            Report currentReport = new Report(currentPatient, parsedDateFrom, parsedDataTo);

            ReportPageController controller = fxmlLoader.getController();
            controller.loadPatient(currentReport);

            Stage thisstage = (Stage)((Node)event.getTarget()).getScene().getWindow();
            thisstage.close();
        }
        catch (IOException e){
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile aprire la pagina di report");
        }

    }

    public void setCurrentPatient(Patient currentPatient, Window parentWindow) {
        this.currentPatient = currentPatient;
        this.parentWindow = parentWindow;
    }
}
