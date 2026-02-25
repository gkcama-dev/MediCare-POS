package repository.custom.impl;

import model.Invoice;
import repository.custom.InvoiceRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    @Override
    public boolean save(Invoice invoice, Connection connection) throws Exception {
        String sql = "INSERT INTO invoice " +
                "(id, date, customer_mobile, paid_amount, discount, balance, total, user_id, payment_method_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setLong(1, invoice.getId());
        pstm.setObject(2, invoice.getDate());
        pstm.setString(3, invoice.getCustomerMobile());
        pstm.setDouble(4, invoice.getPaidAmount());
        pstm.setDouble(5, invoice.getDiscount());
        pstm.setDouble(6, invoice.getBalance());
        pstm.setDouble(7, invoice.getTotal());
        pstm.setInt(8, invoice.getUserId());
        pstm.setInt(9, invoice.getPaymentMethodId());

        return pstm.executeUpdate() > 0;
    }

    @Override
    public long getNextInvoiceId() throws Exception {
        return 0;
    }
}
