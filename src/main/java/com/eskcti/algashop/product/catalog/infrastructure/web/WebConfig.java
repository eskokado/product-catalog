package com.eskcti.algashop.product.catalog.infrastructure.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CaseInsensitiveEnumConverterFactory());
    }

    public static class CaseInsensitiveEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

        @Override
        public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
            return source -> {
                if (source == null || source.isBlank()) {
                    return null;
                }
                return Arrays.stream(targetType.getEnumConstants())
                        .filter(e -> e.name().equalsIgnoreCase(source.trim()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Invalid enum value '" + source + "' for type " + targetType.getSimpleName()));
            };
        }
    }
}
