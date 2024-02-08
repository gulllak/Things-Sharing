package ru.practicum.server.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.server.request.entity.ItemRequestEntity;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequestEntity, Long> {
    List<ItemRequestEntity> findAllByRequestorId(Long userId);

    List<ItemRequestEntity> findAllByRequestorIdNot(Long userId, Pageable pageable);
}
