package com.eskcti.algashop.product.catalog.application.storage;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileReferenceTest {

    @Test
    void shouldCreateFileReferenceWithValidValues() {
        FileReference fileReference = FileReference.builder()
                .fileName("image.png")
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(1024L)
                .expiresIn(Duration.ofMinutes(5))
                .build();

        assertThat(fileReference.getFileName()).isEqualTo("image.png");
        assertThat(fileReference.getContentType()).isEqualTo(MediaType.IMAGE_PNG);
        assertThat(fileReference.getContentLength()).isEqualTo(1024L);
        assertThat(fileReference.getExpiresIn()).isEqualTo(Duration.ofMinutes(5));
    }

    @Test
    void shouldThrowWhenFileNameIsNull() {
        assertThatThrownBy(() -> FileReference.builder()
                .fileName(null)
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(1024L)
                .expiresIn(Duration.ofMinutes(5))
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenContentTypeIsNull() {
        assertThatThrownBy(() -> FileReference.builder()
                .fileName("image.png")
                .contentType(null)
                .contentLength(1024L)
                .expiresIn(Duration.ofMinutes(5))
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenExpiresInIsNull() {
        assertThatThrownBy(() -> FileReference.builder()
                .fileName("image.png")
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(1024L)
                .expiresIn(null)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenContentLengthIsZero() {
        assertThatThrownBy(() -> FileReference.builder()
                .fileName("image.png")
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(0L)
                .expiresIn(Duration.ofMinutes(5))
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenContentLengthIsNegative() {
        assertThatThrownBy(() -> FileReference.builder()
                .fileName("image.png")
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(-1L)
                .expiresIn(Duration.ofMinutes(5))
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
