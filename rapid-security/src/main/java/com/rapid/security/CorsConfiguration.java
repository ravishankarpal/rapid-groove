package com.rapid.security;

import com.rapid.core.dto.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    public WebMvcConfigurer corsConfigure(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
               // WebMvcConfigurer.super.addCorsMappings(registry);
                registry.addMapping("/**")
                        .allowedMethods(Constant.GET, Constant.POST,
                                Constant.DELETE,Constant.PUT)
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);
            }
        };
    }
}
