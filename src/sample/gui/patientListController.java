package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientPage.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Paziente");
        stage.setScene(new Scene(root1));
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(getClass().getResourceAsStream("/imgs/user.png"));
        imageP1.setImage(image);
        System.out.println("Image set");
    }
}
