package com.example.accountx.Excel;

import com.example.accountx.Entity.BankFlow;
import com.example.accountx.HibernateConfiguration.SessionFactoryManager;
import com.example.accountx.command.commandcontroller.CommandController;
import com.example.accountx.command.dto.MultipleBankFlowDTO;
import com.example.accountx.util.Constant;
import com.example.accountx.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

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

import static com.example.accountx.command.types.CommandName.MULTIPLE_BANK_FLOW;
import static com.example.accountx.command.types.CommandType.APPLY;

public class ReadExcel
{
    private final List<BankFlow> resultList;
    //private final List<BankFlowDTO> undoList;
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
            var billingNumber = (billingNumberCell == null ? "N/A" : String.valueOf(billingNumberCell.getStringCellValue()));

            var descriptionCell = row.getCell(2);
            var description = descriptionCell.getStringCellValue();


            var amountCell = row.getCell(3);
            var amount = amountCell.getNumericCellValue();


            var bankFlow = new BankFlow(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.uuuu")), LocalTime.now(),
                    description, Constant.DEFAULT_BANK_FLOW_COST_TYPE, BigDecimal.valueOf(amount), billingNumber);
            resultList.add(bankFlow);
        }

        insertDatabase();


    }

    private void insertDatabase()
    {
        try
        {
            var controller = CommandController.getInstance();
            controller.acceptApply(MULTIPLE_BANK_FLOW, new MultipleBankFlowDTO(resultList, APPLY));

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