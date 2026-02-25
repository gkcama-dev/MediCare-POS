package repository.custom.impl;

import database.DbConnection;
import model.Stock;
import repository.custom.StockRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StockRepositoryImpl implements StockRepository {

    @Override
    public int saveAndGetId(Stock stock, Connection connection) throws Exception {

        String query = "INSERT INTO stock (product_code, selling_price, qty, mfd, exp, user_id) VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        pstm.setString(1, stock.getProductCode());
        pstm.setDouble(2, stock.getSellingPrice());
        pstm.setDouble(3, stock.getQty());
        pstm.setObject(4,stock.getMfd());
        pstm.setObject(5,stock.getExp());
        pstm.setInt(6,1);

        if(pstm.executeUpdate()>0){
            ResultSet rs = pstm.getGeneratedKeys();
            if(rs.next()) return  rs.getInt(1); //new auto increament stock id
        }
        return -1;
    }

    @Override
    public int isExists(Stock stock, Connection connection) throws Exception {
        String query = "SELECT id FROM stock WHERE product_code=? AND selling_price=? AND mfd=? AND exp=? LIMIT 1";
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setString(1, stock.getProductCode());
        pstm.setDouble(2, stock.getSellingPrice());
        pstm.setObject(3, stock.getMfd());
        pstm.setObject(4, stock.getExp());

        ResultSet rs = pstm.executeQuery();
        return rs.next() ? rs.getInt(1) : -1;
    }

    @Override
    public boolean updateQty(int stockId, double qty, Connection connection) throws Exception {
        String query = "UPDATE stock SET qty = qty + ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(query);
        pstm.setDouble(1, qty);
        pstm.setInt(2, stockId);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public List<Stock> getAllStock() throws Exception {
        String query = "SELECT * FROM stock";

        PreparedStatement pstm = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(query);

        ResultSet rs = pstm.executeQuery();

        List<Stock> stockList = new ArrayList<>();

        while (rs.next()) {
            stockList.add(new Stock(
                    rs.getInt("id"),
                    rs.getString("product_code"),
                    rs.getDouble("selling_price"),
                    rs.getDouble("qty"),
                    rs.getDate("mfd").toLocalDate(),
                    rs.getDate("exp").toLocalDate(),
                    rs.getInt("user_id")
            ));
        }

        return stockList;
    }

    @Override
    public boolean reduceQty(int stockId, double qty, Connection connection) throws Exception {

        String sql = "UPDATE stock SET qty = qty - ? WHERE id = ? AND qty >= ?";

        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setDouble(1, qty);
        pstm.setInt(2, stockId);
        pstm.setDouble(3, qty);

        return pstm.executeUpdate() > 0;
    }
}
