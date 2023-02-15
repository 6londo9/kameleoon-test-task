package kameleoon.task.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kameleoon.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1.0/auth")
public final class Authentication {

    @Autowired
    private final AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public ModelAndView getRegistration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("urls/auth/registration.html");
        return modelAndView;
    }

    @PostMapping(path = "/register",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<AuthenticationResponse> registration(
            RegistrationRequest request, HttpServletResponse httpServletResponse
    ) {
        if (userService.checkIfUserExistByUsername(request.getUsername())) {
            return ResponseEntity.noContent().build();
        }
        AuthenticationResponse response = authService.register(request);
        Cookie token = new Cookie("token", response.getToken());
        token.setMaxAge(3600);
        token.setPath("/");
        httpServletResponse.addCookie(token);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create("/v1.0/auth/success"))
                .build();
    }

    @PostMapping(value = "/authenticate",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<AuthenticationResponse> authenticate(
            AuthenticationRequest request, HttpServletResponse httpServletResponse
    ) {
        if (userService.checkIfUserExistByUsername(request.getUsername())) {
            return ResponseEntity.noContent().build();
        }
        AuthenticationResponse response = authService.authenticate(request);
        Cookie token = new Cookie("token", response.getToken());
        token.setMaxAge(3600);
        token.setPath("/");
        httpServletResponse.addCookie(token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/v1.0/top"))
                .build();
    }

    @GetMapping("/success")
    public ModelAndView successfulRegistration() {
        ModelAndView mv = new ModelAndView("urls/auth/successful-registration.html");
        return mv;
    }

    @PostMapping("/logout")
    public RedirectView logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/v1.0/top");
        return redirectView;
    }

}
