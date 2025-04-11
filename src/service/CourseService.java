package service;

import dao.CourseDaoImpl;
import dao.ICourseDao;
import entity.model.Course;

import java.util.List;

public class CourseService {

    private final ICourseDao courseDao = new CourseDaoImpl();

    public List<Course> getCoursesByTeacherID(int teacherId) {
        return courseDao.getAllCourses().stream()
                .filter(c -> c.getTeacher() != null && c.getTeacher().getTeacherID() == teacherId)
                .toList();
    }
}
