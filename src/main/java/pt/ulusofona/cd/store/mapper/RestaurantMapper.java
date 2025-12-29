package pt.ulusofona.cd.store.mapper;

import pt.ulusofona.cd.store.model.Restaurant;
import pt.ulusofona.cd.store.dto.RestaurantRequest;
import pt.ulusofona.cd.store.dto.RestaurantResponse;

public class RestaurantMapper {

    public static Restaurant toEntity(RestaurantRequest dto) {
        Restaurant p = new Restaurant();
        p.setName(dto.getName().trim());
        p.setDescription(dto.getDescription());
        p.setSku(dto.getSku().trim());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setSupplierId(dto.getSupplierId());
        p.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "EUR");
        return p;
    }

    public static RestaurantResponse toResponse(Restaurant entity) {
        RestaurantResponse r = new RestaurantResponse();
        r.setId(entity.getId());
        r.setName(entity.getName());
        r.setDescription(entity.getDescription());
        r.setSku(entity.getSku());
        r.setPrice(entity.getPrice());
        r.setStock(entity.getStock());
        r.setCurrency(entity.getCurrency());
        return r;
    }
}