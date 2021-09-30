package com.project.crud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainLauncher extends Application {
    @Override
    public void start( Stage stage ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader( MainLauncher.class.getResource( "view/crud-program-redesign.fxml" ) );
        Scene scene = new Scene( fxmlLoader.load() );
        stage.setTitle( "Hello!" );
        stage.setScene( scene );
        stage.initStyle( StageStyle.UNDECORATED );
        stage.show();
    }

    public static void main( String[] args ) {
        launch();
    }
}