package com.eskcti.algashop.product.catalog.presentation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductQuantityModelTest {

    @Test
    void shouldCreateModelWithQuantity() {
        ProductQuantityModel model = new ProductQuantityModel();
        model.setQuantity(10);

        assertThat(model.getQuantity()).isEqualTo(10);
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        ProductQuantityModel model1 = new ProductQuantityModel();
        model1.setQuantity(10);

        ProductQuantityModel model2 = new ProductQuantityModel();
        model2.setQuantity(10);

        assertThat(model1).isEqualTo(model2);
        assertThat(model1.hashCode()).isEqualTo(model2.hashCode());
    }

    @Test
    void shouldHaveToString() {
        ProductQuantityModel model = new ProductQuantityModel();
        model.setQuantity(10);

        assertThat(model.toString()).contains("10");
    }
}
