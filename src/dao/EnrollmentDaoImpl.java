package dao;

import entity.model.Course;
import entity.model.Enrollment;
import entity.model.Student;
import entity.model.Teacher;
import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import exceptions.*;

public class EnrollmentDaoImpl implements IEnrollmentDao {

    private Connection getConnection() {
        String connStr = DBPropertyUtil.getConnectionString("db.properties");
        return DBConnUtil.getConnection(connStr);
    }

    @Override
    public void insertEnrollment(Enrollment enrollment) throws DuplicateEnrollmentException, InvalidEnrollmentDataException {
        if (enrollment.getStudent() == null || enrollment.getCourse() == null || enrollment.getEnrollmentDate() == null) {
            throw new InvalidEnrollmentDataException("Enrollment data is incomplete.");
        }

        // Check if student is already enrolled
        for (Enrollment existing : getAllEnrollments()) {
            if (existing.getStudent().getStudentID() == enrollment.getStudent().getStudentID() &&
                existing.getCourse().getCourseID() == enrollment.getCourse().getCourseID()) {
                throw new DuplicateEnrollmentException("Student already enrolled in this course.");
            }
        }

        String sql = "INSERT INTO enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, enrollment.getStudent().getStudentID());
            stmt.setInt(2, enrollment.getCourse().getCourseID());
            stmt.setDate(3, new java.sql.Date(enrollment.getEnrollmentDate().getTime()));

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                enrollment.setEnrollmentID(keys.getInt(1));
            }

            System.out.println("Enrollment inserted successfully.");
        } catch (SQLException e) {
            throw new InvalidEnrollmentDataException("Error inserting enrollment: " + e.getMessage());
        }
    }

    @Override
    public Enrollment getEnrollmentById(int id) {
        String sql = """
            SELECT e.*, 
                   s.first_name AS s_fname, s.last_name AS s_lname, s.date_of_birth, s.email AS s_email, s.phone_number,
                   c.course_id, c.course_name, c.course_code, c.credits,
                   t.teacher_id, t.first_name AS t_fname, t.last_name AS t_lname, t.email AS t_email
            FROM enrollments e
            JOIN students s ON e.student_id = s.student_id
            JOIN courses c ON e.course_id = c.course_id
            LEFT JOIN teachers t ON c.teacher_id = t.teacher_id
            WHERE e.enrollment_id = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("s_fname"),
                        rs.getString("s_lname"),
                        rs.getDate("date_of_birth"),
                        rs.getString("s_email"),
                        rs.getString("phone_number")
                );

                Teacher teacher = new Teacher(
                        rs.getInt("teacher_id"),
                        rs.getString("t_fname"),
                        rs.getString("t_lname"),
                        rs.getString("t_email")
                );

                Course course = new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("course_code"),
                        teacher
                );

                return new Enrollment(
                        rs.getInt("enrollment_id"),
                        student,
                        course,
                        rs.getDate("enrollment_date")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching enrollment: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();

        String sql = """
            SELECT e.*, 
                   s.first_name AS s_fname, s.last_name AS s_lname, s.date_of_birth, s.email AS s_email, s.phone_number,
                   c.course_id, c.course_name, c.course_code, c.credits,
                   t.teacher_id, t.first_name AS t_fname, t.last_name AS t_lname, t.email AS t_email
            FROM enrollments e
            JOIN students s ON e.student_id = s.student_id
            JOIN courses c ON e.course_id = c.course_id
            LEFT JOIN teachers t ON c.teacher_id = t.teacher_id
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("s_fname"),
                        rs.getString("s_lname"),
                        rs.getDate("date_of_birth"),
                        rs.getString("s_email"),
                        rs.getString("phone_number")
                );

                Teacher teacher = new Teacher(
                        rs.getInt("teacher_id"),
                        rs.getString("t_fname"),
                        rs.getString("t_lname"),
                        rs.getString("t_email")
                );

                Course course = new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("course_code"),
                        teacher
                );

                Enrollment enrollment = new Enrollment(
                        rs.getInt("enrollment_id"),
                        student,
                        course,
                        rs.getDate("enrollment_date")
                );

                enrollments.add(enrollment);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving enrollments: " + e.getMessage());
        }

        return enrollments;
    }

    @Override
    public void updateEnrollment(Enrollment enrollment) throws InvalidEnrollmentDataException {
        if (enrollment.getStudent() == null || enrollment.getCourse() == null || enrollment.getEnrollmentDate() == null) {
            throw new InvalidEnrollmentDataException("Enrollment data is incomplete.");
        }

        String sql = "UPDATE enrollments SET student_id = ?, course_id = ?, enrollment_date = ? WHERE enrollment_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enrollment.getStudent().getStudentID());
            stmt.setInt(2, enrollment.getCourse().getCourseID());
            stmt.setDate(3, new java.sql.Date(enrollment.getEnrollmentDate().getTime()));
            stmt.setInt(4, enrollment.getEnrollmentID());

            stmt.executeUpdate();
            System.out.println("Enrollment updated successfully.");
        } catch (SQLException e) {
            throw new InvalidEnrollmentDataException("Error updating enrollment: " + e.getMessage());
        }
    }
}
