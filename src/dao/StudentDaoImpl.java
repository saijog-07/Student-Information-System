package dao;

import entity.model.Student;
import exceptions.InvalidStudentDataException;
import exceptions.StudentNotFoundException;
import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements IStudentDao {

    private Connection getConnection() {
        String connStr = DBPropertyUtil.getConnectionString("db.properties");
        return DBConnUtil.getConnection(connStr);
    }

    @Override
    public void insertStudent(Student student) throws InvalidStudentDataException {
        if (student.getFirstName() == null || student.getEmail() == null || student.getDateOfBirth() == null) {
            throw new InvalidStudentDataException("Student data is incomplete.");
        }

        String sql = "INSERT INTO students (first_name, last_name, date_of_birth, email, phone_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, new java.sql.Date(student.getDateOfBirth().getTime()));
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhoneNumber());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                student.setStudentID(rs.getInt(1));
            }

            System.out.println("Student inserted successfully.");
        } catch (SQLException e) {
            throw new InvalidStudentDataException("❌ Error inserting student: " + e.getMessage());
        }
    }

    @Override
    public Student getStudentById(int id) throws StudentNotFoundException {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );
            } else {
                throw new StudentNotFoundException("Student with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new StudentNotFoundException("Error fetching student: " + e.getMessage());
        }
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }

        return students;
    }

    @Override
    public void updateStudent(Student student) throws StudentNotFoundException, InvalidStudentDataException {
        if (student.getStudentID() <= 0 || student.getEmail() == null) {
            throw new InvalidStudentDataException("Invalid or missing student data.");
        }

        String sql = "UPDATE students SET first_name=?, last_name=?, date_of_birth=?, email=?, phone_number=? WHERE student_id=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, new java.sql.Date(student.getDateOfBirth().getTime()));
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhoneNumber());
            stmt.setInt(6, student.getStudentID());

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new StudentNotFoundException("Student not found for update.");
            System.out.println("Student updated successfully.");
        } catch (SQLException e) {
            throw new InvalidStudentDataException("❌ Error updating student: " + e.getMessage());
        }
    }
}






