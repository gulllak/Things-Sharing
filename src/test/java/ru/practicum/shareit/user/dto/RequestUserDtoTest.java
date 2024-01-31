package ru.practicum.shareit.user.dto;

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
class RequestUserDtoTest {
    @Autowired
    private JacksonTester<RequestUserDto> json;

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
        String jsonString = "{\"name\":\"Jonh\",\"email\":\"Jonh@mail.ru\"}";

        RequestUserDto dto = json.parse(jsonString).getObject();

        assertThat(dto.getName()).isEqualTo("Jonh");
        assertThat(dto.getEmail()).isEqualTo("Jonh@mail.ru");
    }

    @Test
    public void testValidation() {
        RequestUserDto dto = new RequestUserDto();
        dto.setName("");
        dto.setEmail("invalid-email");

        Set<ConstraintViolation<RequestUserDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
    }






}