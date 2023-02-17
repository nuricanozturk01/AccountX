package com.example.accountx.Excel;

import com.example.accountx.Entity.Months_TR;
import com.example.accountx.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;

public class ExportFilterCostType extends CreateExcelFile
{
    public ExportFilterCostType(String fileName, int cellSize, String[] cellNames, String path)
    {
        super(fileName, cellSize, cellNames, path);
    }


    private void writeTitle()
    {
        var date = LocalDate.now();
        var row = sheet.createRow(0);
        var cell = row.createCell(0);
        cell.setCellValue(Months_TR.values()[date.getMonth().getValue()] + " " + date.getYear() + " FİLTRELİ GİDER TÜRLERİ");
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
