package com.eskcti.algashop.product.catalog.application;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageModelTest {

    @Test
    void shouldCreatePageModelWithBuilder() {
        PageModel<String> pageModel = PageModel.<String>builder()
                .number(0)
                .size(10)
                .totalPages(5)
                .totalElements(50)
                .content(List.of("a", "b"))
                .build();

        assertThat(pageModel.getNumber()).isEqualTo(0);
        assertThat(pageModel.getSize()).isEqualTo(10);
        assertThat(pageModel.getTotalPages()).isEqualTo(5);
        assertThat(pageModel.getTotalElements()).isEqualTo(50);
        assertThat(pageModel.getContent()).containsExactly("a", "b");
    }

    @Test
    void shouldCreateEmptyPageModelWithNoArgsConstructor() {
        PageModel<String> pageModel = new PageModel<>();

        assertThat(pageModel.getContent()).isEmpty();
        assertThat(pageModel.getNumber()).isEqualTo(0);
        assertThat(pageModel.getSize()).isEqualTo(0);
    }

    @Test
    void shouldCreatePageModelWithAllArgsConstructor() {
        PageModel<String> pageModel = new PageModel<>(1, 10, 3, 30, List.of("x"));

        assertThat(pageModel.getNumber()).isEqualTo(1);
        assertThat(pageModel.getSize()).isEqualTo(10);
        assertThat(pageModel.getTotalPages()).isEqualTo(3);
        assertThat(pageModel.getTotalElements()).isEqualTo(30);
        assertThat(pageModel.getContent()).containsExactly("x");
    }

    @Test
    void shouldConvertFromSpringDataPage() {
        List<String> content = List.of("alpha", "beta", "gamma");
        Page<String> springPage = new PageImpl<>(content, PageRequest.of(1, 10), 25);

        PageModel<String> result = PageModel.of(springPage);

        assertThat(result.getContent()).containsExactly("alpha", "beta", "gamma");
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getTotalElements()).isEqualTo(25);
    }

    @Test
    void shouldConvertEmptySpringDataPage() {
        Page<String> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        PageModel<String> result = PageModel.of(emptyPage);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);
    }
}
