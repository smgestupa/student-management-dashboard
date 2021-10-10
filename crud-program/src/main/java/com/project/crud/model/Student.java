package com.project.crud.model;

public class Student {

    private final int
            studentNumber,
            yearLevel,
            age;
    private final String
            firstName,
            lastName,
            gender,
            program,
            imagePath;

    public Student( int studentNumber, String firstName, String lastName, int yearLevel, int age, String gender, String program, String imagePath ) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearLevel = yearLevel;
        this.age = age;
        this.gender = gender;
        this.program = program;
        this.imagePath = imagePath;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getProgram() {
        return program;
    }

    public String getImagePath() {
        return imagePath;
    }
}
