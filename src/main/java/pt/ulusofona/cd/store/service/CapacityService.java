package pt.ulusofona.cd.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ulusofona.cd.store.dto.ReleaseSeatsRequest;
import pt.ulusofona.cd.store.exception.AvailabilitySlotNotFoundException;
import pt.ulusofona.cd.store.exception.RestaurantNotFoundException;
import pt.ulusofona.cd.store.model.AvailabilitySlot;
import pt.ulusofona.cd.store.repository.AvailabilitySlotRepository;
import pt.ulusofona.cd.store.repository.RestaurantRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CapacityService {

    private final RestaurantRepository restaurantRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    @Transactional
    public void releaseSeats(Long restaurantId, ReleaseSeatsRequest request) {

        if (restaurantId == null) {
            throw new IllegalArgumentException("restaurantId is required");
        }
        if (request == null) {
            throw new IllegalArgumentException("request body is required");
        }
        if (request.getScheduledAt() == null) {
            throw new IllegalArgumentException("scheduledAt is required");
        }
        if (request.getPartySize() <= 0) {
            throw new IllegalArgumentException("partySize must be greater than zero");
        }

        if (!restaurantRepository.existsById(restaurantId)) {
            throw new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        OffsetDateTime scheduledAt = request.getScheduledAt();
        LocalDate date = scheduledAt.toLocalDate();
        LocalTime time = scheduledAt.toLocalTime();

        AvailabilitySlot slot = availabilitySlotRepository
                .findSlotForUpdate(restaurantId, date, time)
                .orElseThrow(() -> new AvailabilitySlotNotFoundException(
                        "No availability slot found for restaurant " + restaurantId + " at " + scheduledAt
                ));

        int newSeats = slot.getSeatsAvailable() + request.getPartySize();
        if (newSeats > slot.getCapacity()) {
            newSeats = slot.getCapacity();
        }

        slot.setSeatsAvailable(newSeats);
        availabilitySlotRepository.save(slot);
    }
}
