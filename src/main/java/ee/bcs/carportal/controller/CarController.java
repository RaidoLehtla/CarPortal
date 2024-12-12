package ee.bcs.carportal.controller;

import ee.bcs.carportal.persistence.Car;
import ee.bcs.carportal.persistence.FuelType;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ee.bcs.carportal.persistence.FuelType.*;

@RequestMapping("/api/v1")
@RestController
public class CarController {

    private static final double BASE_FEE = 50.0;

    public static List<Car> cars = createCars();

    public static List<Car> createCars() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Model 3", "Tesla", 2020, ELECTRIC, 0.0, 44000));
        cars.add(new Car("Civic", "Honda", 2021, FuelType.PETROL, 0.05, 25000));
        cars.add(new Car("Camry", "Toyota", 2022, FuelType.PETROL, 0.04, 28000));
        cars.add(new Car("F-150", "Ford", 2023, FuelType.PETROL, 0.1, 45000));
        cars.add(new Car("Prius", "Toyota", 2020, FuelType.HYBRID, 0.03, 30000));
        return cars;
    }

    // Mandatory endpoints go to below
    // Please use @Tag annotation as below with all mandatory endpoints:
    // @Tag(name = "Mandatory")

    @GetMapping("/cars/all")
    @Tag(name = "Mandatory")
    public List<Car> getAllCars() {
        return cars;
    }

    @GetMapping("/cars/price-range")
    @Tag(name = "Mandatory")
    public List<Car> getCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        List<Car> carsInPriceRange = new ArrayList<>();
        for (Car car : cars) {
            boolean isWithinPriceRange = car.getPrice() >= from && car.getPrice() <= to;
            if (isWithinPriceRange) {
                carsInPriceRange.add(car);
            }
        }
        return carsInPriceRange;
    }

    @GetMapping("/cars/green/price-range")
    @Tag(name = "Mandatory")
    public List<Car> getGreenCarsInPriceRange(@RequestParam int from, @RequestParam int to) {
        List<Car> greenCarsInPriceRange = new ArrayList<>();
        for (Car car : cars) {
            boolean isWithinPriceRange = car.getPrice()  >= from && car.getPrice()  <= to;
            boolean isGreenCar = car.getFuelType().equals(ELECTRIC) || car.getFuelType().equals(HYBRID);
            if (isWithinPriceRange && isGreenCar) {
                greenCarsInPriceRange.add(car);
            }
        }
        return greenCarsInPriceRange;
    }

    @GetMapping("/car/{carId}/registration-tax")
    @Tag(name = "Mandatory")
    public String getCarRegistrationTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        double registrationTaxRate = calculateRegistrationTaxRate(carId, baseYear);
        double taxAmount = calculateTaxAmount(carId, registrationTaxRate);
        return "The registration tax rate for " + cars.get(carId).getModelYear() + " " + cars.get(carId).getManufacturer() + " "
                + cars.get(carId).getCarModel() + " is " + registrationTaxRate + "% with total tax amount €" + String.format("%.0f", taxAmount);
    }

    @GetMapping("/car/{carId}/annual-tax")
    @Tag(name = "Mandatory")
    public String getCarAnnualTaxByCarId(@PathVariable int carId, @RequestParam int baseYear) {
        double annualTax = calculateAnnualTax(carId, baseYear);
        return String.format("The annual tax for %d %s %s is €%.0f", cars.get(carId).getModelYear(), cars.get(carId).getManufacturer(), cars.get(carId).getCarModel(), annualTax);
    }

    @GetMapping("/car/random/basic-info")
    @Tag(name = "Mandatory")
    public Car getRandomCarBasicInfo() {
        int carId = new Random().nextInt(cars.size());
        return cars.get(carId);
    }

    @GetMapping("/car/random/detailed-info")
    @Tag(name = "Mandatory")
    public Car getRandomCarDetailedInfo() {
        int carId = new Random().nextInt(cars.size());
        return cars.get(carId);
    }

    @GetMapping("/car/{carId}/basic-info")
    @Tag(name = "Mandatory")
    public Car getCarBasicInfoByCarId(@PathVariable int carId) {
        return cars.get(carId);
    }

    @GetMapping("/car/{carId}/detailed-info")
    @Tag(name = "Mandatory")
    public Car getCarDetailedInfoByCarId(@PathVariable int carId) {
        return cars.get(carId);
    }

    // Extra practice endpoints go to below
    // Please use @Tag annotation as below with all extra practice endpoints:
    // @Tag(name = "Extra practice")

    @GetMapping("/cars/registration-tax-range")
    @Tag(name = "Extra practice")
    public List<Car> getCarsByRegistrationTaxRange(@RequestParam int from, @RequestParam int to, @RequestParam int baseYear) {
        int carId = 0;
        List<Car> carsInRegistrationTaxRange = new ArrayList<>();
        for (Car car : cars) {
            double taxRate = calculateRegistrationTaxRate(carId, baseYear);
            double taxAmount = calculateTaxAmount(carId, taxRate);
            if (taxAmount >= from && taxAmount <= to) {
                carsInRegistrationTaxRange.add(car);
            }
            carId++;
        }
        return carsInRegistrationTaxRange;
    }

    @GetMapping("/cars/annual-tax-range")
    @Tag(name = "Extra practice")
    public List<Car> getCarsByAnnualTaxRange(@RequestParam int from, @RequestParam int to, @RequestParam int baseYear) {
        int carId = 0;
        List<Car> carsInAnnualTaxRange = new ArrayList<>();
        double annualTax = calculateAnnualTax(carId, baseYear);
        for (Car car : cars) {
            if (annualTax >= from && annualTax <= to) {
                carsInAnnualTaxRange.add(car);
            }
            carId++;
        }
        return carsInAnnualTaxRange;
    }


    // ALL PRIVATE METHODS go to below
    private double calculateRegistrationTaxRate(int carId, int baseYear) {
        double taxRate = getFuelTypeAdjustedRegistrationTaxRate(carId);
        taxRate = getEmissionsAdjustedRegistrationTaxRate(carId, taxRate);
        taxRate = getModelYearAdjustedRegistrationTaxRate(carId, baseYear, taxRate);
        return getRoundedRegistrationTaxRate(taxRate);
    }

    private double getFuelTypeAdjustedRegistrationTaxRate(int carId) {
        final double BASE_TAX_RATE = 5.0;
        final double ELECTRIC_ADJUSTMENT = -1.5;
        final double HYBRID_ADJUSTMENT = -0.5;
        final double PETROL_ADJUSTMENT = 1.5;
        double taxRate = BASE_TAX_RATE; // Base tax rate of 5%

        switch (cars.get(carId).getFuelType()) {
            case ELECTRIC -> taxRate += ELECTRIC_ADJUSTMENT;
            case HYBRID -> taxRate += HYBRID_ADJUSTMENT;
            case PETROL -> taxRate += PETROL_ADJUSTMENT;
        }
        return taxRate;
    }

    private double getEmissionsAdjustedRegistrationTaxRate(int carId, double taxRate) {
        final double EMISSION_MULTIPLIER = 10.0; // For each 0.01 in emissions, increase tax rate by 0.1%
        double emissionAdjustment = cars.get(carId).getEmissions() * EMISSION_MULTIPLIER; // For each 0.01 in emissions, increase tax rate by 0.1%
        taxRate += emissionAdjustment;
        return taxRate;
    }


    private double getModelYearAdjustedRegistrationTaxRate(int carId, int baseYear, double taxRate) {
        final double MODEL_YEAR_MULTIPLIER = 0.2; // Reduce 0.2% for each year older than the base year
        int yearDifference = baseYear - cars.get(carId).getModelYear();
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce 0.2% for each year older than the base year
        taxRate -= modelYearAdjustment;
        return taxRate;
    }

    private static double getRoundedRegistrationTaxRate(double taxRate) {
        return Math.round(taxRate * 10.0) / 10.0;
    }

    private double calculateTaxAmount(int carId, double registrationTaxRate) {
        return cars.get(carId).getPrice() * (registrationTaxRate / 100);
    }

    private double calculateAnnualTax(int carId, int baseYear) {
        double annualTax = getFuelTypeAdjustedAnnualTax(carId);
        annualTax = getEmissionsAdjustedAnnualTax(carId, annualTax);
        annualTax = getModelYearAdjustedAnnualTax(carId, baseYear, annualTax);
        return getMinimumFeeAdjustedAnnualTax(annualTax);
    }

    private double getFuelTypeAdjustedAnnualTax(int carId) {
        final double HYBRID_ADJUSTMENT = 20.0;
        final double PETROL_ADJUSTMENT = 30.0;
        double annualTax = BASE_FEE; // Base annual tax fee

        switch (cars.get(carId).getFuelType()) {
            case HYBRID -> annualTax += HYBRID_ADJUSTMENT;
            case PETROL -> annualTax += PETROL_ADJUSTMENT;
        }
        return annualTax;
    }

    private double getEmissionsAdjustedAnnualTax(int carId, double annualTax) {
        final double EMISSION_MULTIPLIER = 500.0; // For each 0.01 in emissions, increase tax by €5
        double emissionAdjustment = cars.get(carId).getEmissions() * EMISSION_MULTIPLIER; // For each 0.01 in emissions, increase tax by €5
        annualTax += emissionAdjustment;
        return annualTax;
    }

    private double getModelYearAdjustedAnnualTax(int carId, int baseYear, double annualTax) {
        final double MODEL_YEAR_MULTIPLIER = 2.0; // Reduce €2 for each year older than the base year
        int yearDifference = baseYear - cars.get(carId).getModelYear();
        double modelYearAdjustment = yearDifference * MODEL_YEAR_MULTIPLIER; // Reduce €2 for each year older than the base year
        annualTax -= modelYearAdjustment;
        return annualTax;
    }

    private static double getMinimumFeeAdjustedAnnualTax(double annualTax) {
        boolean annualTaxIsBelowBaseFee = annualTax < BASE_FEE;
        if (annualTaxIsBelowBaseFee) {
            annualTax = BASE_FEE;
        }
        return annualTax;
    }


}
