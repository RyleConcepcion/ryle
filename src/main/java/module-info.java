module com.newyear.storemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.newyear.storemanagement to javafx.fxml;
    exports com.newyear.storemanagement;

    opens Controllers to javafx.fxml, java.base;
    exports Controllers;

    opens Model to javafx.fxml, java.base;
    exports Model;

}