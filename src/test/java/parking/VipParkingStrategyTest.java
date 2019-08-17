package parking;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static parking.ParkingStrategy.NO_PARKING_LOT;

public class VipParkingStrategyTest {

  @Test
  public void testPark_givenAVipCarAndAFullParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

    /* Exercise 4, Write a test case on VipParkingStrategy.park()
     * With using Mockito spy, verify and doReturn */
    Car joiCar = new Car("JoiCarA");
    ParkingLot parkingLot = new ParkingLot("ParkingLot 1", 1);
    parkingLot.getParkedCars().add(new Car("LiuCar"));
    List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
    VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());
    doReturn(true).when(spyVipParkingStrategy).isAllowOverPark(joiCar);

    Receipt receipt = spyVipParkingStrategy.park(parkingLots, joiCar);

    assertEquals("JoiCarA", receipt.getCarName());
    assertEquals("ParkingLot 1", receipt.getParkingLotName());
  }

  @Test
  public void testPark_givenCarIsNotVipAndAFullParkingLog_thenGiveNoSpaceReceipt() {

    /* Exercise 4, Write a test case on VipParkingStrategy.park()
     * With using Mockito spy, verify and doReturn */
    Car joiCar = new Car("JoiCar");
    ParkingLot parkingLot = new ParkingLot("ParkingLot 1", 1);
    parkingLot.getParkedCars().add(new Car("LiuCar"));
    List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
    VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());
    doReturn(false).when(spyVipParkingStrategy).isAllowOverPark(joiCar);

    Receipt receipt = spyVipParkingStrategy.park(parkingLots, joiCar);

    assertEquals("JoiCar", receipt.getCarName());
    assertEquals(NO_PARKING_LOT, receipt.getParkingLotName());
    verify(spyVipParkingStrategy).createNoSpaceReceipt(joiCar);
  }

  @Test
  public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsVipCar_thenReturnTrue() {

    /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
     * You may refactor the code, or try to use
     * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
     */
    Car joiCar = new Car("JoiCarA");
    VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());
    CarDao mCarDao = mock(CarDao.class);
    doReturn(mCarDao).when(spyVipParkingStrategy).getCarDao();
    when(mCarDao.isVip("JoiCarA")).thenReturn(true);

    boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(joiCar);

    assertTrue(allowOverPark);
  }

  @Test
  public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsVipCar_thenReturnFalse() {

    /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
     * You may refactor the code, or try to use
     * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
     */
      Car joiCar = new Car("JoiCar");
      VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());
      CarDao mCarDao = mock(CarDao.class);
      when(mCarDao.isVip("JoiCar")).thenReturn(true);
      doReturn(mCarDao).when(spyVipParkingStrategy).getCarDao();

      boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(joiCar);
      assertFalse(allowOverPark);
  }

  @Test
  public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsNotVipCar_thenReturnFalse() {
    /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
     * You may refactor the code, or try to use
     * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
     */
      Car joiCar = new Car("JoiCarA");
      VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());
      CarDao mCarDao = mock(CarDao.class);
      when(mCarDao.isVip("JoiCarA")).thenReturn(false);
      doReturn(mCarDao).when(spyVipParkingStrategy).getCarDao();

      boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(joiCar);
      assertFalse(allowOverPark);
  }

  @Test
  public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsNotVipCar_thenReturnFalse() {
    /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
     * You may refactor the code, or try to use
     * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
     */
      Car joiCar = new Car("JoiCar");
      VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());
      CarDao mCarDao = mock(CarDao.class);
      when(mCarDao.isVip("JoiCar")).thenReturn(false);
      doReturn(mCarDao).when(spyVipParkingStrategy).getCarDao();

      boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(joiCar);
      assertFalse(allowOverPark);
  }

  private Car createMockCar(String carName) {
    Car car = mock(Car.class);
    when(car.getName()).thenReturn(carName);
    return car;
  }
}
