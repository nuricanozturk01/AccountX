package com.example.accountx.util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Constant
{
    public static DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###.00");
    public static DecimalFormat NUMBER_LESS_ZERO_FORMAT = new DecimalFormat("##0.###");
    private final static String DATE_TIME_FORMAT = "dd/MM/yyyy";

    public static String ZERO = "0,00";
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    public static String DELIMITER = "~";
    public static final String ICON = "yedi_iklim.jpg";
    public static final int MAX_OFF_DAY = 15;
    public static final int START_YEAR = 2015;
    public static final int STOP_YEAR = LocalDate.now().getYear() + 1;
    public static final String HIBERNATE_CONFIGURATION_FILE = "hibernate.cfg.xml";
    public static final String[] EXCEL_COST_FORM_TITLES = new String[]
            {"Fatura Tarihi", "Fiş Numarası", "Açıklama", "Gider Türü", "Fatura Tutarı(₺)", "KDV(%)", "Toplam Tutar(₺)", "Harcama Sorumlusu", "Harcanan yer"};

    public static final String[] EXCEL_BANK_FLOW_TITLES = new String[]{"Fatura Tarihi", "Fiş Numarası", "Açıklama", "Gider Türü", "Fatura Tutarı(₺)"};
    public static final String[] EXCEL_USERS_TITLES = new String[]{"İsim", "Soyisim", "Kalan Avans Miktarı"};
    public static final String[] EXCEL_COST_TYPES_TITLES = new String[]{"Gider Türü"};

    public static final String DEFAULT_BANK_FLOW_COST_TYPE = "BANK FLOW";
}
