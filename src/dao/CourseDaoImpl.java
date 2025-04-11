package dao;

import entity.model.Course;
import entity.model.Teacher;
import util.DBConnUtil;
import util.DBPropertyUtil;
import exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements ICourseDao {

    private Connection getConnection() {
        String connStr = DBPropertyUtil.getConnectionString("db.properties");
        return DBConnUtil.getConnection(connStr);
    }

    @Override
    public void insertCourse(Course course) throws InvalidCourseDataException {
        if (course.getCourseName() == null || course.getCourseName().isBlank()
                || course.getCourseCode() == null || course.getCourseCode().isBlank()
                || course.getCredits() <= 0) {
            throw new InvalidCourseDataException("Course data is incomplete or invalid.");
        }

        String sql = "INSERT INTO courses (course_name, course_code, credits, teacher_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getCourseCode());
            stmt.setInt(3, course.getCredits());

            if (course.getInstructorName() != null) {
                stmt.setInt(4, course.getInstructorName().getTeacherID());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                course.setCourseID(keys.getInt(1));
            }

            if (course.getInstructorName() != null) {
                course.getInstructorName().assignCourse(course);
            }

            System.out.println("Course inserted successfully.");
        } catch (SQLException e) {
            throw new InvalidCourseDataException("❌ Error inserting course: " + e.getMessage());
        }
    }

    @Override
    public Course getCourseById(int id) throws CourseNotFoundException {
        String sql = "SELECT * FROM courses c LEFT JOIN teachers t ON c.teacher_id = t.teacher_id WHERE c.course_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Teacher teacher = null;
                if (rs.getInt("teacher_id") != 0) {
                    teacher = new Teacher(
                            rs.getInt("teacher_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email")
                    );
                }

                Course course = new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("course_code"),
                        teacher
                );

                if (teacher != null) {
                    teacher.assignCourse(course);
                }

                return course;
            } else {
                throw new CourseNotFoundException("Course with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new CourseNotFoundException("Database error while retrieving course: " + e.getMessage());
        }
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses c LEFT JOIN teachers t ON c.teacher_id = t.teacher_id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Teacher teacher = null;
                if (rs.getInt("teacher_id") != 0) {
                    teacher = new Teacher(
                            rs.getInt("teacher_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email")
                    );
                }

                Course course = new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("course_code"),
                        teacher
                );

                if (teacher != null) {
                    teacher.assignCourse(course);
                }

                courses.add(course);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
        }

        return courses;
    }

    @Override
    public void updateCourse(Course course) throws CourseNotFoundException, InvalidCourseDataException {
        if (course.getCourseName() == null || course.getCourseCode() == null || course.getCredits() <= 0) {
            throw new InvalidCourseDataException("Course data is incomplete or invalid.");
        }

        String sql = "UPDATE courses SET course_name = ?, course_code = ?, credits = ?, teacher_id = ? WHERE course_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getCourseCode());
            stmt.setInt(3, course.getCredits());

            if (course.getInstructorName() != null) {
                stmt.setInt(4, course.getInstructorName().getTeacherID());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setInt(5, course.getCourseID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new CourseNotFoundException("Course with ID " + course.getCourseID() + " not found.");
            }

            if (course.getInstructorName() != null) {
                course.getInstructorName().assignCourse(course);
            }

            System.out.println("Course updated successfully.");
        } catch (SQLException e) {
            throw new InvalidCourseDataException("❌ Error updating course: " + e.getMessage());
        }
    }
}
