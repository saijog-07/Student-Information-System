package dao;

import entity.model.Enrollment;
import exceptions.DuplicateEnrollmentException;
import exceptions.InvalidEnrollmentDataException;

import java.util.List;

public interface IEnrollmentDao {
    void insertEnrollment(Enrollment enrollment) throws DuplicateEnrollmentException, InvalidEnrollmentDataException;
    Enrollment getEnrollmentById(int id);
    List<Enrollment> getAllEnrollments();
    void updateEnrollment(Enrollment enrollment) throws InvalidEnrollmentDataException;
}
