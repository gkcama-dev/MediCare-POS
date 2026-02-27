package service;

import repository.custom.impl.GRNItemRepositoryImpl;
import repository.custom.impl.StockRepositoryImpl;
import service.custom.impl.*;
import util.RepositoryType;
import util.ServiceType;

public class ServiceFactory {

    private static ServiceFactory instance;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance == null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType serviceType) {
        switch (serviceType) {
            case SUPPLIER:
                return (T) new SupplierServiceImpl();
            case PRODUCT:
                return (T) new ProductServiceImpl();
            case BRAND:
                return (T) new BrandServiceImpl();
            case CATEGORY:
                return (T) new CategoryServiceImpl();
            case GRN:
                return (T) new GRNServiceImpl();
            case USER:
                return (T) new UserServiceImpl();
            case INVOICE:
                return (T) new InvoiceServiceImpl();
            case STOCKVIEW:
                return (T) new StockViewServiceImpl();
            case DASHBOARD:
                return (T) new DashboardServiceImpl();
        }
        return null;
    }
}
