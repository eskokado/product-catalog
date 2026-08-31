package com.eskcti.algashop.product.catalog.application.product.query;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductSummaryOutputTest {

    @Test
    void shouldReturnSlugFromName() {
        ProductSummaryOutput output = ProductSummaryOutput.builder()
                .name("Notebook X11")
                .build();

        assertThat(output.getSlug()).isEqualTo("notebook-x11");
    }

    @Test
    void shouldReturnNullSlugWhenNameIsNull() {
        ProductSummaryOutput output = ProductSummaryOutput.builder().build();

        assertThat(output.getSlug()).isNull();
    }
}
