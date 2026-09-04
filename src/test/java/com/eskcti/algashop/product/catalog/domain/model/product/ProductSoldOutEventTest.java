package com.eskcti.algashop.product.catalog.domain.model.product;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductSoldOutEventTest {

    @Test
    void shouldCreateEventWithProductIdAndTimestamp() {
        UUID productId = UUID.randomUUID();

        ProductSoldOutEvent event = ProductSoldOutEvent.builder()
                .productId(productId)
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getSoldOutAt()).isNotNull();
        assertThat(event.getSoldOutAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void shouldCreateEventWithCustomTimestamp() {
        UUID productId = UUID.randomUUID();
        OffsetDateTime customTime = OffsetDateTime.now().minusHours(1);

        ProductSoldOutEvent event = ProductSoldOutEvent.builder()
                .productId(productId)
                .soldOutAt(customTime)
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getSoldOutAt()).isEqualTo(customTime);
    }

}
