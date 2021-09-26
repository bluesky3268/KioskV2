package project.kiosk.kiosk.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberCheckInterceptor implements HandlerInterceptor {

    /**
     * requestURI에서 members/ 뒤에 오는  PathVarialbe(memberNo)값을 뽑아내서
     * DB에 저장된 memberNo의 아아디 값과 session loggIn에 저장된 값이랑 비교해서 같으면 통과 다르면 redirect:/
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

}
