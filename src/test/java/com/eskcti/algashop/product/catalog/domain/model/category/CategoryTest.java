package com.eskcti.algashop.product.catalog.domain.model.category;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryTest {

    @Test
    void shouldCreateCategoryWithGeneratedIdAndCreatedAt() {
        Category category = new Category("Notebook", true);

        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo("Notebook");
        assertThat(category.getEnabled()).isTrue();
        assertThat(category.getCreatedAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void shouldUpdateName() {
        Category category = new Category("Notebook", true);

        category.setName("Notebook Gamer");

        assertThat(category.getName()).isEqualTo("Notebook Gamer");
    }

    @Test
    void shouldNotAllowBlankName() {
        Category category = new Category("Notebook", true);

        assertThatThrownBy(() -> category.setName(" "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> category.setName(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(category.getName()).isEqualTo("Notebook");
    }

    @Test
    void shouldChangeEnabledFlag() {
        Category category = new Category("Notebook", true);

        category.setEnabled(false);

        assertThat(category.getEnabled()).isFalse();
    }

    @Test
    void shouldNotAllowNullEnabled() {
        Category category = new Category("Notebook", true);

        assertThatThrownBy(() -> category.setEnabled(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldCreateCategoryWithEnabledFalse() {
        Category category = new Category("Mouse", false);

        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo("Mouse");
        assertThat(category.getEnabled()).isFalse();
        assertThat(category.getCreatedAt()).isBeforeOrEqualTo(OffsetDateTime.now());
    }

    @Test
    void shouldSetEnabledToTrue() {
        Category category = new Category("Mouse", false);

        category.setEnabled(true);

        assertThat(category.getEnabled()).isTrue();
    }
}
