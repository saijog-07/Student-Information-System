package dao;

import entity.model.*;
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
    
}
