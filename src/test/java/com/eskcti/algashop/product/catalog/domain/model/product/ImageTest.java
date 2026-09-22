package com.eskcti.algashop.product.catalog.domain.model.product;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageTest {

    @Test
    void shouldCreateImageWithIdAndName() {
        UUID id = UUID.randomUUID();

        Image image = new Image(id, "photo.png");

        assertThat(image.getId()).isEqualTo(id);
        assertThat(image.getName()).isEqualTo("photo.png");
    }

    @Test
    void shouldGenerateIdWhenCreatingWithNameOnly() {
        Image image = new Image("photo.png");

        assertThat(image.getId()).isNotNull();
        assertThat(image.getName()).isEqualTo("photo.png");
    }

    @Test
    void shouldCreateImageWithProtectedNoArgsConstructor() {
        Image image = new Image();

        assertThat(image.getId()).isNull();
        assertThat(image.getName()).isNull();
    }

    @Test
    void shouldRejectNullName() {
        assertThatThrownBy(() -> new Image(UUID.randomUUID(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectBlankName() {
        assertThatThrownBy(() -> new Image(UUID.randomUUID(), " "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNullId() {
        assertThatThrownBy(() -> new Image(null, "photo.png"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBeEqualWhenIdsMatch() {
        UUID id = UUID.randomUUID();

        Image first = new Image(id, "photo.png");
        Image second = new Image(id, "other.png");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdsDiffer() {
        Image first = new Image(UUID.randomUUID(), "photo.png");
        Image second = new Image(UUID.randomUUID(), "photo.png");

        assertThat(first).isNotEqualTo(second);
    }
}
