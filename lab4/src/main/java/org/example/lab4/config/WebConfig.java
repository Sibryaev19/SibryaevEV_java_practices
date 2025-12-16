package org.example.lab4.config;

import org.example.lab4.filter.XsltHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<XsltHeaderFilter> xsltFilterRegistration(XsltHeaderFilter filter) {
        FilterRegistrationBean<XsltHeaderFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(filter);
        registration.addUrlPatterns("/api/*"); // ТОЛЬКО для API endpoints
        registration.setOrder(1); // Важный! Должен быть высокий приоритет

        System.out.println("=== XSLT FILTER REGISTERED ===");
        System.out.println("URL Patterns: /api/*");
        System.out.println("Order: " + registration.getOrder());

        return registration;
    }
}