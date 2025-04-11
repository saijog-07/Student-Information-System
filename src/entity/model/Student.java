package entity.model;

import exceptions.InvalidStudentDataException;
import service.EnrollmentService;
import service.PaymentService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student {

    private int studentID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;

    private List<Enrollment> enrollments;
    private List<Payment> payments;

    public Student() {
        this.enrollments = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public Student(int studentID, String firstName, String lastName, Date dateOfBirth, String email, String phoneNumber) {
        this();
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void addEnrollment(Enrollment e) {
        if (e != null && !enrollments.contains(e)) {
            enrollments.add(e);
        }
    }

    public void addPayment(Payment p) {
        if (p != null && !payments.contains(p)) {
            payments.add(p);
        }
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void enrollInCourse(Course course) {
        new EnrollmentService().enrollExistingStudent(this, course);
    }

    public void updateStudentInfo(String firstName, String lastName, Date dateOfBirth, String email, String phoneNumber) throws InvalidStudentDataException {
        if (firstName == null || email == null) {
            throw new InvalidStudentDataException("Name and email must not be null.");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void makePayment(double amount, Date paymentDate) {
        new PaymentService().recordStudentPayment(this.studentID, amount, paymentDate);
    }

    public void displayStudentInfo() {
        System.out.println("Student ID: " + studentID);
        System.out.println("Name      : " + firstName + " " + lastName);
        System.out.println("DOB       : " + dateOfBirth);
        System.out.println("Email     : " + email);
        System.out.println("Phone     : " + phoneNumber);
    }

    public List<Course> getEnrolledCourses() {
        return new EnrollmentService().getCoursesForStudent(studentID);
    }

    public List<Payment> getPaymentHistory() {
        return new PaymentService().getPaymentsForStudent(studentID);
    }

    // Getters and Setters

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
