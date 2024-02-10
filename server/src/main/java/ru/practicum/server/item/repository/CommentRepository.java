package ru.practicum.server.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.item.entity.CommentEntity;
import ru.practicum.server.item.entity.ItemEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByItem(ItemEntity itemEntity);
}
