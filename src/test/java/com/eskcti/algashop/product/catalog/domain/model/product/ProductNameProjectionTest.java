package com.eskcti.algashop.product.catalog.domain.model.product;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductNameProjectionTest {

    @Test
    void shouldCreateProjectionWithIdAndName() {
        UUID id = UUID.randomUUID();
        ProductNameProjection projection = new ProductNameProjection(id, "Notebook X11");

        assertThat(projection.id()).isEqualTo(id);
        assertThat(projection.name()).isEqualTo("Notebook X11");
    }
}
