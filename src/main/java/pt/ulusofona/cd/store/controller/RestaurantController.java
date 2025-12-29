package pt.ulusofona.cd.store.controller;

import lombok.RequiredArgsConstructor;
import pt.ulusofona.cd.store.dto.RestaurantRequest;
import pt.ulusofona.cd.store.dto.RestaurantResponse;
import pt.ulusofona.cd.store.mapper.RestaurantMapper;
import pt.ulusofona.cd.store.model.Restaurant;
import pt.ulusofona.cd.store.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(
            @Valid @RequestBody RestaurantRequest request
    ) {
        Restaurant created = service.createRestaurant(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestaurantMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable UUID id) {
        Restaurant restaurant = service.getProductById(id);
        return ResponseEntity.ok(RestaurantMapper.toResponse(restaurant));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAll() {
        List<Restaurant> restaurants = service.getAllRestaurants();
        List<RestaurantResponse> responseList = restaurants.stream()
                .map(RestaurantMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody RestaurantRequest request
    ) {
        Restaurant updated = service.updateRestaurant(id, request);
        return ResponseEntity.ok(RestaurantMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}