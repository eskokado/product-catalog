package com.eskcti.algashop.product.catalog.presentation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnprocessableContentExceptionTest {

    @Test
    void shouldCreateWithAllConstructors() {
        Throwable cause = new IllegalStateException("root");

        assertThat(new UnprocessableContentException().getMessage()).isNull();
        assertThat(new UnprocessableContentException("invalid").getMessage()).isEqualTo("invalid");
        UnprocessableContentException withCause = new UnprocessableContentException("invalid", cause);
        assertThat(withCause.getMessage()).isEqualTo("invalid");
        assertThat(withCause.getCause()).isSameAs(cause);
        assertThat(new UnprocessableContentException(cause).getCause()).isSameAs(cause);
        assertThat(new UnprocessableContentException("m", cause, true, true).getMessage()).isEqualTo("m");
    }
}
