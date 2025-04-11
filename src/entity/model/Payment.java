package entity.model;

import java.util.Date;
import exceptions.PaymentValidationException;

public class Payment {

    private int paymentID;
    private Student student;
    private double amount;
    private Date date;

    public Payment() {
        super();
    }

    public Payment(int paymentID, Student student, double amount, java.util.Date date) {
        this.paymentID = paymentID;
        this.student = student;
        this.amount = amount;
        this.date = date;
    }

    public Student getStudent() {
        return student;
    }

    public double getPaymentAmount() {
        return amount;
    }

    public Date getPaymentDate() {
        return date;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws PaymentValidationException {
        if (amount <= 0) throw new PaymentValidationException("Amount must be greater than 0.");
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}