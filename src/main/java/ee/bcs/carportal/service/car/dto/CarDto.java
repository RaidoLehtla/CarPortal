package ee.bcs.carportal.service.car.dto;

import jakarta.validation.constraints.NotNull;
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
    private Integer manufacturerId;
    private Integer fuelTypeId;
    @NotNull
    @Size(max = 255)
    private String model;
    @NotNull
    private Integer year;
    @NotNull
    private BigDecimal emissions;
    @NotNull
    private Integer price;
}