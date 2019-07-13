package sample.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import sample.Patient;

import java.io.IOException;

public class reportAskDatesController {

    private Patient currentPatient = null;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    void handleShow(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reportPage.fxml"));
        Parent root1 = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setTitle("Genera Report");
        stage.setScene(new Scene(root1));
        stage.show();

        reportPageController controller = fxmlLoader.getController();
        controller.loadPatient(currentPatient, dateFrom.getValue(), dateTo.getValue());

        Stage thisstage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        thisstage.close();
    }

}
