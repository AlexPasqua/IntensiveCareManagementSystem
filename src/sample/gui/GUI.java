package sample.gui;

import javafx.scene.control.Alert;

public final class GUI {
    private GUI(){}

    static void showDialog(Alert.AlertType type, String title, String msg){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
