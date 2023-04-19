module org.manager.note {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires java.xml;

    requires lombok;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.commons.lang3;

    opens org.manager.note to javafx.fxml,org.apache.logging.log4j.core;
    opens org.manager.note.controls to javafx.fxml,org.apache.logging.log4j.core;
    exports org.manager.note;
}