package com.eskcti.algashop.product.catalog.infrastructure.storage.s3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StorageProviderExceptionTest {

    @Test
    void shouldCreateWithoutMessageNorCause() {
        StorageProviderException exception = new StorageProviderException();

        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception.getMessage()).isNull();
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void shouldCreateWithMessage() {
        StorageProviderException exception = new StorageProviderException("storage unavailable");

        assertThat(exception.getMessage()).isEqualTo("storage unavailable");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void shouldCreateWithMessageAndCause() {
        IllegalStateException cause = new IllegalStateException("root cause");

        StorageProviderException exception = new StorageProviderException("storage unavailable", cause);

        assertThat(exception.getMessage()).isEqualTo("storage unavailable");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void shouldCreateWithCause() {
        IllegalStateException cause = new IllegalStateException("root cause");

        StorageProviderException exception = new StorageProviderException(cause);

        assertThat(exception.getMessage()).isEqualTo(cause.toString());
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void shouldCreateWithMessageCauseAndStackTraceFlag() {
        IllegalStateException cause = new IllegalStateException("root cause");

        StorageProviderException exception =
                new StorageProviderException("storage unavailable", cause, false, false);

        assertThat(exception.getMessage()).isEqualTo("storage unavailable");
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getStackTrace()).isEmpty();
    }
}
