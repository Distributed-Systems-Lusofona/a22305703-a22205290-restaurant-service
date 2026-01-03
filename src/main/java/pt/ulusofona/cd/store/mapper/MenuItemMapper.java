package pt.ulusofona.cd.store.mapper;

import pt.ulusofona.cd.store.dto.MenuItemRequest;
import pt.ulusofona.cd.store.dto.MenuItemResponse;
import pt.ulusofona.cd.store.model.MenuItem;

public class MenuItemMapper {

    private MenuItemMapper() {
    }

    public static MenuItem toEntity(Long restaurantId, MenuItemRequest dto) {
        MenuItem m = new MenuItem();
        m.setRestaurantId(restaurantId);

        m.setName(dto.getName() == null ? null : dto.getName().trim());
        m.setDescription(dto.getDescription() == null ? null : dto.getDescription().trim());
        m.setPrice(dto.getPrice());
        m.setCurrency(dto.getCurrency() == null ? null : dto.getCurrency().trim().toUpperCase());
        m.setActive(dto.isActive());

        return m;
    }

    public static MenuItemResponse toResponse(MenuItem entity) {
        MenuItemResponse r = new MenuItemResponse();
        r.setId(entity.getId());
        r.setRestaurantId(entity.getRestaurantId());
        r.setName(entity.getName());
        r.setDescription(entity.getDescription());
        r.setPrice(entity.getPrice());
        r.setCurrency(entity.getCurrency());
        r.setActive(entity.isActive());
        return r;
    }
}
