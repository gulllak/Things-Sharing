package ru.practicum.shareit.request.dto;

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
class RequestItemRequestDtoTest {
    @Autowired
    private JacksonTester<RequestItemRequestDto> json;

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
        String jsonString = "{\"description\":\"Описание запроса\"}";

        RequestItemRequestDto dto = json.parse(jsonString).getObject();

        assertThat(dto.getDescription()).isEqualTo("Описание запроса");
    }

    @Test
    public void testValidation() {
        RequestItemRequestDto dto = new RequestItemRequestDto();
        dto.setDescription("");

        Set<ConstraintViolation<RequestItemRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Описание вещи не может быть пустым"))).isTrue();
    }

}