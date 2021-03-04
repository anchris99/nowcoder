package com.anchris.config;

import com.anchris.controller.interceptor.LoginRequiredInterceptor;
import com.anchris.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/js/*.js","/img/*.png","/img/*.jpg","/img/*.jpeg","/css/*.css");
//        registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/js/*.js","/img/*.png","/img/*.jpg","/img/*.jpeg","/css/*.css");

    }
}
