//package com.project.crud.controller;
//
//import com.project.crud.listener.Listen;
//import com.project.crud.model.Student;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.geometry.Insets;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Region;
//import javafx.scene.layout.VBox;
//
//import java.io.*;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class CrudController implements Initializable {
//
//    @FXML private Button addButton, deleteButton, updateButton, searchButton;
//    @FXML private TextField searchField;
//    @FXML private GridPane grid;
//
//    private List< Student > students = new ArrayList<>();
//    private Listen listener;
//    private String studentName;
//
//    BufferedReader read = null;
//    BufferedWriter write = null;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        addStudents();
//    }
//
//    public List< Student > getStudents() throws IOException {
//        List< Student > students = new ArrayList<>();
//
//        try {
//            read = new BufferedReader( new FileReader( "database/students-list.txt" ) );
//
//            String s;
//            while ( ( s = read.readLine() ) != null ) {
//                String[] entry = s.split( "&" );
//                students.add( new Student( entry[0], entry[1], entry[2], entry[3], entry[4], entry[5], entry[6] ) );
//            }
//        } catch ( IOException err ) {
//            err.printStackTrace();
//        } finally {
//            if ( read != null ) read.close();
//        }
//
//        return students;
//    }
//
//    public void addStudent() throws IOException {
//        String input = searchField.getText().trim();
//
//        if ( !input.isEmpty() ){
//            String[] entry = input.split( "," );
//
//            try {
//                write = new BufferedWriter( new FileWriter( "database/students-list.txt", true ) );
//                write.append( entry[0] + "&" + entry[1] + "&" + entry[2] + "&" + entry[3] + "&" + entry[4] + "&" + entry[5] + "&" + entry[6] );
//                write.append( "\n" );
//            } catch ( IOException err ) {
//                err.printStackTrace();
//            } finally {
//                if ( write != null ) write.close();
//            }
//        }
//
//        addStudents();
//    }
//
//    public void addStudents() {
//        int column = 0;
//        int row = 1;
//
//        try {
//            grid.getChildren().clear();
//            students.clear();
//            students.addAll( getStudents() );
//            if ( students.size() > 0 ) {
//                listener = new Listen() {
//                    @Override
//                    public void onClickListener(MouseEvent event, Student student) {
//                        studentName = student.getFirstName();
//                    }
//                };
//            }
//
//            for ( Student student : students ) {
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation( getClass().getResource( "/com/project/crud/components/student-model.fxml" ) );
//                VBox vbox = loader.load();
//
//                StudentController studentController = loader.getController();
//                studentController.setData( student, listener );
//                if ( column == 4 ) {
//                    column = 0;
//                    row++;
//                }
//
//                grid.add( vbox, column++, row );
//                grid.setMinWidth( Region.USE_COMPUTED_SIZE );
//                grid.setPrefWidth( Region.USE_COMPUTED_SIZE );
//                grid.setMaxWidth( Region.USE_PREF_SIZE );
//
//                grid.setMinHeight( Region.USE_COMPUTED_SIZE );
//                grid.setPrefHeight( Region.USE_COMPUTED_SIZE );
//                grid.setMaxHeight( Region.USE_PREF_SIZE );
//
//                GridPane.setMargin( vbox, new Insets( 10 ) );
//            }
//        } catch ( IOException err ) {
//            err.printStackTrace();
//        }
//    }
//}