package service;

import dao.*;
import entity.model.Course;
import entity.model.Enrollment;
import entity.model.Student;
import exceptions.CourseNotFoundException;
import exceptions.DuplicateEnrollmentException;

import java.util.Date;
import java.util.List;

public class EnrollmentService {

    private final IStudentDao studentDao = new StudentDaoImpl();
    private final ICourseDao courseDao = new CourseDaoImpl();
    private final IEnrollmentDao enrollmentDao = new EnrollmentDaoImpl();

    public void enrollNewStudent(Student student, List<String> courseNames) {
        studentDao.insertStudent(student);
        List<Course> allCourses = courseDao.getAllCourses();

        for (String courseName : courseNames) {
            boolean found = false;
            for (Course c : allCourses) {
                if (c.getCourseName().equalsIgnoreCase(courseName)) {
                    found = true;
                    enrollExistingStudent(student, c);
                    break;
                }
            }
            if (!found) throw new CourseNotFoundException("Course not found: " + courseName);
        }
    }

    public void enrollExistingStudent(Student student, Course course) {
        // Check if already enrolled
        List<Enrollment> existing = enrollmentDao.getAllEnrollments();
        for (Enrollment e : existing) {
            if (e.getStudent().getStudentID() == student.getStudentID()
                    && e.getCourse().getCourseID() == course.getCourseID()) {
                throw new DuplicateEnrollmentException("Student is already enrolled in " + course.getCourseName());
            }
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(new Date());
        enrollmentDao.insertEnrollment(enrollment);

        System.out.println("✅ Student " + student.getFirstName() + " enrolled in " + course.getCourseName());
    }

    public void generateEnrollmentReport(String courseName) {
        List<Course> allCourses = courseDao.getAllCourses();
        Course course = allCourses.stream()
                .filter(c -> c.getCourseName().equalsIgnoreCase(courseName))
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("❌ Course not found: " + courseName));

        List<Enrollment> enrollments = enrollmentDao.getAllEnrollments();
        System.out.println("\n📋 Enrollment Report for: " + courseName);

        enrollments.stream()
                .filter(e -> e.getCourse().getCourseID() == course.getCourseID())
                .map(Enrollment::getStudent)
                .forEach(s -> System.out.println("Student: " + s.getFirstName() + " " + s.getLastName() + " | Email: " + s.getEmail()));
    }

    public List<Course> getCoursesForStudent(int studentId) {
        return enrollmentDao.getAllEnrollments().stream()
                .filter(e -> e.getStudent().getStudentID() == studentId)
                .map(Enrollment::getCourse)
                .toList();
    }

    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        return enrollmentDao.getAllEnrollments().stream()
                .filter(e -> e.getCourse().getCourseID() == courseId)
                .toList();
    }
}
