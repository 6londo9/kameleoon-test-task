package kameleoon.task.config;

import jakarta.servlet.http.HttpServletRequest;

public final class AllPathsRequestMatcher implements org.springframework.security.web.util.matcher.RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }
}
