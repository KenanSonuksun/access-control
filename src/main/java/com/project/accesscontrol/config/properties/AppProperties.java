package com.project.accesscontrol.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @Valid
    private Cors cors = new Cors();

    @Valid
    private Security security = new Security();

    @Valid
    private Features features = new Features();

    public Cors getCors() {
        return cors;
    }

    public void setCors(Cors cors) {
        this.cors = cors;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    public static class Cors {

        @NotEmpty
        private List<String> allowedOrigins = List.of("http://localhost:3000");

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public static class Security {

        @NotBlank
        private String jwtSecret = "local-dev-secret-key";

        @Min(1)
        private long accessTokenExpirationMinutes = 15;

        @Min(1)
        private long refreshTokenExpirationDays = 7;

        public String getJwtSecret() {
            return jwtSecret;
        }

        public void setJwtSecret(String jwtSecret) {
            this.jwtSecret = jwtSecret;
        }

        public long getAccessTokenExpirationMinutes() {
            return accessTokenExpirationMinutes;
        }

        public void setAccessTokenExpirationMinutes(long accessTokenExpirationMinutes) {
            this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        }

        public long getRefreshTokenExpirationDays() {
            return refreshTokenExpirationDays;
        }

        public void setRefreshTokenExpirationDays(long refreshTokenExpirationDays) {
            this.refreshTokenExpirationDays = refreshTokenExpirationDays;
        }
    }

    public static class Features {

        private boolean requestLogEnabled = true;

        public boolean isRequestLogEnabled() {
            return requestLogEnabled;
        }

        public void setRequestLogEnabled(boolean requestLogEnabled) {
            this.requestLogEnabled = requestLogEnabled;
        }
    }
}