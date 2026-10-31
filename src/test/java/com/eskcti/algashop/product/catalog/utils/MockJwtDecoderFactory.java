package com.eskcti.algashop.product.catalog.utils;

import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockJwtDecoderFactory {

    public static final String DEFAULT_ISSUER_URI = "http://auth.algashop.local:8081";

    public static final String[] DEFAULT_SCOPES = new String[] {
            "products:read",
            "products:write",
            "products:stock:write",
            "categories:read",
            "categories:write",
    };

    public static final String DEFAULT_SUBJECT = "00000000-0000-0000-0000-000000000001";

    public static final String DEFAULT_ROLE = "MANAGER";

    public static final String DEFAULT_TOKEN_VALUE = "fake.jwt.token";

    public static JwtDecoder createMockJwtDecoder() {
        return createMockJwtDecoder(DEFAULT_SUBJECT, DEFAULT_ISSUER_URI, DEFAULT_SCOPES, DEFAULT_ROLE);
    }

    public static JwtDecoder createMockJwtDecoder(String subject, String issuer, String[] scopes) {
        return createMockJwtDecoder(subject, issuer, scopes, DEFAULT_ROLE);
    }

    public static JwtDecoder createMockJwtDecoder(String subject, String issuer, String[] scopes, String role) {
        JwtDecoder jwtDecoder = Mockito.mock(JwtDecoder.class);
        Mockito.when(jwtDecoder.decode(Mockito.anyString()))
                .thenReturn(buildJwt(subject, issuer, scopes, role));
        return jwtDecoder;
    }

    public static Jwt buildJwt(String subject, String issuer, String[] scopes) {
        return buildJwt(subject, issuer, scopes, DEFAULT_ROLE);
    }

    public static Jwt buildJwt(String subject, String issuer, String[] scopes, String role) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(600);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        claims.put("iss", issuer);
        claims.put("aud", List.of("algashop-api"));

        if (scopes != null && scopes.length > 0) {
            claims.put("scope", String.join(" ", scopes));
        }

        if (role != null && !role.isBlank()) {
            claims.put("role", role);
        }

        return Jwt.withTokenValue(DEFAULT_TOKEN_VALUE)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .issuer(issuer)
                .subject(subject)
                .audience(List.of("algashop-api"))
                .claims(c -> c.putAll(claims))
                .headers(h -> h.put("alg", "none"))
                .build();
    }

}
