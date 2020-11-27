package com.ali.bugtracker.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${imagesPath}")
    String imagesPath;
   
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
        return  bCryptPasswordEncoder;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(imagesPath);
    }
}