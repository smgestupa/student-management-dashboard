module com.project.crud.crudprogram {
    requires javafx.controls;
    requires javafx.fxml;
            
                        
    opens com.project.crud to javafx.fxml;
    exports com.project.crud;
    exports com.project.crud.controller;
    opens com.project.crud.controller to javafx.fxml;
}