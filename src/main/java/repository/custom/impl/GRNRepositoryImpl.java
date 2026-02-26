package repository.custom.impl;

import model.GRN;
import model.GRNItem;
import model.Stock;
import model.tableModel.GrnTM;
import repository.custom.GRNRepository;
import util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

    @Override
    public List<GrnTM> getAllGRNForView() throws Exception {
        String sql =
                "SELECT g.id, g.date, g.total, g.paid_amount, g.balance, " +
                        "CONCAT(s.first_name, ' ', s.last_name) AS supplier, SUM(gi.qty) AS qty " +
                        "FROM grn g " +
                        "JOIN supplier s ON g.supplier_id = s.id " +
                        "JOIN grn_item gi ON g.id = gi.grn_id " +
                        "GROUP BY g.id, g.date, g.total, g.paid_amount, g.balance, s.first_name, s.last_name";

        ResultSet rs = CrudUtil.execute(sql);

        List<GrnTM> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new GrnTM(
                    rs.getLong("id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getDouble("total"),
                    rs.getDouble("paid_amount"),
                    rs.getDouble("balance"),
                    rs.getString("supplier"),
                    rs.getDouble("qty")
            ));
        }

        return list;
    }
}
