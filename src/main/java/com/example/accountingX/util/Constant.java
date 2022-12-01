package com.example.accountingX.util;

import java.time.LocalDate;

public final class Constant
{
    public static String DELIMITER = "~";
    public static final String ICON = "icon.png";
    public static final int MAX_OFF_DAY = 15;
    public static final int START_YEAR = 2015;
    public static final int STOP_YEAR = LocalDate.now().getYear() + 1;
    public static final String HIBERNATE_CONFIGURATION_FILE = "hibernate.cfg.xml";

    public static final String[] EXCEL_COST_FORM_TITLES = new String[]
            {"Fatura Tarihi", "Fiş Numarası", "Açıklama", "Gider Türü", "Fatura Tutarı(₺)", "KDV(%)", "Toplam Tutar(₺)", "Harcama Sorumlusu", "Harcanan yer"};


}
