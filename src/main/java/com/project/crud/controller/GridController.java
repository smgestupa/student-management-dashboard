package com.project.crud.controller;

import com.project.crud.listener.Listen;
import com.project.crud.model.Student;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class GridController {

    public GridController( List< Student > students, GridPane grid, Listen listener ) {
        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                int column = 0;
                int row = 1;

                try {
                    for ( Student student : students ) {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation( getClass().getResource( "/com/project/crud/components/student-model.fxml" ) );
                        VBox vbox = loader.load();

                        StudentController studentController = loader.getController();
                        studentController.setData( student, listener );

                        if ( column == 4 ) {
                            column = 0;
                            row++;
                        }

                        grid.add( vbox, column++, row );
                        grid.setMinWidth( Region.USE_COMPUTED_SIZE );
                        grid.setPrefWidth( Region.USE_COMPUTED_SIZE );
                        grid.setMaxWidth( Region.USE_PREF_SIZE );

                        grid.setMinHeight( Region.USE_COMPUTED_SIZE );
                        grid.setPrefHeight( Region.USE_COMPUTED_SIZE );
                        grid.setMaxHeight( Region.USE_PREF_SIZE );

                        GridPane.setMargin( vbox, new Insets( 17 ) );
                    }
                } catch ( IOException err ) {
                    System.err.println( "Warning! IOException has occurred at GridController thread: " + err.getMessage() );
                }
            }
        } );
    }
}
