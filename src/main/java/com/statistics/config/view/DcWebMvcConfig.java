package com.statistics.config.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
public class DcWebMvcConfig extends WebMvcConfigurerAdapter {
 
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
 
    /**
	 * 配置html视图解析器
	 * 如果没有配置视图解析器。Spring会使用BeanNameViewResolver，通过查找ID与逻辑视图名称匹配且实现了View接口的beans
	 * 
	 * @return
	 */
	@Bean
	public InternalResourceViewResolver setupViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		//** 设置视图路径的前缀 *//*
		resolver.setPrefix("/WEB-INF/views/");
        //resolver.setPrefix("/WEB-INF/classes/templates/");

		//** 设置视图路径的后缀 *//*
		resolver.setSuffix(".html");
		return resolver;
	}
	
	/*@Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
	
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }*/


    
}

