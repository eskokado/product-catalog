package com.eskcti.algashop.product.catalog.application.upload;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UploadResponseOutputTest {

    @Test
    void shouldBuildOutputWithAllFields() {
        OffsetDateTime expiresAt = OffsetDateTime.now().plusMinutes(5);

        UploadResponseOutput output = UploadResponseOutput.builder()
                .uploadSignedUrl("http://localhost:4566/photo.png?token=abc")
                .remoteFileName("photo.png")
                .contentLength(2048L)
                .contentType("image/png")
                .expiresAt(expiresAt)
                .build();

        assertThat(output.getUploadSignedUrl()).isEqualTo("http://localhost:4566/photo.png?token=abc");
        assertThat(output.getRemoteFileName()).isEqualTo("photo.png");
        assertThat(output.getContentLength()).isEqualTo(2048L);
        assertThat(output.getContentType()).isEqualTo("image/png");
        assertThat(output.getExpiresAt()).isEqualTo(expiresAt);
    }

    @Test
    void shouldAllowMutatingFieldsWithSetters() {
        UploadResponseOutput output = UploadResponseOutput.builder().build();

        output.setUploadSignedUrl("http://localhost:4566/photo.png?token=abc");
        output.setRemoteFileName("photo.png");
        output.setContentLength(2048L);
        output.setContentType("image/png");
        OffsetDateTime expiresAt = OffsetDateTime.now().plusMinutes(5);
        output.setExpiresAt(expiresAt);

        assertThat(output.getUploadSignedUrl()).isEqualTo("http://localhost:4566/photo.png?token=abc");
        assertThat(output.getRemoteFileName()).isEqualTo("photo.png");
        assertThat(output.getContentLength()).isEqualTo(2048L);
        assertThat(output.getContentType()).isEqualTo("image/png");
        assertThat(output.getExpiresAt()).isEqualTo(expiresAt);
    }
}
