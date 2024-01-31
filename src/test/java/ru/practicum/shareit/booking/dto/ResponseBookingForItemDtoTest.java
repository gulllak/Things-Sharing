package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ResponseBookingForItemDtoTest {
    @Autowired
    JacksonTester<ResponseBookingForItemDto> json;

    @Test
    void testSerialize() throws Exception {
        ResponseBookingForItemDto dto = new ResponseBookingForItemDto();
        dto.setId(1L);
        dto.setBookerId(2L);

        JsonContent<ResponseBookingForItemDto> jsonContent = json.write(dto);

        assertThat(jsonContent).hasJsonPathNumberValue("@.id");
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(jsonContent).hasJsonPathNumberValue("@.bookerId");
        assertThat(jsonContent).extractingJsonPathNumberValue("@.bookerId").isEqualTo(2);
    }
}