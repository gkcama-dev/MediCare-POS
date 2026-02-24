package repository.custom.impl;

import model.GRN;
import model.GRNItem;
import model.Stock;
import repository.custom.GRNRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class GRNRepositoryImpl implements GRNRepository {

    @Override
    public boolean save(GRN grn, Connection connection) throws Exception {

        String query = "INSERT INTO grn (id, date, total, supplier_id,paid_amount, balance) VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setLong(1, grn.getId());
        pstm.setObject(2, grn.getDate());
        pstm.setDouble(3, grn.getTotal());
        pstm.setString(4, grn.getSupplierId());
        pstm.setDouble(5, grn.getTotal());
        pstm.setDouble(6, 0.0);
        return pstm.executeUpdate() > 0;

    }

    @Override
    public long getNextGrnId() throws Exception {
        return 0;
    }
}
