package service.custom;

import model.GRN;
import model.GRNItem;
import model.Stock;
import model.tableModel.GrnTM;
import service.SuperService;

import java.util.List;

public interface GRNService extends SuperService {

    boolean placeGRN(GRN grn) throws Exception;

    void validateActiveStatus(GRN grn) throws Exception;

    List<GrnTM> getAllGRNForView() throws Exception;

}
