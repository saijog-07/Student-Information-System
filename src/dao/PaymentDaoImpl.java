package dao;

import entity.model.Payment;
import entity.model.Student;
import exceptions.PaymentValidationException;
import util.DBConnUtil;
import util.DBPropertyUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDaoImpl implements IPaymentDao {

    private Connection getConnection() {
        String connStr = DBPropertyUtil.getConnectionString("db.properties");
        return DBConnUtil.getConnection(connStr);
    }

    @Override
    public void insertPayment(Payment payment) throws PaymentValidationException {
        if (payment.getStudent() == null || payment.getAmount() <= 0 || payment.getDate() == null) {
        	if(payment.getStudent() == null) {
            	System.out.println("1");
            }
            if(payment.getAmount() <= 0) {
            	System.out.println("2");
            }
            if(payment.getDate() == null) {
            	System.out.println("3");
            }
        	
        	throw new PaymentValidationException("Invalid payment data.");
        }

        String sql = "INSERT INTO payments (student_id, amount, payment_date) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getStudent().getStudentID());
            stmt.setDouble(2, payment.getAmount());
            stmt.setDate(3, new java.sql.Date(payment.getDate().getTime()));

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                payment.setPaymentID(rs.getInt(1));
            }

            System.out.println("Payment inserted successfully.");
        } catch (SQLException e) {
            throw new PaymentValidationException("❌ Error inserting payment: " + e.getMessage());
        }
    }

    @Override
    public Payment getPaymentById(int id) {
        String sql = "SELECT * FROM payments p JOIN students s ON p.student_id = s.student_id WHERE p.payment_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );

                return new Payment(
                        rs.getInt("payment_id"),
                        student,
                        rs.getDouble("amount"),
                        rs.getDate("payment_date")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching payment: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments p JOIN students s ON p.student_id = s.student_id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );

                Payment payment = new Payment(
                        rs.getInt("payment_id"),
                        student,
                        rs.getDouble("amount"),
                        rs.getDate("payment_date")
                );

                payments.add(payment);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving payments: " + e.getMessage());
        }

        return payments;
    }

    @Override
    public void updatePayment(Payment payment) throws PaymentValidationException {
        if (payment.getStudent() == null || payment.getAmount() <= 0 || payment.getDate() == null) {
        	throw new PaymentValidationException("Invalid payment data.");
        }

        String sql = "UPDATE payments SET student_id = ?, amount = ?, payment_date = ? WHERE payment_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getStudent().getStudentID());
            stmt.setDouble(2, payment.getAmount());
            stmt.setDate(3, new java.sql.Date(payment.getDate().getTime()));
            stmt.setInt(4, payment.getPaymentID());

            stmt.executeUpdate();
            System.out.println("Payment updated successfully.");
        } catch (SQLException e) {
            throw new PaymentValidationException("❌ Error updating payment: " + e.getMessage());
        }
    }
}
