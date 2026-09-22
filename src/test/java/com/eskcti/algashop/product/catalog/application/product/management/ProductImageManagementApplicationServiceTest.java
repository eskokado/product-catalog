package com.eskcti.algashop.product.catalog.application.product.management;

import com.eskcti.algashop.product.catalog.application.product.query.ImageOutput;
import com.eskcti.algashop.product.catalog.application.storage.StorageProvider;
import com.eskcti.algashop.product.catalog.application.utility.Mapper;
import com.eskcti.algashop.product.catalog.domain.model.DomainException;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.product.Image;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductImageManagementApplicationServiceTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final StorageProvider storageProvider = Mockito.mock(StorageProvider.class);
    private final Mapper mapper = Mockito.mock(Mapper.class);
    private final ProductImageManagementApplicationService service =
            new ProductImageManagementApplicationService(productRepository, storageProvider, mapper);

    private ImageInput input(String remoteFileName) {
        ImageInput input = new ImageInput();
        input.setRemoteFileName(remoteFileName);
        return input;
    }

    private Product productWithId() {
        Category category = new Category("Notebook", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();
        return product;
    }

    @Test
    void shouldCreateImageAndReturnOutput() {
        UUID productId = UUID.randomUUID();
        Product product = productWithId();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(storageProvider.fileExists("photo.png")).thenReturn(true);
        Mockito.when(productRepository.existsByImagesName("photo.png")).thenReturn(false);
        ImageOutput output = new ImageOutput();
        Mockito.when(mapper.convert(Mockito.any(Image.class), Mockito.eq(ImageOutput.class)))
                .thenReturn(output);

        ImageOutput result = service.create(productId, input("photo.png"));

        assertThat(result).isSameAs(output);
        assertThat(product.getImages()).hasSize(1);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).save(captor.capture());
        assertThat(captor.getValue()).isSameAs(product);
    }

    @Test
    void shouldThrowWhenCreatingImageForUnknownProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(productId, input("photo.png")))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verifyNoInteractions(storageProvider);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenFileDoesNotExistOnStorage() {
        UUID productId = UUID.randomUUID();
        Product product = productWithId();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(storageProvider.fileExists("photo.png")).thenReturn(false);

        assertThatThrownBy(() -> service.create(productId, input("photo.png")))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("photo.png")
                .hasMessageContaining("was not found on storage provider");
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenImageNameIsAlreadyInUse() {
        UUID productId = UUID.randomUUID();
        Product product = productWithId();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(storageProvider.fileExists("photo.png")).thenReturn(true);
        Mockito.when(productRepository.existsByImagesName("photo.png")).thenReturn(true);

        assertThatThrownBy(() -> service.create(productId, input("photo.png")))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("photo.png")
                .hasMessageContaining("already in use");
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenCreatingWithNullProductId() {
        assertThatThrownBy(() -> service.create(null, input("photo.png")))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenCreatingWithNullInput() {
        assertThatThrownBy(() -> service.create(UUID.randomUUID(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldDeleteImageAndRemoveFileFromStorage() {
        UUID productId = UUID.randomUUID();
        Product product = productWithId();
        UUID imageId = product.addImage("photo.png");
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        service.delete(productId, imageId);

        assertThat(product.getImages()).isEmpty();
        Mockito.verify(storageProvider).deleteFile("photo.png");
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void shouldThrowWhenDeletingImageOfUnknownProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(productId, UUID.randomUUID()))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verifyNoInteractions(storageProvider);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenDeletingUnknownImage() {
        UUID productId = UUID.randomUUID();
        Product product = productWithId();
        UUID imageId = product.addImage("photo.png");
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        UUID unknownImageId = UUID.randomUUID();

        assertThatThrownBy(() -> service.delete(productId, unknownImageId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(unknownImageId.toString());
        Mockito.verifyNoInteractions(storageProvider);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenDeletingWithNullProductId() {
        assertThatThrownBy(() -> service.delete(null, UUID.randomUUID()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenDeletingWithNullImageId() {
        assertThatThrownBy(() -> service.delete(UUID.randomUUID(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldSetImageAsPrimary() {
        UUID productId = UUID.randomUUID();
        Product product = productWithId();
        UUID firstImageId = product.addImage("photo.png");
        UUID secondImageId = product.addImage("other.png");
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        service.primary(productId, secondImageId);

        assertThat(product.getMainImage().getId()).isEqualTo(secondImageId);
        assertThat(product.getMainImage().getId()).isNotEqualTo(firstImageId);
        Mockito.verify(productRepository).save(product);
    }

    @Test
    void shouldThrowWhenSettingPrimaryOnUnknownProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.primary(productId, UUID.randomUUID()))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenSettingPrimaryToUnknownImage() {
        UUID productId = UUID.randomUUID();
        Product product = productWithId();
        product.addImage("photo.png");
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        UUID unknownImageId = UUID.randomUUID();

        assertThatThrownBy(() -> service.primary(productId, unknownImageId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(unknownImageId.toString());
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowWhenSettingPrimaryWithNullProductId() {
        assertThatThrownBy(() -> service.primary(null, UUID.randomUUID()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenSettingPrimaryWithNullImageId() {
        assertThatThrownBy(() -> service.primary(UUID.randomUUID(), null))
                .isInstanceOf(NullPointerException.class);
    }
}
