package service.custom.impl;

import model.Dashboard;
import repository.RepositoryFactory;
import repository.custom.DashboardRepository;
import service.custom.DashboardService;
import util.RepositoryType;

import java.util.List;

public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepo = RepositoryFactory.getInstance().getRepository(RepositoryType.DASHBOARD);

    @Override
    public Dashboard getDashboardStats() throws Exception {
        return new Dashboard(
                dashboardRepo.getTodayTotalEarning(),
                dashboardRepo.getTodaySalesCount(),
                dashboardRepo.getLowStockCount(),
                dashboardRepo.getExpiringSoonCount()
        );
    }

    @Override
    public List<String> getLowStockAlerts() throws Exception {
        return dashboardRepo.getLowStockDetails();
    }

    @Override
    public List<String> getExpiringSoonAlerts() throws Exception {
        return dashboardRepo.getExpiringSoonDetails();
    }
}
