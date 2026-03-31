package com.habitia.shared.infrastructure.storage;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class StorageConfig implements WebMvcConfigurer {
    
    private final StorageProperties props;

    public StorageConfig(StorageProperties props) {
        this.props = props;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(StorageConstants.UPLOADS_PATH + "/**")
                .addResourceLocations("file:" + props.uploadDir() + "/");
    }
}
