package com.eskcti.algashop.product.catalog.application.utility;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageMediaTypeExtractorTest {

    @Test
    void shouldBeInstantiable() {
        assertThat(new ImageMediaTypeExtractor()).isNotNull();
    }

    @Test
    void shouldReturnJpegMediaTypeForJpgExtension() {
        assertThat(ImageMediaTypeExtractor.fromFileName("photo.jpg"))
                .isEqualTo(MediaType.IMAGE_JPEG);
    }

    @Test
    void shouldReturnJpegMediaTypeForUppercaseJpgExtension() {
        assertThat(ImageMediaTypeExtractor.fromFileName("PHOTO.JPG"))
                .isEqualTo(MediaType.IMAGE_JPEG);
    }

    @Test
    void shouldReturnPngMediaTypeForPngExtension() {
        assertThat(ImageMediaTypeExtractor.fromFileName("image.png"))
                .isEqualTo(MediaType.IMAGE_PNG);
    }

    @Test
    void shouldReturnLowercaseMediaTypeForUppercaseExtension() {
        assertThat(ImageMediaTypeExtractor.fromFileName("image.PNG"))
                .isEqualTo(MediaType.IMAGE_PNG);
    }

    @Test
    void shouldThrowWhenExtensionIsMissing() {
        assertThatThrownBy(() -> ImageMediaTypeExtractor.fromFileName("image"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
