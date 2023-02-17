package com.example.accountx.Excel;

import com.example.accountx.Entity.AdvanceAmount;
import com.example.accountx.Entity.Months_TR;
import com.example.accountx.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportCostForm extends CreateExcelFile{

    private final BigDecimal sumOfCostForms;
    private final BigDecimal advanceAmount;
    private final List<AdvanceAmount> advanceAmounts;
    private final LocalDate date;
    private final String path;
    public ExportCostForm(String fileName, String[] cellNames, BigDecimal sumOfCostForms, BigDecimal advanceAmount, List<AdvanceAmount> advanceAmounts, String path, LocalDate date)
    {
        super(fileName, 9, cellNames, path);
        this.sumOfCostForms = sumOfCostForms;
        this.advanceAmount = advanceAmount;
        this.advanceAmounts = advanceAmounts;
        this.path = path;
        this.date = date;
    }

    private void writeDate()
    {
        var row = sheet.createRow(2);
        var cell = row.createCell(2);
        cell.setCellValue(Months_TR.values()[date.getMonth().getValue()] + " " + date.getYear() + " MASRAF FORMU");
        prepareCell(cell);
    }


    private void writeTotal(int rowCount)
    {
        var columnCounter = 8;
        rowCount++;
        var r = sheet.createRow(rowCount++);
        var cell = r.createCell(columnCounter);
        cell.setCellValue("TOPLAM TUTAR: " + sumOfCostForms + " ₺");
        prepareCell(cell);
        r = sheet.createRow(rowCount++);
        cell = r.createCell(columnCounter);
        cell.setCellValue("AVANS MİKTARI: " + advanceAmount + " ₺");
        prepareCell(cell);
        r = sheet.createRow(rowCount++);
        cell = r.createCell(columnCounter);
        cell.setCellValue("KALAN: " + advanceAmount.add(sumOfCostForms) + " ₺");
        prepareCell(cell);
        if (advanceAmounts != null && !advanceAmounts.isEmpty()) {
            for (int i = 0; i < advanceAmounts.size(); i++)
            {
                r = sheet.createRow(rowCount++);
                cell = r.createCell(columnCounter);
                var advanceAmountStr = "["+ DateTimeFormatter.ofPattern("dd/MM/yyyy").format(advanceAmounts.get(i).getAmount_date()) + "] " +
                advanceAmounts.get(i).getAmount() + " ₺";
                cell.setCellValue(advanceAmountStr);
                prepareCell(cell);
            }

        }
    }
    public void export()
    {
        writeDate();
        write("");
        int row = getRowCount();
        writeTotal(row);
        saveFile();
        UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Excel dosyası başarıyla oluşturuldu!", ButtonType.OK);
    }

}
