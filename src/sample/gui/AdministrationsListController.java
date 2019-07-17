package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.Administration;
import sample.Patient;
import sample.Prescription;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class AdministrationsListController {

    private Patient currentPatient = null;
    @FXML private TableView<Administration> administrationsList;
    @FXML private TableColumn<Administration, String> aMed;
    @FXML private TableColumn<Administration, String> aDose;
    @FXML private TableColumn<Administration, String> aTimestamp;
    @FXML private Button newAdministrationButton;


    @FXML
    void handleNewAdministration(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Somministrazione", "addAdministration.fxml", event);
        AddAdministrationController controller = fxmlLoader.<AddAdministrationController>getController();
        controller.setCurrentPatient(currentPatient);
    }

    @FXML
    void handleClick(MouseEvent event) {
        System.out.println("Mouse click");
        if (event.getClickCount() == 2 ){
            showDialog(Alert.AlertType.INFORMATION, administrationsList.getSelectionModel().getSelectedItem().getNotes() );
        }

    }



    void setCurrentPatient(Patient patient){
        this.currentPatient = patient;
        if (!patient.getHospitalization())
            newAdministrationButton.setDisable(true);
        associateTableCols();


    }

    private void associateTableCols(){
        if (!currentPatient.getAdministrations().isEmpty())
            loadList();

        aMed.setCellValueFactory(new PropertyValueFactory<Administration, String>("medicine"));
        aDose.setCellValueFactory(new PropertyValueFactory<Administration, String>("mgDose"));
        aTimestamp.setCellValueFactory(new PropertyValueFactory<Administration, String>("timestamp"));
    }


    private void loadList() {
        ObservableList<Administration> data = FXCollections.observableArrayList(currentPatient.getAdministrations());
        administrationsList.setItems(data);
        administrationsList.getSelectionModel().selectFirst();
    }


    FXMLLoader openPopupWindow(String title, String fxml, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        Window thiswindow = ((Node)event.getTarget()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thiswindow);
        stage.setScene(new Scene(root));
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_HIDDEN, this::closeWindowEvent);
        stage.show();

        return fxmlLoader;
    }

    void showDialog(Alert.AlertType type, String msg){
        Alert alert = new Alert(type);
        alert.setTitle("Note Somministrazione");
        alert.setHeaderText(null);
        alert.setContentText("Note somministrazione: \n"+ msg);
        alert.showAndWait();
    }


    private void closeWindowEvent(WindowEvent event){ loadList(); }
}
