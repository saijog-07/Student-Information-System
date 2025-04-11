package service;

import dao.*;
import entity.model.Course;
import entity.model.Teacher;
import exceptions.CourseNotFoundException;

import java.util.List;

public class TeacherService {

    private final ITeacherDao teacherDao = new TeacherDaoImpl();
    private final ICourseDao courseDao = new CourseDaoImpl();

    public void assignTeacherToCourse(Teacher teacher, String courseCode) {
        teacherDao.insertTeacher(teacher);

        List<Course> courses = courseDao.getAllCourses();
        Course found = courses.stream()
                .filter(c -> c.getCourseCode().equalsIgnoreCase(courseCode))
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("Course with code " + courseCode + " not found."));

        found.setInstructorName(teacher);
        teacher.assignCourse(found); // maintain teacher-course relation

        courseDao.updateCourse(found);
        System.out.println("✅ Teacher assigned to course: " + found.getCourseName());
    }
}
