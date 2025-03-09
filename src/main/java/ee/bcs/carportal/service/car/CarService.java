package ee.bcs.carportal.service.car;

import ee.bcs.carportal.persistence.car.Car;
import ee.bcs.carportal.persistence.car.CarMapper;
import ee.bcs.carportal.persistence.car.CarRepository;
import ee.bcs.carportal.persistence.fueltype.FuelType;
import ee.bcs.carportal.persistence.fueltype.FuelTypeRepository;
import ee.bcs.carportal.persistence.manufacturer.Manufacturer;
import ee.bcs.carportal.persistence.manufacturer.ManufacturerRepository;
import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final FuelTypeRepository fuelTypeRepository;
    private final CarMapper carMapper;


    public CarInfo findCarInfo(Integer carId) {
        Car car = carRepository.getReferenceById(carId);
        return carMapper.toCarInfo(car);
    }

    public List<CarInfo> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return carMapper.toCarInfos(cars);
    }

    public List<Car> findCarsInPriceRange(Integer from, Integer to) {
        return carRepository.findCarsBy(from, to);
    }

    public List<Car> findCarsInPriceRangeWithFuelType(Integer from, Integer to, String fuelTypeCode ) {
        return carRepository.findCarsBy(from, to, fuelTypeCode);
    }

    public void addCar(CarDto carDto) {
        Manufacturer manufacturer = manufacturerRepository.findById(carDto.getManufacturerId()).orElse(null);
        FuelType fuelType = fuelTypeRepository.findById(carDto.getFuelTypeId()).orElse(null);

        Car car = carMapper.toCar(carDto);
        car.setManufacturer(manufacturer);
        car.setFuelType(fuelType);
        carRepository.save(car);
    }

    public CarDetailedInfo findCarDetailedInfo(Integer carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + carId));

        return carMapper.toCarDetailedInfo(car);
    }
}
