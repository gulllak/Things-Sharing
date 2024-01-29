package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookingRepository bookingRepository;

    BookingEntity testBooking;

    @BeforeEach
    void setUp() {
        UserEntity booker = new UserEntity();
        booker.setName("booker");
        booker.setEmail("booker@mail.ru");

        UserEntity owner = new UserEntity();
        owner.setName("owner");
        owner.setEmail("owner@mail.ru");

        ItemEntity item = new ItemEntity();
        item.setName("TestItem");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);

        entityManager.persist(booker);
        entityManager.persist(owner);
        entityManager.persist(item);

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);

        testBooking = new BookingEntity();
        testBooking.setBooker(booker);
        testBooking.setItem(item);
        testBooking.setStart(startTime);
        testBooking.setEnd(endTime);
        testBooking.setStatus(BookingStatus.APPROVED);

        entityManager.persist(testBooking);
        entityManager.flush();
    }

    @Test
    public void findAllByBookerIdAndCurrentTimeBetweenStartAndEnd() {
        long bookerId = testBooking.getBooker().getId();
        LocalDateTime currentTime = testBooking.getStart().plusHours(1);
        Pageable pageable = PageRequest.of(0, 10);

        List<BookingEntity> bookings = bookingRepository.findAllByBookerIdAndCurrentTimeBetweenStartAndEnd(bookerId, currentTime, pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0)).isEqualTo(testBooking);
    }


    @Test
    void findAllByItemOwnerIdAndCurrentTimeBetweenStartAndEnd() {
        long ownerId = testBooking.getItem().getOwner().getId();
        LocalDateTime currentTime = testBooking.getStart().plusHours(1);
        Pageable pageable = PageRequest.of(0, 10);

        List<BookingEntity> bookings = bookingRepository.findAllByItemOwnerIdAndCurrentTimeBetweenStartAndEnd(ownerId, currentTime, pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.get(0)).isEqualTo(testBooking);
    }
}