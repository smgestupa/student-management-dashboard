package com.project.crud.controller;

import com.project.crud.model.Student;
import com.project.crud.listener.Listen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

public class StudentController {

    @FXML private Label firstNameLabel, studentIdLabel, lastNameLabel;
    @FXML private ImageView studentGender;

    private Student student;
    private Listen listener;

    @FXML
    private void click( MouseEvent event ) {
        listener.onClickListener( event, student );
    }

    public void setData( Student student, Listen listener ) throws IOException {
        this.student = student;
        this.listener = listener;

        studentIdLabel.setText( String.valueOf( student.getStudentNumber() ) );
        firstNameLabel.setText( student.getFirstName() );
        lastNameLabel.setText( student.getLastName() );

        if ( !student.getImagePath().equals( "null" ) ) {
            studentGender.setImage( new Image( student.getImagePath() ) );
        } else {
            if ( student.getGender().equals( "Male" ) ) studentGender.setImage( new Image( Objects.requireNonNull( this.getClass().getResourceAsStream("/com/project/crud/images/male-student.png") ) ) );
            else studentGender.setImage( new Image( Objects.requireNonNull( this.getClass().getResourceAsStream( "/com/project/crud/images/female-student.png") ) ) );
        }
    }
}
