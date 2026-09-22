package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.application.upload.UploadRequestApplicationService;
import com.eskcti.algashop.product.catalog.application.upload.UploadRequestInput;
import com.eskcti.algashop.product.catalog.application.upload.UploadResponseOutput;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class UploadRequestControllerTest {

    private final UploadRequestApplicationService uploadRequestApplicationService =
            Mockito.mock(UploadRequestApplicationService.class);
    private final UploadRequestController controller =
            new UploadRequestController(uploadRequestApplicationService);

    @Test
    void shouldDelegateRequestUploadToApplicationService() {
        UploadRequestInput input = new UploadRequestInput();
        input.setOriginalFileName("photo.png");
        input.setContentLength(2048L);
        UploadResponseOutput output = UploadResponseOutput.builder()
                .remoteFileName("photo.png")
                .build();
        Mockito.when(uploadRequestApplicationService.requestPreSignedUrl(input))
                .thenReturn(output);

        UploadResponseOutput result = controller.requestUpload(input);

        Mockito.verify(uploadRequestApplicationService).requestPreSignedUrl(input);
        assertThat(result).isSameAs(output);
    }
}
