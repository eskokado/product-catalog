package com.eskcti.algashop.product.catalog.infrastructure.utility.mapper;

import com.eskcti.algashop.product.catalog.infrastructure.utility.Slugfier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlugfierTest {

    @Test
    void shouldReturnNullWhenTextIsNull() {
        assertThat(Slugfier.slugify(null)).isNull();
    }

    @Test
    void shouldConvertTextToSlug() {
        assertThat(Slugfier.slugify("Hello World")).isEqualTo("hello-world");
    }

    @Test
    void shouldRemoveAccentsAndSpecialCharacters() {
        assertThat(Slugfier.slugify("Cafe com Acucar")).isEqualTo("cafe-com-acucar");
    }

    @Test
    void shouldHandleEmptyString() {
        assertThat(Slugfier.slugify("")).isEmpty();
    }

    @Test
    void shouldHandleWhitespaceOnly() {
        assertThat(Slugfier.slugify("   ")).isEqualTo("---");
    }

    @Test
    void shouldConvertToLowerCase() {
        assertThat(Slugfier.slugify("UPPERCASE")).isEqualTo("uppercase");
    }
}
