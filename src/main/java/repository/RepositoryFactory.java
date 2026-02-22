package repository;

import repository.custom.impl.SupplierRepositoryImpl;
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
        }
        return null;
    }
}
