package com.example.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SecuredFilterConfig {
    @Autowired
    private TokenFilter tokenFilter;

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(tokenFilter);
        bean.addUrlPatterns("/region/adm");
        bean.addUrlPatterns("/region/adm/*");
        bean.addUrlPatterns("/region/adm/**");

        bean.addUrlPatterns("/category/adm");
        bean.addUrlPatterns("/category/adm/*");
        bean.addUrlPatterns("/category/adm/**");

        bean.addUrlPatterns("/profile/adm");
        bean.addUrlPatterns("/profile/adm/*");
        bean.addUrlPatterns("/profile/*");
        bean.addUrlPatterns("/profile/adm/**");


        bean.addUrlPatterns("/articleType/adm");
        bean.addUrlPatterns("/articleType/adm/*");
        bean.addUrlPatterns("/articleType/*");
        bean.addUrlPatterns("/articleType/adm/**");

        bean.addUrlPatterns("/email_history");
        bean.addUrlPatterns("/email_history/adm");
        bean.addUrlPatterns("/email_history/*");

        bean.addUrlPatterns("/sms_history");
        bean.addUrlPatterns("/sms_history/pagination");
        bean.addUrlPatterns("/sms_history/*");

        bean.addUrlPatterns("/attach/delete/*");

        bean.addUrlPatterns("/article/*");
        bean.addUrlPatterns("/article/{id}");
        bean.addUrlPatterns("/article/typeId/{id}");
        bean.addUrlPatterns("/article/articleId");

        return bean;
    }


}
