package ee.bcs.carportal.service.car;

import ee.bcs.carportal.infrastructure.exception.DatabaseConflictException;
import ee.bcs.carportal.infrastructure.exception.ResourceNotFoundException;
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
        Car car = carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
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
        boolean carExists = carRepository.carExistsBy(carDto.getManufacturerId(), carDto.getModel(), carDto.getYear());
        if (carExists) {
            throw new DatabaseConflictException("Car already exists");
        }
        Manufacturer manufacturer = manufacturerRepository.findById(carDto.getManufacturerId()).orElse(null);
        FuelType fuelType = fuelTypeRepository.findById(carDto.getFuelTypeId()).orElse(null);
        Car car = carMapper.toCar(carDto);
        car.setManufacturer(manufacturer);
        car.setFuelType(fuelType);
        carRepository.save(car);
    }

    public CarDetailedInfo findCarDetailedInfo(Integer carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return carMapper.toCarDetailedInfo(car);
    }

    public void updateCar(Integer carId, CarDto carDto) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        carMapper.updateCar(carDto, car);
        if (!car.getManufacturer().getId().equals((carDto.getManufacturerId()))) {
            Manufacturer manufacturer = manufacturerRepository.findById(carDto.getManufacturerId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
            car.setManufacturer(manufacturer);
        }
        if (!car.getFuelType().getId().equals(carDto.getFuelTypeId())) {
            FuelType fuelType = fuelTypeRepository.findById(carDto.getFuelTypeId()).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
            car.setFuelType(fuelType);
        }
        carRepository.save(car);
    }

    public void deleteCar(Integer carId) {
            Car car = carRepository
                    .findById(carId)
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        carRepository.deleteById(carId);
    }
}


