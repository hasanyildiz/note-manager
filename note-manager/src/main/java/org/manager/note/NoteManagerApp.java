package org.manager.note;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class NoteManagerApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NoteManagerApp.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Note Manager!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try{
            launch();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }
}