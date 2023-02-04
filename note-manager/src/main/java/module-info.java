module org.manager.note {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.manager.note to javafx.fxml;
    opens org.manager.note.controls to javafx.fxml;
    exports org.manager.note;
}