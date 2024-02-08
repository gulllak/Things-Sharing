package ru.practicum.server.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.server.item.entity.ItemEntity;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findAllByOwnerIdOrderById(long id, Pageable pageable);

    @Query("select i from ItemEntity i " +
            "where (upper(i.name) like upper(concat('%', ?1,'%')) or " +
            "upper(i.description) like upper(concat('%', ?1,'%'))) and " +
            "i.available = true")
    List<ItemEntity> search(String searchString, Pageable pageable);

    List<ItemEntity> findAllByRequestId(long requestId);
}
