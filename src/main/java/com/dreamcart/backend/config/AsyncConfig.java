/*
 * Enables asynchronous processing.
 * Emails are sent in the background
 * without blocking API responses.
 */
package com.dreamcart.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
}