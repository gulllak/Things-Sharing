package ru.practicum.server.item.dto.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ResponseItemForRequestTest {
    @Autowired
    JacksonTester<ResponseItemForRequest> json;

    @Test
    void testSerialize() throws Exception {
        ResponseItemForRequest item = new ResponseItemForRequest();
        item.setId(1L);
        item.setName("Item Name");
        item.setDescription("Description of the item");
        item.setAvailable(true);
        item.setRequestId(5L);

        JsonContent<ResponseItemForRequest> jsonContent = json.write(item);

        assertThat(jsonContent).hasJsonPathNumberValue("@.id");
        assertThat(jsonContent).extractingJsonPathStringValue("@.name").isEqualTo("Item Name");
        assertThat(jsonContent).extractingJsonPathStringValue("@.description").isEqualTo("Description of the item");
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.available").isEqualTo(true);
        assertThat(jsonContent).hasJsonPathNumberValue("@.requestId");
    }
}