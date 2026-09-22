package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.application.product.management.ImageInput;
import com.eskcti.algashop.product.catalog.application.product.management.ProductImageManagementApplicationService;
import com.eskcti.algashop.product.catalog.application.product.query.ImageOutput;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductImagesControllerTest {

    private final ProductImageManagementApplicationService managementService =
            Mockito.mock(ProductImageManagementApplicationService.class);
    private final ProductImagesController controller =
            new ProductImagesController(managementService);

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
}
