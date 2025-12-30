package pt.ulusofona.cd.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 120)
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(name = "city", nullable = false, length = 80)
    private String city;

    @NotBlank
    @Size(min = 2, max = 80)
    @Column(name = "country", nullable = false, length = 80)
    private String country;

    @NotBlank
    @Size(min = 6, max = 30)
    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "email", nullable = false, length = 255)
    private String email;
}
