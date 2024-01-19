package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.entity.ItemEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findAllByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<BookingEntity> findAllByBookerIdOrderByStartDesc(long userId);

    List<BookingEntity> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime  currentTime);

    List<BookingEntity> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime  currentTime);

    @Query("select b from BookingEntity b where b.booker.id = :userId and :currentTime between b.start and b.end order by b.start desc")
    List<BookingEntity> findAllByBookerIdAndCurrentTimeBetweenStartAndEnd(long userId, LocalDateTime  currentTime);

    List<BookingEntity> findAllByItemOwnerIdOrderByStartDesc(long userId);

    List<BookingEntity> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime  currentTime);

    List<BookingEntity> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime  currentTime);

    @Query("select b from BookingEntity b where b.item.owner.id = :userId and :currentTime between b.start and b.end order by b.start desc")
    List<BookingEntity> findAllByItemOwnerIdAndCurrentTimeBetweenStartAndEnd(long userId, LocalDateTime  currentTime);

    List<BookingEntity> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    Optional<BookingEntity> findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(ItemEntity item, LocalDateTime time, BookingStatus bookingStatus);

    Optional<BookingEntity> findFirstByItemAndStartAfterAndStatusOrderByStart(ItemEntity item, LocalDateTime time, BookingStatus bookingStatus);

    Optional<BookingEntity> findFirstByBookerIdAndItemIdAndEndBefore(long bookerId, long itemId, LocalDateTime time);
}
