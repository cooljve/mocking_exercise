package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

  private static final String SALES_ACTIVITY = "SalesActivity";

  SalesDao salesDao;
  SalesReportDao salesReportDao;
  EcmService ecmService;

  public SalesApp() {
    salesDao = new SalesDao();
    salesReportDao = new SalesReportDao();
    ecmService = new EcmService();
  }

  public SalesDao getSalesDao() {
    return salesDao;
  }

  public SalesReportDao getSalesReportDao() {
    return salesReportDao;
  }

  public EcmService getEcmService() {
    return ecmService;
  }

  public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {

    if (salesId == null) return;
    Sales sales = getSalesDao().getSalesBySalesId(salesId);
    Date today = new Date();
    if (isNotEffective(sales, today)) return;

    List<String> headers;
    List<SalesReportData> filteredReportDataList;
    List<SalesReportData> reportDataList = getSalesReportDao().getReportData(sales);

    filteredReportDataList = filterSalesActivityData(isSupervisor, reportDataList);
    filteredReportDataList = filterRedundantData(maxRow, filteredReportDataList);
    headers = getHeadersByNatTrade(isNatTrade);

    SalesActivityReport report = generateReport(headers, filteredReportDataList);
    getEcmService().uploadDocument(report.toXml());
  }

  protected List<SalesReportData> filterSalesActivityData(boolean isSupervisor, List<SalesReportData> reportDataList) {
    List<SalesReportData> dataList = new ArrayList<>();
    reportDataList.stream().filter(x->isMatchCondition(isSupervisor,x)).forEach(dataList::add);
    return dataList;
  }

  private boolean isMatchCondition(boolean isSupervisor, SalesReportData data) {
    return SALES_ACTIVITY.equalsIgnoreCase(data.getType()) &&(!data.isConfidential() || isSupervisor);
  }

  protected List<SalesReportData> filterRedundantData(int maxRow, List<SalesReportData> reportDataList) {
    int rowSize = reportDataList.size() > maxRow ? maxRow : reportDataList.size();
    reportDataList = reportDataList.subList(0, rowSize);
    return reportDataList;
  }

  protected List<String> getHeadersByNatTrade(boolean isNatTrade) {
    List<String> headers;
    headers = isNatTrade ? Arrays.asList("Sales ID", "Sales Name", "Activity", "Time")
        : Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
    return headers;
  }

  protected boolean isNotEffective(Sales sales, Date today) {
    return today.after(sales.getEffectiveTo()) || today.before(sales.getEffectiveFrom());
  }

  protected SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
    // TODO Auto-generated method stub
    return null;
  }

}
