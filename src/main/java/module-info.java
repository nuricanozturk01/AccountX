/**
 *
 */
module com.example.yediiklim
{
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires org.hibernate.commons.annotations;
    requires org.apache.poi.poi;
    requires static lombok;
    requires org.apache.poi.ooxml;
    requires org.apache.xmlbeans;

    opens com.example.accountingX to javafx.fxml;
    exports com.example.accountingX;
    opens com.example.accountingX.Entity to javafx.fxml, org.hibernate.orm.core;
    exports com.example.accountingX.Entity;
    exports com.example.accountingX.Service;
    opens com.example.accountingX.Service to javafx.fxml;
    exports com.example.accountingX.util;
    opens com.example.accountingX.util to javafx.fxml;
    exports com.example.accountingX.HibernateConfiguration;
    opens com.example.accountingX.HibernateConfiguration to javafx.fxml;
    exports com.example.accountingX.Controller;
    opens com.example.accountingX.Controller to javafx.fxml;
}