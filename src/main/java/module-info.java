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

    opens com.example.accountx to javafx.fxml;
    exports com.example.accountx;
    opens com.example.accountx.Entity to javafx.fxml, org.hibernate.orm.core;
    exports com.example.accountx.Entity;
    exports com.example.accountx.Service;
    opens com.example.accountx.Service to javafx.fxml;
    exports com.example.accountx.util;
    opens com.example.accountx.util to javafx.fxml;
    exports com.example.accountx.HibernateConfiguration;
    opens com.example.accountx.HibernateConfiguration to javafx.fxml;
    exports com.example.accountx.Controller;
    opens com.example.accountx.Controller to javafx.fxml;
}