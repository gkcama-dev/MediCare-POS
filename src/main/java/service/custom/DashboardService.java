package service.custom;

import model.Dashboard;
import service.SuperService;

import java.util.List;

public interface DashboardService extends SuperService {

    Dashboard getDashboardStats() throws Exception;

    List<String> getLowStockAlerts() throws Exception;

    List<String> getExpiringSoonAlerts() throws Exception;
}
