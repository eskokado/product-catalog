package com.eskcti.algashop.product.catalog.infrastructure.storage.s3;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;

import static org.assertj.core.api.Assertions.assertThat;

class StorageProviderAwsS3HealthIndicatorTest {

    private final StorageProviderAwsS3Impl storageProvider = Mockito.mock(StorageProviderAwsS3Impl.class);
    private final StorageProviderAwsS3HealthIndicator healthIndicator =
            new StorageProviderAwsS3HealthIndicator(storageProvider);

    @Test
    void shouldReturnUpWhenStorageIsHealthy() {
        Mockito.when(storageProvider.healthCheck()).thenReturn(true);

        Health health = healthIndicator.health();

        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo(Status.UP);
    }

    @Test
    void shouldReturnDegradedWhenStorageIsNotHealthy() {
        Mockito.when(storageProvider.healthCheck()).thenReturn(false);

        Health health = healthIndicator.health();

        assertThat(health).isNotNull();
        assertThat(health.getStatus().getCode()).isEqualTo("DEGRADED");
    }
}
