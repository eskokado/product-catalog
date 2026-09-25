package com.eskcti.algashop.product.catalog.infrastructure.utility.mapper;

import com.eskcti.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.eskcti.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.eskcti.algashop.product.catalog.application.utility.Mapper;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ModelMapperConfigTest {

    private static final String IMAGE_STORAGE_URL = "http://localhost:4566/algashop-product-image";

    private final ApplicationMappingPropery applicationMappingPropery = new ApplicationMappingPropery();
    private final ModelMapperConfig config = new ModelMapperConfig();

    private Mapper mapper;

    @BeforeEach
    void setUp() {
        applicationMappingPropery.setImageStorageUrl(IMAGE_STORAGE_URL);
        ReflectionTestUtils.setField(config, "applicationMappingPropery", applicationMappingPropery);
        mapper = config.mapper();
    }

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

    @Test
    void shouldMapProductToSummaryOutputAbbreviatingDescriptionAndBuildingImageUrl() {
        Category category = new Category("Electronics", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .description("A Gamer Notebook with a very long description that must be abbreviated to fifty characters")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();
        product.addImage("photo.png");

        ProductSummaryOutput output = mapper.convert(product, ProductSummaryOutput.class);

        assertThat(output.getShortDescription()).hasSize(50).endsWith("...");
        assertThat(output.getMainImage().getId()).isEqualTo(product.getMainImage().getId());
        assertThat(output.getMainImage().getUrl())
                .isEqualTo(IMAGE_STORAGE_URL + "/photo.png");
    }

    @Test
    void shouldReturnNullUrlWhenFileNameIsBlank() {
        Object url = ReflectionTestUtils.invokeMethod(config, "convertFromFileNameToUrl", " ");

        assertThat(url).isNull();
    }
}
