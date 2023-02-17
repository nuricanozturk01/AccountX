package com.example.accountx.Excel;

import com.example.accountx.Entity.Months_TR;
import com.example.accountx.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.math.BigDecimal;
import java.time.LocalDate;
import static com.example.accountx.util.UtilFX.getFormattedNumber;

public class ExportFilterBankFlow extends CreateExcelFile
{
    private BigDecimal sum;
    public ExportFilterBankFlow(String fileName, int cellSize, String[] cellNames, String path, BigDecimal sum)
    {
        super(fileName, cellSize, cellNames, path);
        this.sum = sum;
    }


    private void writeTitle()
    {
        var date = LocalDate.now();
        var row = sheet.createRow(2);
        var cell = row.createCell(2);
        cell.setCellValue(Months_TR.values()[date.getMonth().getValue()] + " " + date.getYear() + " FİLTRELİ BANKA HAREKETLERİ");
        prepareCell(cell);
    }


    public void export()
    {
        writeTitle();
        write("");
        var row = getRowCount();
        writeTotal(row);
        saveFile();
        UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Excel dosyası başarıyla oluşturuldu!", ButtonType.OK);

    }

    private void writeTotal(int rowCount)
    {
        var columnCounter = 4;
        rowCount++;
        var r = sheet.createRow(rowCount++);
        var cell = r.createCell(columnCounter);
        cell.setCellValue("TOPLAM TUTAR: " + getFormattedNumber(sum) + " ₺");
        prepareCell(cell);

    }
}
