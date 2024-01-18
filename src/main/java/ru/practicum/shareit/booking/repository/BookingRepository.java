package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime  currentTime);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime  currentTime);

    @Query("select b from Booking b where b.booker.id = :userId and :currentTime between b.start and b.end order by b.start desc")
    List<Booking> findAllByBookerIdAndCurrentTimeBetweenStartAndEnd(long userId, LocalDateTime  currentTime);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long userId);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime  currentTime);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime  currentTime);

    @Query("select b from Booking b where b.item.owner.id = :userId and :currentTime between b.start and b.end order by b.start desc")
    List<Booking> findAllByItemOwnerIdAndCurrentTimeBetweenStartAndEnd(long userId, LocalDateTime  currentTime);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, BookingStatus status);

    Booking findFirstByItemAndStartBeforeOrderByStartDesc(Item item, LocalDateTime time);

    Booking findFirstByItemAndStartAfterOrderByStart(Item item, LocalDateTime time);
}
