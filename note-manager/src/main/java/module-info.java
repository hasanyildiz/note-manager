module org.manager.note {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.xml;

    requires lombok;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;


    opens org.manager.note to javafx.fxml;
    opens org.manager.note.controls to javafx.fxml;
    exports org.manager.note;
}