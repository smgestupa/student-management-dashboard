package com.project.crud.controller;

import com.project.crud.listener.Listen;
import com.project.crud.model.Student;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class CrudRedesignController implements Initializable  {

    ObservableList< String > yearLevelList = FXCollections.observableArrayList( "1", "2", "3", "4" );
    ObservableList< String > genderList = FXCollections.observableArrayList( "Male", "Female" );
    ObservableList< String > programList = FXCollections.observableArrayList("College of Arts", "College of Business Education", "College of Engineering and Architecture", "College of Information Technology Education","Graduate Program", "Maritime Education");
    ObservableList< String > sortingList = FXCollections.observableArrayList( "Unsorted", "Student Number", "Last Name" );

    @FXML private BorderPane windowProgram;
    @FXML private Button addButton, editButton, deleteButton;
    @FXML private GridPane grid;
    @FXML private HBox addStudentPane, editStudentPane, deleteStudentPane, studentInfoPane;
    @FXML private VBox studentListPane;
    @FXML private TextField
            searchField,
            studentIdField, firstNameField, lastNameField, ageField,
            editStudentIdField, editFirstNameField, editLastNameField, editAgeField;
    @FXML private ComboBox< String >
            yearLevelBox, genderBox, programBox,
            editYearLevelBox, editGenderBox, editProgramBox,
            sortingBox;
    @FXML private Label
            deleteStudentIdText, deleteFirstNameText, deleteLastNameText, deleteYearLevelText, deleteAgeText, deleteGenderText, deleteProgramText,
            infoStudentIdLabel, infoFirstNameLabel, infoLastNameLabel, infoAgeLabel, infoGenderLabel, infoYearLevelLabel, infoProgramLabel;
    @FXML private ImageView infoStudentImage;

    double xOffset;
    double yOffset;

    private List<Student> students = new ArrayList<>();
    private Student selectedStudent;
    private Listen listener;

    BufferedReader read = null;
    BufferedWriter write = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addStudents( getStudents() );
        } catch ( IOException err ) {
            System.err.println( "Warning! IOException has occurred at initialize() function: " + err.getMessage() );
        }

        yearLevelBox.getItems().addAll( yearLevelList );
        genderBox.getItems().addAll( genderList );
        programBox.getItems().addAll( programList );
        editYearLevelBox.getItems().addAll( yearLevelList );
        editGenderBox.getItems().addAll( genderList );
        editProgramBox.getItems().addAll( programList );
        sortingBox.getItems().addAll( sortingList );
        sortingBox.getSelectionModel().select( 0 );
    }

    public List< Student > getStudents() throws IOException {
        List< Student > students = new ArrayList<>();

        try {
            read = new BufferedReader( new FileReader( "database/students-list.txt" ) );

            String s;
            while ( ( s = read.readLine() ) != null ) {
                if ( s.trim().isEmpty() ) continue;

                String[] entry = s.split( "&" );
                students.add( new Student( Integer.parseInt( entry[0] ), entry[1], entry[2], Integer.parseInt( entry[3] ), Integer.parseInt( entry[4] ), entry[5], entry[6] ) );
            }
        } catch ( IOException err ) {
            System.err.println( "Warning! IOException has occurred at getStudents() function: " + err.getMessage() );
        } finally {
            if ( read != null ) read.close();
        }

        return students;
    }

    public void addStudents( List< Student > studentsList ) {
        students.clear();
        students.addAll( studentsList );
        if ( sortingBox.getSelectionModel().getSelectedIndex() == 1 ) students.sort( Comparator.comparing( Student::getStudentNumber ) );
        else if ( sortingBox.getSelectionModel().getSelectedIndex() == 2 ) students.sort( Comparator.comparing( Student::getLastName ) );
        if ( students.size() > 0 ) {
            listener = new Listen() {
                @Override
                public void onClickListener( MouseEvent event, Student student ) {
                    if ( event.getButton().equals( MouseButton.PRIMARY ) ) {
                        selectedStudent = student;

                        if ( event.getClickCount() == 2 ) {
                            infoStudentIdLabel.setText( String.valueOf( student.getStudentNumber() ) );
                            infoFirstNameLabel.setText( student.getFirstName() );
                            infoLastNameLabel.setText( student.getLastName() );
                            infoAgeLabel.setText( String.valueOf( student.getAge() ) );
                            infoGenderLabel.setText( student.getGender() );
                            infoYearLevelLabel.setText( String.valueOf( student.getYearLevel() ) );
                            infoProgramLabel.setText( student.getProgram() );
                            if ( student.getGender().equals( "Male" ) ) infoStudentImage.setImage( new Image( this.getClass().getResourceAsStream( "/com/project/crud/images/male-student.png" ) ) );
                            else infoStudentImage.setImage( new Image( this.getClass().getResourceAsStream( "/com/project/crud/images/female-student.png" ) ) );

                            studentInfoPane.toFront();
                        }
                    }
                }
            };
        }

        new GridController( students, grid, listener );
    }

    @FXML
    void addStudent() throws IOException {
        boolean isFilledOut =
                !studentIdField.getText().trim().isEmpty() &&
                !firstNameField.getText().trim().isEmpty() &&
                !lastNameField.getText().trim().isEmpty() &&
                !yearLevelBox.getSelectionModel().isEmpty() &&
                !ageField.getText().trim().isEmpty() &&
                !genderBox.getSelectionModel().isEmpty() &&
                !programBox.getSelectionModel().isEmpty();

        if ( isFilledOut ) {
            boolean studentIdIsNumber = checkIfNumber( studentIdField.getText().trim() );
            boolean ageIsNumber = checkIfNumber( ageField.getText().trim() );

            if ( studentIdIsNumber && ageIsNumber ) {
                boolean isDuplicate = checkDuplicate( studentIdField.getText() );

                if ( !isDuplicate ) {
                    String studentId = studentIdField.getText().trim();
                    String firstName = firstNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String yearLevel = yearLevelBox.getValue();
                    String age = ageField.getText().trim();
                    String gender = genderBox.getValue();
                    String program = programBox.getValue();

                    try {
                        write = new BufferedWriter( new FileWriter( "database/students-list.txt", true ) );
                        write.append( studentId + "&" + firstName + "&" + lastName + "&" + yearLevel + "&" + age + "&" + gender + "&" + program );
                        write.append( "\n" );
                    } catch ( IOException err ) {
                        System.err.println( "Warning! IOException has occurred at addStudent() function: " + err.getMessage() );
                    } finally {
                        if ( write != null ) write.close();
                    }

                    addStudents( getStudents() );
                    closePane();

                    Alert alert = new Alert( Alert.AlertType.INFORMATION );
                    alert.setTitle( "Success!" );
                    alert.setHeaderText( "You have successfully added a student." );
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert( Alert.AlertType.WARNING );
                    alert.setTitle( "Warning!" );
                    alert.setHeaderText( "A duplicate entry has been found." );
                    alert.setContentText( "You should edit the entry instead." );

                    alert.showAndWait();
                }
            } else if ( !studentIdIsNumber ) {
                Alert alert = new Alert( Alert.AlertType.WARNING );
                alert.setTitle( "Warning!" );
                alert.setHeaderText( "The Student ID must be a number." );
                alert.setContentText( "Please try again." );

                alert.showAndWait();
            } else {
                Alert alert = new Alert( Alert.AlertType.WARNING );
                alert.setTitle( "Warning!" );
                alert.setHeaderText( "The student's age must be a number." );
                alert.setContentText( "Please try again." );

                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setTitle( "Warning!" );
            alert.setHeaderText( "You must fill up everything before adding an entry." );
            alert.setContentText( "Please try again." );

            alert.showAndWait();
        }

    }

    boolean checkDuplicate( String studentNumber ) throws IOException {
        boolean isDuplicate = false;

        try {
            read = new BufferedReader( new FileReader( "database/students-list.txt" ) );

            String s;
            while ( ( s = read.readLine() ) != null ) {
                if ( Pattern.compile( studentNumber ).matcher( s ).find() ) {
                    isDuplicate = true;
                    break;
                }
            }
        } catch( IOException err ) {
            System.err.println( "Warning! IOException has occurred at checkDuplicate() function: " + err.getMessage() );
        } finally {
            if ( read != null ) read.close();
        }

        return isDuplicate;
    }

    @FXML
    void editStudent() throws IOException {
        try {
            read = new BufferedReader( new FileReader( "database/students-list.txt" ) );
            StringBuffer fileContent = new StringBuffer();

            boolean studentIdIsNumber = checkIfNumber( editStudentIdField.getText().trim() );
            boolean ageIsNumber = checkIfNumber( editAgeField.getText().trim() );

            if ( studentIdIsNumber && ageIsNumber ) {
                String s;
                while ( ( s = read.readLine() ) != null ) {
                    if ( Pattern.compile( String.valueOf( selectedStudent.getStudentNumber() ) ).matcher( s ).find() ) {
                        String studentNumber = ( !editStudentIdField.getText().trim().isEmpty() ) ? editStudentIdField.getText().trim() : String.valueOf( selectedStudent.getStudentNumber() );
                        String firstName = ( !editFirstNameField.getText().trim().isEmpty() ) ? editFirstNameField.getText().trim() : selectedStudent.getFirstName();
                        String lastName = ( !editLastNameField.getText().trim().isEmpty() ) ? editLastNameField.getText().trim() : selectedStudent.getLastName();
                        String yearLevel = ( !editYearLevelBox.getSelectionModel().isEmpty() ) ? editYearLevelBox.getValue() : String.valueOf( selectedStudent.getYearLevel() );
                        String age = ( !editAgeField.getText().trim().isEmpty() ) ? editAgeField.getText().trim() : String.valueOf( selectedStudent.getAge() );
                        String gender = ( !editGenderBox.getSelectionModel().isEmpty() ) ? editGenderBox.getValue() : selectedStudent.getGender();
                        String program = ( !editProgramBox.getSelectionModel().isEmpty() ) ? editProgramBox.getValue(): selectedStudent.getProgram();

                        fileContent.append( studentNumber + "&" + firstName + "&" + lastName + "&" + yearLevel + "&" + age + "&" + gender + "&" + program );
                        fileContent.append( "\n" );
                        continue;
                    }

                    fileContent.append( s );
                    fileContent.append( "\n" );
                }

                write = new BufferedWriter( new FileWriter( "database/students-list.txt" ) );
                write.write( fileContent.toString() );

                addStudents( getStudents() );
                closePane();

                Alert alert = new Alert( Alert.AlertType.INFORMATION );
                alert.setTitle( "Success!" );
                alert.setHeaderText( "You have successfully edited a student entry." );
                alert.showAndWait();
            } else if ( !studentIdIsNumber ) {
                Alert alert = new Alert( Alert.AlertType.WARNING );
                alert.setTitle( "Warning!" );
                alert.setHeaderText( "The Student ID must be a number." );
                alert.setContentText( "Please try again." );

                alert.showAndWait();
            } else {
                Alert alert = new Alert( Alert.AlertType.WARNING );
                alert.setTitle( "Warning!" );
                alert.setHeaderText( "The student's age must be a number." );
                alert.setContentText( "Please try again." );

                alert.showAndWait();
            }
        } catch ( IOException err ) {
            System.err.println( "Warning! IOException has occurred at editStudent() function: " + err.getMessage() );
        } finally {
            if ( read != null ) read.close();
            if ( write != null ) write.close();
        }
    }

    @FXML
    void confirmDeleteStudent() throws IOException {
        Alert confirm = new Alert( Alert.AlertType.CONFIRMATION );
        confirm.setTitle( "Confirmation Required" );
        confirm.setHeaderText( "Would you like to delete " + selectedStudent.getFirstName() + "'s entry?" );

        Optional< ButtonType > result = confirm.showAndWait();
        if ( result.get() == ButtonType.OK) {
            try {
                read = new BufferedReader( new FileReader( "database/students-list.txt" ) );
                StringBuffer newFileContent = new StringBuffer();

                String s;
                while ( ( s = read.readLine() ) != null ) {
                    if ( Pattern.compile( String.valueOf( selectedStudent.getStudentNumber() ) ).matcher( s ).find() ) continue;

                    newFileContent.append( s );
                    newFileContent.append( "\n" );
                }

                write = new BufferedWriter( new FileWriter( "database/students-list.txt" ) );
                write.write( newFileContent.toString() );
                selectedStudent = null;

                addStudents( getStudents() );
                closePane();

                Alert alert = new Alert( Alert.AlertType.INFORMATION );
                alert.setTitle( "Success!" );
                alert.setHeaderText( "You have successfully deleted a student entry." );
                alert.showAndWait();
            } catch ( IOException err ) {
                System.err.println( "Warning! IOException has occurred at confirmDeleteStudent() function: " + err.getMessage() );
            } finally {
                if ( read != null ) read.close();
                if ( write != null ) write.close();
            }
        }
    }

    @FXML
    void searchStudents() throws IOException {
        List< Student > students = new ArrayList<>();

        try {
            read = new BufferedReader( new FileReader( "database/students-list.txt" ) );

            String s;
            while ( ( s = read.readLine() ) != null ) {
                if ( !Pattern.compile( searchField.getText(), Pattern.CASE_INSENSITIVE ).matcher( s ).find() ) continue;

                String[] entry = s.split( "&" );
                students.add( new Student( Integer.parseInt( entry[0] ), entry[1], entry[2], Integer.parseInt( entry[3] ), Integer.parseInt( entry[4] ), entry[5], entry[6] ) );
            }

            addStudents( students );
        } catch ( IOException err ) {
            System.err.println( "Warning! IOException has occurred at searchStudents() function: " + err.getMessage() );
        } finally {
            if ( read != null ) read.close();
        }
    }

    @FXML
    void sortStudents() throws IOException {
        searchStudents();
    }

    @FXML
    void closePane() {
        studentListPane.toFront();
        addButton.getStyleClass().clear();
        editButton.getStyleClass().clear();
        deleteButton.getStyleClass().clear();
        addButton.getStyleClass().add( "button" );
        editButton.getStyleClass().add( "button" );
        deleteButton.getStyleClass().add( "button" );
    }

    @FXML
    void openAddStudentPane() {
        studentListPane.toFront();
        addStudentPane.toFront();

        addButton.getStyleClass().clear();
        editButton.getStyleClass().clear();
        deleteButton.getStyleClass().clear();
        addButton.getStyleClass().add( "button-selected" );
        editButton.getStyleClass().add( "button" );
        deleteButton.getStyleClass().add( "button" );

        studentIdField.setText( "" );
        firstNameField.setText( "" );
        lastNameField.setText( "" );
        yearLevelBox.getSelectionModel().select( 0 );
        ageField.setText( "" );
        genderBox.getSelectionModel().select( 0 );
        programBox.getSelectionModel().select( 0 );
    }

    @FXML
    void openEditStudentPane() {
        if ( selectedStudent != null ) {
            studentListPane.toFront();
            editStudentPane.toFront();

            addButton.getStyleClass().clear();
            editButton.getStyleClass().clear();
            deleteButton.getStyleClass().clear();
            addButton.getStyleClass().add( "button" );
            editButton.getStyleClass().add( "button-selected" );
            deleteButton.getStyleClass().add( "button" );

            editStudentIdField.setText( String.valueOf( selectedStudent.getStudentNumber() ) );
            editFirstNameField.setText( selectedStudent.getFirstName() );
            editLastNameField.setText( selectedStudent.getLastName() );
            editYearLevelBox.getSelectionModel().select( String.valueOf( selectedStudent.getYearLevel() ) );
            editAgeField.setText( String.valueOf(  selectedStudent.getAge() ) );
            editGenderBox.getSelectionModel().select( selectedStudent.getGender() );
            editProgramBox.getSelectionModel().select( selectedStudent.getProgram() );
        } else {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setTitle( "Warning!" );
            alert.setHeaderText( "You must choose a student first before opening the editing interface." );

            alert.showAndWait();
        }
    }

    @FXML
    void openDeleteStudentPane() {
        if ( selectedStudent != null ) {
            studentListPane.toFront();
            deleteStudentPane.toFront();

            addButton.getStyleClass().clear();
            editButton.getStyleClass().clear();
            deleteButton.getStyleClass().clear();
            addButton.getStyleClass().add( "button" );
            editButton.getStyleClass().add( "button" );
            deleteButton.getStyleClass().add( "button-selected" );

            deleteStudentIdText.setText( String.valueOf( selectedStudent.getStudentNumber() ) );
            deleteFirstNameText.setText( selectedStudent.getFirstName() );
            deleteLastNameText.setText( selectedStudent.getLastName() );
            deleteYearLevelText.setText( String.valueOf( selectedStudent.getYearLevel() ) );
            deleteAgeText.setText( String.valueOf( selectedStudent.getAge() ) );
            deleteGenderText.setText( selectedStudent.getGender() );
            deleteProgramText.setText( selectedStudent.getProgram() );
        } else {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setTitle( "Warning!" );
            alert.setHeaderText( "You must choose a student first before opening the delete interface." );

            alert.showAndWait();
        }
    }

    @FXML
    void onClickedMouse( MouseEvent event ) {
        xOffset = windowProgram.getScene().getWindow().getX() - event.getScreenX();
        yOffset = windowProgram.getScene().getWindow().getY() - event.getScreenY();
    }

    @FXML
    void onDraggedMouse( MouseEvent event ) {
        windowProgram.getScene().getWindow().setX( event.getScreenX() + xOffset );
        windowProgram.getScene().getWindow().setY( event.getScreenY() + yOffset );
    }

    @FXML
    void closeProgram() {
        Alert confirm = new Alert( Alert.AlertType.CONFIRMATION );
        confirm.setTitle( "Confirmation Required" );
        confirm.setHeaderText( "You are about to close the program.");

        Optional< ButtonType > result = confirm.showAndWait();
        if ( result.get() == ButtonType.OK ) {
            System.exit( 0 );
        }
    }

    @FXML
    void minimizeProgram() {
        Stage stage = ( Stage ) windowProgram.getScene().getWindow();
        stage.setIconified( true );
    }

    boolean checkIfNumber( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        } catch ( NumberFormatException err ) {
            return false;
        }
    }

}