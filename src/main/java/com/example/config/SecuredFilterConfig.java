/*
package com.example.config;

import com.fasterxml.jackson.core.filter.TokenFilter;
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

        bean.addUrlPatterns("/articleLike/like/*");
        bean.addUrlPatterns("/articleLike/dislike/*");
        bean.addUrlPatterns("/articleLike/remove/*");

        bean.addUrlPatterns("/commentLike/like/*");
        bean.addUrlPatterns("/commentLike/dislike/*");
        bean.addUrlPatterns("/commentLike/remove/*");

        bean.addUrlPatterns("/comment/*");
        bean.addUrlPatterns("/comment/adm/*");
        bean.addUrlPatterns("/comment/adm/filter/*");




        return bean;
    }

}
*/
