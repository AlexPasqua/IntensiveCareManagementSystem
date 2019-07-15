package sample.gui;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class alertController implements Initializable {
    @FXML
    private Label labelPatientName;

    @FXML
    private Label labelSeverity;

    @FXML
    private Label labelText;

    @FXML
    private AnchorPane background;

    @FXML
    void handleDisable(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("HEY");
        final Animation animation = new Transition() {

            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(Interpolator.EASE_OUT);
                setCycleCount(INDEFINITE);
            }

            @Override
            protected void interpolate(double frac) {

                Color vColor = new Color(1, 0, 0, 1 - frac);
                background.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        animation.play();
    }
}
