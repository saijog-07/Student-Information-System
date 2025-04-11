package entity.model;

import exceptions.InvalidTeacherDataException;

import java.util.ArrayList;
import java.util.List;

public class Teacher {

    private int teacherID;
    private String firstName;
    private String lastName;
    private String email;

    private List<Course> assignedCourses;

    public Teacher() {
        this.assignedCourses = new ArrayList<>();
    }

    public Teacher(int teacherID, String firstName, String lastName, String email) {
        this();
        this.teacherID = teacherID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // 🔄 Use method name "assignCourse" as expected
    public void assignCourse(Course course) {
        if (course != null && !assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }

    public void updateTeacherInfo(String name, String email, String expertise) throws InvalidTeacherDataException {
        if (name == null || email == null) {
            throw new InvalidTeacherDataException("Teacher name and email are required.");
        }
        String[] split = name.split(" ");
        this.firstName = split[0];
        this.lastName = split.length > 1 ? split[1] : "";
        this.email = email;
    }

    public void displayTeacherInfo() {
        System.out.println("Teacher ID : " + teacherID);
        System.out.println("Name       : " + firstName + " " + lastName);
        System.out.println("Email      : " + email);
    }

    // Getters and Setters

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
