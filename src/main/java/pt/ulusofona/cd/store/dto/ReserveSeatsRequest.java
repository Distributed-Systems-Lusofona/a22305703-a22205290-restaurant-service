package pt.ulusofona.cd.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveSeatsRequest {

    @NotNull
    private Long availabilitySlotId;

    @Min(1)
    private int partySize;
}
