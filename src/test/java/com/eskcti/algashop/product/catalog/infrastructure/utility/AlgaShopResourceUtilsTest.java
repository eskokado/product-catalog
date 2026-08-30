package com.eskcti.algashop.product.catalog.infrastructure.utility;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class AlgaShopResourceUtilsTest {

    @Test
    void shouldReadExistingResourceContent() {
        String content = AlgaShopResourceUtils.readContent("db/testdata/categories.json");

        assertThat(content).isNotBlank();
        assertThat(content).contains("Laptops");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenResourceNotFound() {
        assertThatThrownBy(() -> AlgaShopResourceUtils.readContent("nonexistent/resource.json"))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(java.io.FileNotFoundException.class);
    }

    @Test
    void shouldThrowRuntimeExceptionOnIoException() {
        try (MockedConstruction<BufferedInputStream> mocked = mockConstruction(BufferedInputStream.class,
                withSettings().defaultAnswer(CALLS_REAL_METHODS),
                (mock, context) -> {
                    doThrow(new IOException("Simulated IO error"))
                            .when(mock).read(any(byte[].class), anyInt(), anyInt());
                })) {
            assertThatThrownBy(() -> AlgaShopResourceUtils.readContent("db/testdata/categories.json"))
                    .isInstanceOf(RuntimeException.class)
                    .hasCauseInstanceOf(IOException.class);
        }
    }
}
