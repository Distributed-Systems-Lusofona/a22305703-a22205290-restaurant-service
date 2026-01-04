package pt.ulusofona.cd.store.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ulusofona.cd.store.model.AvailabilitySlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByRestaurantId(Long restaurantId);

    List<AvailabilitySlot> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);

    @Query("""
        SELECT s FROM AvailabilitySlot s
        WHERE s.restaurantId = :restaurantId
          AND s.date = :date
          AND s.seatsAvailable >= :partySize
    """)
    List<AvailabilitySlot> findAvailableSlots(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDate date,
            @Param("partySize") int partySize
    );

    @Query("""
        SELECT COUNT(s) FROM AvailabilitySlot s
        WHERE s.restaurantId = :restaurantId
          AND s.date = :date
          AND (:excludeId IS NULL OR s.id <> :excludeId)
          AND :startTime < s.endTime
          AND :endTime > s.startTime
    """)
    long countOverlappingSlots(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Long excludeId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s FROM AvailabilitySlot s
        WHERE s.restaurantId = :restaurantId
          AND s.date = :date
          AND s.startTime = :startTime
    """)
    Optional<AvailabilitySlot> findForUpdateByRestaurantIdAndDateAndStartTime(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s FROM AvailabilitySlot s
        WHERE s.id = :slotId
          AND s.restaurantId = :restaurantId
    """)
    Optional<AvailabilitySlot> findByIdAndRestaurantIdForUpdate(
            @Param("slotId") Long slotId,
            @Param("restaurantId") Long restaurantId
    );


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s FROM AvailabilitySlot s
        WHERE s.restaurantId = :restaurantId
          AND s.date = :date
          AND s.startTime = :startTime
    """)
    Optional<AvailabilitySlot> findSlotForUpdate(
            @Param("restaurantId") Long restaurantId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime
    );
}
