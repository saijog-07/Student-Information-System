package service;

import dao.*;
import entity.model.Payment;
import entity.model.Student;
import exceptions.PaymentValidationException;
import exceptions.StudentNotFoundException;

import java.util.Date;
import java.util.List;

public class PaymentService {

    private final IPaymentDao paymentDao = new PaymentDaoImpl();
    private final IStudentDao studentDao = new StudentDaoImpl();

    public void recordStudentPayment(int studentId, double amount, Date date) {
        Student student = studentDao.getStudentById(studentId);

        if (student == null) {
            throw new StudentNotFoundException("Student ID " + studentId + " not found.");
        }

        if (amount <= 0) {
            throw new PaymentValidationException("Amount must be greater than 0");
        }

        if (date == null) {
            throw new PaymentValidationException("Invalid payment date");
        }

        Payment payment = new Payment();
        payment.setStudent(student); // this will also add payment to student list
        payment.setAmount(amount);
        payment.setDate(date);
        paymentDao.insertPayment(payment);

        System.out.println("✅ Payment recorded for " + student.getFirstName());
    }

    public List<Payment> getPaymentsForStudent(int studentId) {
        return paymentDao.getAllPayments().stream()
                .filter(p -> p.getStudent().getStudentID() == studentId)
                .toList();
    }
}
