package org.manager.note.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;
import org.manager.note.constant.Shortcuts;
import org.manager.note.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Log4j2
public class MainController {
    @FXML
    public void initialize(){
        try {
            addNewTab();
            log.debug("app initialized");
        } catch (IOException e) {
            log.error("initialization error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TabPane tabs;

    @FXML
    public void addNewTab() throws IOException {
        Tab tab = createTab("New Tab");

        tabs.getTabs().add(tab);
    }

    private Tab createTab(String title) throws IOException {
        Tab tab = FXMLLoader.load(MainController.class.getResource("new-tab.fxml"));

        tab.setText(title);

        TextArea textArea = (TextArea) tab.getContent();

        textArea.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(Shortcuts.FORMAT.getShortcut().match(event)) {
                try {
                    if(textArea.getText().isBlank()) return;

                    if(tab.getText().endsWith(".xml")) textArea.setText(TextUtils.formatXml(textArea.getText()));
                    // other extensions
                } catch (Exception e) {
                    createAlert(Alert.AlertType.ERROR, "Input not valid", "An unknown error has occurred: " + e.getMessage()).showAndWait();
                }
                event.consume();
            }
        });

        return tab;
    }

    private Alert createAlert(Alert.AlertType type, String title, String desc) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(desc);

        return alert;
    }

    @FXML
    public void openFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to open");
        fileChooser.setInitialDirectory(new File((String) System.getProperties().get("user.home")));

        File selectedFile = fileChooser.showOpenDialog(tabs.getScene().getWindow());

        if(Objects.isNull(selectedFile)) return;

        String content = Files.readString(selectedFile.toPath());

        Tab tab = createTab(selectedFile.getName());
        tabs.getTabs().add(tab);

        TextArea textArea = (TextArea) tab.getContent();

        textArea.setText(content);

        tabs.getSelectionModel().clearAndSelect(tabs.getTabs().indexOf(tab));
    }
}
