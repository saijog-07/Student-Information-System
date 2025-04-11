package dao;

import entity.model.Course;
import exceptions.CourseNotFoundException;
import exceptions.InvalidCourseDataException;

import java.util.List;

public interface ICourseDao {
    void insertCourse(Course course) throws InvalidCourseDataException;
    Course getCourseById(int id) throws CourseNotFoundException;
    List<Course> getAllCourses();
    void updateCourse(Course course) throws CourseNotFoundException, InvalidCourseDataException;
}
