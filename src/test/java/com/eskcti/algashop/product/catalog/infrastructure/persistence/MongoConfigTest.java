package com.eskcti.algashop.product.catalog.infrastructure.persistence;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class MongoConfigTest {

    @Test
    void shouldConvertOffsetDateTimeToDate() {
        OffsetDateTime source = OffsetDateTime.of(2026, 8, 26, 12, 30, 45, 0, ZoneOffset.UTC);

        Date result = new MongoConfig.OffsetDateTimeToDateConverter().convert(source);

        assertThat(result).isNotNull();
        assertThat(result.toInstant()).isEqualTo(source.toInstant());
    }

    @Test
    void shouldConvertDateToOffsetDateTimeInUtc() {
        OffsetDateTime expected = OffsetDateTime.of(2026, 8, 26, 12, 30, 45, 0, ZoneOffset.UTC);
        Date source = Date.from(expected.toInstant());

        OffsetDateTime result = new MongoConfig.DateToOffsetDateTimeConverter().convert(source);

        assertThat(result).isNotNull();
        assertThat(result.toInstant()).isEqualTo(expected.toInstant());
        assertThat(result.getOffset()).isEqualTo(ZoneOffset.UTC);
    }
}
