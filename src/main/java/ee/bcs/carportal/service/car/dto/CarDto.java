package ee.bcs.carportal.service.car.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link ee.bcs.carportal.persistence.car.Car}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto implements Serializable {
    @NotNull(message = "Manufacturer ID must not be null")
    @Positive(message = "Manufacturer ID must be a positive number")
    private Integer manufacturerId;

    @NotNull(message = "Fuel Type ID must not be null")
    @Positive(message = "Fuel Type ID must be a positive number")
    private Integer fuelTypeId;

    @NotNull(message = "Model must not be null")
    @Size(max = 255, message = "Model name must not exceed 255 characters")
    private String model;

    @NotNull(message = "Year must not be null")
    private Integer year;

    @NotNull(message = "Emissions must not be null")
    private BigDecimal emissions;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be a positive number")
    private Integer price;
}