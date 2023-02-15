package kameleoon.task.controller;

import kameleoon.task.config.JwtService;
import kameleoon.task.controller.util.ModelUtils;
import kameleoon.task.model.Quote;
import kameleoon.task.service.QuoteService;
import kameleoon.task.service.UserService;
import kameleoon.task.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public ModelAndView homePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("urls/top.html");
        return modelAndView;
    }

    @GetMapping("/top")
    public ModelAndView topQuotes(@CookieValue(name = "token", required = false) String token) {
        String url = "urls/top.html";
        ModelUtils utils = new ModelUtils(token, url,
                jwtService, userDetailsService,
                quoteService, voteService);
        ModelAndView modelAndView = utils.getModelAndView();

        List<Quote> topQuotes = quoteService.findMostUpvotedQuotes();
        modelAndView.addObject("quotes", topQuotes);

        return modelAndView;
    }

    @GetMapping("/flop")
    public ModelAndView flopQuotes(@CookieValue(name = "token", required = false) String token) {
        String url = "urls/flop.html";
        ModelUtils utils = new ModelUtils(token, url,
                jwtService, userDetailsService,
                quoteService, voteService);
        ModelAndView modelAndView = utils.getModelAndView();

        List<Quote> flopQuotes = quoteService.findMostDownvotedQuotes();
        modelAndView.addObject("quotes", flopQuotes);

        return modelAndView;
    }

    @GetMapping("/last")
    public ModelAndView getLastQuote(@CookieValue(name = "token", required = false) String token) {
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
