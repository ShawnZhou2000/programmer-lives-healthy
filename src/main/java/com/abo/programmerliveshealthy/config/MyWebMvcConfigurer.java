package com.abo.programmerliveshealthy.config;

import com.abo.programmerliveshealthy.component.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author abo
 * @date 2020/6/28 15:51
 * @remarks
 **/
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String filePath;
    @Value("${file.upload.path.relative}")
    private String fileRelativePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler(fileRelativePath).addResourceLocations("file:" + filePath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/login","/register","/getMailCode","/resetPassword","/img/**","/css/**","/js/**");
    }
}
