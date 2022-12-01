package com.example.accountingX.util;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public final class UtilFX
{
    public static void initStage(String title, Stage stage, Scene scene, boolean resizable, String icon)
    {
        stage.setTitle(title);
        stage.getIcons().add(new Image(icon));
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.show();
    }
    public static void alertScreen(Alert.AlertType type, String msg, ButtonType btn)
    {
        var alert = new Alert(type,msg,btn);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Constant.ICON));
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }


    public static File fileChoose(String fileName, String fullFormatName, String format) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Nereye Kaydedilecek?");
        fileChooser.setInitialFileName(fileName + format);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(fullFormatName, format));
        return fileChooser.showSaveDialog(new Stage());

    }

}
