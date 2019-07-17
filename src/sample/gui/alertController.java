package sample.gui;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.Datastore;
import sample.Patient;
import sample.User;
import sample.UserType;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;


public class alertController implements Initializable {
    private Patient currentPatient;
    private int severity;
    private Date endTimer;
    private Animation animation;
    private Clip clip;

    @FXML private Label labelPatientName;
    @FXML private Label labelPatientName1;
    @FXML private Label labelSeverity;
    @FXML private Label labelText;
    @FXML private AnchorPane background;
    @FXML private Label labelTimer;
    @FXML private Label labelTimer1;
    @FXML private VBox vboxTimer;
    @FXML private VBox vboxDead;
    @FXML private VBox main;
    @FXML private ImageView imageGrave;


    @FXML
    void handleDisable(ActionEvent event) {
        openLogin(event);
    }


    @FXML
    void handleClose(ActionEvent event){
        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();
    }


    public void loadData(Patient currentPatient, int severity, String text){
        this.currentPatient = currentPatient;
        this.severity = severity;
        this.endTimer = new Date(System.currentTimeMillis() + (60 * (4-severity) * 1000));

        labelPatientName.setText(currentPatient.getFullName());
        labelPatientName1.setText(currentPatient.getFullName());
        labelSeverity.setText("Gravità " + severity);
        labelText.setText(text);
        Image image = new Image(getClass().getResourceAsStream("/imgs/dead.png"));
        imageGrave.setImage(image);

        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/sounds/alarm.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(1000);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){ e.printStackTrace(); }

        updateTimer();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animation = new Transition() {

            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(Interpolator.EASE_IN);
                setCycleCount(INDEFINITE);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(1,0,0, 1 - frac);
                Color vColor2 = new Color(0, 0, 0, 1 - frac);
                background.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                labelTimer.setTextFill(vColor2);
            }
        };
        animation.play();
    }


    public void updateTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            if((new Date()).before(endTimer)){
                long diffInMilliSec =  endTimer.getTime() - (new Date()).getTime();
                String secs = String.format("%02d",  (diffInMilliSec / 1000) % 60);
                String minutes = String.format("%02d", (diffInMilliSec / (1000 * 60)) % 60);
                labelTimer.setText(minutes + ":" + secs);
                labelTimer1.setText(minutes + ":" + secs);
            } else {
                animation.stop();
                clip.stop();
                main.setStyle("");
                main.setBackground(new Background(new BackgroundFill(Color.BLACK,CornerRadii.EMPTY, Insets.EMPTY)));
                vboxTimer.setStyle("visibility: false");
                vboxDead.setStyle("visibility: true");

                //set patient
                currentPatient.setDischargeLetter("Paziente Deceduto per Allarme di Gravità " + severity);
                currentPatient.setHospitalization(false);
                Datastore.write();
                //updating all other windows
                for(Map.Entry<String, FXMLLoader> entry: Datastore.allLoaders.entrySet()){
                    switch (entry.getKey()){
                        case "dashboard":{
                            homeController controller = entry.getValue().getController();
                            controller.reset();
                            controller.loadList();
                            break;
                        }
                        case "patientslist":{
                            patientListController controller = entry.getValue().getController();
                            controller.reset();
                            controller.loadList();
                        }
                    }
                }
                System.out.println("dimesso");
            }
        }));
        timeline.setCycleCount(60*(4-severity));
        timeline.play();
    }


    private void openLogin(ActionEvent event){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Accesso");
        dialog.setHeaderText("Inserisci le credenziali per disattivare l'allarme");
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            boolean logged = false;
            for (User user: Datastore.getUsers()){
                if (user.getUserType() == UserType.CHIEFDOCTOR || user.getUserType() == UserType.DOCTOR){
                    if (user.isValid(usernamePassword.getKey(), usernamePassword.getValue())) {
                        //close
                        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
                        stage.close();
                        logged = true;
                    }
                }
            }
            //error message
            if (!logged)
                GUI.showDialog(Alert.AlertType.ERROR, "Login error", "Credenziali Errate\nNB: Solo Medici e Primari possono disabilitare l'allarme");
        });
    }
}
