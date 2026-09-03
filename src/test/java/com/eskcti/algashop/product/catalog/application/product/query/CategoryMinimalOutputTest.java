package com.eskcti.algashop.product.catalog.application.product.query;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryMinimalOutputTest {

    @Test
    void shouldCreateOutputWithAllFields() {
        UUID id = UUID.randomUUID();
        CategoryMinimalOutput output = CategoryMinimalOutput.builder()
                .id(id)
                .name("Electronics")
                .enabled(true)
                .build();

        assertThat(output.getId()).isEqualTo(id);
        assertThat(output.getName()).isEqualTo("Electronics");
        assertThat(output.getEnabled()).isTrue();
    }

    @Test
    void shouldGenerateSlugFromName() {
        CategoryMinimalOutput output = CategoryMinimalOutput.builder()
                .name("Gaming Notebooks")
                .build();

        assertThat(output.getSlug()).isEqualTo("gaming-notebooks");
    }

    @Test
    void shouldGenerateSlugWithAccents() {
        CategoryMinimalOutput output = CategoryMinimalOutput.builder()
                .name("Acessórios para Notebook")
                .build();

        assertThat(output.getSlug()).isEqualTo("acessorios-para-notebook");
    }
}
