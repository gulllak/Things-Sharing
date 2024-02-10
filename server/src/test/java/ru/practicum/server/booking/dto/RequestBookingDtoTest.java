package ru.practicum.server.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestBookingDtoTest {
    @Autowired
    private JacksonTester<RequestBookingDto> json;

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
}