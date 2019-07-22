package therapy.gui;

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
import therapy.Administration;
import therapy.Patient;
import java.io.IOException;


public class AdministrationsListController {

    private Patient currentPatient = null;
    @FXML private TableView<Administration> administrationsList;
    @FXML private TableColumn<Administration, String> aMed;
    @FXML private TableColumn<Administration, String> aDose;
    @FXML private TableColumn<Administration, String> aTimestamp;
    @FXML private Button newAdministrationButton;


    @FXML
    void handleNewAdministration(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Somministrazione", "addAdministration.fxml", event);
            AddAdministrationController controller = fxmlLoader.<AddAdministrationController>getController();
            controller.setCurrentPatient(currentPatient);
        }
        catch (IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error!", "Impossibile aggiungere somministrazione");
        }
    }


    @FXML
    void handleClick(MouseEvent event) {
        System.out.println("Mouse click");
        if (event.getClickCount() == 2 ){
            GUI.showDialog(Alert.AlertType.INFORMATION, "Info", administrationsList.getSelectionModel().getSelectedItem().getNotes());
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

    private void closeWindowEvent(WindowEvent event){ loadList(); }
}
