package ee.bcs.carportal.persistence.car;

import ee.bcs.carportal.service.car.dto.CarInfo;
import org.springframework.stereotype.Service;

@Service
public class CarMapperImplementation {
    public CarInfo toCarInfo(Car car) {
        if (car == null) {
            return null;
        }
        CarInfo carInfo = new CarInfo();
        carInfo.setMake(carManufacturerName(car));
        carInfo.setModelName(car.getModel());
        carInfo.setReleaseYear(car.getYear());

        return carInfo;
    }

    private String carManufacturerName(Car car) {
        if (car.getManufacturer() == null) {
            return null;
        }
        return car.getManufacturer().getName();
    }
}
