package repository.custom.impl;

import repository.custom.DashboardRepository;
import util.CrudUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DashboardRepositoryImpl implements DashboardRepository {
    @Override
    public double getTodayTotalEarning() throws Exception {
        String sql = "SELECT SUM(total) FROM invoice WHERE date = CURDATE()";
        ResultSet rst = CrudUtil.execute(sql);
        return rst.next() ? rst.getDouble(1) : 0;
    }

    @Override
    public int getTodaySalesCount() throws Exception {
        String sql = "SELECT SUM(total) FROM invoice WHERE date = CURDATE()";
        ResultSet rst = CrudUtil.execute(sql);
        return rst.next() ? rst.getInt(1) : 0;
    }

    @Override
    public int getLowStockCount() throws Exception {
        String sql = "SELECT COUNT(id) FROM stock WHERE qty < 25";
        ResultSet rst = CrudUtil.execute(sql);
        return rst.next() ? rst.getInt(1) : 0;
    }

    @Override
    public int getExpiringSoonCount() throws Exception {
        String sql = "SELECT COUNT(id) FROM stock WHERE exp <= DATE_ADD(CURDATE(), INTERVAL 1 MONTH) AND exp >= CURDATE()";
        ResultSet rst = CrudUtil.execute(sql);
        return rst.next() ? rst.getInt(1) : 0;
    }

    @Override
    public List<String> getLowStockDetails() throws Exception {
        String sql = "SELECT p.name, s.qty FROM stock s " +
                "JOIN product p ON s.product_code = p.code " +
                "WHERE s.qty < 25 AND p.name IS NOT NULL AND p.name != ''";

        ResultSet rst = CrudUtil.execute(sql);
        List<String> list = new ArrayList<>();
        while (rst.next()) {
            list.add(rst.getString(1) + " - " + rst.getDouble(2) + " units left");
        }
        return list;
    }

    @Override
    public List<String> getExpiringSoonDetails() throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT p.name, s.exp FROM stock s JOIN product p ON s.product_code = p.code WHERE s.exp <= DATE_ADD(CURDATE(), INTERVAL 1 MONTH) AND s.exp >= CURDATE()");
        List<String> list = new ArrayList<>();
        while (rst.next()) {
            list.add(rst.getString(1) + " - Expiring on " + rst.getDate(2));
        }
        return list;
    }
}
