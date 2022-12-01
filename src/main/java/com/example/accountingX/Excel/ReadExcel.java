package com.example.accountingX.Excel;

import com.example.accountingX.Entity.BankFlow;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class ReadExcel
{
    private final List<BankFlow> resultList;
    private final Workbook workbook;
    private TreeSet<String> types;
    private Sheet sheet;
    private final String path;

    public ReadExcel(String path)
    {
        try {
            this.path = path;
            types = new TreeSet<>();
            resultList = new ArrayList<>();
            workbook = WorkbookFactory.create(new File(path));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void read()
    {
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();

        sheet = sheetIterator.next();

        Iterator<Row> rowIterator = sheet.rowIterator();

        while (rowIterator.hasNext())
        {

            var row = rowIterator.next();

            if (row.getRowNum() < 12)
                continue;

            var dateCell = row.getCell(0);

            if (dateCell == null)
                break;

            var date = dateCell.getStringCellValue();

            var billingNumberCell = row.getCell(1);
            var billingNumber = (billingNumberCell == null ? "N/A" : billingNumberCell.getStringCellValue());

            var descriptionCell = row.getCell(2);
            var description = descriptionCell.getStringCellValue();

            var typeCell = row.getCell(3);
            var type = typeCell.getStringCellValue();

            var amountCell = row.getCell(4);
            var amount = amountCell.getNumericCellValue();


            var bankFlow = new BankFlow(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.uuuu")), LocalTime.now(),
                    description, type, BigDecimal.valueOf(amount), billingNumber);
            resultList.add(bankFlow);
        }

        insertDatabase();


    }

    private void insertDatabase()
    {
        try
        {
            SessionFactoryManager.addAll(resultList);
            workbook.close();
            UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Banka Hareketleri Başarıyla Eklendi!", ButtonType.OK);
        }
        catch (IOException e) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Bir hata oluştu! Lütfen dosyayı ve dosya formatını kontrol ediniz", ButtonType.OK);
            throw new RuntimeException(e);
        }
    }
}