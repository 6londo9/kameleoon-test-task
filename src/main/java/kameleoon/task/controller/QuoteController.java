package kameleoon.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "Get page of quote creating")
    @ApiResponse(responseCode = "200", description = "The page successfully loaded")
    @GetMapping("/new")
    public ModelAndView getQuoteCreation() {
        return new ModelAndView("urls/quote/new.html");
    }

    @Operation(summary = "Post quote")
    @ApiResponse(responseCode = "302", description = "The quote was successfully created")
    @PostMapping("/create")
    public RedirectView getQuoteCreation(
            HttpServletRequest request,
            @Parameter(description = "JWT token as cookie")
            @CookieValue(value = "token") String token
    ) {
        String username = jwtService.exctractUsername(token);
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));

        String content = request.getParameter("content");
        Quote quote = new Quote(content, user);
        quoteService.save(quote);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/v1.0/top");
        return redirectView;
    }

    @Operation(summary = "Like the chosen quote")
    @ApiResponse(responseCode = "204", description = "The like was successfully added")
    @PostMapping("/{quoteId}/like")
    public ResponseEntity<Void> likeQuote(
            @Parameter(description = "Id of quote to be disliked")
            @PathVariable("quoteId") Long quoteId,
            @Parameter(description = "JWT token as cookie")
            @CookieValue(value = "token") String token
    ) {
        String username = jwtService.exctractUsername(token);

        Quote quote = quoteService.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException("Quote with such id not found"));
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));

        Vote vote = voteService.getVote(user, quote);

        if (vote == null) {
            voteService.createVote(user, quote, VoteType.LIKE);

        } else if (vote.getVoteType().equals(VoteType.LIKE)) {
            return ResponseEntity.noContent().build();

        } else {
            voteService.updateVote(user, quote, VoteType.LIKE);
        }


        quote.setLikeCount(quoteService.getLikesCount(quoteId));
        quote.setDislikeCount(quoteService.getDislikesCount(quoteId));
        quoteService.save(quote);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Dislike the chosen quote")
    @ApiResponse(responseCode = "204", description = "The dislike was successfully added")
    @PostMapping("/{quoteId}/dislike")
    public ResponseEntity<Void> dislikeQuote(
            @Parameter(description = "Id of quote to be disliked")
            @PathVariable("quoteId") Long quoteId,
            @Parameter(description = "JWT token as cookie")
            @CookieValue(value = "token") String token
    ) {
        String username = jwtService.exctractUsername(token);

        Quote quote = quoteService.findById(quoteId)
                .orElseThrow(() -> new QuoteNotFoundException("Quote with such id not found"));
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not authorized"));

        Vote vote = voteService.getVote(user, quote);
        if (vote == null) {
            voteService.createVote(user, quote, VoteType.DISLIKE);

        } else if (vote.getVoteType().equals(VoteType.DISLIKE)) {
            return ResponseEntity.noContent().build();

        } else {
            voteService.updateVote(user, quote, VoteType.DISLIKE);
        }

        quote.setLikeCount(quoteService.getLikesCount(quoteId));
        quote.setDislikeCount(quoteService.getDislikesCount(quoteId));
        quoteService.save(quote);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update quote content")
    @ApiResponse(responseCode = "303", description = "The content successfully updated")
    @PatchMapping("/{id}")
    public Quote updateQuote(
            @Parameter(description = "Id of quote to be updated")
            @PathVariable("id") String id,
            @Parameter(description = "New quote data")
            @RequestBody Quote quote
    ) {
        Long quoteId = Long.parseLong(id);
        return quoteService.updateQuote(quoteId, quote);
    }

    @Operation(summary = "Delete quote")
    @ApiResponse(responseCode = "303", description = "The quote successfully deleted")
    @DeleteMapping("/{id}")
    public void deleteUser(
            @Parameter(description = "Id of quote to be deleted")
            @PathVariable("id") String id
    ) {
        Long quoteId = Long.parseLong(id);
        quoteService.deleteQuote(quoteId);
    }

}
