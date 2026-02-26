package repository.custom;

import model.GRN;
import model.GRNItem;
import model.Stock;
import model.tableModel.GrnTM;
import repository.SuperRepository;

import java.sql.Connection;
import java.util.List;

public interface GRNRepository extends SuperRepository {

    boolean save(GRN grn, Connection connection) throws Exception;

    long getNextGrnId() throws Exception;

    List<GrnTM> getAllGRNForView() throws Exception;
}
