package pt.ulusofona.cd.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ulusofona.cd.store.dto.RestaurantRequest;
import pt.ulusofona.cd.store.dto.RestaurantResponse;
import pt.ulusofona.cd.store.mapper.RestaurantMapper;
import pt.ulusofona.cd.store.model.Restaurant;
import pt.ulusofona.cd.store.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody RestaurantRequest request) {
        Restaurant created = service.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
        Restaurant restaurant = service.getRestaurantById(id);
        return ResponseEntity.ok(RestaurantMapper.toResponse(restaurant));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAll() {
        List<RestaurantResponse> responseList = service.getAllRestaurants()
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request
    ) {
        Restaurant updated = service.updateRestaurant(id, request);
        return ResponseEntity.ok(RestaurantMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
