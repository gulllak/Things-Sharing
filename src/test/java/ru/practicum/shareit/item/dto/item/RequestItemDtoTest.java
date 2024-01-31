package ru.practicum.shareit.item.dto.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestItemDtoTest {
    @Autowired
    private JacksonTester<RequestItemDto> json;

    @Autowired
    private Validator validator;

    @Configuration
    static class TestConfig {
        @Bean
        public javax.validation.Validator validator() {
            return new LocalValidatorFactoryBean();
        }
    }

    @Test
    public void testDeserialize() throws Exception {
        String jsonString = "{\"name\":\"Дрель\",\"description\":\"Красивая\",\"available\":\"true\"}";

        RequestItemDto dto = json.parse(jsonString).getObject();

        assertThat(dto.getName()).isEqualTo("Дрель");
        assertThat(dto.getDescription()).isEqualTo("Красивая");
        assertThat(dto.getAvailable()).isTrue();
    }

    @Test
    public void testValidation() {
        RequestItemDto dto = new RequestItemDto();
        dto.setName("");
        dto.setDescription("");
        dto.setAvailable(null);

        Set<ConstraintViolation<RequestItemDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Имя вещи не может быть пустым"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Описание вещи не может быть пустым"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Необходимо указать статус доступности товара"))).isTrue();
    }
}