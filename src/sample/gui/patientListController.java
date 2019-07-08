package sample.gui;

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
import javafx.stage.Stage;
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
        Stage stage = new Stage();
        stage.setTitle("Aggiungi Paziente");
        stage.setScene(new Scene(root1));
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

    ArrayList<Patient> patients = new ArrayList<>(); //TODO:finche non ho accesso a quello globale
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //metto dei dati dentro il tmp array
        //TODO: rimuovere quando avrò accesso al dataset, solo per test
        Patient p1 = new Patient("XXXX", "Francesco", "Fattori", new Date(), "Soave");
        Patient p2 = new Patient("XXXX", "Giacomo", "Frigo", new Date(), "Soave");
        p1.setHospitalization(false);
        patients.add(p1);
        patients.add(p2);
        //fine

        ObservableList<Node> buttons = gridPatients.getChildren();
        Image image = new Image(getClass().getResourceAsStream("/imgs/user.png"));
        int lastbtn = 0;
        for (int i = 0; i < patients.size(); i++){
            if (patients.get(i).getHospitalization()) {
                Button button = (Button) buttons.get(lastbtn);
                button.setText(patients.get(i).getFullName());
                //button.setStyle("visibility: true");
                button.setStyle("npatient: " + i);
                ((ImageView) button.getGraphic()).setImage(image);
                lastbtn++;
            }
        }

    }
}
