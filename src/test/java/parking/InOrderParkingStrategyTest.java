package parking;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static parking.ParkingStrategy.NO_PARKING_LOT;

public class InOrderParkingStrategyTest {

  @Test
  public void testCreateReceipt_givenACarAndAParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

    /* Exercise 1, Write a test case on InOrderParkingStrategy.createReceipt()
     * With using Mockito to mock the input parameter */
    Car mCar = mock(Car.class);
    when(mCar.getName()).thenReturn("JoiCar");
    ParkingLot mParkingLot = mock(ParkingLot.class);
    when(mParkingLot.getName()).thenReturn("ParkingLot 1");

    InOrderParkingStrategy strategy = new InOrderParkingStrategy();

    Receipt receipt = strategy.createReceipt(mParkingLot, mCar);

    verify(mCar, times(1)).getName();
    verify(mParkingLot).getName();
    assertEquals("JoiCar", receipt.getCarName());
    assertEquals("ParkingLot 1", receipt.getParkingLotName());

  }

  @Test
  public void testCreateNoSpaceReceipt_givenACar_thenGiveANoSpaceReceipt() {

    /* Exercise 1, Write a test case on InOrderParkingStrategy.createNoSpaceReceipt()
     * With using Mockito to mock the input parameter */
    Car mCar = mock(Car.class);
    when(mCar.getName()).thenReturn("JoiCar");

    InOrderParkingStrategy strategy = new InOrderParkingStrategy();

    Receipt receipt = strategy.createNoSpaceReceipt(mCar);

    verify(mCar, times(1)).getName();
    assertEquals("JoiCar", receipt.getCarName());
    assertEquals(NO_PARKING_LOT, receipt.getParkingLotName());

  }

  @Test
  public void testPark_givenNoAvailableParkingLot_thenCreateNoSpaceReceipt() {

    /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for no available parking lot */
    Car car = new Car("JoiCar");
    InOrderParkingStrategy spyStrategy = spy(new InOrderParkingStrategy());

    Receipt receipt = spyStrategy.park(null, car);

    verify(spyStrategy).createNoSpaceReceipt(car);
  }

  @Test
  public void testPark_givenThereIsOneParkingLotWithSpace_thenCreateReceipt() {

    /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot */
    ParkingLot parkingLot = new ParkingLot("ParkingLot 1", 1);
    List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
    Car car = new Car("JoiCar");
    InOrderParkingStrategy spyStrategy = spy(new InOrderParkingStrategy());

    Receipt receipt = spyStrategy.park(parkingLots, car);

    verify(spyStrategy).createReceipt(parkingLot, car);
  }

  @Test
  public void testPark_givenThereIsOneFullParkingLot_thenCreateReceipt() {

    /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot but it is full */
    ParkingLot parkingLot = new ParkingLot("ParkingLot 1", 1);
    parkingLot.getParkedCars().add(new Car("LiuCar"));
    List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
    Car car = new Car("JoiCar");
    InOrderParkingStrategy spyStrategy = spy(new InOrderParkingStrategy());

    Receipt receipt = spyStrategy.park(parkingLots, car);

    verify(spyStrategy).createNoSpaceReceipt(car);
    verify(spyStrategy, times(0)).createReceipt(parkingLot, car);
  }

  @Test
  public void testPark_givenThereIsMultipleParkingLotAndFirstOneIsFull_thenCreateReceiptWithUnfullParkingLot() {

    /* Exercise 3: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for multiple parking lot situation */
    ParkingLot firstParkingLot = new ParkingLot("ParkingLot 1", 1);
    ParkingLot secondParkingLot = new ParkingLot("ParkingLot 2", 1);
    firstParkingLot.getParkedCars().add(new Car("LiuCar"));
    List<ParkingLot> parkingLots = Arrays.asList(firstParkingLot, secondParkingLot);
    Car car = new Car("JoiCar");
    InOrderParkingStrategy spyStrategy = spy(new InOrderParkingStrategy());

    Receipt receipt = spyStrategy.park(parkingLots, car);

    assertEquals("JoiCar", receipt.getCarName());
    verify(spyStrategy).createReceipt(secondParkingLot, car);
  }
}
