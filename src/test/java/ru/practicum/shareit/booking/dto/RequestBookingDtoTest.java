package ru.practicum.shareit.booking.dto;

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
class RequestBookingDtoTest {
    @Autowired
    private JacksonTester<RequestBookingDto> json;

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
        String jsonString = "{\"itemId\":\"1\",\"start\":\"2024-01-29T00:00:00\",\"end\":\"2024-01-30T00:00:00\"}";

        RequestBookingDto dto = json.parse(jsonString).getObject();

        assertThat(dto.getItemId()).isEqualTo(1);
        assertThat(dto.getStart()).isEqualTo("2024-01-29T00:00:00");
        assertThat(dto.getEnd()).isEqualTo("2024-01-30T00:00:00");
    }

    @Test
    public void testValidation() {
        RequestBookingDto dto = new RequestBookingDto();
        dto.setItemId(-1L);
        dto.setStart(null);
        dto.setEnd(null);

        Set<ConstraintViolation<RequestBookingDto>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("id вещи не может быть 0 или меньше"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Время начала аренды не может быть null"))).isTrue();
        assertThat(violations.stream().anyMatch(v -> v.getMessage().contains("Время окончания аренды не может быть null"))).isTrue();
    }
}