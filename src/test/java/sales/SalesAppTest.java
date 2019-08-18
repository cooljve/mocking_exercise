package sales;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SalesAppTest {

  private SalesReportDao salesReportDao;
  private EcmService ecmService;
  private static final String SALES_ACTIVITY = "SalesActivity";
  private static final String SALES_INACTIVITY = "SalesInActivity";

  @Before
  public void setUp() {
    salesReportDao = mock(SalesReportDao.class);
    ecmService = mock(EcmService.class);
  }

  @Test
  public void testFilterRedundantData() {
    SalesApp mSalesApp = spy(new SalesApp());
    List<SalesReportData> reportDataList = expectSalesReportDataList();

    List<SalesReportData> filteredDataList = mSalesApp.filterRedundantData(2, reportDataList);

    assertEquals(3, reportDataList.size());
    assertEquals(2, filteredDataList.size());
  }

  @Test
  public void testFilterSalesActivityDataWithSupervisorIsTrue() {
    SalesApp mSalesApp = spy(new SalesApp());
    List<SalesReportData> reportDataList = expectSalesReportDataList();

    List<SalesReportData> dataList = mSalesApp.filterSalesActivityData(true, reportDataList);

    assertEquals(3, reportDataList.size());
    assertEquals(2, dataList.size());
  }

  @Test
  public void testFilterSalesActivityDataWithSupervisorIsFalse() {
    SalesApp mSalesApp = spy(new SalesApp());
    List<SalesReportData> reportDataList = expectSalesReportDataList();
    List<SalesReportData> dataList = mSalesApp.filterSalesActivityData(false, reportDataList);

    assertEquals(3,reportDataList.size());
    assertEquals(1, dataList.size());
  }

  @Test
  public void testGetHeadersByNatTradeIsTrue() {
    SalesApp mSalesApp = spy(new SalesApp());
    List<String> headers = mSalesApp.getHeadersByNatTrade(true);

    assertEquals("Time",headers.get(headers.size()-1));
  }

  @Test
  public void testGetHeadersByNatTradeIsFalse() {
    SalesApp mSalesApp = spy(new SalesApp());
    List<String> headers = mSalesApp.getHeadersByNatTrade(false);

    assertEquals("Local Time",headers.get(headers.size()-1));
  }

  @Test
  public void testGenerateReport() {

    SalesApp mSalesApp = spy(new SalesApp());
    SalesActivityReport report = new SalesActivityReport();
    List<SalesReportData> reportDataList = expectSalesReportDataList();

    doReturn(false).when(mSalesApp).isNotEffective(any(), any());
    doReturn(report).when(mSalesApp).generateReport(any(), any());
    doReturn(salesReportDao).when(mSalesApp).getSalesReportDao();
    doReturn(ecmService).when(mSalesApp).getEcmService();
    when(salesReportDao.getReportData(any())).thenReturn(reportDataList);

    mSalesApp.generateSalesActivityReport("DUMMY", 1000, false, false);

    verify(ecmService).uploadDocument(any());
  }

  private List<SalesReportData> expectSalesReportDataList() {
    List<SalesReportData> reportDataList = new ArrayList<>();
    reportDataList.add(expectSalesReportData(SALES_ACTIVITY,true));
    reportDataList.add(expectSalesReportData(SALES_ACTIVITY,false));
    reportDataList.add(expectSalesReportData(SALES_INACTIVITY,false));
    return reportDataList;
  }

  private SalesReportData expectSalesReportData(String activityType, boolean isConfidential) {
    SalesReportData reportData = mock(SalesReportData.class);
    when(reportData.getType()).thenReturn(activityType);
    when(reportData.isConfidential()).thenReturn(isConfidential);
    return reportData;
  }
}
