package therapy.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import therapy.Datastore;
import therapy.Patient;
import therapy.UserType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;


public class PatientListController implements Initializable {
    @FXML private GridPane gridPatients;
    @FXML private Label labelHi;
    @FXML private Button buttonAddUser;

    @FXML
    void handleAddPatient(ActionEvent event) {
        FXMLLoader fxmlLoader = null;
        Parent root1 = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../gui/addPatient.fxml"));
            root1 = fxmlLoader.load();
        } catch(IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile aggiungere nuovi pazienti");
        }

        Window thiswindow = ((Node)event.getTarget()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Aggiungi Paziente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thiswindow);
        stage.setScene(new Scene(root1));
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_HIDDEN, this::closeWindowEvent);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
        stage.show();
    }

    @FXML
    void handleOpenPatient(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        String patientId = clicked.getStyle().replace("npatient: ", "");

        if (Datastore.allLoaders.containsKey("patientPage" + Datastore.getPatients().get(Integer.parseInt(patientId)).getCodFis())){
            GUI.showDialog(Alert.AlertType.WARNING, "Paziente", "La scheramata relativa a questo paziente è già aperta");
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../gui/patientPage.fxml"));
                Parent root1 = fxmlLoader.load();
                PatientPageController controller = fxmlLoader.<PatientPageController>getController();
                controller.loadPatient(Integer.parseInt(patientId));

                Stage stage = new Stage();
                stage.setTitle("Paziente");
                stage.setScene(new Scene(root1));
                Datastore.allLoaders.put("patientPage" + Datastore.getPatients().get(Integer.parseInt(patientId)).getCodFis(), fxmlLoader);
                stage.setOnCloseRequest((WindowEvent event1) -> {
                    Datastore.allLoaders.remove("patientPage" + Datastore.getPatients().get(Integer.parseInt(patientId)).getCodFis());
                });
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
                stage.show();
            }
            catch(IOException e){
                GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente non è possibile aprire la pagina del paziente");
            }
        }

    }

    @FXML
    void handleAllPatients(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../gui/patientAllList.fxml"));
            Parent root1 = fxmlLoader.load();
            Window thiswindow = ((Node)event.getTarget()).getScene().getWindow();
            Stage stage = new Stage();
            stage.setTitle("Lista Tutti i Pazienti");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(thiswindow);
            stage.setScene(new Scene(root1));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
            stage.show();
        }
        catch(IOException e){
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente non è possibile mostrare tutti i pazienti");
        }
    }

    @FXML
    private void handleLogOut(ActionEvent event){
        for (Map.Entry<String, FXMLLoader> node : Datastore.allLoaders.entrySet()){
            if (node.getKey().contains("patientPage")){
                GUI.showDialog(Alert.AlertType.ERROR, "Login error", "Chiudi tutte le pagine relative ai pazienti prima");
                return;
            }
        }
        Datastore.setActiveUser(null);

        //chiudo login
        Datastore.allLoaders.remove("patientslist");
        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAddUser(ActionEvent event){
        FXMLLoader fxmlLoader = null;
        Parent root1 = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("../gui/addUser.fxml"));
            root1 = fxmlLoader.load();
        } catch(IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile aggiungere nuovi utenti");
        }

        Window thiswindow = ((Node)event.getTarget()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle("Aggiungi Utente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thiswindow);
        stage.setScene(new Scene(root1));
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_HIDDEN, this::closeWindowEvent);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateList();
        labelHi.setText("Ciao, " + Datastore.getActiveUser().getCompleteName());
        if (Datastore.getActiveUser().getUserType() != UserType.CHIEFDOCTOR)
            buttonAddUser.setDisable(true);
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
        if (Datastore.allLoaders.containsKey("dashboard")){
            HomeController controller = Datastore.allLoaders.get("dashboard").getController();
            controller.reset();
            controller.loadList();
        }

    }

    private void updateList(){
        loadList();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), ev -> {
            reset();
            loadList();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
