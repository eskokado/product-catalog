package com.eskcti.algashop.product.catalog.application.product.query;

import com.eskcti.algashop.product.catalog.application.utility.Mapper;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductRepository;
import com.eskcti.algashop.product.catalog.infrastructure.persistence.product.ProductQueryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductQueryServiceImplTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final Mapper mapper = Mockito.mock(Mapper.class);
    private final ProductQueryServiceImpl service = new ProductQueryServiceImpl(productRepository, mapper);

    @Test
    void shouldFilterReturningNullUntilPersistenceIsImplemented() {
        assertThat(service.filter(10, 0)).isNull();
    }

    @Test
    void shouldFindByIdReturningMappedOutput() {
        UUID productId = UUID.randomUUID();
        Category category = new Category("Electronics", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new java.math.BigDecimal("1500.00"))
                .salePrice(new java.math.BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();
        ProductDetailOutput output = ProductDetailOutputTestDataBuilder.aProduct()
                .id(productId)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(mapper.convert(product, ProductDetailOutput.class)).thenReturn(output);

        assertThat(service.findById(productId)).isEqualTo(output);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(productId))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
