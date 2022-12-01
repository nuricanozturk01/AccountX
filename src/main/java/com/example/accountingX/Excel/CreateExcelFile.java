package com.example.accountingX.Excel;

import com.example.accountingX.util.Constant;
import com.example.accountingX.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class CreateExcelFile
{
    private String fileName;
    private final String EXTENSION = ".xlsx";
    protected XSSFWorkbook workbook;
    protected XSSFSheet sheet;
    private  String[] cellNames;
    private int cellSize;
    private final List<String> buffer;
    private  XSSFCell[] cells;
    private BigDecimal advanceAmount;
    private int rowCount = 5;
    private String path;
    public CreateExcelFile(String fileName, int cellSize, String[] cellNames, String path)
    {
        this.fileName = fileName;
        this.path = path;

        if (!fileName.contains(EXTENSION))
            this.fileName += EXTENSION;

        this.cellSize = cellSize;
        this.cellNames = cellNames;
        buffer = new ArrayList<>();
        cells = new XSSFCell[cellSize];
        create();
    }

    public void setCellSize(int cellSize)
    {
        this.cellSize = cellSize;
        var newCells = new XSSFCell[cellSize];
        cells = newCells;
    }

    public void setCellNames(String[] cellNames) {
        this.cellNames = cellNames;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    protected void clear() {
        buffer.clear();
    }
    public void setAdvanceAmount(BigDecimal advanceAmount){
        this.advanceAmount = advanceAmount;
    }
    public void buffer(String str)
    {
        buffer.add(str);
    }
    protected CellStyle getTitleCellStyle()
    {
        var boldStyle = workbook.createCellStyle();
        var font = workbook.createFont();
        boldStyle.setWrapText(true);
        font.setBold(true);

        boldStyle.setFont(font);
        return boldStyle;
    }
    protected void prepareCell(XSSFCell cell)
    {
        var style = getTitleCellStyle();
        cell.setCellStyle(style);
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
    }


    private void initCells(XSSFRow titleRow)
    {
        var style = getTitleCellStyle();

        for (int i = 0, c = 0; i < cellSize; i++, c++)
        {
            var cell = titleRow.createCell(c);
            cell.setCellValue(cellNames[i]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 7500);
            CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
            cells[i] = cell;
        }
    }
    protected int getRowCount(){
        return rowCount;
    }

    public String[] getCellNames() {
        return cellNames;
    }

    private void writeTitle(String title) {
        var row = sheet.createRow(rowCount - 2);
        var cell = row.createCell(2);
        cell.setCellValue(title);
        cell.setCellStyle(getTitleCellStyle());
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

    }

    protected void write(String title)
    {

        if (!title.isBlank() && !title.isEmpty() && title != null)
            writeTitle(title);
        var titleRow = sheet.createRow(rowCount++);

        initCells(titleRow);

        for (int i = 0; i < buffer.size(); i++)
        {
            var r = sheet.createRow(rowCount++);
            var str = buffer.get(i).split(Constant.DELIMITER);

            for (int j = 0, c = 0; j < cellSize; j++, c++)
            {
                cells[j] = r.createCell(c);
                cells[j].setCellValue(str[j]);
                CellUtil.setAlignment(cells[j], HorizontalAlignment.CENTER);
            }
        }
    }

    protected void saveFile()
    {
        try (FileOutputStream os = new FileOutputStream( path + "\\" + fileName)) {
            workbook.write(os);
            os.close();
            workbook.close();
        }
        catch (IOException e)
        {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Dosya kaydedilemedi...", ButtonType.OK);
        }
        catch (Exception ex) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Bir sorun oluştu...",ButtonType.OK);
        }

    }
    private void create()
    {
        try
        {
            workbook = new XSSFWorkbook();
            var fos = new FileOutputStream(new File(path + "\\" + fileName));
            sheet = workbook.createSheet("sayfa-1");
            workbook.write(fos);
            fos.close();
        }
        catch (IOException e) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Dosya kullanılıyor.. Lütfen önce dosyayı kapayın.", ButtonType.OK);
        }
        catch (Exception ex) {
            UtilFX.alertScreen(Alert.AlertType.ERROR, "Dosya Yaratılamadı...", ButtonType.OK);
        }
    }

}
