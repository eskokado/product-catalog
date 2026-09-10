package com.eskcti.algashop.product.catalog.infrastructure.web;

import com.eskcti.algashop.product.catalog.application.category.query.CategoryFilter;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WebConfigTest {

    private final WebConfig.CaseInsensitiveEnumConverterFactory factory =
            new WebConfig.CaseInsensitiveEnumConverterFactory();

    @Test
    void shouldConvertEnumValueCaseInsensitive() {
        Converter<String, CategoryFilter.SortType> converter =
                factory.getConverter(CategoryFilter.SortType.class);

        assertThat(converter.convert("name")).isEqualTo(CategoryFilter.SortType.NAME);
        assertThat(converter.convert("NAME")).isEqualTo(CategoryFilter.SortType.NAME);
        assertThat(converter.convert("Name")).isEqualTo(CategoryFilter.SortType.NAME);
        assertThat(converter.convert("  name  ")).isEqualTo(CategoryFilter.SortType.NAME);
    }

    @Test
    void shouldReturnNullForBlankSource() {
        Converter<String, CategoryFilter.SortType> converter =
                factory.getConverter(CategoryFilter.SortType.class);

        assertThat(converter.convert(null)).isNull();
        assertThat(converter.convert("")).isNull();
        assertThat(converter.convert("   ")).isNull();
    }

    @Test
    void shouldThrowForInvalidEnumValue() {
        Converter<String, CategoryFilter.SortType> converter =
                factory.getConverter(CategoryFilter.SortType.class);

        assertThatThrownBy(() -> converter.convert("INVALID"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid enum value");
    }

    @Test
    void shouldWorkWithSortDirectionEnum() {
        Converter<String, org.springframework.data.domain.Sort.Direction> converter =
                factory.getConverter(org.springframework.data.domain.Sort.Direction.class);

        assertThat(converter.convert("asc")).isEqualTo(org.springframework.data.domain.Sort.Direction.ASC);
        assertThat(converter.convert("ASC")).isEqualTo(org.springframework.data.domain.Sort.Direction.ASC);
        assertThat(converter.convert("desc")).isEqualTo(org.springframework.data.domain.Sort.Direction.DESC);
    }
}
