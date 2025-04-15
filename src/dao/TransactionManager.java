package dao;

import entity.model.*;

import java.util.Date;
import java.util.Scanner;
import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class TransactionManager {

    private Connection getConnection() throws SQLException {
        String connStr = DBPropertyUtil.getConnectionString("db.properties");
        return DBConnUtil.getConnection(connStr);
    }

    public boolean enrollStudent(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (enrollment_id, student_id, course_id, enrollment_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, enrollment.getEnrollmentID());
                stmt.setInt(2, enrollment.getStudent().getStudentID());
                stmt.setInt(3, enrollment.getCourse().getCourseID());
                stmt.setDate(4, new java.sql.Date(enrollment.getEnrollmentDate().getTime()));

                stmt.executeUpdate();

                conn.commit();
                System.out.println("✅ Enrollment successful and committed.");
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("❌ Enrollment failed. Rolled back. " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("DB connection error: " + e.getMessage());
        }

        return false;
    }

    public boolean assignTeacherToCourse(Course course) {
        String sql = "UPDATE courses SET teacher_id = ? WHERE course_id = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, course.getInstructorName().getTeacherID());
                stmt.setInt(2, course.getCourseID());

                stmt.executeUpdate();

                conn.commit();
                System.out.println("✅ Teacher assigned and committed.");
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("❌ Teacher assignment failed. Rolled back. " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("DB connection error: " + e.getMessage());
        }

        return false;
    }

    public boolean recordPayment(Payment payment) {
        String sql = "INSERT INTO payments (payment_id, student_id, amount, payment_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, payment.getPaymentID());
                stmt.setInt(2, payment.getStudent().getStudentID());
                stmt.setDouble(3, payment.getAmount());
                stmt.setDate(4, new java.sql.Date(payment.getDate().getTime()));

                stmt.executeUpdate();

                conn.commit();
                System.out.println("✅ Payment recorded and committed.");
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("❌ Payment failed. Rolled back. " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("DB connection error: " + e.getMessage());
        }

        return false;
    }
    
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TransactionManager manager = new TransactionManager();

        // Student input
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        Student student = new Student();
        student.setStudentID(studentId);

        // Course input
        System.out.print("Enter Course ID: ");
        int courseId = scanner.nextInt();
        Course course = new Course();
        course.setCourseID(courseId);

        // Teacher input
        System.out.print("Enter Teacher ID to assign to Course: ");
        int teacherId = scanner.nextInt();
        Teacher teacher = new Teacher();
        teacher.setTeacherID(teacherId);
        course.setInstructorName(teacher);

        // Enrollment input
        System.out.print("Enter Enrollment ID: ");
        int enrollmentId = scanner.nextInt();
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentID(enrollmentId);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(new Date());

        System.out.println("\n🔄 Testing enrollStudent...");
        manager.enrollStudent(enrollment);

        // Assign teacher to course
        System.out.println("\n🔄 Testing assignTeacherToCourse...");
        manager.assignTeacherToCourse(course);

        // Payment input
        System.out.print("\nEnter Payment ID: ");
        int paymentId = scanner.nextInt();
        System.out.print("Enter Payment Amount: ");
        double amount = scanner.nextDouble();
        Payment payment = new Payment();
        payment.setPaymentID(paymentId);
        payment.setStudent(student);
        payment.setAmount(amount);
        payment.setDate(new Date());

        System.out.println("\n🔄 Testing recordPayment...");
        manager.recordPayment(payment);

        scanner.close();
    }
}
