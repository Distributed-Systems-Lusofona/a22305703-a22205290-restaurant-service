package pt.ulusofona.cd.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ulusofona.cd.store.client.SupplierClient;
import pt.ulusofona.cd.store.dto.RestaurantRequest;
import pt.ulusofona.cd.store.dto.SupplierDto;
import pt.ulusofona.cd.store.exception.RestaurantNotFoundException;
import pt.ulusofona.cd.store.mapper.RMapper;
import pt.ulusofona.cd.store.model.Restaurant;
import pt.ulusofona.cd.store.repository.RestaurantRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final SupplierClient supplierClient;

    @Transactional
    public Restaurant createRestaurant(RestaurantRequest request) {
        if (request.getSupplierId() != null) {
            try {
                SupplierDto supplier = supplierClient.getSupplierById(request.getSupplierId().toString());
                if (!supplier.isActive()) {
                    throw new IllegalArgumentException("Supplier is not active");
                }
            } catch (Exception e) {
                System.out.println(e);
                throw new IllegalArgumentException("Invalid supplier ID: " + request.getSupplierId());
            }
        }
        Restaurant restaurant = RMapper.toEntity(request);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant getProductById(UUID id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getProductsBySupplier(UUID supplierId) {
        return restaurantRepository.findBySupplierId(supplierId);
    }

    @Transactional
    public Restaurant updateRestaurant(UUID id, RestaurantRequest productDetails) {
        Restaurant restaurant = getProductById(id);

        restaurant.setName(productDetails.getName());
        restaurant.setDescription(productDetails.getDescription());
        restaurant.setSku(productDetails.getSku());
        restaurant.setPrice(productDetails.getPrice());
        restaurant.setStock(productDetails.getStock());
        restaurant.setCurrency(productDetails.getCurrency());
        restaurant.setDiscontinued(productDetails.isDiscontinued());

        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void deleteRestaurant(UUID id) {
        Restaurant restaurant = getProductById(id);
        restaurantRepository.delete(restaurant);
    }

    public int setProductsInactiveBySupplierId(String id){
        UUID supplierUuid = UUID.fromString(id);
        return restaurantRepository.setProductsInactiveBySupplierId(supplierUuid);
    }

    @Transactional()
    public long countProductsBySupplierAndDiscontinued(UUID supplierId, boolean isDiscontinued) {
        return restaurantRepository.countBySupplierIdAndIsDiscontinued(supplierId, isDiscontinued);
    }
}