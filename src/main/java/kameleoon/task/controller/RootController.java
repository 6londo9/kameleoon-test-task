package kameleoon.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import kameleoon.task.config.JwtService;
import kameleoon.task.controller.util.ModelUtils;
import kameleoon.task.model.Quote;
import kameleoon.task.service.QuoteService;
import kameleoon.task.service.UserService;
import kameleoon.task.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/v1.0")
public final class RootController {

    @Autowired
    private QuoteService quoteService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private VoteService voteService;

    @Operation(summary = "Redirect user to the /v1.0/top page")
    @ApiResponse(responseCode = "303", description = "Redirect user to the /v1.0/top page")
    @GetMapping("/")
    public ResponseEntity<Void> homePage(HttpServletResponse response) {
        response.setHeader("Location", "/v1.0/top");
        return new ResponseEntity<>(HttpStatus.SEE_OTHER);
    }

    @Operation(summary = "Show top-10 liked quotes")
    @ApiResponse(responseCode = "200", description = "The page successfully loaded")
    @GetMapping("/top")
    public ModelAndView topQuotes(
            @Parameter(description = "JWT token as cookie")
            @CookieValue(name = "token", required = false) String token
    ) {
        String url = "urls/top.html";
        ModelUtils utils = new ModelUtils(token, url,
                jwtService, userDetailsService,
                quoteService, voteService);
        ModelAndView modelAndView = utils.getModelAndView();

        List<Quote> topQuotes = quoteService.findMostUpvotedQuotes();
        modelAndView.addObject("quotes", topQuotes);

        return modelAndView;
    }

    @Operation(summary = "Show top-10 disliked quotes")
    @ApiResponse(responseCode = "200", description = "The page successfully loaded")
    @GetMapping("/flop")
    public ModelAndView flopQuotes(
            @Parameter(description = "JWT token as cookie")
            @CookieValue(name = "token", required = false) String token
    ) {
        String url = "urls/flop.html";
        ModelUtils utils = new ModelUtils(token, url,
                jwtService, userDetailsService,
                quoteService, voteService);
        ModelAndView modelAndView = utils.getModelAndView();

        List<Quote> flopQuotes = quoteService.findMostDownvotedQuotes();
        modelAndView.addObject("quotes", flopQuotes);

        return modelAndView;
    }

    @Operation(summary = "Show 10 last added quotes")
    @ApiResponse(responseCode = "200", description = "The page successfully loaded")
    @GetMapping("/last")
    public ModelAndView getLastQuote(
            @Parameter(description = "JWT token as cookie")
            @CookieValue(name = "token", required = false) String token
    ) {
        String url = "urls/last.html";
        ModelUtils utils = new ModelUtils(token, url,
                jwtService, userDetailsService,
                quoteService, voteService);
        ModelAndView modelAndView = utils.getModelAndView();

        List<Quote> last10Quotes = quoteService.getLast10Quotes();
        modelAndView.addObject("quotes", last10Quotes);

        return modelAndView;
    }

}
