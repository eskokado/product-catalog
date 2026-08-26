package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.domain.model.DomainEntityNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.DomainException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler =
            new ApiExceptionHandler(Mockito.mock(MessageSource.class));

    @Test
    void shouldHandleNotFoundExceptions() {
        ProblemDetail problemDetail = handler
                .handleResourceNotFoundException(new DomainEntityNotFoundException("Category not found"));

        assertThat(problemDetail.getStatus()).isEqualTo(404);
        assertThat(problemDetail.getTitle()).isEqualTo("Not found");
        assertThat(problemDetail.getDetail()).isEqualTo("Category not found");
        assertThat(problemDetail.getType().toString()).isEqualTo("/errors/not-found");
    }

    @Test
    void shouldHandleUnprocessableContentFromUnprocessableContentException() {
        ProblemDetail problemDetail = handler
                .handleUnprocessableContentException(new UnprocessableContentException("invalid category"));

        assertThat(problemDetail.getStatus()).isEqualTo(422);
        assertThat(problemDetail.getTitle()).isEqualTo("Unprocessable Content");
        assertThat(problemDetail.getDetail()).isEqualTo("invalid category");
        assertThat(problemDetail.getType().toString()).isEqualTo("/errors/unprocessable-content");
    }

    @Test
    void shouldHandleUnprocessableContentFromDomainException() {
        ProblemDetail problemDetail = handler
                .handleUnprocessableContentException(new DomainException("sale price above regular price"));

        assertThat(problemDetail.getStatus()).isEqualTo(422);
        assertThat(problemDetail.getTitle()).isEqualTo("Unprocessable Content");
        assertThat(problemDetail.getDetail()).isEqualTo("sale price above regular price");
        assertThat(problemDetail.getType().toString()).isEqualTo("/errors/unprocessable-content");
    }
}
