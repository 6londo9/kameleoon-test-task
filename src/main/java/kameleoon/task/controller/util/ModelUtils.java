package kameleoon.task.controller.util;

import kameleoon.task.config.JwtService;
import kameleoon.task.model.Quote;
import kameleoon.task.model.User;
import kameleoon.task.model.Vote;
import kameleoon.task.service.QuoteService;
import kameleoon.task.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@AllArgsConstructor
public final class ModelUtils {

    private final String token;
    private final String url;

    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private QuoteService quoteService;
    private VoteService voteService;

    public ModelAndView getModelAndView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        if (token != null && !jwtService.isTokenExpired(token)) {
            String username = jwtService.exctractUsername(token);
            User user = (User) userDetailsService.loadUserByUsername(username);
            List<Quote> lastVotes = voteService.findLast10UpdatedByUser(user.getId())
                    .stream()
                    .map(Vote::getQuote)
                    .toList();

            modelAndView.addObject("user", user);
            modelAndView.addObject("lastVotes", lastVotes);
        }

        Quote randomQuote = quoteService.findRandomQuote();
        if (randomQuote != null) {
            modelAndView.addObject("randomQuote", randomQuote);
        }

        return modelAndView;
    }
}
