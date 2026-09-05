package com.eskcti.algashop.product.catalog.application.product.management;

import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductRepository;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.product.StockMovement;
import com.eskcti.algashop.product.catalog.domain.model.product.StockMovementRepository;
import com.eskcti.algashop.product.catalog.domain.model.product.StockService;
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
    private final StockMovementRepository stockMovementRepository = Mockito.mock(StockMovementRepository.class);
    private final StockService stockService = Mockito.mock(StockService.class);
    private final ProductManagementApplicationService service =
            new ProductManagementApplicationService(productRepository, categoryRepository, stockMovementRepository, stockService);

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
        Category category = new Category("Notebook", true);
        Product product = Product.builder()
                .name("Old Product")
                .brand("Old Brand")
                .regularPrice(new BigDecimal("1000.00"))
                .salePrice(new BigDecimal("800.00"))
                .enabled(true)
                .category(category)
                .build();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(categoryRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(category));

        ProductInput input = ProductInput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .categoryId(UUID.randomUUID())
                .build();

        assertThatCode(() -> service.update(productId, input)).doesNotThrowAnyException();
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void shouldAcceptDisableWithoutSideEffectsUntilPersistenceIsImplemented() {
        UUID productId = UUID.randomUUID();
        Category category = new Category("Notebook", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThatCode(() -> service.disable(productId)).doesNotThrowAnyException();
        assertThat(product.getEnabled()).isFalse();
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void shouldEnableProduct() {
        UUID productId = UUID.randomUUID();
        Category category = new Category("Notebook", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(false)
                .category(category)
                .build();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        service.enable(productId);

        assertThat(product.getEnabled()).isTrue();
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void shouldThrowWhenEnablingNonExistentProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.enable(productId))
                .isInstanceOf(com.eskcti.algashop.product.catalog.domain.model.product.ProductNotFoundException.class);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());
        ProductInput input = ProductInput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .categoryId(UUID.randomUUID())
                .build();

        assertThatThrownBy(() -> service.update(productId, input))
                .isInstanceOf(com.eskcti.algashop.product.catalog.domain.model.product.ProductNotFoundException.class);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenDisablingNonExistentProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.disable(productId))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldRestockProduct() {
        UUID productId = UUID.randomUUID();
        Category category = new Category("Notebook", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();
        Mockito.when(stockService.restock(product, 10)).thenReturn(movement);

        service.restock(productId, 10);

        Mockito.verify(stockService).restock(product, 10);
        ArgumentCaptor<StockMovement> captor = ArgumentCaptor.forClass(StockMovement.class);
        Mockito.verify(stockMovementRepository).save(captor.capture());
        assertThat(captor.getValue().getProductId()).isEqualTo(productId);
        assertThat(captor.getValue().getType()).isEqualTo(StockMovement.MovementType.STOCK_IN);
    }

    @Test
    void shouldThrowWhenRestockingNonExistentProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.restock(productId, 10))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verify(stockService, Mockito.never()).restock(Mockito.any(), Mockito.anyInt());
        Mockito.verify(stockMovementRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldWithdrawProduct() {
        UUID productId = UUID.randomUUID();
        Category category = new Category("Notebook", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(5)
                .previousQuantity(10)
                .newQuantity(5)
                .type(StockMovement.MovementType.STOCK_OUT)
                .build();
        Mockito.when(stockService.withdraw(product, 5)).thenReturn(movement);

        service.withdraw(productId, 5);

        Mockito.verify(stockService).withdraw(product, 5);
        ArgumentCaptor<StockMovement> captor = ArgumentCaptor.forClass(StockMovement.class);
        Mockito.verify(stockMovementRepository).save(captor.capture());
        assertThat(captor.getValue().getProductId()).isEqualTo(productId);
        assertThat(captor.getValue().getType()).isEqualTo(StockMovement.MovementType.STOCK_OUT);
    }

    @Test
    void shouldThrowWhenWithdrawingNonExistentProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.withdraw(productId, 5))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verify(stockService, Mockito.never()).withdraw(Mockito.any(), Mockito.anyInt());
        Mockito.verify(stockMovementRepository, Mockito.never()).save(Mockito.any());
    }
}
