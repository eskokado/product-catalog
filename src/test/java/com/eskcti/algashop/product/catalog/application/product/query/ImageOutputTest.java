package com.eskcti.algashop.product.catalog.application.product.query;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ImageOutputTest {

    @Test
    void shouldSetAndGetFields() {
        UUID id = UUID.randomUUID();
        ImageOutput output = new ImageOutput();
        output.setId(id);
        output.setUrl("http://localhost:4566/photo.png");

        assertThat(output.getId()).isEqualTo(id);
        assertThat(output.getUrl()).isEqualTo("http://localhost:4566/photo.png");
    }
}
