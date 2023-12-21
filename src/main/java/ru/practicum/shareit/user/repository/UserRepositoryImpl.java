package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateResourceException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private long id = 0;
    private static final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        if (existUserWithEmail(user.getId(), user.getEmail())) {
            throw new DuplicateResourceException("Пользователь с таким email уже существует");
        }

        user.setId(getNextId());
        users.put(user.getId(), user);

        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
        if (!existUser(user.getId())) {
            throw new EntityNotFoundException(String.format("Пользователя с id = %d не существует", user.getId()));
        }
        if (existUserWithEmail(user.getId(), user.getEmail())) {
            throw new DuplicateResourceException("Пользователь с таким email уже существует");
        }

        User updatedUser = users.get(user.getId());
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }

        users.put(updatedUser.getId(), updatedUser);
        return users.get(updatedUser.getId());
    }

    @Override
    public User getById(long id) {
        if (!existUser(id)) {
            throw new EntityNotFoundException(String.format("Пользователя с id = %d не существует", id));
        }
        return users.get(id);
    }

    @Override
    public void delete(long id) {
        if (!existUser(id)) {
            throw new EntityNotFoundException(String.format("Пользователя с id = %d не существует", id));
        }

        users.remove(id);
    }

    private long getNextId() {
        return ++id;
    }

    private boolean existUserWithEmail(long id, String email) {
        return users.values().stream()
                .filter(user -> user.getId() != id)
                .map(User::getEmail)
                .anyMatch(e -> e.equals(email));
    }

    private boolean existUser(long id) {
        return users.containsKey(id);
    }
}
