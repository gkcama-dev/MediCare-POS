package service;

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
        }
        return null;
    }
}
