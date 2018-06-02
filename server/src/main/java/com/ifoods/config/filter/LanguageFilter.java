package com.ifoods.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.ifoods.common.Constants;
import com.ifoods.common.model.ConsoleContext;
import com.ifoods.common.util.ThreadLocalManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author zhenghui.li
 * @date 2018年4月18日
 * 
 * 语言返回值设置
 * 
 */
@Slf4j
public class LanguageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        
        //Object languageType = request.getAttribute(Constants.LANGUAGE_KEY);
        String languageType = request.getHeader(Constants.LANGUAGE_KEY);
        //log.info("header i-msg-type={}", languageType);
        if(StringUtils.isEmpty(languageType)) {
            languageType = Constants.DEAULT_LANGUAGE;
        }
        
        ConsoleContext consoleContext = new ConsoleContext();
        consoleContext.setLanguageType(languageType);
        consoleContext.setRequest(request);
        ThreadLocalManager.setConsoleContext(consoleContext);
        
        chain.doFilter(req, resp);
        
    }
}
