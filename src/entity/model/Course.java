package entity.model;

import java.util.List;
import service.EnrollmentService;
import exceptions.InvalidCourseDataException;
import java.util.ArrayList;

public class Course {

	    private int courseID;
	    private String courseName;
	    private String courseCode;
	    private int credits;
	    private Teacher instructorName;

	    private List<Enrollment> enrollments;

	    public Course() {
	        this.enrollments = new ArrayList<>();
	    }

	    public Course(int courseID, String courseName, int credits, String courseCode, Teacher instructorName) {
	        this();
	        this.courseID = courseID;
	        this.courseName = courseName;
	        this.credits = credits;
	        this.courseCode = courseCode;
	        this.instructorName = instructorName;
	    }

	    public void addEnrollment(Enrollment e) {
	        enrollments.add(e);
	    }

	    public List<Enrollment> getEnrollmentsList() {
	        return enrollments;
	    }

    public void assignTeacher(Teacher teacher) throws exceptions.InvalidTeacherDataException {
        if (teacher == null || teacher.getFirstName() == null) {
            throw new exceptions.InvalidTeacherDataException("Invalid teacher data provided.");
        }
        this.instructorName = teacher;
        System.out.println("✅ Teacher assigned to course " + courseName);
    }

    public void updateCourseInfo(String courseCode, String courseName, String instructor)
            throws InvalidCourseDataException {
        if (courseCode == null || courseName == null) {
            throw new InvalidCourseDataException("Course code and name must not be null.");
        }
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public void displayCourseInfo() {
        System.out.println("Course ID: " + courseID);
        System.out.println("Name     : " + courseName);
        System.out.println("Code     : " + courseCode);
        System.out.println("Instructor: " + (instructorName != null ? instructorName.getFirstName() : "None"));
    }

    public List<Enrollment> getEnrollments() {
        return new EnrollmentService().getEnrollmentsByCourse(courseID);
    }

    public Teacher getTeacher() {
        return instructorName;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Teacher getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(Teacher instructorName) {
        this.instructorName = instructorName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}