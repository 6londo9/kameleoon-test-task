package kameleoon.task.controller;

import jakarta.servlet.http.HttpServletRequest;
import kameleoon.task.config.JwtService;
import kameleoon.task.exception.QuoteNotFoundException;
import kameleoon.task.exception.UserNotFoundException;
import kameleoon.task.model.Quote;
import kameleoon.task.model.User;
import kameleoon.task.model.Vote;
import kameleoon.task.model.VoteType;
import kameleoon.task.service.QuoteService;
import kameleoon.task.service.UserService;
import kameleoon.task.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/v1.0/quotes")
public final class QuoteController {

    @Autowired
    private QuoteService quoteService;
    @Autowired
    private UserService userService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/new")
    public ModelAndView getQuoteCreation() {
        return new ModelAndView("urls/quote/new.html");
    }

    @PostMapping("/create")
    public RedirectView getQuoteCreation(HttpServletRequest request,
                                         @CookieValue(value = "token") String token) {
        String username = jwtService.exctractUsername(token);
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));

        String content = request.getParameter("content");
        Quote quote = new Quote(content, user);
        quoteService.createQuote(quote);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/v1.0/top");
        return redirectView;
    }

    @PostMapping("/{quoteId}/like")
    public ResponseEntity<Void> likeQuote(@PathVariable("quoteId") Long quoteId,
                                          @CookieValue(value = "token") String token) {
        String username = jwtService.exctractUsername(token);

        Quote quote = quoteService.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException("Quote with such id not found"));
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));

        Vote vote = voteService.getVote(user, quote);

        if (vote == null) {
            voteService.createVote(user, quote, VoteType.LIKE);
            quote.setLikeCount(quoteService.getLikesCount(quoteId));
            quote.setDislikeCount(quoteService.getDislikesCount(quoteId));

        } else if (vote.getVoteType().equals(VoteType.LIKE)) {
            return ResponseEntity.noContent().build();

        } else {
            voteService.updateVote(user, quote, VoteType.LIKE);
            quote.setLikeCount(quoteService.getLikesCount(quoteId));
            quote.setDislikeCount(quoteService.getDislikesCount(quoteId));
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{quoteId}/dislike")
    public ResponseEntity<Void> dislikeQuote(@PathVariable("quoteId") Long quoteId,
                                          @CookieValue(value = "token") String token) {
        String username = jwtService.exctractUsername(token);

        Quote quote = quoteService.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException("Quote with such id not found"));
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));

        Vote vote = voteService.getVote(user, quote);
        if (vote == null) {
            voteService.createVote(user, quote, VoteType.DISLIKE);
            quote.setLikeCount(quoteService.getLikesCount(quoteId));
            quote.setDislikeCount(quoteService.getDislikesCount(quoteId));

        } else if (vote.getVoteType().equals(VoteType.DISLIKE)) {
            return ResponseEntity.noContent().build();

        } else {
            voteService.updateVote(user, quote, VoteType.DISLIKE);
            quote.setLikeCount(quoteService.getLikesCount(quoteId));
            quote.setDislikeCount(quoteService.getDislikesCount(quoteId));
        }

        return ResponseEntity.noContent().build();
    }

}
