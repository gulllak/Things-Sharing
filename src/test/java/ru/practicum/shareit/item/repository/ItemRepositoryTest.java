package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.entity.ItemEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ItemRepository itemRepository;


    @BeforeEach
    void setUp() {
        ItemEntity item1 = new ItemEntity();
        item1.setName("TestItem1");
        item1.setDescription("Description1");
        item1.setAvailable(true);
        entityManager.persist(item1);

        ItemEntity item2 = new ItemEntity();
        item2.setName("AnotherTestItem2");
        item2.setDescription("Description2");
        item2.setAvailable(true);
        entityManager.persist(item2);

        ItemEntity item3 = new ItemEntity();
        item3.setName("Item3");
        item3.setDescription("Another Description3");
        item3.setAvailable(false);
        entityManager.persist(item3);

        entityManager.flush();
        entityManager.clear();
    }


    @Test
    public void searchShouldReturnAvailableItems() {
        String searchString = "test";
        Pageable pageable = PageRequest.of(0, 10);

        List<ItemEntity> foundItems = itemRepository.search(searchString, pageable);

        assertThat(foundItems).hasSize(2);
        assertThat(foundItems).extracting(ItemEntity::getName).containsExactlyInAnyOrder("TestItem1", "AnotherTestItem2");
    }

    @Test
    public void searchShouldReturnEmptyList() {
        String searchString = "NotExist";
        Pageable pageable = PageRequest.of(0, 10);

        List<ItemEntity> foundItems = itemRepository.search(searchString, pageable);

        assertThat(foundItems).isEmpty();
    }

    @Test
    public void searchShouldNotReturnUnavailableItems() {
        String searchString = "another";
        Pageable pageable = PageRequest.of(0, 10);

        List<ItemEntity> foundItems = itemRepository.search(searchString, pageable);

        assertThat(foundItems).hasSize(1);
        assertThat(foundItems.get(0).getName()).isEqualTo("AnotherTestItem2");
    }
}