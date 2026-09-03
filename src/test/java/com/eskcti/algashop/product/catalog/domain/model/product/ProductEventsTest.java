package com.eskcti.algashop.product.catalog.domain.model.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEventsTest {

    @Test
    void shouldCreateProductAddedEvent() {
        UUID productId = UUID.randomUUID();
        ProductAddedEvent event = ProductAddedEvent.builder()
                .productId(productId)
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getAddedAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void shouldCreateProductDelistedEvent() {
        UUID productId = UUID.randomUUID();
        ProductDelistedEvent event = ProductDelistedEvent.builder()
                .productId(productId)
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getDelistedAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void shouldCreateProductListedEvent() {
        UUID productId = UUID.randomUUID();
        ProductListedEvent event = ProductListedEvent.builder()
                .productId(productId)
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getListedAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void shouldCreateProductPriceChangedEvent() {
        UUID productId = UUID.randomUUID();
        ProductPriceChangedEvent event = ProductPriceChangedEvent.builder()
                .productId(productId)
                .oldRegularPrice(new BigDecimal("1500.00"))
                .oldSalePrice(new BigDecimal("1000.00"))
                .newRegularPrice(new BigDecimal("1600.00"))
                .newSalePrice(new BigDecimal("1100.00"))
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getOldRegularPrice()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(event.getOldSalePrice()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(event.getNewRegularPrice()).isEqualByComparingTo(new BigDecimal("1600.00"));
        assertThat(event.getNewSalePrice()).isEqualByComparingTo(new BigDecimal("1100.00"));
        assertThat(event.getChangedAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void shouldCreateProductPlacedOnSaleEvent() {
        UUID productId = UUID.randomUUID();
        ProductPlacedOnSaleEvent event = ProductPlacedOnSaleEvent.builder()
                .productId(productId)
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .build();

        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getRegularPrice()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(event.getSalePrice()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(event.getPlacedOnSaleAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }
}
