package com.eskcti.algashop.product.catalog.infrastructure.storage.s3;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class StorageProviderAwsS3PropertiesTest {

    private final Validator validator;

    StorageProviderAwsS3PropertiesTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldHaveValidProperties() {
        StorageProviderAwsS3Properties properties = new StorageProviderAwsS3Properties();
        properties.setBucketName("algashop-product-image");

        Set<ConstraintViolation<StorageProviderAwsS3Properties>> violations = validator.validate(properties);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldRejectBlankBucketName() {
        StorageProviderAwsS3Properties properties = new StorageProviderAwsS3Properties();
        properties.setBucketName(" ");

        Set<ConstraintViolation<StorageProviderAwsS3Properties>> violations = validator.validate(properties);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("bucketName"));
    }

    @Test
    void shouldRejectNullBucketName() {
        StorageProviderAwsS3Properties properties = new StorageProviderAwsS3Properties();

        Set<ConstraintViolation<StorageProviderAwsS3Properties>> violations = validator.validate(properties);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("bucketName"));
    }

    @Test
    void shouldSetAndGetBucketName() {
        StorageProviderAwsS3Properties properties = new StorageProviderAwsS3Properties();
        properties.setBucketName("algashop-product-image");

        assertThat(properties.getBucketName()).isEqualTo("algashop-product-image");
    }
}
