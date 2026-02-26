package repository.custom.impl;

import model.Invoice;
import model.tableModel.InvoiceViewTM;
import repository.custom.InvoiceRepository;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<InvoiceViewTM> getAllInvoiceForView() throws Exception {
        String sql =
                "SELECT i.id, i.date, i.discount, i.total, " +
                        "i.customer_mobile, " +
                        "pm.name AS payment_type, " +
                        "u.username AS user " +
                        "FROM invoice i " +
                        "JOIN payment_method pm ON i.payment_method_id = pm.id " +
                        "JOIN user u ON i.user_id = u.id";

        ResultSet rs = CrudUtil.execute(sql);

        List<InvoiceViewTM> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new InvoiceViewTM(
                    rs.getLong("id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("customer_mobile"),
                    rs.getDouble("discount"),
                    rs.getString("payment_type"),
                    rs.getDouble("total"),
                    rs.getString("user")
            ));
        }

        return list;
    }
}
