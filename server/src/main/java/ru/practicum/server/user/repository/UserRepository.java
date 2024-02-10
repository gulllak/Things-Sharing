package ru.practicum.server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
