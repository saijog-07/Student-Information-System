package entity.model;

import java.util.Date;

public class Enrollment {

    private int enrollmentID;
    private Student student;
    private Course course;
    private Date enrollmentDate;

    public Enrollment() {
        super();
    }

    public Enrollment(int enrollmentID, Student student, Course course, Date enrollmentDate) {
        this.enrollmentID = enrollmentID;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public int getEnrollmentID() {
        return enrollmentID;
    }

    public void setEnrollmentID(int enrollmentID) {
        this.enrollmentID = enrollmentID;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}