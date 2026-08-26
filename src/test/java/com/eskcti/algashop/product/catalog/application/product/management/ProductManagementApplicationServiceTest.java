package com.eskcti.algashop.product.catalog.application.product.management;

import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductManagementApplicationServiceTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final ProductManagementApplicationService service =
            new ProductManagementApplicationService(productRepository, categoryRepository);

    @Test
    void shouldCreateProductAndReturnGeneratedId() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category("Notebook", true);
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        ProductInput input = ProductInput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .categoryId(categoryId)
                .description("A Gamer Notebook")
                .build();

        UUID createdId = service.create(input);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).save(captor.capture());
        assertThat(createdId).isEqualTo(captor.getValue().getId());
        assertThat(captor.getValue().getName()).isEqualTo("Notebook X11");
        assertThat(captor.getValue().getBrand()).isEqualTo("Deep Diver");
        assertThat(captor.getValue().getDescription()).isEqualTo("A Gamer Notebook");
    }

    @Test
    void shouldThrowWhenCreatingProductWithNonExistentCategory() {
        UUID categoryId = UUID.randomUUID();
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        ProductInput input = ProductInput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .categoryId(categoryId)
                .build();

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(CategoryNotFoundException.class);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
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
