package pt.ulusofona.cd.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuItemResponse {

    private Long id;
    private Long restaurantId;

    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private boolean active;
}
