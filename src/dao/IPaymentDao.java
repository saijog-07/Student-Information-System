package dao;

import entity.model.Payment;
import exceptions.PaymentValidationException;

import java.util.List;

public interface IPaymentDao {
    void insertPayment(Payment payment) throws PaymentValidationException;
    Payment getPaymentById(int id);
    List<Payment> getAllPayments();
    void updatePayment(Payment payment) throws PaymentValidationException;
}
