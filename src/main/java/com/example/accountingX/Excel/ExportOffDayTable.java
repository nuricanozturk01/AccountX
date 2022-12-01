package com.example.accountingX.Excel;


import com.example.accountingX.Entity.OffDay;
import com.example.accountingX.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;

public class ExportOffDayTable extends CreateExcelFile
{
    private final String path;
    private final String userName;
    private final List<OffDay> offDayList;

    public ExportOffDayTable(String fileName, int cellSize, String[] cellNames, String path, List<OffDay> offDayList, String userName)
    {
        super(fileName, cellSize, cellNames, path);
        this.path = path;
        this.offDayList = offDayList;
        this.userName = userName;

        offDayList.stream().map(OffDay::ExcelFormat).forEach(this::buffer);
    }



    public void export() {
            writeTitle();
            write("");
            int row = getRowCount();
            writeTotalDay(row);
            saveFile();
            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Başarıyla Kaydedildi....", ButtonType.OK);
    }

    private void writeTotalDay(int rowCount) {

        var columnCounter = 1;
        var r = sheet.createRow(rowCount++);
        var cell = r.createCell(columnCounter);
        cell.setCellValue("TOPLAM GÜN: " + offDayList.size());
        prepareCell(cell);
    }



    private void writeTitle() {
        var row = sheet.createRow(1);
        var cell = row.createCell(1);
        cell.setCellValue("YILLIK İZİN TABLOSU");
        prepareCell(cell);

        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("Çalışan");
        prepareCell(cell);

        cell = row.createCell(1);
        cell.setCellValue(userName);
        prepareCell(cell);

        row = sheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue("Tarih");
        prepareCell(cell);

        cell = row.createCell(1);
        cell.setCellValue("İzin Tipi");
        prepareCell(cell);
        //UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Excel dosyası başarıyla oluşturuldu!", ButtonType.OK);
    }


}
