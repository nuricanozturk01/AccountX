package com.example.accountingX.Excel;

import com.example.accountingX.Entity.*;
import com.example.accountingX.HibernateConfiguration.SessionFactoryManager;
import com.example.accountingX.util.UtilFX;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExportManagerReport extends CreateExcelFile{

    private final List<FundFlow> fundFlows;
    private final List<TreasureFlow> treasureFlows;
    private final List<OtherFlow> otherFlows;
    private final List<CostTypeCostMapper> costs;

    public ExportManagerReport(String fileName, int cellSize, String[] cellNames, String path)
    {
        super(fileName, cellSize, cellNames, path);

        costs = new ArrayList<>();
        fundFlows = SessionFactoryManager.getFundFlows();
        otherFlows = SessionFactoryManager.getOtherFlows();
        treasureFlows = SessionFactoryManager.getTreasureFlows();
        createCosts();
    }


    private void createCosts() {
        var types = SessionFactoryManager.getCostTypes();
        types.forEach(this::calculateAndSave);
    }

    private void calculateAndSave(CostType costType) {

        var now = LocalDate.now();
        var year = now.getYear();
        var month = now.getMonthValue();
        var cost_form = SessionFactoryManager.getSumOfTypes(costType.getCostType());
        var bank_flow =SessionFactoryManager.getSumOfTypesBankFlow(costType.getCostType());

        var this_month_cost_form = SessionFactoryManager.getSumOfTypes(costType.getCostType(), month, year);
        var this_month_bank_form = SessionFactoryManager.getSumOfTypesBankFlow(costType.getCostType(), year, month);

        this_month_bank_form = (this_month_bank_form == null ? BigDecimal.ZERO : this_month_bank_form);
        this_month_cost_form = (this_month_cost_form == null ? BigDecimal.ZERO : this_month_cost_form);

        cost_form = (cost_form == null ? BigDecimal.ZERO : cost_form);
        bank_flow = (bank_flow == null ? BigDecimal.ZERO : bank_flow);

        var result = cost_form.add(bank_flow);

        var thisMonth = this_month_cost_form.add(this_month_bank_form);
        costs.add(new CostTypeCostMapper(LocalDate.now(), costType.getCostType(), cost_form, result, bank_flow, thisMonth));
    }
    public void export()
    {
        writeTitle();

        writeFundFlows();
        write("SERMAYE GİDERLERİ");
        writeTotal(getRowCount(), fundFlows);
        clear();

        setRowCount(getRowCount() + 6);
        writeOtherFlows();
        write("ŞİRKETE AİT OLMAYAN DİĞER GİDERLER");
        writeTotal(getRowCount(), otherFlows);
        clear();

        setRowCount(getRowCount() + 6);
        writeTreasureFlows();
        write("GİRDİLER VE MEVCUT KIYMETLER");
        writeTotal(getRowCount(), treasureFlows);
        clear();


        setCellSize(6);
        var mon = Months_TR.values()[LocalDate.now().getMonthValue()] + "(₺)";
        setCellNames(new String[] {"Tarih", "Açıklama", "Toplam(₺)", "Banka Hareketleri", "Kasa Hareketleri(₺)", mon});
        setRowCount(getRowCount() + 6);
        writeCostTypesWithCosts();
        write("GİDER TÜRLERİ");
        clear();

        saveFile();
        UtilFX.alertScreen(Alert.AlertType.INFORMATION, "Yönetici Özeti Oluşturuldu...", ButtonType.OK);
    }
    private <T> BigDecimal getTotal(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        if (list.get(0) instanceof FundFlow)
            return fundFlows.stream().map(FundFlow::getResultAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (list.get(0) instanceof TreasureFlow)
            return treasureFlows.stream().map(TreasureFlow::getResultAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (list.get(0) instanceof OtherFlow)
            return otherFlows.stream().map(OtherFlow::getResultAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (list.get(0) instanceof CostTypeCostMapper)
            return costs.stream().map(CostTypeCostMapper::getM_cost).reduce(BigDecimal.ZERO, BigDecimal::add);
        return BigDecimal.ZERO;
    }
    private<T> void writeTotal(int rowCount, List<T> list) {
        var row = sheet.createRow(++rowCount);
        var cell = row.createCell(4);
        var sum = getTotal(list);
        cell.setCellValue("TOPLAM: " + sum);
        cell.setCellStyle(getTitleCellStyle());
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
    }


    private void writeCostTypesWithCosts() {

        costs.stream().map(CostTypeCostMapper::getExcelFormat).forEach(this::buffer);
    }


    private void writeTreasureFlows() {

        treasureFlows.stream().map(TreasureFlow::getExcelFormat).forEach(this::buffer);
    }

    private void writeOtherFlows()
    {
        otherFlows.stream().map(OtherFlow::getExcelFormat).forEach(this::buffer);
    }

    private void writeFundFlows() {
        fundFlows.stream().map(FundFlow::getExcelFormat).forEach(this::buffer);
    }

    private void writeTitle() {
        var row = sheet.createRow(1);
        var cell = row.createCell(2);
        cell.setCellValue("YÖNETİCİ ÖZETİ");
        prepareCell(cell);
    }

}
