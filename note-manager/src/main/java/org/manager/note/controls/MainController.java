package org.manager.note.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class MainController {
    @FXML
    public void initialize(){
        try {
            addNewTab();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TabPane tabs;

    @FXML
    public void addNewTab() throws IOException {
        Tab tab = (Tab) FXMLLoader.load(MainController.class.getResource("new-tab.fxml"));

        tab.setText("new tab");
        tabs.getTabs().add(tab);
    }
}
