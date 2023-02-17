package kameleoon.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kameleoon.task.config.JwtService;
import kameleoon.task.model.Quote;
import kameleoon.task.model.User;
import kameleoon.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;



@RestController
@RequestMapping("/v1.0/users")
public final class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Operation(summary = "Show user profile")
    @ApiResponse(responseCode = "200", description = "The page successfully loaded")
    @GetMapping("/{id}")
    public ModelAndView getUser(
            @Parameter(description = "Id of user to be found")
            @PathVariable String id,
            @Parameter(description = "JWT token as cookie")
            @CookieValue(name = "token", required = false) String token
    ) {
        ModelAndView modelAndView = new ModelAndView();
        Long userId = Long.parseLong(id);

        if (userService.findById(userId).isPresent()) {
            modelAndView.setViewName("urls/user/profile.html");

            if (token != null) {
                String username = jwtService.exctractUsername(token);
                User user = (User) userDetailsService.loadUserByUsername(username);

                if (userId == user.getId()) {
                    modelAndView.addObject("sameUser", "true");
                }
            }

            User user = userService.findById(userId).get();
            modelAndView.addObject("user", user);

            Quote topQuote = userService.findMostUpvotedQuote(user.getId());
            if (topQuote != null) {
                Long topQuoteId = topQuote.getId();
                modelAndView.addObject("topQuoteId", topQuoteId);
            }

            Quote lastAddedQuote = userService.findLastAddedQuote(userId);
            if (lastAddedQuote != null) {
                Long lastAddedQuoteId = lastAddedQuote.getId();
                modelAndView.addObject("lastQuoteId", lastAddedQuoteId);
            }

        } else {
            modelAndView.setViewName("urls/top.html");
        }
        return modelAndView;
    }

    @Operation(summary = "Update user information")
    @ApiResponse(responseCode = "303", description = "The user information successfully updated")
    @PatchMapping("/{id}")
    public User updateUser(
            @Parameter(description = "Id of user to be updated")
            @PathVariable("id") String id,
            @Parameter(description = "New user data")
            @RequestBody User user
    ) {
        Long userId = Long.parseLong(id);
        return userService.updateUser(userId, user);
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "303", description = "The user successfully deleted")
    @DeleteMapping("/{id}")
    public void deleteUser(
            @Parameter(description = "Id of user to be deleted")
            @PathVariable("id") String id
    ) {
        Long userId = Long.parseLong(id);
        userService.deleteUser(userId);
    }
}
