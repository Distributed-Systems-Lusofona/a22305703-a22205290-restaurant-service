package pt.ulusofona.cd.store.mapper;

import pt.ulusofona.cd.store.dto.AvailabilitySlotRequest;
import pt.ulusofona.cd.store.dto.AvailabilitySlotResponse;
import pt.ulusofona.cd.store.model.AvailabilitySlot;

public class AvailabilitySlotMapper {

    private AvailabilitySlotMapper() {
    }

    public static AvailabilitySlot toEntity(Long restaurantId, AvailabilitySlotRequest dto) {
        AvailabilitySlot s = new AvailabilitySlot();
        s.setRestaurantId(restaurantId);
        s.setDate(dto.getDate());
        s.setStartTime(dto.getStartTime());
        s.setEndTime(dto.getEndTime());
        s.setCapacity(dto.getCapacity());
        s.setSeatsAvailable(dto.getCapacity()); // começa cheio
        return s;
    }

    public static AvailabilitySlotResponse toResponse(AvailabilitySlot entity) {
        AvailabilitySlotResponse r = new AvailabilitySlotResponse();
        r.setId(entity.getId());
        r.setRestaurantId(entity.getRestaurantId());
        r.setDate(entity.getDate());
        r.setStartTime(entity.getStartTime());
        r.setEndTime(entity.getEndTime());
        r.setCapacity(entity.getCapacity());
        r.setSeatsAvailable(entity.getSeatsAvailable());
        return r;
    }
}
