package com.douwe.notes.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer{

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        registry.addViewController("/login").setViewName("login.html");
        registry.addViewController("/logout").setViewName("login.html");
    }
    
}
