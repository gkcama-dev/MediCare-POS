package service;

import service.custom.impl.BrandServiceImpl;
import service.custom.impl.CategoryServiceImpl;
import service.custom.impl.ProductServiceImpl;
import service.custom.impl.SupplierServiceImpl;
import util.ServiceType;

public class ServiceFactory {

    private static ServiceFactory instance;

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        return instance==null?instance=new ServiceFactory():instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType serviceType) {
        switch (serviceType) {
            case SUPPLIER:return (T) new SupplierServiceImpl();
            case PRODUCT:return (T) new ProductServiceImpl();
            case BRAND:return (T) new BrandServiceImpl();
            case CATEGORY:return (T) new CategoryServiceImpl();
        }
        return null;
    }
}
