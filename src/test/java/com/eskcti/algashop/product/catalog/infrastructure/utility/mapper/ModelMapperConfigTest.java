package com.eskcti.algashop.product.catalog.infrastructure.utility.mapper;

import com.eskcti.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.eskcti.algashop.product.catalog.application.utility.Mapper;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ModelMapperConfigTest {

    private final Mapper mapper = new ModelMapperConfig().mapper();

    @Test
    void shouldMapProductToDetailOutputWithSlug() {
        Category category = new Category("Electronics", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .description("A Gamer Notebook with long description for testing abbreviation")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();

        ProductDetailOutput output = mapper.convert(product, ProductDetailOutput.class);

        assertThat(output.getSlug()).isEqualTo("notebook-x11");
        assertThat(output.getName()).isEqualTo("Notebook X11");
        assertThat(output.getBrand()).isEqualTo("Deep Diver");
    }
}
