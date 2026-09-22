package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.application.product.management.ImageInput;
import com.eskcti.algashop.product.catalog.application.product.management.ProductImageManagementApplicationService;
import com.eskcti.algashop.product.catalog.application.product.query.ImageOutput;
import com.eskcti.algashop.product.catalog.application.product.query.ProductImageQueryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductImagesControllerTest {

    private final ProductImageManagementApplicationService managementService =
            Mockito.mock(ProductImageManagementApplicationService.class);
    private final ProductImageQueryService queryService =
            Mockito.mock(ProductImageQueryService.class);
    private final ProductImagesController controller =
            new ProductImagesController(managementService, queryService);

    @Test
    void shouldDelegateCreateToApplicationService() {
        UUID productId = UUID.randomUUID();
        ImageInput input = new ImageInput();
        input.setRemoteFileName("photo.png");
        ImageOutput output = new ImageOutput();
        Mockito.when(managementService.create(productId, input)).thenReturn(output);

        ImageOutput result = controller.create(productId, input);

        Mockito.verify(managementService).create(productId, input);
        assertThat(result).isSameAs(output);
    }

    @Test
    void shouldDelegateDeleteToApplicationService() {
        UUID productId = UUID.randomUUID();
        UUID imageId = UUID.randomUUID();

        controller.delete(productId, imageId);

        Mockito.verify(managementService).delete(productId, imageId);
    }

    @Test
    void shouldDelegatePrimaryToApplicationService() {
        UUID productId = UUID.randomUUID();
        UUID imageId = UUID.randomUUID();

        controller.primary(productId, imageId);

        Mockito.verify(managementService).primary(productId, imageId);
    }

    @Test
    void shouldDelegateGetAllToQueryService() {
        UUID productId = UUID.randomUUID();
        List<ImageOutput> outputs = List.of(new ImageOutput(), new ImageOutput());
        Mockito.when(queryService.getAllImages(productId)).thenReturn(outputs);

        List<ImageOutput> result = controller.getAll(productId);

        Mockito.verify(queryService).getAllImages(productId);
        assertThat(result).isEqualTo(outputs);
    }

    @Test
    void shouldDelegateGetOneToQueryService() {
        UUID productId = UUID.randomUUID();
        UUID imageId = UUID.randomUUID();
        ImageOutput output = new ImageOutput();
        Mockito.when(queryService.getImage(productId, imageId)).thenReturn(output);

        ImageOutput result = controller.getOne(productId, imageId);

        Mockito.verify(queryService).getImage(productId, imageId);
        assertThat(result).isSameAs(output);
    }
}
