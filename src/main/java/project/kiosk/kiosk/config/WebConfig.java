package project.kiosk.kiosk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.kiosk.kiosk.interceptor.LoginInterceptor;
import project.kiosk.kiosk.interceptor.RoleCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/static/**", "/login", "/css/**", "/js/**", "/error", "/*.ico");

        registry.addInterceptor(new RoleCheckInterceptor())
                .order(2)
                .addPathPatterns("/admin/member", "/admin/members/**")
                .excludePathPatterns("/admin", "/admin/items/**", "/static/**", "/logout", "/css/**", "/js/**", "/error", "/*.ico", "/api/**");

    }



}
