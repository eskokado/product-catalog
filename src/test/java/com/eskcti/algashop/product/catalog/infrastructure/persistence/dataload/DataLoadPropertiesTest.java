package com.eskcti.algashop.product.catalog.infrastructure.persistence.dataload;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DataLoadPropertiesTest {

    private final Validator validator;

    DataLoadPropertiesTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldHaveValidProperties() {
        DataLoadProperties properties = new DataLoadProperties();
        properties.setEnabled(true);
        properties.setAutoDrop(false);

        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("db/testdata/categories.json");
        source.setCollection("categories");
        properties.setSources(List.of(source));

        Set<ConstraintViolation<DataLoadProperties>> violations = validator.validate(properties);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldRejectNullEnabled() {
        DataLoadProperties properties = new DataLoadProperties();
        properties.setEnabled(null);
        properties.setAutoDrop(false);

        Set<ConstraintViolation<DataLoadProperties>> violations = validator.validate(properties);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("enabled"));
    }

    @Test
    void shouldRejectNullAutoDrop() {
        DataLoadProperties properties = new DataLoadProperties();
        properties.setEnabled(true);
        properties.setAutoDrop(null);

        Set<ConstraintViolation<DataLoadProperties>> violations = validator.validate(properties);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("autoDrop"));
    }

    @Test
    void shouldAllowNullSources() {
        DataLoadProperties properties = new DataLoadProperties();
        properties.setEnabled(true);
        properties.setAutoDrop(false);
        properties.setSources(null);

        Set<ConstraintViolation<DataLoadProperties>> violations = validator.validate(properties);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldRejectSourceWithBlankLocation() {
        DataLoadProperties properties = new DataLoadProperties();
        properties.setEnabled(true);
        properties.setAutoDrop(false);

        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("");
        source.setCollection("categories");
        properties.setSources(List.of(source));

        Set<ConstraintViolation<DataLoadProperties>> violations = validator.validate(properties);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().contains("location"));
    }

    @Test
    void shouldRejectSourceWithBlankCollection() {
        DataLoadProperties properties = new DataLoadProperties();
        properties.setEnabled(true);
        properties.setAutoDrop(false);

        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("db/testdata/categories.json");
        source.setCollection("");
        properties.setSources(List.of(source));

        Set<ConstraintViolation<DataLoadProperties>> violations = validator.validate(properties);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().contains("collection"));
    }

    @Test
    void shouldSetAndGetEnabled() {
        DataLoadProperties properties = new DataLoadProperties();
        properties.setEnabled(true);
        assertThat(properties.getEnabled()).isTrue();
    }

    @Test
    void shouldSetAndGetAutoDrop() {
        DataLoadProperties properties = new DataLoadProperties();
        properties.setAutoDrop(true);
        assertThat(properties.getAutoDrop()).isTrue();
    }

    @Test
    void shouldSetAndGetSources() {
        DataLoadProperties properties = new DataLoadProperties();
        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("test.json");
        source.setCollection("test");
        properties.setSources(List.of(source));

        assertThat(properties.getSources()).hasSize(1);
        assertThat(properties.getSources().get(0).getLocation()).isEqualTo("test.json");
        assertThat(properties.getSources().get(0).getCollection()).isEqualTo("test");
    }
}
