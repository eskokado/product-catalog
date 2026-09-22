package com.eskcti.algashop.product.catalog.application.product.query;

import com.eskcti.algashop.product.catalog.application.utility.Mapper;
import com.eskcti.algashop.product.catalog.domain.model.DomainEntityNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.product.Image;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductImageQueryServiceTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final Mapper mapper = Mockito.mock(Mapper.class);
    private final ProductImageQueryService service =
            new ProductImageQueryService(productRepository, mapper);

    private Product product;
    private UUID firstImageId;
    private UUID secondImageId;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(new Category("Notebook", true))
                .build();
        firstImageId = product.addImage("photo.png");
        secondImageId = product.addImage("other.png");

        Mockito.when(mapper.convert(Mockito.any(Image.class), Mockito.eq(ImageOutput.class)))
                .thenAnswer(invocation -> {
                    Image image = invocation.getArgument(0);
                    ImageOutput output = new ImageOutput();
                    output.setId(image.getId());
                    output.setUrl("http://localhost:4566/" + image.getName());
                    return output;
                });
    }

    @Test
    void shouldGetAllImagesMappedToOutput() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        List<ImageOutput> result = service.getAllImages(productId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ImageOutput::getId)
                .containsExactlyInAnyOrder(firstImageId, secondImageId);
        assertThat(result).extracting(ImageOutput::getUrl)
                .containsExactlyInAnyOrder(
                        "http://localhost:4566/photo.png",
                        "http://localhost:4566/other.png");
    }

    @Test
    void shouldThrowWhenGettingAllImagesOfUnknownProduct() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAllImages(productId))
                .isInstanceOf(ProductNotFoundException.class);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void shouldGetOneImageById() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ImageOutput result = service.getImage(productId, secondImageId);

        assertThat(result.getId()).isEqualTo(secondImageId);
        assertThat(result.getUrl()).isEqualTo("http://localhost:4566/other.png");
    }

    @Test
    void shouldThrowWhenGettingUnknownImage() {
        UUID productId = UUID.randomUUID();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        UUID unknownImageId = UUID.randomUUID();

        assertThatThrownBy(() -> service.getImage(productId, unknownImageId))
                .isInstanceOf(DomainEntityNotFoundException.class);
    }
}
