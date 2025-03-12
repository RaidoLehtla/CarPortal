package ee.bcs.carportal.persistence.car;

import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {

    @Mapping(source = "manufacturer.name", target = "make")
    @Mapping(source = "model", target = "modelName")
    @Mapping(source = "year", target = "releaseYear")
    CarInfo toCarInfo(Car car);

    @Mapping(source = "manufacturer.name", target = "make")
    @Mapping(source = "model", target = "modelName")
    @Mapping(source = "year", target = "releaseYear")
    @Mapping(source = "fuelType.name", target = "fuelType")
    @Mapping(source = "emissions", target = "emissions")
    @Mapping(source = "price", target = "price")
    CarDetailedInfo toCarDetailedInfo(Car car);
    List<CarInfo> toCarInfos(List<Car> cars);
    Car toCar(CarDto cardto);

    @InheritConfiguration(name = "toCar")
    void updateCar(CarDto carDto, @MappingTarget Car car );
}
