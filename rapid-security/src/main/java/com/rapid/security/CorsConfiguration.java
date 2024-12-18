package com.rapid.security;
import com.rapid.core.dto.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {
//    @Bean
//    public WebMvcConfigurer corsConfigure(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//               // WebMvcConfigurer.super.addCorsMappings(registry);
//                registry.addMapping("/**")
//                        .allowedMethods(Constant.GET, Constant.POST,
//                                Constant.DELETE,Constant.PUT)
//                        .allowedHeaders("*")
//                        .allowedOriginPatterns("*")
//                        .allowCredentials(true);
//            }
//        };
//    }

    @Bean
    public WebMvcConfigurer corsConfigure(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                WebMvcConfigurer.super.addCorsMappings(registry);
                registry.addMapping("/**")
                        .allowedMethods(Constant.GET, Constant.POST,
                                Constant.PUT, Constant.DELETE)
                        .allowedOrigins("http://127.0.0.1:5500")
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);
            }
        };




    }

    @Bean
    public RestTemplate restTemplate() {
            return new RestTemplate();
    }



}
