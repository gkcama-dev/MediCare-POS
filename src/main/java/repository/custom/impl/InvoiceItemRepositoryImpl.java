package repository.custom.impl;

import model.InvoiceItem;
import repository.custom.InvoiceItemRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InvoiceItemRepositoryImpl implements InvoiceItemRepository {
    @Override
    public boolean save(InvoiceItem item, Connection connection) throws Exception {
        String sql = "INSERT INTO invoice_item " +
                "(invoice_id, stock_id, qty, unit_price) VALUES (?,?,?,?)";

        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setLong(1, item.getInvoiceId());
        pstm.setInt(2, item.getStockId());
        pstm.setDouble(3, item.getQty());
        pstm.setDouble(4, item.getUnitPrice());

        return pstm.executeUpdate() > 0;
    }
}
