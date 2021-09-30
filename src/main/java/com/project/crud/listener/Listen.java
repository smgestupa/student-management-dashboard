package com.project.crud.listener;

import com.project.crud.model.Student;
import javafx.scene.input.MouseEvent;

public interface Listen {

    public void onClickListener( MouseEvent event, Student student ); // creates an interface that can be used by the [StudentController] class
}
