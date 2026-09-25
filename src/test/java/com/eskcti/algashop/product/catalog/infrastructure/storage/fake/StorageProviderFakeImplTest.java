package com.eskcti.algashop.product.catalog.infrastructure.storage.fake;

import com.eskcti.algashop.product.catalog.application.storage.FileReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.net.URI;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StorageProviderFakeImplTest {

    private final StorageProviderFakeImpl storageProvider = new StorageProviderFakeImpl();

    private FileReference fileReference() {
        return FileReference.builder()
                .fileName("photo.png")
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(2048L)
                .expiresIn(Duration.ofMinutes(5))
                .build();
    }

    @Test
    void shouldReturnLocalhostUploadUrlWithFileNameAndToken() {
        URI url = storageProvider.requestUploadUrl(fileReference());

        assertThat(url.toString())
                .startsWith("http://localhost:4566/photo.png?token=");
    }

    @Test
    void shouldGenerateUniqueTokenPerRequest() {
        URI first = storageProvider.requestUploadUrl(fileReference());
        URI second = storageProvider.requestUploadUrl(fileReference());

        assertThat(first.toString()).isNotEqualTo(second.toString());
    }

    @Test
    void shouldThrowWhenFileNameIsNotAValidUriComponent() {
        FileReference reference = FileReference.builder()
                .fileName("photo image.png")
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(2048L)
                .expiresIn(Duration.ofMinutes(5))
                .build();

        assertThatThrownBy(() -> storageProvider.requestUploadUrl(reference))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldDeleteFileWithoutError() {
        storageProvider.deleteFile("photo.png");
    }

    @Test
    void shouldReturnTrueWhenFileExists() {
        assertThat(storageProvider.fileExists("photo.png")).isTrue();
    }

    @Test
    void shouldReturnFalseWhenFileDoesNotExist() {
        assertThat(storageProvider.fileExists("fail.jpg")).isFalse();
    }
}
