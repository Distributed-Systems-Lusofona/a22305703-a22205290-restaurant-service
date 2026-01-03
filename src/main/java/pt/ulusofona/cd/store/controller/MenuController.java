package pt.ulusofona.cd.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ulusofona.cd.store.dto.MenuItemRequest;
import pt.ulusofona.cd.store.dto.MenuItemResponse;
import pt.ulusofona.cd.store.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menu")
@RequiredArgsConstructor
public class MenuController {

    private final RestaurantService service;

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequest request
    ) {
        MenuItemResponse created = service.createMenuItem(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> list(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(service.getMenuItems(restaurantId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<MenuItemResponse> get(
            @PathVariable Long restaurantId,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(service.getMenuItem(restaurantId, itemId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable Long restaurantId,
            @PathVariable Long itemId,
            @Valid @RequestBody MenuItemRequest request
    ) {
        return ResponseEntity.ok(service.updateMenuItem(restaurantId, itemId, request));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long restaurantId,
            @PathVariable Long itemId
    ) {
        service.deleteMenuItem(restaurantId, itemId);
        return ResponseEntity.noContent().build();
    }
}
