package com.eskcti.algashop.product.catalog.domain.model.product;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRestockedEventTest {

    @Test
    void shouldCreateEventWithProductIdAndTimestamp() {
        UUID productId = UUID.randomUUID();

        ProductRestockedEvent event = ProductRestockedEvent.builder()
                .productId(productId)
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getRestockedAt()).isNotNull();
        assertThat(event.getRestockedAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void shouldCreateEventWithCustomTimestamp() {
        UUID productId = UUID.randomUUID();
        OffsetDateTime customTime = OffsetDateTime.now().minusHours(1);

        ProductRestockedEvent event = ProductRestockedEvent.builder()
                .productId(productId)
                .restockedAt(customTime)
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getRestockedAt()).isEqualTo(customTime);
    }

}
