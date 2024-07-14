package ru.practicum.server.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.server.BaseTest;
import ru.practicum.server.user.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest extends BaseTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void isExistsByEmail() {
        UserEntity userEntity = createUserEntity("test@test.com", "test");
        userRepository.save(userEntity);
        boolean isEmailExists = userRepository.existsByEmail(userEntity.getEmail());
        assertTrue(isEmailExists);
    }

    @Test
    void isNotExistsByEmail() {
        UserEntity userEntity = createUserEntity("test@test.com", "test");
        userRepository.save(userEntity);
        boolean isEmailExists = userRepository.existsByEmail("wrong@test.com");
        assertFalse(isEmailExists);
    }

    private UserEntity createUserEntity(String email, String name) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setName(name);
        return userEntity;
    }
}