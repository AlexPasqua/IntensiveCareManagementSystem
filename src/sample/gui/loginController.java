package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class loginController {

    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    void handleLogin(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientList.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Lista Pazienti");
        stage.setScene(new Scene(root1));
        stage.show();
    }
}

