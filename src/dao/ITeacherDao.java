package dao;

import entity.model.Teacher;
import exceptions.InvalidTeacherDataException;
import exceptions.TeacherNotFoundException;

import java.util.List;

public interface ITeacherDao {
    void insertTeacher(Teacher teacher) throws InvalidTeacherDataException;
    Teacher getTeacherById(int id) throws TeacherNotFoundException;
    List<Teacher> getAllTeachers();
    void updateTeacher(Teacher teacher) throws InvalidTeacherDataException, TeacherNotFoundException;
}
