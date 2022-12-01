package com.example.accountingX;

import com.example.accountingX.util.Constant;
import com.example.accountingX.util.UtilFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
public class App extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        var fxmlLoader = new FXMLLoader(App.class.getResource("preloader.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.sizeToScene();
        UtilFX.initStage("AccountingX",stage,new Scene(fxmlLoader.load()),false, Constant.ICON);
    }
    public static void run(String[] args)
    {
        launch(args);
    }
}