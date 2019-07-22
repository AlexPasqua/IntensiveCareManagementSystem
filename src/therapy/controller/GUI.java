package therapy.controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import therapy.Datastore;
import javafx.stage.WindowEvent;
import java.util.Map;
import java.util.Optional;

public final class GUI {
    private GUI(){}

    public static void showDialog(Alert.AlertType type, String title, String msg){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(GUI.class.getResourceAsStream("/imgs/icon.png")));
        alert.showAndWait();
    }

    static boolean showPrompt(String title, String msg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    public static void quit(){
        Datastore.write();
        Platform.exit();
        System.exit(1);
    }

    //closing event of patientListController
    static EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
        Datastore.allLoaders.remove("patientslist");
        if (GUI.showPrompt("Sei Sicuro?", "Vuoi effettuare il LogOut prima di chiudere la finestra?")){
            //yes
            boolean found = false;
            for (Map.Entry<String, FXMLLoader> node : Datastore.allLoaders.entrySet()){
                if (node.getKey().contains("patientPage")){
                    GUI.showDialog(Alert.AlertType.ERROR, "Login error", "Chiudi tutte le pagine relative ai pazienti prima");
                    found = true;
                    event.consume();
                }
            }
            if (!found) Datastore.setActiveUser(null);
        }
    };
}
