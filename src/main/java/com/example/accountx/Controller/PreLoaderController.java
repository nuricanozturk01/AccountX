package com.example.accountx.Controller;

import com.example.accountx.App;
import com.example.accountx.util.Constant;
import com.example.accountx.util.UtilFX;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
@SuppressWarnings("all")
public class PreLoaderController implements Initializable
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        var fade = new FadeTransition(Duration.seconds(3), imageView);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);
        fade.play();
        fade.setOnFinished(this::onFinish);
    }
    private void onFinish(ActionEvent actionEvent)
    {
        try
        {
            var pane = (AnchorPane) FXMLLoader.load(App.class.getResource("main.fxml"));

            UtilFX.initStage("AccountX", new Stage(), new Scene(pane), false, Constant.ICON);

            var preLoaderScreen = (Stage) imageView.getScene().getWindow();
            preLoaderScreen.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
