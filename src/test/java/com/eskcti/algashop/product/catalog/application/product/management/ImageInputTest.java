package com.eskcti.algashop.product.catalog.application.product.management;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageInputTest {

    @Test
    void shouldSetAndGetRemoteFileName() {
        ImageInput input = new ImageInput();
        input.setRemoteFileName("photo.png");

        assertThat(input.getRemoteFileName()).isEqualTo("photo.png");
    }
}
