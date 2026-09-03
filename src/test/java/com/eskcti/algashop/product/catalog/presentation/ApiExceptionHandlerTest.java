package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.application.ResourceNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.DomainEntityNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.DomainException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private final MessageSource messageSource = Mockito.mock(MessageSource.class);
    private final ApiExceptionHandler handler = new ApiExceptionHandler(messageSource);

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
    void shouldHandleResourceNotFoundException() {
        ProblemDetail problemDetail = handler
                .handleResourceNotFoundException(new ResourceNotFoundException());

        assertThat(problemDetail.getStatus()).isEqualTo(404);
        assertThat(problemDetail.getTitle()).isEqualTo("Not found");
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

    @Test
    void shouldHandleMethodArgumentNotValid() {
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "name", "must not be blank");
        Mockito.when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        Mockito.when(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                .thenReturn("must not be blank");

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindingResult);

        var response = handler
                .handleMethodArgumentNotValid(exception, new HttpHeaders(), HttpStatusCode.valueOf(400),
                        Mockito.mock(WebRequest.class));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
        ProblemDetail problemDetail = (ProblemDetail) response.getBody();
        assertThat(problemDetail.getTitle()).isEqualTo("Invalid fields");
        assertThat(problemDetail.getDetail()).isEqualTo("One or more fields are invalid");
        assertThat(problemDetail.getType().toString()).isEqualTo("/errors/invalid-fields");
    }
}
