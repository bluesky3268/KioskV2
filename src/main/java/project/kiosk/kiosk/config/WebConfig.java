package project.kiosk.kiosk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.kiosk.kiosk.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 인터셉터 등록

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/member", "/admin/duplicateCheck", "/login", "/css/*", "/js/*", "/*.ico", "/error");
    }
}
