package com.eskcti.algashop.product.catalog.infrastructure.utility.mapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationMappingProperyTest {

    @Test
    void shouldStoreImageStorageUrl() {
        ApplicationMappingPropery property = new ApplicationMappingPropery();

        property.setImageStorageUrl("http://localhost:4566/algashop-product-image");

        assertThat(property.getImageStorageUrl())
                .isEqualTo("http://localhost:4566/algashop-product-image");
    }
}
