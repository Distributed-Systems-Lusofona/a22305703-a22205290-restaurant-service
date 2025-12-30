package pt.ulusofona.cd.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ulusofona.cd.store.dto.RestaurantRequest;
import pt.ulusofona.cd.store.exception.RestaurantNotFoundException;
import pt.ulusofona.cd.store.mapper.RestaurantMapper;
import pt.ulusofona.cd.store.model.Restaurant;
import pt.ulusofona.cd.store.repository.RestaurantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

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
}
