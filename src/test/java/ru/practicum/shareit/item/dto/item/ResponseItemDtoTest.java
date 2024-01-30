package ru.practicum.shareit.item.dto.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ResponseItemDtoTest {
    @Autowired
    JacksonTester<ResponseItemDto> json;

    @Test
    void testSerialize() throws Exception {
        ResponseItemDto itemDto = new ResponseItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item Name");
        itemDto.setDescription("Description of the item");
        itemDto.setAvailable(true);
        itemDto.setLastBooking(null);
        itemDto.setNextBooking(null);
        itemDto.setComments(null);
        itemDto.setRequestId(5L);

        JsonContent<ResponseItemDto> jsonContent = json.write(itemDto);

        assertThat(jsonContent).hasJsonPathNumberValue("@.id");
        assertThat(jsonContent).extractingJsonPathStringValue("@.name").isEqualTo("Item Name");
        assertThat(jsonContent).extractingJsonPathStringValue("@.description").isEqualTo("Description of the item");
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.lastBooking").isEqualTo(null);
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.nextBooking").isEqualTo(null);
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.comments").isEqualTo(null);
        assertThat(jsonContent).hasJsonPathNumberValue("@.requestId");
    }



}