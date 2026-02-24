package repository;

import repository.custom.impl.*;
import service.custom.impl.GRNServiceImpl;
import util.RepositoryType;

public class RepositoryFactory {

    private static RepositoryFactory instance;

    private RepositoryFactory() {}

    public static RepositoryFactory getInstance(){
        return instance==null?instance=new RepositoryFactory():instance;
    }

    public <T extends SuperRepository>T getRepository(RepositoryType repositoryType){
        switch (repositoryType){
            case SUPPLIER:return (T) new SupplierRepositoryImpl();
            case PRODUCT:return (T) new ProductRepositoryImpl();
            case BRAND:return (T) new BrandRepositoryImpl();
            case CATEGORY:return (T) new CategoryRepositoryImpl();
            case GRN:return (T) new GRNRepositoryImpl();
            case GRN_ITEM:return (T) new GRNItemRepositoryImpl();
            case STOCK:return (T) new StockRepositoryImpl();
        }
        return null;
    }
}
