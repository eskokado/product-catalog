package com.eskcti.algashop.product.catalog.infrastructure.utility;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlgaShopResourceUtilsTest {

    @Test
    void shouldReadExistingResourceContent() {
        String content = AlgaShopResourceUtils.readContent("db/testdata/categories.json");

        assertThat(content).isNotBlank();
        assertThat(content).contains("Laptops");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenResourceNotFound() {
        assertThatThrownBy(() -> AlgaShopResourceUtils.readContent("nonexistent/resource.json"))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(java.io.FileNotFoundException.class);
    }
}
