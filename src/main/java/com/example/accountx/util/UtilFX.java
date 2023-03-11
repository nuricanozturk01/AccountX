package com.example.accountx.util;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.accountx.util.Constant.*;


public final class UtilFX
{
    public static void initStage(String title, Stage stage, Scene scene, boolean resizable, String icon)
    {
        stage.setTitle(title);
        stage.getIcons().add(new Image(icon));
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.show();

        if (!stage.getTitle().equals("Yedi İklim"))
            stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> setOnClosable(event, stage));
    }

    private static void setOnClosable(KeyEvent event, Stage stage)
    {
        if (event.getCode() == KeyCode.ESCAPE)
            stage.close();
    }

    public static void alertScreen(Alert.AlertType type, String msg, ButtonType btn)
    {
        var alert = new Alert(type,msg,btn);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Constant.ICON));
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
    public static File fileChoose(String fileName, String fullFormatName, String format)
    {
        var fileChooser = new FileChooser();

        fileChooser.setTitle("Nereye Kaydedilecek?");
        fileChooser.setInitialFileName(fileName + format);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(fullFormatName, format));

        return fileChooser.showSaveDialog(new Stage());
    }

    public static String getFormattedNumber(BigDecimal number)
    {
        if (number.compareTo(BigDecimal.ZERO) == 0)
            return ZERO;

        if ((number.compareTo(BigDecimal.ZERO) < 0 && number.compareTo(BigDecimal.ONE.negate()) > 0)
                                                    ||
                (number.compareTo(BigDecimal.ZERO) > 0 && number.compareTo(BigDecimal.ONE) < 0))
            return NUMBER_LESS_ZERO_FORMAT.format(number);

        return NUMBER_FORMAT.format(number);
    }

    public static <T> List<T> clone(Collection<T> source)
    {
        return new ArrayList<T>(source);
    }
}
