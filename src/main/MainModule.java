package main;

import dao.CourseDaoImpl;
import dao.StudentDaoImpl;
import dao.TeacherDaoImpl;
import entity.model.*;
import service.EnrollmentService;
import service.PaymentService;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentDaoImpl studentDao = new StudentDaoImpl();
        TeacherDaoImpl teacherDao = new TeacherDaoImpl();
        CourseDaoImpl courseDao = new CourseDaoImpl();
        EnrollmentService enrollmentService = new EnrollmentService();
        PaymentService paymentService = new PaymentService();

        System.out.println("Welcome to Student Information System");
        int choice;

        do {
            System.out.println("\n========= MENU =========");
            System.out.println("1. Register Student");
            System.out.println("2. Register Teacher");
            System.out.println("3. Create Course & Assign Teacher");
            System.out.println("4. Enroll Student in a Course");
            System.out.println("5. Make Payment");
            System.out.println("6. View Student Info");
            System.out.println("7. View Enrolled Courses");
            System.out.println("8. View Payment History");
            System.out.println("9. View Teacher Info");
            System.out.println("10. Enrollment Report for Course");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.println("\nRegister Student:");
                    System.out.print("First Name: ");
                    String fname = sc.nextLine();
                    System.out.print("Last Name: ");
                    String lname = sc.nextLine();
                    System.out.print("DOB (yyyy-mm-dd): ");
                    Date dob = Date.valueOf(sc.nextLine());
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Phone: ");
                    String phone = sc.nextLine();

                    Student s = new Student();
                    s.updateStudentInfo(fname, lname, dob, email, phone);
                    studentDao.insertStudent(s);
                }

                case 2 -> {
                    System.out.println("\nRegister Teacher:");
                    System.out.print("Full Name: ");
                    String fullName = sc.nextLine();
                    String[] names = fullName.split(" ");
                    String tFname = names[0];
                    String tLname = names.length > 1 ? names[1] : "";
                    System.out.print("Email: ");
                    String tEmail = sc.nextLine();
                    

                    Teacher t = new Teacher();
                    t.updateTeacherInfo(tFname + " " + tLname, tEmail, tEmail);
                    teacherDao.insertTeacher(t);
                }

                case 3 -> {
                    System.out.println("\nCreate Course:");
                    System.out.print("Course Name: ");
                    String courseName = sc.nextLine();
                    System.out.print("Course Code: ");
                    String courseCode = sc.nextLine();
                    System.out.print("Credits: ");
                    int credits = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Assign Existing Teacher ID: ");
                    int teacherId = sc.nextInt();
                    sc.nextLine();

                    Teacher t = teacherDao.getTeacherById(teacherId);
                    if (t == null) {
                        System.out.println("Teacher not found.");
                        break;
                    }

                    Course c = new Course();
                    c.setCourseName(courseName);
                    c.setCourseCode(courseCode);
                    c.setCredits(credits);
                    c.setInstructorName(t);
                    courseDao.insertCourse(c);
                }

                case 4 -> {
                    System.out.println("\nEnrolll Student in a Course");
                    System.out.print("Student ID: ");
                    int studentId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Course Name: ");
                    String cname = sc.nextLine();

                    Student st = studentDao.getStudentById(studentId);
                    if (st == null) {
                        System.out.println("Student not found.");
                        break;
                    }

                    List<Course> allCourses = courseDao.getAllCourses();
                    for (Course course : allCourses) {
                        if (course.getCourseName().equalsIgnoreCase(cname)) {
                            st.enrollInCourse(course);
                            break;
                        }
                    }
                }

                case 5 -> {
                    System.out.println("\nMake a Payment:");
                    System.out.print("Student ID: ");
                    int sid = sc.nextInt();
                    System.out.print("Amount: ");
                    double amt = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Payment Date (yyyy-mm-dd): ");
                    Date payDate = Date.valueOf(sc.nextLine());

                    Student stud = studentDao.getStudentById(sid);
                    if (stud == null) {
                        System.out.println("Student not found.");
                        break;
                    }
                    stud.makePayment(amt, payDate);
                }

                case 6 -> {
                    System.out.print("\nEnter Student ID to view info: ");
                    int sid = sc.nextInt();
                    Student s = studentDao.getStudentById(sid);
                    if (s != null) s.displayStudentInfo();
                    else System.out.println("Student not found.");
                }

                case 7 -> {
                    System.out.print("\nEnter Student ID: ");
                    int sid = sc.nextInt();
                    Student s = studentDao.getStudentById(sid);
                    if (s != null) {
                        List<Course> enrolled = s.getEnrolledCourses();
                        for (Course c : enrolled) {
                            c.displayCourseInfo();
                            System.out.println("-------------------");
                        }
                    } else System.out.println("Student not found.");
                }

                case 8 -> {
                    System.out.print("\nEnter Student ID: ");
                    int sid = sc.nextInt();
                    Student s = studentDao.getStudentById(sid);
                    if (s != null) {
                        List<Payment> p = s.getPaymentHistory();
                        for (Payment pay : p) {
                            System.out.println("Paid ₹" + pay.getPaymentAmount() + " on " + pay.getPaymentDate());
                        }
                    } else System.out.println("Student not found.");
                }

                case 9 -> {
                    System.out.print("\nEnter Teacher ID: ");
                    int tid = sc.nextInt();
                    Teacher t = teacherDao.getTeacherById(tid);
                    if (t != null) t.displayTeacherInfo();
                    else System.out.println("Teacher not found.");
                }

                case 10 -> {
                    System.out.print("\nEnter Course Name for Report: ");
                    String cname = sc.nextLine();
                    enrollmentService.generateEnrollmentReport(cname);
                }

                case 0 -> System.out.println("\nExiting...");

                default -> System.out.println("Invalid option. Try again.");
            }

        } while (choice != 0);

        sc.close();
    }
}
