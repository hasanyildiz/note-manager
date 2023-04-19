package org.manager.note.controls;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;
import org.manager.note.constant.Shortcuts;
import org.manager.note.util.FileUtils;
import org.manager.note.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Log4j2
public class MainController {

    private int selectedTabIndex = -1;
    private final Map<String,TabInfo> tabInfos= new HashMap<>();
    private static final TabInfo DEFAULT_TAB = new TabInfo(null, null, "New Tab",null);
    @FXML
    public void initialize(){
        try {
            tabs.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
                selectedTabIndex = tabs.getSelectionModel().getSelectedIndex();
                TabInfo tabInfo = tabInfos.get(nv.getId());
                if(tabInfo!=null && tabInfo.getRelatedFile()!=null){
                    try {
                        if(!Files.exists(tabInfo.getRelatedFile().toPath())){
                            log.info("file is removed:{}",tabInfo.getRelatedFile().getAbsolutePath());
                            Alert alert = new Alert(Alert.AlertType.WARNING,
                                    "file is removed. Do you remove it from this tab?\n path:"+tabInfo.getRelatedFile().getAbsolutePath(),
                                    ButtonType.OK,
                                    ButtonType.CANCEL);
                            alert.setTitle("File is removed");
                            Optional<ButtonType> result = alert.showAndWait();
                            if(result.isPresent() && result.get() == ButtonType.OK){
                                tabs.getTabs().remove(nv);
                                tabInfos.remove(tabInfo.getTabId());
                            }
                        }else{
                            String content = FileUtils.readFileContent(tabInfo.getRelatedFile());
                            byte[] hashedText = TextUtils.hashText(content);
                            if(!Arrays.equals(hashedText, tabInfo.getFileHash())){
                                Alert alert = new Alert(Alert.AlertType.WARNING,
                                                "file is changed. Do you want to reload it?\n path:"+tabInfo.getRelatedFile().getAbsolutePath(),
                                                ButtonType.OK,
                                                ButtonType.CANCEL);
                                alert.setTitle("File is changed");
                                Optional<ButtonType> result = alert.showAndWait();

                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    TextArea textArea = (TextArea) nv.getContent();
                                    textArea.setText(content);
                                    tabInfo.setFileHash(TextUtils.hashText(content));
                                }
                                log.info("text is changed for tab:{} and file:{}",nv.getId(),tabInfo.getRelatedFile().getAbsolutePath());
                        }

                        }
                    } catch (IOException | NoSuchAlgorithmException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                log.info("selected tab name"+nv.getText());
            });

            addNewTab();
            log.info("app initialized");
        } catch (IOException e) {
            log.error("initialization error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    private TabPane tabs;

    @FXML
    public void addNewTab() throws IOException {
        createTab(DEFAULT_TAB);
    }

    private Tab createTab(TabInfo tabInfo) throws IOException {
        Tab tab = FXMLLoader.load(MainController.class.getResource("new-tab.fxml"));

        tab.setText(tabInfo.getTabHeader());

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
            }else if(Shortcuts.SAVE.getShortcut().match(event)){
                String text = textArea.getText();
                try {
                    if(tabInfo.getRelatedFile()==null || !Files.exists(Paths.get(tabInfo.getRelatedFile().getAbsolutePath() ))){
                        File fileToSave = openSaveDialog();
                        if(fileToSave!=null){
                            Files.write(fileToSave.toPath(), text.getBytes(StandardCharsets.UTF_8));
                            tabInfo.setTabHeader(fileToSave.getName());
                            tabInfo.setRelatedFile(fileToSave);
                            tabInfo.setFileHash(TextUtils.hashText(text));
                            tab.setText(tabInfo.getTabHeader());
                        }
                    }else{
                        Files.write(tabInfo.getRelatedFile().toPath(), text.getBytes(StandardCharsets.UTF_8));
                        tabInfo.setFileHash(TextUtils.hashText(text));
                    }

                } catch (IOException | NoSuchAlgorithmException e) {
                    log.error(e.getMessage(),e);
                }
            }
        });

        textArea.addEventFilter(KeyEvent.KEY_TYPED, event -> {

        });
        String tabId = UUID.randomUUID().toString();
        tab.setId(tabId);
        tabs.getTabs().add(tab);
        tabInfo.setTabId(tabId);
        tabInfos.put(tabId, tabInfo);
        return tab;
    }

    private Alert createAlert(Alert.AlertType type, String title, String desc) {
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(desc);

        return alert;
    }

    public File openSaveDialog(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File fileToSave = fileChooser.showSaveDialog(tabs.getScene().getWindow());
        return fileToSave;
    }

    @FXML
    public void openFile() throws IOException, NoSuchAlgorithmException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to open");
        fileChooser.setInitialDirectory(new File((String) System.getProperties().get("user.home")));

        File selectedFile = fileChooser.showOpenDialog(tabs.getScene().getWindow());

        if(Objects.isNull(selectedFile)) return;

        String content = FileUtils.readFileContent(selectedFile);
        TabInfo tabInfo = new TabInfo(null, selectedFile, selectedFile.getName(),TextUtils.hashText(content));
        Tab tab = createTab(tabInfo);
        TextArea textArea = (TextArea) tab.getContent();

        textArea.setText(content);

        tabs.getSelectionModel().clearAndSelect(tabs.getTabs().indexOf(tab));
    }

    public void base64Encode(ActionEvent actionEvent) {
        Tab tab = tabs.getTabs().get(selectedTabIndex);
        TextArea textArea = (TextArea) tab.getContent();
        textArea.setText(TextUtils.base64Encode(textArea.getText(), false));
    }

    public void base64EncodeWithPadding(ActionEvent actionEvent) {
        Tab tab = tabs.getTabs().get(selectedTabIndex);
        TextArea textArea = (TextArea) tab.getContent();
        textArea.setText(TextUtils.base64Encode(textArea.getText(), true));
    }

    public void base64EncodeByLine(ActionEvent actionEvent) {
        Tab tab = tabs.getTabs().get(selectedTabIndex);
        TextArea textArea = (TextArea) tab.getContent();
        ObservableList<CharSequence> paragraphs = textArea.getParagraphs();
        for(int i = 0;i<paragraphs.size();i++){
            textArea.insertText(i,TextUtils.base64Encode(paragraphs.get(i).toString(),false));
        }

    }
}
