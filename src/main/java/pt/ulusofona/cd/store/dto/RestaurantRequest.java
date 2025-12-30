package pt.ulusofona.cd.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequest {

    @NotBlank(message = "Restaurant name is required")
    @Size(min = 2, max = 120, message = "Restaurant name must be between 2 and 120 characters")
    private String name;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 80, message = "City must be between 2 and 80 characters")
    private String city;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 80, message = "Country must be between 2 and 80 characters")
    private String country;

    @NotBlank(message = "Phone is required")
    @Size(min = 6, max = 30, message = "Phone must be between 6 and 30 characters")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must be at most 255 characters long")
    private String email;
}
