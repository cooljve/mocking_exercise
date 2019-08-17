package parking;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VipParkingStrategy implements ParkingStrategy {

	CarDao carDao;

	public VipParkingStrategy(){
		carDao = new CarDaoImpl();
	}

	public CarDao getCarDao() {
		return carDao;
	}

	public Receipt park(List<ParkingLot> parkingLots, Car car) {

		parkingLots = Optional.ofNullable(parkingLots).orElse(new ArrayList<>());
		
		for (ParkingLot parkingLot : parkingLots) {
			if (parkingLot.isFull() && !isAllowOverPark(car)) {
				continue;
			}
			
			parkingLot.getParkedCars().add(car);
			return createReceipt(parkingLot, car);
		}
		
		return createNoSpaceReceipt(car);
	}

	protected Receipt createReceipt(ParkingLot parkingLot, Car car) {
		
		Receipt receipt = new Receipt();
		receipt.setCarName(car.getName());
		receipt.setParkingLotName(parkingLot.getName());
		return receipt;
	}

	protected Receipt createNoSpaceReceipt(Car car) {
		
		Receipt receipt = new Receipt();
		receipt.setCarName(car.getName());
		receipt.setParkingLotName(NO_PARKING_LOT);
		return receipt;
	}

	protected boolean isAllowOverPark(Car car){
		return getCarDao().isVip(car.getName()) && StringUtils.contains(car.getName(), "A");
	}

	@Override
	public Integer calculateHourlyPrice() {
		return ParkingLot.getBasicHourlyPrice() * 2;
	}
}
