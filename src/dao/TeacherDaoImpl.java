package dao;

import entity.model.Teacher;
import exceptions.InvalidTeacherDataException;
import exceptions.TeacherNotFoundException;
import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDaoImpl implements ITeacherDao {

    private Connection getConnection() {
        String connStr = DBPropertyUtil.getConnectionString("db.properties");
        return DBConnUtil.getConnection(connStr);
    }

    @Override
    public void insertTeacher(Teacher teacher) throws InvalidTeacherDataException {
        if (teacher.getFirstName() == null || teacher.getEmail() == null) {
            throw new InvalidTeacherDataException("Teacher data is incomplete.");
        }

        String sql = "INSERT INTO teachers (first_name, last_name, email) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getEmail());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                teacher.setTeacherID(rs.getInt(1));
            }

            System.out.println("Teacher inserted successfully.");
        } catch (SQLException e) {
            throw new InvalidTeacherDataException("❌ Error inserting teacher: " + e.getMessage());
        }
    }

    @Override
    public Teacher getTeacherById(int id) throws TeacherNotFoundException {
        String sql = "SELECT * FROM teachers WHERE teacher_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Teacher(
                        rs.getInt("teacher_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                );
            } else {
                throw new TeacherNotFoundException("Teacher with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new TeacherNotFoundException("❌ Error fetching teacher: " + e.getMessage());
        }
    }

    @Override
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                teachers.add(new Teacher(
                        rs.getInt("teacher_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving teachers: " + e.getMessage());
        }

        return teachers;
    }

    @Override
    public void updateTeacher(Teacher teacher) throws InvalidTeacherDataException, TeacherNotFoundException {
        if (teacher.getTeacherID() <= 0 || teacher.getEmail() == null) {
            throw new InvalidTeacherDataException("Teacher data is invalid or incomplete.");
        }

        String sql = "UPDATE teachers SET first_name=?, last_name=?, email=? WHERE teacher_id=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getEmail());
            stmt.setInt(4, teacher.getTeacherID());

            int rows = stmt.executeUpdate();
            if (rows == 0) throw new TeacherNotFoundException("Teacher not found for update.");
            System.out.println("Teacher updated successfully.");
        } catch (SQLException e) {
            throw new InvalidTeacherDataException("❌ Error updating teacher: " + e.getMessage());
        }
    }
}
