package sample.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.Datastore;
import sample.Patient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class patientListController implements Initializable  {

    @FXML
    private Menu menuAddPatient;

    @FXML
    private Menu menuLogout;

    @FXML
    private Button patient1;

    @FXML
    private ImageView imageP1;

    @FXML
    private GridPane gridPatients;

    @FXML
    void handleAddPatient(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addPatient.fxml"));
        Parent root1 = fxmlLoader.load();

        Window thiswindow = ((Node)event.getTarget()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Aggiungi Paziente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thiswindow);
        stage.setScene(new Scene(root1));
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_HIDDEN, this::closeWindowEvent);
        stage.show();
    }

    @FXML
    void handleOpenPatient(ActionEvent event) throws Exception{
        Button clicked = (Button) event.getSource();
        String patientId = clicked.getStyle().replace("npatient: ", "");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientPage.fxml"));
        Parent root1 = fxmlLoader.load();

        patientPageController controller = fxmlLoader.<patientPageController>getController();
        controller.loadPatient(Integer.parseInt(patientId));

        Stage stage = new Stage();
        stage.setTitle("Paziente");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadList();
    }

    public void loadList(){
        ArrayList<Patient> patients = Datastore.getPatients();
        ObservableList<Node> buttons = gridPatients.getChildren();
        Image image = new Image(getClass().getResourceAsStream("/imgs/user.png"));
        int lastbtn = 0;
        for (int i = 0; i < patients.size(); i++){
            Button button = (Button) buttons.get(lastbtn);
            if (patients.get(i).getHospitalization()) {
                button.setText(patients.get(i).getFullName());
                button.setStyle("npatient: " + i);
                ((ImageView) button.getGraphic()).setImage(image);
                lastbtn++;
            } else {
                button.setStyle("visibility: false");
            }
        }
    }

    public void reset(){
        for (Node btn :gridPatients.getChildren()){
            btn.setStyle("visibility: false");
        }
    }

    private void closeWindowEvent(WindowEvent event) {
        System.out.println("Updating patient list");
        loadList();
    }
}
