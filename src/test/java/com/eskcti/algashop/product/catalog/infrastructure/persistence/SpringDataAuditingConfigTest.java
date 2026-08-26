package com.eskcti.algashop.product.catalog.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class SpringDataAuditingConfigTest {

    @Test
    void shouldProvideCurrentDateTimeTruncatedToMillis() {
        DateTimeProvider dateTimeProvider = new SpringDataAuditingConfig().auditingDateTimeProvider();

        Optional<TemporalAccessor> now = dateTimeProvider.getNow();

        assertThat(now).isPresent();
        assertThat(now.get()).isInstanceOf(OffsetDateTime.class);
        OffsetDateTime value = OffsetDateTime.from(now.get());
        assertThat(value).isCloseToUtcNow(within(1, ChronoUnit.SECONDS));
    }

    @Test
    void shouldProvideCurrentAuditor() {
        AuditorAware<UUID> auditorAware = new SpringDataAuditingConfig().auditorProvider();

        Optional<UUID> auditor = auditorAware.getCurrentAuditor();

        assertThat(auditor).isPresent();
    }
}
