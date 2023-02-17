package com.example.accountx.Excel;

import com.example.accountx.Entity.Months_TR;
import com.example.accountx.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;

public class ExportFilterUsers extends CreateExcelFile
{
    public ExportFilterUsers(String fileName, int cellSize, String[] cellNames, String path)
    {
        super(fileName, cellSize, cellNames, path);
    }


    private void writeTitle()
    {
        var date = LocalDate.now();
        var row = sheet.createRow(1);
        var cell = row.createCell(1);
        cell.setCellValue(Months_TR.values()[date.getMonth().getValue()] + " " + date.getYear() + " FİLTRELİ KULLANICILAR");
        prepareCell(cell);
    }


    public void export()
    {
        writeTitle();
        write("");
        saveFile();
        UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Excel dosyası başarıyla oluşturuldu!", ButtonType.OK);

    }

}
