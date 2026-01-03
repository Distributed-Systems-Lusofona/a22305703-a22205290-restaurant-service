package pt.ulusofona.cd.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ulusofona.cd.store.dto.AvailabilitySlotRequest;
import pt.ulusofona.cd.store.dto.AvailabilitySlotResponse;
import pt.ulusofona.cd.store.dto.ReleaseSeatsRequest;
import pt.ulusofona.cd.store.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants/{restaurantId}")
public class AvailabilityController {

    private final RestaurantService service;

    @GetMapping("/availability")
    public ResponseEntity<List<AvailabilitySlotResponse>> getAvailability(
            @PathVariable Long restaurantId,
            @RequestParam("date") LocalDate date,
            @RequestParam("partySize") int partySize
    ) {
        return ResponseEntity.ok(service.getAvailabilityForPartySize(restaurantId, date, partySize));
    }

    @PostMapping("/availability/release")
    public ResponseEntity<Void> release(
            @PathVariable Long restaurantId,
            @Valid @RequestBody ReleaseSeatsRequest request
    ) {
        service.releaseSeats(restaurantId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/availability-slots")
    public ResponseEntity<AvailabilitySlotResponse> createSlot(
            @PathVariable Long restaurantId,
            @Valid @RequestBody AvailabilitySlotRequest request
    ) {
        return ResponseEntity.ok(service.createAvailabilitySlot(restaurantId, request));
    }

    @GetMapping("/availability-slots")
    public ResponseEntity<List<AvailabilitySlotResponse>> listSlots(
            @PathVariable Long restaurantId,
            @RequestParam(value = "date", required = false) LocalDate date
    ) {
        return ResponseEntity.ok(service.listAvailabilitySlots(restaurantId, date));
    }

    @PutMapping("/availability-slots/{slotId}")
    public ResponseEntity<AvailabilitySlotResponse> updateSlot(
            @PathVariable Long restaurantId,
            @PathVariable Long slotId,
            @Valid @RequestBody AvailabilitySlotRequest request
    ) {
        return ResponseEntity.ok(service.updateAvailabilitySlot(restaurantId, slotId, request));
    }

    @DeleteMapping("/availability-slots/{slotId}")
    public ResponseEntity<Void> deleteSlot(
            @PathVariable Long restaurantId,
            @PathVariable Long slotId
    ) {
        service.deleteAvailabilitySlot(restaurantId, slotId);
        return ResponseEntity.noContent().build();
    }
}
