package pt.ulusofona.cd.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ulusofona.cd.store.dto.*;
import pt.ulusofona.cd.store.exception.*;
import pt.ulusofona.cd.store.mapper.AvailabilitySlotMapper;
import pt.ulusofona.cd.store.mapper.MenuItemMapper;
import pt.ulusofona.cd.store.mapper.RestaurantMapper;
import pt.ulusofona.cd.store.model.AvailabilitySlot;
import pt.ulusofona.cd.store.model.MenuItem;
import pt.ulusofona.cd.store.model.Restaurant;
import pt.ulusofona.cd.store.repository.AvailabilitySlotRepository;
import pt.ulusofona.cd.store.repository.MenuItemRepository;
import pt.ulusofona.cd.store.repository.RestaurantRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
//cenas do restaurant
    @Transactional
    public Restaurant createRestaurant(RestaurantRequest request) {
        Restaurant restaurant = RestaurantMapper.toEntity(request);
        return restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Transactional
    public Restaurant updateRestaurant(Long id, RestaurantRequest details) {
        Restaurant restaurant = getRestaurantById(id);

        restaurant.setName(details.getName().trim());
        restaurant.setAddress(details.getAddress().trim());
        restaurant.setCity(details.getCity().trim());
        restaurant.setCountry(details.getCountry().trim());
        restaurant.setPhone(details.getPhone().trim());
        restaurant.setEmail(details.getEmail().trim());

        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }

//isto é a zona de menu
    @Transactional
    public MenuItemResponse createMenuItem(Long restaurantId, MenuItemRequest request) {
        getRestaurantById(restaurantId);

        if (request.getPrice() == null || request.getPrice().signum() <= 0) {
            throw new DomainValidationException("price must be greater than zero");
        }

        MenuItem entity = MenuItemMapper.toEntity(restaurantId, request);
        return MenuItemMapper.toResponse(menuItemRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItems(Long restaurantId) {
        getRestaurantById(restaurantId);
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(MenuItemMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItem(Long restaurantId, Long itemId) {
        getRestaurantById(restaurantId);
        MenuItem item = menuItemRepository.findById(itemId)
                .filter(i -> i.getRestaurantId().equals(restaurantId))
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " + itemId));
        return MenuItemMapper.toResponse(item);
    }

    @Transactional
    public MenuItemResponse updateMenuItem(Long restaurantId, Long itemId, MenuItemRequest request) {
        getRestaurantById(restaurantId);

        if (request.getPrice() == null || request.getPrice().signum() <= 0) {
            throw new DomainValidationException("price must be greater than zero");
        }

        MenuItem item = menuItemRepository.findById(itemId)
                .filter(i -> i.getRestaurantId().equals(restaurantId))
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " + itemId));

        item.setName(request.getName().trim());
        item.setDescription(request.getDescription() == null ? null : request.getDescription().trim());
        item.setPrice(request.getPrice());
        item.setCurrency(request.getCurrency().trim().toUpperCase());
        item.setActive(request.isActive());

        return MenuItemMapper.toResponse(menuItemRepository.save(item));
    }

    @Transactional
    public void deleteMenuItem(Long restaurantId, Long itemId) {
        getRestaurantById(restaurantId);
        MenuItem item = menuItemRepository.findById(itemId)
                .filter(i -> i.getRestaurantId().equals(restaurantId))
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found with id: " + itemId));
        menuItemRepository.delete(item);
    }

    //vagas
    @Transactional
    public AvailabilitySlotResponse createAvailabilitySlot(Long restaurantId, AvailabilitySlotRequest request) {
        getRestaurantById(restaurantId);

        validateSlotTimes(request.getDate(), request.getStartTime(), request.getEndTime(), request.getCapacity());

        long overlaps = availabilitySlotRepository.countOverlappingSlots(
                restaurantId, request.getDate(), request.getStartTime(), request.getEndTime(), null
        );
        if (overlaps > 0) {
            throw new OverlappingAvailabilitySlotException("Availability slot overlaps with an existing slot");
        }

        AvailabilitySlot slot = AvailabilitySlotMapper.toEntity(restaurantId, request);
        slot.setSeatsAvailable(request.getCapacity());
        return AvailabilitySlotMapper.toResponse(availabilitySlotRepository.save(slot));
    }

    @Transactional(readOnly = true)
    public List<AvailabilitySlotResponse> listAvailabilitySlots(Long restaurantId, LocalDate date) {
        getRestaurantById(restaurantId);

        List<AvailabilitySlot> slots = (date == null)
                ? availabilitySlotRepository.findByRestaurantId(restaurantId)
                : availabilitySlotRepository.findByRestaurantIdAndDate(restaurantId, date);

        return slots.stream().map(AvailabilitySlotMapper::toResponse).toList();
    }

    @Transactional
    public AvailabilitySlotResponse updateAvailabilitySlot(Long restaurantId, Long slotId, AvailabilitySlotRequest request) {
        getRestaurantById(restaurantId);

        validateSlotTimes(request.getDate(), request.getStartTime(), request.getEndTime(), request.getCapacity());

        AvailabilitySlot slot = availabilitySlotRepository.findById(slotId)
                .filter(s -> s.getRestaurantId().equals(restaurantId))
                .orElseThrow(() -> new AvailabilitySlotNotFoundException("Slot not found with id: " + slotId));

        long overlaps = availabilitySlotRepository.countOverlappingSlots(
                restaurantId, request.getDate(), request.getStartTime(), request.getEndTime(), slotId
        );
        if (overlaps > 0) {
            throw new OverlappingAvailabilitySlotException("Availability slot overlaps with an existing slot");
        }

        int oldCapacity = slot.getCapacity();
        int oldSeatsAvailable = slot.getSeatsAvailable();
        int reserved = oldCapacity - oldSeatsAvailable;

        slot.setDate(request.getDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setCapacity(request.getCapacity());

        int newSeatsAvailable = request.getCapacity() - reserved;
        if (newSeatsAvailable < 0) {
            throw new DomainValidationException("Cannot reduce capacity below already reserved seats");
        }
        slot.setSeatsAvailable(newSeatsAvailable);

        return AvailabilitySlotMapper.toResponse(availabilitySlotRepository.save(slot));
    }

    @Transactional
    public void deleteAvailabilitySlot(Long restaurantId, Long slotId) {
        getRestaurantById(restaurantId);
        AvailabilitySlot slot = availabilitySlotRepository.findById(slotId)
                .filter(s -> s.getRestaurantId().equals(restaurantId))
                .orElseThrow(() -> new AvailabilitySlotNotFoundException("Slot not found with id: " + slotId));
        availabilitySlotRepository.delete(slot);
    }

    @Transactional(readOnly = true)
    public List<AvailabilitySlotResponse> getAvailabilityForPartySize(Long restaurantId, LocalDate date, int partySize) {
        getRestaurantById(restaurantId);

        if (partySize <= 0) {
            throw new IllegalArgumentException("partySize must be greater than zero");
        }

        return availabilitySlotRepository.findAvailableSlots(restaurantId, date, partySize).stream()
                .map(AvailabilitySlotMapper::toResponse)
                .toList();
    }

    @Transactional
    public void releaseSeats(Long restaurantId, ReleaseSeatsRequest request) {
        getRestaurantById(restaurantId);

        if (request.getPartySize() <= 0) {
            throw new IllegalArgumentException("partySize must be greater than zero");
        }
        if (request.getScheduledAt() == null) {
            throw new IllegalArgumentException("scheduledAt is required");
        }

        LocalDate date = request.getScheduledAt().toLocalDate();
        LocalTime startTime = request.getScheduledAt().toLocalTime();

        AvailabilitySlot slot = availabilitySlotRepository
                .findForUpdateByRestaurantIdAndDateAndStartTime(restaurantId, date, startTime)
                .orElseThrow(() -> new AvailabilitySlotNotFoundException(
                        "No availability slot found for scheduledAt=" + request.getScheduledAt()
                ));

        int newSeats = slot.getSeatsAvailable() + request.getPartySize();
        if (newSeats > slot.getCapacity()) {
            newSeats = slot.getCapacity();
        }
        slot.setSeatsAvailable(newSeats);

        availabilitySlotRepository.save(slot);
    }

    private void validateSlotTimes(LocalDate date, LocalTime start, LocalTime end, int capacity) {
        if (date == null || start == null || end == null) {
            throw new DomainValidationException("date/startTime/endTime are required");
        }
        if (!end.isAfter(start)) {
            throw new DomainValidationException("endTime must be after startTime");
        }
        if (capacity <= 0) {
            throw new DomainValidationException("capacity must be positive");
        }

        LocalDateTime startsAt = LocalDateTime.of(date, start);
        if (!startsAt.isAfter(LocalDateTime.now())) {
            throw new DomainValidationException("slot start must be in the future");
        }
    }
}
