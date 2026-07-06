package com.Safe.Link.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        Locale esLatam = Locale.forLanguageTag("es-419");
        resolver.setSupportedLocales(List.of(esLatam, Locale.US));
        resolver.setDefaultLocale(esLatam);
        return resolver;
    }
}
