package therapy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import therapy.controller.GUI;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Main extends Application{

    public static void main(String[] args) {
        Datastore.read();

        // RMI section
        RMIinterface server = new RMIimplements();

        try{
            RMIinterface stub = (RMIinterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);

            registry.bind("RMIserver", stub);

        } catch(AlreadyBoundException | RemoteException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Server error", "Impossibile stabilire una connessione col sistema di monitoraggio");
        }
        // END RMI SECTION

        launch(args); //GUI Start
        Datastore.write();

        //force closing - if the user close GUI, force shutdown so the server rmi stop.
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui/home.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("Dashboard");
            primaryStage.setScene(new Scene(root, 1000, 500));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
            primaryStage.show();
            Datastore.allLoaders.put("dashboard", fxmlLoader);
        }
        catch (IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Impossibile avviare l'interfaccia grafica");
        }
    }
}
