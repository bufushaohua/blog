package com.jxlg.interceptor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    //  过滤掉除"/admin"和"/admin/login"之外的所有"/admin/**"
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/user-input")
                .excludePathPatterns("/admin/user")
                .excludePathPatterns("/admin/user/input")
                .excludePathPatterns("/admin/BackLogin");
    }
}
