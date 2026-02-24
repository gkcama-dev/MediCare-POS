package repository.custom.impl;

import model.GRNItem;
import repository.custom.GRNItemRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class GRNItemRepositoryImpl implements GRNItemRepository {

    @Override
    public boolean save(GRNItem item, Connection connection) throws Exception {

        String query = "INSERT INTO grn_item (qty, buying_price, stock_id, grn_id) VALUES (?,?,?,?)";

        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setDouble(1, item.getQty());
        pstm.setDouble(2, item.getBuyingPrice());
        pstm.setInt(3, item.getStockId());
        pstm.setLong(4, item.getGrnId());
        return pstm.executeUpdate() > 0;
    }
}
