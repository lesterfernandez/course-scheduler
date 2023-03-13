package me.lesterfernandez.CourseScheduler.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

  @Autowired
  private AuthContext authContext;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    try {
      authContext.authorize(request);
      if (authContext.authorized && authContext.getUsername() != null) {
        return true;
      }
      response.setStatus(401);
      return false;
    } catch (Exception e) {
      response.setStatus(401);
      return false;
    }
  }
}
