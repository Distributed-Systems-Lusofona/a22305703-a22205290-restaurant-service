package pt.ulusofona.cd.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ReleaseSeatsRequest {

    @NotNull
    private OffsetDateTime scheduledAt;

    @Min(1)
    private int partySize;
}
