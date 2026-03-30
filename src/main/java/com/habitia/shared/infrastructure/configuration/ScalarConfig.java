package com.habitia.shared.infrastructure.configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ScalarConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Esto redirige la ruta /docs a un HTML que carga Scalar
        registry.addViewController("/docs").setViewName("forward:/scalar.html");
    }
}