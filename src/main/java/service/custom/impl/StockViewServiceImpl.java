package service.custom.impl;

import model.Stock;
import repository.RepositoryFactory;
import repository.custom.StockRepository;
import service.custom.StockViewService;
import util.RepositoryType;

import java.util.List;

public class StockViewServiceImpl implements StockViewService {

    private final StockRepository stockRepository =
            RepositoryFactory.getInstance().getRepository(RepositoryType.STOCK);

    @Override
    public List<Stock> getAllStock() throws Exception {
        return stockRepository.getAllStock();
    }


}
