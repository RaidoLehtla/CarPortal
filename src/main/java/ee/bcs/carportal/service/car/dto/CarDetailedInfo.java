package ee.bcs.carportal.service.car.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link ee.bcs.carportal.persistence.car.Car}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CarDetailedInfo extends CarInfo implements Serializable {
    private String fuelTypeName;
    @NotNull
    private BigDecimal emissions;
    @NotNull
    private Integer price;
}