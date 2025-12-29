package pt.ulusofona.cd.store.repository;


import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.ulusofona.cd.store.model.Restaurant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Optional<Restaurant> findBySku(String sku);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    List<Restaurant> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Restaurant> findBySupplierId(UUID supplierId);

    Long countBySupplierIdAndIsDiscontinued(UUID supplierId, Boolean isDiscontinued);

    @Transactional
    @Modifying
    @Query("UPDATE Restaurant p SET p.isDiscontinued = true WHERE p.supplierId = :supplierId")
    int setProductsInactiveBySupplierId(@Param("supplierId") UUID supplierId);
}
