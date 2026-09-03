package com.eskcti.algashop.product.catalog.infrastructure.listener.product;

import com.eskcti.algashop.product.catalog.domain.model.product.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

class ProductEventListenerTest {

    private final ProductEventListener listener = new ProductEventListener();

    @Test
    void shouldHandleProductAddedEvent() {
        ProductAddedEvent event = ProductAddedEvent.builder()
                .productId(UUID.randomUUID())
                .build();

        assertThatCode(() -> listener.handle(event)).doesNotThrowAnyException();
    }

    @Test
    void shouldHandleProductDelistedEvent() {
        ProductDelistedEvent event = ProductDelistedEvent.builder()
                .productId(UUID.randomUUID())
                .build();

        assertThatCode(() -> listener.handle(event)).doesNotThrowAnyException();
    }

    @Test
    void shouldHandleProductListedEvent() {
        ProductListedEvent event = ProductListedEvent.builder()
                .productId(UUID.randomUUID())
                .build();

        assertThatCode(() -> listener.handle(event)).doesNotThrowAnyException();
    }

    @Test
    void shouldHandleProductPriceChangedEvent() {
        ProductPriceChangedEvent event = ProductPriceChangedEvent.builder()
                .productId(UUID.randomUUID())
                .oldRegularPrice(new BigDecimal("1500.00"))
                .oldSalePrice(new BigDecimal("1000.00"))
                .newRegularPrice(new BigDecimal("1600.00"))
                .newSalePrice(new BigDecimal("1100.00"))
                .build();

        assertThatCode(() -> listener.handle(event)).doesNotThrowAnyException();
    }

    @Test
    void shouldHandleProductPlacedOnSaleEvent() {
        ProductPlacedOnSaleEvent event = ProductPlacedOnSaleEvent.builder()
                .productId(UUID.randomUUID())
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .build();

        assertThatCode(() -> listener.handle(event)).doesNotThrowAnyException();
    }
}
