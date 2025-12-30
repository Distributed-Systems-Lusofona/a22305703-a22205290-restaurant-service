package pt.ulusofona.cd.store.mapper;

import pt.ulusofona.cd.store.dto.RestaurantRequest;
import pt.ulusofona.cd.store.dto.RestaurantResponse;
import pt.ulusofona.cd.store.model.Restaurant;

public class RestaurantMapper {

    public static Restaurant toEntity(RestaurantRequest dto) {
        Restaurant r = new Restaurant();
        r.setName(dto.getName().trim());
        r.setCity(dto.getCity().trim());
        r.setCountry(dto.getCountry().trim());
        r.setPhone(dto.getPhone().trim());
        r.setEmail(dto.getEmail().trim());
        return r;
    }

    public static RestaurantResponse toResponse(Restaurant entity) {
        RestaurantResponse r = new RestaurantResponse();
        r.setId(entity.getId());
        r.setName(entity.getName());
        r.setCity(entity.getCity());
        r.setCountry(entity.getCountry());
        r.setPhone(entity.getPhone());
        r.setEmail(entity.getEmail());
        return r;
    }
}
