package com.ifoods.config;

import javax.servlet.Filter;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.ifoods.config.filter.CorsFilter;
import com.ifoods.config.filter.LanguageFilter;

/**
 * 
 */
@Configuration
public class AppConfig {
    
    /**
     * 错误码国际化
     */
    @Bean
    public FilterRegistrationBean langFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        
        LanguageFilter languageFilter = new LanguageFilter();
        registrationBean.setName("languageFilter");
        registrationBean.setFilter(languageFilter);
        registrationBean.setOrder(2);
        
        registrationBean.addUrlPatterns("/*");
        
        return registrationBean;
    }
    

    /**
     * 编码
     * @return
     */
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter =new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
    
    /**
     * 错误页面
     * @return
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {

                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

                container.addErrorPages(error401Page, error404Page, error500Page);
            }
        };
    }
}