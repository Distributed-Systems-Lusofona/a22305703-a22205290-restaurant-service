package pt.ulusofona.cd.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ulusofona.cd.store.exception.RestaurantNotFoundException;
import pt.ulusofona.cd.store.model.AvailabilitySlot;
import pt.ulusofona.cd.store.repository.AvailabilitySlotRepository;
import pt.ulusofona.cd.store.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final RestaurantRepository restaurantRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    @Transactional(readOnly = true)
    public List<AvailabilitySlot> getAvailability(Long restaurantId, LocalDate date, int partySize) {

        if (partySize <= 0) {
            throw new IllegalArgumentException("partySize must be greater than zero");
        }

        boolean exists = restaurantRepository.existsById(restaurantId);
        if (!exists) {
            throw new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        // slots do restaurante nesse dia com lugares suficientes
        return availabilitySlotRepository.findAvailableSlots(restaurantId, date, partySize);
    }
}
