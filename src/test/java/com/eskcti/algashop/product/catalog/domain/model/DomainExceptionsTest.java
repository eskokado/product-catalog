package com.eskcti.algashop.product.catalog.domain.model;

import com.eskcti.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DomainExceptionsTest {

    @Test
    void shouldCreateDomainExceptionWithAllConstructors() {
        Throwable cause = new IllegalStateException("root");

        assertThat(new DomainException().getMessage()).isNull();
        assertThat(new DomainException("oops").getMessage()).isEqualTo("oops");
        DomainException withCause = new DomainException("oops", cause);
        assertThat(withCause.getMessage()).isEqualTo("oops");
        assertThat(withCause.getCause()).isSameAs(cause);
        assertThat(new DomainException(cause).getCause()).isSameAs(cause);
        assertThat(new DomainException("m", cause, true, true).getMessage()).isEqualTo("m");
    }

    @Test
    void shouldCreateDomainEntityNotFoundExceptionWithAllConstructors() {
        Throwable cause = new IllegalStateException("root");

        assertThat(new DomainEntityNotFoundException().getMessage()).isNull();
        assertThat(new DomainEntityNotFoundException("not found").getMessage()).isEqualTo("not found");
        DomainEntityNotFoundException withCause = new DomainEntityNotFoundException("not found", cause);
        assertThat(withCause.getMessage()).isEqualTo("not found");
        assertThat(withCause.getCause()).isSameAs(cause);
        assertThat(new DomainEntityNotFoundException(cause).getCause()).isSameAs(cause);
        assertThat(new DomainEntityNotFoundException("m", cause, true, true).getMessage()).isEqualTo("m");
    }

    @Test
    void shouldBuildCategoryNotFoundExceptionMessage() {
        UUID categoryId = UUID.randomUUID();

        CategoryNotFoundException exception = new CategoryNotFoundException(categoryId);

        assertThat(exception.getMessage())
                .isEqualTo("Category with id %s was not found".formatted(categoryId));
    }
}
