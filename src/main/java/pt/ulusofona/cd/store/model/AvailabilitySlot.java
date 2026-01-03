package pt.ulusofona.cd.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "availability_slots",
        indexes = {
                @Index(name = "idx_availability_restaurant_date", columnList = "restaurant_id,date")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Min(1)
    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Min(0)
    @Column(name = "seats_available", nullable = false)
    private int seatsAvailable;

    @AssertTrue(message = "endTime must be after startTime")
    public boolean isEndAfterStart() {
        if (startTime == null || endTime == null) return true;
        return endTime.isAfter(startTime);
    }

    @AssertTrue(message = "seatsAvailable must be between 0 and capacity")
    public boolean isSeatsAvailableWithinCapacity() {
        return seatsAvailable >= 0 && seatsAvailable <= capacity;
    }
}
