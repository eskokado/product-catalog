package com.eskcti.algashop.product.catalog.application.product.management;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ProductManagementApplicationServiceTest {

    private final ProductManagementApplicationService service = new ProductManagementApplicationService();

    @Test
    void shouldCreateProductReturningNullUntilPersistenceIsImplemented() {
        ProductInput input = ProductInput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .categoryId(UUID.randomUUID())
                .description("A Gamer Notebook")
                .build();

        assertThat(service.create(input)).isNull();
    }

    @Test
    void shouldAcceptUpdateWithoutSideEffectsUntilPersistenceIsImplemented() {
        UUID productId = UUID.randomUUID();
        ProductInput input = ProductInput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .categoryId(UUID.randomUUID())
                .build();

        assertThatCode(() -> service.update(productId, input)).doesNotThrowAnyException();
    }

    @Test
    void shouldAcceptDisableWithoutSideEffectsUntilPersistenceIsImplemented() {
        assertThatCode(() -> service.disable(UUID.randomUUID())).doesNotThrowAnyException();
    }
}
