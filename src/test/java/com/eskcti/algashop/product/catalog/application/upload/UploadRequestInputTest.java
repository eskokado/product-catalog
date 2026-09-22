package com.eskcti.algashop.product.catalog.application.upload;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UploadRequestInputTest {

    @Test
    void shouldSetAndGetOriginalFileName() {
        UploadRequestInput input = new UploadRequestInput();
        input.setOriginalFileName("photo.png");

        assertThat(input.getOriginalFileName()).isEqualTo("photo.png");
    }

    @Test
    void shouldSetAndGetContentLength() {
        UploadRequestInput input = new UploadRequestInput();
        input.setContentLength(2048L);

        assertThat(input.getContentLength()).isEqualTo(2048L);
    }
}
