package dao;

import entity.model.Student;
import exceptions.StudentNotFoundException;
import exceptions.InvalidStudentDataException;

import java.util.List;

public interface IStudentDao {
    void insertStudent(Student student) throws InvalidStudentDataException;
    Student getStudentById(int id) throws StudentNotFoundException;
    List<Student> getAllStudents();
    void updateStudent(Student student) throws StudentNotFoundException, InvalidStudentDataException;
}
