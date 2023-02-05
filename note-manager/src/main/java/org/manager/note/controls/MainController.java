package org.manager.note.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        tabs.getTabs().add(createTab("New Tab"));
    }

    private Tab createTab(String title) throws IOException {
        Tab tab = FXMLLoader.load(MainController.class.getResource("new-tab.fxml"));

        tab.setText(title);

        return tab;
    }

    @FXML
    public void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to open");
        fileChooser.setInitialDirectory(new File((String) System.getProperties().get("user.home")));

        File selectedFile = fileChooser.showOpenDialog(tabs.getScene().getWindow());

        String content = Files.readString(selectedFile.toPath());

        Tab tab = createTab(selectedFile.getName());
        tabs.getTabs().add(tab);

        TextArea textArea = (TextArea) tab.getContent();

        textArea.setText(content);

        tabs.getSelectionModel().clearAndSelect(tabs.getTabs().indexOf(tab));
    }
}
