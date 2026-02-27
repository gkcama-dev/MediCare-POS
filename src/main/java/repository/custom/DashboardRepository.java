package repository.custom;

import repository.SuperRepository;

import java.util.List;

public interface DashboardRepository extends SuperRepository {

    double getTodayTotalEarning() throws Exception;

    int getTodaySalesCount() throws Exception;

    int getLowStockCount() throws Exception;

    int getExpiringSoonCount() throws Exception;

    List<String> getLowStockDetails() throws Exception;

    List<String> getExpiringSoonDetails() throws Exception;
}
