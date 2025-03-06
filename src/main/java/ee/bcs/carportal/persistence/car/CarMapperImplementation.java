package ee.bcs.carportal.persistence.car;

import ee.bcs.carportal.service.car.dto.CarInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarMapperImplementation {

    public List<CarInfo> toCarInfos(List<Car> cars) {
        if (cars == null) {
            return null;
        }
        List<CarInfo> carInfos = new ArrayList<>();
        for (Car car : cars) {
            carInfos.add(toCarInfo(car));
        }
        return carInfos;
    }

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
        return (car == null) ? null : car.getManufacturer().getName();
    }

}
