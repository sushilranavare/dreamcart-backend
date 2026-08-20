/*
 * Web configuration for DreamCart.
 *
 * This configuration exposes uploaded product images
 * stored on the local filesystem through HTTP.
 */

package com.dreamcart.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*
     * Maps:
     *
     * /uploads/products/**
     *
     * to:
     *
     * uploads/products/
     *
     * on the local filesystem.
     */
    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {

        String uploadPath =
                Paths.get("uploads/products")
                        .toAbsolutePath()
                        .normalize()
                        .toUri()
                        .toString();

        //Spring requires a trailing slash for directory locations.
        if (!uploadPath.endsWith("/")){
            uploadPath +="/";
        }
        registry.addResourceHandler(
                "/uploads/products/**"
        ).addResourceLocations("file:uploads/products/"
        );
    }
}