package therapy.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import therapy.Datastore;
import therapy.User;
import therapy.UserType;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    @FXML private TextField textboxName;
    @FXML private TextField textboxSurname;
    @FXML private TextField textboxUsername;
    @FXML private PasswordField textboxPassword;
    @FXML private ComboBox<String> comboPower;

    @FXML
    void handleConfirm(ActionEvent event) {
        if (textboxName.getText().isEmpty() || textboxSurname.getText().isEmpty() || textboxUsername.getText().isEmpty() || textboxPassword.getText().isEmpty()){
            GUI.showDialog(Alert.AlertType.WARNING, "Warning", "Tutti i campi sono obbligatori");
            return;
        }

        UserType power = null;
        switch (comboPower.getValue()){
            case "Medico":{
                power = UserType.DOCTOR;
                break;
            }
            case "Infermiere":{
                power = UserType.NURSE;
                break;
            }
        }
        User newuser = new User(textboxName.getText(), textboxSurname.getText(), textboxUsername.getText(), textboxPassword.getText(), power);
        Datastore.addUser(newuser);
        Datastore.write();

        GUI.showDialog(Alert.AlertType.INFORMATION, "Info", "Utente aggiunto correttamente!");

        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboPower.getItems().addAll("Medico", "Infermiere");
        comboPower.getSelectionModel().selectFirst();
    }
}
