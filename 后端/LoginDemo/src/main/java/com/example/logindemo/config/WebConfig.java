package com.example.logindemo.config;
import com.example.logindemo.interceptor.CheckTokenInterceptor;
import com.example.logindemo.utils.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//解决跨域问题
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private CheckTokenInterceptor checkTokenInterceptor;

    //添加DateConverter
    @Override
    public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(new DateConverter());
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry  registry) {

        registry.addInterceptor(checkTokenInterceptor)
                .addPathPatterns("/**")//拦截所有url
                .excludePathPatterns("/auth/**","/swagger-resources/**","/doc.html","/webjars/**","/v3/api-docs/**","/swagger-ui.html");
    }

    //跨域设置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")//允许跨域的域名
                .allowedMethods("*")//允许任何方法
                .allowedHeaders("*")//允许任何请求头
                .maxAge(3600L); //允许跨域的请求时间3600秒，不需要刷新，可以缓存，下次请求不用再发请求
    }

    //添加资源处理器
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {//添加静态资源映射
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
    }
}
